package fr.asr.calculator.service.impl;

import static fr.asr.calculator.exception.ExceptionType.EXPORT_GENERATE_ERROR;
import static fr.asr.calculator.util.NumberUtil.toTwoDecimalPlaces;

import fr.asr.calculator.data.dto.HotelZoneDataDTO;
import fr.asr.calculator.data.dto.enumeration.MultiPurposeFrequencyDTO;
import fr.asr.calculator.data.dto.request.SubmitDTO;
import fr.asr.calculator.data.dto.response.CalculateDTO;
import fr.asr.calculator.data.entity.Estimate;
import fr.asr.calculator.exception.functionnal.ExportException;
import fr.asr.calculator.service.ExportService;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellCopyPolicy;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Slf4j
public class ExportServiceImpl implements ExportService {

    private static final int SHEET_INDEX_TERTIARY = 2;
    private static final int SHEET_INDEX_HOTEL = 1;
    private static final int SHEET_INDEX_MULTI_PURPOSE = 0;

    private final byte[] templateBytes;

    public ExportServiceImpl() throws IOException {
        try (InputStream in = getClass().getResourceAsStream("/template/Export3.xlsx")) {
            templateBytes = in.readAllBytes();
        }
    }

    @Override
    public byte[] process(Estimate estimate, SubmitDTO submitDTO, CalculateDTO calculateDTO) {
        try (XSSFWorkbook workbook = new XSSFWorkbook(new ByteArrayInputStream(cloneTemplate()));
                ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

            this.processMultiPurpose(workbook, estimate, calculateDTO);
            this.processTertiary(workbook, estimate, submitDTO, calculateDTO);
            this.processHotel(workbook, estimate, submitDTO, calculateDTO);

            workbook.write(bos);
            return bos.toByteArray();
        } catch (Exception e) {
            throw new ExportException(EXPORT_GENERATE_ERROR, e);
        }
    }

    public void processTertiary(
            XSSFWorkbook workbook, Estimate estimate, SubmitDTO submitDTO, CalculateDTO calculateDTO) {
        if (calculateDTO.getBuildings() == null || calculateDTO.getBuildings().isEmpty()) {
            return;
        }

        List<CalculateDTO.CalculateBuilding> buildings = calculateDTO.getBuildings();
        XSSFSheet templateSheet = workbook.getSheetAt(SHEET_INDEX_TERTIARY);
        String templateSheetName = workbook.getSheetName(SHEET_INDEX_TERTIARY);

        int templateSheetIndex = workbook.getSheetIndex(templateSheet);
        List<XSSFSheet> buildingSheets = new ArrayList<>();
        buildingSheets.add(templateSheet);
        for (int i = 1; i < buildings.size(); i++) {
            buildingSheets.add(workbook.cloneSheet(templateSheetIndex));
        }

        for (int buildingIndex = 0; buildingIndex < buildings.size(); buildingIndex++) {
            CalculateDTO.CalculateBuilding building = buildings.get(buildingIndex);
            XSSFSheet sheet = buildingSheets.get(buildingIndex);

            // rename sheet
            String buildingName = building.getName();
            String sheetName = (buildingName == null || buildingName.isBlank())
                    ? templateSheetName
                    : buildingName + " - " + templateSheetName;
            workbook.setSheetName(workbook.getSheetIndex(sheet), sheetName);

            // fill data
            getComponent(sheet, 'B', 2).setCellValue(estimate.getCompanyName());

            CalculateDTO.AggregateResult aggregateResult = calculateDTO.getAggregateResult();
            getComponent(sheet, 'B', 14).setCellValue(aggregateResult.getSurfaceSum());
            getComponent(sheet, 'D', 14)
                    .setCellValue(calculateDTO.getBiggestFrequency().getValue());
            getComponent(sheet, 'E', 14).setCellValue(submitDTO.getHourlyRate().doubleValue());

            int rowIndex = 19;
            for (CalculateDTO.CalculateDetail room : building.getDetails()) {
                if (rowIndex >= 53) {
                    duplicateRowBelow(sheet, rowIndex + 1);
                }

                getComponent(sheet, 'B', rowIndex).setCellValue(building.getName());
                getComponent(sheet, 'D', rowIndex).setCellValue(room.getName());
                getComponent(sheet, 'E', rowIndex).setCellValue(room.getSurface());
                getComponent(sheet, 'F', rowIndex)
                        .setCellValue(room.getFrequencyDTO().getValue());

                if (room.getRemarks() != null) {
                    getComponent(sheet, 'G', rowIndex).setCellValue(room.getRemarks());
                }

                if (room.getCadence() != null) {
                    getComponent(sheet, 'K', rowIndex).setCellValue(room.getCadence());
                }

                // ça override pas
                getComponent(sheet, 'L', rowIndex).setCellFormula("ROUND(E" + (rowIndex) + "/K" + (rowIndex) + ",2)");
                getComponent(sheet, 'M', rowIndex).setCellFormula("ROUND(L" + (rowIndex) + "*F" + (rowIndex) + ",2)");
                getComponent(sheet, 'N', rowIndex)
                        .setCellFormula("ROUND(M" + (rowIndex) + "*" + room.getWeekNumber() + "/12,2)");
                getComponent(sheet, 'O', rowIndex).setCellFormula("ROUND(N" + (rowIndex) + "*E$14,2)");

                rowIndex++;
            }
        }
    }

