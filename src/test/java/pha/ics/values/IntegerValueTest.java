package pha.ics.values;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IntegerValueTest extends TestCase {

    public void testEqualsOnSimilarValues() throws Exception {
        IntegerValue integerValue1 = new IntegerValue("4", null);
        IntegerValue integerValue2 = new IntegerValue("4", null);

        assertTrue("Values not equal", integerValue1.equals(integerValue2));
    }

    public void testEqualsOnDifferentValues() throws Exception {
        IntegerValue integerValue1 = new IntegerValue("4", null);
        IntegerValue integerValue2 = new IntegerValue("40", null);

        assertFalse("Values should not be equal", integerValue1.equals(integerValue2));
    }

    public void testEqualsOnDifferentTypes() throws Exception {
        Value integerValue1 = new IntegerValue("4", null);
        Value integerValue2 = new StringValue("4", null);

        assertFalse("Values should not be equal", integerValue1.equals(integerValue2));
    }

    public void testToFormattedString() throws Exception {
        IntegerValue integerValue1 = new IntegerValue("43", null);

        String formattedString = integerValue1.getValueAsString();

        assertEquals("Incorrect format", "43", formattedString);
    }

    public void testCompareTo() throws Exception {
        IntegerValue integerValue1 = new IntegerValue("4", null);
        IntegerValue integerValue2 = new IntegerValue("43", null);
        IntegerValue integerValue3 = new IntegerValue("3", null);
        IntegerValue integerValue4 = new IntegerValue("73", null);

        List<Value> list = new ArrayList<Value>();

        list.add(integerValue1);
        list.add(integerValue2);
        list.add(integerValue3);
        list.add(integerValue4);

        Collections.sort(list); // Use the IntegerValue.compareTo method

        assertEquals("Incorrect value 0", integerValue3, list.get(0));
        assertEquals("Incorrect value 1", integerValue1, list.get(1));
        assertEquals("Incorrect value 2", integerValue2, list.get(2));
        assertEquals("Incorrect value 3", integerValue4, list.get(3));
    }
}