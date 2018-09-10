package pha.ics;

import org.jetbrains.annotations.NotNull;
import pha.ics.finder.*;
import pha.ics.values.*;

/**
 * Base class holding builder method used to create a date finder used to find a single date
 * in an event as specified by a start date, repeat rule and exclude list.
 * Different finders, concrete classes of this class, are returned based on the frequency of the
 * repeat rule. This avoid overly complex algorithms trying to handle multiple different
 * configurations.
 *
 * Created by paul on 17/10/15.
 */
public abstract class DateFinder {

    protected final RepeatRule repeatRule;
    private final DateList exclude;
    private final DateObject startDate;

    protected DateObject currentDate;
    private int countLeft;

    public static DateFinder build(DateValue start, @NotNull RepeatRule repeatRule, DateList exclude) {
        Frequency frequency = repeatRule.getFrequency();

        switch (frequency) {
            case YEARLY:
                return new YearlyDateFinder(start, repeatRule, exclude);
            case MONTHLY:
                return new MonthlyDateFinder(start, repeatRule, exclude);
            case WEEKLY:
                return new WeeklyDateFinder(start, repeatRule, exclude);
            case DAILY:
                return new DailyDateFinder(start, repeatRule, exclude);
            case HOURLY:
                return new HourlyDateFinder(start, repeatRule, exclude);

        }
        throw new IllegalArgumentException("Unknown frequency :" + frequency);

    }

    public DateFinder(DateValue startDate, RepeatRule repeatRule, DateList exclude) {
        this.startDate = startDate.getDateObject();
        this.repeatRule = repeatRule;
        this.exclude = exclude;

    }


    /**
     * Determine if the given date is present in the repeating date
     * range supplied on construction.
     *
     * @param targetDate to find
     * @return true if found
     */
    public boolean find(@NotNull DateValue targetDate) {

        DateObject dateToFind = targetDate.getDateObject();

        initialise();

        while (currentDate.isBefore(dateToFind) && countLeft > 0) {
            moveToNextDate();
            countLeft--;
        }
        return currentDate.equals(dateToFind);

    }

    protected void initialise() {

        countLeft = repeatRule.getCount() > 0 ? repeatRule.getCount() - 1 : Integer.MAX_VALUE;
        currentDate = startDate;
    }

    /**
     * Replace currentDate with the next date in the sequence. Skipping over any
     * excluded dates.
     */
    private void moveToNextDate() {
        incrementDate();

        if (exclude != null) {
            while (exclude.contains(currentDate)) {
                incrementDate();
            }
        }
    }


    protected abstract void incrementDate();


}
