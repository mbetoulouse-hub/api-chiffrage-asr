package fr.asr.calculator.service;

import fr.asr.calculator.data.dto.EstimateDTO;
import fr.asr.calculator.data.dto.request.CreateEstimateDTO;
import fr.asr.calculator.data.dto.request.SaveEstimateDTO;
import fr.asr.calculator.data.dto.response.FindEstimateDTO;
import fr.asr.calculator.data.dto.response.PagedEstimateDTO;
import fr.asr.calculator.data.dto.response.SavedEstimateDTO;
import fr.asr.calculator.data.entity.Estimate;
import fr.asr.calculator.data.entity.Profile;
import org.springframework.data.domain.Page;

public interface EstimateService {

    Page<PagedEstimateDTO> getAllEstimates(int page, String email, String input, String status);

    EstimateDTO createNewEstimate(String email, CreateEstimateDTO createEstimateDTO);

    SavedEstimateDTO updateEstimate(String currentEmail, SaveEstimateDTO saveEstimateDTO);

    FindEstimateDTO getEstimate(String email, long id);

    void deleteEstimate(String email, long id);

    boolean profileCanInteractWithEstimate(Profile profile, Estimate estimate);
}
