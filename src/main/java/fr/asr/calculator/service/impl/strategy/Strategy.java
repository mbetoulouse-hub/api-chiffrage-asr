package fr.asr.calculator.service.impl.strategy;

import static fr.asr.calculator.data.dto.enumeration.AdditionalProperties.INT_INPUT_CONTAINERS;
import static fr.asr.calculator.exception.ExceptionType.STRATEGY_ADDITIONAL_PROPERTY_NOT_FOUND;

import fr.asr.calculator.data.dto.BuildingDto;
import fr.asr.calculator.data.dto.DetermineCadenceDto;
import fr.asr.calculator.data.dto.RoomDto;
import fr.asr.calculator.data.dto.enumeration.AdditionalProperties;
import fr.asr.calculator.exception.functionnal.CadenceException;

public abstract class Strategy {

    public abstract DetermineCadenceDto applyStrategy(
            RoomDto room, BuildingDto building, AdditionalProperties properties);

    public abstract AdditionalProperties getSupportedProperty();

    protected String getValue(RoomDto room, BuildingDto building, AdditionalProperties properties) {
        return room.getAdditionalProperties().stream()
                .filter(property -> property.getKey().equals(properties.name()))
                .map(RoomDto.AdditionalProperties::getValue)
                .findFirst()
                .orElseThrow(() -> new CadenceException(
                        STRATEGY_ADDITIONAL_PROPERTY_NOT_FOUND,
                        "property",
                        properties.name(),
                        "roomType",
                        room.getType(),
                        "roomName",
                        room.getName(),
                        "buildingName",
                        building.getName()));
    }

    public static int formatToInt(String value, RoomDto room, BuildingDto building) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new CadenceException(
                    STRATEGY_ADDITIONAL_PROPERTY_NOT_FOUND,
                    "property",
                    INT_INPUT_CONTAINERS.name(),
                    "roomType",
                    room.getType(),
                    "value",
                    value,
                    "roomName",
                    room.getName(),
                    "buildingName",
                    building.getName());
        }
    }
}
