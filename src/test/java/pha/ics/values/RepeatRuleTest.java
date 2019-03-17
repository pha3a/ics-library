package pha.ics.values;

import org.junit.Test;
import pha.ics.AbstractTest;
import pha.ics.PropertyParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;


public class RepeatRuleTest extends AbstractTest {



    @Test
    public void testEqualOnIdenticalObjects() {

        List<PropertyParameter> params = new ArrayList<>();

        String value = "FREQ=WEEKLY;INTERVAL=2;BYDAY=MO,TU,WE,TH,FR;UNTIL=20130803T083000Z";
        RepeatRule rr1 = createRepeatRule(value, params);
        RepeatRule rr2 = createRepeatRule(value, params);

        assertTrue("Rules are not equal", rr1.equals(rr2));
    }

    @Test
    public void testEqualOnSameObject() {

        List<PropertyParameter> params = new ArrayList<>();

        String value = "FREQ=WEEKLY;INTERVAL=2;BYDAY=MO,TU,WE,TH,FR;UNTIL=20130803T083000Z";
        RepeatRule rr1 = createRepeatRule(value, params);

        assertTrue("Rules are not equal", rr1.equals(rr1));
    }

    @Test
    public void testCountManagementIsCorrect() {
        Map<RepeatRule.Key, String> result = new HashMap<>();

        result.put(RepeatRule.Key.FREQ, "WEEKLY");
        result.put(RepeatRule.Key.BYDAY, "TU");

        RepeatRule repeatRule = new RepeatRule(result, null);

        // Check default value
        assertEquals("Incorrect count", -1, repeatRule.getCount());
        assertFalse("Incorrect count flag", repeatRule.hasCount());

        repeatRule.setCount(2);

        // Check value set
        assertEquals("Incorrect count", 2, repeatRule.getCount());
        assertTrue("Incorrect count flag", repeatRule.hasCount());

        repeatRule.clearCount();

        // Check clearing value
        assertEquals("Incorrect count", -1, repeatRule.getCount());
        assertFalse("Incorrect count flag", repeatRule.hasCount());

    }
}

