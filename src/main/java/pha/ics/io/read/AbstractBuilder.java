package pha.ics.io.read;

import pha.ics.PropertyParameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract builder hodling common functionality of {@link EventBuilder} and {@link TimeZoneBuilder}.
 *
 * Created by paul on 24/07/16.
 */
public class AbstractBuilder {

    protected static List<PropertyParameter> parseParameters(List<String> params) {

        List<PropertyParameter> result = new ArrayList<>();

        if (params != null) {
            for (String param : params) {
                String[] split = param.split("=");
                if (split.length > 1) {

                    assert split.length == 2 : "Parameter has more than 1 '=' param='" + param + "'";
                    String name = split[0];
                    String value = split[1];
                    result.add(new PropertyParameter(name, value));
                }
            }
        }

        return result;

    }

}
