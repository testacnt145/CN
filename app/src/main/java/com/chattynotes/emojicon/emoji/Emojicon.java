package com.chattynotes.emojicon.emoji;

import java.io.Serializable;


public class Emojicon implements Serializable {
    private static final long serialVersionUID = 1L;
    private String emoji;

    private Emojicon() {
    }

    public static Emojicon fromCodePoint(int codePoint) {
        Emojicon emoji = new Emojicon();
        emoji.emoji = newString(codePoint);
        return emoji;
    }

    //http://stackoverflow.com/questions/26231263/create-string-with-emoji-unicode-flag-countries
    public static Emojicon fromCodePoints(int[] codePoints) {
        Emojicon emoji = new Emojicon();
        emoji.emoji = new String(codePoints, 0, codePoints.length);
        return emoji;
    }

//    public static Emojicon fromString(String str) {
//        Emojicon emoji = new Emojicon();
//        emoji.emoji = str;
//        return emoji;
//    }

    public static Emojicon fromChar(char ch) {
        Emojicon emoji = new Emojicon();
        emoji.emoji = Character.toString(ch);
        return emoji;
    }

    public Emojicon(String emoji) {
        this.emoji = emoji;
    }

    public String getEmoji() {
        return emoji;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Emojicon && emoji.equals(((Emojicon) o).emoji);
    }

    @Override
    public int hashCode() {
        return emoji.hashCode();
    }

    public static String newString(int codePoint) {
        if (Character.charCount(codePoint) == 1) {
            return String.valueOf(codePoint);
        } else {
            return new String(Character.toChars(codePoint));
        }
    }
}
