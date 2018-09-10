package pha.ics.values;

import org.jetbrains.annotations.Nullable;

import java.util.TimeZone;

/**
 * Denotes a class that provides a time zone.
 */
public interface TimeZoneProvider {

    /**
     * Return the time zone of this date or null if none set.
     *
     * @return time zone, it may not reference a defined time zone in the calendar.
     */
    @Nullable
    TimeZone getTimeZone();

    @Nullable
    String getTimeZoneName();

    /**
     * Set the time zone of this date.
     *
     * @param timeZoneName name of timezone, is not validated and may be null to delete timezone.
     */
    void setTimeZoneName(String timeZoneName);
}
