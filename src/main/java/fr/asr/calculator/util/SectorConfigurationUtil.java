package fr.asr.calculator.util;

import fr.asr.calculator.configuration.properties.sector.AbstractSectorProperties;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class SectorConfigurationUtil {

    private static final String EMPTY_CADENCE = "EMPTY";

    public static void verifyConfiguration(AbstractSectorProperties properties) {
        // Verify that all room types have a valid cadence
        properties.getRoomsType().forEach((roomTypeName, roomType) -> {
            String cadenceName = roomType.getCadence();
            if (!EMPTY_CADENCE.equals(cadenceName) && !properties.getCadences().containsKey(cadenceName)) {
                throw new IllegalArgumentException(
                        "Cadence " + cadenceName + " not found for room type " + roomTypeName);
            }
        });

        // Verify that all building types reference valid room types
        properties.getBuildingsType().forEach((buildingTypeName, buildingType) -> {
            buildingType.getRoomsType().forEach(roomTypeName -> {
                if (!properties.getRoomsType().containsKey(roomTypeName)) {
                    throw new IllegalArgumentException(
                            "Room type " + roomTypeName + " not found in building type " + buildingTypeName);
                }
            });
        });

        // Verify that the frequencies are valid
        properties.getCadences().forEach((cadenceName, cadence) -> {
            cadence.getFrequencies().forEach((frequencyName, frequency) -> {
                if (frequency.getFixedTimeInHours() != null
                        && (frequency.getMinTimeInHours() != null
                                || frequency.getCarpetedFloor() != null
                                || frequency.getWashableFloor() != null)) {
                    throw new IllegalArgumentException("Frequency " + frequencyName + " in cadence " + cadenceName
                            + " must have only fixedTimeInHours defined.");
                }

                if (frequency.getMinTimeInHours() != null
                        && (frequency.getCarpetedFloor() == null || frequency.getWashableFloor() == null)) {
                    throw new IllegalArgumentException("Frequency " + frequencyName + " in cadence " + cadenceName
                            + " must have carpeted and washable cadence.");
                }
            });
        });
    }
}
