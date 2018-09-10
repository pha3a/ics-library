package pha.ics.values;

import junit.framework.TestCase;
import pha.ics.PropertyParameter;

import java.util.ArrayList;
import java.util.List;

public class DateValueTest extends TestCase {

    public void testToFormattedStringWithDateTimeFloating() throws Exception {

        String dateTime = "20111219T140000";
        DateValue dateValue = new DateValue(dateTime, null);

        String formattedString = dateValue.getValueAsString();

        assertEquals("Not equal", dateTime, formattedString);
        assertFalse("Should not be UTC", dateValue.isUTC());
        assertTrue("No time", dateValue.hasTimePart());
    }

    public void testToFormattedStringWithDateTimeUTC() throws Exception {

        String dateTime = "20111219T100030Z";
        DateValue dateValue = new DateValue(dateTime, null);

        String formattedString = dateValue.getValueAsString();

        assertEquals("Not equal", dateTime, formattedString);
        assertTrue("Should be UTC", dateValue.isUTC());
    }

    public void testToFormattedStringWithDateOnly() throws Exception {

        String dateTime = "20001019";
        List<PropertyParameter> params = new ArrayList<>();
        PropertyParameter param1 = new PropertyParameter("VALUE","DATE");
        params.add(param1);
        DateValue dateValue = new DateValue(dateTime, params);

        String formattedString = dateValue.getValueAsString();

        assertEquals("Not equal", dateTime, formattedString);
        assertFalse("Should not be UTC", dateValue.isUTC());
        assertFalse("Should have no time", dateValue.hasTimePart());
    }

    public void testEqualsWithParameter() throws Exception {

        String dateTime = "20001019";
        List<PropertyParameter> params = new ArrayList<>();
        PropertyParameter param1 = new PropertyParameter("VALUE","DATE");
        params.add(param1);
        DateValue dateValue1 = new DateValue(dateTime, params);

        List<PropertyParameter> paramsV2 = new ArrayList<>();
        PropertyParameter param2 = new PropertyParameter("VALUE","DATE");
        paramsV2.add(param2);
        DateValue dateValue2 = new DateValue(dateTime, paramsV2);
        assertTrue("Values should equal", dateValue1.equals(dateValue2));
    }

    public void testEquivalentWithTimeZoneReturnsTrue() throws Exception {

        String dateTime = "20110619T110030";
        List<PropertyParameter> params = new ArrayList<>();
        PropertyParameter param1 = new PropertyParameter("TZID","Europe/London");
        params.add(param1);
        DateValue dateValue1 = new DateValue(dateTime, params);

        String dateTime2 = "20110619T100030Z";
        List<PropertyParameter> paramsV2 = new ArrayList<>();
        DateValue dateValue2 = new DateValue(dateTime2, paramsV2);

        assertTrue("Values should NOT be equal", dateValue1.equals(dateValue2));

        assertFalse("Values should be equivalent", dateValue1.equalIgnoreTimezone(dateValue2));
    }

    public void testInDaylightTimeReturnsTrueForJune() throws Exception {

        String dateTime = "20110619T110030";
        List<PropertyParameter> params = new ArrayList<>();
        PropertyParameter param1 = new PropertyParameter("TZID","Europe/London");
        params.add(param1);
        DateValue dateValue1 = new DateValue(dateTime, params);

        assertTrue("Daylight time should be true", dateValue1.inDaylightTime());
    }

    public void testInDaylightTimeReturnsFalseForDecember() throws Exception {

        String dateTime = "20111209T110030";
        List<PropertyParameter> params = new ArrayList<>();
        PropertyParameter param1 = new PropertyParameter("TZID","Europe/London");
        params.add(param1);
        DateValue dateValue1 = new DateValue(dateTime, params);

        assertFalse("Daylight time should be false",  dateValue1.inDaylightTime());
    }

    public void testEqualsWithoutParameterIsTrue() throws Exception {

        String dateTime = "20111219T100030Z";
        List<PropertyParameter> params = new ArrayList<>();
        DateValue dateValue1 = new DateValue(dateTime, params);

        List<PropertyParameter> paramsV2 = new ArrayList<>();
        DateValue dateValue2 = new DateValue(dateTime, paramsV2);
        assertTrue("Values should equal", dateValue1.equals(dateValue2));
    }

    public void testEqualsWithoutParameterIsFalse() throws Exception {

        String dateTime = "20111219T100030Z";
        List<PropertyParameter> params = new ArrayList<>();
        DateValue dateValue1 = new DateValue(dateTime, params);

        String dateTimeV2 = "20111219T100035Z";
        List<PropertyParameter> paramsV2 = new ArrayList<>();
        DateValue dateValue2 = new DateValue(dateTimeV2, paramsV2);
        assertFalse("Values not should equal", dateValue1.equals(dateValue2));
    }

    public void testSettimeZoneUpdatesTimezoneParameter() throws Exception {

        String dateTime = "20111219T100030";
        List<PropertyParameter> params = new ArrayList<>();
        PropertyParameter param1 = new PropertyParameter("TZID","UTC");
        params.add(param1);
        DateValue dateValue = new DateValue(dateTime, params);

        assertEquals("Incorrect timezone", "UTC", dateValue.getTimeZoneName());

        dateValue.setTimeZoneName("GMT");

        assertEquals("Incorrect timezone", "GMT", dateValue.getTimeZoneName());

    }

    public void testDateWithZEqualsUTCTimezone() throws Exception {

        String dateTime = "20111219T100030";
        List<PropertyParameter> params = new ArrayList<>();
        PropertyParameter param1 = new PropertyParameter("TZID","UTC");
        params.add(param1);
        DateValue dateValue1 = new DateValue(dateTime, params);

        String dateTime2 = "20111219T100030Z";
        DateValue dateValue2 = new DateValue(dateTime2, null);

        assertTrue("Dates should match", dateValue1.equalIgnoreTimezone(dateValue2));

        assertTrue("Dates should match", dateValue1.equals(dateValue2));

    }
}