package fr.asr.calculator.data.dto.response;

import fr.asr.calculator.data.dto.EstimateDTO;
import fr.asr.calculator.data.dto.HotelZoneDataDTO;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SavedEstimateDTO {

    private EstimateDTO estimate;
    private HotelZoneDataDTO hotelZoneData;
}
