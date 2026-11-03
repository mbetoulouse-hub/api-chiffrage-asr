package fr.asr.calculator.configuration.properties;

import fr.asr.calculator.util.FrequencyUtil;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "global")
@Validated
public class GlobalConfigurationProperties {

    @NotEmpty
    private Map<String, @Valid Rate> rates;

    @Getter
    @Setter
    public static class Rate {
        @DecimalMin("0.0")
        private BigDecimal min;

        @DecimalMin("0.0")
        private BigDecimal max;
    }

    @PostConstruct
    public void postConstruct() {
        FrequencyUtil.expandRangeKeys(rates);
    }
}
