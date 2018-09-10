package pha.ics.values;


import org.jetbrains.annotations.NotNull;
import pha.ics.PropertyParameter;
import pha.ics.WeekDay;
import sun.util.calendar.ZoneInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Represents a repeating rule for an event.
 */
public class RepeatRule extends AbstractValue {


    private Frequency frequency;
    private DateObject until;
    private int count = -1;
    private int interval = 1;
    private WeekDay wkst;
    private List<Integer> bySec;
    private List<Integer> byMin;
    private List<Integer> byHour;
    private List<OrdinalDay> byDay;
    private List<Integer> byMonthDay;
    private List<Integer> byYearDay;
    private List<Integer> byWeekNo;
    private List<Integer> byMonth;
    private List<Integer> bySetPos;


    public RepeatRule(@NotNull Map<Key, String> values, List<PropertyParameter> param) {
        super(param);

        for (Map.Entry<Key, String> entry : values.entrySet()) {
            storeValue(entry.getKey(), entry.getValue());
        }
    }


    private void storeValue(Key key, String value) {

        switch (key) {
            case FREQ:
                frequency = Frequency.valueOf(value);

                break;
            case UNTIL:
                until = new DateObject(value, getTimeZone());

                break;
            case COUNT:
                count = Integer.parseInt(value);

                break;
            case INTERVAL:
                interval = Integer.parseInt(value);

                break;
            case BYMONTHDAY:
                byMonthDay = convertArray(value);

                break;
            case BYSETPOS:
                bySetPos = convertArray(value);

                break;
            case BYMONTH:
                byMonth = convertArray(value);

                break;
            case BYDAY:
                byDay = convertOrdArray(value);

                break;
            case BYHOUR:
                byHour = convertArray(value);

                break;
            case BYMINUTE:
                byMin = convertArray(value);

                break;
            case BYSECOND:
                bySec = convertArray(value);

                break;
            case BYYEARDAY:
                byYearDay = convertArray(value);

                break;
            case BYWEEKNO:
                byWeekNo = convertArray(value);

                break;
            case WKST:
                wkst = WeekDay.valueOf(value);
                break;
        }
    }

    @Override
    public String toString() {
        return "RepeatRule{" + getValueAsString() + "}" + super.toString();
    }


    @Override
    public String getValueAsString() {
        return frequency + ", " + until + ", " + count + ", " + interval + ", " + byMonthDay + ", " + bySetPos +
                ", " + byMonth + ", " + byDay + ", " + byHour + ", " + byMin + ", " + bySec + ", " + byYearDay +
                ", " + byWeekNo + ", " + wkst;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RepeatRule)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        RepeatRule that = (RepeatRule) o;

        if (count != that.count) {
            return false;
        }
        if (interval != that.interval) {
            return false;
        }
        if (byDay != null ? !byDay.equals(that.byDay) : that.byDay != null) {
            return false;
        }
        if (byHour != null ? !byHour.equals(that.byHour) : that.byHour != null) {
            return false;
        }
        if (byMin != null ? !byMin.equals(that.byMin) : that.byMin != null) {
            return false;
        }
        if (byMonth != null ? !byMonth.equals(that.byMonth) : that.byMonth != null) {
            return false;
        }
        if (byMonthDay != null ? !byMonthDay.equals(that.byMonthDay) : that.byMonthDay != null) {
            return false;
        }
        if (bySec != null ? !bySec.equals(that.bySec) : that.bySec != null) {
            return false;
        }
        if (bySetPos != null ? !bySetPos.equals(that.bySetPos) : that.bySetPos != null) {
            return false;
        }
        if (byWeekNo != null ? !byWeekNo.equals(that.byWeekNo) : that.byWeekNo != null) {
            return false;
        }
        if (byYearDay != null ? !byYearDay.equals(that.byYearDay) : that.byYearDay != null) {
            return false;
        }
        if (frequency != that.frequency) {
            return false;
        }
        if (until != null) {
            if (!until.equals(that.until)) {
                return false;
            }
        } else if (that.until != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (frequency != null ? frequency.hashCode() : 0);
        result = 31 * result + (until != null ? until.hashCode() : 0);
        result = 31 * result + count;
        result = 31 * result + interval;
        result = 31 * result + (bySec != null ? bySec.hashCode() : 0);
        result = 31 * result + (byMin != null ? byMin.hashCode() : 0);
        result = 31 * result + (byHour != null ? byHour.hashCode() : 0);
        result = 31 * result + (byDay != null ? byDay.hashCode() : 0);
        result = 31 * result + (byMonthDay != null ? byMonthDay.hashCode() : 0);
        result = 31 * result + (byYearDay != null ? byYearDay.hashCode() : 0);
        result = 31 * result + (byWeekNo != null ? byWeekNo.hashCode() : 0);
        result = 31 * result + (byMonth != null ? byMonth.hashCode() : 0);
        result = 31 * result + (bySetPos != null ? bySetPos.hashCode() : 0);
        result = 31 * result + (wkst != null ? wkst.hashCode() : 0);
        return result;
    }

