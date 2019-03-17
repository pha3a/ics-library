package pha.ics.values;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pha.ics.PropertyParameter;

import java.util.List;

/**
 * Wrapper for a string value.
 * <p>
 * Created by paul on 04/10/14.
 */
public class StringValue extends AbstractValue {

    private final String value;

    public StringValue(String value, List<PropertyParameter> params) {
        super(params);
        this.value = value;
    }

    public StringValue(String value) {
        this.value = value;
    }

    @Nullable
    public String getValue() {
        return value;
    }

    public int length() {
        return value == null ? 0 : value.length();
    }

    /**
     * Returns true if this string is empty, if the parameters indicate that this
     * is an HTML string then strip out the formatting before checking.
     *
     * @return true if this string is null or empty.
     */
    public boolean isEmpty() {
        if (isHTML()) {
            String strippedText = value.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " ");
            strippedText = strippedText.replace("\\n", "");

            return strippedText.trim().isEmpty();
        }
        return (value == null || value.trim().isEmpty());
    }

    private boolean isHTML() {
        PropertyParameter parameter = getParameter("FMTTYPE");
        return value != null && parameter != null && parameter.value.equals("text/html");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null && isEmpty()) {
            return true;
        }

        if (o instanceof StringValue) {
            String thatValue = ((StringValue) o).value;
            if (value == null) {
                return thatValue == null;
            }
            return value.equals(thatValue);
        }
        return false;
    }

    /**
     * Convienience method to apply a regex match to this string.
     *
     * @param regex to match
     * @return true if it matches or false if not match or this is null
     */
    public boolean matches(String regex) {
        return value != null && value.matches(regex);
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "StringValue{" + getValueAsString() + '}' + super.toString();
    }

    @Override
    public String getValueAsString() {
        return value;
    }


    @Override
    public int compareTo(@NotNull Value otherValue) {
        if (otherValue instanceof StringValue) {
            return value.compareTo(((StringValue) otherValue).value);
        }
        return super.compareTo(otherValue);
    }

    /**
     * Does the string value start with the specified characters.
     *
     * @param charactersToFind characters to find
     * @return true if this is not null and starts with charactersToFind.
     */
    public boolean startsWith(String charactersToFind) {
        return value != null && value.startsWith(charactersToFind);
    }

    public boolean startsWith(StringValue charactersToFind) {
        return charactersToFind != null && charactersToFind.getValue() != null && startsWith(charactersToFind
                .getValue());
    }

    /**
     * Does this string value contain the supplied string?
     *
     * @param string to check for, may be null
     * @return true if string equals value
     */
    public boolean equalsString(String string) {
        if (string != null) {
            return string.equals(value);
        } else {
            return value == null;
        }
    }
}
