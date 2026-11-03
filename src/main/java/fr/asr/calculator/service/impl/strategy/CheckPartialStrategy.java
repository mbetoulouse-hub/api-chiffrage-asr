package fr.asr.calculator.service.impl.strategy;

import static fr.asr.calculator.data.dto.enumeration.AdditionalProperties.*;
import static fr.asr.calculator.exception.ExceptionType.CADENCE_INVALID_ROOM_TYPE_FOR_PARTIAL_STRATEGY;

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
public class CheckPartialStrategy extends Strategy {

    private final StrategyValuesConfigurationProperties strategyValues;

    @Override
    public DetermineCadenceDto applyStrategy(RoomDto room, BuildingDto building, AdditionalProperties properties) {
        log.info("---- Using strategy CHECK_BOX_PARTIAL for room type: {}", room.getType());
        boolean partial = Boolean.parseBoolean(getValue(room, building, CHECK_BOX_PARTIAL));

        return DetermineCadenceDto.builder()
                .cadence(partial ? cadenceForPartial(room, building) : cadenceForFull(room, building))
                .build();
    }

    @Override
    public AdditionalProperties getSupportedProperty() {
        return CHECK_BOX_PARTIAL;
    }

    private int cadenceForPartial(RoomDto room, BuildingDto building) {
        var map = strategyValues.getCheckPartial().getPartial();

        if (map.containsKey(room.getType())) {
            return map.get(room.getType());
        }

        throw new CadenceException(
                CADENCE_INVALID_ROOM_TYPE_FOR_PARTIAL_STRATEGY,
                "property",
                CADENCE_INVALID_ROOM_TYPE_FOR_PARTIAL_STRATEGY.name(),
                "roomType",
                room.getType(),
                "roomName",
                room.getName(),
                "buildingName",
                building.getName());
    }

    private int cadenceForFull(RoomDto room, BuildingDto building) {
        var map = strategyValues.getCheckPartial().getFull();

        if (map.containsKey(room.getType())) {
            return map.get(room.getType());
        }
        throw new CadenceException(
                CADENCE_INVALID_ROOM_TYPE_FOR_PARTIAL_STRATEGY,
                "property",
                CADENCE_INVALID_ROOM_TYPE_FOR_PARTIAL_STRATEGY.name(),
                "roomType",
                room.getType(),
                "roomName",
                room.getName(),
                "buildingName",
                building.getName());
    }
}
