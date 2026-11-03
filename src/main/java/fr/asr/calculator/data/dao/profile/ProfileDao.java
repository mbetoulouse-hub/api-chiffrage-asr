package fr.asr.calculator.data.dao.profile;

import fr.asr.calculator.data.dao.Dao;
import fr.asr.calculator.data.entity.Profile;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProfileDao extends Dao<Profile, Integer> {

    @NotNull
    List<Profile> finAllManager();

    @NotNull
    Page<Profile> findAll(String input, String role, @NotNull Pageable pageable);

    @NotNull
    Optional<Profile> findByEmailOptional(@NotNull String email);

    @NotNull
    Profile findByEmail(@NotNull String email);

    boolean existsByEmail(@NotNull String email);
}
