package fr.asr.calculator.service.impl.strategy;

import static fr.asr.calculator.data.dto.enumeration.AdditionalProperties.CHECK_BOX_PARKING;

import fr.asr.calculator.configuration.properties.StrategyValuesConfigurationProperties;
import fr.asr.calculator.data.dto.BuildingDto;
import fr.asr.calculator.data.dto.DetermineCadenceDto;
import fr.asr.calculator.data.dto.RoomDto;
import fr.asr.calculator.data.dto.enumeration.AdditionalProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CheckParkingStrategy extends Strategy {

    private final StrategyValuesConfigurationProperties strategyValues;

    @Override
    public DetermineCadenceDto applyStrategy(RoomDto room, BuildingDto building, AdditionalProperties properties) {
        log.info("---- Using strategy CHECK_BOX_PARKING for room type: {}", room.getType());
        boolean picking = Boolean.parseBoolean(getValue(room, building, CHECK_BOX_PARKING));

        return DetermineCadenceDto.builder()
                .cadence(
                        picking
                                ? strategyValues.getCheckParking().getWithPicking()
                                : strategyValues.getCheckParking().getWithoutPicking())
                .build();
    }

    @Override
    public AdditionalProperties getSupportedProperty() {
        return CHECK_BOX_PARKING;
    }
}
