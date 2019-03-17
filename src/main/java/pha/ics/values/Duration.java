package pha.ics.values;


import org.jetbrains.annotations.NotNull;
import pha.ics.PropertyParameter;

import java.util.List;

/**
 * Represents a duration of time.
 * <p>
 * Created by paul on 13/07/16.
 */
public class Duration extends AbstractValue {

    /**
     * Information extracted from value, used to perform calcuations.
     */
    private boolean negative;
    private int hours;
    private int minutes;
    private int seconds;
    private final List<PropertyParameter> params;
    private int weeks;
    private int days;

    public Duration(boolean negative, int weeks, int days, int hours, int minutes, int seconds, List<PropertyParameter> params) {
        super(params);

        this.negative = negative;
        this.weeks = weeks;
        this.days = days;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.params = params;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Duration duration = (Duration) o;

        if (negative != duration.negative) return false;
        if (hours != duration.hours) return false;
        if (minutes != duration.minutes) return false;
        if (seconds != duration.seconds) return false;
        if (weeks != duration.weeks) return false;
        return days == duration.days;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (negative ? 1 : 0);
        result = 31 * result + hours;
        result = 31 * result + minutes;
        result = 31 * result + seconds;
        result = 31 * result + weeks;
        result = 31 * result + days;
        return result;
    }

    @Override
    public String toString() {
        return "Duration{" + getValueAsString() + '}' + super.toString();
    }

    @Override
    public String getValueAsString() {
        return (negative ? "-" : "")+ "W" + weeks + "D" + days + "H" + hours + "M" + minutes + "S" + seconds;
    }


    /**
     * Return true if all the parsed fields are zero.
     *
     * @return true if all fields are zero
     */
    public boolean isZeroLength() {
        return weeks == 0 && days == 0 && hours == 0 && minutes == 0 && seconds == 0;
    }

    @Override
    public int compareTo(@NotNull Value otherValue) {
        if (otherValue instanceof Duration) {
            Duration otherDuration = (Duration) otherValue;

            if (negative && !otherDuration.negative) {
                return -1;
            }
            if (!negative && otherDuration.negative) {
                return 1;
            }
            int weekCompare = Integer.compare(weeks, otherDuration.weeks);
            if (weekCompare != 0) {
                return weekCompare;
            }
            int dayCompare = Integer.compare(days, otherDuration.days);
            if (dayCompare != 0) {
                return dayCompare;
            }
            int hoursCompare = Integer.compare(hours, otherDuration.hours);
            if (hoursCompare != 0) {
                return hoursCompare;
            }
            int minutesCompare = Integer.compare(minutes, otherDuration.minutes);
            if (minutesCompare != 0) {
                return minutesCompare;
            }

            return Integer.compare(seconds, otherDuration.seconds);

        }
        return super.compareTo(otherValue);
    }

    public boolean isNegative() {
        return negative;
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

    public int getWeeks() {
        return weeks;
    }

    public int getDays() {
        return days;
    }


}
