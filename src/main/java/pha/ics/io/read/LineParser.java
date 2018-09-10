package pha.ics.io.read;

import org.jetbrains.annotations.NotNull;
import pha.ics.FieldName;

import java.util.ArrayList;
import java.util.List;

/**
 * Split a line into 3 parts, a name, a list of parameters(optional) and a value.
 * <p>
 * Created by paul on 21/09/14.
 */
class LineParser {

    static Line split(@NotNull String original) {


        char[] chars = original.toCharArray();
        List<Region> regions = getQuoteRegions(chars);


        int colonPosition = find(':', original, regions);

        String valuePart = null;
        if (haveColonAt(colonPosition)) {
            valuePart = original.substring(colonPosition + 1);
        }

        int semiPosition = find(';', original, regions);

        String key;
        List<String> params = null;
        if (haveSemicolonAt(semiPosition) && haveColonAt(colonPosition) && semiPosition < colonPosition) {
            key = original.substring(0, semiPosition);

            params = splitParams(original, semiPosition + 1, colonPosition, regions);

        } else if (colonPosition > -1) {
            key = original.substring(0, colonPosition);
        } else {
            key = original;
        }

        FieldName fieldName = FieldName.parse(key);

        if (fieldName == null) {
            fieldName = FieldName.UNKNOWN;
        }
        return new Line(fieldName, params, valuePart);
    }

    private static boolean haveColonAt(int position) {
        return position > -1;
    }

    private static boolean haveSemicolonAt(int position) {
        return position > -1;
    }

    /**
     * Split the string of parameters into an array.
     *
     * @param paramString to split
     * @param startPos    position of the start of the parameters in paramString
     * @param endPos      position of the end of the parameters in paramString
     * @param regions     quoted regions that should not be split
     * @return array of parameters
     */
    @NotNull
    private static List<String> splitParams(String paramString, int startPos, int endPos, List<Region> regions) {
        List<String> params = new ArrayList<String>();
        int start = startPos;
        int semiPosition = find(';', paramString, regions, start);
        while (haveSemicolonAt(semiPosition) && semiPosition < endPos) {
            params.add(paramString.substring(start, semiPosition));
            start = semiPosition + 1;
            semiPosition = find(';', paramString, regions, start);
        }
        params.add(paramString.substring(start, endPos));

        return params;
    }

    /**
     * Find the given character in the original string that is not between quotes.
     *
     * @param charToFind character to find
     * @param original   string to search
     * @param regions    quoted regions
     * @param startPos   position in original to start the search.
     * @return position of first matching char after startPos
     */
    private static int find(char charToFind, String original, List<Region> regions, int startPos) {
        int charPosition = original.indexOf(charToFind, startPos);

        while (charPosition > -1 && inQuotes(charPosition, regions)) {
            charPosition = original.indexOf(charToFind, charPosition + 1);
        }
        return charPosition;
    }

    private static int find(char charToFind, String original, List<Region> regions) {
        return find(charToFind, original, regions, 0);
    }

    /**
     * Is the given position inside quotes?
     *
     * @param position to check
     * @param regions  the quoted regions of the string
     * @return true if position is inside a quoted region
     */
    private static boolean inQuotes(int position, List<Region> regions) {
        for (Region region : regions) {
            if (region.contains(position)) {
                return true;
            }
        }

        return false;
    }

    private static List<Region> getQuoteRegions(char[] chars) {
        List<Region> regions = new ArrayList<Region>();

        // Find the quote regions
        int start = -1;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '"') {
                if (start == -1) {
                    start = i;
                } else {
                    regions.add(new Region(start, i));
                    start = -1;
                }
            }
        }
        // Unterminated quote
        if (start != -1) {
            regions.add(new Region(start, Integer.MAX_VALUE));
        }
        return regions;
    }

    private static class Region {
        private int start;
        private int end;

        Region(int startOfRegion, int endOfRegion) {
            start = startOfRegion;
            end = endOfRegion;
        }

        boolean contains(int position) {
            return position > start && position < end;
        }
    }
}
