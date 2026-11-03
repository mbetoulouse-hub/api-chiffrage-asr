package fr.asr.calculator.service;

import fr.asr.calculator.data.dto.request.SubmitDTO;
import fr.asr.calculator.data.dto.response.CalculateDTO;
import fr.asr.calculator.data.entity.Estimate;

public interface ExportService {

    byte[] process(Estimate estimate, SubmitDTO submitDTO, CalculateDTO calculateDTO);
}
