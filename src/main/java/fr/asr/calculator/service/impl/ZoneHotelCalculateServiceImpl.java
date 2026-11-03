package fr.asr.calculator.service.impl;

import static fr.asr.calculator.exception.ExceptionType.CALCULATE_NO_ROOMS_PROVIDED;
import static fr.asr.calculator.util.NumberUtil.toTwoDecimalPlaces;

import fr.asr.calculator.data.dto.HotelZoneDataDTO;
import fr.asr.calculator.data.dto.response.CalculateDTO;
import fr.asr.calculator.exception.functionnal.CalculateException;
import fr.asr.calculator.service.ZoneHotelCalculateService;
import fr.asr.calculator.util.HoursUtil;
import java.util.Comparator;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ZoneHotelCalculateServiceImpl implements ZoneHotelCalculateService {

    @Override
    public CalculateDTO.HotelCalculationResult calculateHotelZone(HotelZoneDataDTO hotelZoneDataDTO) {
        log.info("Calculating hotel zone with data ...");

        double numberOfRooms = hotelZoneDataDTO.getRoomCounts().values().stream()
                .mapToInt(HotelZoneDataDTO.HotelRoomCount::getCount)
                .sum();

        return CalculateDTO.HotelCalculationResult.builder()
                .minimumPackage(calculateMinimumPackage(hotelZoneDataDTO))
                .managementPackage(calculateManagementPackage(hotelZoneDataDTO))
                .numberOfRooms(numberOfRooms)
                .build();
    }

    private CalculateDTO.HotelMinimumPackage calculateMinimumPackage(HotelZoneDataDTO hotelZoneDataDTO) {
        Optional<HotelZoneDataDTO.HotelRoomType> optionalHotelRoomType = getRoomWithMaxCount(hotelZoneDataDTO);
        if (optionalHotelRoomType.isEmpty()) {
            throw new CalculateException(CALCULATE_NO_ROOMS_PROVIDED);
        }

        HotelZoneDataDTO.HotelRoomType biggestRoomType = optionalHotelRoomType.get();
        HotelZoneDataDTO.HotelRoomCount biggestRoomCount =
                hotelZoneDataDTO.getRoomCounts().get(biggestRoomType.getId());

        double numberOfRoomsPerMonth = (biggestRoomCount.getCount()
                        * (hotelZoneDataDTO.getOccupancyRate() / 100)
                        * 0.5
                        * hotelZoneDataDTO.getOpeningDaysPerWeek()
                        * 52)
                / 12;

        double numberOfRoomsPerDay = numberOfRoomsPerMonth / 30;

        double roomUnitPrice = getClearanceUnitPrice(biggestRoomType, hotelZoneDataDTO.getGlobalRate());
        double finalResult = numberOfRoomsPerMonth * roomUnitPrice;

        log.info(
                "-- Calculated minimum package: numberOfRoomsPerMonth={}, roomUnitPrice={}, finalResult={}",
                numberOfRoomsPerMonth,
                roomUnitPrice,
                finalResult);
        CalculateDTO.HotelMinimumPackage minimumPackage = new CalculateDTO.HotelMinimumPackage();
        minimumPackage.setMinimumRoomsPerMonth(toTwoDecimalPlaces(numberOfRoomsPerMonth));
        minimumPackage.setUnitPriceRoom(toTwoDecimalPlaces(roomUnitPrice));
        minimumPackage.setRoomUsedId(biggestRoomType.getId());
        minimumPackage.setMinimumPackagePrice(toTwoDecimalPlaces(finalResult));
        minimumPackage.setNumberOfRoomsPerDay(toTwoDecimalPlaces(numberOfRoomsPerDay));
        return minimumPackage;
    }

    private CalculateDTO.HotelManagementPackage calculateManagementPackage(HotelZoneDataDTO hotelZoneDataDTO) {
        HotelZoneDataDTO.HotelManagementPackage managementPackage = hotelZoneDataDTO.getManagementPackage();

        double numberOfRooms = hotelZoneDataDTO.getRoomCounts().values().stream()
                        .mapToInt(HotelZoneDataDTO.HotelRoomCount::getCount)
                        .sum()
                * (hotelZoneDataDTO.getOccupancyRate() / 100);

        double timeRepartition = HoursUtil.roundUpToQuarter(((0.5 * managementPackage.getDaysPerWeek()) * 52) / 12);

        double timeControl = HoursUtil.roundUpToQuarter(((managementPackage.getControlPercentage() / 100)
                        * numberOfRooms
                        * (hotelZoneDataDTO.getClassification() > 3 ? (4.0 / 60) : 0.05)
                        * 52)
                / 12);

        double timeLaundryManagement = 0;
        if (managementPackage.getLaundryManagementHours() != null
                && managementPackage.getLaundryDaysPerWeek() != null) {
            timeLaundryManagement = HoursUtil.roundUpToQuarter(
                    (managementPackage.getLaundryManagementHours() * managementPackage.getLaundryDaysPerWeek() * 52)
                            / 12);
        }

        double timeForTraining = HoursUtil.roundUpToQuarter(
                ((numberOfRooms < 50 ? 0.5 : (numberOfRooms <= 100 ? 0.75 : 1.0)) * 52) / 12);

        double totalTime = timeRepartition + timeControl + timeLaundryManagement + timeForTraining;
        double price = (totalTime * managementPackage.getHourlyRate() * 52) / 12;

        log.info(
                "-- Calculated management package: timeRepartition={}, timeControl={}, "
                        + "timeLaundryManagement={}, timeForTraining={}, price={}",
                timeRepartition,
                timeControl,
                timeLaundryManagement,
                timeForTraining,
                price);
        CalculateDTO.HotelManagementPackage managementPackageResult = new CalculateDTO.HotelManagementPackage();
        managementPackageResult.setAllocationTime(toTwoDecimalPlaces(timeRepartition));
        managementPackageResult.setControlTime(toTwoDecimalPlaces(timeControl));
        managementPackageResult.setLaundryManagementTime(toTwoDecimalPlaces(timeLaundryManagement));
        managementPackageResult.setTrainingTime(toTwoDecimalPlaces(timeForTraining));
        managementPackageResult.setManagementPackagePrice(toTwoDecimalPlaces(price));
        managementPackageResult.setTotalTime(toTwoDecimalPlaces(roundTotalTime(totalTime)));
        return managementPackageResult;
    }

    private Optional<HotelZoneDataDTO.HotelRoomType> getRoomWithMaxCount(HotelZoneDataDTO hotelZoneDataDTO) {
        return hotelZoneDataDTO.getRoomCounts().entrySet().stream()
                .max(Comparator.comparingInt(entry -> entry.getValue().getCount()))
                .flatMap(entry -> hotelZoneDataDTO.getRoomTypes().stream()
                        .filter(roomType -> roomType.getId().equals(entry.getKey()))
                        .findFirst());
    }

    private double getClearanceUnitPrice(HotelZoneDataDTO.HotelRoomType biggestRoomType, Double globalRate) {
        return (biggestRoomType.getClearanceRate() / 60D) * globalRate;
    }

    private double roundTotalTime(double totalTime) {
        return Math.floor(totalTime) == 34 ? 35 : totalTime;
    }
}
