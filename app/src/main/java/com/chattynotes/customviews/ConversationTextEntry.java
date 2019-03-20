package com.chattynotes.customviews;

import com.chattynotes.emojicon.EmojiconHandler;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;

import com.chattynotes.R;

public class ConversationTextEntry extends EditText {
	//com.chattynotes.customviews.ConversationTextEntry

//_________________________________________________________________________________________
	private int mEmojiconSize;

    public ConversationTextEntry(Context context) {
        super(context);
        mEmojiconSize = (int) getTextSize();

    }

    public ConversationTextEntry(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ConversationTextEntry(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Emojicon);
        mEmojiconSize = (int) a.getDimension(R.styleable.Emojicon_emojiconSize, getTextSize());
        a.recycle();
        setText(getText());
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        EmojiconHandler.addEmojis(getContext(), getText(), mEmojiconSize);
    }

    /**
     * Set the size of emojicon in pixels.
     */
    public void setEmojiconSize(int pixels) {
        mEmojiconSize = pixels;
    }

//_________________________________________________________________________________________
	public interface KeyImeChange {
	    void onKeyIme(int keyCode, KeyEvent event);
	}

	private KeyImeChange keyImeChangeListener;

//comment because of duplication
//    public CustomEditText(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }

    public void setKeyImeChangeListener(KeyImeChange listener){
        keyImeChangeListener = listener;
    }

    @Override
    public boolean onKeyPreIme (int keyCode, KeyEvent event){
        if(keyImeChangeListener != null) {
            keyImeChangeListener.onKeyIme(keyCode, event);
        }
        return false;
    }

    //http://stackoverflow.com/questions/5014219/multiline-edittext-with-done-softinput-action-label-on-2-3
    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
    	 InputConnection conn = super.onCreateInputConnection(outAttrs);
         outAttrs.imeOptions &= ~EditorInfo.IME_FLAG_NO_ENTER_ACTION;
         return conn;
    }

}