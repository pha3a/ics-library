package pha.ics.io.write;

import org.junit.Test;
import pha.ics.values.UTCOffset;

import static org.junit.Assert.assertEquals;
import static pha.ics.FieldName.TZOFFSETFROM;

/**
 * Created by paul on 31/07/16.
 */
public class ValueFormatterTest {

    @Test
    public void formatPositiveUTCOffset() {
        UTCOffset utcOffset = new UTCOffset("+0500", null);

        assertEquals("Incorrect format", "+0500", ValueFormatter.format(TZOFFSETFROM, utcOffset));
    }

    @Test
    public void formatNegativeUTCOffset() {
        UTCOffset utcOffset = new UTCOffset("-0130", null);

        assertEquals("Incorrect format", "-0130", ValueFormatter.format(TZOFFSETFROM, utcOffset));
    }

    @Test
    public void formatUTCOffsetWithSeconds() {
        UTCOffset utcOffset = new UTCOffset("+013010", null);

        assertEquals("Incorrect format", "+013010", ValueFormatter.format(TZOFFSETFROM, utcOffset));
    }
}