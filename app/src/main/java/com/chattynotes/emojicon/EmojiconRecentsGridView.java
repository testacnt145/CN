package com.chattynotes.emojicon;

import com.chattynotes.emojicon.emoji.Emojicon;
import android.content.Context;
import android.widget.GridView;
import com.chattynotes.R;


class EmojiconRecentsGridView extends EmojiconGridView implements EmojiconRecents {
	private AdapterEmoji mAdapter;
	
	EmojiconRecentsGridView(Context context, Emojicon[] emojicons, EmojiconRecents recents, EmojiconsPopup emojiconsPopup) {
		super(context, emojicons, recents, emojiconsPopup, 0);
		EmojiconRecentsManager recents1 = EmojiconRecentsManager.getInstance(rootView.getContext());
		mAdapter = new AdapterEmoji(rootView.getContext(),  recents1);
		mAdapter.setEmojiClickListener(new OnEmojiconClickedListener() {
			@Override
			public void onEmojiconClicked(Emojicon emojicon) {
				if (mEmojiconPopup.onEmojiconClickedListener != null) {
		            mEmojiconPopup.onEmojiconClickedListener.onEmojiconClicked(emojicon);
		        }
		    }
		});
        GridView gridView = (GridView) rootView.findViewById(R.id.Emoji_GridView);
        gridView.setAdapter(mAdapter);
    }

    @Override
    public void addRecentEmoji(Context context, Emojicon emojicon) {
        EmojiconRecentsManager recents = EmojiconRecentsManager.getInstance(context);
        recents.push(emojicon);

        // notify data set changed
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

}
