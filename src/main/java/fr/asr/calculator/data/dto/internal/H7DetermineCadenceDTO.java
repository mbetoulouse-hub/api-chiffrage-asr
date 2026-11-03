package fr.asr.calculator.data.dto.internal;

import fr.asr.calculator.data.dto.DetermineCadenceDto;

public class H7DetermineCadenceDTO extends DetermineCadenceDto {

    public H7DetermineCadenceDTO(DetermineCadenceDto determineCadenceDto) {
        this.fixedTimeInHours = determineCadenceDto.getFixedTimeInHours() != null
                ? determineCadenceDto.getFixedTimeInHours() * 1.2
                : null;
        this.minTimeInHours =
                determineCadenceDto.getMinTimeInHours() != null ? determineCadenceDto.getMinTimeInHours() * 1.2 : null;
        this.cadence = determineCadenceDto.getCadence() != null
                ? (int) (determineCadenceDto.getCadence() + (determineCadenceDto.getCadence() * 1.2))
                : null;
    }
}
