package fr.asr.calculator.service.impl;

import static fr.asr.calculator.exception.ExceptionType.*;

import fr.asr.calculator.data.dto.BuildingDto;
import fr.asr.calculator.data.dto.DetermineCadenceDto;
import fr.asr.calculator.data.dto.RoomDto;
import fr.asr.calculator.data.dto.enumeration.AdditionalProperties;
import fr.asr.calculator.exception.functionnal.CadenceException;
import fr.asr.calculator.service.StrategyService;
import fr.asr.calculator.service.impl.strategy.*;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StrategyServiceImpl implements StrategyService {

    private final Map<AdditionalProperties, Strategy> strategies = new EnumMap<>(AdditionalProperties.class);

    public StrategyServiceImpl(List<Strategy> strategyList) {
        for (Strategy strategy : strategyList) {
            strategies.put(strategy.getSupportedProperty(), strategy);
        }
    }

    @Override
    public DetermineCadenceDto applyStrategy(
            RoomDto room,
            BuildingDto building,
            fr.asr.calculator.configuration.properties.sector.AbstractSectorProperties.ConfigurationRoomType
                    roomTypeConfig) {
        log.info("--- Applying strategy for room type: {}", room.getType());

        for (String propName : roomTypeConfig.getAdditionalProperties()) {
            AdditionalProperties prop = AdditionalProperties.valueOf(propName);
            if (prop.isModifyCadenceStrategy() && strategies.containsKey(prop)) {
                Strategy foundStrategy = strategies.get(prop);
                return foundStrategy.applyStrategy(room, building, prop);
            }
        }

        throw new CadenceException(
                STRATEGY_NOT_FOUND,
                "roomType",
                room.getType(),
                "roomName",
                room.getName(),
                "buildingName",
                building.getName());
    }
}
