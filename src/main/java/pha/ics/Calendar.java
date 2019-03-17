package pha.ics;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pha.ics.io.read.Line;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents a calendar.
 */
public class Calendar {

    /**
     * Name of calendar, usually the name of the file the calendar was read from.
     */
    private String name;

    /**
     * List of events in this calendar, order is not guaranteed.
     */
    private final List<Event> events = new ArrayList<>();

    /**
     * Defined timezones used in this claendar's events
     */
    private final List<TimeZone> timeZones = new ArrayList<>();

    /**
     * Collection of raw lines that may hold additional information about the calendar.
     */
    private final List<Line> headerLines = new ArrayList<>();

    /**
     * Build an empty calendar,
     *
     * @param name Name of the calendar to readICSFrom
     */
    public Calendar(String name) {
        this.name = name;
    }


    public void addEvent(Event event) {
        events.add(event);
    }

    public void addEvents(Collection<Event> eventsToAdd) {
        events.addAll(eventsToAdd);

    }

    /**
     * Add a new timezone to this Calendar. This method will check if the named
     * timezone already exists and will fail silently if it is already present.
     *
     * @param zone to add
     */
    public void addTimeZone(TimeZone zone) {
        for (TimeZone timeZone : timeZones) {
            if (timeZone.getId().equals(zone.getId())) {
                return;
            }
        }

        timeZones.add(zone);
    }

    /**
     * Remove the specified time zone from this calendar.
     *
     * @param timeZone to add
     */
    public void removeTimeZone(TimeZone timeZone) {
        timeZones.remove(timeZone);
    }

    /**
     * Return all time zones held by this calendar.
     *
     * @return a list of all timezones.
     */
    public List<TimeZone> getTimeZones() {
        return timeZones;
    }

    /**
     * Do 2 calendars equal each other?
     * Only compares events & timezones
     *
     * NOTE Comparision is order sensitive, so all events need to be sorted first.
     *
     * @param obj other calendar
     * @return true if they do.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Calendar) {
            Calendar otherCal = (Calendar) obj;

            return timeZones.equals(otherCal.timeZones) && events.equals(otherCal.events);
        }
        return false;
    }

    public int getEventCount() {
        return events.size();
    }

    @NotNull public List<Event> getEvents() {
        return events;
    }

    @NotNull public List<Event> cloneEvents() {
        return new ArrayList<>(events);
    }


    public void removeEvent(Event event) {
        events.remove(event);
    }

    public void removeEvents(Collection<Event> deleteEvents) {
        events.removeAll(deleteEvents);
    }

    public boolean hasEvents() {
        return !events.isEmpty();
    }

    @Nullable public String getName() {
        return name;
    }

    public void clearEvents() {
        events.clear();
    }

    public void addHeaderLine(Line line) {
        headerLines.add(line);
    }

    public List<Line> getHeaderLines() {
        return headerLines;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Name:").append(name).append(" ");
        builder.append("Evnets = ").append(events.size());

        return builder.toString();
    }
}
