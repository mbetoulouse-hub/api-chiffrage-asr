package fr.asr.calculator.service.impl.strategy;

import static fr.asr.calculator.data.dto.enumeration.AdditionalProperties.CHECK_BOX_ARMCHAIR;

import fr.asr.calculator.configuration.properties.StrategyValuesConfigurationProperties;
import fr.asr.calculator.data.dto.BuildingDto;
import fr.asr.calculator.data.dto.DetermineCadenceDto;
import fr.asr.calculator.data.dto.RoomDto;
import fr.asr.calculator.data.dto.enumeration.AdditionalProperties;
import fr.asr.calculator.data.dto.enumeration.FrequencyDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CheckArmchairStrategy extends Strategy {

    private final StrategyValuesConfigurationProperties strategyValues;

    @Override
    public DetermineCadenceDto applyStrategy(RoomDto room, BuildingDto building, AdditionalProperties properties) {
        log.info("---- Using strategy CHECK_BOX_ARMCHAIR for room type: {}", room.getType());
        boolean armchair = Boolean.parseBoolean(getValue(room, building, CHECK_BOX_ARMCHAIR));

        return DetermineCadenceDto.builder()
                .cadence(armchair ? cadenceForArmchair(room.getFrequency()) : cadenceForNoArmchair(room.getFrequency()))
                .build();
    }

    @Override
    public AdditionalProperties getSupportedProperty() {
        return CHECK_BOX_ARMCHAIR;
    }

    private int cadenceForArmchair(FrequencyDTO frequencyDTO) {
        var map = strategyValues.getCheckArmchair().getWithArmchair();
        return map.containsKey(frequencyDTO.name()) ? map.get(frequencyDTO.name()) : map.get("H1");
    }

    private int cadenceForNoArmchair(FrequencyDTO frequencyDTO) {
        var map = strategyValues.getCheckArmchair().getWithoutArmchair();
        return map.containsKey(frequencyDTO.name()) ? map.get(frequencyDTO.name()) : map.get("H1");
    }
}
