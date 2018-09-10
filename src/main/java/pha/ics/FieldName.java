package pha.ics;

import java.util.ArrayList;
import java.util.List;

import static pha.ics.ValueType.*;

/**
* Represents a single field of an Event.
*/
public enum FieldName {

    UNKNOWN(TEXT),

    SUMMARY(TEXT),
    DESCRIPTION(TEXT),
    COMMENT(TEXT),
    UID(TEXT),
    LOCATION(TEXT),
    ORGANIZER(ADDRESS_TYPE),
    ATTENDEE(ADDRESS_TYPE),
    CONTACT(TEXT),
    ATTACH(TEXT),
    DURATION(DURATION_TYPE),

    CATEGORIES(TEXT_LIST),
    CLASS(CLASS_TYPE),
    STATUS(STATUS_TYPE),
    TRANSP(TRANSP_TYPE),

    PRIORITY(INTEGER),
    SEQUENCE(INTEGER),

    DTSTART(DATE), DTEND(DATE),
    RECURRENCE_ID(DATE),
    CREATED(DATE), DTSTAMP(DATE),
    LAST_MODIFIED(DATE),

    RDATE(DATE_LIST), EXDATE(DATE_LIST),

    RRULE(RECUR), EXRULE(RECUR),

    X_ALT_DESC, X_MICROSOFT_DISALLOW_COUNTER,  X_MICROSOFT_ISDRAFT,

    X_MICROSOFT_CDO_BUSYSTATUS, X_MICROSOFT_CDO_IMPORTANCE,

    X_MS_OLK_ALLOWEXTERNCHECK, X_MS_OLK_APPTSEQTIME,
    X_MS_OLK_AUTOFILLLOCATION, X_MS_OLK_CONFTYPE, X_MS_OLK_CONFCHECK,
    X_MS_OLK_AUTOSTARTCHECK, X_MS_OLK_APPTLASTSEQUENCE,

    TZOFFSETFROM, TZOFFSETTO, TZID, TZNAME,

    BEGIN, END,

    PRODID, VERSION, CALSCALE, METHOD,
    X_WR_CALNAME,X_WR_TIMEZONE,X_WR_RELCALID,
    X_CALSTART, X_CALEND, X_CLIPSTART, X_CLIPEND,

    TRIGGER, ACTION;

    private static List<String> namesArray;

    private final ValueType type;

    FieldName(ValueType type) {
        this.type = type;
    }

    FieldName() {
        this(TEXT);
    }

    @Override
    public String toString() {
        return super.toString().replace("_", "-");
    }

    /**
     * Convert string to name value returning null if the string does not match anything.
     *
     * @param value to convert to FieldName
     * @return matching FieldName or null
     */
    public static FieldName parse(String value) {
        String replace = value.replace("-", "_");
        try {
            return valueOf(replace);
        } catch (IllegalArgumentException ignored) {
        }

        return null;
    }

    public ValueType getType() {
        return type;
    }

    public static List<String> names() {
        if (namesArray == null) {
            namesArray = new ArrayList<String>();
            for (FieldName value : values()) {
                namesArray.add(value.toString());
            }

        }
        return namesArray;
    }
}
