package fr.asr.calculator.configuration.properties.sector;

import fr.asr.calculator.util.FrequencyUtil;
import fr.asr.calculator.util.SectorConfigurationUtil;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "medical")
@Validated
public class MedicalConfigurationProperties extends AbstractSectorProperties {

    @PostConstruct
    public void postConstruct() {
        cadences.values().forEach(cadence -> FrequencyUtil.expandRangeKeys(cadence.getFrequencies()));
        SectorConfigurationUtil.verifyConfiguration(this);
    }
}
