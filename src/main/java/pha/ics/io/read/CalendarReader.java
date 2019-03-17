package pha.ics.io.read;

import pha.ics.Calendar;
import pha.ics.Event;
import pha.ics.TimeZone;

import java.io.IOException;
import java.io.Reader;

/**
 * Read a complete Calendar object from the supplied reader.
 *
 * Created by paul on 21/09/14.
 */
public class CalendarReader {

    private final LineReader lineReader;

    /**
     * Create a reader to read an ICS encoded calendar file from the supplied stream.
     *
     * @param reader to read the file from.
     */
    public CalendarReader(Reader reader) {
        lineReader = new LineReader(reader);
    }

    public Calendar readCalendar(String name) throws IOException {

        Line line = lineReader.readLine();

        // Check that the first line of the file is correct.
        if (!line.begins("VCALENDAR")) {
            throw new IOException(line.toString());
        }

        Calendar calendar = new Calendar(name);


        line = lineReader.readLine();


        while (!line.begins()) {
            calendar.addHeaderLine(line);

            if (line.value.equals("-//BUSINESS-CALENDAR//APPGENIX-SOFTWARE//")) {
                lineReader.setFormatToAppgenix();
            }

            line = lineReader.readLine();
        }


        // We may have a BEGIN:VTIMEZONE
        while (line.begins("VTIMEZONE")) {
            TimeZone timeZone = TimeZoneBuilder.build(lineReader);
            if (timeZone != null) {
                calendar.addTimeZone(timeZone);
                line = lineReader.readLine();
            }

        }

        while (!line.begins()) {
            line = lineReader.readLine();
        }

        Event event;
        do {
            event = EventBuilder.build(lineReader);
            if (event != null) {
                calendar.addEvent(event);
                line = lineReader.readLine();
            }
        } while (event != null && line.begins("VEVENT"));

        return calendar;
    }


    public void close() throws IOException {
        lineReader.close();
    }

}
