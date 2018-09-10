package pha.ics.values;


import org.jetbrains.annotations.NotNull;
import pha.ics.WeekDay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;

/**
 * Represents a date or date-time pair which can be incorporated in either a
 * DateValue or DateList.
 * This object is immutable and can be compared to other date objects.
 * <p>
 * Created by paul on 11/10/14.
 */
public class DateObject {

    /**
     * Expected formats of the date string, either yyyy MM dd(T)HH mm ss OR yyyy MM dd
     */
    private final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
    private final SimpleDateFormat formatDay = new SimpleDateFormat("yyyyMMdd");

    /**
     * Date as a long in milli seconds, in the local timezone.
     */
    private final Date valueDate;
    private final long value;

    /**
     * Indicating if this time is floating or fixed UTC time.
     */
    private final boolean utcTime;

    /**
     * Does this object contain a date or date-time pair?
     */
    private final boolean dayOnly;

    /**
     * Create a DateObject with the same valueDate as the given Date.
     *
     * @param date to copy
     */
    public DateObject(java.util.Date date) {
        valueDate = date;
        value = date.getTime();
        dayOnly = false;
        utcTime = false;

    }

    /**
     * Create a DateObject with the given information.
     *
     * @param date      the number of milliseconds since January 1, 1970, 00:00:00 GMT
     * @param isDayOnly true indicates date only contains a day and not time of day
     * @param utc       time zone
     */
    public DateObject(long date, boolean isDayOnly, boolean utc) {
        value = date;
        valueDate = new Date(date);
        dayOnly = isDayOnly;
        utcTime = utc;

        if (utc && !isDayOnly) {
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
        }
    }

    /**
     * Create a DateObject with the given information.
     *
     * @param date      the number of milliseconds since January 1, 1970, 00:00:00 GMT
     * @param isDayOnly true indicates date only contains a day and not time of day
     * @param utc       time zone
     */
    public DateObject(Date date, boolean isDayOnly, boolean utc) {
        valueDate = date;
        value = date.getTime();
        dayOnly = isDayOnly;
        utcTime = utc;

        if (utc && !isDayOnly) {
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
        }
    }

    /**
     * Create a date object by parsing the given string. The string must contain a date
     * in one of following formats;
     * <ol>
     * <li>yyyyMMddTHHmmss</li>
     * <li>yyyyMMddTHHmmssZ</li>
     * <li>yyyyMMdd</li>
     * </ol>
     * <p>
     * NOTE: Format 2 represents a date-time in UTC format.
     *
     * @param stringValue to read time from
     * @param isDayOnly   true if the string only contains a date, format 3 above.
     * @param timeZone    Timezone of date string, only needed if isDayOnly is false
     * @throws IllegalArgumentException if the format is wrong.
     */
    public DateObject(@NotNull String stringValue, TimeZone timeZone, boolean isDayOnly) {

        if (!isDayOnly && timeZone != null && stringValue.endsWith("Z") &&
                !timeZone.getID().equalsIgnoreCase("UTC")) {
            throw new IllegalArgumentException("Time string ends with 'Z' but also has timezone :"+stringValue);
        }

        dayOnly = isDayOnly;
        try {
            if (dayOnly) {
                formatDay.setTimeZone(TimeZone.getDefault());
                valueDate = formatDay.parse(stringValue);
                utcTime = false;
            } else {
                String substring;
                if (stringValue.endsWith("Z")) {
                    utcTime = true;
                    substring = stringValue.substring(0, stringValue.length()-1);
                } else {
                    utcTime = (timeZone != null && timeZone.getID().equalsIgnoreCase("UTC"));
                    substring = stringValue;
                }


                if (utcTime) {
                    format.setTimeZone(TimeZone.getTimeZone("UTC"));
                } else if (timeZone == null) {
                    format.setTimeZone(TimeZone.getDefault());
                } else {
                    format.setTimeZone(timeZone);
                }

                substring = cleanDate(substring);


                valueDate = format.parse(substring);
            }

            value = valueDate.getTime();
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date in string parameter", e);
        }

    }

    private String cleanDate(String substring) {
        // Check if the date contains illegal characters
        if (substring.contains(":")) {
            return substring.substring(substring.lastIndexOf(':')+1);
        }
        return substring;
    }

    public DateObject(String valueAsString, TimeZone timeZone) {
        this(valueAsString, timeZone, valueAsString.length() < 9);
    }

