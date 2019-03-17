package pha.ics.values;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pha.ics.PropertyParameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class of a values that holds the parameters that a value may have.
 * <p>
 * Created by paul on 04/10/14.
 */
public abstract class AbstractValue implements Value {

    private final List<PropertyParameter> parameters = new ArrayList<>();

    protected AbstractValue(List<PropertyParameter> params) {
        if (params != null) {
            for (Object parameter : params) {
                if (parameter instanceof PropertyParameter) {
                    storeParameter((PropertyParameter) parameter);
                } else {
                    String[] split = parameter.toString().split("=");

                    storeParameter(new PropertyParameter(split[0], split[1]));
                }
            }
        }

    }

    protected void storeParameter(PropertyParameter e) {
        parameters.add(e);
    }

    protected AbstractValue(PropertyParameter param) {
        storeParameter(param);
    }

    protected AbstractValue() {
    }

    @NotNull public List<PropertyParameter> getParameters() {
        return parameters;
    }

    /**
     * Return the named property, if present or null if not.
     *
     * @param name of parameter to return.
     * @return the parameter or null
     */
    @Override
    @Nullable
    public PropertyParameter getParameter(String name) {
        for (PropertyParameter parameter : parameters) {
            if (parameter.name.equals(name)) {
                return parameter;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof AbstractValue) {
            AbstractValue value = (AbstractValue) o;

            assert parameters != null : "No parameters";

            return parameters.equals(value.parameters);

        }

        // Wrong class or null
        return false;
    }

    @Override
    public int hashCode() {
        return parameters.hashCode();
    }

    /**
     * Default compareTo method that simply compares the string representation of the values.
     *
     * @param otherValue other object being compared to
     * @return -1, 0, 1 (0=same)
     */
    public int compareTo(@NotNull Value otherValue) {
        return toString().compareTo(otherValue.toString());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (!parameters.isEmpty()) {
            sb.append("{");
            for (PropertyParameter parameter : parameters) {
                sb.append(parameter.name).append("=");
                sb.append(parameter.value);
                sb.append(";");
            }
            sb.setCharAt(sb.length() - 1, '}');
        }

        return sb.toString();
    }

    public boolean isEmpty() {
        return false;
    }
}
