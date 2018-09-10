package pha.ics.finder;

import pha.ics.values.DateObject;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by paul on 23/10/15.
 */
public class YearlyByMonthExpander implements Filter {

    private Filter source;
    private List<Integer> allowedMonths;

    private Queue<Integer> availableMonths = new ArrayBlockingQueue<Integer>(12);


    private YearlyByMonthExpander(Filter nextFilter, List<Integer> allowedMonths) {
        this.source = nextFilter;
        this.allowedMonths = allowedMonths;

    }

    public DateObject getNextDate(DateObject currentDate) {

        DateObject nextDate = currentDate;

        int currentMonth = currentDate.getMonth();
        while (!availableMonths.isEmpty() && availableMonths.peek() < currentMonth) {
            availableMonths.remove();
        }

        if (availableMonths.isEmpty()) {
            nextDate = source.getNextDate(currentDate);
            availableMonths.addAll(allowedMonths);

        }

        return nextDate.setMonth(availableMonths.remove());
    }

    /**
     * Initilise the availableMonths, removing any that are before the starting month.
     *
     * @param startDate of the range
     * @return an adjusted start date or startDate
     */
    public DateObject initialise(DateObject startDate) {

        availableMonths.addAll(allowedMonths);

        DateObject date = source.initialise(startDate);

        int currentMonth = date.getMonth();
        while (!availableMonths.isEmpty() && availableMonths.peek() <= currentMonth) {
            availableMonths.remove();
        }

        return date;
    }
}
