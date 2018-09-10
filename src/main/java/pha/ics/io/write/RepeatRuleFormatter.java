package pha.ics.io.write;

import org.jetbrains.annotations.NotNull;
import pha.ics.WeekDay;
import pha.ics.values.DateObject;
import pha.ics.values.RepeatRule;

import java.util.List;

import static pha.ics.values.RepeatRule.Key.*;
import static pha.ics.values.RepeatRule.Key.WKST;

/**
 * Format a supplied repeat rule suitable for writing to a ICS file.
 *
 * Created by paul on 25/07/16.
 */
class RepeatRuleFormatter {

    static String format(RepeatRule rule) {
            StringBuilder builder = new StringBuilder();

            builder.append(FREQ).append("=").append(rule.getFrequency());

            addToBuilder(builder, UNTIL, rule.getUntil());
            addToBuilder(builder, COUNT, rule.getCount());
            addToBuilder(builder, INTERVAL, rule.getInterval());
            addToBuilder(builder, BYSECOND, rule.getBySec());
            addToBuilder(builder, BYMINUTE, rule.getByMin());
            addToBuilder(builder, BYHOUR, rule.getByHour());
            addToBuilder(builder, BYDAY, rule.getByDay());
            addToBuilder(builder, BYMONTHDAY, rule.getByMonthDay());
            addToBuilder(builder, BYYEARDAY, rule.getByYearDay());
            addToBuilder(builder, BYWEEKNO, rule.getByWeekNo());
            addToBuilder(builder, BYMONTH, rule.getByMonth());
            addToBuilder(builder, BYSETPOS, rule.getBySetPos());
            addToBuilder(builder, WKST, rule.getWkst());

            return builder.toString();
        }

    private static void addToBuilder(StringBuilder builder, RepeatRule.Key key, WeekDay val) {
        if (val != null) {
            builder.append(";").append(key).append("=").append(val);
        }
    }

    private static void addToBuilder(StringBuilder builder, RepeatRule.Key key, DateObject val) {
        if (val != null) {
            builder.append(";").append(key).append("=").append(val);
        }
    }

    private static void addToBuilder(StringBuilder builder, RepeatRule.Key key, int val) {
        if (val > 0) {
            builder.append(";").append(key).append("=").append(val);
        }
    }

    private static void addToBuilder(StringBuilder builder, RepeatRule.Key key, List<?> val) {
        if (val != null) {
            builder.append(";").append(key).append("=");
            for (Object o : val) {
                builder.append(o);
                builder.append(',');
            }
            builder.setLength(builder.length() - 1);
        }
    }

}
