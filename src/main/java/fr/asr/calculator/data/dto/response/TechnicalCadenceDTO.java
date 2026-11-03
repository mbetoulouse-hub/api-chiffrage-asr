package fr.asr.calculator.data.dto.response;

import fr.asr.calculator.configuration.properties.TechnicalCadenceConfigurationProperties;
import fr.asr.calculator.data.dto.enumeration.MultiPurposeType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TechnicalCadenceDTO {

    private String key;
    private String title;
    private List<TechnicalCadenceServiceDTO> services;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TechnicalCadenceServiceDTO {
        private String key;
        private String title;
        private Integer cadence;
        private MultiPurposeType type;
        private TechnicalCadenceConfigurationProperties.Range range;
    }
}
