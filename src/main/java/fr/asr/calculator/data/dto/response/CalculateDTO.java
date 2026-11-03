package fr.asr.calculator.data.dto.response;

import fr.asr.calculator.data.dto.enumeration.AlertDTO;
import fr.asr.calculator.data.dto.enumeration.FrequencyDTO;
import fr.asr.calculator.data.dto.enumeration.MultiPurposeFrequencyDTO;
import java.math.BigDecimal;
import java.util.List;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CalculateDTO {

    private FrequencyDTO biggestFrequency;
    private AggregateResult aggregateResult;
    private List<CalculateBuilding> buildings;
    private List<AlertDTO> alerts;
    private HotelCalculationResult hotelCalculationResult;
    private MultiPurposeCalculationResult multiPurposeCalculationResult;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CalculateBuilding {
        private String name;
        private String type;
        private AggregateResult aggregateResult;
        private List<CalculateDetail> details;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AggregateResult {
        private Integer surfaceSum;
        private Double timeSum;
        private Double timeSumRounded;
        private Double timeSumRoundedByService;
        private BigDecimal monthlyRateSum;
        private BigDecimal pricePerSquareMeter;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CalculateDetail {
        private String name;
        private String roomType;
        private String remarks;
        private Integer surface;
        private FrequencyDTO frequencyDTO;
        private Integer cadence;
        private Integer weekNumber;
        private Double timeInHoursByService;
        private Double timeInHoursByMonth;
        private Double timeInHours; // tps hebdomadaire
        private BigDecimal monthlyRate;
        private BigDecimal hourlyRateUsed;
    }

    // ZoneHotelData
    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HotelCalculationResult {
        private HotelMinimumPackage minimumPackage;
        private HotelManagementPackage managementPackage;
        private Double numberOfRooms;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HotelMinimumPackage {
        private Double minimumRoomsPerMonth;
        private Double unitPriceRoom;
        private String roomUsedId;
        private Double minimumPackagePrice;
        private Double numberOfRoomsPerDay;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HotelManagementPackage {
        private Double allocationTime;
        private Double controlTime;
        private Double laundryManagementTime;
        private Double trainingTime;
        private Double managementPackagePrice;
        private Double totalTime;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MultiPurposeCalculationResult {
        private Double totalTime;
        private Double totalPrice;
        private Double totalSurface;
        private List<MultiPurposeCalculationDetail> details;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MultiPurposeCalculationDetail {
        private String name;
        private String type;
        private String serviceType;
        private Double surface;
        private String remarks;
        private Integer cadence;
        private MultiPurposeFrequencyDTO frequency;
        private boolean usedTravelCosts;
        private Double travelCosts;

        private Double hourlyRateUsed;

        private Double timeInHours;
        private Double price;
        private Double priceByService;
    }
}
