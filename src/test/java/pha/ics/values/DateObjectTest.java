package pha.ics.values;

import org.junit.Test;
import pha.ics.PropertyParameter;
import pha.ics.io.read.DurationBuilder;
import sun.util.calendar.ZoneInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import static org.junit.Assert.*;

/**
 * Created by paul on 22/07/16.
 */
public class DateObjectTest {

    @Test
    public void parseDateWithColon() throws Exception {

        String dateTime = "00:20091019T101030";

        DateObject dateObject = new DateObject(dateTime, null, false);


        assertEquals("Incorrect result", "20091019T101030", dateObject.toString());
    }

    @Test
    public void addDurationOfOneDay() throws Exception {
        Duration duration = DurationBuilder.build("P1D");

        String dateTime = "20001019";

        DateObject dateObject = new DateObject(dateTime, null, true);

        DateObject result = dateObject.add(duration);

        assertEquals("Incorrect result", "20001020", result.toString());
    }

    @Test
    public void addDurationOfTwoWeeks() throws Exception {
        Duration duration = DurationBuilder.build("P2W");

        String dateTime = "20001009";

        DateObject dateObject = new DateObject(dateTime,  null, true);

        DateObject result = dateObject.add(duration);

        assertEquals("Incorrect result", "20001023", result.toString());
    }

    @Test
    public void testGetTimePartReturnsTheCorrectValue() {
        String dateTime = "20001009T123409";

        DateObject dateObject = new DateObject(dateTime,  null, false);

        assertEquals("Incorrect date-time", "20001009", dateObject.getDayAsString());
        assertEquals("Incorrect date-time", "123409", dateObject.getTimeAsString());

    }

    @Test
    public void testSameDayReturnsTrueForSameDayAndSameTimeZone() {

        DateObject dateObject = new DateObject("20001009T123409",  null, false);
        DateObject otherDate = new DateObject("20001009T093000", null, false);

        assertTrue("Should be same day", dateObject.isSameDay(otherDate));

    }

    @Test
    public void testSameDayReturnsTrueForUTCDay() {

        DateObject dateObject = new DateObject("20001009T123409",  null, false);
        DateObject otherDate = new DateObject("20001009T093000Z", null, false);

        assertTrue("Should be same day", dateObject.isSameDay(otherDate));

    }
    @Test
    public void testSameDayReturnsTrueForDifferentTimeZones() {

        TimeZone timeZone = ZoneInfo.getTimeZone("Europe/Paris");
        DateObject dateObject = new DateObject("20001009T123409",  timeZone, false);
        DateObject otherDate = new DateObject("20001009T093000Z", null, false);

        assertTrue("Should be same day", dateObject.isSameDay(otherDate));

    }

    @Test
    public void testEqualReturnsTrueForDifferentTimeZonesInSummer() {

        TimeZone timeZone = ZoneInfo.getTimeZone("Europe/London");
        DateObject dateObject = new DateObject("19941018T190000",  timeZone, false);
        DateObject otherDate = new DateObject("19941018T180000Z", null, false);

        assertTrue("Should be the same", dateObject.equals(otherDate));

    }

    @Test
    public void testDifferenceInSeconds30Sec() {

        DateObject dateObject = new DateObject("19941018T190000",  null, false);
        DateObject otherDate = new DateObject("19941018T190030", null, false);

        assertEquals("Should be the same", 30, dateObject.getDifferenceInSeconds(otherDate));

    }

    @Test
    public void testDifferenceInSecondsNeg1hr() {

        DateObject dateObject = new DateObject("19941018T200000",  null, false);
        DateObject otherDate = new DateObject("19941018T190000", null, false);

        assertEquals("Should be the same", -3600, dateObject.getDifferenceInSeconds(otherDate));

    }

    @Test
    public void testDifferenceInSecondsWhenSame() {

        DateObject dateObject = new DateObject("19941018T190000",  null, false);
        DateObject otherDate = new DateObject("19941018T190000", null, false);

        assertEquals("Should be the same", 0, dateObject.getDifferenceInSeconds(otherDate));

    }

    @Test
    public void testDifferenceInDays3Days() {

        DateObject dateObject = new DateObject("19941018T190000",  null, false);
        DateObject otherDate = new DateObject("19941021T190000", null, false);

        assertEquals("Should be the same", 3, dateObject.getDifferenceInDays(otherDate));

    }

    @Test
    public void testDifferenceInDaysNeg365() {

        DateObject dateObject = new DateObject("19951018T200000",  null, false);
        DateObject otherDate = new DateObject("19941018T200000", null, false);

        assertEquals("Should be the same", -365, dateObject.getDifferenceInDays(otherDate));

    }

    @Test
    public void testDifferenceInDaysWhenSame() {

        DateObject dateObject = new DateObject("19941018T190000",  null, false);
        DateObject otherDate = new DateObject("19941018T190000", null, false);

        assertEquals("Should be the same", 0, dateObject.getDifferenceInDays(otherDate));

    }
}