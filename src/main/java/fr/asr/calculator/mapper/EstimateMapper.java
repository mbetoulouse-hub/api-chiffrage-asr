package fr.asr.calculator.mapper;

import fr.asr.calculator.data.dto.EstimateDTO;
import fr.asr.calculator.data.dto.request.CreateEstimateDTO;
import fr.asr.calculator.data.dto.response.PagedEstimateDTO;
import fr.asr.calculator.data.entity.Building;
import fr.asr.calculator.data.entity.Estimate;
import fr.asr.calculator.data.entity.Profile;
import fr.asr.calculator.data.entity.Room;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(
        componentModel = "spring",
        imports = {MultiPurposeMapper.class})
public interface EstimateMapper {

    EstimateMapper INSTANCE = Mappers.getMapper(EstimateMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "buildings", ignore = true)
    @Mapping(target = "hourlyRate", ignore = true)
    @Mapping(target = "multiPurposeData", ignore = true)
    @Mapping(target = "hotelZoneData", ignore = true)
    @Mapping(target = "status", constant = "DRAFT")
    @Mapping(target = "buildingCount", constant = "0")
    @Mapping(target = "roomCount", constant = "0")
    @Mapping(target = "companyName", source = "createEstimate.name")
    @Mapping(target = "companySector", source = "createEstimate.businessSector")
    Estimate ofDto(Profile profile, CreateEstimateDTO createEstimate);

    @Mapping(target = "company.name", source = "companyName")
    @Mapping(target = "company.businessSector", source = "companySector")
    @Mapping(target = "buildings", source = "buildings", qualifiedByName = "ofBuildingsModelToDto")
    @Mapping(target = "rooms", source = "buildings", qualifiedByName = "ofBuildingsModelToRoomDtoMap")
    @Mapping(
            target = "multiPurposeData",
            expression = "java(MultiPurposeMapper.INSTANCE.of(estimate.getMultiPurposeData()))")
    EstimateDTO ofModel(Estimate estimate);

    @Mapping(target = "businessSector", source = "companySector")
    @Mapping(target = "createdBy", source = "profile.email")
    PagedEstimateDTO ofPagedModel(Estimate estimate);

    EstimateDTO.Room ofRoomModelToDto(Room room);

    @Mapping(target = "id", expression = "java(create ? null : room.getId())")
    @Mapping(target = "name", source = "room.name")
    Room ofRoomDtoToModel(EstimateDTO.Room room, Building building, boolean create);

    default Building ofBuildingDto(
            Estimate estimate,
            EstimateDTO.Building buildingDTO,
            Map<Long, List<EstimateDTO.Room>> rooms,
            Set<Long> existingRoomsId) {
        Building building = new Building(null, buildingDTO.getName(), buildingDTO.getBuildingType(), estimate, null);
        List<Room> roomList = rooms == null
                ? List.of()
                : rooms.getOrDefault(buildingDTO.getId(), List.of()).stream()
                        .map(room -> ofRoomDtoToModel(room, building, !existingRoomsId.contains(room.getId())))
                        .toList();
        building.setRooms(roomList);

        return building;
    }

    @Named("ofBuildingsModelToDto")
    default List<EstimateDTO.Building> ofBuildingsModelToDto(List<Building> buildings) {
        return buildings.stream()
                .map(building ->
                        new EstimateDTO.Building(building.getId(), building.getName(), building.getBuildingType()))
                .collect(Collectors.toList());
    }

    @Named("ofBuildingsModelToRoomDtoMap")
    default Map<Long, List<EstimateDTO.Room>> ofBuildingsModelToRoomDtoMap(List<Building> buildings) {
        return buildings.stream().collect(Collectors.toMap(Building::getId, b -> b.getRooms().stream()
                .map(this::ofRoomModelToDto)
                .collect(Collectors.toList())));
    }
}
