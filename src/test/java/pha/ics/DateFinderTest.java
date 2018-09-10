package pha.ics;


import org.junit.Test;
import pha.ics.values.DateValue;
import pha.ics.values.RepeatRule;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Created by paul on 18/10/15.
 */
public class DateFinderTest extends AbstractTest {


    @Test
    public void testWeeklyFindMatchesWhenDateBeforeUntil() {
        List<PropertyParameter> params = new ArrayList<>();

        String value = "FREQ=WEEKLY;UNTIL=20130803";
        RepeatRule ruleToTest = createRepeatRule(value, params);

        String startDate = "20121219T100000Z";
        DateValue start = new DateValue(startDate, params);

        String dateTime = "20130220T100000Z";
        DateValue dateToMatch = new DateValue(dateTime, params);

        DateFinder finder = DateFinder.build(start, ruleToTest, null);
        boolean contains = finder.find(dateToMatch);

        assertTrue("Date should match repreat rule", contains);

    }

    @Test
    public void testWeeklyFindFailsWhenDateMatchesButTimesDoesNot() {
        List<PropertyParameter> params = new ArrayList<>();

        String value = "FREQ=WEEKLY;UNTIL=20130803";
        RepeatRule ruleToTest = createRepeatRule(value, params);

        String startDate = "20121219T100000Z";
        DateValue start = new DateValue(startDate, params);

        String dateTime = "20130220T102000Z";
        DateValue dateToMatch = new DateValue(dateTime, params);

        DateFinder finder = DateFinder.build(start, ruleToTest, null);
        boolean contains = finder.find(dateToMatch);

        assertFalse("Date should match repreat rule", contains);

    }

    @Test
    public void testWeeklyFindFailsWhenDateAfterUntil() {
        List<PropertyParameter> params = new ArrayList<>();

        RepeatRule ruleToTest = createRepeatRule("FREQ=WEEKLY;UNTIL=20130803", params);

        String startDate = "20121219T100000Z";
        DateValue start = new DateValue(startDate, params);

        String dateTime = "20140220T100000Z";
        DateValue dateToMatch = new DateValue(dateTime, params);

        DateFinder finder = DateFinder.build(start, ruleToTest, null);
        boolean contains = finder.find(dateToMatch);

        assertFalse("Date should match repreat rule", contains);

    }

    @Test
    public void testWeeklyFindFailsWithDateBeforeStart() {
        List<PropertyParameter> params = new ArrayList<>();

        RepeatRule ruleToTest = createRepeatRule("FREQ=WEEKLY;UNTIL=20130803", params);

        String startDate = "20121219T100000";
        DateValue start = new DateValue(startDate, params);

        String dateTime = "20111219T100030Z";
        DateValue dateToMatch = new DateValue(dateTime, params);

        DateFinder finder = DateFinder.build(start, ruleToTest, null);
        boolean contains = finder.find(dateToMatch);

        assertFalse("Date should NOT match repreat rule", contains);

    }

    @Test
    public void testDailyFindMatchesWithDayRepeat() {
        List<PropertyParameter> params = new ArrayList<>();

        RepeatRule ruleToTest = createRepeatRule("FREQ=DAILY;UNTIL=20130803", params);

        String startDate = "20130719T100000Z";
        DateValue start = new DateValue(startDate, params);

        String dateTime = "20130801T100000Z";
        DateValue dateToMatch = new DateValue(dateTime, params);

        DateFinder finder = DateFinder.build(start, ruleToTest, null);
        boolean contains = finder.find(dateToMatch);

        assertTrue("Date should match repreat rule", contains);

    }

    @Test
    public void testDailyFindFailsWithDayAndIntervalRepeat() {
        List<PropertyParameter> params = new ArrayList<>();

        String value = "FREQ=DAILY;INTERVAL=2;UNTIL=20130813";
        RepeatRule ruleToTest = createRepeatRule(value, params);

        String startDate = "20130803T100000Z";
        DateValue start = new DateValue(startDate, params);

        String dateTime = "20130810T100000Z";
        DateValue dateToMatch = new DateValue(dateTime, params);

        DateFinder finder = DateFinder.build(start, ruleToTest, null);
        boolean contains = finder.find(dateToMatch);

        assertFalse("Date should NOT match repreat rule", contains);

    }

    @Test
    public void testWeeklyFindMatchesWithDayAndIntervalRepeat() {
        List<PropertyParameter> params = new ArrayList<>();

        String value = "FREQ=WEEKLY;INTERVAL=4;UNTIL=20131213";
        RepeatRule ruleToTest = createRepeatRule(value, params);

        String startDate = "20130803T100000Z";
        DateValue start = new DateValue(startDate, params);

        String dateTime = "20130928T100000Z";
        DateValue dateToMatch = new DateValue(dateTime, params);

        DateFinder finder = DateFinder.build(start, ruleToTest, null);
        boolean contains = finder.find(dateToMatch);

        assertTrue("Date should match repreat rule", contains);

    }

