package com.chattynotes.emojicon;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.chattynotes.R;
import com.chattynotes.emojicon.EmojiconGridView.OnEmojiconClickedListener;
import com.chattynotes.emojicon.emoji.Emojicon;


class AdapterEmojiTypeFace extends ArrayAdapter<Emojicon> {

	private OnEmojiconClickedListener emojiClickListener;
    private EmojiconsPopup popup;

    AdapterEmojiTypeFace(Context context, Emojicon[] data, EmojiconsPopup _popup) {
        super(context, R.layout.emojicon_item, data);
        popup = _popup;
    }
    
    void setEmojiClickListener(OnEmojiconClickedListener listener){
    	this.emojiClickListener = listener;
    }

    @NonNull
    @Override
    public View getView(final int position, final View convertView, @NonNull final ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            v = View.inflate(getContext(), R.layout.emojicon_item, null);
            ViewHolder holder = new ViewHolder();
            holder.icon = (TextView) v.findViewById(R.id.emojicon_icon);
            v.setTag(holder);
        }

        Emojicon emoji = getItem(position);
        final ViewHolder holder = (ViewHolder) v.getTag();
        holder.icon.setText(emoji.getEmoji());

        //onClick
        holder.icon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                emojiClickListener.onEmojiconClicked(getItem(position));
			}
		});
        return v;
    }

    class ViewHolder {
        TextView icon;
    }
}