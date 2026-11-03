package fr.asr.calculator.service.impl;

import static fr.asr.calculator.data.dto.enumeration.FrequencyDTO.*;
import static fr.asr.calculator.data.dto.enumeration.SectorDTO.GSS_MARKET;
import static fr.asr.calculator.data.dto.enumeration.SectorDTO.HOTEL;
import static fr.asr.calculator.exception.ExceptionType.*;
import static fr.asr.calculator.util.NumberUtil.toTwoDecimalPlaces;

import fr.asr.calculator.configuration.properties.sector.AbstractSectorProperties;
import fr.asr.calculator.data.dao.estimate.EstimateDao;
import fr.asr.calculator.data.dao.profile.ProfileDao;
import fr.asr.calculator.data.dto.BuildingDto;
import fr.asr.calculator.data.dto.DetermineCadenceDto;
import fr.asr.calculator.data.dto.RoomDto;
import fr.asr.calculator.data.dto.enumeration.AdditionalProperties;
import fr.asr.calculator.data.dto.enumeration.FrequencyDTO;
import fr.asr.calculator.data.dto.enumeration.SectorDTO;
import fr.asr.calculator.data.dto.request.SimulateDTO;
import fr.asr.calculator.data.dto.request.SubmitDTO;
import fr.asr.calculator.data.dto.response.CalculateDTO;
import fr.asr.calculator.data.entity.Estimate;
import fr.asr.calculator.data.entity.Profile;
import fr.asr.calculator.exception.functionnal.CalculateException;
import fr.asr.calculator.exception.functionnal.EstimateException;
import fr.asr.calculator.service.*;
import fr.asr.calculator.service.impl.strategy.Strategy;
import fr.asr.calculator.util.HoursUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public class CalculateServiceImpl implements CalculateService {

    private final AlertService alertService;
    private final ExportService exportService;
    private final ZoneMultiPurposeCalculateService zoneMultiPurposeCalculateService;
    private final CadenceService cadenceService;
    private final SectorService sectorService;
    private final EstimateService estimateService;
    private final ZoneHotelCalculateService zoneHotelCalculateService;
    private final EstimateDao estimateDao;
    private final ProfileDao profileDao;

    @Override
    public byte[] export(String email, SubmitDTO submitDTO) {
        log.info("Starting export for email : {} and estimate id : {}", email, submitDTO.getId());
        Estimate estimate = identifyEstimate(email, submitDTO);

        return exportService.process(estimate, submitDTO, simulate(submitDTO));
    }

    @Transactional
    @Override
    public CalculateDTO submit(String email, SubmitDTO submitDTO) {
        log.info("Starting submission for email : {} and estimate id : {}", email, submitDTO.getId());

        Estimate estimate = identifyEstimate(email, submitDTO);
        CalculateDTO calculateDTO = simulate(submitDTO);

        estimate.setStatus(Estimate.Status.COMPLETE);
        estimateDao.save(estimate);

        return calculateDTO;
    }

    // TODO: TertiaryCalculateService
    @Override
    public CalculateDTO simulate(SimulateDTO simulateDTO) {
        log.info(
                "Starting simulation with {} buildings in sector {}",
                simulateDTO.getBuildings().size(),
                simulateDTO.getSector());

        if (GSS_MARKET.equals(simulateDTO.getSector())) {
            log.info("Sector is GSS_MARKET, applying special surface reduction for rooms (only AUTOLAVEUSE)");

            simulateDTO.getBuildings().forEach(buildingDto -> buildingDto.getRooms().stream()
                    .filter(roomDto -> roomDto.getAdditionalProperties() != null
                            && roomDto.getAdditionalProperties().stream()
                                    .anyMatch(additionalProperties -> additionalProperties
                                            .getKey()
                                            .equals(AdditionalProperties.AUTOLAVEUSE.name())))
                    .forEach(roomDto -> roomDto.setSurface((int) (roomDto.getSurface() * 0.6))));
        }

        List<CalculateDTO.CalculateBuilding> buildings = simulateDTO.getBuildings().stream()
                .map(building -> calculateBuilding(simulateDTO.getHourlyRate(), simulateDTO.getSector(), building))
                .collect(Collectors.toList());

        List<CalculateDTO.CalculateDetail> allDetails = buildings.stream()
                .flatMap(building -> building.getDetails().stream())
                .toList();

        FrequencyDTO biggestFrequency = getBiggestFrequencyFromSimulateDTO(simulateDTO);

        CalculateDTO calculateDTO = new CalculateDTO();
        calculateDTO.setBuildings(buildings);
        calculateDTO.setBiggestFrequency(biggestFrequency);
        calculateDTO.setAggregateResult(aggregateResult(allDetails, biggestFrequency));
        calculateDTO.setAlerts(alertService.extractAlerts(biggestFrequency, calculateDTO));

        if (HOTEL.equals(simulateDTO.getSector())) {
            if (simulateDTO.getHotelZoneData() == null) {
                throw new CalculateException(CALCULATE_NO_ZONE_HOTEL_DATA_PROVIDED);
            }

            calculateDTO.setHotelCalculationResult(
                    zoneHotelCalculateService.calculateHotelZone(simulateDTO.getHotelZoneData()));
        }

        calculateDTO.setMultiPurposeCalculationResult(
                zoneMultiPurposeCalculateService.calculate(simulateDTO.getMultiPurposeData()));

        return calculateDTO;
    }

    private Estimate identifyEstimate(String email, SubmitDTO submitDTO) {
        Estimate estimate = estimateDao.findById(submitDTO.getId());
        Profile profile = profileDao.findByEmail(email);

        if (!estimateService.profileCanInteractWithEstimate(profile, estimate)) {
            log.warn("Profile with email {} is not allowed to update estimate with ID: {}", email, submitDTO.getId());
            throw new EstimateException(ESTIMATE_PROFILE_CANT_INTERACT);
        }

        return estimate;
    }

    private CalculateDTO.CalculateBuilding calculateBuilding(
            BigDecimal hourlyRate, SectorDTO sectorDTO, BuildingDto building) {
        CalculateDTO.CalculateBuilding calculateBuilding = new CalculateDTO.CalculateBuilding();
        calculateBuilding.setName(building.getName());
        calculateBuilding.setType(building.getBuildingType());

        List<CalculateDTO.CalculateDetail> details = new ArrayList<>();
        log.info("Starting with Building: {}", building.getName());

        for (RoomDto room : building.getRooms()) {
            log.info(
                    "-- Room: {} - Surface: {} - Frequency: {} - type: {}",
                    room.getName(),
                    room.getSurface(),
                    room.getFrequency(),
                    room.getType());

            if (!isRoomTypeValidForBuilding(sectorDTO, building, room)) {
                throw new CalculateException(
                        CALCULATE_NO_CORRESPONDING_ROOM_TYPE,
                        "roomType",
                        room.getType(),
                        "buildingType",
                        building.getBuildingType(),
                        "roomName",
                        room.getName(),
                        "buildingName",
                        building.getName());
            }

            if (H7.equals(room.getFrequency())) {
                RoomDto h6Room = room.clone();
                h6Room.setFrequency(H6);
                h6Room.setName(room.getName() + " - H6");
                room.setName(room.getName() + " - H7");

                details.add(buildCalculateDetail(h6Room, building, sectorDTO, hourlyRate));
            }

            details.add(buildCalculateDetail(room, building, sectorDTO, hourlyRate));
        }

        calculateBuilding.setDetails(details);
        calculateBuilding.setAggregateResult(aggregateResult(details, getBiggestFrequency(building.getRooms())));
        return calculateBuilding;
    }

    private CalculateDTO.AggregateResult aggregateResult(
            List<CalculateDTO.CalculateDetail> details, FrequencyDTO biggestFrequency) {
        CalculateDTO.AggregateResult aggregateResult = new CalculateDTO.AggregateResult();

        if (details.isEmpty()) {
            log.info("No details provided for aggregation, returning empty aggregate result.");
            aggregateResult.setSurfaceSum(0);
            aggregateResult.setTimeSum(0D);
            aggregateResult.setTimeSumRounded(0D);
            aggregateResult.setMonthlyRateSum(BigDecimal.ZERO);
            aggregateResult.setPricePerSquareMeter(BigDecimal.ZERO);
            return aggregateResult;
        }

        aggregateResult.setSurfaceSum(details.stream()
                .mapToInt(CalculateDTO.CalculateDetail::getSurface)
                .sum());
        aggregateResult.setTimeSum(details.stream()
                .mapToDouble(CalculateDTO.CalculateDetail::getTimeInHours)
                .sum());
        aggregateResult.setTimeSumRounded(HoursUtil.roundUpToQuarter(aggregateResult.getTimeSum()));
        if (biggestFrequency != null) {
            aggregateResult.setTimeSumRoundedByService(
                    HoursUtil.roundUpToQuarter(aggregateResult.getTimeSum() / biggestFrequency.getValue()));
        }
        aggregateResult.setMonthlyRateSum(details.stream()
                .map(CalculateDTO.CalculateDetail::getMonthlyRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        aggregateResult.setPricePerSquareMeter(aggregateResult
                .getMonthlyRateSum()
                .divide(BigDecimal.valueOf(aggregateResult.getSurfaceSum()), 2, RoundingMode.DOWN));

        return aggregateResult;
    }

    private CalculateDTO.CalculateDetail buildCalculateDetail(
            RoomDto room, BuildingDto building, SectorDTO sectorDTO, BigDecimal hourlyRate) {
        CalculateDTO.CalculateDetail calculateDetail = new CalculateDTO.CalculateDetail();
        calculateDetail.setName(room.getName());
        calculateDetail.setRoomType(room.getType());
        calculateDetail.setRemarks(room.getRemarks());
        calculateDetail.setSurface(room.getSurface());
        calculateDetail.setFrequencyDTO(room.getFrequency());
        calculateDetail.setHourlyRateUsed(hourlyRate);

        DetermineCadenceDto cadenceDto = cadenceService.determineCadence(sectorDTO, room, building);
        calculateDetail.setCadence(cadenceDto.getCadence());

        // Calculate
        FrequencyDTO frequency = room.getFrequency();
        int weekNumber = getCustomWeekNumber(room, building);

        calculateDetail.setWeekNumber(weekNumber);
        calculateDetail.setTimeInHoursByService(toTwoDecimalPlaces(calculateTimeInHours(room, building, cadenceDto)));
        calculateDetail.setTimeInHours(
                toTwoDecimalPlaces(frequency.getValue() * calculateDetail.getTimeInHoursByService()));
        calculateDetail.setTimeInHoursByMonth(toTwoDecimalPlaces((calculateDetail.getTimeInHours() * weekNumber) / 12));

        calculateDetail.setMonthlyRate(toTwoDecimalPlaces(calculateDetail
                .getHourlyRateUsed()
                .multiply(BigDecimal.valueOf(calculateDetail.getTimeInHours()))
                .multiply(BigDecimal.valueOf(weekNumber))
                .divide(BigDecimal.valueOf(12), 2, RoundingMode.UP)));

        if (H7.equals(room.getFrequency())) {
            calculateDetail.setSurface(0);
        }

        log.info(
                "--- Monthly Rate: {} - Time in hours: {} - Week number: {}",
                calculateDetail.getMonthlyRate(),
                calculateDetail.getTimeInHours(),
                calculateDetail.getWeekNumber());

        return calculateDetail;
    }

    private double calculateTimeInHours(RoomDto room, BuildingDto building, DetermineCadenceDto cadenceDto) {
        if (cadenceDto.getFixedTimeInHours() != null) {
            return cadenceDto.getFixedTimeInHours();
        }

        if (cadenceDto.getCadence() == null) {
            throw new CalculateException(
                    CALCULATE_NO_CADENCE_FOR_TIME_HOURS,
                    "roomName",
                    room.getName(),
                    "buildingName",
                    building.getName());
        }

        double calculatedTime = room.getSurface() / (double) cadenceDto.getCadence();
        if (cadenceDto.getMinTimeInHours() != null) {
            log.info(
                    "--- Calculated time: {} hours, Minimum time: {} hours",
                    calculatedTime,
                    cadenceDto.getMinTimeInHours());
            return Math.max(calculatedTime, cadenceDto.getMinTimeInHours());
        }

        return calculatedTime;
    }

    private boolean isRoomTypeValidForBuilding(SectorDTO sectorDTO, BuildingDto building, RoomDto room) {
        AbstractSectorProperties.ConfigurationBuildingType buildingType = sectorService
                .getSectorProperties(sectorDTO.name())
                .getBuildingsType()
                .get(building.getBuildingType());

        if (buildingType == null || !buildingType.getRoomsType().contains(room.getType())) {
            log.warn(
                    "No Room type found for building type: {} and room type: {}",
                    building.getBuildingType(),
                    room.getType());
            return false;
        }

        return true;
    }

    private int getCustomWeekNumber(RoomDto room, BuildingDto building) {
        if (room.getAdditionalProperties() != null) {
            return room.getAdditionalProperties().stream()
                    .filter(prop -> prop.getKey().equals(AdditionalProperties.CUSTOM_WEEK_NUMBER.name()))
                    .findFirst()
                    .map(prop -> Strategy.formatToInt(prop.getValue(), room, building))
                    .orElse(52);
        }
        return 52;
    }

    private FrequencyDTO getBiggestFrequencyFromSimulateDTO(SimulateDTO simulateDTO) {
        return getBiggestFrequency(simulateDTO.getBuildings().stream()
                .flatMap(building -> building.getRooms().stream())
                .toList());
    }

    private FrequencyDTO getBiggestFrequency(List<RoomDto> roomDtos) {
        return roomDtos.stream()
                .map(RoomDto::getFrequency)
                .max(Comparator.comparingDouble(FrequencyDTO::getValue))
                .orElse(H1);
    }
}
