package pha.ics.values;

import org.jetbrains.annotations.Nullable;
import pha.ics.PropertyParameter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Collection of comparable values, stored in insertion order. Not sure now why we don't
 * sort them but I seem to remember that there was a reason.
 *
 * <p>
 * Created by paul on 16/11/14.
 */
public class CompoundValue implements Value {

    private final List<Value> values = new ArrayList<Value>();

    public CompoundValue() {
    }

    public CompoundValue(Value value) {
        add(value);
    }

    public List<Value> getValues() {
        return values;
    }

    public void add(Value value) {
        if (value instanceof CompoundValue) {
            CompoundValue compoundValue = (CompoundValue) value;
            for (Value subValue : compoundValue.getValues()) {
                add(subValue);
            }
        } else {

            if (!values.contains(value)) {
                values.add(value);
            }
        }
    }

    @Override
    public List<PropertyParameter> getParameters() {
        return null;
    }

    @Override
    public String getValueAsString() {
        StringBuilder builder = new StringBuilder();
        for (Value value : values) {
            String s = value.getValueAsString();
            if (builder.length() > 0) {
                builder.append(",").append(s);
            }
        }

        return builder.toString();
    }

    @Nullable
    @Override
    public PropertyParameter getParameter(String name) {
        for (Value value : values) {
            PropertyParameter propertyParameter = value.getParameter(name);
            if (propertyParameter != null) {
                return propertyParameter;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Value value : values) {
            String s = value.toString();
            builder.append(s).append("\n");
        }

        return builder.toString();
    }

    @Override
    public boolean isEmpty() {
        return values.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CompoundValue that = (CompoundValue) o;

        if (values.size() != that.values.size()) {
            return false;
        }
        for (Value value : values) {
            if (!that.values.contains(value)) {
                return false;
            }
        }

        return true;

    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }

    public int size() {
        return values.size();
    }

    @Override
    public int compareTo(Value o) {
        return 0;
    }
}
