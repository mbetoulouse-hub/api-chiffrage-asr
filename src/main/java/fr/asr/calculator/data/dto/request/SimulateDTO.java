package fr.asr.calculator.data.dto.request;

import fr.asr.calculator.data.dto.BuildingDto;
import fr.asr.calculator.data.dto.EstimateDTO;
import fr.asr.calculator.data.dto.HotelZoneDataDTO;
import fr.asr.calculator.data.dto.enumeration.SectorDTO;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
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
public class SimulateDTO {

    @NotNull
    private SectorDTO sector;

    @Valid
    @NotNull
    private List<BuildingDto> buildings;

    @NotNull
    private BigDecimal hourlyRate;

    @Valid
    @NotNull
    private EstimateDTO.MultiPurposeData multiPurposeData;

    @Valid
    @Nullable
    private HotelZoneDataDTO hotelZoneData;
}
