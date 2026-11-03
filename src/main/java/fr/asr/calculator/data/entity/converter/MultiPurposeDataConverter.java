package fr.asr.calculator.data.entity.converter;

import static fr.asr.calculator.exception.ExceptionType.HOTEL_ESTIMATE_MANAGEMENT_PACKAGE_LOAD_ERROR;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.asr.calculator.data.entity.Estimate;
import fr.asr.calculator.exception.functionnal.EstimateException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.io.IOException;

@Converter
public class MultiPurposeDataConverter implements AttributeConverter<Estimate.MultiPurposeData, String> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Estimate.MultiPurposeData meta) {
        if (meta == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(meta);
        } catch (JsonProcessingException ex) {
            throw new EstimateException(
                    HOTEL_ESTIMATE_MANAGEMENT_PACKAGE_LOAD_ERROR, ex, "meta", "convertToDatabaseColumn");
        }
    }

    @Override
    public Estimate.MultiPurposeData convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(dbData, Estimate.MultiPurposeData.class);
        } catch (IOException ex) {
            throw new EstimateException(
                    HOTEL_ESTIMATE_MANAGEMENT_PACKAGE_LOAD_ERROR, ex, "meta", "convertToEntityAttribute");
        }
    }
}
