package pha.ics.io.read;

import pha.ics.FieldName;
import pha.ics.PropertyParameter;
import pha.ics.TimeZone;
import pha.ics.values.DateValue;
import pha.ics.values.StringValue;
import pha.ics.values.UTCOffset;

import java.io.IOException;
import java.util.List;

/**
 * Builder used to create an timeZone from a sequence of lines.
 */
class TimeZoneBuilder extends AbstractBuilder {


    /**
     * Parse the body of a VTIMEZONE. This should consist of 4 parts;
     * <ol>
     * <li>An id of the time zone</li>
     * <li>A last modified date</li>
     * <li>A standard time sub section</li>
     * <li>A daylight subsection</li>
     * </ol>
     *
     * Uses END_TIMEZONE to identify the end of the timeZone.
     *
     * @param reader from which command lines are read.
     * @throws IOException thrown if there is a problem reading lines from
     */
    static TimeZone build(LineReader reader) throws IOException {

        TimeZone timeZone = new TimeZone();

        Line line = reader.readLine();

        // Loop until we reach an "END:VTIMEZONE"
        while (!line.ends("VTIMEZONE")) {

            FieldName fieldName = line.name;

            switch (fieldName) {
                case TZID: {

                    List<PropertyParameter> params = parseParameters(line.params);
                    String value = line.value;
                    timeZone.setId(new StringValue(value, params));

                    break;
                }
                case LAST_MODIFIED: {

                    List<PropertyParameter> params = parseParameters(line.params);
                    String value = line.value;
                    timeZone.setLastModified(new DateValue(value, params));

                    break;
                }
                case BEGIN:

                    TimeZone.Type type = parseType(reader, line.value);
                    timeZone.addType(line.value, type);
                    break;
            }

            line = reader.readLine();
        }

        return timeZone;
    }

    private static TimeZone.Type parseType(LineReader reader, String typeName) throws IOException {
        Line line = reader.readLine();

        TimeZone.Type type = new TimeZone.Type();

        while (!line.ends(typeName)) {

            FieldName fieldName = line.name;
            List<PropertyParameter> params = parseParameters(line.params);
            String value = line.value;

            switch (fieldName) {

                case DTSTART:
                    type.addField(fieldName, new DateValue(value, params));
                    break;
                case RDATE:
                    type.addField(fieldName, new DateValue(value, params));
                    break;
                case RRULE:
                    type.addField(fieldName, RepeatRuleBuilder.build(value, params));
                    break;
                case TZOFFSETFROM:
                    type.addField(fieldName, new UTCOffset(value, params));
                    break;
                case TZOFFSETTO:
                    type.addField(fieldName, new UTCOffset(value, params));
                    break;
                case TZNAME:
                    type.addField(fieldName, new StringValue(value, params));
                    break;
               default:
                   throw new IllegalStateException("Unknown field type :"+fieldName);

            }

            line = reader.readLine();

        }

        return type;
    }

}
