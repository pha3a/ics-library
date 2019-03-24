package pha.ics.finder;

import pha.ics.DateFinder;
import pha.ics.values.DateList;
import pha.ics.values.DateValue;
import pha.ics.values.OrdinalDay;
import pha.ics.values.RepeatRule;

import java.util.List;

/**
 * Created by paul on 23/10/15.
 */
public class MonthlyDateFinder extends DateFinder {

    private Filter dateSource;

    public MonthlyDateFinder(DateValue start, RepeatRule repeatRule, DateList exclude) {
        super(start, repeatRule, exclude);
    }

    @Override
    protected void initialise() {
        super.initialise();

        dateSource = new MonthlyIntervalFilter(repeatRule.getInterval());

        List<Integer> byMonth = repeatRule.getByMonth();
        if (byMonth != null) {
            dateSource = new MonthlyByMonthFilter(dateSource, byMonth);
        }

        List<Integer> byMonthDay = repeatRule.getByMonthDay();
        List<OrdinalDay> byDay = repeatRule.getByDay();
        if (byMonthDay != null) {
            dateSource = new MonthlyByMonthDayExpander(dateSource, byMonthDay);

            if (byDay != null) {
                dateSource = new MonthlyByDayFilter(dateSource, byDay);
            }
        } else if (byDay != null) {
            dateSource = new MonthlyByDayExpander(dateSource, byDay);
        }

        currentDate = dateSource.initialise(currentDate);
    }


    /**
     * Delegate to the dateSource for the next date.
     */
    @Override
    protected void incrementDate() {
        currentDate = dateSource.getNextDate(currentDate);

    }
}
