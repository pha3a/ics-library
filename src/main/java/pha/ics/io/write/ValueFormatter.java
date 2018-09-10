package pha.ics.io.write;

import pha.ics.FieldName;
import pha.ics.io.write.DurationFormatter;
import pha.ics.io.write.RepeatRuleFormatter;
import pha.ics.values.*;

/**
 * Method to format any type of value suitable to be written out to an ICS file.
 *
 * Created by paul on 31/07/16.
 */
class ValueFormatter {

    static String format(FieldName fieldName, Value value) {
        String formatted = value.getValueAsString();

        if (value instanceof StringList) {
            StringBuilder b = new StringBuilder();
            for (String s : ((StringList) value).getValue()) {
                b.append(s).append(',');
            }
            if (b.length()>0) {
                b.setLength(b.length()-1);
            }

            formatted = b.toString();
        } else if (value instanceof RepeatRule) {
            formatted = RepeatRuleFormatter.format((RepeatRule) value);
        } else if (fieldName == FieldName.SEQUENCE) {
            IntegerValue sequence = (IntegerValue) value;

            if (sequence.getValue() == 0) {
                return null; // Don't write out a sequence number of 0
            }
        } else if (fieldName == FieldName.DURATION) {
            formatted = DurationFormatter.format((Duration) value);

        }else if (value instanceof UTCOffset) {
            UTCOffset offset = (UTCOffset) value;
            formatted = (offset.isNegative() ? "-" : "+") +
                    String.format("%02d%02d", offset.getHours(), offset.getMinutes());

            int seconds = offset.getSeconds();
            if (seconds > 0) {
                formatted += String.format("%02d", seconds);
            }
        }

        return formatted;
    }
}
