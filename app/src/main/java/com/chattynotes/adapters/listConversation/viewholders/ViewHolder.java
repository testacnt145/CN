package com.chattynotes.adapters.listConversation.viewholders;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.chattynotes.emojicon.EmojiconTextView;

public class ViewHolder {

    public static class Parent {
        //timebar
        public LinearLayout llTimeBar;
        public ImageView imgViewShare;
        public TextView txtViewTime;
        public ImageView imgViewStar;
        public ImageView imgViewMsgStatus;
        //layouts
        public RelativeLayout rlFather;
        public RelativeLayout rlMother;
        public RelativeLayout.LayoutParams rlParam;
    }

    public static class Text extends Parent {
        public EmojiconTextView txtViewMessage_T;
    }

    public static class Image extends Parent {
        public ImageView imgViewMessage_I;
        public EmojiconTextView txtViewMedCap_I;
    }

    public static class Video extends Parent {
        public ImageView imgViewMessage_V;
        public EmojiconTextView txtViewMedCap_V;
        public RelativeLayout rl_Compressing_V;
    }

    public static class Audio extends Parent {
        //audio player
        public Button btnPlay_A;
        public SeekBar sBar_A;
        public TextView tvTimerRight_A;
    }

    public static class LongText extends Parent {
        public EmojiconTextView txtViewMessage_LT;
        public Button btnViewMore_LT;
    }

    public static class Location extends Parent {
        public ImageView imgViewMessage_L;
    }

    public static class Address extends Parent {
        public ImageView imgViewMessage_Ad;
        //detail address
        public TextView txtViewAddName_Ad;
        public TextView txtViewAddFull_Ad;
    }

    public static class Contact extends Parent {
        //contact name
        public TextView txtViewContactName_C;
    }

    public static class Link extends Parent {
        public EmojiconTextView txtViewMessage_L;
        public TextView txtViewTitle_L;
        public TextView txtViewDesc_L;
        public TextView txtViewUrl_L;
        public ImageView imgView_L;
    }

    public static class Date {
        public EmojiconTextView txtViewDateSeparator;
    }

    public static class LoadEarlier {
        public TextView txtViewLoadEarlier;
    }
}
