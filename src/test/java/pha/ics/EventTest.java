package pha.ics;

import org.junit.Test;
import pha.ics.values.DateValue;
import pha.ics.values.RepeatRule;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by paul on 17/10/15.
 */
public class EventTest extends AbstractTest {

    @Test
    public void testEventIsRepeating() {
        Event event = new Event();

        RepeatRule rule = createRepeatRule("FREQ=WEEKLY;INTERVAL=1;BYDAY=MO");
        event.addValue(FieldName.RRULE, rule);

        assertTrue("Repeat Rule should be true", event.isRepeating());
    }

    @Test
    public void testEventContainsDate() {
        Event event = new Event();

        RepeatRule rule = createRepeatRule("FREQ=WEEKLY;INTERVAL=1");
        event.addValue(FieldName.RRULE, rule);

        String dateTime = "20111212T100030Z";
        DateValue dateValue = new DateValue(dateTime, null);
        event.setField(FieldName.DTSTART, dateValue);

        String findDate = "20111219T100030Z";
        DateValue dateToFind = new DateValue(findDate, null);

        assertTrue("Event should contain date", event.containsDate(dateToFind));
    }

    @Test
    public void testEventCopyDoesCopyAllFields() {
        Event event = new Event();

        RepeatRule rule = createRepeatRule("FREQ=WEEKLY;INTERVAL=1");
        event.addValue(FieldName.RRULE, rule);

        String dateTime = "20111212T100030Z";
        DateValue dateValue = new DateValue(dateTime, null);
        event.setField(FieldName.DTSTART, dateValue);

        String dateEndTime = "20111212T100030Z";
        DateValue dateEndValue = new DateValue(dateEndTime, null);
        event.setField(FieldName.DTEND, dateEndValue);

        Event targetEvent = new Event();

        assertNotEquals("Events should be different", targetEvent, event);

        targetEvent.copyFields(event.getFieldNames(), event);

        assertEquals("Event should be the same", targetEvent, event);
    }

    @Test
    public void testAddedCategoryCanBeRetrieved() {
        Event event = new Event();

        assertTrue("Categories should be empty", event.getCategories().isEmpty());

        event.addCategory("CAR");

        assertTrue("Category should be present", event.getCategories().contains("CAR"));

        event.addCategory("BIG-BUS");

        assertTrue("Category should be present", event.getCategories().contains("CAR"));
        assertTrue("Category should be present", event.getCategories().contains("BIG-BUS"));


    }

    @Test
    public void testRemovedCategoryNoLongerPresent() {
        Event event = new Event();

        event.addCategory("CAR");
        event.addCategory("BIG-BUS");
        event.addCategory("TRAIN");

        assertTrue("Category should be present", event.getCategories().contains("CAR"));
        assertTrue("Category should be present", event.getCategories().contains("BIG-BUS"));
        assertTrue("Category should be present", event.getCategories().contains("TRAIN"));

        event.removeCategory("TRAIN");

        assertTrue("Category should be present", event.getCategories().contains("CAR"));
        assertTrue("Category should be present", event.getCategories().contains("BIG-BUS"));
        assertFalse("Category should NOT be present", event.getCategories().contains("TRAIN"));

       // Remove category not present
        event.removeCategory("TRAIN");

        assertTrue("Category should be present", event.getCategories().contains("CAR"));
        assertTrue("Category should be present", event.getCategories().contains("BIG-BUS"));
        assertFalse("Category should NOT be present", event.getCategories().contains("TRAIN"));

    }

    @Test
    public void testAddedCategoriesAreSorted() {
        Event event = new Event();

        event.addCategory("CAR");
        event.addCategory("BIG-BUS");
        event.addCategory("TRAIN");

        assertTrue("Category should be present", event.getCategories().contains("CAR"));
        assertTrue("Category should be present", event.getCategories().contains("BIG-BUS"));
        assertTrue("Category should be present", event.getCategories().contains("TRAIN"));

        List<String> categories = event.getCategories();
        assertEquals("Category should be sorted", "BIG-BUS", categories.get(0));
        assertEquals("Category should be sorted", "CAR", categories.get(1));
        assertEquals("Category should be sorted", "TRAIN", categories.get(2));

    }
}