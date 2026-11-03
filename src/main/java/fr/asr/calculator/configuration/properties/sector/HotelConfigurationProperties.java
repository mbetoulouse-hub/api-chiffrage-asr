package fr.asr.calculator.configuration.properties.sector;

import fr.asr.calculator.util.FrequencyUtil;
import fr.asr.calculator.util.SectorConfigurationUtil;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "hotel")
@Validated
public class HotelConfigurationProperties extends AbstractSectorProperties {

    @NotNull
    protected ZoneHotel zoneHotel;

    @Getter
    @Setter
    public static class ZoneHotel {

        @NotEmpty
        private Map<Integer, @Valid Map<String, @Valid PrefillRoomType>> prefillRoomType;
    }

    @Getter
    @Setter
    public static class PrefillRoomType {

        @NotBlank
        private String name;

        @Positive
        private Integer clearanceRate;

        @Positive
        private Integer recoucheRate;
    }

    @PostConstruct
    public void postConstruct() {
        cadences.values().forEach(cadence -> FrequencyUtil.expandRangeKeys(cadence.getFrequencies()));
        SectorConfigurationUtil.verifyConfiguration(this);
    }
}
