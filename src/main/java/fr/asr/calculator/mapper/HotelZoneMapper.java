package fr.asr.calculator.mapper;

import fr.asr.calculator.data.dto.HotelZoneDataDTO;
import fr.asr.calculator.data.entity.HotelZoneData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface HotelZoneMapper {

    HotelZoneMapper INSTANCE = Mappers.getMapper(HotelZoneMapper.class);

    HotelZoneDataDTO of(HotelZoneData zoneData);

    @Mapping(target = "id", source = "legacyHotelZoneData.id")
    @Mapping(target = "classification", source = "hotelZoneDataDTO.classification")
    @Mapping(target = "globalRate", source = "hotelZoneDataDTO.globalRate")
    @Mapping(target = "occupancyRate", source = "hotelZoneDataDTO.occupancyRate")
    @Mapping(target = "openingDaysPerWeek", source = "hotelZoneDataDTO.openingDaysPerWeek")
    @Mapping(target = "roomTypes", source = "hotelZoneDataDTO.roomTypes")
    @Mapping(target = "roomCounts", source = "hotelZoneDataDTO.roomCounts")
    @Mapping(target = "managementPackage", source = "hotelZoneDataDTO.managementPackage")
    HotelZoneData of(HotelZoneDataDTO hotelZoneDataDTO, HotelZoneData legacyHotelZoneData);
}
