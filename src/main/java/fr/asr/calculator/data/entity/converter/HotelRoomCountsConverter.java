package fr.asr.calculator.data.entity.converter;

import static fr.asr.calculator.exception.ExceptionType.HOTEL_ESTIMATE_ROOM_COUNTS_LOAD_ERROR;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.asr.calculator.data.entity.HotelZoneData;
import fr.asr.calculator.exception.functionnal.EstimateException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.io.IOException;
import java.util.Map;

@Converter
public class HotelRoomCountsConverter implements AttributeConverter<Map<String, HotelZoneData.HotelRoomCount>, String> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, HotelZoneData.HotelRoomCount> meta) {
        if (meta == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(meta);
        } catch (JsonProcessingException ex) {
            throw new EstimateException(HOTEL_ESTIMATE_ROOM_COUNTS_LOAD_ERROR, ex, "meta", "convertToDatabaseColumn");
        }
    }

    @Override
    public Map<String, HotelZoneData.HotelRoomCount> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return Map.of();
        }
        try {
            return OBJECT_MAPPER.readValue(
                    dbData,
                    OBJECT_MAPPER
                            .getTypeFactory()
                            .constructMapType(Map.class, String.class, HotelZoneData.HotelRoomCount.class));
        } catch (IOException ex) {
            throw new EstimateException(HOTEL_ESTIMATE_ROOM_COUNTS_LOAD_ERROR, ex, "meta", "convertToEntityAttribute");
        }
    }
}
