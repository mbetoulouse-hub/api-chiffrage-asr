package fr.asr.calculator.data.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BuildingTypeDto {

    @NotBlank
    private String key;

    @NotBlank
    private String title;

    @NotEmpty
    private List<RoomTypeDto> rooms;
}
