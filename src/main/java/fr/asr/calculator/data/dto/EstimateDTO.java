package fr.asr.calculator.data.dto;

import fr.asr.calculator.data.dto.enumeration.*;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Validated
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EstimateDTO {

    @NotNull
    private Long id;

    @Valid
    @NotNull
    private Company company;

    @Valid
    @Nullable
    private List<Building> buildings;

    @Valid
    @Nullable
    private Map<Long, List<Room>> rooms; // Keyed by building ID

    @Nullable
    private Double hourlyRate;

    @Valid
    @NotNull
    private MultiPurposeData multiPurposeData;

    @Validated
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MultiPurposeData {
        @Valid
        @NotNull
        private List<MultiPurpose> multiPurposes;

        @Valid
        @NotNull
        private List<MultiPurposeRate> multiPurposeRates;
    }

    @Validated
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MultiPurpose {
        @NotNull
        private Long id;

        @NotBlank
        private String name;

        @NotBlank
        private String type;

        @NotNull
        @Min(0)
        private Double surface;

        @NotBlank
        private String serviceType;

        @NotNull
        private MultiPurposeFrequencyDTO frequency;

        @Nullable
        private Double travelCosts;

        @Nullable
        private String remarks;

        @Nullable
        private Integer customCadence;
    }

    @Validated
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MultiPurposeRate {
        @NotNull
        private MultiPurposeType type;

        @NotNull
        @Min(0)
        private Double hourlyRate;
    }

    @Validated
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Company {
        @NotBlank
        private String name;

        @NotNull
        private SectorDTO businessSector;
    }

    @Validated
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Building {
        @NotNull
        private Long id;

        @NotBlank
        private String name;

        @NotBlank
        private String buildingType;
    }

    @Validated
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Room {
        @NotNull
        private Long id;

        @NotBlank
        private String name;

        @NotBlank
        private String type;

        @NotNull
        private Double surface;

        @NotNull
        private FloorTypeDTO floorType;

        @NotNull
        private FrequencyDTO frequency;

        @Nullable
        private String remarks;

        @Valid
        @Nullable
        private List<KeyValue> additionalProperties;
    }

    @Validated
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class KeyValue {
        @NotBlank
        private String key;

        @NotNull
        private Object value;
    }
}
