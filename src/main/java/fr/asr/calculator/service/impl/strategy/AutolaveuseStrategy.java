package fr.asr.calculator.service.impl.strategy;

import static fr.asr.calculator.data.dto.enumeration.AdditionalProperties.AUTOLAVEUSE;

import fr.asr.calculator.configuration.properties.StrategyValuesConfigurationProperties;
import fr.asr.calculator.data.dto.BuildingDto;
import fr.asr.calculator.data.dto.DetermineCadenceDto;
import fr.asr.calculator.data.dto.RoomDto;
import fr.asr.calculator.data.dto.enumeration.AdditionalProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AutolaveuseStrategy extends Strategy {

    private final StrategyValuesConfigurationProperties strategyValues;

    @Override
    public DetermineCadenceDto applyStrategy(RoomDto room, BuildingDto building, AdditionalProperties properties) {
        log.info("---- Using strategy AUTOLAVEUSE for room type: {}", room.getType());

        int cadence = room.getSurface() >= strategyValues.getAutolaveuse().getSurfaceThreshold()
                ? strategyValues.getAutolaveuse().getCadenceAboveThreshold()
                : strategyValues.getAutolaveuse().getCadenceBelowThreshold();

        return DetermineCadenceDto.builder().cadence(cadence).build();
    }

    @Override
    public AdditionalProperties getSupportedProperty() {
        return AUTOLAVEUSE;
    }
}
