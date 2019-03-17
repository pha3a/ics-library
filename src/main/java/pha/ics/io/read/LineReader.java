package pha.ics.io.read;

import org.jetbrains.annotations.NotNull;
import pha.ics.FieldName;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Read single lines of a ics file.
 * <p>
 * Created by paul on 12/07/16.
 */
class LineReader {

    /**
     * Special line returned when there are no more lines to be read
     */
    static final Line END_OF_INPUT = new Line(FieldName.END, null, null);


    /**
     * Internal buffer used to hold the next line while merging wrapped lines
     */
    private String nextLine;

    /**
     * Handler used to determine how to merge multiple lines from different files.
     */
    private Handler handler = new GeneralHandler();


    private final BufferedReader wrappedReader;

    LineReader(Reader reader) {
        wrappedReader = new BufferedReader(reader);
    }


    @NotNull
    Line readLine() throws IOException {

        StringBuilder builder;
        if (nextLine == null) {
            nextLine = wrappedReader.readLine();
            if (nextLine == null) {
                return END_OF_INPUT;
            }
        }
        builder = new StringBuilder(nextLine);
        nextLine = getNextLine(builder);

        return LineParser.split(builder.toString());
    }

    /**
     * Collect wrapped lines into a single handler handling any continuation handler
     * characters.
     *
     * @param builder used to colect all parts of this handler.
     * @return the following handler, note this may be the fist part of another wrapped handler.
     * @throws IOException from underlaying reader when handler read from it.
     */
    private String getNextLine(StringBuilder builder) throws IOException {

        String line = wrappedReader.readLine();
        if (line != null) {
            if (handler.isNewLine(line)) {
                return line;
            }
            handler.appendTo(builder, line);
            return getNextLine(builder);
        }
        return null;
    }

    void setFormatToAppgenix() {
        handler = new AppgenixHandler();
    }


    void close() throws IOException {
        wrappedReader.close();
    }


    interface Handler {
        boolean isNewLine(String line);

        void appendTo(StringBuilder builder, String line);

    }

    private class GeneralHandler implements Handler {

        @Override
        public boolean isNewLine(String line) {
            return !line.startsWith("\t") && !line.startsWith(" ");
        }

        @Override
        public void appendTo(StringBuilder builder, String line) {
            if (line.startsWith("\t") || line.startsWith(" ")) {
                // Just remove the tab/space
                builder.append(line.substring(1));
            } else {
                throw new IllegalStateException("No tab or space at start of line");
            }
        }
    }

    private class AppgenixHandler implements Handler {


        /**
         * Last recognized header, used to decide if multple lines can be wrapped into
         * one.
         * <p>
         * NOTE: First line read can NOT be a description, as this field will not have been set.
         */
        private FieldName lastHeader;

        /**
         * Flag indicating that we ahve seen a new line startign with a TAB character.
         * This indicates that this file has been created by a "fixed" application.
         */
        private boolean seenTabs = false;

        AppgenixHandler() {
        }

        @Override
        public boolean isNewLine(String line) {

            // Is this a fixed file with correct line wrapping?
            if (line.startsWith("\t")) {
                seenTabs = true;
                return false;
            }

            for (FieldName keyName : FieldName.values()) {
                String keyNameStr = keyName.toString();
                if (line.startsWith(keyNameStr) && line.length() > keyNameStr.length()) {
                    char c = line.charAt(keyNameStr.length());
                    if (c == ':' || c == ';') {
                        lastHeader = keyName;
                        return true;
                    }
                }
            }
            return false;

        }

        @Override
        public void appendTo(StringBuilder builder, String line) {

            if (seenTabs && line.startsWith("\t") && line.length() > 1) {
                // Just remove the tab
                builder.append(line.substring(1));
            } else {
                // Keep the CR if we are processign a description.
                // This is the only field that may have multiple lines.
                if (lastHeader == FieldName.DESCRIPTION) {
                    builder.append("\n");
                }
                builder.append(line);
            }
        }
    }
}
