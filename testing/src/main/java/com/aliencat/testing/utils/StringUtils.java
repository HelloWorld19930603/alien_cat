package com.aliencat.testing.utils;


public class StringUtils {
    public static boolean isNotEmpty(final CharSequence cs) {
        return !isEmpty(cs);
    }
    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean startsWith(String anyString, String eq) {
        if(anyString == null || eq == null){
            return false;
        }
        return anyString.startsWith(eq);
    }
}
