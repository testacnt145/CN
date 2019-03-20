package com.chattynotes.emojicon;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;
import com.chattynotes.R;


public class EmojiconTextView extends TextView {
    private float mEmojiconSize;
    private int mTextStart = 0;
    private int mTextLength = -1;

    public EmojiconTextView(Context context) {
        super(context);
        init(null);
    }

    public EmojiconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public EmojiconTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs == null) {
            mEmojiconSize = getEmojiconSize();
        } else {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Emojicon);
            mEmojiconSize = a.getDimension(R.styleable.Emojicon_emojiconSize, getEmojiconSize());
            mTextStart = a.getInteger(R.styleable.Emojicon_emojiconTextStart, 0);
            mTextLength = a.getInteger(R.styleable.Emojicon_emojiconTextLength, -1);
            a.recycle();
        }
        setText(getText());
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (!TextUtils.isEmpty(text)) {
            SpannableStringBuilder builder = new SpannableStringBuilder(text);
            EmojiconHandler.addEmojis(getContext(), builder, (int)mEmojiconSize, mTextStart, mTextLength);
            text = builder;
        }
        super.setText(text, type);
    }



    //[custom_: ] [jugaar_: ]
    //@link https://github.com/rockerhieu/emojicon/blob/master/library/src/main/java/com/rockerhieu/emojicon/EmojiconTextView.java
    // Set the size of emojicon in pixels.
    // below method is copied from Android TextView.java (setTextSize method)
    public void setEmojiconSize(int pixels) {
        Context c = getContext();
        Resources r;
        if (c == null)
            r = Resources.getSystem();
        else
            r = c.getResources();
        mEmojiconSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, pixels, r.getDisplayMetrics());
        super.setText(getText());
    }

    //use this method to get emoji size
    public float getEmojiconSize() {
        mEmojiconSize = getTextSize();
        return mEmojiconSize;
    }

}