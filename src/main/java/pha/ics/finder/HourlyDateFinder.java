package pha.ics.finder;

import pha.ics.DateFinder;
import pha.ics.values.DateList;
import pha.ics.values.DateValue;
import pha.ics.values.RepeatRule;

/**
 * Created by paul on 23/10/15.
 */
public class HourlyDateFinder extends DateFinder {

    public HourlyDateFinder(DateValue start, RepeatRule repeatRule, DateList exclude) {
        super(start, repeatRule, exclude);
    }

    @Override
    protected void incrementDate() {
        currentDate = currentDate.addHours(repeatRule.getInterval());

    }
}