    /**
     * Copy constructor, to be used when a mutable copy is required.
     *
     * @param dateObject to copy
     */
    public DateObject(DateObject dateObject) {
        valueDate = dateObject.valueDate;
        value = dateObject.value;
        utcTime = dateObject.utcTime;
        dayOnly = dateObject.dayOnly;
    }


    /**
     * Returns the number of milliseconds since January 1, 1970, 00:00:00 GMT
     * represented by this <tt>DateObject</tt>.
     *
     * @return the number of milliseconds since January 1, 1970, 00:00:00 GMT
     */
    public long getAsLong() {
        return value;
    }

    @Override
    public String toString() {
        if (dayOnly) {
            return formatDay.format(valueDate);
        }
        return format.format(valueDate) + (utcTime ? "Z" : "");
    }

    /**
     * Return the day part of this date object as a string.
     *
     * @return day part of date in the format "yyyymmdd"
     */
    public String getDayAsString() {
        if (dayOnly) {
            return formatDay.format(valueDate);
        }
        return format.format(valueDate).substring(0, 8);
    }

    /**
     * Return the time part of this date object as a string.
     *
     * @return the time part of the date in format HHmmss
     */
    public String getTimeAsString() {
        if (dayOnly) {
            return "000000";
        }
        return format.format(valueDate).substring(9); // 9 = just after the "T"

    }

    /**
     * Return true if this date is newer than the other date. Compares the strings
     * which may compare the date and time.
     *
     * @param otherDate to compare this to
     * @return true if this date is later than otherDate, false if this is null
     */
    public boolean isAfter(@NotNull DateObject otherDate) {

        return value > otherDate.value;
    }

    /**
     * Return true if this date is older than the other date.
     *
     * @param otherDate to compare this to
     * @return true if this date is later than otherDate, false if this is null
     */
    public boolean isBefore(@NotNull DateObject otherDate) {

        return value < otherDate.value;
    }

    /**
     * Compare the 2 times after conversion to local time.
     *
     * @param obj other DateObject to check
     * @return true if they are the same, down to milli-second interval
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof DateObject) {
            DateObject otherDate = (DateObject) obj;

            return value == otherDate.value;
        }
        return false;
    }

    /**
     * Returns true if this DateObject holds a time part.
     *
     * @return true if this object has time information, false if it is only a date
     */
    public boolean hasTimePart() {
        return !dayOnly;
    }

    /**
     * Returns true if this object represents a full date,time object in the
     * UTC time zone. If this returns false check the containing DateValue for
     * a timezone parameter.
     *
     * @return true if UTC timezone
     */
    public boolean isUTC() {
        return utcTime;
    }

    public boolean isSameDay(DateObject otherDate) {
        if (dayOnly && otherDate.dayOnly) {
            return value == otherDate.value;
        }
        String myString = getDayAsString();
        String otherString = otherDate.getDayAsString();

        return myString.equals(otherString);
    }

    /**
     * Add years to this DateObject.
     *
     * @param count years to add
     * @return a new DateObject containing this + count "years"
     */
    public DateObject addYears(int count) {
        return add(count, Calendar.YEAR);

    }

    /**
     * Add months to this DateObject.
     *
     * @param count months to add
     * @return a new DateObject containing this + count "months"
     */
    public DateObject addMonths(int count) {
        return add(count, Calendar.MONTH);
    }

    /**
     * Add weeks to this DateObject.
     *
     * @param count weeks to add
     * @return a new DateObject containing this + count "weeks"
     */
    public DateObject addWeeks(int count) {
        return add(count, Calendar.WEEK_OF_MONTH);
    }

    /**
     * Add days to this DateObject.
     *
     * @param count days to add
     * @return a new DateObject containing this + count "days"
     */
    public DateObject addDays(int count) {

        return add(count, Calendar.DATE);

    }

    /**
     * Add hours to this DateObject.
     *
     * @param count hours to add
     * @return a new DateObject containing this + count "hours"
     */
    public DateObject addHours(int count) {
        return add(count, Calendar.HOUR);

    }

    /**
     * Add minute to this DateObject.
     *
     * @param count minutes to add
     * @return a new DateObject containing this + count "minutes"
     */
    public DateObject addMinutes(int count) {
        return add(count, Calendar.MINUTE);

    }

    /**
     * Add seconds to this DateObject.
     *
     * @param count seconds to add
     * @return a new DateObject containing this + count "seconds"
     */
    public DateObject addSeconds(int count) {
        return add(count, Calendar.SECOND);

    }

