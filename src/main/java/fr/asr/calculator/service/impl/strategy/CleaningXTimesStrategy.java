package fr.asr.calculator.service.impl.strategy;

import static fr.asr.calculator.data.dto.enumeration.AdditionalProperties.CLEANING_X_TIMES;
import static fr.asr.calculator.exception.ExceptionType.CADENCE_INVALID_X_TIMES;

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
public class CleaningXTimesStrategy extends Strategy {

    private final StrategyValuesConfigurationProperties strategyValues;

    @Override
    public DetermineCadenceDto applyStrategy(RoomDto room, BuildingDto building, AdditionalProperties properties) {
        log.info("---- Using strategy CLEANING_X_TIMES for room type: {}", room.getType());
        int cleaningTimes = formatToInt(getValue(room, building, CLEANING_X_TIMES), room, building);

        int cadence =
                switch (cleaningTimes) {
                    case 3 -> strategyValues.getCleaningXTimes().getTimes3();
                    case 5 -> strategyValues.getCleaningXTimes().getTimes5();
                    case 7 -> strategyValues.getCleaningXTimes().getTimes7();
                    default -> throw new CadenceException(
                            CADENCE_INVALID_X_TIMES,
                            "property",
                            CLEANING_X_TIMES.name(),
                            "roomType",
                            room.getType(),
                            "value",
                            cleaningTimes,
                            "roomName",
                            room.getName(),
                            "buildingName",
                            building.getName());
                };

        log.info("---- Use cleaning times cadence : {}", cadence);

        return DetermineCadenceDto.builder().cadence(cadence).build();
    }

    @Override
    public AdditionalProperties getSupportedProperty() {
        return CLEANING_X_TIMES;
    }
}
