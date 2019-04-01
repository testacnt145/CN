package com.chattynotes.adapters.listConversation.utility;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.chattynoteslite.R;
import com.chattynotes.emojicon.EmojiconTextView;
import com.chattynotes.adapters.listConversation.viewholders.ViewHolder;
import com.chattynotes.preferences.Prefs;

public class InitializeUtil {

    public static void date(ViewHolder.Date holderDate, View convertView) {
        holderDate.txtViewDateSeparator = (EmojiconTextView) convertView.findViewById(R.id.id_txtViewDateSeparator);
    }

    public static void loadEarlier(ViewHolder.LoadEarlier holderLoadEarlier, View convertView) {
        holderLoadEarlier.txtViewLoadEarlier = (TextView) convertView.findViewById(R.id.id_txtViewLoadEarlier);
    }

    public static void text(Context context, ViewHolder.Text holderText, View convertView) {
        holderText.txtViewMessage_T = (EmojiconTextView) convertView.findViewById(R.id.txtViewMessage_T);
        holderText.txtViewMessage_T.setTextSize(TypedValue.COMPLEX_UNIT_SP, Prefs.getFontSize(context));
        holderText.txtViewMessage_T.setEmojiconSize(Prefs.getEmojiconSize(context));
        //((EmojiconTextView)holderText.txtViewMessage_T).setEmojiconSize(Prefs.getFontSize(context) + 100);
        //XML__ issues (2)
        //http://stackoverflow.com/questions/14135228/linkify-listview-actionmode-regular-text-no-longer-clickable
        //http://stackoverflow.com/questions/8558732/listview-textview-with-linkmovementmethod-makes-list-item-unclickable
        //holderText.txtViewMessage_T.setMovementMethod(LinkMovementMethod.getInstance());
        //Linkify.addLinks(holderText.txtViewMessage_T, Linkify.ALL);
        parentLayouts(holderText, convertView);
        msgTimeBar(holderText, convertView);
    }

    public static void image(Context context, ViewHolder.Image holderImage, View convertView) {
        holderImage.imgViewMessage_I = (ImageView) convertView.findViewById(R.id.imgViewMessage_I);
        holderImage.txtViewMedCap_I = (EmojiconTextView) convertView.findViewById(R.id.txtViewMedCap_I);
        holderImage.txtViewMedCap_I.setTextSize(TypedValue.COMPLEX_UNIT_SP, Prefs.getFontSize(context));
        holderImage.txtViewMedCap_I.setEmojiconSize(Prefs.getEmojiconSize(context));
        parentLayouts(holderImage, convertView);
        msgTimeBar(holderImage, convertView);
    }

    public static void audio(ViewHolder.Audio holderAudio, View convertView) {
        //audio player
        holderAudio.btnPlay_A = (Button) convertView.findViewById(R.id.btnPlay_A);
        holderAudio.sBar_A = (SeekBar) convertView.findViewById(R.id.sBar_A);
        holderAudio.tvTimerRight_A = (TextView) convertView.findViewById(R.id.tvTimerRight_A);
        //other
        parentLayouts(holderAudio, convertView);
        msgTimeBar(holderAudio, convertView);
    }

    public static void video(Context context, ViewHolder.Video holderVideo, View convertView) {
        holderVideo.imgViewMessage_V = (ImageView) convertView.findViewById(R.id.imgViewMessage_V);
        holderVideo.txtViewMedCap_V = (EmojiconTextView) convertView.findViewById(R.id.txtViewMedCap_V);
        holderVideo.txtViewMedCap_V.setTextSize(TypedValue.COMPLEX_UNIT_SP, Prefs.getFontSize(context));
        holderVideo.txtViewMedCap_V.setEmojiconSize(Prefs.getEmojiconSize(context));
        holderVideo.rl_Compressing_V = (RelativeLayout)convertView.findViewById(R.id.rl_Compressing_V);
        parentLayouts(holderVideo, convertView);
        msgTimeBar(holderVideo, convertView);
    }

