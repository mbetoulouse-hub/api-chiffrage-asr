package fr.asr.calculator.data.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
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
public class HotelZoneDataDTO {

    @Min(1)
    private Integer classification;

    @Positive
    private Double globalRate;

    @Valid
    @Nullable
    private List<HotelRoomType> roomTypes;

    @Valid
    @Nullable
    private Map<String, HotelRoomCount> roomCounts;

    @Positive
    @Nullable
    private Double occupancyRate;

    @Min(1)
    @Nullable
    private Integer openingDaysPerWeek;

    @Valid
    @Nullable
    private HotelManagementPackage managementPackage;

    @Validated
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HotelRoomType {
        @NotBlank
        private String id;

        @NotBlank
        private String name;

        @Min(0)
        private Double clearanceRate;

        @Min(0)
        private Double recoucheRate;

        @NotNull
        private Boolean isCustom;
    }

    @Validated
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HotelRoomCount {
        @Min(0)
        private Integer count;

        @NotNull
        private Boolean hasKitchen;

        @Nullable
        private Integer kitchenTime;
    }

    @Validated
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HotelManagementPackage {
        @NotNull
        private Boolean statutEncadrant;

        @NotNull
        private Boolean premiereFemmeDeChambre;

        @Min(0)
        private Double hourlyRate;

        @Min(0)
        private Integer daysPerWeek;

        @Min(0)
        private Double controlPercentage;

        @Nullable
        private Double laundryManagementHours;

        @Nullable
        private Integer laundryDaysPerWeek;
    }
}
