package pha.ics.values;

import org.junit.Test;
import pha.ics.AbstractTest;
import pha.ics.PropertyParameter;

import java.util.List;

import static org.junit.Assert.*;

public class DateListTest extends AbstractTest {

    @Test
    public void testToFormattedStringWithDateTimeFloating() {

        String dateTime = "20111219T140000";
        DateList dateList = new DateList(dateTime, null);

        String formattedString = dateList.getValueAsString();

        assertEquals("Not equal", dateTime, formattedString);

        DateObject firstDate = dateList.getDateList().get(0);
        assertFalse("Should not be UTC", firstDate.isUTC());
        assertTrue("No time", firstDate.hasTimePart());
    }

    @Test
    public void testToFormattedStringWithDateTimeUTC() {

        String dateTime = "20111219T100030Z";
        DateList dateList = new DateList(dateTime, null);

        String formattedString = dateList.getValueAsString();

        assertEquals("Not equal", dateTime, formattedString);
        assertTrue("Should be UTC", dateList.getDateList().get(0).isUTC());
    }

    @Test
    public void testToFormattedStringWithDayOnly() {

        String dateTime = "20001019";
        List<PropertyParameter> params = createParameters("VALUE=DATE");
        DateList dateList = new DateList(dateTime, params);

        String formattedString = dateList.getValueAsString();

        assertEquals("Not equal", dateTime, formattedString);
        DateObject firstDate = dateList.getDateList().get(0);
        assertFalse("Should not be UTC", firstDate.isUTC());
        assertFalse("Should have no time", firstDate.hasTimePart());
    }

    @Test
    public void testToFormattedStringThreeDays() {

        String dateTime = "20001019,20120410,20130101";
        List<PropertyParameter> params = createParameters("VALUE=DATE");
        DateList dateList = new DateList(dateTime, params);

        String formattedString = dateList.getValueAsString();

        String expectedOutput = dateTime;
        assertEquals("Not equal", expectedOutput, formattedString);
        List<DateObject> dateObjectses = dateList.getDateList();
        String[] expected = dateTime.split(",");
        DateObject firstDate = dateObjectses.get(0);
        assertEquals("Not equal 0", expected[0], firstDate.toString());
        DateObject secondDate = dateObjectses.get(1);
        assertEquals("Not equal, 1", expected[1], secondDate.toString());
        DateObject thirdDate = dateObjectses.get(2);
        assertEquals("Not equal, 2", expected[2], thirdDate.toString());

    }

    @Test
    public void testToFormattedStringTwoDates() {

        String dateTime = "20111219T100030Z,20111219T100030Z";
        DateList dateList = new DateList(dateTime, null);

        String formattedString = dateList.getValueAsString();

        String expectedOutput = dateTime;
        assertEquals("Not equal", expectedOutput, formattedString);
        List<DateObject> dateObjectses = dateList.getDateList();
        String[] expected = dateTime.split(",");
        DateObject firstDate = dateObjectses.get(0);
        assertEquals("Not equal 0", expected[0], firstDate.toString());
        DateObject secondDate = dateObjectses.get(1);
        assertEquals("Not equal, 1", expected[1], secondDate.toString());

    }
}