    @Test
    public void testWeeklyFindMatchesWithDayAndCountRepeat() {
        List<PropertyParameter> params = new ArrayList<>();

        String value = "FREQ=WEEKLY;COUNT=4";
        RepeatRule ruleToTest = createRepeatRule(value, params);

        String startDate = "20130803T100000Z";
        DateValue start = new DateValue(startDate, params);

        String dateTime = "20130810T100000Z";
        DateValue dateToMatch = new DateValue(dateTime, params);

        DateFinder finder = DateFinder.build(start, ruleToTest, null);
        boolean contains = finder.find(dateToMatch);

        assertTrue("Date should match repreat rule", contains);

    }

    @Test
    public void testWeeklyFindFailsWithDayAndCountRepeat() {
        List<PropertyParameter> params = new ArrayList<>();

        String value = "FREQ=WEEKLY;COUNT=9";
        RepeatRule ruleToTest = createRepeatRule(value, params);

        String startDate = "20130803T100000Z";
        DateValue start = new DateValue(startDate, params);

        String dateTime = "20131005T100000Z";
        DateValue dateToMatch = new DateValue(dateTime, params);

        DateFinder finder = DateFinder.build(start, ruleToTest, null);
        boolean contains = finder.find(dateToMatch);

        assertFalse("Date should NOT match repreat rule", contains);

    }

    @Test
    public void testWeeklyFindMatchesWithByDayThatMatches() {
        List<PropertyParameter> params = new ArrayList<>();

        String value = "FREQ=WEEKLY;BYDAY=TH;COUNT=4";
        RepeatRule ruleToTest = createRepeatRule(value, params);

        String startDate = "20150803T100000Z";
        DateValue start = new DateValue(startDate, params);

        String dateTime = "20150813T100000Z";
        DateValue dateToMatch = new DateValue(dateTime, params);

        DateFinder finder = DateFinder.build(start, ruleToTest, null);
        boolean contains = finder.find(dateToMatch);

        assertTrue("Date should match repreat rule", contains);

    }

    @Test
    public void testWeeklyFindMatchesWithByDayListThatMatches() {
        List<PropertyParameter> params = new ArrayList<>();

        String value = "FREQ=WEEKLY;BYDAY=TU,TH;COUNT=4";
        RepeatRule ruleToTest = createRepeatRule(value, params);

        String startDate = "20150803T100000Z";
        DateValue start = new DateValue(startDate, params);

        String dateTime = "20150813T100000Z";
        DateValue dateToMatch = new DateValue(dateTime, params);

        DateFinder finder = DateFinder.build(start, ruleToTest, null);
        boolean contains = finder.find(dateToMatch);

        assertTrue("Date should match repreat rule", contains);

    }

    @Test
    public void testWeeklyFindMatchesWithByDayWhenDatesSpanSummerTime() {
        List<PropertyParameter> params = new ArrayList<>();

        String value = "FREQ=WEEKLY;BYDAY=TU";
        RepeatRule ruleToTest = createRepeatRule(value, params);

        String startDate = "20150303T100000Z";
        DateValue start = new DateValue(startDate, params);

        String dateTime = "20150414T090000Z";
        DateValue dateToMatch = new DateValue(dateTime, params);

        DateFinder finder = DateFinder.build(start, ruleToTest, null);
        boolean contains = finder.find(dateToMatch);

        assertTrue("Date should match repreat rule", contains);

    }

    @Test
    public void testWeeklyFindFailsWithByDayThatDoesNotMatch() {
        List<PropertyParameter> params = new ArrayList<>();

        String value = "FREQ=WEEKLY;BYDAY=TH;COUNT=4";
        RepeatRule ruleToTest = createRepeatRule(value, params);

        String startDate = "20150803T100000Z";
        DateValue start = new DateValue(startDate, params);

        String dateTime = "20150814T100000Z";
        DateValue dateToMatch = new DateValue(dateTime, params);

        DateFinder finder = DateFinder.build(start, ruleToTest, null);
        boolean contains = finder.find(dateToMatch);

        assertFalse("Date should NOT match repreat rule", contains);

    }

//    ###############

