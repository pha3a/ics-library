package pha.ics.values;

import pha.ics.PropertyParameter;
import sun.util.calendar.ZoneInfo;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Wrapper for a Date object read from a calendar.
 */
public class DateValue extends AbstractDateValue {


    /**
     * Date as a long in milli seconds.
     */
    private DateObject value;

    public DateValue(DateObject dateObject) {
        value = dateObject;
    }

    public DateValue(java.util.Date date) {
        super();
        value = new DateObject(date);

    }
    public DateValue(java.util.Date date, List<PropertyParameter> params) {
        super(params);
        value = new DateObject(date);

    }

    public DateValue(long dateLong, List<PropertyParameter> params) {
        super(params);
        value = new DateObject(dateLong, isDayOnly(), false);
    }

    public DateValue(String stringValue, List<PropertyParameter> params) {
        super(params);

        value = new DateObject(stringValue, getTimeZone(), isDayOnly());

    }

    /**
     * Create a new DateValue with date and time of now.
     *
     * @return DateValue of now
     */
    public static DateValue now() {
        return new DateValue(new Date());
    }

    /**
     * Return the date part of this date-time object.
     *
     * @return date in the format YYYYmmDD
     */
    public String getDayAsString() {

        return value.getDayAsString();
    }

    /**
     * Return the time part of this date object as a string.
     *
     * @return the time part of the date in format HHmmss
     */
    public String getTimeAsString() {

        return value.getTimeAsString();
    }

    public DateObject getDateObject() {
        return value;
    }

    public boolean isSameDay(DateValue otherDate) {

        return otherDate != null && value.isSameDay(otherDate.value);
    }

    public boolean isSameDay(DateObject otherDate) {
        return value.isSameDay(otherDate);
    }

    @Override
    public String toString() {
        return "DateValue{" + getValueAsString() + "}" + super.toString();
    }

    @Override
    public String getValueAsString() {
        return value.toString();
    }

    /**
     * Does this value represent a day only, with no time part.
     *
     * @return true if the "VALUE" parameter equals "DATE"
     */
    public boolean isDayOnly() {

        PropertyParameter valueParameter = getParameter("VALUE");
        return valueParameter != null && valueParameter.value.equals("DATE");

    }

    /**
     * Return true if this date is newer than the other date. Compares the strings
     * which may compare the date and time.
     *
     * @param otherDate to compare this to
     * @return true if this date is later than otherDate, false if this is null
     */
    public boolean isAfter(DateValue otherDate) {

        return otherDate != null && value.isAfter(otherDate.value);
    }

    /**
     * Return true if this date is older than the other date.
     *
     * @param otherDate to compare this to
     * @return true if this date is later than otherDate, false if this is null
     */
    public boolean isBefore(DateValue otherDate) {

        return otherDate != null && value.isBefore(otherDate.value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof DateValue) {
            DateValue otherDate = (DateValue) obj;

            return value.equals(otherDate.value) && super.equals(obj);

        }
        return false;
    }

    /**
     * Equal after converting back to original time zones.
     *
     * @param other DateValue to compare with
     * @return true if they are equivalent
     */
    public boolean equalIgnoreTimezone(DateValue other) {
        return value.equalIgnoreTimezone(other.value);
    }

    /**
     * Returns true if this DateValue holds a time part.
     *
     * @return true if this object has time information, false if it is only a date
     */
    public boolean hasTimePart() {
        return value.hasTimePart();
    }

    /**
     * Returns true if this dataValue is in UTC (time zone), first check the
     * date for a "Z" at the end but if that fails check the parameters for a time zone.
     *
     * @return true if date in UTC
     */
    public boolean isUTC() {
        boolean utcTime = value.isUTC();

        if (!utcTime) {
            TimeZone zone = getTimeZone();
            utcTime = zone != null && zone.getDisplayName().equals("UTC");
        }
        return utcTime;
    }

    public void setUTC(boolean utc) {
        value = value.setUTC(utc);
    }

    /**
     * Remove the time part of the DateValue and mark this as only holding a
     * Date.
     */
    public void removeTime() {

        if (!isDayOnly()) {

            List<PropertyParameter> parameters = getParameters();

            PropertyParameter valueParam = getParameter("VALUE");
            if (valueParam != null) {
                parameters.remove(valueParam);
            }
            parameters.add(new PropertyParameter("VALUE", "DATE"));

            value = new DateObject(value.getDayAsString(), null, true);
        }

    }

    /**
     * Add days to this DateValue. Mutates current object.
     *
     * @param days to add
     */
    public void addDays(int days) {

        value = value.addDays(days);

    }

    /**
     * Add hours to this DateValue, Mutates current object.
     *
     * @param hours to add
     */
    public void addHours(int hours) {

        value = value.addHours(hours);

    }

    /**
     * Return true if current date is in daylight saving zone according to timezone. Will use default
     * time zone if none specified on this object.
     *
     * @return true if date is in summertime, false otherwise
     */
    public boolean inDaylightTime() {
        TimeZone zone = getTimeZone();
        if (zone == null) {
            zone = ZoneInfo.getDefault();
        }
        long asLong = getDateObject().getAsLong();
        return zone.inDaylightTime(new Date(asLong));
    }


}
