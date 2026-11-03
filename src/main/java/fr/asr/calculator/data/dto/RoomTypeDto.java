package fr.asr.calculator.data.dto;

import fr.asr.calculator.data.dto.enumeration.AdditionalProperties;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomTypeDto {

    private String key;
    private String title;
    private List<AdditionalProperties> additionalProperties;
}
