package pha.ics.values;

import org.jetbrains.annotations.Nullable;
import pha.ics.PropertyParameter;
import sun.util.calendar.ZoneInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * Common date functionality.
 */
abstract class AbstractDateValue extends AbstractValue implements TimeZoneProvider {

    private PropertyParameter timeZone;

    AbstractDateValue(List<PropertyParameter> parameters) {
        super(parameters);
    }

    AbstractDateValue() {
    }

    @Override
    protected void storeParameter(PropertyParameter e) {
        if (e.name.equalsIgnoreCase("TZID")) {
            timeZone = e;
        }
        super.storeParameter(e);
    }

    /**
     * Return the time zone of this date or null if none set.
     *
     * @return time zone, it may not reference a defined time zone in the calendar.
     */
    @Override
    @Nullable
    public TimeZone getTimeZone() {
        return timeZone != null ? ZoneInfo.getTimeZone(timeZone.value) : null;
    }

    @Override
    @Nullable
    public String getTimeZoneName() {
        return timeZone != null ? timeZone.value : null;
    }

    /**
     * Set the time zone of this date.
     *
     * @param timeZoneName name of timezone, is not validated and may be null to delete timezone.
     */
    @Override
    public void setTimeZoneName(String timeZoneName) {

        List<PropertyParameter> parameters = getParameters();

        if (timeZone != null) {
            parameters.remove(timeZone);
            timeZone = null;
        }

        if (timeZoneName != null) {
            storeParameter(new PropertyParameter("TZID", timeZoneName));
        }
    }

    /**
     * Equals ignoring time zones.
     *
     * @param o to compare with
     * @return true if this equals o
     */
    @Override
    public boolean equals(Object o) {

        if (!(o instanceof AbstractDateValue)) {
            return false;
        }
        AbstractDateValue otherValue = (AbstractDateValue) o;

        List<PropertyParameter> parameters = getParameters();
        List<PropertyParameter> otherParmaeters = otherValue.getParameters();

        if (parameters.isEmpty() && otherParmaeters.isEmpty()) {
            return true;
        }

        ArrayList<PropertyParameter> filteredParams = new ArrayList<>(parameters);
        ArrayList<PropertyParameter> filteredOtherParams = new ArrayList<>(otherParmaeters);

        filteredParams.remove(timeZone);
        filteredOtherParams.remove(otherValue.timeZone);

        return filteredParams.equals(filteredOtherParams);
    }
}
