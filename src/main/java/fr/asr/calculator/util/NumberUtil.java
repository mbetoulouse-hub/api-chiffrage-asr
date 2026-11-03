package fr.asr.calculator.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.experimental.UtilityClass;

@UtilityClass
public class NumberUtil {

    public static double toTwoDecimalPlaces(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public static BigDecimal toTwoDecimalPlaces(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }
}
