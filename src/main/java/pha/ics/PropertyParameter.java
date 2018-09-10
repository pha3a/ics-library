package pha.ics;

/**
 * Name value pair of property parameters.
 * <p/>
 * Created by paul on 08/10/14.
 */
public class PropertyParameter {

    public final String name;
    public final String value;

    public PropertyParameter(String name, String value) {
        this.name = name;
        this.value = value;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PropertyParameter)) {
            return false;
        }

        PropertyParameter that = (PropertyParameter) o;

        if (!name.equals(that.name)) {
            return false;
        }
        if (value != null ? !value.equals(that.value) : that.value != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return name + "=" + value;
    }
}