    /**
     * Generic method to add/subtract count from the specified field; YEAR, MONTH, DAY, HOUR
     *
     * @param count valueDate to add (subtract if -ve) to field
     * @param field to modify, used Calendar fields.
     * @return a copy of this with the specified modification.
     */
    private DateObject add(int count, int field) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(value);
        calendar.add(field, count);

        return new DateObject(calendar.getTimeInMillis(), dayOnly, utcTime);
    }

    /**
     * Return the month of the date as a number from 1 - 12
     *
     * @return month number
     */
    public int getMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(value);

        return calendar.get(Calendar.MONTH) + 1; // add 1 because Calendar month starts at 0
    }

    /**
     * Sets the month to the month parameter of this DateObject.
     *
     * @param month to set, valueDate should be in the range 1-12
     * @return a new DateObject containing this with the month set to the given valueDate
     */
    public DateObject setMonth(int month) {

        assert month > 0 && month < 13 : "Illegal valueDate for month";

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(value);
        calendar.set(Calendar.MONTH, month - 1); // subtract 1 as calendar month starts at 0

        return new DateObject(calendar.getTimeInMillis(), dayOnly, utcTime);

    }

    /**
     * Return the week day of this DateObject.
     *
     * @return Enum of the weekday
     */
    public WeekDay getDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(value);
        int day = calendar.get(Calendar.DAY_OF_WEEK) - 2; // subtract 2 as Monday is 2

        if (day < 0) {
            day += 7;
        }

        return WeekDay.values()[day];
    }

    public DateObject setDay(WeekDay weekDay) {

        int ordinal = weekDay.ordinal() + 2;
        if (ordinal == 8) {
            ordinal = 1;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(value);
        calendar.set(Calendar.DAY_OF_WEEK, ordinal);

        return new DateObject(calendar.getTimeInMillis(), dayOnly, utcTime);
    }

    public DateObject setWeek(int weekNumber) {
        assert weekNumber > 0 && weekNumber < 5 : "Illegal valueDate for week";

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(value);
        calendar.set(Calendar.WEEK_OF_MONTH, weekNumber);

        return new DateObject(calendar.getTimeInMillis(), dayOnly, utcTime);

    }

    public int getDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(value);

        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public int getWeekOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(value);

        return calendar.get(Calendar.WEEK_OF_MONTH);
    }

    public DateObject setMonthDay(int day) {

        assert day > 0 && day < 32 : "Illegal day of month, day=" + day;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(value);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        return new DateObject(calendar.getTimeInMillis(), dayOnly, utcTime);
    }

    public DateObject setUTC(boolean utcTime) {
        return new DateObject(valueDate, dayOnly, utcTime);
    }

    /**
     * Extract time information from the supplied duration and add it to this
     * DateObject, returning a new DateObject. This method does NOT mutate this object.
     *
     * @param duration to add to this.
     * @return a new DateObject with Duration added.
     */
    public DateObject add(@NotNull Duration duration) {

        int sign = 1;

        if (duration.isNegative()) {
            sign = -1;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(value);

        int week = duration.getWeeks();
        if (week > 0) {
            calendar.add(Calendar.WEEK_OF_MONTH, week * sign);
        }
        int day = duration.getDays();
        if (day > 0) {
            calendar.add(Calendar.DATE, day * sign);
        }
        int hours = duration.getHours();
        if (hours > 0) {
            calendar.add(Calendar.HOUR, hours * sign);
        }
        int min = duration.getMinutes();
        if (min > 0) {
            calendar.add(Calendar.MINUTE, min * sign);
        }
        int sec = duration.getSeconds();
        if (sec > 0) {
            calendar.add(Calendar.SECOND, sec * sign);
        }

        return new DateObject(calendar.getTimeInMillis(), dayOnly, utcTime);

    }

    /**
     * Return the difference between other and this in seconds..
     *
     * @param other DateObjerct to compare to.
     * @return other - this in seconds
     */
    public long getDifferenceInSeconds(DateObject other) {
        return (other.value - value)/1000;
    }

    /**
     * Return the difference between other and this in days..
     *
     * @param other DateObjerct to compare to.
     * @return other - this in seconds
     */
    public long getDifferenceInDays(DateObject other) {
        return (other.value - value)/86400000;
    }

    /**
     * Are the 2 dateObjects equal after converting back to their original time zones.
     *
     * @param other to compare with
     * @return true if equal
     */
    public boolean equalIgnoreTimezone(DateObject other) {
        return toString().equals(other.toString());
    }
}
