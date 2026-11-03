package fr.asr.calculator.service.impl;

import static fr.asr.calculator.data.dto.enumeration.AlertDTO.H1_MINIMUM_PRICE_NOT_REACHED;
import static fr.asr.calculator.data.dto.enumeration.AlertDTO.RATIO_FREQUENCY_NOT_VALID;
import static fr.asr.calculator.data.dto.enumeration.FrequencyDTO.H1;

import fr.asr.calculator.data.dto.enumeration.AlertDTO;
import fr.asr.calculator.data.dto.enumeration.FrequencyDTO;
import fr.asr.calculator.data.dto.response.CalculateDTO;
import fr.asr.calculator.service.AlertService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AlertServiceImpl implements AlertService {

    @Override
    public List<AlertDTO> extractAlerts(FrequencyDTO frequency, CalculateDTO calculateDTO) {
        List<AlertDTO> alerts = new ArrayList<>();
        if (calculateDTO.getBuildings().isEmpty()) {
            return alerts;
        }

        if (H1.equals(frequency)
                && calculateDTO.getAggregateResult().getMonthlyRateSum().compareTo(BigDecimal.valueOf(249)) < 0) {
            alerts.add(H1_MINIMUM_PRICE_NOT_REACHED);
        }

        double ratio = calculateDTO.getAggregateResult().getTimeSumRounded() / frequency.getValue();
        if (ratio < 1.5) {
            alerts.add(RATIO_FREQUENCY_NOT_VALID);
        }

        return alerts;
    }
}
