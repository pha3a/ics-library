package pha.ics.io.write;

import pha.ics.*;
import pha.ics.Calendar;
import pha.ics.TimeZone;
import pha.ics.io.read.Line;
import pha.ics.io.write.ValueFormatter;
import pha.ics.io.write.WrapingOutputStream;
import pha.ics.values.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Write a complete calendar file to the supplied output stream using UTF-8 encoding.
 * <p>
 * Created by paul on 10/07/16.
 */
public class CalendarWriter {


    private final PrintWriter writer;

    public CalendarWriter(FileOutputStream outputStream) {
        FilterOutputStream filterOutputStream = new WrapingOutputStream(outputStream);

        OutputStreamWriter osw = null;
        osw = new OutputStreamWriter(filterOutputStream, StandardCharsets.UTF_8);
        writer = new PrintWriter(osw);
    }

    public void write(Calendar calendar) {
        writer.println("BEGIN:VCALENDAR");

        for (Line line : calendar.getHeaderLines()) {
            writeHeaderLine(line);
        }

        for (TimeZone timeZone : calendar.getTimeZones()) {
            writeTimeZone(timeZone);
        }

        List<Event> events = calendar.getEvents();
        List<Event> sortedEvents = sortByTime(events);
        for (Event event : sortedEvents) {
            writeEvent(event);
        }
        writer.println("END:VCALENDAR");

    }

    public void close() {
        writer.close();
    }

    private void writeTimeZone(TimeZone timeZone) {
        writer.println("BEGIN:VTIMEZONE");

        StringValue id = getTimeZoneId(timeZone);
        writeLine(FieldName.TZID, null, id);
        DateValue lastModified = timeZone.getLastModified();
        if (lastModified != null) {
            writeLine(FieldName.LAST_MODIFIED, null, lastModified);
        }

        Map<String, TimeZone.Type> types = timeZone.getTypes();

        for (Map.Entry<String, TimeZone.Type> entry : types.entrySet()) {
            write(entry);
        }

        writer.println("END:VTIMEZONE");

    }

    private StringValue getTimeZoneId(TimeZone timeZone) {
        StringValue id = timeZone.getId();
        if (id.getValue().contains(" ")) {
            id = new StringValue("\""+ id.getValue() +"\"", id.getParameters());
        }
        return id;
    }

    private void write(Map.Entry<String, TimeZone.Type> entry) {
        writer.append("BEGIN:").println(entry.getKey());

        TimeZone.Type value = entry.getValue();
        List<FieldName> fieldNames = new ArrayList<>(value.getFieldNames());
        Collections.sort(fieldNames);
        for (FieldName name : fieldNames) {
            writeLine(name, null, value.getValue(name));
        }

        writer.append("END:").println(entry.getKey());
    }

    private void writeHeaderLine(Line line) {
        writer.print(line.name.toString());
        writer.print(":");
        writer.println(line.value);
    }

    /**
     * Sort the events by start time and then summary.
     *
     * @param events to sort, this list will not be modified
     * @return a sorted copy of the events parameter
     */
    private List<Event> sortByTime(List<Event> events) {
        List<Event> sorted = new ArrayList<Event>(events);
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

    /**
     * Write a single event to the sriter, including the BEGIN and END lines.
     *
     * @param event to writeLine
     */
    private void writeEvent(Event event) {

        writer.println("BEGIN:VEVENT");

        // Using FieldName.values() we can fix the order the fields are written out in.
        for (FieldName fieldName : FieldName.values()) {
            Value value = event.getFieldValue(fieldName);
            if (value != null && !value.isEmpty()) {
                List<PropertyParameter> parameters = value.getParameters();
                if (value instanceof CompoundValue) {
                    CompoundValue compoundValue = (CompoundValue) value;
                    for (Value subValue : compoundValue.getValues()) {
                        writeLine(fieldName, parameters, subValue);
                    }
                } else {
                    writeLine(fieldName, parameters, value);
                }
            }
        }

        writer.println("END:VEVENT");
    }

    /**
     * Write a compete line to the output stream, including;
     * <ul>
     * <li>Field name, e.g. SUMMARY</li>
     * <li>";" (optional)</li>
     * <li>Parameters (optional)</li>
     * <li>":"</li>
     * <li>Value of line</li>
     * </ul>
     *
     * @param fieldName  to write
     * @param parameters to write if present
     * @param value      to write
     */
    private void writeLine(FieldName fieldName, List<PropertyParameter> parameters, Value value) {

        String formatted = ValueFormatter.format(fieldName, value);
        if (formatted != null) {
            writeLine(fieldName, parameters, formatted);
        }
    }

    private void writeLine(FieldName fieldName, List<PropertyParameter> parameters, String stringToWrite) {
        writer.print(fieldName.toString());

        if (parameters != null) {
            for (PropertyParameter parameter : parameters) {
                writer.print(";");
                String value = parameter.value;
                if (value.contains(" ")) {
                    value = "\"" + value + "\"";
                }
                writer.print(parameter.name + "=" + value);
            }
        }

        writer.print(":");
        writer.println(encodeValue(stringToWrite, fieldName));
    }

    /**
     * Encode special characters before they are written to a file.
     *
     * @param value     string to encode
     * @param fieldName of the field this value represents, used to determien if the value should be encoded.
     *                  Currently only encodes DESCRIPTION fields
     * @return an encoded version of this string if it should be encoded
     */
    private String encodeValue(String value, FieldName fieldName) {
        if (fieldName == FieldName.DESCRIPTION ||
                fieldName == FieldName.X_ALT_DESC) {
            String replaced = value.replace("\\", "\\\\");
            replaced = replaced.replace("\n", "\\n");
            replaced = replaced.replace(";", "\\;");
            replaced = replaced.replace(",", "\\,");
            return replaced;
        }
        return value;
    }
}
