package fr.asr.calculator.service.impl.strategy;

import static fr.asr.calculator.data.dto.enumeration.AdditionalProperties.SELECT_RESTROOM_CLEAN_TYPE;
import static fr.asr.calculator.exception.ExceptionType.CADENCE_INVALID_RESTROOM_CLEAN_TYPE;

import fr.asr.calculator.configuration.properties.StrategyValuesConfigurationProperties;
import fr.asr.calculator.data.dto.BuildingDto;
import fr.asr.calculator.data.dto.DetermineCadenceDto;
import fr.asr.calculator.data.dto.RoomDto;
import fr.asr.calculator.data.dto.enumeration.AdditionalProperties;
import fr.asr.calculator.exception.functionnal.CadenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class SelectRestroomCleanTypeStrategy extends Strategy {

    private final StrategyValuesConfigurationProperties strategyValues;

    @Override
    public DetermineCadenceDto applyStrategy(RoomDto room, BuildingDto building, AdditionalProperties properties) {
        log.info("---- Using strategy SELECT_RESTROOM_CLEAN_TYPE for RESTROOM");

        String response = getValue(room, building, SELECT_RESTROOM_CLEAN_TYPE);

        int cadence =
                switch (response) {
                    case "ASPIRATION" -> strategyValues
                            .getSelectRestroomCleanType()
                            .getAspiration();
                    case "CARPET" -> strategyValues.getSelectRestroomCleanType().getCarpet();
                    case "COMPLETE" -> strategyValues
                            .getSelectRestroomCleanType()
                            .getComplete();
                    default -> throw new CadenceException(
                            CADENCE_INVALID_RESTROOM_CLEAN_TYPE,
                            "property",
                            SELECT_RESTROOM_CLEAN_TYPE.name(),
                            "roomType",
                            room.getType(),
                            "value",
                            response,
                            "roomName",
                            room.getName(),
                            "buildingName",
                            building.getName());
                };

        return DetermineCadenceDto.builder().cadence(cadence).build();
    }

    @Override
    public AdditionalProperties getSupportedProperty() {
        return SELECT_RESTROOM_CLEAN_TYPE;
    }
}
