package pha.ics.io.read;

import pha.ics.PropertyParameter;
import pha.ics.values.RepeatRule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Build a Repeat Rule from an ICS encoded string.
 *
 * Created by paul on 28/07/16.
 */
public class RepeatRuleBuilder {

    public static RepeatRule build(String value, List<PropertyParameter> params) {

        Map<RepeatRule.Key, String> parts = parseRule(value);

        return new RepeatRule(parts, params);
    }

    /**
     * Split a repeat rule into its component parts and return them as a map
     * of key value pairs.
     *
     * @param ruleAsString format Key=Value;key2=Value2
     * @return a map of key,value pairs
     */
    private static Map<RepeatRule.Key, String> parseRule(String ruleAsString) {

        HashMap<RepeatRule.Key, String> result = new HashMap<>();

        String[] values = ruleAsString.split(";");

        for (String keyValuePair : values) {
            String[] keyAndValue = keyValuePair.split("=");

            String keyAsString = keyAndValue[0];

            RepeatRule.Key key = RepeatRule.Key.valueOf(keyAsString);
            result.put(key, keyAndValue[1]);

        }

        return result;
    }
}
