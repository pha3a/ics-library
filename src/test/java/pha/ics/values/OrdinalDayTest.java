package pha.ics.values;

import junit.framework.TestCase;
import pha.ics.values.OrdinalDay;

public class OrdinalDayTest extends TestCase {

    public void testEqualsOnIdenticalObjects() {
        OrdinalDay ordinalDay1 = new OrdinalDay("MO");
        OrdinalDay ordinalDay2 = new OrdinalDay("MO");

        assertTrue("Not the same", ordinalDay1.equals(ordinalDay2));
    }

    public void testEqualsOnDifferentObjects() {
        OrdinalDay ordinalDay1 = new OrdinalDay("MO");
        OrdinalDay ordinalDay2 = new OrdinalDay("WE");

        assertFalse("Should not be the same", ordinalDay1.equals(ordinalDay2));
    }

    public void testEqualsOnDifferentOrdObjects() {
        OrdinalDay ordinalDay1 = new OrdinalDay("3MO");
        OrdinalDay ordinalDay2 = new OrdinalDay("1MO");

        assertFalse("Should not be the same", ordinalDay1.equals(ordinalDay2));
    }
}