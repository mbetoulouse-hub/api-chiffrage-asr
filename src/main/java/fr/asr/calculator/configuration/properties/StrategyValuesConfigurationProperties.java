package fr.asr.calculator.configuration.properties;

import fr.asr.calculator.util.FrequencyUtil;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Component
@Validated
@ConfigurationProperties(prefix = "strategy-values")
public class StrategyValuesConfigurationProperties {

    @Valid
    @NotNull
    private SelectKitchenCleanType selectKitchenCleanType;

    @Valid
    @NotNull
    private SelectRestroomCleanType selectRestroomCleanType;

    @Valid
    @NotNull
    private SelectWeightsSpecific selectWeightsSpecific;

    @Valid
    @NotNull
    private SelectCardioSpecific selectCardioSpecific;

    @Valid
    @NotNull
    private CheckArmchair checkArmchair;

    @Valid
    @NotNull
    private CheckActivity checkActivity;

    @Valid
    @NotNull
    private CheckPartial checkPartial;

    @Valid
    @NotNull
    private CheckParking checkParking;

    @Valid
    @NotNull
    private Autolaveuse autolaveuse;

    @Valid
    @NotNull
    private CleaningXTimes cleaningXTimes;

    @Valid
    @NotNull
    private InputContainers inputContainers;

    @Getter
    @Setter
    public static class SelectKitchenCleanType {
        @Min(1)
        @NotNull
        private Integer floor;

        @Min(1)
        @NotNull
        private Integer complete;
    }

    @Getter
    @Setter
    public static class SelectRestroomCleanType {
        @Min(1)
        @NotNull
        private Integer aspiration;

        @Min(1)
        @NotNull
        private Integer carpet;

        @Min(1)
        @NotNull
        private Integer complete;
    }

    @Getter
    @Setter
    public static class SelectWeightsSpecific {
        @Valid
        @NotNull
        private Map<String, @Positive Integer> vacuum;

        @Valid
        @NotNull
        private Map<String, @Positive Integer> clean;

        @Valid
        @NotNull
        private Map<String, @Positive Integer> dust;
    }

    @Getter
    @Setter
    public static class SelectCardioSpecific {
        @Valid
        @NotNull
        private Map<String, @Positive Integer> floor;

        @Valid
        @NotNull
        private Map<String, @Positive Integer> dust;
    }

    @Getter
    @Setter
    public static class CheckArmchair {
        @Valid
        @NotNull
        private Map<String, @Positive Integer> withArmchair;

        @Valid
        @NotNull
        private Map<String, @Positive Integer> withoutArmchair;
    }

    @Getter
    @Setter
    public static class CheckActivity {
        @Min(1)
        @NotNull
        private Integer withDisinfection;

        @Min(1)
        @NotNull
        private Integer withoutDisinfection;
    }

    @Getter
    @Setter
    public static class CheckPartial {
        @Valid
        @NotNull
        private Map<String, @Positive Integer> partial;

        @Valid
        @NotNull
        private Map<String, @Positive Integer> full;
    }

    @Getter
    @Setter
    public static class CheckParking {
        @Min(1)
        @NotNull
        private Integer withPicking;

        @Min(1)
        @NotNull
        private Integer withoutPicking;
    }

    @Getter
    @Setter
    public static class Autolaveuse {
        @Min(1)
        @NotNull
        private Integer surfaceThreshold;

        @Min(1)
        @NotNull
        private Integer cadenceAboveThreshold;

        @Min(1)
        @NotNull
        private Integer cadenceBelowThreshold;
    }

    @Getter
    @Setter
    public static class CleaningXTimes {
        @Min(1)
        @NotNull
        private Integer times3;

        @Min(1)
        @NotNull
        private Integer times5;

        @Min(1)
        @NotNull
        private Integer times7;
    }

    @Getter
    @Setter
    public static class InputContainers {
        @Min(0)
        @NotNull
        private Integer lowThreshold;

        @Positive
        @NotNull
        private Double lowCadence;

        @Positive
        @NotNull
        private Double highCadenceFactor;
    }

    @PostConstruct
    public void postConstruct() {
        FrequencyUtil.expandRangeKeys(selectWeightsSpecific.getVacuum());
        FrequencyUtil.expandRangeKeys(selectWeightsSpecific.getClean());
        FrequencyUtil.expandRangeKeys(selectWeightsSpecific.getDust());
        FrequencyUtil.expandRangeKeys(selectCardioSpecific.getFloor());
        FrequencyUtil.expandRangeKeys(selectCardioSpecific.getDust());
        FrequencyUtil.expandRangeKeys(checkArmchair.getWithArmchair());
        FrequencyUtil.expandRangeKeys(checkArmchair.getWithoutArmchair());
    }
}
