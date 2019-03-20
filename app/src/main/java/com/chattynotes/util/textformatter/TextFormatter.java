package com.chattynotes.util.textformatter;

import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;
import java.util.ArrayList;
import static com.chattynotes.util.textformatter.TextUtil.BOLD_FLAG;
import static com.chattynotes.util.textformatter.TextUtil.INVALID_INDEX;
import static com.chattynotes.util.textformatter.TextUtil.ITALIC_FLAG;
import static com.chattynotes.util.textformatter.TextUtil.STRIKE_FLAG;

public class TextFormatter {

    public static void applyFormatting(final TextView textView, String msg) {
        if (!TextUtils.isEmpty(msg)) {
            CharSequence formatted = extractFlagsForTextView(msg);
            textView.setText(formatted);
        } else
            textView.setText(msg);
    }

    private static CharSequence extractFlagsForTextView(CharSequence text) {

        char[] textChars = text.toString().toCharArray();
        //used to put characters except valid flags
        //meaning if 1 * so it will be put, but 2 ** wont be put
        ArrayList<Character> characterList = new ArrayList<>();
        //used to put valid flags
        ArrayList<TextUtil.FormatFlag> formatFlagList = new ArrayList<>();

        //boldFlag (startIndex, endIndex, *)
        //initialize boldFlag startIndex and endIndex as INVALID_INDEX
        TextUtil.FormatFlag boldFlag = new TextUtil.FormatFlag(INVALID_INDEX, INVALID_INDEX, BOLD_FLAG);
        TextUtil.FormatFlag strikeFlag = new TextUtil.FormatFlag(INVALID_INDEX, INVALID_INDEX, STRIKE_FLAG);
        TextUtil.FormatFlag italicFlag = new TextUtil.FormatFlag(INVALID_INDEX, INVALID_INDEX, ITALIC_FLAG);


        //Extracting all valid flags and putting them in flagList
        //it text is between 2 * (*hi*) add boldFlag in flagList
        for (int i = 0, j = 0; i < textChars.length; i++) {
            //loop through each character
            char c = textChars[i];

            //if character is BOLD_FLAG,
            //1- check whether its in start or end
            if (c == BOLD_FLAG) {
                //if startIndex == INVALID_INDEX, * is in start
                if (boldFlag.start == INVALID_INDEX) {
                    if (TextUtil.hasFlagSameLine(text, BOLD_FLAG, i + 1)) {
                        boldFlag.start = j; //2- mark the boldFlag startIndex
                        continue;
                    }
                } else {
                    boldFlag.end = j; //3- mark the boldFlag endIndex
                    formatFlagList.add(boldFlag); //4- add the flag into flagList
                    boldFlag = new TextUtil.FormatFlag(INVALID_INDEX, INVALID_INDEX, BOLD_FLAG); //5- reinitialize for further bold text in string
                    continue;
                }
            }
            if (c == STRIKE_FLAG) {
                if (strikeFlag.start == INVALID_INDEX) {
                    if (TextUtil.hasFlagSameLine(text, STRIKE_FLAG, i + 1)) {
                        strikeFlag.start = j;
                        continue;
                    }
                } else {
                    strikeFlag.end = j;
                    formatFlagList.add(strikeFlag);
                    strikeFlag = new TextUtil.FormatFlag(INVALID_INDEX, INVALID_INDEX, STRIKE_FLAG);
                    continue;
                }
            }
            if (c == ITALIC_FLAG) {
                if (italicFlag.start == INVALID_INDEX) {
                    if (TextUtil.hasFlagSameLine(text, ITALIC_FLAG, i + 1)) {
                        italicFlag.start = j;
                        continue;
                    }
                } else {
                    italicFlag.end = j;
                    formatFlagList.add(italicFlag);
                    italicFlag = new TextUtil.FormatFlag(INVALID_INDEX, INVALID_INDEX, ITALIC_FLAG);
                    continue;
                }
            }
            //put characters rather than valid flags
            characterList.add(c);
            j++;
        }


        //After filling flagList
        //apply styling on the text of these flagList items
        //as each flag contains startIndex and endIndex and what type of flag it is
        String formatted = TextUtil.getText(characterList);
        SpannableStringBuilder builder = new SpannableStringBuilder(formatted);

        for (int i = 0; i < formatFlagList.size(); i++) {
            TextUtil.FormatFlag formatFlag = formatFlagList.get(i);
            if (formatFlag.flag == BOLD_FLAG) {
                StyleSpan bss = new StyleSpan(Typeface.BOLD);
                builder.setSpan(bss, formatFlag.start, formatFlag.end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            } else if (formatFlag.flag == STRIKE_FLAG) {
                builder.setSpan(new StrikethroughSpan(), formatFlag.start, formatFlag.end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            } else if (formatFlag.flag == ITALIC_FLAG) {
                StyleSpan iss = new StyleSpan(Typeface.ITALIC);
                builder.setSpan(iss, formatFlag.start, formatFlag.end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            }
        }
        return builder;
    }
}
