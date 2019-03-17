package pha.ics.values;

import pha.ics.WeekDay;

/**
 * An ordinal pair is an integer followed by a sequence of letters, normally 2.
 */
public class OrdinalDay {
    private Integer ord;
    private WeekDay weekDay;

    public Integer getOrd() {
        return ord;
    }

    public WeekDay getWeekDay() {
        return weekDay;
    }

    OrdinalDay(String text) {
        int letterIndex = indexOfFirstLetter(text);
        if (letterIndex == 0) {
            ord = null;
            weekDay = WeekDay.valueOf(text);
        } else {
            ord = Integer.parseInt(text.substring(0, letterIndex));
            weekDay = WeekDay.valueOf(text.substring(letterIndex));
        }

    }

    private int indexOfFirstLetter(String text) {
        char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (Character.isLetter(chars[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof OrdinalDay) {
            OrdinalDay otherDay = (OrdinalDay) obj;
            if (weekDay.equals(otherDay.weekDay)) {
                if (ord != null && ord.equals(otherDay.ord)) {
                    return true;
                }

                if (ord == null && otherDay.ord == null) {
                    return true;
                }

            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return weekDay.hashCode() + (ord != null ? ord.hashCode()*30 : 0);
    }

    @Override
    public String toString() {
        if (ord != null) {
            return ord + weekDay.toString();
        }
        return weekDay.toString();
    }
}
