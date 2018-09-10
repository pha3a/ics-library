package pha.ics.io.write;

import pha.ics.values.Duration;

/**
 * Format a duration correctly according to ICS formatting rules.
 *
 * Created by paul on 28/07/16.
 */
class DurationFormatter {

    static String format(Duration duration) {

        StringBuilder builder = new StringBuilder();

        if (duration.isNegative()) {
            builder.append("-");
        }

        builder.append("P");

        if (duration.isZeroLength()) {
            // This seem a bit of a hack, and I'm not sure if its right, but the spec does not exclude it
            // Without this code we would get "PT0S" for a zero length duration.
            builder.append("0D");
        }
        if (duration.getWeeks() > 0) {
             builder.append(Integer.toString(duration.getWeeks())).append("W");
        } else {

            int days = duration.getDays();
            if (days > 0) {
                builder.append(Integer.toString(days)).append("D");
            }
            builder.append("T");

            int hours = duration.getHours();
            int minutes = duration.getMinutes();
            int seconds = duration.getSeconds();

            if (hours > 0) {
                builder.append(Integer.toString(hours)).append("H");
                builder.append(Integer.toString(minutes)).append("M");
                builder.append(Integer.toString(seconds)).append("S");
            } else if (minutes > 0) {
                builder.append(Integer.toString(minutes)).append("M");
                builder.append(Integer.toString(seconds)).append("S");
            } else {
                builder.append(Integer.toString(seconds)).append("S");
            }
        }

        return builder.toString();
    }
}
