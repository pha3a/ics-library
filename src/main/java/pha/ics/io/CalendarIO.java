package pha.ics.io;

import org.jetbrains.annotations.Nullable;
import pha.ics.Calendar;
import pha.ics.Event;
import pha.ics.io.read.CalendarReader;
import pha.ics.io.write.CalendarWriter;
import pha.ics.values.DateValue;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Read and write ICS files to Calendar objects to/from a File.
 * <p/>
 * Created by paul on 28/09/14.
 */
public class CalendarIO {

    /**
     * Build a Calendar of Events read from the given file.
     *
     * @param file to read
     */
    @Nullable
    public static Calendar readICS(File file) throws IOException {

        CalendarReader reader = null;
        try {

            InputStream is = new FileInputStream(file);
            Reader fileReader = new InputStreamReader(is, StandardCharsets.UTF_8);

            reader = new CalendarReader(fileReader);

            return reader.readCalendar(file.getName());

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
            }
        }

    }

    /**
     * Write the given calendar to the given directory, the file name will be the calendar name
     * with a suffix of ".ics"
     *
     * @param calendar  to write
     * @param directory to write it to
     * @throws FileNotFoundException if the directory does not exist.
     */
    public static File writeICS(Calendar calendar, File directory) throws FileNotFoundException {

        File targetFile = new File(directory, calendar.getName() + ".ics");
        FileOutputStream outputStream = new FileOutputStream(targetFile);

        CalendarWriter calendarWriter = new CalendarWriter(outputStream);
        calendarWriter.write(calendar);
        calendarWriter.close();

        return targetFile;
    }

    /**
     * Helper method used for debugging. This method writes the events in the given
     * calendar out as simple text file to a file in directory with a name equivalent to the calendar name.
     *
     * @param calendar  to write to file
     * @param directory to write file to
     * @throws FileNotFoundException if the text file could not be create, if say the directory does not exist
     */
    public static void writeTextTo(Calendar calendar, File directory) throws FileNotFoundException {

        File resultFile = new File(directory, calendar.getName() + ".txt");
        PrintStream pw = new PrintStream(resultFile);
        pw.println("**********NEW FILE " + new Date());
        List<Event> events = calendar.getEvents();
        for (Event event : sortByTime(events)) {
            pw.print("###### (");
            pw.print(event.getDTStart().getValueAsString() + "-");
            DateValue endDate = event.getDTEnd();
            if (endDate != null) {
                pw.print(endDate.getValueAsString());
            }
            pw.print(") ");
            pw.println(event.getSummary());
            String description = event.getDescription();
            if (description != null) {
                pw.println("## desc='" + description + "'");
            }
            String uid = event.getUid();
            if (uid != null) {
                pw.println("## UID=" + uid);
            }
            pw.println("## rrule=" + event.getRepeatRule());
            pw.println("## lastMod=" + event.getLastModified().getValueAsString());
        }

        pw.close();
    }

    /**
     * Sort the events by start time and then summary.
     *
     * @param events to sort, this list will not be modified
     * @return a sorted copy of the events parameter
     */
    private static List<Event> sortByTime(List<Event> events) {
        List<Event> sorted = new ArrayList<>(events);
        Collections.sort(sorted, new Comparator<Event>() {
            public int compare(Event event, Event event2) {
                DateValue startDate1 = event.getDTStart();
                DateValue startDate2 = event2.getDTStart();
                if (startDate1.isAfter(startDate2)) {
                    return 1;
                } else if (startDate1.isBefore(startDate2)) {
                    return -1;
                } else {
                    String eventSummary = event.getSummary();
                    String event2Summary = event2.getSummary();
                    if (eventSummary != null && event2Summary != null) {
                        return eventSummary.compareTo(event2Summary);
                    }
                    return 0;
                }
            }
        });
        return sorted;
    }
}
