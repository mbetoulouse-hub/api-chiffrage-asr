package fr.asr.calculator.data.dto.response;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ZoneHotelPrefillRoomTypeDTO {

    private Map<Integer, List<RoomType>> prefillRoomType;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoomType {
        private String id;
        private String name;
        private double clearanceRate;
        private double recoucheRate;
    }
}
