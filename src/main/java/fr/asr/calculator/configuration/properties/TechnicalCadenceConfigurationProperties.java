package fr.asr.calculator.configuration.properties;

import fr.asr.calculator.data.dto.enumeration.MultiPurposeType;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "technical-cadence")
@Validated
public class TechnicalCadenceConfigurationProperties {

    @NotEmpty
    private Map<String, @Valid TechnicalCadence> cadences;

    @NotEmpty
    private Map<MultiPurposeType, @Valid Range> rates;

    @Getter
    @Setter
    public static class TechnicalCadence {
        @NotEmpty
        private String title;

        @Valid
        private Map<String, @Valid TechnicalCadenceService> services;
    }

    @Getter
    @Setter
    public static class TechnicalCadenceService {

        @NotEmpty
        private String title;

        @Nullable
        @Valid
        private Range range;

        @NotNull
        private Integer cadence;

        @NotNull
        private MultiPurposeType type;
    }

    @Getter
    @Setter
    public static class Range {
        @NotNull
        private Double min;

        @NotNull
        private Double max;
    }
}
