package com.chattynotes.util.textformatter;

import java.util.ArrayList;

class TextUtil {

    static class FormatFlag {
        int start; int end; char flag;
        FormatFlag(int start, int end, char flag) {
            this.start = start;
            this.end = end;
            this.flag = flag;
        }
    }

    private static final char NEW_LINE = '\n';
    static final char BOLD_FLAG = '*';
    static final char STRIKE_FLAG = '~';
    static final char ITALIC_FLAG = '_';
    static final int INVALID_INDEX = -1;

    public static String getText(ArrayList<Character> characters) {
        char[] chars = new char[characters.size()];

        for (int i = 0; i < chars.length; i++) {
            chars[i] = characters.get(i);
        }
        return new String(chars);
    }

    static boolean hasFlagSameLine(CharSequence sequence, char flag, int fromIndex) {
        for(int i=fromIndex;i<sequence.length();i++) {
            char c = sequence.charAt(i);
            if(c == NEW_LINE) {
                return false;
            }
            if(c == flag ) {
                return i != fromIndex;
            }
        }
        return false;
    }
}
