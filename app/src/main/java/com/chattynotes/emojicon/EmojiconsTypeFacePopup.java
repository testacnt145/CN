package com.chattynotes.emojicon;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.PopupWindow;
import com.chattynotes.R;
import com.chattynotes.emojicon.EmojiconGridView.OnEmojiconClickedListener;
import com.chattynotes.emojicon.emoji.Emojicon;


class EmojiconsTypeFacePopup extends PopupWindow {

	private View rootView;
	private View anchorView;
	private Context mContext;

	EmojiconsTypeFacePopup(View anchorView, ViewGroup parent, Context mContext, Emojicon[] emojicons, final EmojiconsPopup popup) {
		//http://stackoverflow.com/a/19908611/4754141
		super(mContext);
		this.mContext = mContext;
		this.anchorView = anchorView;
		LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.rootView = layoutInflater.inflate(R.layout.emojicons, parent, false);
		View popupView = layoutInflater.inflate(R.layout.emoji_popup_layout, parent, false);
		//grid-view
		GridView gridView = (GridView)popupView.findViewById(R.id.Emoji_GridView);
		AdapterEmojiTypeFace mAdapter = new AdapterEmojiTypeFace(mContext, emojicons, popup);
		gridView.setAdapter(mAdapter);
		mAdapter.setEmojiClickListener(new OnEmojiconClickedListener() {
			@Override
			public void onEmojiconClicked(Emojicon emojicon) {
				if (popup.onEmojiconClickedListener != null) {
					popup.onEmojiconClickedListener.onEmojiconClicked(emojicon);
				}
			}
		});

		//popup view
		setContentView(popupView);
		// Closes the popup window when touch outside of it - when looses focus
		//http://stackoverflow.com/questions/12232724/popupwindow-dismiss-when-clicked-outside
		setBackgroundDrawable(new ColorDrawable());
		setOutsideTouchable(true);
		//setFocusable(true);
		//http://stackoverflow.com/a/11244139/4754141
//		setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
//		setHeight(100);
		//default size
		setSize(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		//setSize((int) mContext.getResources().getDimension(R.dimen.keyboard_height), 100);
	}


//__________________________________________________________________________________________________
	private void setSize(int width, int height){
		setWidth(width);
		setHeight(height);
	}

	void showAtLocation() {
		int location[] = new int[2];
		rootView.getLocationOnScreen(location);
		showAtLocation(rootView, Gravity.BOTTOM, location[0], location[1]);
	}

	@Override
	public void dismiss() {
		super.dismiss();
	}

}
