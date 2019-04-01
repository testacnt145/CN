package com.chattynotes.emojicon;

import java.util.Arrays;
import com.chattynotes.emojicon.emoji.Emojicon;
import com.chattynotes.emojicon.emoji.People_1;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import com.chattynoteslite.R;


public class EmojiconGridView {

	public View rootView;
	EmojiconsPopup mEmojiconPopup;
    private EmojiconRecents mRecents;
    private Emojicon[] mData;
    
    public EmojiconGridView(Context context, Emojicon[] emojicons, EmojiconRecents recents, EmojiconsPopup emojiconPopup, int tab) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		mEmojiconPopup = emojiconPopup;
		rootView = inflater.inflate(R.layout.emojicon_grid, null);
		setRecents(recents);
		GridView gridView = (GridView) rootView.findViewById(R.id.Emoji_GridView);
	        if (emojicons==null) {
	            mData = People_1.DATA;
	        } else {
	            Object[] o = emojicons;
	            mData = Arrays.asList(o).toArray(new Emojicon[o.length]);
	        }
	        AdapterEmoji mAdapter = new AdapterEmoji(rootView.getContext(), mData, tab, mEmojiconPopup);

			//single click
	        mAdapter.setEmojiClickListener(new OnEmojiconClickedListener() {
				@Override
				public void onEmojiconClicked(Emojicon emojicon) {
					if (mEmojiconPopup.onEmojiconClickedListener != null) {
			            mEmojiconPopup.onEmojiconClickedListener.onEmojiconClicked(emojicon);
			        }
			        if (mRecents != null) {
			            mRecents.addRecentEmoji(rootView.getContext(), emojicon);
			        }
				}
			});

	        gridView.setAdapter(mAdapter);
	}
    
	private void setRecents(EmojiconRecents recents) {
        mRecents = recents;
    }

    public interface OnEmojiconClickedListener {
        void onEmojiconClicked(Emojicon emojicon);
    }
}
