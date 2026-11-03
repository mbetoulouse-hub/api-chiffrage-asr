package fr.asr.calculator.service.impl;

import static fr.asr.calculator.constant.ApplicationConstant.PAGE_SIZE;
import static fr.asr.calculator.data.dto.enumeration.SectorDTO.HOTEL;
import static fr.asr.calculator.exception.ExceptionType.ESTIMATE_PROFILE_CANT_INTERACT;

import fr.asr.calculator.data.dao.estimate.EstimateDao;
import fr.asr.calculator.data.dao.profile.ProfileDao;
import fr.asr.calculator.data.dto.EstimateDTO;
import fr.asr.calculator.data.dto.HotelZoneDataDTO;
import fr.asr.calculator.data.dto.request.CreateEstimateDTO;
import fr.asr.calculator.data.dto.request.SaveEstimateDTO;
import fr.asr.calculator.data.dto.response.FindEstimateDTO;
import fr.asr.calculator.data.dto.response.PagedEstimateDTO;
import fr.asr.calculator.data.dto.response.SavedEstimateDTO;
import fr.asr.calculator.data.entity.*;
import fr.asr.calculator.exception.functionnal.EstimateException;
import fr.asr.calculator.mapper.EstimateMapper;
import fr.asr.calculator.mapper.HotelZoneMapper;
import fr.asr.calculator.mapper.MultiPurposeMapper;
import fr.asr.calculator.service.EstimateService;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public class EstimateServiceImpl implements EstimateService {

    private final EstimateDao estimateDao;
    private final ProfileDao profileDao;

    @Override
    public Page<PagedEstimateDTO> getAllEstimates(int page, String email, String input, String status) {
        Profile profile = profileDao.findByEmail(email);
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);

        if (profile.getRole().equals(Profile.Role.ADMIN)) {
            return estimateDao.findAll(input, status, pageable).map(EstimateMapper.INSTANCE::ofPagedModel);
        }

        return estimateDao.findAll(input, status, profile.getId(), pageable).map(EstimateMapper.INSTANCE::ofPagedModel);
    }

    @Transactional
    @Override
    public EstimateDTO createNewEstimate(String email, CreateEstimateDTO createEstimateDTO) {
        Profile profile = profileDao.findByEmail(email);
        Estimate estimate = EstimateMapper.INSTANCE.ofDto(profile, createEstimateDTO);
        return EstimateMapper.INSTANCE.ofModel(estimateDao.save(estimate));
    }

    @Transactional
    @Override
    public SavedEstimateDTO updateEstimate(String currentEmail, SaveEstimateDTO saveEstimateDTO) {
        EstimateDTO estimateDTO = saveEstimateDTO.getEstimate();
        log.info("Updating estimate with ID: {}", estimateDTO.getId());
        Estimate estimate = estimateDao.findById(estimateDTO.getId());
        Profile profile = profileDao.findByEmail(currentEmail);

        if (!profileCanInteractWithEstimate(profile, estimate)) {
            log.warn(
                    "Profile with email {} is not allowed to update estimate with ID: {}",
                    currentEmail,
                    estimateDTO.getId());
            throw new EstimateException(ESTIMATE_PROFILE_CANT_INTERACT);
        }

        log.info("Profile with email {} is allowed to update estimate with ID: {}", currentEmail, estimateDTO.getId());
        estimate.setCompanyName(estimateDTO.getCompany().getName());
        estimate.setCompanySector(estimateDTO.getCompany().getBusinessSector());
        estimate.setHourlyRate(estimateDTO.getHourlyRate());
        estimate.setMultiPurposeData(MultiPurposeMapper.INSTANCE.of(estimateDTO.getMultiPurposeData()));

        HotelZoneDataDTO hotelZoneDataDTOToReturn = saveEstimateDTO.getHotelZoneData();
        if (HOTEL.equals(estimateDTO.getCompany().getBusinessSector())) {
            if (saveEstimateDTO.getHotelZoneData() != null) {
                log.info("Updating hotel zone data for estimate with ID: {}", estimate.getId());
                HotelZoneData legacyHotelZoneData = estimate.getHotelZoneData();
                HotelZoneData hotelZoneData =
                        HotelZoneMapper.INSTANCE.of(saveEstimateDTO.getHotelZoneData(), legacyHotelZoneData);
                estimate.setHotelZoneData(hotelZoneData);

                hotelZoneDataDTOToReturn = HotelZoneMapper.INSTANCE.of(hotelZoneData);
            }
        } else {
            estimate.setHotelZoneData(null);
        }

        if (estimateDTO.getBuildings() == null) {
            estimate.setBuildingCount(0);
            estimate.setRoomCount(0);
            return SavedEstimateDTO.builder()
                    .estimate(EstimateMapper.INSTANCE.ofModel(estimateDao.save(estimate)))
                    .hotelZoneData(hotelZoneDataDTOToReturn)
                    .build();
        }

        List<Building> buildings = estimate.getBuildings();

        Set<Long> existingBuildingIds = buildings.stream().map(Building::getId).collect(Collectors.toSet());

        Map<Long, Building> existingBuildingMap =
                estimate.getBuildings().stream().collect(Collectors.toMap(Building::getId, b -> b));

        Set<Long> dtoIds = estimateDTO.getBuildings().stream()
                .map(EstimateDTO.Building::getId)
                .collect(Collectors.toSet());

        // REMOVE
        List<Building> buildingsToRemove = estimate.getBuildings().stream()
                .filter(b -> !dtoIds.contains(b.getId()))
                .toList();

        log.info("Removing {} buildings that are not in the DTO", buildingsToRemove.size());
        estimate.getBuildings().removeAll(buildingsToRemove);

        // ADD OR UPDATE
        for (EstimateDTO.Building dto : estimateDTO.getBuildings()) {
            if (existingBuildingIds.contains(dto.getId())) {
                log.info("- Updating existing building with ID: {}", dto.getId());
                Building existingBuilding = existingBuildingMap.get(dto.getId());

                Set<Long> existingRoomsId =
                        existingBuilding.getRooms().stream().map(Room::getId).collect(Collectors.toSet());

                Building buildingToUpdate =
                        EstimateMapper.INSTANCE.ofBuildingDto(estimate, dto, estimateDTO.getRooms(), existingRoomsId);
                buildingToUpdate.setId(dto.getId());
                estimate.getBuildings().set(estimate.getBuildings().indexOf(existingBuilding), buildingToUpdate);
            } else {
                log.info("- Adding new building with ID (dto id): {}", dto.getId());
                estimate.getBuildings()
                        .add(EstimateMapper.INSTANCE.ofBuildingDto(estimate, dto, estimateDTO.getRooms(), Set.of()));
            }
        }

        estimate.setBuildingCount(dtoIds.size());
        estimate.setRoomCount(
                estimateDTO.getRooms() == null
                        ? 0
                        : estimateDTO.getRooms().values().stream()
                                .mapToInt(List::size)
                                .sum());

        return SavedEstimateDTO.builder()
                .estimate(EstimateMapper.INSTANCE.ofModel(estimateDao.save(estimate)))
                .hotelZoneData(hotelZoneDataDTOToReturn)
                .build();
    }

    @Override
    public FindEstimateDTO getEstimate(String email, long id) {
        log.info("Retrieving estimate with ID: {}", id);
        Estimate estimate = estimateDao.findById(id);
        Profile profile = profileDao.findByEmail(email);

        if (!profileCanInteractWithEstimate(profile, estimate)) {
            log.warn("Profile with email {} is not allowed to update estimate with ID: {}", email, id);
            throw new EstimateException(ESTIMATE_PROFILE_CANT_INTERACT);
        }

        return FindEstimateDTO.builder()
                .estimate(EstimateMapper.INSTANCE.ofModel(estimate))
                .hotelZoneData(HotelZoneMapper.INSTANCE.of(estimate.getHotelZoneData()))
                .build();
    }

    @Override
    public void deleteEstimate(String email, long id) {
        log.info("Deleting estimate with ID: {}", id);
        Estimate estimate = estimateDao.findById(id);
        Profile profile = profileDao.findByEmail(email);

        if (!profileCanInteractWithEstimate(profile, estimate)) {
            log.warn("Profile with email {} is not allowed to update estimate with ID: {}", email, id);
            throw new EstimateException(ESTIMATE_PROFILE_CANT_INTERACT);
        }

        estimateDao.delete(estimate);
    }

    @Override
    public boolean profileCanInteractWithEstimate(Profile profile, Estimate estimate) {
        Profile estimateProfile = estimate.getProfile();

        return profile.getId().equals(estimateProfile.getId())
                || profile.getRole() == Profile.Role.ADMIN
                || (profile.getRole() == Profile.Role.MANAGER
                        && estimateProfile.getManager() != null
                        && estimateProfile.getManager().getId().equals(profile.getId()));
    }
}
