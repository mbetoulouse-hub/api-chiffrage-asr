package fr.asr.calculator.configuration.properties.sector;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractSectorProperties {

    @NotBlank
    private String title;

    @NotEmpty
    protected Map<String, @Valid ConfigurationBuildingType> buildingsType;

    @NotEmpty
    protected Map<String, @Valid ConfigurationRoomType> roomsType;

    @NotEmpty
    protected Map<String, @Valid ConfigurationCadence> cadences;

    @Getter
    @Setter
    public static class ConfigurationBuildingType {
        @NotBlank
        protected String title;

        @NotEmpty
        protected List<String> roomsType;
    }

    @Getter
    @Setter
    public static class ConfigurationRoomType {
        @NotBlank
        protected String title;

        @NotBlank
        protected String cadence;

        private List<String> additionalProperties;
    }

    @Getter
    @Setter
    public static class ConfigurationCadence {
        @NotBlank
        protected String title;

        @NotEmpty
        protected Map<String, @Valid ConfigurationFrequency> frequencies;
    }

    @Getter
    @Setter
    public static class ConfigurationFrequency {
        @Min(1)
        protected Integer washableFloor;

        @Min(1)
        protected Integer carpetedFloor;

        @Positive
        protected Double minTimeInHours;

        @Positive
        protected Double fixedTimeInHours;
    }
}