    public static void longtext(Context context, ViewHolder.LongText holderLongText, View convertView) {
        holderLongText.txtViewMessage_LT = (EmojiconTextView) convertView.findViewById(R.id.txtViewMessage_LT);
        holderLongText.txtViewMessage_LT.setTextSize(TypedValue.COMPLEX_UNIT_SP, Prefs.getFontSize(context));
        holderLongText.txtViewMessage_LT.setEmojiconSize(Prefs.getEmojiconSize(context));
        holderLongText.btnViewMore_LT = (Button) convertView.findViewById(R.id.btnViewMore_LT);
        parentLayouts(holderLongText, convertView);
        msgTimeBar(holderLongText, convertView);
    }

    public static void location(ViewHolder.Location holderLocation, View convertView) {
        holderLocation.imgViewMessage_L = (ImageView) convertView.findViewById(R.id.imgViewMessage_L);
        parentLayouts(holderLocation, convertView);
        msgTimeBar(holderLocation, convertView);
    }

    public static void address(Context context, ViewHolder.Address holderAddress, View convertView) {
        holderAddress.imgViewMessage_Ad = (ImageView) convertView.findViewById(R.id.imgViewMessage_Ad);
        holderAddress.txtViewAddName_Ad = (TextView) convertView.findViewById(R.id.txtViewAddName_Ad);
        holderAddress.txtViewAddFull_Ad = (TextView) convertView.findViewById(R.id.txtViewAddFull_Ad);
        holderAddress.txtViewAddName_Ad.setTextSize(TypedValue.COMPLEX_UNIT_SP, Prefs.getFontSize(context));
        holderAddress.txtViewAddFull_Ad.setTextSize(TypedValue.COMPLEX_UNIT_SP, Prefs.getFontSize(context));
        parentLayouts(holderAddress, convertView);
        msgTimeBar(holderAddress, convertView);
    }

    public static void contact(Context context, ViewHolder.Contact holderContact, View convertView) {
        holderContact.txtViewContactName_C = (TextView) convertView.findViewById(R.id.txtViewContactName_C);
        holderContact.txtViewContactName_C.setTextSize(TypedValue.COMPLEX_UNIT_SP, Prefs.getFontSize(context));
        parentLayouts(holderContact, convertView);
        msgTimeBar(holderContact, convertView);
    }

    public static void link(ViewHolder.Link holderLink, View convertView) {
        holderLink.txtViewMessage_L = (EmojiconTextView) convertView.findViewById(R.id.txtViewMessage_L);
        holderLink.txtViewTitle_L = (TextView) convertView.findViewById(R.id.conversation_link_bar_title);
        holderLink.txtViewDesc_L = (TextView) convertView.findViewById(R.id.conversation_link_bar_description);
        holderLink.txtViewUrl_L = (TextView) convertView.findViewById(R.id.conversation_link_bar_url);
        holderLink.imgView_L = (ImageView) convertView.findViewById(R.id.conversation_link_bar_image);
        parentLayouts(holderLink, convertView);
        msgTimeBar(holderLink, convertView);
    }

    private static void parentLayouts(ViewHolder.Parent holderParent, View convertView) {
        holderParent.rlFather = (RelativeLayout) convertView.findViewById(R.id.conversation_bubble_rl_father);
        holderParent.rlMother = (RelativeLayout) convertView.findViewById(R.id.conversation_bubble_rl_mother);
        holderParent.rlParam = (RelativeLayout.LayoutParams) holderParent.rlMother.getLayoutParams();
    }

    private static void msgTimeBar(ViewHolder.Parent holderTimeBar, View convertView) {
        holderTimeBar.llTimeBar = (LinearLayout) convertView.findViewById(R.id.conversation_list_timebar);
        holderTimeBar.imgViewShare = (ImageView) convertView.findViewById(R.id.conversation_list_timebar_share);
        holderTimeBar.txtViewTime = (TextView) convertView.findViewById(R.id.conversation_list_timebar_msg_timestamp);
        holderTimeBar.imgViewStar = (ImageView) convertView.findViewById(R.id.conversation_list_timebar_msg_star);
        holderTimeBar.imgViewMsgStatus = (ImageView) convertView.findViewById(R.id.conversation_list_timebar_msg_status);
    }
}
