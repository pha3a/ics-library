package pha.ics;


import org.junit.Test;
import pha.ics.values.DateObject;
import pha.ics.values.DateValue;
import pha.ics.values.RepeatRule;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


/**
 * Created by paul on 21/03/19.
 */
@SuppressWarnings("ConstantConditions")
public class DateFinderLastDateTest extends AbstractTest {


    @Test
    public void testWeeklyWithUntilOnDifferentDayOfWeek() {
        List<PropertyParameter> params = new ArrayList<>();

        String value = "FREQ=WEEKLY;UNTIL=20190303";
        RepeatRule ruleToTest = createRepeatRule(value, params);

        String startDate = "20181219T100000Z";
        DateValue start = new DateValue(startDate, params);


        DateFinder finder = DateFinder.build(start, ruleToTest, null);
        DateObject last = finder.getLast();

        assertEquals("Date should match until", "20190227", last.getDayAsString());

    }

    @Test
    public void testWeeklyWithUntilOnSameDayOfWeek() {
        List<PropertyParameter> params = new ArrayList<>();

        String value = "FREQ=WEEKLY;UNTIL=20190306";
        RepeatRule ruleToTest = createRepeatRule(value, params);

        String startDate = "20181219T100000Z";
        DateValue start = new DateValue(startDate, params);


        DateFinder finder = DateFinder.build(start, ruleToTest, null);
        DateObject last = finder.getLast();

        assertEquals("Date should match until", "20190227", last.getDayAsString());

    }


    @Test
    public void testWeeklyLastDateWithNoEnd() {
        List<PropertyParameter> params = new ArrayList<>();

        String value = "FREQ=WEEKLY;INTERVAL=4";
        RepeatRule ruleToTest = createRepeatRule(value, params);

        String startDate = "20130803T100000Z";
        DateValue start = new DateValue(startDate, params);

        DateFinder finder = DateFinder.build(start, ruleToTest, null);
        DateObject last = finder.getLast();

        assertNull("Date object should be null", last);

    }


    @Test
    public void testWeeklyLastDateWithCount() {
        List<PropertyParameter> params = new ArrayList<>();

        String value = "FREQ=WEEKLY;COUNT=4";
        RepeatRule ruleToTest = createRepeatRule(value, params);

        String startDate = "20180803T100000Z";
        DateValue start = new DateValue(startDate, params);

        DateFinder finder = DateFinder.build(start, ruleToTest, null);
        DateObject contains = finder.getLast();

        assertEquals("Date should match repeat rule", "20180824", contains.getDayAsString());

    }

    @Test
    public void testWeeklyLastDateWithWeekDayAndCount() {
        List<PropertyParameter> params = new ArrayList<>();

        String value = "FREQ=WEEKLY;BYDAY=TH;COUNT=4";
        RepeatRule ruleToTest = createRepeatRule(value, params);

        String startDate = "20181003T100000Z";
        DateValue start = new DateValue(startDate, params);

        DateFinder finder = DateFinder.build(start, ruleToTest, null);
        DateObject contains = finder.getLast();

        assertEquals("Date should match repeat rule", "20181025", contains.getDayAsString());

    }

    @Test
    public void testWeeklyLastDateWithTwoWeekDaysAndCount() {
        List<PropertyParameter> params = new ArrayList<>();

        String value = "FREQ=WEEKLY;BYDAY=TU,TH;COUNT=4";
        RepeatRule ruleToTest = createRepeatRule(value, params);

        String startDate = "20180802T100000Z";
        DateValue start = new DateValue(startDate, params);

        DateFinder finder = DateFinder.build(start, ruleToTest, null);
        DateObject contains = finder.getLast();

        assertEquals("Date should match repeat rule", "20180814", contains.getDayAsString());

    }


    @Test
    public void testMonthlyLastDateWithMonthDayAndCount() {
        List<PropertyParameter> params = new ArrayList<>();

        String value = "FREQ=MONTHLY;BYMONTHDAY=3,4,5,10;COUNT=4";
        RepeatRule ruleToTest = createRepeatRule(value, params);

        String startDate = "20180803T100000Z";
        DateValue start = new DateValue(startDate, params);

        DateFinder finder = DateFinder.build(start, ruleToTest, null);
        DateObject contains = finder.getLast();

        assertEquals("Date should match repeat rule", "20180810", contains.getDayAsString());

    }


    @Test
    public void testMonthlyLastDateWithWeekDayAndCount() {
        List<PropertyParameter> params = new ArrayList<>();

        String value = "FREQ=MONTHLY;BYDAY=TH;COUNT=4";
        RepeatRule ruleToTest = createRepeatRule(value, params);

        String startDate = "20180906T100000Z";
        DateValue start = new DateValue(startDate, params);

        DateFinder finder = DateFinder.build(start, ruleToTest, null);
        DateObject contains = finder.getLast();

        assertEquals("Date should NOT match repeat rule", "20180927", contains.getDayAsString());

    }

    @Test
    public void testMonthlyLastDayFirstThursdayWithCount() {
        List<PropertyParameter> params = new ArrayList<>();

        String value = "FREQ=MONTHLY;BYDAY=1TH;COUNT=2";
        RepeatRule ruleToTest = createRepeatRule(value, params);

        String startDate = "20190307T133000Z";
        DateValue start = new DateValue(startDate, params);

        DateFinder finder = DateFinder.build(start, ruleToTest, null);
        DateObject contains = finder.getLast();

        assertEquals("Date should match repeat rule", "20190404", contains.getDayAsString());

    }

    @Test
    public void testMonthlyLastDayLastThursdayWithCount() {
        List<PropertyParameter> params = new ArrayList<>();

        String value = "FREQ=MONTHLY;BYDAY=-1MO;COUNT=2";
        RepeatRule ruleToTest = createRepeatRule(value, params);

        String startDate = "20190128T133000Z";
        DateValue start = new DateValue(startDate, params);

        DateFinder finder = DateFinder.build(start, ruleToTest, null);
        DateObject contains = finder.getLast();

        assertEquals("Date should match repeat rule", "20190225", contains.getDayAsString());

    }
}