package pha.ics.values;

import org.junit.Test;
import pha.ics.AbstractTest;
import pha.ics.PropertyParameter;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by paul on 02/11/14.
 */
public class StringListTest extends AbstractTest {

    @Test
    public void testSameStringList() {
        List<String> strings = Arrays.asList("A","B","C");
        StringList list1 = new StringList(strings, null);

        assertTrue("List is not identical", list1.equals(list1));
    }

    @Test
    public void testIdenticalStringList() {
        List<String> strings = Arrays.asList("A","B","C");
        StringList list1 = new StringList(strings, null);
        StringList list2 = new StringList(strings, null);

        assertTrue("List is not identical", list1.equals(list2));
    }

    @Test
    public void testDifferentStringList() {
        List<String> strings = Arrays.asList("A","B","C");
        StringList list1 = new StringList(strings, null);
        List<String> strings1 = Arrays.asList("A","B");
        StringList list2 = new StringList(strings1, null);

        assertFalse("List is not identical", list1.equals(list2));
    }

    @Test
    public void testToFormattedStringOn2ItemList() {
        List<String> strings = Arrays.asList("A","B");
        StringList list = new StringList(strings, null);

        assertEquals("List is not correct", "[A, B]", list.getValueAsString());
    }

    @Test
    public void testToFormattedStringWithParam() {
        List<PropertyParameter> params = createParameters("X-TYPE=DATE");
        List<String> strings = Arrays.asList("A","B");
        StringList list = new StringList(strings, params);

        assertEquals("List is not correct", "[A, B]", list.getValueAsString());
    }

    @Test
    public void testAddToStringListIsStored() {
        List<String> strings = Arrays.asList("A","B","C");
        StringList list = new StringList(strings, null);

        assertEquals("List is not correct at start", "[A, B, C]", list.getValueAsString());

        list.add("DOG");

        assertEquals("List is not correct after add", "[A, B, C, DOG]", list.getValueAsString());

    }


    @Test
    public void testRemoveFromListRemovesItem() {

        StringList list = new StringList(null, null);

        list.add("DOG");
        list.add("CAT");
        list.add("ZOO");

        assertEquals("List is not correct after add", "[CAT, DOG, ZOO]", list.getValueAsString());

        list.remove("DOG");

        assertEquals("List is not correct after add", "[CAT, ZOO]", list.getValueAsString());

    }

    @Test
    public void testContainsReturnsTrueForPresentItem() {

        StringList list = new StringList(null, null);

        list.add("DOG");
        list.add("CAT");
        list.add("ZOO");


        assertTrue("Item should be present", list.contains("ZOO"));

    }

    @Test
    public void testContainsReturnsFalseForNotPresentItem() {

        StringList list = new StringList(null, null);

        list.add("DOG");
        list.add("CAT");
        list.add("ZOO");


        assertFalse("Item should not be present", list.contains("ZO"));

    }
}
