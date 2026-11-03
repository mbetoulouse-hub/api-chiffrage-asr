package fr.asr.calculator.service.impl;

import static fr.asr.calculator.data.dto.enumeration.FrequencyDTO.*;
import static fr.asr.calculator.exception.ExceptionType.*;

import fr.asr.calculator.configuration.properties.sector.AbstractSectorProperties;
import fr.asr.calculator.data.dto.BuildingDto;
import fr.asr.calculator.data.dto.DetermineCadenceDto;
import fr.asr.calculator.data.dto.RoomDto;
import fr.asr.calculator.data.dto.enumeration.AdditionalProperties;
import fr.asr.calculator.data.dto.enumeration.FloorTypeDTO;
import fr.asr.calculator.data.dto.enumeration.FrequencyDTO;
import fr.asr.calculator.data.dto.enumeration.SectorDTO;
import fr.asr.calculator.data.dto.internal.H7ConfigFrequency;
import fr.asr.calculator.data.dto.internal.H7DetermineCadenceDTO;
import fr.asr.calculator.exception.functionnal.CadenceException;
import fr.asr.calculator.service.CadenceService;
import fr.asr.calculator.service.SectorService;
import fr.asr.calculator.service.StrategyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CadenceServiceImpl implements CadenceService {

    private final SectorService sectorService;
    private final StrategyService strategyService;

    @Override
    public DetermineCadenceDto determineCadence(SectorDTO sector, RoomDto room, BuildingDto building) {
        AbstractSectorProperties sectorProperties = sectorService.getSectorProperties(sector.name());
        AbstractSectorProperties.ConfigurationRoomType roomTypeConfig =
                sectorProperties.getRoomsType().get(room.getType());

        if (roomTypeConfig == null) {
            throw new CadenceException(
                    CADENCE_NO_ROOM_TYPE,
                    "sector",
                    sector,
                    "roomType",
                    room.getType(),
                    "roomName",
                    room.getName(),
                    "buildingName",
                    building.getName());
        }

        if (hasAdditionalProperties(roomTypeConfig)) {
            DetermineCadenceDto determineCadenceDto = strategyService.applyStrategy(room, building, roomTypeConfig);
            return H7.equals(room.getFrequency())
                    ? new H7DetermineCadenceDTO(determineCadenceDto)
                    : determineCadenceDto;
        }

        String roomCadenceKey = roomTypeConfig.getCadence();
        AbstractSectorProperties.ConfigurationCadence cadenceConfig =
                sectorProperties.getCadences().get(roomCadenceKey);

        if (cadenceConfig == null) {
            throw new CadenceException(
                    CADENCE_NOT_FOUND,
                    "sector",
                    sector,
                    "roomType",
                    room.getType(),
                    "cadence",
                    roomCadenceKey,
                    "roomName",
                    room.getName(),
                    "buildingName",
                    building.getName());
        }

        FrequencyDTO frequency = room.getFrequency();
        AbstractSectorProperties.ConfigurationFrequency cadenceFrequency = getFrequency(frequency, cadenceConfig);

        if (cadenceFrequency == null) {
            throw new CadenceException(
                    CADENCE_NO_FREQUENCY,
                    "sector",
                    sector,
                    "roomType",
                    room.getType(),
                    "cadence",
                    roomCadenceKey,
                    "frequency",
                    frequency,
                    "roomName",
                    room.getName(),
                    "buildingName",
                    building.getName());
        }

        if (cadenceFrequency.getFixedTimeInHours() != null) {
            return DetermineCadenceDto.builder()
                    .fixedTimeInHours(cadenceFrequency.getFixedTimeInHours())
                    .build();
        }

        return DetermineCadenceDto.builder()
                .minTimeInHours(cadenceFrequency.getMinTimeInHours())
                .cadence(
                        room.getFloorType() == FloorTypeDTO.WASHABLE
                                ? cadenceFrequency.getWashableFloor()
                                : cadenceFrequency.getCarpetedFloor())
                .build();
    }

    private AbstractSectorProperties.ConfigurationFrequency getFrequency(
            FrequencyDTO frequency, AbstractSectorProperties.ConfigurationCadence cadenceConfig) {
        if (H7.equals(frequency)) {
            AbstractSectorProperties.ConfigurationFrequency hFrequency =
                    extractHighestFrequency(frequency, cadenceConfig);

            if (hFrequency == null) return null;

            return new H7ConfigFrequency(hFrequency);
        }

        return extractHighestFrequency(frequency, cadenceConfig);
    }

    private AbstractSectorProperties.ConfigurationFrequency extractHighestFrequency(
            FrequencyDTO frequency, AbstractSectorProperties.ConfigurationCadence cadenceConfig) {
        if (H1.equals(frequency)) {
            return cadenceConfig.getFrequencies().get(H1.name());
        }

        AbstractSectorProperties.ConfigurationFrequency configFrequency =
                cadenceConfig.getFrequencies().get(frequency.name());
        return configFrequency != null
                ? configFrequency
                : extractHighestFrequency(FrequencyDTO.values()[frequency.ordinal() - 1], cadenceConfig);
    }

    private boolean hasAdditionalProperties(AbstractSectorProperties.ConfigurationRoomType roomTypeConfig) {
        return roomTypeConfig.getAdditionalProperties() != null
                && roomTypeConfig.getAdditionalProperties().stream()
                        .anyMatch(s -> AdditionalProperties.valueOf(s).isModifyCadenceStrategy());
    }
}
