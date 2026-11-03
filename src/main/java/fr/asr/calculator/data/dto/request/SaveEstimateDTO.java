package fr.asr.calculator.data.dto.request;

import fr.asr.calculator.data.dto.EstimateDTO;
import fr.asr.calculator.data.dto.HotelZoneDataDTO;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
public class SaveEstimateDTO {

    @Valid
    @NotNull
    private EstimateDTO estimate;

    @Valid
    @Nullable
    private HotelZoneDataDTO hotelZoneData;
}
