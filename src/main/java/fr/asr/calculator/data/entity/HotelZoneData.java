package fr.asr.calculator.data.entity;

import static fr.asr.calculator.constant.ApplicationConstant.SCHEMA_NAME;

import fr.asr.calculator.data.entity.converter.HotelManagementPackageConverter;
import fr.asr.calculator.data.entity.converter.HotelRoomCountsConverter;
import fr.asr.calculator.data.entity.converter.HotelRoomTypesConverter;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(schema = SCHEMA_NAME)
public class HotelZoneData implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer classification;

    @Column(nullable = false)
    private Double globalRate;

    @Column
    private Double occupancyRate;

    @Column
    private Integer openingDaysPerWeek;

    /**
     * Here, data from the UI is stored without processing. Applying at 100% SQL philosophy is counterproductive
     * and adds a lot of complexity. Here, the choice to use the advantages of PostgreSQL with JSONB has been made.
     * And to be honest, I should have used this solution for the Estimate entity as well...
     */
    @ColumnTransformer(write = "?::jsonb")
    @Column(columnDefinition = "jsonb", nullable = false)
    @Convert(converter = HotelRoomTypesConverter.class)
    private List<HotelRoomType> roomTypes;

    @ColumnTransformer(write = "?::jsonb")
    @Column(columnDefinition = "jsonb")
    @Convert(converter = HotelRoomCountsConverter.class)
    private Map<String, HotelRoomCount> roomCounts;

    @ColumnTransformer(write = "?::jsonb")
    @Column(columnDefinition = "jsonb")
    @Convert(converter = HotelManagementPackageConverter.class)
    private HotelManagementPackage managementPackage;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HotelRoomType implements Serializable {
        private String id;
        private String name;
        private Double clearanceRate;
        private Double recoucheRate;
        private Boolean isCustom;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HotelRoomCount implements Serializable {
        private Integer count;
        private Boolean hasKitchen;
        private Integer kitchenTime;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HotelManagementPackage implements Serializable {
        private Boolean statutEncadrant;
        private Boolean premiereFemmeDeChambre;
        private Double hourlyRate;
        private Integer daysPerWeek;
        private Double controlPercentage;
        private Double laundryManagementHours;
        private Integer laundryDaysPerWeek;
    }
}
