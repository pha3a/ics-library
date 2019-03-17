package pha.ics.values;

import junit.framework.TestCase;

import java.util.List;

/**
 * Test the behaviour of a Compound Value
 */
public class CompoundValueTest extends TestCase {

    public void testGetValues() {
        Value value = new StringValue("XYZ");
        CompoundValue compoundValue = new CompoundValue(value);

        List<Value> returnedValues = compoundValue.getValues();
        assertFalse("Should not be empty", returnedValues.isEmpty());
        assertEquals("Should have 1 value", 1, returnedValues.size());

        compoundValue.add(new StringValue("ABC"));

        assertFalse("Should not be empty", returnedValues.isEmpty());
        assertEquals("Should have 2 values", 2, returnedValues.size());
    }

    public void testToFormattedString() {

    }

    public void testEqualsOnSame() {
        CompoundValue compoundValue = new CompoundValue(new StringValue("XYZ"));
        compoundValue.add(new StringValue("ABC"));

        assertEquals("Should be the same", compoundValue, compoundValue);
    }

    public void testEqualsOnCopy() {
        CompoundValue compoundValue1 = new CompoundValue(new StringValue("XYZ"));
        compoundValue1.add(new StringValue("ABC"));

        CompoundValue compoundValue2 = new CompoundValue(new StringValue("XYZ"));
        compoundValue2.add(new StringValue("ABC"));

        assertEquals("Should be the same", compoundValue1, compoundValue2);
    }

    public void testEqualsOnDiffOrderCopy()  {
        CompoundValue compoundValue1 = new CompoundValue(new StringValue("XYZ"));
        compoundValue1.add(new StringValue("ABC"));

        CompoundValue compoundValue2 = new CompoundValue(new StringValue("ABC"));
        compoundValue2.add(new StringValue("XYZ"));

        assertEquals("Should be the same", compoundValue1, compoundValue2);
    }

    public void testEqualsOnDifferent() {
        CompoundValue compoundValue1 = new CompoundValue(new StringValue("XYZ"));
        compoundValue1.add(new StringValue("ABC"));

        CompoundValue compoundValue2 = new CompoundValue(new StringValue("XYZ"));

        assertNotSame("Should not be the same", compoundValue1, compoundValue2);
    }

    public void testAddDuplicateValuesOnlyKeepsOneCopy() {
        CompoundValue compoundValue1 = new CompoundValue(new StringValue("ABC"));
        compoundValue1.add(new StringValue("ABC"));

        assertEquals("Incorrect length", 1, compoundValue1.size());
    }

    public void testAddCompundValueFlattensList() {
        CompoundValue compoundValue1 = new CompoundValue(new StringValue("ABC"));
        compoundValue1.add(new StringValue("ONE-TWO"));
        CompoundValue compoundValue2 = new CompoundValue(new StringValue("LIST2"));
        compoundValue2.add(new StringValue("LIST-2-PART2"));

        compoundValue1.add(compoundValue2);

        assertEquals("Incorrect length", 4, compoundValue1.size());

        assertEquals("Incorrect items", "StringValue{ABC}\n" +
                        "StringValue{ONE-TWO}\n" +
                        "StringValue{LIST2}\n" +
                        "StringValue{LIST-2-PART2}\n"
                , compoundValue1.toString());

    }

    public void testAddedItemsKeptInOriginalOrder() {
        CompoundValue compoundValue1 = new CompoundValue(new StringValue("ABC"));
        compoundValue1.add(new StringValue("ONE"));
        compoundValue1.add(new StringValue("1234"));
        compoundValue1.add(new StringValue("BIG Dog on bord"));

        assertEquals("Incorrect length", 4, compoundValue1.size());

        assertEquals("Incorrect items",
                "StringValue{ABC}\n" +
                        "StringValue{ONE}\n" +
                        "StringValue{1234}\n" +
                        "StringValue{BIG Dog on bord}\n"
                , compoundValue1.toString());

    }
}