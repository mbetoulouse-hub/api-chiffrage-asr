package fr.asr.calculator.service;

import fr.asr.calculator.data.dto.response.TechnicalCadenceDTO;
import fr.asr.calculator.data.dto.response.TechnicalCadenceRateDTO;
import java.util.List;

public interface TechnicalCadenceService {

    List<TechnicalCadenceDTO> getAllCadences();

    List<TechnicalCadenceRateDTO> getAllRates();
}
