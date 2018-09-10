package pha.ics.finder;

import pha.ics.*;
import pha.ics.values.DateList;
import pha.ics.values.DateValue;
import pha.ics.values.OrdinalDay;
import pha.ics.values.RepeatRule;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by paul on 23/10/15.
 */
public class WeeklyDateFinder extends DateFinder {

    private Queue<OrdinalDay> currentDays = new ArrayBlockingQueue<OrdinalDay>(8);

    public WeeklyDateFinder(DateValue start, RepeatRule repeatRule, DateList exclude) {
        super(start, repeatRule, exclude);
    }

    @Override
    protected void initialise() {
        super.initialise();
        setDay();
    }

    @Override
    protected void incrementDate() {

        if (currentDays.isEmpty()) {
            currentDate = currentDate.addWeeks(repeatRule.getInterval());
        }
        setDay();
    }

    /**
     * Set the day of week on the current date if there is anything in current days.
     * This collection will be empty if days has not been set.
     */
    private void setDay() {

        refillCurrentDays();

        if (!currentDays.isEmpty()) {
            OrdinalDay ordinalDay = currentDays.remove();
            WeekDay weekDay = ordinalDay.getWeekDay();

            currentDate = currentDate.setDay(weekDay);
        }
    }

    /**
     * If the curentDays collection is empty and the Rule contains days then
     * repopulate currentDays.
     */
    private void refillCurrentDays() {
        if (currentDays.isEmpty() && repeatRule.getByDay() != null) {
            currentDays.addAll(repeatRule.getByDay());
        }
    }

}
