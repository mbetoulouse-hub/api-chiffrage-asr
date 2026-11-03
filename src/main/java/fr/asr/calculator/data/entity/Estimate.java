package fr.asr.calculator.data.entity;

import static fr.asr.calculator.constant.ApplicationConstant.SCHEMA_NAME;

import fr.asr.calculator.data.dto.enumeration.MultiPurposeFrequencyDTO;
import fr.asr.calculator.data.dto.enumeration.MultiPurposeType;
import fr.asr.calculator.data.dto.enumeration.SectorDTO;
import fr.asr.calculator.data.entity.converter.MultiPurposeDataConverter;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
public class Estimate extends Auditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String companyName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SectorDTO companySector;

    @Column
    private Double hourlyRate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private Integer buildingCount;

    @Column(nullable = false)
    private Integer roomCount;

    @ColumnTransformer(write = "?::jsonb")
    @Column(columnDefinition = "jsonb", nullable = false)
    @Convert(converter = MultiPurposeDataConverter.class)
    private MultiPurposeData multiPurposeData;

    @ManyToOne(optional = false)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "hotel_zone_data_id")
    private HotelZoneData hotelZoneData;

    @OneToMany(mappedBy = "estimate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Building> buildings = new ArrayList<>();

    public enum Status {
        DRAFT,
        COMPLETE;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MultiPurposeData implements Serializable {
        private List<MultiPurpose> multiPurposes;
        private List<MultiPurposeRate> multiPurposeRates;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MultiPurpose implements Serializable {
        private Integer id;
        private String name;
        private String type;
        private Double surface;
        private String serviceType;
        private MultiPurposeFrequencyDTO frequency;
        private Double travelCosts;
        private String remarks;
        private Integer customCadence;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MultiPurposeRate implements Serializable {
        private MultiPurposeType type;
        private Double hourlyRate;
    }
}
