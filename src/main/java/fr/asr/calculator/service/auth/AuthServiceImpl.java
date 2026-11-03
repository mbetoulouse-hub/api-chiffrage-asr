package fr.asr.calculator.service.auth;

import static fr.asr.calculator.exception.ExceptionType.*;

import fr.asr.calculator.data.dao.profile.ProfileDao;
import fr.asr.calculator.data.dao.refreshtoken.RefreshTokenDao;
import fr.asr.calculator.data.dto.internal.AuthenticateDTO;
import fr.asr.calculator.data.dto.internal.TokenDTO;
import fr.asr.calculator.data.entity.Profile;
import fr.asr.calculator.data.entity.RefreshToken;
import fr.asr.calculator.exception.functionnal.AuthException;
import fr.asr.calculator.mapper.RefreshTokenMapper;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final ProfileDao profileDao;
    private final RefreshTokenDao refreshTokenDao;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final JwtDecoder jwtDecoder;

    @Transactional
    @Override
    public AuthenticateDTO authenticate(String email, String password) {
        log.info("Authenticating user with email: {}", email);

        try {
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

            log.info("User authenticated successfully");

            return generateTokens(authentication.getName(), authentication.getAuthorities());
        } catch (BadCredentialsException e) {
            throw new AuthException(AUTH_INVALID_CREDENTIALS, e);
        }
    }

    @Override
    public AuthenticateDTO refreshToken(String token) {
        RefreshToken refreshToken = refreshTokenDao.findByToken(token);

        if (Boolean.TRUE.equals(refreshToken.getRevoked())) {
            throw new AuthException(AUTH_INVALID_REFRESH_TOKEN);
        }

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            throw new AuthException(AUTH_EXPIRED_REFRESH_TOKEN);
        }

        Profile profile = refreshToken.getProfile();
        Jwt jwt = jwtDecoder.decode(token);
        if (!profile.getEmail().equals(jwt.getSubject())) {
            throw new AuthException(AUTH_INVALID_REFRESH_TOKEN);
        }

        refreshToken.setRevoked(Boolean.TRUE);

        return generateTokens(
                profile.getEmail(), List.of(new SimpleGrantedAuthority("ROLE_%s".formatted(profile.getRole()))));
    }

    @Override
    public void logout(String token) {
        RefreshToken refreshToken = refreshTokenDao.findByToken(token);
        refreshToken.setRevoked(Boolean.TRUE);
        refreshTokenDao.save(refreshToken);
    }

    private AuthenticateDTO generateTokens(String name, Collection<? extends GrantedAuthority> authorities) {
        TokenDTO accessToken = tokenService.generateAccessToken(name, authorities);
        TokenDTO generateRefreshToken = tokenService.generateRefreshToken(name);

        Profile profile = profileDao.findByEmail(name);
        RefreshToken refreshToken = RefreshTokenMapper.INSTANCE.toEntity(
                generateRefreshToken.getToken(), Instant.ofEpochMilli(generateRefreshToken.getExpiresAt()), profile);

        refreshTokenDao.save(refreshToken);

        log.info("Access & refresh token generated.");

        return AuthenticateDTO.builder()
                .accessToken(accessToken)
                .refreshToken(generateRefreshToken)
                .build();
    }
}