    public void processHotel(XSSFWorkbook workbook, Estimate estimate, SubmitDTO submitDTO, CalculateDTO calculateDTO) {
        XSSFSheet sheet = workbook.getSheetAt(SHEET_INDEX_HOTEL);

        HotelZoneDataDTO input = submitDTO.getHotelZoneData();
        CalculateDTO.HotelCalculationResult result = calculateDTO.getHotelCalculationResult();

        if (result == null || input == null) {
            workbook.removeSheetAt(SHEET_INDEX_HOTEL);
            return;
        }

        getComponent(sheet, 'A', 1).setCellValue(estimate.getCompanyName());

        getComponent(sheet, 'B', 15).setCellValue(input.getGlobalRate());
        getComponent(sheet, 'B', 16).setCellValue(input.getOccupancyRate());
        getComponent(sheet, 'B', 17).setCellValue(result.getNumberOfRooms());
        getComponent(sheet, 'B', 18).setCellValue(result.getMinimumPackage().getNumberOfRoomsPerDay());

        int delayIndex = 0;
        int i = 21;
        for (HotelZoneDataDTO.HotelRoomType roomType : input.getRoomTypes()) {
            duplicateRowBelow(sheet, i);
            delayIndex++;

            getComponent(sheet, 'A', i).setCellValue(roomType.getName());
            getComponent(sheet, 'C', i).setCellValue(roomType.getRecoucheRate());
            // passer ça dans le service de calcule
            getComponent(sheet, 'D', i).setCellValue((roomType.getRecoucheRate() / 60) * input.getGlobalRate());
            getComponent(sheet, 'E', i).setCellValue(roomType.getClearanceRate());
            // passer ça dans le service de calcule
            getComponent(sheet, 'F', i).setCellValue((roomType.getClearanceRate() / 60) * input.getGlobalRate());

            HotelZoneDataDTO.HotelRoomCount roomCount = input.getRoomCounts().get(roomType.getId());
            if (roomCount != null) {
                getComponent(sheet, 'B', i).setCellValue(roomCount.getCount());

                if (Boolean.TRUE.equals(roomCount.getHasKitchen())) {
                    getComponent(sheet, 'G', i).setCellFormula(null);
                    getComponent(sheet, 'G', i).setCellValue(roomCount.getKitchenTime());
                    // passer ça dans le service de calcule
                    getComponent(sheet, 'H', i).setCellFormula(null);
                    getComponent(sheet, 'H', i).setCellValue(roomCount.getKitchenTime() * input.getGlobalRate());
                }
            }

            i++;
        }

        getComponent(sheet, 'A', 26 + delayIndex)
                .setCellValue(result.getMinimumPackage().getMinimumPackagePrice());
        getComponent(sheet, 'B', 26 + delayIndex)
                .setCellValue(result.getMinimumPackage().getMinimumRoomsPerMonth());

        HotelZoneDataDTO.HotelManagementPackage managementPackage = input.getManagementPackage();
        if (managementPackage != null) {
            if (Boolean.TRUE.equals(managementPackage.getPremiereFemmeDeChambre())) {
                getComponent(sheet, 'B', 30 + delayIndex).setCellValue("X");

                getComponent(sheet, 'D', 30 + delayIndex)
                        .setCellValue(result.getManagementPackage().getTotalTime());
                // passer ça dans le service de calcule
                getComponent(sheet, 'E', 30 + delayIndex)
                        .setCellValue(toTwoDecimalPlaces(
                                result.getManagementPackage().getTotalTime() / managementPackage.getDaysPerWeek()));
            } else {
                getComponent(sheet, 'C', 30 + delayIndex).setCellValue("X");
            }

            if (Boolean.TRUE.equals(managementPackage.getStatutEncadrant())) {
                getComponent(sheet, 'B', 31 + delayIndex).setCellValue("X");

                getComponent(sheet, 'D', 31 + delayIndex)
                        .setCellValue(result.getManagementPackage().getTotalTime());
                // passer ça dans le service de calcule
                getComponent(sheet, 'E', 31 + delayIndex)
                        .setCellValue(toTwoDecimalPlaces(
                                result.getManagementPackage().getTotalTime() / managementPackage.getDaysPerWeek()));
            } else {
                getComponent(sheet, 'C', 31 + delayIndex).setCellValue("X");
            }

            getComponent(sheet, 'A', 35 + delayIndex)
                    .setCellValue(result.getManagementPackage().getManagementPackagePrice());
            getComponent(sheet, 'B', 35 + delayIndex)
                    .setCellValue(result.getManagementPackage().getAllocationTime());
            getComponent(sheet, 'C', 35 + delayIndex)
                    .setCellValue(result.getManagementPackage().getControlTime());
            getComponent(sheet, 'D', 35 + delayIndex)
                    .setCellValue(result.getManagementPackage().getLaundryManagementTime());
            getComponent(sheet, 'E', 35 + delayIndex)
                    .setCellValue(result.getManagementPackage().getTrainingTime());
        }
    }

