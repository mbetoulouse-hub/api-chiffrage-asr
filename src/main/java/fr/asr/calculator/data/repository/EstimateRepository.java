package fr.asr.calculator.data.repository;

import fr.asr.calculator.data.entity.Estimate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EstimateRepository extends JpaRepository<Estimate, Long>, JpaSpecificationExecutor<Estimate> {}
