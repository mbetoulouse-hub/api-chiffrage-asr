package fr.asr.calculator.service.auth;

import fr.asr.calculator.data.dto.internal.TokenDTO;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

public interface TokenService {

    TokenDTO generateAccessToken(String name, Collection<? extends GrantedAuthority> authorities);

    TokenDTO generateRefreshToken(String name);
}
