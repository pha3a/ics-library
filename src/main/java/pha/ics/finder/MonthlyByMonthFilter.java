package pha.ics.finder;

import pha.ics.values.DateObject;

import java.util.List;

/**
 * Created by paul on 23/10/15.
 */
public class MonthlyByMonthFilter implements Filter {

    private final Filter source;
    private final List<Integer> allowedMonths;

    public MonthlyByMonthFilter(Filter nextFilter, List<Integer> allowedMonths) {
        this.source = nextFilter;
        this.allowedMonths = allowedMonths;

        assert !allowedMonths.isEmpty() : "No allowed months supplied";
    }

    @Override
    public DateObject getNextDate(DateObject currentDate) {
        DateObject nextDate = source.getNextDate(currentDate);

        while (!allowedMonths.contains(nextDate.getMonth())) {
            nextDate = source.getNextDate(nextDate);
        }
        return nextDate;
    }

    @Override
    public DateObject initialise(DateObject startDate) {
        DateObject date = source.initialise(startDate);

        while (!allowedMonths.contains(date.getMonth())) {
            date = source.getNextDate(date);
        }
        return date;

    }
}
