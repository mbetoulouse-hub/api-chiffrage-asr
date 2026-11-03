package fr.asr.calculator.mapper;

import fr.asr.calculator.configuration.properties.sector.AbstractSectorProperties;
import fr.asr.calculator.data.dto.RoomTypeDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RoomTypeMapper {

    RoomTypeMapper INSTANCE = Mappers.getMapper(RoomTypeMapper.class);

    @Mapping(target = "key", source = "key")
    @Mapping(target = "title", source = "roomType.title")
    @Mapping(target = "additionalProperties", source = "roomType.additionalProperties")
    RoomTypeDto of(String key, AbstractSectorProperties.ConfigurationRoomType roomType);
}
