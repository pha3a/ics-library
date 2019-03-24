package pha.ics.finder;

import pha.ics.WeekDay;
import pha.ics.values.DateObject;
import pha.ics.values.OrdinalDay;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by paul on 23/10/15.
 */
public class MonthlyByDayExpander implements Filter {

    private Queue<OrdinalDay> currentDays = new ArrayBlockingQueue<>(8);

    private final Filter source;
    private final List<OrdinalDay> allDays;

    public MonthlyByDayExpander(Filter source, List<OrdinalDay> byDay) {
        this.source = source;
        this.allDays = byDay;
    }

    @Override
    public DateObject getNextDate(DateObject currentDate) {

        DateObject nextDate = currentDate;
        while (currentDays.isEmpty()) {
            currentDays.addAll(allDays);

            nextDate = moveToNextWeek(nextDate);

            // Discard any days before the current day or ones that specify a week
            WeekDay currentDay = nextDate.getDay();
            int week = nextDate.getWeekOfMonth();
            while (!currentDays.isEmpty()) {
                OrdinalDay ordinalDay = currentDays.peek();
                if (ordinalDay.getOrd() == null) {
                    //No week number specified, can match all weeks
                    if (ordinalDay.getWeekDay().compareTo(currentDay) < 0) {
                        currentDays.remove();
                    } else {
                        return useNextDay(nextDate);
                    }
                } else {
                    // A week number has been specified so we need to check it matches
                    Integer ord = ordinalDay.getOrd();
                    if (ord > -1 && ord != week) {
                        currentDays.remove();
                    } else if (ord == -1) {
                        // last week of month
                        if (nextDate.addWeeks(1).getMonth() == nextDate.getMonth()) {
                            // Not the last month so discard
                            currentDays.remove();
                        } else {
                            if (ordinalDay.getWeekDay().compareTo(currentDay) < 0) {
                                currentDays.remove();
                            } else {
                                return useNextDay(nextDate);
                            }
                        }
                    } else {
                        if (ordinalDay.getWeekDay().compareTo(currentDay) < 0) {
                            currentDays.remove();
                        } else {
                            return useNextDay(nextDate);
                        }
                    }
                }

            }

        }
        return useNextDay(nextDate);
    }

    /**
     * Add 1 to the week number, if we run off the end of the month ask
     * the source for the next month and start at the beginning of that.
     *
     * @param nextDate to add week to
     * @return next date to use
     */
    private DateObject moveToNextWeek(DateObject nextDate) {
        // After we have generated days for the current week move to the next week

        return nextDate.addDays(7);
    }

    /**
     * Pull the next day from the queue and add it to the given date.
     *
     * @param nextDate to add day to
     * @return next valid date
     */
    private DateObject useNextDay(DateObject nextDate) {
        OrdinalDay ordinalDay = currentDays.remove();
        WeekDay weekDay = ordinalDay.getWeekDay();

        nextDate = nextDate.setDay(weekDay);

        return nextDate;
    }

    @Override
    public DateObject initialise(DateObject startDate) {

        DateObject date = source.initialise(startDate);

        currentDays.addAll(allDays);

        OrdinalDay ordinalDay = currentDays.remove();
        WeekDay weekDay = ordinalDay.getWeekDay();

        return date.setDay(weekDay);

    }

}
