package pha.ics.values;

import org.jetbrains.annotations.NotNull;
import pha.ics.PropertyParameter;

import java.util.List;

/**
 * Wrapper for a Integer value.
 * <p>
 * Created by paul on 04/10/14.
 */
public class IntegerValue extends AbstractValue {

    private final Integer value;

    public IntegerValue(String value, List<PropertyParameter> parameters) {
        super(parameters);
        this.value = Integer.valueOf(value);
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IntegerValue that = (IntegerValue) o;

        if (value == null) {
            if (that.value != null) {
                return false;
            }
        } else if (!value.equals(that.value)) {
            return false;
        }

        return super.equals(that);
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "IntegerValue{" + getValueAsString() + '}' + super.toString();
    }

    @Override
    public String getValueAsString() {
        return value.toString();
    }

    @Override
    public int compareTo(@NotNull Value otherValue) {
        if (otherValue instanceof IntegerValue) {
            return value - ((IntegerValue) otherValue).value;
        }
        return super.compareTo(otherValue);
    }
}
