package pha.ics;

import pha.ics.values.RepeatRule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by paul on 24/07/16.
 */
public class AbstractTest {

    protected RepeatRule createRepeatRule(String value) {
        return createRepeatRule(value, null);
    }

    protected static RepeatRule createRepeatRule(String rawValue, List<PropertyParameter> param) {

        Map<RepeatRule.Key, String> result = new HashMap<>();

        String[] values = rawValue.split(";");

        for (String keyValuePair : values) {
            String[] keyAndValue = keyValuePair.split("=");

            String s = keyAndValue[0];
            RepeatRule.Key key = RepeatRule.Key.valueOf(s);

            result.put(key, keyAndValue[1]);

        }
        return new RepeatRule(result, param);
    }

    /**
     * Create a list of parameters for a test.
     *
     * @param params as text to add to list
     * @return a list
     */
    protected List<PropertyParameter> createParameters(String... params) {

         List<PropertyParameter> result = new ArrayList<>();

            for (String param : params) {
                String[] split = param.split("=");
                if (split.length > 1) {

                    String name = split[0];
                    String value = split[1];
                    result.add(new PropertyParameter(name, value));
                }
            }


        return result;

    }
}
