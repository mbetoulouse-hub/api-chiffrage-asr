package fr.asr.calculator.data.repository;

import fr.asr.calculator.data.entity.Profile;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProfileRepository extends JpaRepository<Profile, Integer>, JpaSpecificationExecutor<Profile> {

    List<Profile> findByRole(Profile.Role role);

    @NotNull
    Optional<Profile> findByEmail(@NotNull String email);

    boolean existsByEmail(String email);
}
