package fr.asr.calculator.data.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
public class BuildingDto {

    @NotBlank
    private String name;

    @NotBlank
    private String buildingType;

    @Valid
    @NotEmpty
    private List<RoomDto> rooms;
}
