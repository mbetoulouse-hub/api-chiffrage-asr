package fr.asr.calculator.data.dao.refreshtoken;

import fr.asr.calculator.data.dao.Dao;
import fr.asr.calculator.data.entity.RefreshToken;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface RefreshTokenDao extends Dao<RefreshToken, Integer> {

    @NotNull
    List<RefreshToken> findExpiredRefreshToken(@NotNull Instant now);

    @NotNull
    Optional<RefreshToken> findByTokenOptional(@NotNull String token);

    @NotNull
    RefreshToken findByToken(@NotNull String token);

    void deleteAll(@NotNull List<RefreshToken> entity);
}
