package pha.ics.values;

import pha.ics.PropertyParameter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Wrapper for a list of sorted strings.
 * <p>
 * Created by paul on 04/10/14.
 */
public class StringList extends AbstractValue {

    private final List<String> value = new ArrayList<>();

    public StringList(List<String> values, List<PropertyParameter> params) {
        super(params);
        if (values != null) {
            value.addAll(values);
            Collections.sort(value);
        }
    }

    public List<String> getValue() {
        return value;
    }

    public int length() {
        return value.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null && isEmpty()) {
            return true;
        }
        if (o == null || !(o instanceof StringList)) {
            return false;
        }

        StringList that = (StringList) o;

        if (!value.equals(that.value)) {
            return false;
        }

        return true; // super.equals(that);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "StringList{"+ getValueAsString()+"}";
    }

    @Override
    public String getValueAsString() {
        return value.toString();
    }

    @Override
    public boolean isEmpty() {
        return value.isEmpty();
    }

    public boolean contains(String item) {
        return value.contains(item);
    }

    public void add(String item) {
        value.add(item);
        Collections.sort(value);
    }

    public void remove(String item) {
        value.remove(item);
    }
}
