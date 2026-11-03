package fr.asr.calculator.service.impl;

import static fr.asr.calculator.data.dto.enumeration.AdditionalProperties.CUSTOM_CADENCE;
import static fr.asr.calculator.exception.ExceptionType.SECTOR_NOT_FOUND;

import fr.asr.calculator.configuration.properties.sector.*;
import fr.asr.calculator.data.dto.BuildingTypeDto;
import fr.asr.calculator.data.dto.RoomTypeDto;
import fr.asr.calculator.data.dto.enumeration.SectorDTO;
import fr.asr.calculator.data.dto.response.SectorTypeDTO;
import fr.asr.calculator.data.dto.response.ZoneHotelPrefillRoomTypeDTO;
import fr.asr.calculator.exception.functionnal.NotFoundException;
import fr.asr.calculator.mapper.RoomTypeMapper;
import fr.asr.calculator.mapper.ZoneHotelMapper;
import fr.asr.calculator.service.SectorService;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;

@Slf4j
@RequiredArgsConstructor
public class SectorServiceImpl implements SectorService {

    private static final String EMPTY_CADENCE_KEY = "EMPTY";

    private final TertiaireConfigurationProperties tertiaireConfigurationProperties;
    private final LifeBasedConfigurationProperties lifeBasedConfigurationProperties;
    private final CoproprietesConfigurationProperties coproprietesConfigurationProperties;
    private final IndustrieConfigurationProperties industrieConfigurationProperties;
    private final AgroConfigurationProperties agroConfigurationProperties;
    private final BioConfigurationProperties bioConfigurationProperties;
    private final MedicalConfigurationProperties medicalConfigurationProperties;
    private final CrecheConfigurationProperties crecheConfigurationProperties;
    private final SchoolConfigurationProperties schoolConfigurationProperties;
    private final ConcessionsConfigurationProperties concessionsConfigurationProperties;
    private final GSSMarketConfigurationProperties gssMarketConfigurationProperties;
    private final SportConfigurationProperties sportConfigurationProperties;
    private final HotelConfigurationProperties hotelConfigurationProperties;
    private final RestaurantConfigurationProperties restaurantConfigurationProperties;
    private final VeterinaireConfigurationProperties veterinaireConfigurationProperties;

    @Cacheable(value = "sectorTypes", key = "'allSectorTypes'")
    @Override
    public List<SectorTypeDTO> getAllSectorTypes() {
        return Arrays.stream(SectorDTO.values())
                .map(sector -> {
                    AbstractSectorProperties sectorProperties = getSectorProperties(sector.name());

                    List<BuildingTypeDto> buildingTypeDtoList = sectorProperties.getBuildingsType().entrySet().stream()
                            .map(buildingEntry -> {
                                List<RoomTypeDto> roomTypeDtos = sectorProperties.getRoomsType().entrySet().stream()
                                        .filter(entry -> buildingEntry
                                                .getValue()
                                                .getRoomsType()
                                                .contains(entry.getKey()))
                                        .map(entry -> {
                                            if (EMPTY_CADENCE_KEY.equals(
                                                    entry.getValue().getCadence())) {
                                                if (entry.getValue().getAdditionalProperties() == null) {
                                                    entry.getValue()
                                                            .setAdditionalProperties(List.of(CUSTOM_CADENCE.name()));
                                                } else {
                                                    entry.getValue()
                                                            .getAdditionalProperties()
                                                            .add(CUSTOM_CADENCE.name());
                                                }
                                            }

                                            return RoomTypeMapper.INSTANCE.of(entry.getKey(), entry.getValue());
                                        })
                                        .toList();

                                return new BuildingTypeDto(
                                        buildingEntry.getKey(),
                                        buildingEntry.getValue().getTitle(),
                                        roomTypeDtos);
                            })
                            .toList();

                    return new SectorTypeDTO(sector, sectorProperties.getTitle(), buildingTypeDtoList);
                })
                .toList();
    }

    @Override
    public AbstractSectorProperties getSectorProperties(String name) {
        return switch (name.toLowerCase()) {
            case "tertiaire" -> tertiaireConfigurationProperties;
            case "life_based" -> lifeBasedConfigurationProperties;
            case "coproprietes" -> coproprietesConfigurationProperties;
            case "industrie" -> industrieConfigurationProperties;
            case "agro" -> agroConfigurationProperties;
            case "bio" -> bioConfigurationProperties;
            case "medical" -> medicalConfigurationProperties;
            case "creche" -> crecheConfigurationProperties;
            case "school" -> schoolConfigurationProperties;
            case "concessions" -> concessionsConfigurationProperties;
            case "gss_market" -> gssMarketConfigurationProperties;
            case "sport" -> sportConfigurationProperties;
            case "hotel" -> hotelConfigurationProperties;
            case "restaurant" -> restaurantConfigurationProperties;
            case "veterinaire" -> veterinaireConfigurationProperties;
            default -> throw new NotFoundException(SECTOR_NOT_FOUND, "sector", name);
        };
    }

    @Cacheable(value = "zoneHotelPrefillRoomType", key = "'zoneHotelPrefillRoomType'")
    @Override
    public ZoneHotelPrefillRoomTypeDTO getZoneHotelPrefillRoomType() {
        HotelConfigurationProperties.ZoneHotel zoneHotel = hotelConfigurationProperties.getZoneHotel();
        if (zoneHotel == null || zoneHotel.getPrefillRoomType() == null) {
            return new ZoneHotelPrefillRoomTypeDTO(Map.of());
        }

        return new ZoneHotelPrefillRoomTypeDTO(zoneHotel.getPrefillRoomType().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().entrySet().stream()
                        .map(roomEntry -> ZoneHotelMapper.INSTANCE.of(roomEntry.getKey(), roomEntry.getValue()))
                        .toList())));
    }
}
