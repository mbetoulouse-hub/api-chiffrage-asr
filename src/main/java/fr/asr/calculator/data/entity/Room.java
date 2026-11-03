package fr.asr.calculator.data.entity;

import static fr.asr.calculator.constant.ApplicationConstant.SCHEMA_NAME;

import fr.asr.calculator.data.dto.enumeration.FloorTypeDTO;
import fr.asr.calculator.data.dto.enumeration.FrequencyDTO;
import fr.asr.calculator.data.entity.converter.AdditionalPropertiesConverter;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(schema = SCHEMA_NAME)
public class Room extends Auditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private Double surface;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FloorTypeDTO floorType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FrequencyDTO frequency;

    @Column
    private String remarks;

    @ColumnTransformer(write = "?::jsonb")
    @Column(columnDefinition = "jsonb")
    @Convert(converter = AdditionalPropertiesConverter.class)
    private List<AdditionalPropertiesValue> additionalProperties;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class AdditionalPropertiesValue implements Serializable {
        private String key;
        private Object value;
    }
}
