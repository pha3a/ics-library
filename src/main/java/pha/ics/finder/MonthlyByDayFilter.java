package pha.ics.finder;

import pha.ics.WeekDay;
import pha.ics.values.DateObject;
import pha.ics.values.OrdinalDay;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by paul on 23/10/15.
 */
public class MonthlyByDayFilter implements Filter {

    private final Filter source;
    private final List<WeekDay> allowedDays = new ArrayList<>();

    public MonthlyByDayFilter(Filter source, List<OrdinalDay> days) {
        this.source = source;
        for (OrdinalDay day : days) {
            allowedDays.add(day.getWeekDay());
        }

    }

    /**
     * Return the first data that has a day of week that is in allowedDays.
     *
     * @param currentDate Previous date to base teh new date on.
     * @return next legal date.
     */
    @Override
    public DateObject getNextDate(DateObject currentDate) {

        DateObject nextDate = source.getNextDate(currentDate);
        while (!allowedDays.contains(nextDate.getDay())) {
            nextDate = source.getNextDate(currentDate);
        }
        return nextDate;
    }

    @Override
    public DateObject initialise(DateObject startDate) {
        return source.initialise(startDate);
    }
}
