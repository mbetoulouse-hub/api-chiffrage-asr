package fr.asr.calculator.configuration.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

public class RoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        if (!source.getClaims().containsKey("scope")) {
            return new ArrayList<>();
        }
        return ((List<String>) source.getClaims().get("scope"))
                .stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
