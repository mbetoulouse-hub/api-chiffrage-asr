package fr.asr.calculator.service;

import fr.asr.calculator.configuration.properties.sector.AbstractSectorProperties;
import fr.asr.calculator.data.dto.BuildingDto;
import fr.asr.calculator.data.dto.DetermineCadenceDto;
import fr.asr.calculator.data.dto.RoomDto;

public interface StrategyService {

    DetermineCadenceDto applyStrategy(
            RoomDto room, BuildingDto building, AbstractSectorProperties.ConfigurationRoomType roomTypeConfig);
}
