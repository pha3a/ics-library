package pha.ics.values;

import org.jetbrains.annotations.NotNull;
import pha.ics.PropertyParameter;

import java.util.Collections;
import java.util.List;

/**
 * Event status enum.
 * <p/>
 * Created by paul on 16/06/14.
 */
public class Status extends AbstractValue {

    public static Status UNDEFINED = new Status("UNDEFINED");
    public static Status TENTATIVE = new Status("TENTATIVE");
    public static Status CONFIRMED = new Status("CONFIRMED");
    public static Status CANCELLED = new Status("CANCELLED");

    private final String value;

    private Status(String value) {
        this.value = value;
    }

    /**
     * Convert string to name value returning null if the string does not match anything.
     *
     * @param value to convert to FieldName
     * @return matching FieldName or null
     */
    public static Status parse(String value) {
        if (value != null) {
            if (value.equals(TENTATIVE.value)) {
                return TENTATIVE;
            } else if (value.equals(CONFIRMED.value)) {
                return CONFIRMED;
            } else if (value.equals(CANCELLED.value)) {
                return CANCELLED;
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
        return "StatusValue{" + getValueAsString() + "}";
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
