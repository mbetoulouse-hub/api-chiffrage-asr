package fr.asr.calculator.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class HoursUtilTest {

    @Test
    void testRoundUpToQuarter() {
        assertEquals(0.0, HoursUtil.roundUpToQuarter(0.0), 0.0001);
        assertEquals(1.25, HoursUtil.roundUpToQuarter(1.12), 0.0001);
        assertEquals(1.50, HoursUtil.roundUpToQuarter(1.33), 0.0001);
        assertEquals(1.75, HoursUtil.roundUpToQuarter(1.65), 0.0001);
        assertEquals(2.00, HoursUtil.roundUpToQuarter(1.88), 0.0001);

        assertEquals(1.25, HoursUtil.roundUpToQuarter(1.25), 0.0001);
        assertEquals(1.50, HoursUtil.roundUpToQuarter(1.50), 0.0001);
        assertEquals(1.75, HoursUtil.roundUpToQuarter(1.75), 0.0001);
        assertEquals(2.00, HoursUtil.roundUpToQuarter(2.00), 0.0001);
    }

    @Test
    void testRoundUpToHour() {
        assertEquals(1.0, HoursUtil.roundUpToHour(0.1));
        assertEquals(1.0, HoursUtil.roundUpToHour(0.5));
        assertEquals(2.0, HoursUtil.roundUpToHour(1.1));
        assertEquals(3.0, HoursUtil.roundUpToHour(2.5));
        assertEquals(3.0, HoursUtil.roundUpToHour(3.0));
    }

    @Test
    void testRoundUpToHalfDay() {
        assertEquals(4.0, HoursUtil.roundUpToHalfDay(2.0));
        assertEquals(4.0, HoursUtil.roundUpToHalfDay(4.0));
        assertEquals(7.0, HoursUtil.roundUpToHalfDay(5.0));
        assertEquals(14.0, HoursUtil.roundUpToHalfDay(10.0));
        assertEquals(21.0, HoursUtil.roundUpToHalfDay(15.0));
    }
}
