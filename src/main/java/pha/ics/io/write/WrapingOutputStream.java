package pha.ics.io.write;

import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;

/**
 * Output stream that will wrap all lines at 75 octets (bytes). UTF-8 characters
 * longer than 1 bytes will not be split across multiple lines. If an extended UTF-8
 * character would span a line break then the line break is written out before
 * the UTF-8 character, resulting in a line shorter than 75 bytes.
 * <p>
 * Also makes sure that lines are terminated by CR & LF. Used to export Calendar files.
 * <p>
 * Created by paul on 25/08/14.
 */
class WrapingOutputStream extends FilterOutputStream {

    private int currentLineLength = 0;
    private byte[] utf8Buffer = new byte[7];
    private int utf8Index = 0;

    public WrapingOutputStream(FileOutputStream outputStream) {
        super(outputStream);
    }

    @Override
    public void write(int b) throws IOException {
        if (b == '\n') {
            if (System.getProperty("line.separator").equals("\n")) {
                super.write('\r');
            }
            super.write('\n');
            currentLineLength = 0;

        } else {

            wrapLine(b);

            super.write(b);
            currentLineLength++;
        }
    }


    /**
     * Calculate how long this character is, if we have the first byte
     * of a UTF-8 character return its total length in bytes, otherwise return 1.
     * This is used to prevent line wrapping occuring half way through a UTF-8
     * character.
     *
     * @param b byte to write
     * @return number of bytes in character.
     */
    private int bytesInSequence(int b) {
        int chr =  (b & 0b11111111);


        if ((chr & 0b11100000) == 0b11000000) {
            return 2;
        }
        if ((chr & 0b11110000) == 0b11100000) {
            return 3;
        }
        if ((chr & 0b11111000) == 0b11110000) {
            return 4;
        }
        if ((chr & 0b11111100) == 0b11111000) {
            return 5;
        }
        if ((chr & 0b11111110) == 0b11111100) {
            return 6;
        }

        return 1;

    }

    /**
     * If the line will be too long when the byte is written, wrap the line.
     * Byte is check to verify that it is not an extended UTF-8 character, if
     * such a character would be split by the next line wrap then wrap the line now.
     *
     * @param b byte to write
     * @throws IOException if we fail to write the byte
     */
    private void wrapLine(int b) throws IOException {
        if (currentLineLength + bytesInSequence(b) > 75) {
            super.write('\r');
            super.write('\n');
            super.write('\t');
            currentLineLength = 0;
        }
    }
}
