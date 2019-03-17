package pha.ics.io.read;

import pha.ics.FieldName;

import java.util.List;
import java.util.Objects;

/**
 * Simple immutable data structure created by LineParser, holds all parts of a line.
 * <p>
 * Created by paul on 04/10/14.
 */
public class Line {

    public final FieldName name;
    public final List<String> params;
    public final String value;

    public Line(FieldName keyValue, List<String> paramValues, String lineValue) {
        name = keyValue;
        params = paramValues;
        value = lineValue;
    }

    /**
     * Does the line represent the beggining of a block?
     *
     * @return true if it does
     */
    public boolean begins() {
        return name == FieldName.BEGIN;
    }

    public boolean begins(String value) {
        return name == FieldName.BEGIN && Objects.equals(this.value, value);
    }

    /**
     * Does the line represent the end of a block?
     *
     * @return true if it does
     */
    public boolean ends() {
        return name == FieldName.END;
    }

    public boolean ends(String value) {
        return name == FieldName.END && Objects.equals(this.value, value);
    }

    @Override
    public String toString() {
        return name + " params=" + (params != null ? params : "") + " value=" + value;
    }
}
