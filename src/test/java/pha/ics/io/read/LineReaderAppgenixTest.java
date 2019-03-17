package pha.ics.io.read;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static pha.ics.FieldName.*;

public class LineReaderAppgenixTest {

    /**
     * Test that a single line is read.
     *
     * @throws IOException if error reading line
     */
    @Test
    public void testReadLineWithSingleLine() throws IOException {

        StringReader sr = new StringReader("CLASS:PRIVATE");
        BufferedReader bufferedReader = new BufferedReader(sr);

        LineReader reader = new LineReader(bufferedReader);
        reader.setFormatToAppgenix();

        Line actualLine = reader.readLine();
        assertEquals("Missing name", CLASS, actualLine.name);
        assertEquals("Missing value", "PRIVATE", actualLine.value);

        assertEquals("Missing end of stream marker", LineReader.END_OF_INPUT, reader.readLine());
    }

    /**
     * Test that 2 lines can be read.
     *
     * @throws IOException if error reading line
     */
    @Test
    public void testReadLineWithTwoLine2() throws IOException {

        StringReader sr = new StringReader("ORGANIZER:Message\r\nCLASS:PRIVATE");
        BufferedReader bufferedReader = new BufferedReader(sr);

        LineReader reader = new LineReader(bufferedReader);
        reader.setFormatToAppgenix();

        Line actualLine = reader.readLine();
        assertEquals("Missing name", ORGANIZER, actualLine.name);
        assertEquals("Missing value", "Message", actualLine.value);

        Line actualLine2 = reader.readLine();
        assertEquals("Missing name", CLASS, actualLine2.name);
        assertEquals("Missing value", "PRIVATE", actualLine2.value);

        assertEquals("Missing end of stream marker", LineReader.END_OF_INPUT, reader.readLine());
    }

    /**
     * Test that 2 lines can be read when the first is wrapped round onto the second line
     *
     * @throws IOException if error reading line
     */
    @Test
    public void testReadLineWithAnExtendedLine() throws IOException {

        StringReader sr = new StringReader("ORGANIZER:Message\r\nExtension\r\nDESCRIPTION:Message\r\nExtension\r\nCLASS:PRIVATE");
        BufferedReader bufferedReader = new BufferedReader(sr);

        LineReader reader = new LineReader(bufferedReader);
        reader.setFormatToAppgenix();

        Line actualLine1 = reader.readLine();
        assertEquals("Missing name", ORGANIZER, actualLine1.name);
        assertEquals("Missing value", "MessageExtension", actualLine1.value);

        Line actualLine = reader.readLine();
        assertEquals("Missing name", DESCRIPTION, actualLine.name);
        assertEquals("Missing value", "Message\nExtension", actualLine.value);

        Line actualLine2 = reader.readLine();
        assertEquals("Missing name", CLASS, actualLine2.name);
        assertEquals("Missing value", "PRIVATE", actualLine2.value);

        assertEquals("Missing end of stream marker", LineReader.END_OF_INPUT, reader.readLine());
    }

    /**
     * Test that an extended line can be read if it is the last line..
     *
     * @throws IOException if error reading line
     */
    @Test
    public void testReadLineWithExtendedLineBeingLast() throws IOException {

        StringReader sr = new StringReader("SUMMARY:ONE\r\nDESCRIPTION:20130623T200559Z\r\nExtension\r\n");
        BufferedReader bufferedReader = new BufferedReader(sr);

        LineReader reader = new LineReader(bufferedReader);
        reader.setFormatToAppgenix();

        Line actualLine1 = reader.readLine();
        assertEquals("Missing name", SUMMARY, actualLine1.name);

        Line actualLine2 = reader.readLine();
        assertEquals("Missing name", DESCRIPTION, actualLine2.name);
        assertEquals("Missing value", "20130623T200559Z\nExtension", actualLine2.value);

        assertEquals("Missing end of stream marker", LineReader.END_OF_INPUT, reader.readLine());
    }
}
