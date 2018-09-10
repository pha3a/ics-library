package pha.ics.values;

import org.jetbrains.annotations.Nullable;
import pha.ics.PropertyParameter;
import sun.util.calendar.ZoneInfo;

import java.util.*;

/**
 * List of exclude dates from a repeat rule on an event.
 * The dates are a comma separated list of dates or days, See DateObject for date format.
 *
 * @see DateObject
 */
public class DateList extends AbstractDateValue {

    private List<DateObject> dateList = new ArrayList<DateObject>();

    private Comparator<DateObject> comparator = new Comparator<DateObject>() {
        @Override
        public int compare(DateObject lhs, DateObject rhs) {
            if (lhs.getAsLong() == rhs.getAsLong()) {
                return 0;
            }
            if (lhs.getAsLong() < rhs.getAsLong()) {
                return 1;
            }
            return -1;
        }
    };

    /**
     * Create an empty date list.
     */
    public DateList() {

    }

    public DateList(String text, List<PropertyParameter> param) {
        super(param);

        boolean dayOnly = isDayOnly();
        if (text != null) {

            String[] dates = text.split(",");
            for (String date : dates) {
                dateList.add(new DateObject(date, getTimeZone(), dayOnly));
            }

        }
    }

    /**
     * Return the time zone of this date or null if none set.
     *
     * @return time zone, it may not reference a defined time zone in the calendar.
     */
    @Nullable
    public TimeZone getTimeZone() {
        return super.getTimeZone();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof DateList) {
            DateList other = (DateList) obj;

            // Ask this lists, note order is important
            return dateList.equals(other.dateList);
        }
        return false;
    }

    public boolean isDayOnly() {
        PropertyParameter parameter = getParameter("VALUE");

        return parameter != null && parameter.value.equals("DATE");
    }

    @Override
    public String toString() {
        return "DateList{" + getValueAsString() + "}" + super.toString();
    }

    @Override
    public String getValueAsString() {
        StringBuilder builder = new StringBuilder();
        for (DateObject date : dateList) {
            builder.append(date).append(",");
        }

        int length = builder.length();
        if (length > 0) {
            builder.setLength(length - 1);
        }

        return builder.toString();
    }


    public List<DateObject> getDateList() {
        return dateList;
    }

    @Override
    public boolean isEmpty() {
        return dateList.isEmpty();
    }

    public void add(DateValue date) {
        DateObject dateObject = date.getDateObject();
        add(dateObject);
    }

    public void add(DateObject dateObject) {
        boolean contains = dateList.contains(dateObject);
        if (!contains) {
            dateList.add(dateObject);
            Collections.sort(dateList, comparator);
        }
    }

    public boolean contains(DateObject date) {
        return dateList.contains(date);
    }

    public void addAll(DateList dates) {
        for (DateObject dateObject : dates.getDateList()) {
            add(dateObject);
        }
    }
}
