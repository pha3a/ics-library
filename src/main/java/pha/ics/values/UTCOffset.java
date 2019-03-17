package pha.ics.values;

import org.jetbrains.annotations.NotNull;
import pha.ics.PropertyParameter;

import java.util.List;

/**
 * Stores an offset from UTC time as a string, mainly used in TimeZones.
 * <p>
 * Created by paul on 07/07/16.
 */
public class UTCOffset extends AbstractValue {

    private int hours;
    private int minutes;
    private int seconds;
    private boolean negative;

    public UTCOffset(@NotNull String value, List<PropertyParameter> parameters) {
        super(parameters);

        parse(value);
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public boolean isNegative() {
        return negative;
    }

    private void parse(String value) {

        if (value.length() < 5 ||
                (!value.startsWith("-") && !value.startsWith("+"))) {
            throw new IllegalArgumentException(value);
        }

        negative = value.startsWith("-");
        hours = Integer.parseInt(value.substring(1,3));
        minutes = Integer.parseInt(value.substring(3, 5));

        if (value.length() >= 7) {
            seconds = Integer.parseInt(value.substring(5,7));
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        UTCOffset utcOffset = (UTCOffset) o;

        if (hours != utcOffset.hours) return false;
        if (minutes != utcOffset.minutes) return false;
        if (seconds != utcOffset.seconds) return false;
        return negative == utcOffset.negative;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + hours;
        result = 31 * result + minutes;
        result = 31 * result + seconds;
        result = 31 * result + (negative ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UTCOffset{" + getValueAsString() + '}' + super.toString();
    }

    @Override
    public String getValueAsString() {
        return (negative ? "-":"+")+hours+","+minutes+","+seconds;
    }

    @Override
    public int compareTo(@NotNull Value otherValue) {
        if (otherValue instanceof UTCOffset) {
            UTCOffset otherOffset = ((UTCOffset) otherValue);

            return getValueAsString().compareTo(otherOffset.getValueAsString());
        }
        return super.compareTo(otherValue);
    }
}
