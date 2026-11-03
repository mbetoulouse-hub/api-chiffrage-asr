package fr.asr.calculator.controller;

import static fr.asr.calculator.exception.ExceptionType.AUTH_INVALID_REFRESH_TOKEN;

import fr.asr.calculator.data.dto.internal.AuthenticateDTO;
import fr.asr.calculator.data.dto.internal.TokenDTO;
import fr.asr.calculator.data.dto.model.ProfileDTO;
import fr.asr.calculator.data.dto.response.LoginDTO;
import fr.asr.calculator.exception.functionnal.AuthException;
import fr.asr.calculator.service.ProfileService;
import fr.asr.calculator.service.auth.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final String COOKIE_NAME = "refresh_token";

    private final AuthService authService;
    private final ProfileService profileService;
    private final Environment environment;

    @PostMapping("/login")
    public TokenDTO login(@Valid @RequestBody LoginDTO loginDTO, HttpServletResponse response) {
        AuthenticateDTO authenticateDto = authService.authenticate(loginDTO.getEmail(), loginDTO.getPassword());
        setCookie(authenticateDto.getRefreshToken(), response);
        return authenticateDto.getAccessToken();
    }

    @PostMapping("/refresh")
    public TokenDTO refresh(HttpServletRequest request, HttpServletResponse response) {
        Optional<String> refreshTokenOptional = request.getCookies() == null
                ? Optional.empty()
                : Arrays.stream(request.getCookies())
                        .filter(cookie -> COOKIE_NAME.equals(cookie.getName()))
                        .map(Cookie::getValue)
                        .findFirst();

        if (refreshTokenOptional.isEmpty()) {
            throw new AuthException(AUTH_INVALID_REFRESH_TOKEN);
        }

        AuthenticateDTO authenticateDto = authService.refreshToken(refreshTokenOptional.get());
        setCookie(authenticateDto.getRefreshToken(), response);
        return authenticateDto.getAccessToken();
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Optional<String> refreshTokenOptional = request.getCookies() == null
                ? Optional.empty()
                : Arrays.stream(request.getCookies())
                        .filter(cookie -> COOKIE_NAME.equals(cookie.getName()))
                        .map(Cookie::getValue)
                        .findFirst();

        if (refreshTokenOptional.isEmpty()) {
            throw new AuthException(AUTH_INVALID_REFRESH_TOKEN);
        }

        authService.logout(refreshTokenOptional.get());

        Cookie refreshTokenCookie = new Cookie(COOKIE_NAME, null);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);
        response.addCookie(refreshTokenCookie);
    }

    @GetMapping("/me")
    public ProfileDTO test(Authentication authentication) {
        return profileService.getProfile(authentication.getName());
    }

    private void setCookie(TokenDTO refreshToken, HttpServletResponse response) {
        int validitySeconds = (int) ((refreshToken.getExpiresAt() / 1000) - (System.currentTimeMillis() / 1000));

        Cookie refreshTokenCookie = new Cookie(COOKIE_NAME, refreshToken.getToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(!environment.matchesProfiles("local"));
        refreshTokenCookie.setMaxAge(validitySeconds);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setAttribute("SameSite", "Lax");
        response.addCookie(refreshTokenCookie);
    }
}
