package pha.ics.finder;

import pha.ics.values.DateObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by paul on 23/10/15.
 */
public class MonthlyByMonthDayExpender implements Filter {

    private Queue<DateObject> availableDays = new ArrayBlockingQueue<DateObject>(31);

    private Filter source;
    private List<Integer> allowableDays;

    public MonthlyByMonthDayExpender(Filter dateSource, List<Integer> monthDays) {
        this.source = dateSource;
        this.allowableDays = monthDays;
    }

    @Override
    public DateObject getNextDate(DateObject currentDate) {

        if (availableDays.isEmpty()) {

            DateObject nextDate = source.getNextDate(currentDate);

            for (Integer day : allowableDays) {
                DateObject newDate = nextDate.setMonthDay(day);
                if (newDate.getMonth() == nextDate.getMonth()) {

                    availableDays.add(newDate);
                }
            }
        }

        return availableDays.remove();

    }

    /**
     * Populate the availableDays with a filtered set of days, removing any that are before
     * the given start date. We pass the start date to the source incase it needs to adjust
     * the start date first.
     *
     * @param startDate the first date in the range given to us from the finder.
     * @return an adjusted start date, may be the same as start date.
     */
    @Override
    public DateObject initialise(DateObject startDate) {

        DateObject date = source.initialise(startDate);
        int currentMonthDate = date.getDayOfMonth();

        List<Integer> filteredDays = new ArrayList<Integer>(31);
        for (Integer day : allowableDays) {
            if (day >= currentMonthDate) {
                filteredDays.add(day);
            }
        }


        for (Integer day : filteredDays) {
            DateObject newDate = date.setMonthDay(day);
            if (newDate.getMonth() == date.getMonth()) {

                availableDays.add(newDate);
            }
        }

        return availableDays.remove();
    }
}
