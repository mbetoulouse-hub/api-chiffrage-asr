package fr.asr.calculator.data.dto.request;

import fr.asr.calculator.data.dto.enumeration.SectorDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateEstimateDTO {

    @NotBlank
    private String name;

    @NotNull
    private SectorDTO businessSector;
}
