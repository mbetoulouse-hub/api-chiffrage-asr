package fr.asr.calculator.data.entity.converter;

import static fr.asr.calculator.exception.ExceptionType.HOTEL_ESTIMATE_ROOM_TYPES_LOAD_ERROR;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.asr.calculator.data.entity.HotelZoneData;
import fr.asr.calculator.exception.functionnal.EstimateException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.io.IOException;
import java.util.List;

@Converter
public class HotelRoomTypesConverter implements AttributeConverter<List<HotelZoneData.HotelRoomType>, String> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<HotelZoneData.HotelRoomType> meta) {
        if (meta == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(meta);
        } catch (JsonProcessingException ex) {
            throw new EstimateException(HOTEL_ESTIMATE_ROOM_TYPES_LOAD_ERROR, ex, "meta", "convertToDatabaseColumn");
        }
    }

    @Override
    public List<HotelZoneData.HotelRoomType> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return List.of();
        }
        try {
            return OBJECT_MAPPER.readValue(
                    dbData,
                    OBJECT_MAPPER
                            .getTypeFactory()
                            .constructCollectionType(List.class, HotelZoneData.HotelRoomType.class));
        } catch (IOException ex) {
            throw new EstimateException(HOTEL_ESTIMATE_ROOM_TYPES_LOAD_ERROR, ex, "meta", "convertToEntityAttribute");
        }
    }
}
