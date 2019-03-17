package pha.ics.finder;

import pha.ics.DateFinder;
import pha.ics.values.DateList;
import pha.ics.values.DateValue;
import pha.ics.values.RepeatRule;

/**
 * Created by paul on 23/10/15.
 */
public class DailyDateFinder extends DateFinder {
    public DailyDateFinder(DateValue start, RepeatRule repeatRule, DateList exclude) {
        super(start, repeatRule, exclude);
    }

    @Override
    protected void incrementDate() {
        currentDate = currentDate.addDays(repeatRule.getInterval());

    }
}
