package fr.asr.calculator.data.dto.response;

import fr.asr.calculator.data.dto.BuildingTypeDto;
import fr.asr.calculator.data.dto.enumeration.SectorDTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SectorTypeDTO {

    @NotNull
    private SectorDTO sector;

    @NotNull
    private String title;

    @NotEmpty
    private List<BuildingTypeDto> buildingTypes;
}
