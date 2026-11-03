package fr.asr.calculator.data.dao.estimate;

import fr.asr.calculator.data.dao.Dao;
import fr.asr.calculator.data.entity.Estimate;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EstimateDao extends Dao<Estimate, Long> {

    @NotNull
    Page<Estimate> findAll(String input, String status, long profileId, @NotNull Pageable pageable);

    @NotNull
    Page<Estimate> findAll(String input, String status, @NotNull Pageable pageable);
}
