package pha.ics.io.read;

import pha.ics.PropertyParameter;
import pha.ics.values.Duration;

import java.util.List;

/**
 * Take an ics encoded string and create an equivalent Duration object.
 *
 * Created by paul on 28/07/16.
 */
public class DurationBuilder {

    private String value;

    private int days;
    private int weeks;
    private boolean negative;
    private int hours;
    private int minutes;
    private int seconds;
    private List<PropertyParameter> params;

    private DurationBuilder(String value, List<PropertyParameter> params) {
        this.value = value;
        this.params = params;
    }

    public static Duration build(String value, List<PropertyParameter> params) throws IllegalDurationException {
        return new DurationBuilder(value, params).parseValue().createDuration();
    }

    public static Duration build(String value) throws IllegalDurationException {
        return new DurationBuilder(value, null).parseValue().createDuration();
    }

    private Duration createDuration() {
        return new Duration(negative, weeks, days, hours, minutes, seconds, params);
    }

    private DurationBuilder parseValue() throws IllegalDurationException {

        if (value == null || value.length() < 3) {
            throw new IllegalDurationException(value);
        }
        int currentChar = handleSign();

        if (value.charAt(currentChar) != 'P') {
            throw new IllegalDurationException(value);
        }
        currentChar++;

        if (value.charAt(currentChar) == 'T') {
            recordTime(currentChar+1);
        } else {
            int sectionEnd = getSection(currentChar);
            if (sectionEnd == -1) {
                throw new IllegalDurationException(value);
            }
            if (value.charAt(sectionEnd) == 'W') {
                weeks = Integer.parseInt(value.substring(currentChar, sectionEnd));
            } else if (value.charAt(sectionEnd) == 'D') {
                recordDay(currentChar, sectionEnd);
            } else {
                // Its not a Week or Day but it might be a time without a "T" prefix.
                recordTime(currentChar);
            }
        }

        normalize();

        return this;
    }

    /**
     * Normalize the fields so that none contain a large number.
     */
    private void normalize() {
        if (seconds > 59) {
            minutes+= seconds / 60;
            seconds = seconds % 60;
        }
        if (minutes > 59) {
            hours+= minutes / 60;
            minutes = minutes % 60;
        }
        if (hours > 23) {
            days+= hours / 24;
            hours = hours % 24;
        }
    }

    /**
     * Record the number of days and optionally time, example input include;
     * <ul>
     *     <li>2D</li>
     *     <li>1DT4H5M12S</li>
     *     <li>1DT5M12S</li>
     *     <li>1DT12S</li>
     * </ul>
     * @param currentChar poitns to the first character of the number
     * @param sectionEnd points to the 'D'
     */
    private void recordDay(int currentChar, int sectionEnd) throws IllegalDurationException {
        days = Integer.parseInt(value.substring(currentChar, sectionEnd));

        int nextCharIndex = sectionEnd + 1;
        if (nextCharIndex < value.length() && value.charAt(nextCharIndex) == 'T') {
            int startOfTime = nextCharIndex + 1;
            if (startOfTime < value.length()) {
                recordTime(startOfTime);
            } else {
                throw new IllegalDurationException(value);
            }
        }
    }

    private int handleSign() {
        negative = value.startsWith("-");
        if (negative || value.startsWith("+")) {
            return 1;
        }
        return 0;
    }

    /**
     * Assuming that index points to the first digit of a time section set the
     * hours, minutes and seconds if found in value, example formats include;
     * <ul>
     *     <li>4H5M12S</li>
     *     <li>5M12S</li>
     *     <li>12S</li>
     * </ul>
     *
     * @param index of the first digit of a section, either hours, min or sec
     */
    private void recordTime(int index) throws IllegalDurationException {
        int section = getSection(index);
        if (section == -1) {
            throw new IllegalDurationException(value);
        }

        switch (value.charAt(section)) {
            case 'H':
                hours = Integer.parseInt(value.substring(index, section));
                recordTime(section+1);
                break;
            case 'M':
                minutes = Integer.parseInt(value.substring(index, section));
                recordTime(section+1);
                break;
            case 'S':
                seconds = Integer.parseInt(value.substring(index, section));
                break;

        }
    }

    /**
     * Return the position of the first none digit character, this is assumed to
     * mark the end of section.
     *
     * @param index into value where to start the search, it is expected to point to a digit.
     * @return index of first letter, may be -1 if the end of string is found.
     */
    private int getSection(int index) {
        while (index< value.length() && Character.isDigit(value.charAt(index))) {
            index++;
        }

        if (index < value.length()) {
            return index;
        }
        return -1;
    }


    public static class IllegalDurationException extends Exception {
        public IllegalDurationException(String value) {
            super(value);
        }
    }
}