    private TimeZone getTimeZone() {
        PropertyParameter tzid = getParameter("TZID");
        return tzid != null ? ZoneInfo.getTimeZone(tzid.value) : null;
    }

    private List<Integer> convertArray(String valueAsString) {
        String[] splitValue = valueAsString.split(",");
        List<Integer> intValues = new ArrayList<Integer>(splitValue.length);
        for (String str : splitValue) {
            intValues.add(Integer.parseInt(str));
        }

        return intValues;
    }

    /**
     * Split the given string at the commas and convert the elements into number,name pairs.
     *
     * @param valueAsString to split
     * @return
     */
    private List<OrdinalDay> convertOrdArray(String valueAsString) {
        String[] splitValue = valueAsString.split(",");
        List<OrdinalDay> ordinalDays = new ArrayList<OrdinalDay>(splitValue.length);
        for (String str : splitValue) {
            ordinalDays.add(new OrdinalDay(str));
        }

        return ordinalDays;
    }

    public boolean isYearly() {
        return frequency == Frequency.YEARLY;
    }

    public boolean isMonthly() {
        return frequency == Frequency.MONTHLY;
    }

    public boolean isWeekly() {
        return frequency == Frequency.WEEKLY;
    }

    public boolean isDaily() {
        return frequency == Frequency.DAILY;
    }

    /**
     * Return the rule interval, defaults to 1 if not found.
     *
     * @return
     */
    public int getInterval() {

        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    public List<OrdinalDay> getByDay() {
        return byDay;
    }

    public void addByDay(OrdinalDay day) {
        if (byDay == null) {
            byDay = new ArrayList<>();
        }
        byDay.add(day);
    }

    public List<Integer> getByMonth() {
        return byMonth;
    }

    public void addByMonth(int month) {

        assert month > 0 && month < 13 : "Month out of range";
        if (byMonth == null) {
            byMonth = new ArrayList<>();
        }
        byMonth.add(month);
    }

    public List<Integer> getByMonthDay() {
        return byMonthDay;
    }

    public void addByMonthDay(int day) {

        assert day > 0 && day < 32 : "Day out of range";

        if (byMonthDay == null) {
            byMonthDay = new ArrayList<>();
        }
        byMonthDay.add(day);
    }

    public void clearCount() {
        count = -1;
    }

    public int getCount() {

        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean hasCount() {
        return count > 0;
    }

    public boolean hasUntil() {
        return until != null;
    }

    public void clearUntil() {
        until = null;
    }

    public DateObject getUntil() {

        return until;
    }

    public void setUntil(DateObject until) {
        this.until = until;
    }

    public List<Integer> getBySec() {
        return bySec;
    }

    public List<Integer> getByMin() {
        return byMin;
    }

    public List<Integer> getByHour() {
        return byHour;
    }

    public List<Integer> getByYearDay() {
        return byYearDay;
    }

    public List<Integer> getByWeekNo() {
        return byWeekNo;
    }

    public List<Integer> getBySetPos() {
        return bySetPos;
    }

    public WeekDay getWkst() {
        return wkst;
    }

    public enum Key {
        FREQ,
        UNTIL,
        COUNT,
        INTERVAL,
        BYSECOND,
        BYMINUTE,
        BYHOUR,
        BYDAY,
        BYMONTHDAY,
        BYYEARDAY,
        BYWEEKNO,
        BYMONTH,
        BYSETPOS,
        WKST
    }

}