    @Test
    public void testMonthlyFindMatchesWhenDateBeforeUntil() {
        List<PropertyParameter> params = new ArrayList<>();

        String value = "FREQ=MONTHLY;UNTIL=20130803";
        RepeatRule ruleToTest = createRepeatRule(value, params);

        String startDate = "20121219T100000Z";
        DateValue start = new DateValue(startDate, params);

        String dateTime = "20130219T100000Z";
        DateValue dateToMatch = new DateValue(dateTime, params);

        DateFinder finder = DateFinder.build(start, ruleToTest, null);
        boolean contains = finder.find(dateToMatch);

        assertTrue("Date should match repreat rule", contains);

    }

    @Test
    public void testMonthlyFindFailsWhenDateMatchesButTimesDoesNot() {
        List<PropertyParameter> params = new ArrayList<>();

        String value = "FREQ=MONTHLY;UNTIL=20130803";
        RepeatRule ruleToTest = createRepeatRule(value, params);

        String startDate = "20121219T100000Z";
        DateValue start = new DateValue(startDate, params);

        String dateTime = "20130220T102000Z";
        DateValue dateToMatch = new DateValue(dateTime, params);

        DateFinder finder = DateFinder.build(start, ruleToTest, null);
        boolean contains = finder.find(dateToMatch);

        assertFalse("Date should match repreat rule", contains);

    }

    @Test
    public void testMonthlyFindFailsWhenDateAfterUntil() {
        List<PropertyParameter> params = new ArrayList<>();

        String value = "FREQ=MONTHLY;UNTIL=20130803";
        RepeatRule ruleToTest = createRepeatRule(value, params);

        String startDate = "20121219T100000Z";
        DateValue start = new DateValue(startDate, params);

        String dateTime = "20140220T100000Z";
        DateValue dateToMatch = new DateValue(dateTime, params);

        DateFinder finder = DateFinder.build(start, ruleToTest, null);
        boolean contains = finder.find(dateToMatch);

        assertFalse("Date should match repreat rule", contains);

    }

    @Test
    public void testMonthlyFindFailsWithDateBeforeStart() {
        List<PropertyParameter> params = new ArrayList<>();

        String value = "FREQ=MONTHLY;UNTIL=20130803";
        RepeatRule ruleToTest = createRepeatRule(value, params);

        String startDate = "20121219T100000";
        DateValue start = new DateValue(startDate, params);

        String dateTime = "20111219T100030Z";
        DateValue dateToMatch = new DateValue(dateTime, params);

        DateFinder finder = DateFinder.build(start, ruleToTest, null);
        boolean contains = finder.find(dateToMatch);

        assertFalse("Date should NOT match repreat rule", contains);

    }

    @Test
    public void testMonthlyFindMatchesWithDayRepeat() {
        List<PropertyParameter> params = new ArrayList<>();

        String value = "FREQ=MONTHLY;UNTIL=20140803";
        RepeatRule ruleToTest = createRepeatRule(value, params);

        String startDate = "20130719T100000Z";
        DateValue start = new DateValue(startDate, params);

        String dateTime = "20130919T100000Z";
        DateValue dateToMatch = new DateValue(dateTime, params);

        DateFinder finder = DateFinder.build(start, ruleToTest, null);
        boolean contains = finder.find(dateToMatch);

        assertTrue("Date should match repreat rule", contains);

    }

    @Test
    public void testMonthlyFindFailsWithDayAndIntervalRepeat() {
        List<PropertyParameter> params = new ArrayList<>();

        String value = "FREQ=MONTHLY;INTERVAL=2;UNTIL=20130813";
        RepeatRule ruleToTest = createRepeatRule(value, params);

        String startDate = "20130803T100000Z";
        DateValue start = new DateValue(startDate, params);

        String dateTime = "20130810T100000Z";
        DateValue dateToMatch = new DateValue(dateTime, params);

        DateFinder finder = DateFinder.build(start, ruleToTest, null);
        boolean contains = finder.find(dateToMatch);

        assertFalse("Date should NOT match repreat rule", contains);

    }

    @Test
    public void testMonthlyFindMatchesWithUntilAndInterval() {
        List<PropertyParameter> params = new ArrayList<>();

        String value = "FREQ=MONTHLY;INTERVAL=4;UNTIL=20140803";
        RepeatRule ruleToTest = createRepeatRule(value, params);

        String startDate = "20130803T100000Z";
        DateValue start = new DateValue(startDate, params);

        String dateTime = "20140403T100000Z";
        DateValue dateToMatch = new DateValue(dateTime, params);

        DateFinder finder = DateFinder.build(start, ruleToTest, null);
        boolean contains = finder.find(dateToMatch);

        assertTrue("Date should match repreat rule", contains);

    }

