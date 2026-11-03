package fr.asr.calculator.data.repository;

import fr.asr.calculator.data.entity.RefreshToken;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Integer> {

    List<RefreshToken> findByExpiryDateBeforeOrRevokedTrue(@NotNull Instant now);

    @NotNull
    Optional<RefreshToken> findByToken(@NotNull String email);
}
