package pha.ics.io.read;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static pha.ics.FieldName.*;

public class LineParserTest {

    @Test
    public void parseSingleKey() {
        Line line = LineParser.split("BEGIN");

        assertEquals("Missing name", BEGIN, line.name);
        assertNull("Unexpected parameters", line.params);
        assertNull("Unexpected value", line.value);
    }

    @Test
    public void parseKeyWithValue() {
        Line line = LineParser.split("DESCRIPTION:A message");

        assertEquals("Missing name", DESCRIPTION, line.name);
        assertNull("Unexpected parameters", line.params);
        assertEquals("Missing value", "A message", line.value);
    }

    @Test
    public void parseKeyWithQuotedValue() {
        Line line = LineParser.split("DESCRIPTION:\"A message\"");

        assertEquals("Missing name", DESCRIPTION, line.name);
        assertNull("Unexpected parameters", line.params);
        assertEquals("Missing value", "\"A message\"", line.value);
    }

    @Test
    public void parseValueWithMultipleQuotedRegions() {
        Line line = LineParser.split("DESCRIPTION:\"A message\"; and more \"Than\" this is");

        assertEquals("Missing name", DESCRIPTION, line.name);
        assertNull("Unexpected parameters", line.params);
        assertEquals("Missing value", "\"A message\"; and more \"Than\" this is", line.value);
    }

    @Test
    public void parseValueWithQuotedRegionsAndSemiColon() {
        Line line = LineParser.split("DESCRIPTION:\"A message\"; and more \"Than\" that; this is");

        assertEquals("Missing name", DESCRIPTION, line.name);
        assertNull("Unexpected parameters", line.params);
        assertEquals("Missing value", "\"A message\"; and more \"Than\" that; this is", line.value);
    }

    @Test
    public void parseKeyWithValueAndParameter() {
        Line line = LineParser.split("DTSTART;VALUE=DATE:19670621");

        List<String> expectedParams = new ArrayList<>();
        expectedParams.add("VALUE=DATE");

        assertEquals("Missing name", DTSTART, line.name);
        assertEquals("Missing parameters", expectedParams, line.params);
        assertEquals("Missing value", "19670621", line.value);
    }

    @Test
    public void parseMultipleParameters() {
        Line line = LineParser.split("DTSTART;VALUE=DATE;TZID=\"GMT Standard Time\":19670621");

        List<String> expectedParams = new ArrayList<>();
        expectedParams.add("VALUE=DATE");
        expectedParams.add("TZID=\"GMT Standard Time\"");

        assertEquals("Missing name", DTSTART, line.name);
        assertEquals("Missing parameters", expectedParams, line.params);
        assertEquals("Missing value", "19670621", line.value);
    }

    @Test
    public void parseMultipleAttendeeParameters() {
        Line line = LineParser.split("ATTENDEE;CN=\"Dano Sorensen, Henrik (DK Aalborg)\";RSVP=TRUE:mailto:henrik.danoe@baesystems.com");

        List<String> expectedParams = new ArrayList<>();
        expectedParams.add("CN=\"Dano Sorensen, Henrik (DK Aalborg)\"");
        expectedParams.add("RSVP=TRUE");

        assertEquals("Missing name", ATTENDEE, line.name);
        assertEquals("Missing parameters", expectedParams, line.params);
        assertEquals("Missing value", "mailto:henrik.danoe@baesystems.com", line.value);
    }

    @Test
    public void parseKeyWithQuotedParameter() {
        Line line = LineParser.split("DTSTART;TZID=\"GMT Standard Time\":19871229T143500");

        List<String> expectedParams = new ArrayList<>();
        expectedParams.add("TZID=\"GMT Standard Time\"");

        assertEquals("Missing name", DTSTART, line.name);
        assertEquals("Missing parameters", expectedParams, line.params);
        assertEquals("Missing value", "19871229T143500", line.value);
    }

    @Test
    public void parseValueWithMultipleColons() {
        Line line = LineParser.split("DTSTART;TZID=GMT+00:00:19991029T143500");

        List<String> expectedParams = new ArrayList<>();
        expectedParams.add("TZID=GMT+00");

        assertEquals("Missing name", DTSTART, line.name);
        assertEquals("Missing parameters", expectedParams, line.params);
        assertEquals("Missing value", "00:19991029T143500", line.value);
    }

}
