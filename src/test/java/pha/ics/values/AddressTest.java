package pha.ics.values;

import org.junit.Test;
import pha.ics.AbstractTest;
import pha.ics.PropertyParameter;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Verify the correct behaviour of the Address class.
 *
 * Created by paul on 22/11/14.
 */
public class AddressTest extends AbstractTest {

    @Test
    public void testIdenticalValuesEqual() {
        String value = "mailto:paul3.abbott@gmail.com";
        List<PropertyParameter> param = createParameters("CN=paul3.abbott@gmail.com");
        Address addressValue = new Address(value, param);

        assertEquals("Values should be equal", addressValue, addressValue);
    }

    @Test
    public void testSameValuesEqual() {
        String value = "mailto:paul3.abbott@gmail.com";
        List<PropertyParameter> param = createParameters("CN=paul3.abbott@gmail.com");
        Address addressValue1 = new Address(value, param);
        Address addressValue2 = new Address(value, param);

        assertEquals("Values should be equal", addressValue1, addressValue2);
    }

    @Test
    public void testDifferentValuesNotEqual() {
        String value1 = "mailto:paul3.abbott@gmail.com";
        String value2 = "mailto:paul3.abbott@bcs.org.uk";
        List<PropertyParameter> param = createParameters("CN=paul.abbott@gmail.com");
        Address addressValue1 = new Address(value1, param);
        Address addressValue2 = new Address(value2, param);

        assertNotSame("Values should not be equal", addressValue1, addressValue2);
    }

    @Test
    public void testToFormatterString() {
        String value = "mailto:paul3.abbott@gmail.com";
        List<PropertyParameter> params = createParameters("CN=paul3.abbott@gmail.com");
        Address addressValue = new Address(value, params);

        assertEquals("Values should be equal", value,
                addressValue.getValueAsString());
    }
}
