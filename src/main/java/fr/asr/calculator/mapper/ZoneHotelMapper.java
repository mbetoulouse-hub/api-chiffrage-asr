package fr.asr.calculator.mapper;

import fr.asr.calculator.configuration.properties.sector.HotelConfigurationProperties;
import fr.asr.calculator.data.dto.response.ZoneHotelPrefillRoomTypeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ZoneHotelMapper {

    ZoneHotelMapper INSTANCE = Mappers.getMapper(ZoneHotelMapper.class);

    ZoneHotelPrefillRoomTypeDTO.RoomType of(String id, HotelConfigurationProperties.PrefillRoomType prefillRoomType);
}