    @Test
    public void testMonthlyFindMatchesWithByMonthDayAndCountRepeat() {
        List<PropertyParameter> params = new ArrayList<>();

        String value = "FREQ=MONTHLY;BYMONTHDAY=3,4,5,10;COUNT=4";
        RepeatRule ruleToTest = createRepeatRule(value, params);

        String startDate = "20130803T100000Z";
        DateValue start = new DateValue(startDate, params);

        String dateTime = "20130810T100000Z";
        DateValue dateToMatch = new DateValue(dateTime, params);

        DateFinder finder = DateFinder.build(start, ruleToTest, null);
        boolean contains = finder.find(dateToMatch);

        assertTrue("Date should match repreat rule", contains);

    }

    @Test
    public void testMonthlyFindFailsWithByMonthDayAndCountRepeat() {
        List<PropertyParameter> params = new ArrayList<>();

        String value = "FREQ=MONTHLY;BYMONTHDAY=3,4,10;COUNT=9";
        RepeatRule ruleToTest = createRepeatRule(value, params);

        String startDate = "20130803T100000Z";
        DateValue start = new DateValue(startDate, params);

        String dateTime = "20131005T100000Z";
        DateValue dateToMatch = new DateValue(dateTime, params);

        DateFinder finder = DateFinder.build(start, ruleToTest, null);
        boolean contains = finder.find(dateToMatch);

        assertFalse("Date should NOT match repreat rule", contains);

    }

    @Test
    public void testMonthlyFindMatchesWithByDayAndCount() {
        List<PropertyParameter> params = new ArrayList<>();

        String value = "FREQ=MONTHLY;BYDAY=TH;COUNT=8";
        RepeatRule ruleToTest = createRepeatRule(value, params);

        String startDate = "20150806T100000Z";
        DateValue start = new DateValue(startDate, params);

        String dateTime = "20150827T100000Z";
        DateValue dateToMatch = new DateValue(dateTime, params);

        DateFinder finder = DateFinder.build(start, ruleToTest, null);
        boolean contains = finder.find(dateToMatch);

        assertTrue("Date should match repreat rule", contains);

    }

    @Test
    public void testMonthlyFindMatchesWithByDayListThatMatches() {
        List<PropertyParameter> params = new ArrayList<>();

        String value = "FREQ=MONTHLY;BYDAY=TU,TH;COUNT=4";
        RepeatRule ruleToTest = createRepeatRule(value, params);

        String startDate = "20150803T100000Z";
        DateValue start = new DateValue(startDate, params);

        String dateTime = "20150813T100000Z";
        DateValue dateToMatch = new DateValue(dateTime, params);

        DateFinder finder = DateFinder.build(start, ruleToTest, null);
        boolean contains = finder.find(dateToMatch);

        assertTrue("Date should match repreat rule", contains);

    }

    @Test
    public void testMonthlyFindFailsWithByDayThatDoesNotMatch() {
        List<PropertyParameter> params = new ArrayList<>();

        String value = "FREQ=MONTHLY;BYDAY=TH;COUNT=4";
        RepeatRule ruleToTest = createRepeatRule(value, params);

        String startDate = "20150803T100000Z";
        DateValue start = new DateValue(startDate, params);

        String dateTime = "20150814T100000Z";
        DateValue dateToMatch = new DateValue(dateTime, params);

        DateFinder finder = DateFinder.build(start, ruleToTest, null);
        boolean contains = finder.find(dateToMatch);

        assertFalse("Date should NOT match repreat rule", contains);

    }

    @Test
    public void testMonthlyFindByDayWithWeekNumber() {
        List<PropertyParameter> params = new ArrayList<>();

        String value = "FREQ=MONTHLY;BYDAY=1TH;COUNT=2";
        RepeatRule ruleToTest = createRepeatRule(value, params);

        String startDate = "20030731T133000Z";
        DateValue start = new DateValue(startDate, params);

        String dateTime = "20030807T133000Z";
        DateValue dateToMatch = new DateValue(dateTime, params);

        DateFinder finder = DateFinder.build(start, ruleToTest, null);
        boolean contains = finder.find(dateToMatch);

        assertTrue("Date should match repreat rule", contains);

    }

    @Test
    public void testMonthlyFindByDayWithWeekNumberMinusOne() {
        List<PropertyParameter> params = new ArrayList<>();

        String value = "FREQ=MONTHLY;BYDAY=-1TH;COUNT=2";
        RepeatRule ruleToTest = createRepeatRule(value, params);

        String startDate = "20030810T133000Z";
        DateValue start = new DateValue(startDate, params);

        String dateTime = "20030828T133000Z";
        DateValue dateToMatch = new DateValue(dateTime, params);

        DateFinder finder = DateFinder.build(start, ruleToTest, null);
        boolean contains = finder.find(dateToMatch);

        assertTrue("Date should match repreat rule", contains);

    }
}