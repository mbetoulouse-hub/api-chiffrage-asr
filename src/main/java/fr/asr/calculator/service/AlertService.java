package fr.asr.calculator.service;

import fr.asr.calculator.data.dto.enumeration.AlertDTO;
import fr.asr.calculator.data.dto.enumeration.FrequencyDTO;
import fr.asr.calculator.data.dto.response.CalculateDTO;
import java.util.List;

public interface AlertService {

    List<AlertDTO> extractAlerts(FrequencyDTO frequency, CalculateDTO calculateDTO);
}
