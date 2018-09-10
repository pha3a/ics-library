package pha.ics.values;

import org.junit.Test;
import pha.ics.io.read.DurationBuilder;

import static org.junit.Assert.*;

/**
 * Created by paul on 21/07/16.
 */
public class DurationBuilderTest {

    @Test
    public void testTwoIdenticalDurationsAreEqual() throws Exception {
        Duration duration1 = DurationBuilder.build("P1D");
        Duration duration2 = DurationBuilder.build("P1D");

        assertTrue("Should be equal", duration1.equals(duration2));
    }

    @Test
    public void testTwoDifferentDurationsAreNotEqual() throws Exception {
        Duration duration1 = DurationBuilder.build("P1D");
        Duration duration2 = DurationBuilder.build("P1DT23S");

        assertFalse("Should not be equal", duration1.equals(duration2));
    }


    @Test
    public void testParseDurationReturnsDays() throws Exception {
        Duration duration = DurationBuilder.build("P2D");

        assertEquals("Incorrect days", 2, duration.getDays());
    }

    @Test
    public void testParseDurationReturnsNegativeDays() throws Exception {
        Duration duration = DurationBuilder.build("-P2D");

        assertTrue("Incorrect sign", duration.isNegative());
        assertEquals("Incorrect days", 2, duration.getDays());
    }

    @Test
    public void testParseDurationReturnsPositiveDays() throws Exception {
        Duration duration = DurationBuilder.build("+P2D");

        assertFalse("Incorrect sign", duration.isNegative());
        assertEquals("Incorrect days", 2, duration.getDays());
    }

    @Test
    public void testParseDurationReturnsWeeks() throws Exception {
        Duration duration = DurationBuilder.build("P3W");

        assertEquals("Incorrect weeks", 3, duration.getWeeks());
    }

    @Test
    public void testParseDurationReturnsTime() throws Exception {
        Duration duration = DurationBuilder.build("PT2H30M27S");

        assertEquals("Incorrect hours", 2, duration.getHours());
        assertEquals("Incorrect minutes", 30, duration.getMinutes());
        assertEquals("Incorrect seconds", 27, duration.getSeconds());
    }

    @Test
    public void testParseDurationReturnsDayTime() throws Exception {
        Duration duration = DurationBuilder.build("P3DT2H10M7S");

        assertEquals("Incorrect days", 3, duration.getDays());
        assertEquals("Incorrect hours", 2, duration.getHours());
        assertEquals("Incorrect minutes", 10, duration.getMinutes());
        assertEquals("Incorrect seconds", 7, duration.getSeconds());
    }

    @Test
    public void testCompareToReturnsNegForOtherMoreThanThis() throws Exception {
        Duration duration1 = DurationBuilder.build("P1D");
        Duration duration2 = DurationBuilder.build("P1DT23S");

        assertEquals("Should be negative", -1, duration1.compareTo(duration2));
    }

    @Test
    public void testCompareToReturnsPosForOtherLessThanThis() throws Exception {
        Duration duration1 = DurationBuilder.build("P10D");
        Duration duration2 = DurationBuilder.build("P1DT12H20M45S");

        assertEquals("Should be positive", 1, duration1.compareTo(duration2));
    }

    @Test
    public void testCompareToReturnsNegWhenThisNegative() throws Exception {
        Duration duration1 = DurationBuilder.build("-P1D");
        Duration duration2 = DurationBuilder.build("P1D");

        assertEquals("Should be negative", -1, duration1.compareTo(duration2));
    }

    @Test
    public void testCompareToReturnsZeroWhenBothSame() throws Exception {
        Duration duration1 = DurationBuilder.build("P10D");
        Duration duration2 = DurationBuilder.build("P10D");

        assertEquals("Should be zero", 0, duration1.compareTo(duration2));
    }

    @Test
    public void testParseHandlesStringWithSecondCountButNoTPrefix() throws Exception {
        Duration duration1 = DurationBuilder.build("P21600S");

        assertEquals("Should be zero", "W0D0H6M0S0", duration1.getValueAsString());
    }

    @Test
    public void testParseHandlesStringWith6HoursInSecondCountWithT() throws Exception {
        Duration duration1 = DurationBuilder.build("PT21600S");

        assertEquals("Should be zero", "W0D0H6M0S0", duration1.getValueAsString());
    }

    @Test
    public void testParseNormalizes6Hrs4Min12SecInSecondCount() throws Exception {
        Duration duration1 = DurationBuilder.build("PT21852S");

        assertEquals("Should be zero", "W0D0H6M4S12", duration1.getValueAsString());
    }

    @Test(expected = DurationBuilder.IllegalDurationException.class)
    public void testParseFailsWithEmptyParameter() throws Exception {
        Duration duration = DurationBuilder.build("");

        fail("Duration is illegal");
    }

    @Test(expected = DurationBuilder.IllegalDurationException.class)
    public void testParseFailsWithPOnlyParameter() throws Exception {
        Duration duration = DurationBuilder.build("P");

        fail("Duration is illegal");
    }

    @Test(expected = DurationBuilder.IllegalDurationException.class)
    public void testParseFailsWithTruncatedParameter() throws Exception {
        Duration duration = DurationBuilder.build("P1");

        fail("Duration is illegal");
    }

    @Test(expected = DurationBuilder.IllegalDurationException.class)
    public void testParseFailsWithTruncatedHourParameter() throws Exception {
        Duration duration = DurationBuilder.build("P1DT12H");

        fail("Duration is illegal");
    }

    @Test(expected = DurationBuilder.IllegalDurationException.class)
    public void testParseFailsWithTruncatedTimeParameter() throws Exception {
        Duration duration = DurationBuilder.build("P1DT");

        fail("Duration is illegal");
    }
}