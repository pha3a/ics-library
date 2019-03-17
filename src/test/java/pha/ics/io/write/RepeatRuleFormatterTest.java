package pha.ics.io.write;

import org.junit.Test;
import pha.ics.AbstractTest;
import pha.ics.PropertyParameter;
import pha.ics.WeekDay;
import pha.ics.values.DateObject;
import pha.ics.values.OrdinalDay;
import pha.ics.values.RepeatRule;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by paul on 25/07/16.
 */
public class RepeatRuleFormatterTest extends AbstractTest {

    @Test
    public void testToFormattedStringWithWeeklyOnMonday() throws Exception {

        List<PropertyParameter> params = new ArrayList<>();

        RepeatRule rr = createRepeatRule("FREQ=WEEKLY;INTERVAL=1;BYDAY=MO", params);

        String formatted = RepeatRuleFormatter.format(rr);
        assertEquals("Incorrect format", "FREQ=WEEKLY;INTERVAL=1;BYDAY=MO", formatted);

        assertTrue("Not Weekly", rr.isWeekly());
        assertFalse("Should not be Monthly", rr.isMonthly());
        assertFalse("Should not be Yearly", rr.isYearly());
        assertEquals("Interval not 1", 1, rr.getInterval());
        assertEquals("ByDay is wrong", WeekDay.MO, rr.getByDay().get(0).getWeekDay());
    }

    @Test
    public void testToFormattedStringWithUntil() throws Exception {

        List<PropertyParameter> params = new ArrayList<>();

        RepeatRule rr = createRepeatRule("FREQ=WEEKLY;INTERVAL=1;BYDAY=TU;UNTIL=20140107T160000Z", params);

        String expectedValue = "FREQ=WEEKLY;UNTIL=20140107T160000Z;INTERVAL=1;BYDAY=TU";

        String formatted = RepeatRuleFormatter.format(rr);
        assertEquals("Incorrect format", expectedValue, formatted);

        assertTrue("Not Weekly", rr.isWeekly());
        assertFalse("Should not be Monthly", rr.isMonthly());
        assertFalse("Should not be Yearly", rr.isYearly());
        assertEquals("Interval not 1", 1, rr.getInterval());
        assertEquals("ByDay is wrong", WeekDay.TU, rr.getByDay().get(0).getWeekDay());
        assertEquals("Incorrect until", new DateObject("20140107T160000Z", null, false), rr.getUntil());
    }

    @Test
    public void testToFormattedStringWithMultipleDays() throws Exception {

        List<PropertyParameter> params = new ArrayList<>();

        RepeatRule rr = createRepeatRule("FREQ=WEEKLY;INTERVAL=2;BYDAY=MO,TU,WE,TH,FR;UNTIL=20130803T083000Z", params);

        String expectedValue =
                "FREQ=WEEKLY;UNTIL=20130803T083000Z;INTERVAL=2;BYDAY=MO,TU,WE,TH,FR";

        String formatted = RepeatRuleFormatter.format(rr);
        assertEquals("Incorrect format", expectedValue, formatted);

        assertTrue("Not Weekly", rr.isWeekly());
        assertFalse("Should not be Monthly", rr.isMonthly());
        assertFalse("Should not be Yearly", rr.isYearly());
        assertEquals("Interval wrong", 2, rr.getInterval());
        List<OrdinalDay> byDay = rr.getByDay();
        assertEquals("ByDay is wrong", WeekDay.MO, byDay.get(0).getWeekDay());
        assertEquals("ByDay is wrong", WeekDay.TU, byDay.get(1).getWeekDay());
        assertEquals("ByDay is wrong", WeekDay.WE, byDay.get(2).getWeekDay());
        assertEquals("ByDay is wrong", WeekDay.TH, byDay.get(3).getWeekDay());
        assertEquals("ByDay is wrong", WeekDay.FR, byDay.get(4).getWeekDay());
        assertEquals("Incorrect until", new DateObject("20130803T083000Z", null, false), rr.getUntil());
    }
}