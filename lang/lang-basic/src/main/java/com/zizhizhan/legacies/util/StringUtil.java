package com.zizhizhan.legacies.util;

public class StringUtil {

    private StringUtil() {
    }

    /**
     * Determines if the string is null or blank.  This was created
     * because of the PMD report.  It concludes that calling
     * trim() on a string creates a new String and it recommends
     * checking the characters for a non-whitespace character.
     *
     * @param s string
     * @return true if the string is null or blank.
     */
    public static boolean isEmpty(String s) {
        boolean empty = true;
        if (s != null) {
            char[] cArray = s.toCharArray();
            for (int i = 0; i < cArray.length; i++) {
                if (!Character.isWhitespace(s.charAt(i))) {
                    empty = false;
                    break;
                }
            }
        }
        return empty;
    }

    /**
     * Checks that the string array is not null and that
     * every element in the array has a value.
     *
     * @param stringArray
     * @return true if the array is populated, false otherwise
     */
    public static boolean isPopulated(String[] stringArray) {
        boolean isValid = (stringArray != null && stringArray.length > 0);
        for (int i = 0; isValid && i < stringArray.length; i++) {
            isValid &= !StringUtil.isEmpty(stringArray[i]);
        }
        return isValid;
    }

    /**
     * Returns the string representation of the object array in the same format
     * as the 1.5 Arrays.toString() method.
     *
     * @param o Array of objects.
     * @return String representation of array.
     */
    public static String toString(Object[] o) {
        StringBuffer sb = new StringBuffer('[');
        if (o != null && o.length > 0) {
            int index = 0;
            while (index < o.length - 1) {
                Object object = o[index++];
                sb.append(object == null ? "null" : object.toString());
                sb.append(", ");
            }
            sb.append(o[index]);
        }
        sb.append(']');
        return sb.toString();
    }

}
