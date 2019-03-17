package pha.ics.io.read;

import pha.ics.Event;
import pha.ics.FieldName;
import pha.ics.PropertyParameter;
import pha.ics.values.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static pha.ics.FieldName.BEGIN;

/**
 * Builder used to create an event from a sequence of lines.
 */
class EventBuilder extends AbstractBuilder {

    /**
     * Parse an event into an Event object from the supplied reader. Uses
     * END_EVENT to identify the end of the event.
     *
     * @param reader to read Event from
     * @throws java.io.IOException if something when wrong reading from reader
     */
    static Event build(LineReader reader) throws IOException {

        Event event = new Event();

        // Read first field line of event
        Line line = reader.readLine();

        while (!line.ends("VEVENT")) {

            parseEventLine(line, reader, event);

            line = reader.readLine();

        }

        // Run out of lines, no more events
        if (line == LineReader.END_OF_INPUT) {
            return null;
        }
        return event;
    }


    private static void parseEventLine(Line line, LineReader reader, Event event) throws IOException {

        if (line.name == BEGIN) {
            parseSubElement(reader, line.value);
        } else {
            storeField(line, event);
        }
    }


    /**
     * Store a field in the given event based on information in the supplied Line.
     * <p>
     *
     * @param line  holding the field name, parameters and value to store
     * @param event to store field in
     */
    private static void storeField(Line line, Event event) throws IOException {

        FieldName fieldName = line.name;
        List<PropertyParameter> params = parseParameters(line.params);
        String value = line.value;

        try {
            switch (fieldName.getType()) {
                case DATE:
                    event.addValue(fieldName, new DateValue(value, params));
                    break;
                case INTEGER:
                    event.addValue(fieldName, new IntegerValue(value, params));
                    break;
                case TEXT:
                    event.addValue(fieldName, new StringValue(decode(value), params));
                    break;
                case ADDRESS_TYPE:
                    event.addValue(fieldName, new Address(value, params));
                    break;
                case RECUR:
                    event.addValue(fieldName, RepeatRuleBuilder.build(value, params));
                    break;
                case DATE_LIST:
                    event.addValue(fieldName, new DateList(value, params));
                    break;
                case TEXT_LIST:
                    List<String> values = decodeList(value);
                    event.addValue(fieldName, new StringList(values, params));
                    break;
                case STATUS_TYPE:
                    event.addValue(fieldName, Status.parse(value));
                    break;
                case CLASS_TYPE:
                    event.addValue(fieldName, SecurityClass.parse(value));
                    break;
                case TRANSP_TYPE:
                    event.addValue(fieldName, new StringValue(decode(value), params));
                    break;
                case DURATION_TYPE:
                    event.addValue(fieldName, DurationBuilder.build(value, params));
                    break;
            }
        } catch (Exception e) {
            throw new IOException("Error while processing FieldName:" + fieldName + " Value:" + value, e);
        }

    }

    private static List<String> decodeList(String valueList) {
        String[] values = valueList.split(",");
        List<String> result = new ArrayList<>();

        for (String value : values) {
            result.add(decode(value));
        }

        return result;
    }

    private static String decode(String value) {
        if (value != null) {
            String newValue = value.replace("\\n", "\n");
            newValue = newValue.replace("\\N", "\n");
            newValue = newValue.replace("\\;", ";");
            newValue = newValue.replace("\\,", ",");
            newValue = newValue.replace("\\\\", "\\");
            return newValue;
        }
        return null;
    }


    private static void parseSubElement(LineReader reader, String typeName) throws IOException {
        Line line = reader.readLine();
        while (!line.ends(typeName)) {

            line = reader.readLine();

        }
    }
}
