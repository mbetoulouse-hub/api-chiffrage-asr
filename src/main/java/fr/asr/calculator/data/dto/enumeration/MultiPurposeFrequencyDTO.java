package fr.asr.calculator.data.dto.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MultiPurposeFrequencyDTO {
    UNIQUE(-1),
    H1(1 * 52D),

    M(0.23 * 52D),
    M2(0.46 * 52D),
    BIMESTRIEL(0.115 * 52D),
    T(0.077 * 52D),
    S(0.0385 * 52D),
    A(0.0192 * 52D),

    H2(2 * 52D),
    H3(3 * 52D),
    H4(4 * 52D),
    H5(5 * 52D),
    H6(6 * 52D),
    H7(7 * 52D),
    ;

    private final double value;
}
