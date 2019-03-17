package pha.ics.values;

/**
 * Represents a null string value.
 * Created by paul on 04/10/14.
 */
public class NullValue extends StringValue {

    public static final NullValue NULL = new NullValue();

    private NullValue() {
        super(null);
    }

    public String toString() {
        return "nullValue";
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public String getValueAsString() {
        return null;
    }

}
