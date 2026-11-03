package fr.asr.calculator.data.dto.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FrequencyDTO {
    H1(1),

    M(0.23),
    M2(0.46),
    BIMESTRIEL(0.115),
    T(0.077),
    S(0.0385),
    A(0.0192),

    H2(2),
    H3(3),
    H4(4),
    H5(5),
    H6(6),
    H7(7),
    ;

    private final double value;
}
