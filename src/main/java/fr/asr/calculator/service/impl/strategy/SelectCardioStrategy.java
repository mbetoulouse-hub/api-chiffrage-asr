package fr.asr.calculator.service.impl.strategy;

import static fr.asr.calculator.data.dto.enumeration.AdditionalProperties.*;
import static fr.asr.calculator.exception.ExceptionType.CADENCE_INVALID_CARDIO_SPECIFIC;

import fr.asr.calculator.configuration.properties.StrategyValuesConfigurationProperties;
import fr.asr.calculator.data.dto.BuildingDto;
import fr.asr.calculator.data.dto.DetermineCadenceDto;
import fr.asr.calculator.data.dto.RoomDto;
import fr.asr.calculator.data.dto.enumeration.AdditionalProperties;
import fr.asr.calculator.data.dto.enumeration.FrequencyDTO;
import fr.asr.calculator.exception.functionnal.CadenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class SelectCardioStrategy extends Strategy {

    private final StrategyValuesConfigurationProperties strategyValues;

    @Override
    public DetermineCadenceDto applyStrategy(RoomDto room, BuildingDto building, AdditionalProperties properties) {
        log.info("---- Using strategy SELECT_CARDIO_SPECIFIC for CARDIO_AREA");

        String response = getValue(room, building, SELECT_CARDIO_SPECIFIC);

        int cadence =
                switch (response) {
                    case "FLOOR" -> cadenceForFloor(room.getFrequency());
                    case "DUST" -> cadenceForDust(room.getFrequency());
                    default -> throw new CadenceException(
                            CADENCE_INVALID_CARDIO_SPECIFIC,
                            "property",
                            SELECT_WEIGHTS_SPECIFIC.name(),
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
        return SELECT_CARDIO_SPECIFIC;
    }

    private int cadenceForFloor(FrequencyDTO frequencyDTO) {
        var map = strategyValues.getSelectCardioSpecific().getFloor();
        return map.containsKey(frequencyDTO.name()) ? map.get(frequencyDTO.name()) : map.get("H1");
    }

    private int cadenceForDust(FrequencyDTO frequencyDTO) {
        var map = strategyValues.getSelectCardioSpecific().getDust();
        return map.containsKey(frequencyDTO.name()) ? map.get(frequencyDTO.name()) : map.get("H1");
    }
}
