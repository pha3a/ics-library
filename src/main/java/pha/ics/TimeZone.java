package pha.ics;

import org.jetbrains.annotations.Nullable;
import pha.ics.values.DateValue;
import pha.ics.values.StringValue;
import pha.ics.values.Value;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a time zone referenced by calendar events.
 * <p>
 * Created by paul on 06/07/16.
 */
public class TimeZone {

    /**
     * Timezone types, either standard or daylight
     */
    private final Map<String, Type> types = new HashMap<>();

    /**
     * Unique id for this time zone, a time zone will be referenced by this name.
     */
    private StringValue id;
    private DateValue lastModified;

    public void setId(StringValue id) {
        this.id = id;
    }

    public void addType(String typeName, Type type) {
        types.put(typeName, type);
    }

    public void setLastModified(DateValue lastModified) {
        this.lastModified = lastModified;
    }

    public StringValue getId() {
        return id;
    }

    public DateValue getLastModified() {
        return lastModified;
    }


    @Override
    public String toString() {
        return "TimeZone: "+id.getValue();
    }

    public Map<String, Type> getTypes() {
        return types;
    }

    public static class Type {
        private final Map<FieldName, Value> fields = new HashMap<>();

        public void addField(FieldName fieldName, Value value) {
            fields.put(fieldName, value);
        }

        /**
         * Return a collection of all fields held by this event.
         *
         * @return all fields in this event.
         */
        public Collection<FieldName> getFieldNames() {
            return fields.keySet();
        }

        /**
         * Get the raw named value.
         *
         * @param field of the value to return
         * @return value or null if not defined
         */
        @Nullable
        public Value getValue(FieldName field) {
            return fields.get(field);
        }
    }

}
