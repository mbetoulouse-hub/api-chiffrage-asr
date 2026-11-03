package fr.asr.calculator.service;

import fr.asr.calculator.configuration.properties.sector.AbstractSectorProperties;
import fr.asr.calculator.data.dto.response.SectorTypeDTO;
import fr.asr.calculator.data.dto.response.ZoneHotelPrefillRoomTypeDTO;
import java.util.List;

public interface SectorService {

    List<SectorTypeDTO> getAllSectorTypes();

    AbstractSectorProperties getSectorProperties(String name);

    ZoneHotelPrefillRoomTypeDTO getZoneHotelPrefillRoomType();
}
