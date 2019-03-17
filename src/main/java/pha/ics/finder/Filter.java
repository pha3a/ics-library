package pha.ics.finder;

import pha.ics.values.DateObject;

/**
 * Used to generate dates based on the start date given at initialization and
 * configuration specified on construction.
 *
 * Created by paul on 23/10/15.
 */
public interface Filter {

    /**
     * Return the next date object in the sequence, this is normally
     * generated based on the start date, passed to the initialise method
     * and information supplied on construction.
     * These objects may be chained to expand or restrict the dates generated.
     *
     * @param currentDate Normally the last date generated
     * @return Next date in sequence
     */
    DateObject getNextDate(DateObject currentDate);

    /**
     * Initalize this filter based on the supplied startDate
     *
     * @param startDate First date in sequence.
     * @return First date generated.
     */
    DateObject initialise(DateObject startDate);
}
