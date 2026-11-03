package fr.asr.calculator.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class HoursUtil {

    private static final double STEP = 0.25;

    public static double roundUpToQuarter(double hour) {
        return Math.ceil(hour / STEP) * STEP;
    }

    public static double roundUpToHour(double hour) {
        return Math.ceil(hour);
    }

    public static double roundUpToHalfDay(double hour) {
        return hour <= 4 ? 4 : Math.ceil(hour / 7) * 7;
    }
}
