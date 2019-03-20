package com.chattynotes.emojicon;

import com.chattynotes.emojicon.emoji.Emojicon;
import android.content.Context;


interface EmojiconRecents {
    void addRecentEmoji(Context context, Emojicon emojicon);
}
