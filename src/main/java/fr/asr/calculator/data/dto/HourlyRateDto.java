package fr.asr.calculator.data.dto;

import fr.asr.calculator.data.dto.enumeration.FrequencyDTO;
import java.math.BigDecimal;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HourlyRateDto {

    private Map<FrequencyDTO, Range> ranges;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Range {
        private BigDecimal min;
        private BigDecimal max;
    }
}
