package fr.asr.calculator.data.dto.enumeration;

import java.util.Arrays;
import java.util.Optional;

public enum SectorDTO {
    TERTIAIRE,
    LIFE_BASED,
    COPROPRIETES,
    INDUSTRIE,
    AGRO,
    BIO,
    MEDICAL,
    CRECHE,
    SCHOOL,
    CONCESSIONS,
    GSS_MARKET,
    SPORT,
    HOTEL,
    RESTAURANT,
    VETERINAIRE,
    ;

    public static Optional<SectorDTO> from(String value) {
        return Arrays.stream(values())
                .filter(sector -> sector.name().equalsIgnoreCase(value))
                .findFirst();
    }
}