    public void processMultiPurpose(XSSFWorkbook workbook, Estimate estimate, CalculateDTO calculateDTO) {
        XSSFSheet sheet = workbook.getSheetAt(SHEET_INDEX_MULTI_PURPOSE);

        CalculateDTO.MultiPurposeCalculationResult result = calculateDTO.getMultiPurposeCalculationResult();

        getComponent(sheet, 'B', 2).setCellValue(estimate.getCompanyName());
        getComponent(sheet, 'B', 12).setCellValue(result.getTotalSurface());

        int i = 17;
        for (CalculateDTO.MultiPurposeCalculationDetail detail : result.getDetails()) {
            if (i >= 46) {
                duplicateRowBelow(sheet, i + 1);
            }

            getComponent(sheet, 'D', i).setCellValue(detail.getName());
            getComponent(sheet, 'E', i).setCellValue(detail.getSurface());

            if (detail.getFrequency() == MultiPurposeFrequencyDTO.UNIQUE) {
                getComponent(sheet, 'F', i).setCellValue("UNIQUE");
            } else {
                getComponent(sheet, 'F', i).setCellValue((detail.getFrequency().getValue() / 52D));
            }

            if (detail.getRemarks() != null) {
                getComponent(sheet, 'G', i).setCellValue(detail.getRemarks());
            }

            if (detail.isUsedTravelCosts()) {
                getComponent(sheet, 'K', i).setCellValue(detail.getTravelCosts());
            }

            if (detail.getCadence() != null) {
                getComponent(sheet, 'L', i).setCellValue(detail.getCadence());
            }

            // ça override pas
            getComponent(sheet, 'M', i).setCellFormula(null);
            getComponent(sheet, 'M', i).setCellValue(detail.getTimeInHours());
            getComponent(sheet, 'N', i).setCellFormula(null);
            getComponent(sheet, 'N', i).setCellValue(detail.getPrice());

            i++;
        }
    }

    private XSSFCell getComponent(XSSFSheet sheet, char letter, int row) {
        XSSFCell comment = sheet.getRow(row - 1).getCell(letter - 65);
        if (comment == null) {
            throw new ExportException(EXPORT_GENERATE_ERROR);
        }

        return comment;
    }

    private void duplicateRowBelow(XSSFSheet sheet, int srcRowIdx) {
        CellCopyPolicy policy = new CellCopyPolicy();
        policy.setCopyCellValue(false);

        sheet.shiftRows(srcRowIdx, sheet.getLastRowNum(), 1);
        sheet.copyRows(srcRowIdx - 1, srcRowIdx - 1, srcRowIdx, policy);
    }

    private byte[] cloneTemplate() {
        return templateBytes.clone();
    }
}
