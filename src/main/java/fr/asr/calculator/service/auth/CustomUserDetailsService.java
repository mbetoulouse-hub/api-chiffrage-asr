package fr.asr.calculator.service.auth;

import fr.asr.calculator.data.dao.profile.ProfileDao;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final ProfileDao profileDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return profileDao
                .findByEmailOptional(username)
                .map(custom -> new User(
                        custom.getEmail(),
                        custom.getPassword(),
                        custom.getEnabled(),
                        true,
                        true,
                        true,
                        List.of(new SimpleGrantedAuthority("ROLE_%s".formatted(custom.getRole())))))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
