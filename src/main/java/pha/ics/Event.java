package pha.ics;



import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pha.ics.values.*;

import java.util.*;

/**
 * Represents a Calendar event. An event has a summary and optionally start time and end time. It
 * may be repeating and can contain attachments.
 */
public class Event {

    private static final StringValue EMPTY = NullValue.NULL;

    private final Map<FieldName, Value> fields = new LinkedHashMap<>();

    /**
     * Parent event, used where this is an exclusion to a repeating parent event.
     */
    private Event parentEvent;

    /**
     * Child events, normally used for exclusions to a repeating event.
     */
    private final Set<Event> childEvents = new HashSet<>();

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Event)) {
            return false;
        }
        Event other = (Event) obj;
        return fields.equals(other.fields);

    }

    /**
     * Add a field value under the given name to this event.
     * <p>
     * If a value is already present and both the old and new values are comparable value
     * then add them to a CompoundValue. Otherwise just overwrite the value.
     * Most values are comparable, only Status and SecurityClass are not as they are enums
     *
     * @param fieldName to store the value against
     * @param value to store.
     */
    public void addValue(FieldName fieldName, Value value) {
        if (fields.containsKey(fieldName)) {
            Value oldValue = fields.get(fieldName);

            if (oldValue instanceof CompoundValue) {
                CompoundValue collection = (CompoundValue) oldValue;
                collection.add(value);

            } else if (oldValue instanceof DateList) {
                DateList dateList = (DateList) oldValue;
                if (value instanceof DateList) {
                    dateList.addAll((DateList) value);
                } else {
                    assert false : "Trying to add a "+value+" to a DateList";
                }
            }
            else if (oldValue != null){
                CompoundValue newValue = new CompoundValue(oldValue);
                newValue.add(value);
                fields.put(fieldName, newValue);

            } else {
                fields.put(fieldName, value);
            }
        } else {
            fields.put(fieldName, value);
        }
    }

    /**
     * Add the named category to this event, if not already present.
     *
     * @param category any none null, none empty string
     */
    public void addCategory(String category) {
        if (category != null && !category.isEmpty()) {
            StringList categories = (StringList) fields.get(FieldName.CATEGORIES);

            if (categories == null) {
                categories = new StringList(null, null);
                categories.add(category);
                fields.put(FieldName.CATEGORIES, categories);
            } else if (!categories.contains(category)){
                categories.add(category);
            }
        }

    }

    public void removeCategory(String category) {
        StringList categories = (StringList) fields.get(FieldName.CATEGORIES);

        if (categories != null && categories.contains(category)) {
            categories.remove(category);
        }
    }

    /**
     * Return a list of categories held by this event.
     * @return
     */
    public List<String> getCategories() {
        StringList categories = (StringList) fields.get(FieldName.CATEGORIES);

        if (categories != null) {
            return categories.getValue();
        }
        return Collections.emptyList();
    }

    /**
     * Repeating events are ones have a repeat rule.
     *
     * @return true if the event is a repeating event.
     */
    public boolean isRepeating() {
        return getFieldValue(FieldName.RRULE) != null;
    }
    public boolean isRepeatingYearly() {
        RepeatRule repeatRule = getRepeatRule();

        return repeatRule != null && repeatRule.isYearly();
    }
    public boolean isRepeatingMonthly() {
        RepeatRule repeatRule = getRepeatRule();

        return repeatRule != null && repeatRule.isMonthly();
    }

    public boolean isRepeatingDaily() {
        RepeatRule repeatRule = getRepeatRule();

        return repeatRule != null && repeatRule.isDaily();
    }

    public String getSummary() {
        StringValue value = (StringValue) getFieldValue(FieldName.SUMMARY);

        return value != null ? value.getValue() : null;
    }

    @Override
    public int hashCode() {
        return fields.hashCode();
    }

    public String getDescription() {
        StringValue description = (StringValue) getFieldValue(FieldName.DESCRIPTION);
        return description != null ? description.getValue() : null;
    }

    public DateValue getDTEnd() {
        return (DateValue) fields.get(FieldName.DTEND);
    }

    @Nullable
    public SecurityClass getSecurityClass() {
        return (SecurityClass) fields.get(FieldName.CLASS);
    }


    /**
     * Get the raw named value.
     *
     * @param fieldName of the value to return
     * @return value or null if not defined
     */
    @Nullable
    public Value getFieldValue(FieldName fieldName) {
        return fields.get(fieldName);
    }


    public boolean isEmpty() {
        return fields.isEmpty();
    }

    @Override
    public String toString() {
        return String.valueOf(fields);
    }

    /**
     * Return the start date field, this may be just the date or the date and time.
     *
     * @return the start date field.
     */
    public DateValue getDTStart() {
        return (DateValue) fields.get(FieldName.DTSTART);
    }

    /**
     * Return true if the start and end days are the same. If the start and end have no time part
     * and end = start + 1 then we say it is a all day event are return true.
     *
     * @return true if this event is contained on a single day.
     */
    public boolean onSingleDay() {
        DateValue start = (DateValue) fields.get(FieldName.DTSTART);
        DateObject end = getEnd();
        // Missing start is not valid
        if (start == null) {
            return false;
        }
        // A start and end time on the same day (with or without a time) is a single day event
        if (end == null || start.isSameDay(end)) {
            return true;
        }
        // If we have no time part both represent midnight, so look for an end 1 day after
        // a start this is a single day event.
        if (!start.hasTimePart() && !end.hasTimePart() && start.getDateObject().addDays(1).equals(end)) {
            return true;
        }
        return false;
    }

    /**
     * If this is a delete or cancel event that relates to a repeating event. Then the parent is the repeating event.
     *
     * @param event Repeating event this relates to.
     */
    public void setParent(Event event) {

        if (parentEvent != null) {
            parentEvent.removeChild(this);
        }
        parentEvent = event;

        if (parentEvent != null) {
            parentEvent.addChild(this);
        }
    }

    private void addChild(Event event) {
        childEvents.add(event);
    }

    private void removeChild(Event event) {
        childEvents.remove(event);
    }

    /**
     * Return the end of this event as a DateObject. This will first look for a
     * DTEND field and if that is not defined it will look for DTSTART,
     * DURATION fields and calculate the end from them.
     *
     * @return the end of the event.
     */
    public DateObject getEnd() {
        DateValue end = (DateValue) fields.get(FieldName.DTEND);

        if (end != null) {
            return end.getDateObject();
        } else {
            DateValue start = (DateValue) fields.get(FieldName.DTSTART);
            Duration duration = (Duration) fields.get(FieldName.DURATION);
            if (start != null) {
                if (duration != null) {
                    return start.getDateObject().add(duration);
                } else {
                    return start.getDateObject();
                }
            }
            return null;
        }
    }

    public boolean hasUid() {
        return fields.get(FieldName.UID) != null;
    }

    public String getUid() {
        StringValue value = (StringValue) fields.get(FieldName.UID);
        if (value != null) {
            return value.getValue();
        }
        return null;
    }

    public void setValue(FieldName fieldName, Value value) {
        if (value != null) {
            fields.put(fieldName, value);
        } else {
            fields.remove(fieldName);
        }

    }

    /**
     * Return true if this event is newer than matching event.
     *
     * @param matching event to compare this to
     * @return true if either the date stamp or created values are newer in this than matching,
     * false if not or values not found.
     */
    public boolean isAfter(Event matching) {
        DateValue dtStamp = (DateValue) getFieldValue(FieldName.DTSTAMP);
        DateValue otherDtStamp = (DateValue) matching.getFieldValue(FieldName.DTSTAMP);

        if (dtStamp != null) {
            if (dtStamp.isAfter(otherDtStamp)) {
                return true;
            } else if (dtStamp.isBefore(otherDtStamp)) {
                return false;
            }
        }

        DateValue lastModified = (DateValue) getFieldValue(FieldName.LAST_MODIFIED);
        DateValue otherLastModified = (DateValue) matching.getFieldValue(FieldName.LAST_MODIFIED);
        if (lastModified != null) {
            if (lastModified.isAfter(otherLastModified)) {
                return true;
            } else if (lastModified.isBefore(otherLastModified)) {
                return false;
            }
        }

        return false;
    }

    /**
     * Return true if this event has a sequence number.
     *
     * @return true if a sequence number field is found.
     */
    public boolean hasSequenceNumber() {
        return fields.get(FieldName.SEQUENCE) != null;
    }

    public boolean isSequenceBefore(Event other) {
        IntegerValue sequence = (IntegerValue) getFieldValue(FieldName.SEQUENCE);
        IntegerValue otherSeq = (IntegerValue) other.getFieldValue(FieldName.SEQUENCE);

        if (sequence == null && otherSeq != null) {
            return true;
        }
        if (sequence == null || otherSeq == null) {
            return false;
        }
        return sequence.getValue() < otherSeq.getValue();
    }

    public RepeatRule getRepeatRule() {
        return (RepeatRule) getFieldValue(FieldName.RRULE);
    }

    public DateValue getLastModified() {
        return (DateValue) getFieldValue(FieldName.LAST_MODIFIED);
    }

    public DateValue getDateStamp() {
        return (DateValue) getFieldValue(FieldName.DTSTAMP);
    }

    /**
     * Remove a value matching the named field. This method will not report
     * an error if the value is missing.
     * <p/>
     * MUTATING method
     *
     * @param field of the value to remove.
     */
    public void remove(FieldName field) {
        fields.remove(field);
    }

    /**
     * Return all the names of fields held by this event.
     *
     * @return all fields in this event.
     */
    public @NotNull Collection<FieldName> getFieldNames() {
        return fields.keySet();
    }

    public boolean isCancelled() {
        Value status = fields.get(FieldName.STATUS);
        return (status == Status.CANCELLED);
    }

    /**
     * Add the given date to the exclude date list. This method will only update
     * the event if this event is repeating.
     * <p/>
     * MUTATING method
     *
     * @param date to add to exclude list.
     */
    public void excludeDate(DateValue date) {
        if (isRepeating()) {
            DateList excludeDates = getExcludeDates();
            excludeDates.add(date);
        }
    }

    /**
     * Get the current exclude date list and cast it down to a DateList. If the field
     * does not exist, create it.
     *
     * @return a list of dates
     */
    @NotNull
    public DateList getExcludeDates() {
        if (fields.containsKey(FieldName.EXDATE)) {
            return (DateList) fields.get(FieldName.EXDATE);
        }
        DateList dateList = new DateList();
        fields.put(FieldName.EXDATE, dateList);

        return dateList;
    }

    /**
     * Check if the given date matches any date of this event, including repeated dates.
     *
     * @param date to find
     * @return if the given date matches any date of this event
     */
    public boolean containsDate(DateValue date) {

        if (date != null) {
            DateValue start = getDTStart();
            RepeatRule repeatRule = getRepeatRule();
            if (start != null && repeatRule != null) {
                DateList excludeDates = getExcludeDates();

                DateFinder dateFinder = DateFinder.build(start, repeatRule, excludeDates);

                return dateFinder.find(date);
            } else if (start != null) {
                return start.equals(date);
            }
        }

        return false;
    }

    /**
     * Does this event have attendees?
     *
     * @return true if the ATTENDEE field is present.
     */
    public boolean hasAttendees() {
        return fields.containsKey(FieldName.ATTENDEE);
    }

    /**
     * Return the Parent event, which will be the repeating event this is an exclusion of.
     *
     * @return the parent event
     */
    public Event getParentEvent() {
        return parentEvent;
    }

    /**
     * Return the child events that represent exclusions to this repreting event.
     *
     * @return a set of events
     */
    public Set<Event> getChildEvents() {
        return childEvents;
    }
}
