package fr.asr.calculator.data.dto;

import jakarta.annotation.Nullable;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetermineCadenceDto {

    @Nullable
    protected Double fixedTimeInHours;

    @Nullable
    protected Double minTimeInHours;

    @Nullable
    protected Integer cadence;
}
