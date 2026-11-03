package fr.asr.calculator.service.impl.strategy;

import static fr.asr.calculator.data.dto.enumeration.AdditionalProperties.*;
import static fr.asr.calculator.exception.ExceptionType.CADENCE_INVALID_WEIGHTS_SPECIFIC;

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
public class SelectWeigthsStrategy extends Strategy {

    private final StrategyValuesConfigurationProperties strategyValues;

    @Override
    public DetermineCadenceDto applyStrategy(RoomDto room, BuildingDto building, AdditionalProperties properties) {
        log.info("---- Using strategy SELECT_WEIGHTS_SPECIFIC for WEIGHTS_ROOM");

        String response = getValue(room, building, SELECT_WEIGHTS_SPECIFIC);

        int cadence =
                switch (response) {
                    case "VACUUM" -> cadenceForVacuum(room.getFrequency());
                    case "CLEAN" -> cadenceForClean(room.getFrequency());
                    case "DUST" -> cadenceForDust(room.getFrequency());
                    default -> throw new CadenceException(
                            CADENCE_INVALID_WEIGHTS_SPECIFIC,
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
        return SELECT_WEIGHTS_SPECIFIC;
    }

    private int cadenceForVacuum(FrequencyDTO frequencyDTO) {
        var map = strategyValues.getSelectWeightsSpecific().getVacuum();
        return map.containsKey(frequencyDTO.name()) ? map.get(frequencyDTO.name()) : map.get("H1");
    }

    private int cadenceForClean(FrequencyDTO frequencyDTO) {
        var map = strategyValues.getSelectWeightsSpecific().getClean();
        return map.containsKey(frequencyDTO.name()) ? map.get(frequencyDTO.name()) : map.get("H1");
    }

    private int cadenceForDust(FrequencyDTO frequencyDTO) {
        var map = strategyValues.getSelectWeightsSpecific().getDust();
        return map.containsKey(frequencyDTO.name()) ? map.get(frequencyDTO.name()) : map.get("H1");
    }
}
