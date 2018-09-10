package pha.ics.io.read;

import junit.framework.TestCase;
import pha.ics.io.read.Line;
import pha.ics.io.read.LineReader;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static pha.ics.FieldName.STATUS;
import static pha.ics.FieldName.SUMMARY;

public class LineReaderTest extends TestCase {

    /**
     * Test that a single line is read.
     *
     * @throws Exception
     */
    public void testReadLineWithSingleLine() throws Exception {

        StringReader sr = new StringReader("SUMMARY:Message");
        BufferedReader bufferedReader = new BufferedReader(sr);

        LineReader reader = new LineReader(bufferedReader);

        Line actualLine = reader.readLine();
        assertEquals("Missing name", SUMMARY, actualLine.name);
        assertEquals("Missing value", "Message", actualLine.value);

        assertEquals("Missing end of stream marker", LineReader.END_OF_INPUT, reader.readLine());
    }

    /**
     * Test that 2 lines can be read.
     *
     * @throws Exception
     */
    public void testReadLineWithTwoLine2() throws Exception {

        StringReader sr = new StringReader("SUMMARY:Message\r\nSTATUS:TENTATIVE");
        BufferedReader bufferedReader = new BufferedReader(sr);

        LineReader reader = new LineReader(bufferedReader);

        Line actualLine = reader.readLine();
        assertEquals("Missing name", SUMMARY, actualLine.name);
        assertEquals("Missing value", "Message", actualLine.value);

        Line actualLine2 = reader.readLine();
        assertEquals("Missing name", STATUS, actualLine2.name);
        assertEquals("Missing value", "TENTATIVE", actualLine2.value);

        assertEquals("Missing end of stream marker", LineReader.END_OF_INPUT, reader.readLine());
    }

    /**
     * Test that 2 lines can be read when the first is wrapped round onto the second line
     *
     * @throws Exception
     */
    public void testReadLineWithAnExtendedLine() throws Exception {

        StringReader sr = new StringReader("SUMMARY:Message\r\n\tExtension\r\nSTATUS:TENTATIVE");
        BufferedReader bufferedReader = new BufferedReader(sr);

        LineReader reader = new LineReader(bufferedReader);

        Line actualLine = reader.readLine();
        assertEquals("Missing name", SUMMARY, actualLine.name);
        assertEquals("Missing value", "MessageExtension", actualLine.value);

        Line actualLine2 = reader.readLine();
        assertEquals("Missing name", STATUS, actualLine2.name);
        assertEquals("Missing value", "TENTATIVE", actualLine2.value);

        assertEquals("Missing end of stream marker", LineReader.END_OF_INPUT, reader.readLine());
    }
    /**
     * Test that 2 lines can be read when the first is wrapped round onto the second line
     *
     * @throws Exception
     */
    public void testReadLineWithAnExtendedSpaceLine() throws Exception {

        StringReader sr = new StringReader("SUMMARY:Message\r\n Extension\r\nSTATUS:TENTATIVE");
        BufferedReader bufferedReader = new BufferedReader(sr);

        LineReader reader = new LineReader(bufferedReader);

        Line actualLine = reader.readLine();
        assertEquals("Missing name", SUMMARY, actualLine.name);
        assertEquals("Missing value", "MessageExtension", actualLine.value);

        Line actualLine2 = reader.readLine();
        assertEquals("Missing name", STATUS, actualLine2.name);
        assertEquals("Missing value", "TENTATIVE", actualLine2.value);

        assertEquals("Missing end of stream marker", LineReader.END_OF_INPUT, reader.readLine());
    }

    /**
     * Test that an extended line can be read if it is the last line..
     *
     * @throws Exception
     */
    public void testReadLineWithExtendedLineBeingLast() throws Exception {

        StringReader sr = new StringReader("SUMMARY:Message\r\n\tExtension\r\n");
        BufferedReader bufferedReader = new BufferedReader(sr);

        LineReader reader = new LineReader(bufferedReader);

        Line actualLine = reader.readLine();
        assertEquals("Missing name", SUMMARY, actualLine.name);
        assertEquals("Missing value", "MessageExtension", actualLine.value);

        assertEquals("Missing end of stream marker", LineReader.END_OF_INPUT, reader.readLine());
    }
}
