package fr.asr.calculator.service.auth;

import fr.asr.calculator.data.dto.internal.TokenDTO;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final JwtEncoder encoder;

    @Override
    public TokenDTO generateAccessToken(String name, Collection<? extends GrantedAuthority> authorities) {
        Instant now = Instant.now();
        List<String> scope =
                authorities.stream().map(GrantedAuthority::getAuthority).toList();

        Instant expireAt = now.plus(1, ChronoUnit.HOURS);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self-asr-calculator")
                .issuedAt(now)
                .expiresAt(expireAt)
                .subject(name)
                .claim("scope", scope)
                .build();

        return TokenDTO.builder()
                .token(this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue())
                .expiresAt(expireAt.toEpochMilli())
                .build();
    }

    @Override
    public TokenDTO generateRefreshToken(String name) {
        Instant now = Instant.now();

        Instant expireAt = now.plus(30, ChronoUnit.DAYS);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self-asr-calculator")
                .issuedAt(now)
                .expiresAt(expireAt)
                .subject(name)
                .build();

        return TokenDTO.builder()
                .token(this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue())
                .expiresAt(expireAt.toEpochMilli())
                .build();
    }
}
