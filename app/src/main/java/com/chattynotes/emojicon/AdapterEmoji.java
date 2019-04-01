package com.chattynotes.emojicon;

import java.util.List;
import com.chattynotes.emojicon.EmojiconGridView.OnEmojiconClickedListener;
import com.chattynotes.emojicon.emoji.Activity_4;
import com.chattynotes.emojicon.emoji.Emojicon;
import com.chattynotes.emojicon.emoji.People_1;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.chattynoteslite.R;
import android.support.annotation.NonNull;

class AdapterEmoji extends ArrayAdapter<Emojicon> {

	private OnEmojiconClickedListener emojiClickListener;

    static EmojiconsTypeFacePopup emojiTypeFacePopup;
    private EmojiconsPopup popup;
    private int tab=0;

    AdapterEmoji(Context context, List<Emojicon> data) {
        super(context, R.layout.emojicon_item, data);
    }

    AdapterEmoji(Context context, Emojicon[] data, int _tab, EmojiconsPopup _popup) {
        super(context, R.layout.emojicon_item, data);
        tab = _tab;
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

        //onLongClick
        holder.icon.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(isTypeFaceEmoji(tab, position)) {
                    //LogUtil.e(getClass().getSimpleName() + "~~~", "t: " + tab + " :pos: " + position);
                    //[emoji_: when adding typeface emoji, make sure to enter it here]
                    Emojicon[] emojiArray = new Emojicon[0];
                    if (tab == 1) {
                        emojiArray = People_1.getTypeFaceEmojiArray(position);
                    } else if (tab == 4) {
                        emojiArray = Activity_4.getTypeFaceEmojiArray(position);
                    }
                    emojiTypeFacePopup = new EmojiconsTypeFacePopup(holder.icon, parent, getContext(), emojiArray, popup);
                    emojiTypeFacePopup.showAtLocation();

                    return true;
                }
                return false;
            }
        });

        return v;
    }

    //[emoji_: when adding typeface emoji, make sure to enter it here]
    private boolean isTypeFaceEmoji(int tab, int position) {
        if(tab == 1) {
            switch (position) {
                case People_1.POSITION_PERSON_BOTH_HAND_CELEBRATION:
                //12
                case People_1.POSITION_CLAPPING_HAND:
                case People_1.POSITION_WAVING_HANDS:
                case People_1.POSITION_THUMBS_UP:
                case People_1.POSITION_THUMBS_DOWN:
                case People_1.POSITION_FIST_HAND:
                case People_1.POSITION_RAISED_FIST:
                case People_1.POSITION_VICTORY_HAND:
                case People_1.POSITION_OK_HAND:
                //13
                case People_1.POSITION_RAISED_HAND:
                case People_1.POSITION_OPEN_HAND:
                case People_1.POSITION_FLEXED_BICEPS:
                case People_1.POSITION_FOLDED_HANDS:
                case People_1.POSITION_UP_POINTING_INDEX:
                case People_1.POSITION_UP_POINTING_BACKHAND_INDEX:
                case People_1.POSITION_DOWN_POINTING_BACKHAND_INDEX:
                case People_1.POSITION_LEFT_POINTING_BACKHAND_INDEX:
                //14
                case People_1.POSITION_RIGHT_POINTING_BACKHAND_INDEX:
                case People_1.POSITION_REVERSE_MIDDLE_FINGER:
                case People_1.POSITION_RAISED_HAND_FINGERS_SPLAYED:
                case People_1.POSITION_SIGN_OF_HORN:
                case People_1.POSITION_RAISED_HAND_PART_BETWEEN_MIDDLE_RING:
                case People_1.POSITION_WRITING_HAND:
                case People_1.POSITION_NAIL_POLISH:
                //15
                case People_1.POSITION_EAR:
                case People_1.POSITION_NOSE:
                //16
                case People_1.POSITION_BABY:
                case People_1.POSITION_BOY:
                case People_1.POSITION_GIRL:
                case People_1.POSITION_MAN:
                case People_1.POSITION_WOMEN:
                case People_1.POSITION_PERSON_WITH_BLOND_HAIR:
                case People_1.POSITION_OLDER_MAN:
                case People_1.POSITION_OLDER_WOMEN:
                //17
                case People_1.POSITION_MAN_WITH_GUA_PI_MAO:
                case People_1.POSITION_MAN_WITH_TURBAN:
                case People_1.POSITION_POLICE_OFFICER:
                case People_1.POSITION_CONSTRUCTION_WORKER:
                case People_1.POSITION_GUARDS_MAN:
                case People_1.POSITION_FATHER_CHRISTMAS:
                case People_1.POSITION_BABY_ANGEL:
                //18
                case People_1.POSITION_PRINCESS:
                case People_1.POSITION_BRIDE_WITH_VEIL:
                case People_1.POSITION_PEDESTRIAN:
                case People_1.POSITION_RUNNER:
                case People_1.POSITION_DANCER:
                //19
                case People_1.POSITION_PERSON_BOWING_DEEPLY:
                case People_1.POSITION_INFORMATION_DESK_PERSON:
                case People_1.POSITION_FACE_WITH_NO_GOOD_GESTURE:
                case People_1.POSITION_FACE_WITH_OK_GESTURE:
                case People_1.POSITION_HAPPY_PERSON_RAISE_ONE_HAND:
                case People_1.POSITION_PERSON_WITH_POUTING_FACE:
                case People_1.POSITION_PERSON_FROWNING:
                //20
                case People_1.POSITION_HAIRCUT:
                case People_1.POSITION_FACE_MASSAGE:
                return true;
            }
        } else if(tab == 4) {
            switch (position) {
                case Activity_4.POSITION_ROW_BOAT:
                case Activity_4.POSITION_SWIMMER:
                case Activity_4.POSITION_SURFER:
                case Activity_4.POSITION_BATH:
                case Activity_4.POSITION_PERSON_WITH_BALL:
                case Activity_4.POSITION_WEIGHT_LIFTER:
                case Activity_4.POSITION_BICYCLIST:
                case Activity_4.POSITION_MOUNTAIN_BICYCLIST:
                case Activity_4.POSITION_HORSE_RACING:
                return true;
            }
        }
        return false;
    }

    private class ViewHolder {
        TextView icon;
    }
}