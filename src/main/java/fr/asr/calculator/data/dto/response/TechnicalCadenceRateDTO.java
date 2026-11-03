package fr.asr.calculator.data.dto.response;

import fr.asr.calculator.configuration.properties.TechnicalCadenceConfigurationProperties;
import fr.asr.calculator.data.dto.enumeration.MultiPurposeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TechnicalCadenceRateDTO {

    private MultiPurposeType type;
    private TechnicalCadenceConfigurationProperties.Range range;
}
