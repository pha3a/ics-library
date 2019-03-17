package pha.ics.values;

import org.junit.Test;
import pha.ics.PropertyParameter;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by paul on 15/07/16.
 */
public class UTCOffsetTest {

    @Test
    public void testThatEqualReturnsTrueForIdenticalValues() {

        List<PropertyParameter> params = new ArrayList<>();
        UTCOffset UTCOffset = new UTCOffset("-0400", params);

        UTCOffset UTCOffset2 = new UTCOffset("-0400", params);

        assertTrue("Should be the same", UTCOffset.equals(UTCOffset2));
    }

    @Test
    public void testThatEqualReturnsFalseForDifferentValues() {

        List<PropertyParameter> params = new ArrayList<>();
        UTCOffset UTCOffset = new UTCOffset("-0400", params);

        UTCOffset UTCOffset2 = new UTCOffset("-0300", params);

        assertFalse("Should be the different", UTCOffset.equals(UTCOffset2));
    }

    @Test
    public void testThatCompareReturnsOneForDifferentValues() {

        List<PropertyParameter> params = new ArrayList<>();
        UTCOffset UTCOffset = new UTCOffset("-0400", params);

        UTCOffset UTCOffset2 = new UTCOffset("-0300", params);

        assertEquals("Should be the different", 1, UTCOffset.compareTo(UTCOffset2));
    }

    @Test
    public void testThatCompareReturnsOneForDifferentValuesWithDifferentSigns() {

        List<PropertyParameter> params = new ArrayList<>();
        UTCOffset UTCOffset = new UTCOffset("-0400", params);

        UTCOffset UTCOffset2 = new UTCOffset("+0100", params);

        assertEquals("Should be the different", 2, UTCOffset.compareTo(UTCOffset2));
    }

}