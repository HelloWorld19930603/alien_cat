package com.aliencat.javabase.code;

public class RareCharacterUtility {

    public static boolean containsUserDefinedUnicode(String string) {
        if (string == null) {
            throw new NullPointerException("Stirng must be non-null");
        }
        int[] code = toCodePointArray(string);
        // U+E000..U+F8FF
        for (int c : code) {
            if (c >= '\ue000' && c <= '\uf8ff') {
                return true;
            }
        }
        return false;
    }

    static int[] toCodePointArray(String str) {
        int len = str.length();
        int[] acp = new int[str.codePointCount(0, len)];

        for (int i = 0, j = 0; i < len; i = str.offsetByCodePoints(i, 1)) {
            acp[j++] = str.codePointAt(i);
        }
        return acp;
    }

    static String toHex(int[] chars) {
        String r = "[";
        for (int i = 0; i < chars.length; i++) {
            if (r.length() > 1) {
                r += ",";
            }
            r += Integer.toHexString(chars[i]);
        }
        r += "]";
        return r;
    }

    public static void main(String[] argu) {
        String rr = ("\u5f20\ue0bf\uD86C\uDE70\uD840\uDC10\uD86D\uDF44\uD87E\uDCAC\u9fc6");
        rr = "张\uE0BF\uD86C\uDE70\uD840\uDC10\uD86D\uDF44\uD87E\uDCAC鿆";
        System.out.println("Unicode = " + toHex(toCodePointArray(rr)));

        boolean r = (containsUserDefinedUnicode(rr));
        System.out.println("Test result = " + r);
    }
}
