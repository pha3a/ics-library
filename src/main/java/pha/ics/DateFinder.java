package pha.ics;

import org.jetbrains.annotations.NotNull;
import pha.ics.finder.*;
import pha.ics.values.*;

interface End {
    /**
     * Check if the end has been reached.
     *
     * @param currentDate used by some tests
     * @return true if the end condition has been met.
     */
    boolean reached(DateObject currentDate);
}

class CountEnd implements End {

    private int countLeft;

    CountEnd(int i) {
        countLeft = i;
    }

    @Override
    public boolean reached(DateObject currentDate) {
        return countLeft-- == 0;
    }
}

class UntilEnd implements End {

    private DateObject endDate;

    UntilEnd(DateObject until) {

        endDate = until;
    }

    @Override
    public boolean reached(DateObject currentDate) {
        return currentDate.isAfter(endDate);
    }
}

class InfiniteEnd implements End {

    @Override
    public boolean reached(DateObject currentDate) {
        return false;
    }
}
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
//    private int countLeft;

    /**
     * End criteria, used to determine if the repeating rule end has been reached.
     */
    private End end;

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

        while (currentDate.isBefore(dateToFind) && !end.reached(currentDate)) {
            moveToNextDate();
        }
        return currentDate.equals(dateToFind);

    }

    protected void initialise() {

        if (repeatRule.hasCount()) {
            end = new CountEnd(repeatRule.getCount() - 1);
        } else if (repeatRule.hasUntil()) {
            end = new UntilEnd(repeatRule.getUntil());
        } else {
            end = new InfiniteEnd();
        }
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
