package pha.ics.io;

import org.junit.Test;
import pha.ics.Calendar;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.*;

/**
 * Created by paul on 04/06/16.
 */
public class CalendarIOTest {

    @Test
    public void testWriteCreatesAFile() throws Exception {

        Path tempDirectory = Files.createTempDirectory("ics-tests");
        File directory = tempDirectory.toFile();

        Calendar calendar = new Calendar("Cal-Name");

        CalendarIO.writeICS(calendar, directory);

        assertEquals("Incorrect number of files :", 1, directory.list().length);

    }

    @Test
    public void testWriteFollwedByParseGetsTheSameCalendarBack() throws IOException {

        File fileToRead = new File("src/test/resources/TestCalendar.ics");

        Calendar originalCalendar = CalendarIO.readICS(fileToRead);

        Path tempPath = Files.createTempDirectory("ics-tests");
        File tempDir = tempPath.toFile();

        File newFile = CalendarIO.writeICS(originalCalendar, tempDir);

        Calendar rereadCalendar = CalendarIO.readICS(newFile);

        assertEquals("Calendars are different :", originalCalendar, rereadCalendar);

    }

    @Test
    public void parseNormalCalendarWithEventsButNoTimeZones() throws IOException {
        File fileToRead = new File("src/test/resources/TestCalendar.ics");

        Calendar calendar = CalendarIO.readICS(fileToRead);

        assertNotNull("No calendar read", calendar);

        assertEquals("Incorrect number of events", 19, calendar.getEventCount());
        assertEquals("Incorrect number of header lines", 2, calendar.getHeaderLines().size());
        assertEquals("Incorrect number of timezones", 0, calendar.getTimeZones().size());
    }

    @Test
    public void parseAppgenixCalendarWithEventsButNoTimeZones() throws IOException {
        File fileToRead = new File("src/test/resources/TestCalendarFromAPPGENIX.ics");

        Calendar calendar = CalendarIO.readICS(fileToRead);

        assertNotNull("No calendar read", calendar);

        assertEquals("Incorrect number of events", 2, calendar.getEventCount());
        assertEquals("Incorrect number of header lines", 6, calendar.getHeaderLines().size());
        assertEquals("Incorrect number of timezones", 0, calendar.getTimeZones().size());
    }

    @Test
    public void parseNormalCalendarWithEventsAndTimeZones() throws IOException {
        File fileToRead = new File("src/test/resources/TestCalendarWithTimeZones.ics");

        Calendar calendar = CalendarIO.readICS(fileToRead);

        assertNotNull("No calendar read", calendar);

        assertEquals("Incorrect number of events", 2, calendar.getEventCount());
        assertEquals("Incorrect number of header lines", 9, calendar.getHeaderLines().size());
        assertEquals("Incorrect number of timezones", 2, calendar.getTimeZones().size());
    }

    @Test(expected = IOException.class)
    public void parseInvalidFile() throws IOException {
        File fileToRead = new File("src/test/resources/TestText.txt");

        Calendar calendar = CalendarIO.readICS(fileToRead);

        assertNotNull("No calendar read", calendar);

        assertEquals("Incorrect number of events", 19, calendar.getEventCount());
    }

}