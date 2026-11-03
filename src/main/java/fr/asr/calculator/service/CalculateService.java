package fr.asr.calculator.service;

import fr.asr.calculator.data.dto.request.SimulateDTO;
import fr.asr.calculator.data.dto.request.SubmitDTO;
import fr.asr.calculator.data.dto.response.CalculateDTO;

public interface CalculateService {

    byte[] export(String email, SubmitDTO submitDTO);

    CalculateDTO submit(String email, SubmitDTO submitDTO);

    CalculateDTO simulate(SimulateDTO simulateDTO);
}
