package fr.asr.calculator.data.dto;

import static fr.asr.calculator.exception.ExceptionType.CALCULATE_FAILED_TO_CALCULATE_H7;

import fr.asr.calculator.data.dto.enumeration.FloorTypeDTO;
import fr.asr.calculator.data.dto.enumeration.FrequencyDTO;
import fr.asr.calculator.exception.functionnal.CalculateException;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
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
public class RoomDto implements Cloneable {

    @NotBlank
    private String name;

    @NotBlank
    private String type;

    @Min(1)
    private Integer surface;

    @NotNull
    private FloorTypeDTO floorType;

    @NotNull
    private FrequencyDTO frequency;

    @Nullable
    private String remarks;

    @Valid
    @Nullable
    private List<AdditionalProperties> additionalProperties;

    @Override
    public RoomDto clone() {
        try {
            RoomDto clone = (RoomDto) super.clone();
            clone.additionalProperties = additionalProperties != null
                    ? additionalProperties.stream()
                            .map(prop -> new AdditionalProperties(prop.getKey(), prop.getValue()))
                            .toList()
                    : null;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new CalculateException(CALCULATE_FAILED_TO_CALCULATE_H7, "roomType", type, "roomName", name);
        }
    }

    @Validated
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AdditionalProperties {

        @NotBlank
        private String key;

        @NotBlank
        private String value;
    }
}
