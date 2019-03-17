package pha.ics.values;

import org.jetbrains.annotations.NotNull;
import pha.ics.PropertyParameter;

import java.util.Collections;
import java.util.List;

/**
* Created by paul on 16/06/14.
*/
public class SecurityClass extends AbstractValue {

    public static SecurityClass UNDEFINED = new SecurityClass("UNDEFINED");
    public static SecurityClass PUBLIC = new SecurityClass("PUBLIC");
    public static SecurityClass PRIVATE = new SecurityClass("PRIVATE");
    public static SecurityClass CONFIDENTIAL = new SecurityClass("CONFIDENTIAL");

    private final String value;

    SecurityClass(String value) {

        this.value = value;
    }

    public boolean isStronger(SecurityClass other) {
        if (other == this) {
            return false;
        } else if (other == null || other == UNDEFINED) {
            return true;
        } else if (other == PUBLIC && (this == PRIVATE || this == CONFIDENTIAL)) {
            return true;
        }
        return (other == PRIVATE && this == CONFIDENTIAL);
    }

    /**
     * Convert string to name value returning UNDEFINED if the string does not match anything.
     *
     * @param value to convert to FieldName
     * @return matching FieldName or null
     */
    public static SecurityClass parse(String value) {
        if (value != null) {
            if (value.equals(PUBLIC.value)) {
                return PUBLIC;
            } else if (value.equals(PRIVATE.value)) {
                return PRIVATE;
            } else if (value.equals(CONFIDENTIAL.value)) {
                return CONFIDENTIAL;
            }
        }
        return UNDEFINED;
    }

    @NotNull
    @Override
    public List<PropertyParameter> getParameters() {
        return Collections.emptyList();
    }

    @Override
    public String toString() {
        return "SecurityClassValue{"+value+"}";
    }

    @Override
    public String getValueAsString() {
        return value;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }


}
