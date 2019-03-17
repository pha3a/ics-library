package pha.ics.finder;

import pha.ics.values.DateObject;

/**
 * Filter to add the given interval to the month of the given date.
 */
public class MonthlyIntervalFilter implements Filter {

    private int interval;

    public MonthlyIntervalFilter(int interval) {
        this.interval = interval;
    }

    @Override
    public DateObject getNextDate(DateObject currentDate) {
        return currentDate.addMonths(interval);
    }

    @Override
    public DateObject initialise(DateObject startDate) {
        return startDate;
    }
}
