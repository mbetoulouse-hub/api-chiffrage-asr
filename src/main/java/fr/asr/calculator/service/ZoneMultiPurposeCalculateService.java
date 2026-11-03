package fr.asr.calculator.service;

import fr.asr.calculator.data.dto.EstimateDTO;
import fr.asr.calculator.data.dto.response.CalculateDTO;

public interface ZoneMultiPurposeCalculateService {

    CalculateDTO.MultiPurposeCalculationResult calculate(EstimateDTO.MultiPurposeData multiPurposeData);
}
