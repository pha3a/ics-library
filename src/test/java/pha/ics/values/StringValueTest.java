package pha.ics.values;

import org.junit.Test;
import pha.ics.PropertyParameter;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by paul on 23/07/16.
 */
public class StringValueTest {

    @Test
    public void testIsEmptyOnNormalText() {
        StringValue stringValue = new StringValue("XYY is short");

        assertFalse("Should not be empty", stringValue.isEmpty());
    }

    @Test
    public void testIsEmptyOnEmptyString() {
        StringValue stringValue = new StringValue("");

        assertTrue("Should be empty", stringValue.isEmpty());

    }

    @Test
    public void testIsEmptyOnEmptyHTML() {
        List<PropertyParameter> params = new ArrayList<>();
        params.add(new PropertyParameter("FMTTYPE", "text/html"));

        StringValue stringValue = new StringValue("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2//EN\">\\n<HTML>\\n<HEAD>\\n<META NAME=\"Generator\" CONTENT=\"MS Exchange Server version 14.02.5004.000\">\\n<TITLE></TITLE>\\n</HEAD>\\n<BODY>\\n<!-- Converted from text/rtf format -->\\n<BR>\\n\\n</BODY>\\n</HTML>", params);

        assertTrue("Should be empty", stringValue.isEmpty());

    }

    @Test
    public void testIsEmptyOnValidHTML() {
        List<PropertyParameter> params = new ArrayList<>();
        params.add(new PropertyParameter("FMTTYPE", "text/html"));

        StringValue stringValue = new StringValue("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2//EN\">\\n<HTML>\\n<HEAD>\\n<META NAME=\"Generator\" CONTENT=\"MS Exchange Server version 14.02.5004.000\">\\n<TITLE>Title</TITLE>\\n</HEAD>\\n<BODY>\\n<!-- Converted from text/rtf format -->\\n<BR>Message in body\\n</BODY>\\n</HTML>", params);

        assertFalse("Should not be empty", stringValue.isEmpty());

    }
}