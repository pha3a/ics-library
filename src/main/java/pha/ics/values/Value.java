package pha.ics.values;

import org.jetbrains.annotations.Nullable;
import pha.ics.PropertyParameter;

import java.util.List;

/**
 * Main interface of a value.
 *
 * Created by paul on 18/10/14.
 */
public interface Value extends Comparable<Value> {


    /**
     * Property parameters of the value, often empty.
     *
     * @return a list of values, order
     */
    List<PropertyParameter> getParameters();

    /**
     * Return this Value as a raw, unformatted, String. This does not include
     * the parameters which are included in the result of "toString".
     *
     * @return formatted version of value
     */
    String getValueAsString();

    /**
     * Return the named property, if present or null if not.
     *
     * @param name of parameter to return.
     * @return the parameter or null
     */
    @Nullable
    PropertyParameter getParameter(String name);

    /**
     * Used to detect empty values, mainly used to test compound values.
     *
     * @return true if value is empty
     */
    boolean isEmpty();
}
