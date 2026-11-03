package fr.asr.calculator.data.dto.internal;

import fr.asr.calculator.configuration.properties.sector.AbstractSectorProperties;

public class H7ConfigFrequency extends AbstractSectorProperties.ConfigurationFrequency {
    public H7ConfigFrequency(AbstractSectorProperties.ConfigurationFrequency configurationFrequency) {
        Integer washableFloor = configurationFrequency.getWashableFloor();
        Integer carpetedFloor = configurationFrequency.getCarpetedFloor();
        Double minTimeInHours = configurationFrequency.getMinTimeInHours();
        Double fixedTimeInHours = configurationFrequency.getFixedTimeInHours();

        this.washableFloor = washableFloor != null ? (int) (washableFloor + (washableFloor * 1.2)) : null;
        this.carpetedFloor = carpetedFloor != null ? (int) (carpetedFloor + (carpetedFloor * 1.2)) : null;
        this.minTimeInHours = minTimeInHours != null ? (minTimeInHours * 1.2) : null;
        this.fixedTimeInHours = fixedTimeInHours != null ? (fixedTimeInHours * 1.2) : null;
    }
}
