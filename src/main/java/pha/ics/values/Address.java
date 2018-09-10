package pha.ics.values;


import org.jetbrains.annotations.Nullable;
import pha.ics.PropertyParameter;

import java.util.List;

/**
 * Wrapper for a calendar address parameter.
 * <p/>
 * Created by paul on 04/10/14.
 */
public class Address extends AbstractValue {

    private final String value;

    public Address(String value, List<PropertyParameter> params) {
        super(params);

        this.value = value;
    }

    public Address(String value) {
        this.value = value;
    }

    @Nullable
    public String getValue() {
        return value;
    }

    /**
     * Try and extract the true address name from the value. Removign any "mailto" or "CN=" from it.
     *
     * @return
     */
    public String getName() {
        if (value.toLowerCase().startsWith("mailto:")) {
            return value.substring(7);
        } else if (value.toLowerCase().startsWith("cn=")) {
            return value.substring(3);
        }
        return value;
    }

    public int length() {
        return value != null ? value.length() : 0;
    }

    public boolean isEmpty() {
        return (value == null || value.trim().isEmpty());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null && isEmpty()) {
            return true;
        }

        if (o instanceof String) {
            return o.equals(value);
        }

        if (o instanceof Address) {
            Address that = (Address) o;
            String otherValue = that.value;
            if (this.value == null) {
                return otherValue == null;
            } else if (!this.value.equals(otherValue)) {
                return false;
            }

            return true; // super.equals(that);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Address{" + value + '}' + super.toString();
    }

    @Override
    public String getValueAsString() {
        return value;
    }

    /**
     * Does the string value start with the specified characters.
     *
     * @param charactersToFind
     * @return true if this is not null and starts with charactersToFind.
     */
    public boolean startsWith(String charactersToFind) {
        return value != null && value.startsWith(charactersToFind);
    }

//    public boolean startsWith(Address stringValue) {
//        return stringValue != null && stringValue.getFieldValue() != null && startsWith(stringValue
//                .getFieldValue());
//    }
}
