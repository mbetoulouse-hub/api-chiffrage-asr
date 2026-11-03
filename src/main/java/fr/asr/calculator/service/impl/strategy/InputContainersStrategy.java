package fr.asr.calculator.service.impl.strategy;

import static fr.asr.calculator.data.dto.enumeration.AdditionalProperties.INT_INPUT_CONTAINERS;

import fr.asr.calculator.configuration.properties.StrategyValuesConfigurationProperties;
import fr.asr.calculator.data.dto.BuildingDto;
import fr.asr.calculator.data.dto.DetermineCadenceDto;
import fr.asr.calculator.data.dto.RoomDto;
import fr.asr.calculator.data.dto.enumeration.AdditionalProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class InputContainersStrategy extends Strategy {

    private final StrategyValuesConfigurationProperties strategyValues;

    @Override
    public DetermineCadenceDto applyStrategy(RoomDto room, BuildingDto building, AdditionalProperties properties) {
        log.info("---- Using strategy INT_INPUT_CONTAINERS for CONTAINERS_DISINFECTION");

        int numberOfContainers = formatToInt(getValue(room, building, INT_INPUT_CONTAINERS), room, building);

        double fixedTimeInHours =
                numberOfContainers <= strategyValues.getInputContainers().getLowThreshold()
                        ? strategyValues.getInputContainers().getLowCadence()
                        : strategyValues.getInputContainers().getHighCadenceFactor() * numberOfContainers;

        return DetermineCadenceDto.builder().fixedTimeInHours(fixedTimeInHours).build();
    }

    @Override
    public AdditionalProperties getSupportedProperty() {
        return INT_INPUT_CONTAINERS;
    }
}
