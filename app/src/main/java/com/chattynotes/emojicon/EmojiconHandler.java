package com.chattynotes.emojicon;

import android.content.Context;
import android.text.Spannable;
import android.util.SparseIntArray;
import com.chattynoteslite.R;
import java.util.Arrays;
import com.chattynotes.emojicon.emoji.Activity_4;
import com.chattynotes.emojicon.emoji.Flags_8;
import com.chattynotes.emojicon.emoji.Food_3;
import com.chattynotes.emojicon.emoji.Nature_2;
import com.chattynotes.emojicon.emoji.Objects_6;
import com.chattynotes.emojicon.emoji.People_1;
import com.chattynotes.emojicon.emoji.Symbols_7;
import com.chattynotes.emojicon.emoji.Travel_5;


public final class EmojiconHandler {
    //rockerhieu @link https://github.com/rockerhieu/emojicon/blob/master/library/src/main/java/com/rockerhieu/emojicon/EmojiconHandler.java
    //super-nova @link https://github.com/hani-momanii/SuperNova-Emoji/blob/master/supernova-emoji-library/src/main/java/hani/momanii/supernova_emoji_library/emoji/People.java
    //instachat @link https://github.com/instachat/emoji-library
    //signal @link https://github.com/WhisperSystems/Signal-Android/blob/master/src/org/thoughtcrime/securesms/components/emoji/EmojiPages.java
    //telegram @link https://github.com/DrKLO/Telegram/blob/master/TMessagesProj/src/main/java/org/telegram/messenger/EmojiData.java
    //for github @link https://github.com/vdurmont/emoji-java

    //WhatsApp Emoji arranged in line
    //100% in line
        //@link http://emoji.codes/
        //@link http://getemoji.com/
    //Most of the emoji
        //WhatsApp uses iPhone emoji @link http://emojipedia.org/whatsapp/android/
        //iPhone emoji @link http://emojipedia.org/apple/
        //@link https://www.emojibase.com/emojilist/food          //animal,plant,star,sun

    //@link https://en.wikipedia.org/wiki/Emoji#SoftBank_Unicode_Private_Use_Area_encoding
    //sSoftbanksMap.put(0xe04f, R.drawable.e0809); //cat

    //Grinning Face
    //rockerhieu and super-nova uses CodePoint
    // "\ud83d\ude00"   (used by Signal - it does not uses CodePoint)
    // "😃"              (used by telegram)
    // 1f600            http://www.fileformat.info/info/unicode/char/1f600/index.htm
    // https://www.emojibase.com/emoji/1f600/grinningface

    //Android N new emojee release
    //http://blog.emojipedia.org/android-n-drops-gender-neutral-emojis/

    //STEPS FOR ANDROID APPS
    //1- Go to http://getemoji.com/
    //2- Click on emoji, it will move to @link http://emojipedia.org/automobile/
    //3- Copy Hex from bottom of page
    //4- Search on google "Emojibase 1F697"
    //5- TADA @link https://www.emojibase.com/emoji/1f697/automobile

    //STEPS FOR WORD DOCUMENT
    //1- Drag images from here @link http://emojipedia.org/apple/
    //2- take code from java files

    //Color variation emoji detail
    //http://unicode.org/reports/tr51/
    //http://emojipedia.org/modifiers/

    //Regex for edit: \s\=\s(.*?)\n
    //Regex to replace: ),\n

    //Stack Overflow
    //http://stackoverflow.com/questions/19427375/how-to-show-popup-window-in-another-popup-window-in-android
    //http://stackoverflow.com/questions/37784227/android-floating-menu-like-in-typeface-emoji-keyboard

    //______________________________________________________________________________________________ ISSUES
    //______________________CODE POINTS
    //EYE = 0x1f441;
    //EYE_IN_SPEECH_BUBBLE = {0x1f441, ZERO_WIDTH_JOINER, 0x1f5e8};

    //MAN = 0x1f468;
    //WOMEN = 0x1f469;
    //GIRL = 0x1f467;
    //FAMILY_MAN_WOMEN_GIRL = {0x1F468, ZERO_WIDTH_JOINER, 0x1F469, ZERO_WIDTH_JOINER, 0x1F467 }; //http://emojipedia.org/family-man-woman-girl/

    //______________________STRING
    //EYE = "\uD83D\uDC41"
    //EYE_IN_SPEECH_BUBBLE = "\uD83D\uDC41\u200D\uD83D\uDDE8"

    //______________________________________________________________________________________________
    //LIST OF int[] that are not type face
    /* People_1 secondUnicode is [ZERO_WIDTH_JOINER]

        People_1.COUPLE_WITH_HEART_WOMAN = {0x1F469, ZERO_WIDTH_JOINER, 0x2764, 0xFE0F, ZERO_WIDTH_JOINER, 0x1F469};
        People_1.COUPLE_WITH_HEART_MAN = {0x1F468, ZERO_WIDTH_JOINER, 0x2764, 0xFE0F, ZERO_WIDTH_JOINER, 0x1F468};

        People_1.KISS_WOMAN = {0x1F469, ZERO_WIDTH_JOINER, 0x2764, 0xFE0F, ZERO_WIDTH_JOINER, 0x1F48B, ZERO_WIDTH_JOINER, 0x1F469};
        People_1.KISS_MAN = {0x1F468, ZERO_WIDTH_JOINER, 0x2764, 0xFE0F, ZERO_WIDTH_JOINER, 0x1F48B, ZERO_WIDTH_JOINER, 0x1F468};

        People_1.FAMILY_MAN_WOMEN_GIRL = {0x1F468, ZERO_WIDTH_JOINER, 0x1F469, ZERO_WIDTH_JOINER, 0x1F467 };
        People_1.FAMILY_MAN_WOMEN_GIRL_BOY = {0x1F468, ZERO_WIDTH_JOINER, 0x1F469, ZERO_WIDTH_JOINER, 0x1F467, ZERO_WIDTH_JOINER, 0x1F466};
        People_1.FAMILY_MAN_WOMEN_BOY_BOY = {0x1F468, ZERO_WIDTH_JOINER, 0x1F469, ZERO_WIDTH_JOINER, 0x1F466, ZERO_WIDTH_JOINER, 0x1F466};
        People_1.FAMILY_MAN_WOMEN_GIRL_GIRL = {0x1F468, ZERO_WIDTH_JOINER, 0x1F469, ZERO_WIDTH_JOINER, 0x1F467, ZERO_WIDTH_JOINER, 0x1F467};
        People_1.FAMILY_WOMAN_WOMEN_BOY = {0x1F469, ZERO_WIDTH_JOINER, 0x1F469, ZERO_WIDTH_JOINER, 0x1F466};
        People_1.FAMILY_WOMAN_WOMEN_GIRL = {0x1F469, ZERO_WIDTH_JOINER, 0x1F469, ZERO_WIDTH_JOINER, 0x1F467};
        People_1.FAMILY_WOMAN_WOMEN_GIRL_BOY = {0x1F469, ZERO_WIDTH_JOINER, 0x1F469, ZERO_WIDTH_JOINER, 0x1F467, ZERO_WIDTH_JOINER, 0x1F466};
        People_1.FAMILY_WOMAN_WOMEN_BOY_BOY = {0x1F469, ZERO_WIDTH_JOINER, 0x1F469, ZERO_WIDTH_JOINER, 0x1F466, ZERO_WIDTH_JOINER, 0x1F466};
        People_1.FAMILY_WOMAN_WOMEN_GIRL_GIRL = {0x1F469, ZERO_WIDTH_JOINER, 0x1F469, ZERO_WIDTH_JOINER, 0x1F467, ZERO_WIDTH_JOINER, 0x1F467};
        People_1.FAMILY_MAN_MAN_BOY = {0x1F468, ZERO_WIDTH_JOINER, 0x1F468, ZERO_WIDTH_JOINER, 0x1F466};
        People_1.FAMILY_MAN_MAN_GIRL = {0x1F468, ZERO_WIDTH_JOINER, 0x1F468, ZERO_WIDTH_JOINER, 0x1F467};
        People_1.FAMILY_MAN_MAN_GIRL_BOY = {0x1F468, ZERO_WIDTH_JOINER, 0x1F468, ZERO_WIDTH_JOINER, 0x1F467, ZERO_WIDTH_JOINER, 0x1F466};
        People_1.FAMILY_MAN_MAN_BOY_BOY = {0x1F468, ZERO_WIDTH_JOINER, 0x1F468, ZERO_WIDTH_JOINER, 0x1F466, ZERO_WIDTH_JOINER, 0x1F466};
        People_1.FAMILY_MAN_MAN_GIRL_GIRL = {0x1F468, ZERO_WIDTH_JOINER, 0x1F468, ZERO_WIDTH_JOINER, 0x1F467, ZERO_WIDTH_JOINER, 0x1F467};
     */

     /* Activity_4 secondUnicode is [ZERO_WIDTH_JOINER]
        Activity_4.OLYMPIC_RINGS = {0x25EF, ZERO_WIDTH_JOINER, 0x25EF, ZERO_WIDTH_JOINER, 0x25EF, ZERO_WIDTH_JOINER, 0x25EF, ZERO_WIDTH_JOINER, 0x25EF};
     */

    /* Symbols_7 secondUnicode is [ZERO_WIDTH_JOINER | 0x20e3]

        Symbols_7.KEYCAP_DIGIT_ZERO = {0x0030, 0x20e3};
        Symbols_7.KEYCAP_DIGIT_ONE = {0x0031, 0x20e3};
        Symbols_7.KEYCAP_DIGIT_TWO = {0x0032, 0x20e3};
        Symbols_7.KEYCAP_DIGIT_THREE = {0x0033, 0x20e3};
        Symbols_7.KEYCAP_DIGIT_FOUR = {0x0034, 0x20e3};
        Symbols_7.KEYCAP_DIGIT_FIVE = {0x0035, 0x20e3};
        Symbols_7.KEYCAP_DIGIT_SIX = {0x0036, 0x20e3};
        Symbols_7.KEYCAP_DIGIT_SEVEN = {0x0037, 0x20e3};
        Symbols_7.KEYCAP_DIGIT_EIGHT = {0x0038, 0x20e3};
        Symbols_7.KEYCAP_DIGIT_NINE = {0x0039, 0x20e3};

        Symbols_7.KEYCAP_NUMBER_SIGN = {0x0023, 0x20e3};
        Symbols_7.KEYCAP_ASTERISK = {0x002a, 0x20e3};

        Symbols_7.EYE_IN_SPEECH_BUBBLE = {0x1f441, ZERO_WIDTH_JOINER, 0x1f5e8};
    */

    /* Flags_8 secondUnicode is [A-Z Symbol]
        All
    * */



    private EmojiconHandler() {
    }

    private static final int length =
            People_1.DATA.length + Nature_2.DATA.length + Food_3.DATA.length + Activity_4.DATA.length + Travel_5.DATA.length + Objects_6.DATA.length + Symbols_7.DATA.length + Flags_8.DATA.length;
    private static final SparseIntArray sEmojisMap = new SparseIntArray(length);

    static {
//__________________________________________________________________________________________________ People_1
        //1
        sEmojisMap.put(People_1.GRINNING_FACE, R.drawable.e1358);
        sEmojisMap.put(People_1.GRIMACING_FACE, R.drawable.e1402);
        sEmojisMap.put(People_1.GRIMACING_FACE_WITH_SMILE_EYES, R.drawable.e1359);
        sEmojisMap.put(People_1.FACE_WITH_TEAR_OF_JOY, R.drawable.e1360);
        sEmojisMap.put(People_1.SMILING_FACE_WITH_OPEN_MOUTH, R.drawable.e1361);
        sEmojisMap.put(People_1.SMILING_FACE_WITH_OPEN_MOUTH_EYES, R.drawable.e1362);
        sEmojisMap.put(People_1.SMILING_FACE_WITH_OPEN_MOUTH_COLD_SWEAT, R.drawable.e1363);
        sEmojisMap.put(People_1.SMILING_FACE_WITH_OPEN_MOUTH_HAND_TIGHT, R.drawable.e1364);
        //2
        sEmojisMap.put(People_1.SMILING_FACE_WITH_HALO, R.drawable.e1365);
        sEmojisMap.put(People_1.WINKING_FACE, R.drawable.e1367);
        sEmojisMap.put(People_1.BLACK_SMILING_FACE, R.drawable.e1368);
        sEmojisMap.put(People_1.SLIGHTLY_SMILING_FACE, R.drawable.e1424);
        sEmojisMap.put(People_1.UPSIDE_DOWN_FACE, R.drawable.e1425);
        sEmojisMap.put(People_1.WHITE_SMILING_FACE, R.drawable.e0077);
        sEmojisMap.put(People_1.FACE_SAVOURING_DELICIOUS_FOOD, R.drawable.e1369);
        sEmojisMap.put(People_1.RELIEVED_FACE, R.drawable.e1370);
        //3
        sEmojisMap.put(People_1.SMILING_FACE_HEART_EYES, R.drawable.e1371);
        sEmojisMap.put(People_1.FACE_THROWING_KISS, R.drawable.e1382);
        sEmojisMap.put(People_1.KISSING_FACE, R.drawable.e1381);
        sEmojisMap.put(People_1.KISSING_FACE_WITH_SMILE_EYES, R.drawable.e1383);
        sEmojisMap.put(People_1.KISSING_FACE_WITH_CLOSED_EYES, R.drawable.e1384);
        sEmojisMap.put(People_1.FACE_WITH_TONGUE_WINK_EYE, R.drawable.e1386);
        sEmojisMap.put(People_1.FACE_WITH_TONGUE_CLOSED_EYE, R.drawable.e1387);
        sEmojisMap.put(People_1.FACE_WITH_STUCK_OUT_TONGUE, R.drawable.e1385);
        //4
        sEmojisMap.put(People_1.MONEY_MOUTH_FACE, R.drawable.e1591);
        sEmojisMap.put(People_1.NERD_FACE, R.drawable.e1593);
        sEmojisMap.put(People_1.SMILING_FACE_WITH_SUN_GLASS, R.drawable.e1372);
        sEmojisMap.put(People_1.HUGGING_FACE, R.drawable.e1597);
        sEmojisMap.put(People_1.SMIRKING_FACE, R.drawable.e1373);
        sEmojisMap.put(People_1.FACE_WITHOUT_MOUTH, R.drawable.e1412);
        sEmojisMap.put(People_1.NEUTRAL_FACE, R.drawable.e1374);
        sEmojisMap.put(People_1.EXPRESSIONLESS_FACE, R.drawable.e1375);
        //5
        sEmojisMap.put(People_1.UNAMUSED_FACE, R.drawable.e1376);
        sEmojisMap.put(People_1.FACE_WITH_ROLLING_EYES, R.drawable.e1426);
        sEmojisMap.put(People_1.THINKING_FACE, R.drawable.e1594);
        sEmojisMap.put(People_1.FLUSHED_FACE, R.drawable.e1409);
        sEmojisMap.put(People_1.DISAPPOINTED_FACE, R.drawable.e1388);
        sEmojisMap.put(People_1.WORRIED_FACE, R.drawable.e1389);
        sEmojisMap.put(People_1.ANGRY_FACE, R.drawable.e1390);
        sEmojisMap.put(People_1.POUTING_FACE, R.drawable.e1391);
        //6
        sEmojisMap.put(People_1.PENSIVE_FACE, R.drawable.e1378);
        sEmojisMap.put(People_1.CONFUSED_FACE, R.drawable.e1379);
        sEmojisMap.put(People_1.SLIGHTLY_FROWNING_FACE, R.drawable.e1423);
        sEmojisMap.put(People_1.WHITE_FROWNING_FACE, R.drawable.e0076);
        sEmojisMap.put(People_1.PERSEVERING_FACE, R.drawable.e1393);
        sEmojisMap.put(People_1.CONFOUNDED_FACE, R.drawable.e1380);
        sEmojisMap.put(People_1.TIRED_FACE, R.drawable.e1401);
        sEmojisMap.put(People_1.WEARY_FACE, R.drawable.e1399);
        //7
        sEmojisMap.put(People_1.FACE_WITH_LOOK_OF_TRIUMPH, R.drawable.e1394);
        sEmojisMap.put(People_1.FACE_WITH_OPEN_MOUTH, R.drawable.e1404);
        sEmojisMap.put(People_1.FACE_SCREAMING_IN_FEAR, R.drawable.e1407);
        sEmojisMap.put(People_1.FEARFUL_FACE, R.drawable.e1398);
        sEmojisMap.put(People_1.FACE_WITH_OPEN_MOUTH_COLD_SWEAT, R.drawable.e1406);
        sEmojisMap.put(People_1.HUSHED_FACE, R.drawable.e1405);
        sEmojisMap.put(People_1.FROWNING_FACE_WITH_OPEN_MOUTH, R.drawable.e1396);
        sEmojisMap.put(People_1.ANGUISHED_FACE, R.drawable.e1397);
        //8
        sEmojisMap.put(People_1.CRYING_FACE, R.drawable.e1392);
        sEmojisMap.put(People_1.DISAPPOINTED_BUT_RELIEVED_FACE, R.drawable.e1395);
        sEmojisMap.put(People_1.SLEEPY_FACE, R.drawable.e1400);
        sEmojisMap.put(People_1.FACE_WITH_COLD_SWEAT, R.drawable.e1377);
        sEmojisMap.put(People_1.LOUDLY_CRYING_FACE, R.drawable.e1403);
        sEmojisMap.put(People_1.DIZZY_FACE, R.drawable.e1411);
        sEmojisMap.put(People_1.ASTONISHED_FACE, R.drawable.e1408);
        sEmojisMap.put(People_1.ZIPPER_MOUTH_FACE, R.drawable.e1590);
        //9
        sEmojisMap.put(People_1.FACE_WITH_MEDICAL_MASK, R.drawable.e1413);
        sEmojisMap.put(People_1.FACE_WITH_THERMOMETER, R.drawable.e1592);
        sEmojisMap.put(People_1.FACE_WITH_HEAD_BANDAGE, R.drawable.e1595);
        sEmojisMap.put(People_1.SLEEPING_FACE, R.drawable.e1410);
        sEmojisMap.put(People_1.SLEEPING_SYMBOL, R.drawable.e1113);
        sEmojisMap.put(People_1.PILE_OF_POO, R.drawable.e1118);
        sEmojisMap.put(People_1.SMILING_FACE_WITH_HORNS, R.drawable.e1366);
        sEmojisMap.put(People_1.IMP, R.drawable.e1042);
        //10
        sEmojisMap.put(People_1.JAPANESE_OGRE, R.drawable.e1031);
        sEmojisMap.put(People_1.JAPANESE_GOBLIN, R.drawable.e1032);
        sEmojisMap.put(People_1.SKULL, R.drawable.e1043);
        sEmojisMap.put(People_1.GHOST, R.drawable.e1033);
        sEmojisMap.put(People_1.EXTRA_TERRESTRIAL_ALIEN, R.drawable.e1040);
        sEmojisMap.put(People_1.ROBOT_FACE, R.drawable.e1596);
        sEmojisMap.put(People_1.SMILING_CAT_FACE_OPEN_MOUTH, R.drawable.e1416);
        sEmojisMap.put(People_1.GRINNING_CAT_FACE_SMILE_EYES, R.drawable.e1414);
        //11
        sEmojisMap.put(People_1.CAT_FACE_TEARS_OF_JOY, R.drawable.e1415);
        sEmojisMap.put(People_1.SMILING_CAT_FACE_HEART_SHAPED_EYES, R.drawable.e1417);
        sEmojisMap.put(People_1.CAT_FACE_WRY_SMILE, R.drawable.e1418);
        sEmojisMap.put(People_1.KISSING_CAT_FACE_CLOSED_EYES, R.drawable.e1419);
        sEmojisMap.put(People_1.WEARY_CAT_FACE, R.drawable.e1422);
        sEmojisMap.put(People_1.CRYING_CAT_FACE, R.drawable.e1421);
        sEmojisMap.put(People_1.POUTING_CAT_FACE, R.drawable.e1420);
        sEmojisMap.put(People_1.PERSON_BOTH_HAND_CELEBRATION, R.drawable.e1454);
        //12
        sEmojisMap.put(People_1.CLAPPING_HAND, R.drawable.e0895);
        sEmojisMap.put(People_1.WAVING_HANDS, R.drawable.e0871);
        sEmojisMap.put(People_1.THUMBS_UP, R.drawable.e0883);
        sEmojisMap.put(People_1.THUMBS_DOWN, R.drawable.e0889);
        sEmojisMap.put(People_1.FIST_HAND, R.drawable.e0865);
        sEmojisMap.put(People_1.RAISED_FIST, R.drawable.e0143);
        sEmojisMap.put(People_1.VICTORY_HAND, R.drawable.e0155);
        sEmojisMap.put(People_1.OK_HAND, R.drawable.e0877);
        //13
        sEmojisMap.put(People_1.RAISED_HAND, R.drawable.e0149);
        sEmojisMap.put(People_1.OPEN_HAND, R.drawable.e0901);
        sEmojisMap.put(People_1.FLEXED_BICEPS, R.drawable.e1119);
        sEmojisMap.put(People_1.FOLDED_HANDS, R.drawable.e1472);
        sEmojisMap.put(People_1.UP_POINTING_INDEX, R.drawable.e0062);
        sEmojisMap.put(People_1.UP_POINTING_BACKHAND_INDEX, R.drawable.e0841);
        sEmojisMap.put(People_1.DOWN_POINTING_BACKHAND_INDEX, R.drawable.e0847);
        sEmojisMap.put(People_1.LEFT_POINTING_BACKHAND_INDEX, R.drawable.e0853);
        //14
        sEmojisMap.put(People_1.RIGHT_POINTING_BACKHAND_INDEX, R.drawable.e0859);
        sEmojisMap.put(People_1.REVERSE_MIDDLE_FINGER, R.drawable.e1321);
        sEmojisMap.put(People_1.RAISED_HAND_FINGERS_SPLAYED, R.drawable.e1315);
        sEmojisMap.put(People_1.SIGN_OF_HORN, R.drawable.e1598);
        sEmojisMap.put(People_1.RAISED_HAND_PART_BETWEEN_MIDDLE_RING, R.drawable.e1327);
        sEmojisMap.put(People_1.WRITING_HAND, R.drawable.e0161);
        sEmojisMap.put(People_1.NAIL_POLISH, R.drawable.e1063);
        sEmojisMap.put(People_1.MOUTH, R.drawable.e0839);
        //15
        sEmojisMap.put(People_1.TONGUE, R.drawable.e0840);
        sEmojisMap.put(People_1.EAR, R.drawable.e0827);
        sEmojisMap.put(People_1.NOSE, R.drawable.e0833);
        sEmojisMap.put(People_1.EYE, R.drawable.e0825);
        sEmojisMap.put(People_1.EYES, R.drawable.e0824);
        sEmojisMap.put(People_1.BUST_IN_SILHOUETTE, R.drawable.e0926);
        sEmojisMap.put(People_1.BUSTS_IN_SILHOUETTE, R.drawable.e0927);
        sEmojisMap.put(People_1.SPEAKING_HEAD_IN_SILHOUETTE, R.drawable.e1348);
        //16
        sEmojisMap.put(People_1.BABY, R.drawable.e1013);
        sEmojisMap.put(People_1.BOY, R.drawable.e0928);
        sEmojisMap.put(People_1.GIRL, R.drawable.e0934);
        sEmojisMap.put(People_1.MAN, R.drawable.e0940);
        sEmojisMap.put(People_1.WOMEN, R.drawable.e0946);
        sEmojisMap.put(People_1.PERSON_WITH_BLOND_HAIR, R.drawable.e0983);
        sEmojisMap.put(People_1.OLDER_MAN, R.drawable.e1001);
        sEmojisMap.put(People_1.OLDER_WOMEN, R.drawable.e1007);
        //17
        sEmojisMap.put(People_1.MAN_WITH_GUA_PI_MAO, R.drawable.e0989);
        sEmojisMap.put(People_1.MAN_WITH_TURBAN, R.drawable.e0995);
        sEmojisMap.put(People_1.POLICE_OFFICER, R.drawable.e0970);
        sEmojisMap.put(People_1.CONSTRUCTION_WORKER, R.drawable.e1019);
        sEmojisMap.put(People_1.GUARDS_MAN, R.drawable.e1050);
        sEmojisMap.put(People_1.SPY, R.drawable.e1305);
        sEmojisMap.put(People_1.FATHER_CHRISTMAS, R.drawable.e0615);
        sEmojisMap.put(People_1.BABY_ANGEL, R.drawable.e1034);
        //18
        sEmojisMap.put(People_1.PRINCESS, R.drawable.e1025);
        sEmojisMap.put(People_1.BRIDE_WITH_VEIL, R.drawable.e0977);
        sEmojisMap.put(People_1.PEDESTRIAN, R.drawable.e1547);
        sEmojisMap.put(People_1.RUNNER, R.drawable.e0677);
        sEmojisMap.put(People_1.DANCER, R.drawable.e1056);
        sEmojisMap.put(People_1.WOMEN_WITH_BUNNY_YEARS, R.drawable.e0976);
        sEmojisMap.put(People_1.MAN_WOMEN_HOLDING_HANDS, R.drawable.e0967);
        sEmojisMap.put(People_1.TWO_MAN_HOLDING_HANDS, R.drawable.e0968);
        //19
        sEmojisMap.put(People_1.TWO_WOMEN_HOLDING_HANDS, R.drawable.e0969);
        sEmojisMap.put(People_1.PERSON_BOWING_DEEPLY, R.drawable.e1439);
        sEmojisMap.put(People_1.INFORMATION_DESK_PERSON, R.drawable.e1044);
        sEmojisMap.put(People_1.FACE_WITH_NO_GOOD_GESTURE, R.drawable.e1427);
        sEmojisMap.put(People_1.FACE_WITH_OK_GESTURE, R.drawable.e1433);
        sEmojisMap.put(People_1.HAPPY_PERSON_RAISE_ONE_HAND, R.drawable.e1448);
        sEmojisMap.put(People_1.PERSON_WITH_POUTING_FACE, R.drawable.e1466);
        sEmojisMap.put(People_1.PERSON_FROWNING, R.drawable.e1460);
        //20
        sEmojisMap.put(People_1.HAIRCUT, R.drawable.e1075);
        sEmojisMap.put(People_1.FACE_MASSAGE, R.drawable.e1069);
        sEmojisMap.put(People_1.COUPLE_WITH_HEART, R.drawable.e1092);
        sEmojisMap.put(People_1.KISS, R.drawable.e1088);
        //21
        sEmojisMap.put(People_1.FAMILY, R.drawable.e0952);
        //22
        sEmojisMap.put(People_1.WOMAN_CLOTHES, R.drawable.e0916);
        //23
        sEmojisMap.put(People_1.T_SHIRT, R.drawable.e0911);
        sEmojisMap.put(People_1.JEANS, R.drawable.e0912);
        sEmojisMap.put(People_1.NECKTIE, R.drawable.e0910);
        sEmojisMap.put(People_1.DRESS, R.drawable.e0913);
        sEmojisMap.put(People_1.BIKINI, R.drawable.e0915);
        sEmojisMap.put(People_1.KIMONO, R.drawable.e0914);
        sEmojisMap.put(People_1.LIPSTICK, R.drawable.e1062);
        sEmojisMap.put(People_1.KISS_MARK, R.drawable.e1084);
        //24
        sEmojisMap.put(People_1.FOOTPRINTS, R.drawable.e0925);
        sEmojisMap.put(People_1.HIGH_HEELED_SHOE, R.drawable.e0922);
        sEmojisMap.put(People_1.WOMAN_SANDAL, R.drawable.e0923);
        sEmojisMap.put(People_1.WOMAN_BOOTS, R.drawable.e0924);
        sEmojisMap.put(People_1.MAN_SHOE, R.drawable.e0920);
        sEmojisMap.put(People_1.ATHLETIC_SHOE, R.drawable.e0921);
        sEmojisMap.put(People_1.WOMAN_HAT, R.drawable.e0908);
        sEmojisMap.put(People_1.TOP_HAT, R.drawable.e0651);
        //25
        sEmojisMap.put(People_1.GRADUATION_CAP, R.drawable.e0634);
        sEmojisMap.put(People_1.CROWN, R.drawable.e0907);
        sEmojisMap.put(People_1.HELMET_WITH_WHITE_CROSS, R.drawable.e0118);
        sEmojisMap.put(People_1.SCHOOL_SATCHEL, R.drawable.e0633);
        sEmojisMap.put(People_1.POUCH, R.drawable.e0919);
        sEmojisMap.put(People_1.PURSE, R.drawable.e0917);
        sEmojisMap.put(People_1.HANDBAG, R.drawable.e0918);
        sEmojisMap.put(People_1.BRIEFCASE, R.drawable.e1142);
        //26
        sEmojisMap.put(People_1.EYE_GLASSES, R.drawable.e0909);
        sEmojisMap.put(People_1.DARK_SUN_GLASSES, R.drawable.e1306);
        sEmojisMap.put(People_1.RING, R.drawable.e1086);
        sEmojisMap.put(People_1.CLOSED_UMBRELLA, R.drawable.e0486);

//__________________________________________________________________________________________________ Nature_2
        //1
        sEmojisMap.put(Nature_2.DOG_FACE, R.drawable.e0814);
        sEmojisMap.put(Nature_2.CAT_FACE, R.drawable.e0809);
//        sSoftbanksMap.put(0xe04f, R.drawable.e0809);
        sEmojisMap.put(Nature_2.MOUSE_FACE, R.drawable.e0805);
//        sSoftbanksMap.put(0xe053, R.drawable.e0805);
        sEmojisMap.put(Nature_2.HAMSTER_FACE, R.drawable.e0817);
//        sSoftbanksMap.put(0xe524, R.drawable.e0817);
        sEmojisMap.put(Nature_2.RABBIT_FACE, R.drawable.e0808);
//        sSoftbanksMap.put(0xe52c, R.drawable.e0808);
        sEmojisMap.put(Nature_2.BEAR_FACE, R.drawable.e0819);
//        sSoftbanksMap.put(0xe051, R.drawable.e0819);
        sEmojisMap.put(Nature_2.PANDA_FACE, R.drawable.e0820);
        sEmojisMap.put(Nature_2.KOALA_FACE, R.drawable.e0800);
//        sSoftbanksMap.put(0xe527, R.drawable.e0800);
        //2
        sEmojisMap.put(Nature_2.TIGER_FACE, R.drawable.e0807);
//        sSoftbanksMap.put(0xe050, R.drawable.e0807);
        sEmojisMap.put(Nature_2.LION_FACE, R.drawable.e1605);
        sEmojisMap.put(Nature_2.COW_FACE, R.drawable.e0806);
//        sSoftbanksMap.put(0xe52b, R.drawable.e0806);
        sEmojisMap.put(Nature_2.PIG_FACE, R.drawable.e0815);
//        sSoftbanksMap.put(0xe10b, R.drawable.e0815);
        sEmojisMap.put(Nature_2.PIG_NOSE, R.drawable.e0821);
        sEmojisMap.put(Nature_2.FROG_FACE, R.drawable.e0816);
//        sSoftbanksMap.put(0xe531, R.drawable.e0816);
        sEmojisMap.put(Nature_2.OCTOPUS, R.drawable.e0785);
//        sSoftbanksMap.put(0xe10a, R.drawable.e0785);
        sEmojisMap.put(Nature_2.MONKEY_FACE, R.drawable.e0813);
//        sSoftbanksMap.put(0xe109, R.drawable.e0813);
        //3
        sEmojisMap.put(Nature_2.SEE_NO_EVIL_MONKEY, R.drawable.e1445);
        sEmojisMap.put(Nature_2.HEAR_NO_EVIL_MONKEY, R.drawable.e1446);
        sEmojisMap.put(Nature_2.SPEAK_NO_EVIL_MONKEY, R.drawable.e1447);
        sEmojisMap.put(Nature_2.MONKEY, R.drawable.e0778);
//        sSoftbanksMap.put(0xe528, R.drawable.e0778);
        sEmojisMap.put(Nature_2.CHICKEN, R.drawable.e0780);
//        sSoftbanksMap.put(0xe52e, R.drawable.e0780);
        sEmojisMap.put(Nature_2.PENGUIN, R.drawable.e0799);
//        sSoftbanksMap.put(0xe055, R.drawable.e0799);
        sEmojisMap.put(Nature_2.BIRD, R.drawable.e0798);
//        sSoftbanksMap.put(0xe521, R.drawable.e0798);
        sEmojisMap.put(Nature_2.BABY_CHICK, R.drawable.e0796);
        //4
        sEmojisMap.put(Nature_2.HATCHING_CHICK, R.drawable.e0795);
//        sSoftbanksMap.put(0xe523, R.drawable.e0795);
        sEmojisMap.put(Nature_2.FRONT_FACE_CHICK, R.drawable.e0797);
        sEmojisMap.put(Nature_2.WOLF_FACE, R.drawable.e0818);
//        sSoftbanksMap.put(0xe52a, R.drawable.e0818);
        sEmojisMap.put(Nature_2.BOAR, R.drawable.e0783);
//        sSoftbanksMap.put(0xe52f, R.drawable.e0783);
        sEmojisMap.put(Nature_2.HORSE_FACE, R.drawable.e0812);
//        sSoftbanksMap.put(0xe01a, R.drawable.e0812);
        sEmojisMap.put(Nature_2.UNICORN_FACE, R.drawable.e1608);
        sEmojisMap.put(Nature_2.HONEY_BEE, R.drawable.e0789);
        sEmojisMap.put(Nature_2.BUG, R.drawable.e0787);
//        sSoftbanksMap.put(0xe525, R.drawable.e0787);
        //5
        sEmojisMap.put(Nature_2.SNAIL, R.drawable.e0772);
        sEmojisMap.put(Nature_2.LADY_BEETLE, R.drawable.e0790);
        sEmojisMap.put(Nature_2.ANT, R.drawable.e0788);
        sEmojisMap.put(Nature_2.SPIDER, R.drawable.e1307);
        sEmojisMap.put(Nature_2.SCORPION, R.drawable.e1606);
        sEmojisMap.put(Nature_2.CRAB, R.drawable.e1604);
        sEmojisMap.put(Nature_2.SNAKE, R.drawable.e0773);
//        sSoftbanksMap.put(0xe52d, R.drawable.e0773);
        sEmojisMap.put(Nature_2.TURTLE, R.drawable.e0794);
        //6
        sEmojisMap.put(Nature_2.TROPICAL_FISH, R.drawable.e0792);
//        sSoftbanksMap.put(0xe522, R.drawable.e0792);
        sEmojisMap.put(Nature_2.FISH, R.drawable.e0791);
        sEmojisMap.put(Nature_2.BLOW_FISH, R.drawable.e0793);
        sEmojisMap.put(Nature_2.DOLPHIN, R.drawable.e0804);
//        sSoftbanksMap.put(0xe520, R.drawable.e0804);
        sEmojisMap.put(Nature_2.SPOUTING_WHALE, R.drawable.e0811);
//        sSoftbanksMap.put(0xe054, R.drawable.e0811);
        sEmojisMap.put(Nature_2.WHALE, R.drawable.e0771);
        sEmojisMap.put(Nature_2.CROCODILE, R.drawable.e0770);
        sEmojisMap.put(Nature_2.LEOPARD, R.drawable.e0766);
        //7
        sEmojisMap.put(Nature_2.TIGER, R.drawable.e0765);
        sEmojisMap.put(Nature_2.WATER_BUFFALO, R.drawable.e0763);
        sEmojisMap.put(Nature_2.OX, R.drawable.e0762);
        sEmojisMap.put(Nature_2.COW, R.drawable.e0764);
        sEmojisMap.put(Nature_2.DROMEDARY_CAMEL, R.drawable.e0802);
        sEmojisMap.put(Nature_2.BACTRIAN_CAMEL, R.drawable.e0803);
//        sSoftbanksMap.put(0xe530, R.drawable.e0803);
        sEmojisMap.put(Nature_2.ELEPHANT, R.drawable.e0784);
//        sSoftbanksMap.put(0xe526, R.drawable.e0784);
        sEmojisMap.put(Nature_2.GOAT, R.drawable.e0776);
        //8
        sEmojisMap.put(Nature_2.RAM, R.drawable.e0775);
        sEmojisMap.put(Nature_2.SHEEP, R.drawable.e0777);
//        sSoftbanksMap.put(0xe529, R.drawable.e0777);
        sEmojisMap.put(Nature_2.HORSE, R.drawable.e0774);
//        sSoftbanksMap.put(0xe134, R.drawable.e0774);
        sEmojisMap.put(Nature_2.PIG, R.drawable.e0782);
        sEmojisMap.put(Nature_2.RAT, R.drawable.e0760);
        sEmojisMap.put(Nature_2.MOUSE, R.drawable.e0761);
        sEmojisMap.put(Nature_2.ROOSTER, R.drawable.e0779);
        sEmojisMap.put(Nature_2.TURKEY, R.drawable.e1607);
        //9
        sEmojisMap.put(Nature_2.DOVE, R.drawable.e1272);
        sEmojisMap.put(Nature_2.DOG, R.drawable.e0781);
        sEmojisMap.put(Nature_2.POODLE, R.drawable.e0801);
//        sSoftbanksMap.put(0xe052, R.drawable.e0801);
        sEmojisMap.put(Nature_2.CAT, R.drawable.e0768);
        sEmojisMap.put(Nature_2.RABBIT, R.drawable.e0767);
        sEmojisMap.put(Nature_2.CHIPMUNK, R.drawable.e0823);
        sEmojisMap.put(Nature_2.PAW_PRINTS, R.drawable.e0822);
//        sSoftbanksMap.put(0xe536, R.drawable.e0822);
        sEmojisMap.put(Nature_2.DRAGON, R.drawable.e0769);
        //10
        sEmojisMap.put(Nature_2.DRAGON_FACE, R.drawable.e0810);
        sEmojisMap.put(Nature_2.CACTUS, R.drawable.e0535);
//        sSoftbanksMap.put(0xe308, R.drawable.e0535);
        sEmojisMap.put(Nature_2.CHRISTMAS_TREE, R.drawable.e0614);
//        sSoftbanksMap.put(0xe033, R.drawable.e0614);
        sEmojisMap.put(Nature_2.EVER_GREEN_TREE, R.drawable.e0532);
        sEmojisMap.put(Nature_2.DECIDUOUS_TREE, R.drawable.e0533);
        sEmojisMap.put(Nature_2.PALM_TREE, R.drawable.e0534);
//        sSoftbanksMap.put(0xe307, R.drawable.e0534);
        sEmojisMap.put(Nature_2.SEEDLING, R.drawable.e0531);
//        sSoftbanksMap.put(0xe110, R.drawable.e0531);
        sEmojisMap.put(Nature_2.HERB, R.drawable.e0545);
        //11
        sEmojisMap.put(Nature_2.SHAMROCK , R.drawable.e0061);
        sEmojisMap.put(Nature_2.FOUR_LEAF, R.drawable.e0546);
        sEmojisMap.put(Nature_2.PINE_DECORATION, R.drawable.e0628);
        sEmojisMap.put(Nature_2.TANABATA_TREE, R.drawable.e0626);
        sEmojisMap.put(Nature_2.LEAF_WIND, R.drawable.e0549);
//        sSoftbanksMap.put(0xe447, R.drawable.e0549);
        sEmojisMap.put(Nature_2.FALLEN_LEAF, R.drawable.e0548);
//        sSoftbanksMap.put(0xe119, R.drawable.e0548);
        sEmojisMap.put(Nature_2.MAPLE_LEAF, R.drawable.e0547);
//        sSoftbanksMap.put(0xe118, R.drawable.e0547);
        sEmojisMap.put(Nature_2.EAR_OF_RICE, R.drawable.e0544);
//        sSoftbanksMap.put(0xe444, R.drawable.e0544);
        //12
        sEmojisMap.put(Nature_2.HIBISCUS, R.drawable.e0540);
//        sSoftbanksMap.put(0xe303, R.drawable.e0540);
        sEmojisMap.put(Nature_2.SUNFLOWER, R.drawable.e0541);
//        sSoftbanksMap.put(0xe305, R.drawable.e0541);
        sEmojisMap.put(Nature_2.ROSE, R.drawable.e0539);
//        sSoftbanksMap.put(0xe032, R.drawable.e0539);
        sEmojisMap.put(Nature_2.TULIP, R.drawable.e0537);
//        sSoftbanksMap.put(0xe304, R.drawable.e0537);
        sEmojisMap.put(Nature_2.BLOSSOM, R.drawable.e0542);
        sEmojisMap.put(Nature_2.CHERRY_BLOSSOM, R.drawable.e0538);
//        sSoftbanksMap.put(0xe030, R.drawable.e0538);
        sEmojisMap.put(Nature_2.BOUQUET, R.drawable.e1091);
//        sSoftbanksMap.put(0xe306, R.drawable.e1091);
        sEmojisMap.put(Nature_2.MUSHROOM, R.drawable.e0550);
        //13
        sEmojisMap.put(Nature_2.CHESTNUT, R.drawable.e0530);
        sEmojisMap.put(Nature_2.JACK_O_LANTERN, R.drawable.e0613);
        sEmojisMap.put(Nature_2.SPIRAL_SHELL, R.drawable.e0786);
//        sSoftbanksMap.put(0xe441, R.drawable.e0786);
        sEmojisMap.put(Nature_2.SPIDER_WEB, R.drawable.e1308);
        sEmojisMap.put(Nature_2.EARTH_AMERICA, R.drawable.e0497);
        sEmojisMap.put(Nature_2.EARTH_EUROPE, R.drawable.e0498);
        sEmojisMap.put(Nature_2.EARTH_AUSTRALIA, R.drawable.e0499);
        sEmojisMap.put(Nature_2.FULL_MOON, R.drawable.e0505);
        //14
        sEmojisMap.put(Nature_2.WANING_GIBBOUS_MOON, R.drawable.e0506);
        sEmojisMap.put(Nature_2.LAST_QUARTER_MOON, R.drawable.e0507);
        sEmojisMap.put(Nature_2.WANING_CRESCENT_MOON, R.drawable.e0508);
        sEmojisMap.put(Nature_2.NEW_MOON_SYMBOL, R.drawable.e0501);
        sEmojisMap.put(Nature_2.WAXING_CRESCENT_MOON, R.drawable.e0502);
        sEmojisMap.put(Nature_2.FIRST_QUARTER_MOON, R.drawable.e0503);
//        sSoftbanksMap.put(0xe04c, R.drawable.e0503);
        sEmojisMap.put(Nature_2.WAXING_GIBBOUS_MOON, R.drawable.e0504);
        sEmojisMap.put(Nature_2.NEW_MOON_WITH_FACE, R.drawable.e0510);
        //15
        sEmojisMap.put(Nature_2.FULL_MOON_FACE, R.drawable.e0513);
        sEmojisMap.put(Nature_2.FIRST_QUARTER_MOON_FACE, R.drawable.e0511);
        sEmojisMap.put(Nature_2.LAST_QUARTER_MOON_FACE, R.drawable.e0512);
        sEmojisMap.put(Nature_2.SUN_FACE, R.drawable.e0514);
        sEmojisMap.put(Nature_2.CRESCENT_MOON, R.drawable.e0509);
        sEmojisMap.put(Nature_2.WHITE_STAR, R.drawable.e0199);
//        sSoftbanksMap.put(0xe32f, R.drawable.e0199);
        sEmojisMap.put(Nature_2.GLOWING_STAR, R.drawable.e0515);
//        sSoftbanksMap.put(0xe335, R.drawable.e0515);
        sEmojisMap.put(Nature_2.DIZZY_SYMBOL, R.drawable.e1125);
        //16
        sEmojisMap.put(Nature_2.SPARKLES , R.drawable.e0173);
//        sSoftbanksMap.put(0xe32e, R.drawable.e0173);
        sEmojisMap.put(Nature_2.COMET , R.drawable.e0056);
        sEmojisMap.put(Nature_2.BLACK_SUN_WITH_RAYS, R.drawable.e0052);
//        sSoftbanksMap.put(0xe04a, R.drawable.e0052);
        sEmojisMap.put(Nature_2.WHITE_SUN_SMALL_CLOUD, R.drawable.e0518);
        sEmojisMap.put(Nature_2.SUN_BEHIND_CLOUD, R.drawable.e0114);
        sEmojisMap.put(Nature_2.WHITE_SUN_BEHIND_CLOUD, R.drawable.e0519);
        sEmojisMap.put(Nature_2.WHITE_SUN_BEHIND_CLOUD_RAIN, R.drawable.e0520);
        sEmojisMap.put(Nature_2.CLOUD, R.drawable.e0053);
//        sSoftbanksMap.put(0xe049, R.drawable.e0053);
        //17
        sEmojisMap.put(Nature_2.CLOUD_WITH_RAIN, R.drawable.e0521);
        sEmojisMap.put(Nature_2.THUNDER_CLOUD_RAIN, R.drawable.e0115);
        sEmojisMap.put(Nature_2.CLOUD_LIGHTENING, R.drawable.e0523);
        sEmojisMap.put(Nature_2.HIGH_VOLTAGE, R.drawable.e0106); //thunder
//        sSoftbanksMap.put(0xe13d, R.drawable.e0106);
        sEmojisMap.put(Nature_2.FIRE, R.drawable.e1246);
//        sSoftbanksMap.put(0xe11d, R.drawable.e1246);
        sEmojisMap.put(Nature_2.COLLISION, R.drawable.e1114);
        sEmojisMap.put(Nature_2.SNOW_FLAKE, R.drawable.e0176);
        sEmojisMap.put(Nature_2.CLOUD_WITH_SNOW, R.drawable.e0522);
        //18
        sEmojisMap.put(Nature_2.SNOWMAN , R.drawable.e0055);
        sEmojisMap.put(Nature_2.SNOWMAN_WITHOUT_SNOW, R.drawable.e0113);
//        sSoftbanksMap.put(0xe048, R.drawable.e0113);
        sEmojisMap.put(Nature_2.WIND_BLOWING_FACE, R.drawable.e0526);
        sEmojisMap.put(Nature_2.DASH_SYMBOL, R.drawable.e1117);
//        sSoftbanksMap.put(0xe330, R.drawable.e1117);
        sEmojisMap.put(Nature_2.CLOUD_WITH_TORNADO, R.drawable.e0524);
        sEmojisMap.put(Nature_2.FOG, R.drawable.e0525);
        sEmojisMap.put(Nature_2.UMBRELLA, R.drawable.e0054);
        sEmojisMap.put(Nature_2.UMBRELLA_WITH_RAIN_DROPS , R.drawable.e0059);
//        sSoftbanksMap.put(0xe04b, R.drawable.e0059);
        //19
        sEmojisMap.put(Nature_2.DROPLET, R.drawable.e1116);
        sEmojisMap.put(Nature_2.SPLASHING_SWEAT, R.drawable.e1115);
//        sSoftbanksMap.put(0xe331, R.drawable.e1115);
        sEmojisMap.put(Nature_2.WATER_WAVE, R.drawable.e0494);
//        sSoftbanksMap.put(0xe43e, R.drawable.e0494);

//__________________________________________________________________________________________________ Food_3
        //1
        sEmojisMap.put(Food_3.GREEN_APPLE, R.drawable.e0561);
        sEmojisMap.put(Food_3.RED_APPLE, R.drawable.e0560);
//        sSoftbanksMap.put(0xe345, R.drawable.e0560);
        sEmojisMap.put(Food_3.PEAR, R.drawable.e0562);
        sEmojisMap.put(Food_3.TANGERINE, R.drawable.e0556);
//        sSoftbanksMap.put(0xe346, R.drawable.e0556);
        sEmojisMap.put(Food_3.LEMON, R.drawable.e0557);
        sEmojisMap.put(Food_3.BANANA, R.drawable.e0558);
        sEmojisMap.put(Food_3.WATERMELON, R.drawable.e0555);
//        sSoftbanksMap.put(0xe348, R.drawable.e0555);
        sEmojisMap.put(Food_3.GRAPES, R.drawable.e0553);
         //2
        sEmojisMap.put(Food_3.STRAWBERRY, R.drawable.e0565);
//        sSoftbanksMap.put(0xe347, R.drawable.e0565);
        sEmojisMap.put(Food_3.MELON, R.drawable.e0554);
        sEmojisMap.put(Food_3.CHERRY, R.drawable.e0564);
        sEmojisMap.put(Food_3.PEACH, R.drawable.e0563);
        sEmojisMap.put(Food_3.PINEAPPLE, R.drawable.e0559);
        sEmojisMap.put(Food_3.TOMATO, R.drawable.e0551);
//        sSoftbanksMap.put(0xe349, R.drawable.e0551);
        sEmojisMap.put(Food_3.EGG_PLANT, R.drawable.e0552);
//        sSoftbanksMap.put(0xe34a, R.drawable.e0552);
        sEmojisMap.put(Food_3.HOT_PEPPER, R.drawable.e0536);
        //3
        sEmojisMap.put(Food_3.EAR_OF_MAIZE, R.drawable.e0543);
        sEmojisMap.put(Food_3.ROASTED_SWEET_POTATO, R.drawable.e0578);
        sEmojisMap.put(Food_3.HONEY_POT, R.drawable.e0593);
        sEmojisMap.put(Food_3.BREAD, R.drawable.e0576);
//        sSoftbanksMap.put(0xe339, R.drawable.e0576);
        sEmojisMap.put(Food_3.CHEESE, R.drawable.e1609);
        sEmojisMap.put(Food_3.POULTRY_LEG, R.drawable.e0569);
        sEmojisMap.put(Food_3.MEAT_ON_BONE, R.drawable.e0568);
        sEmojisMap.put(Food_3.FRIED_SHRIMP, R.drawable.e0582);
        //4
        sEmojisMap.put(Food_3.COOKING, R.drawable.e0597);
//        sSoftbanksMap.put(0xe147, R.drawable.e0597);
        sEmojisMap.put(Food_3.HAMBURGER, R.drawable.e0566);
//        sSoftbanksMap.put(0xe120, R.drawable.e0566);
        sEmojisMap.put(Food_3.FRENCH_FRIES, R.drawable.e0577);
//        sSoftbanksMap.put(0xe33b, R.drawable.e0577);
        sEmojisMap.put(Food_3.HOT_DOG, R.drawable.e0527);
        sEmojisMap.put(Food_3.SLICE_OF_PIZZA, R.drawable.e0567);
        sEmojisMap.put(Food_3.SPAGHETTI, R.drawable.e0575);
//        sSoftbanksMap.put(0xe33f, R.drawable.e0575);
        sEmojisMap.put(Food_3.TACO, R.drawable.e0528);
        sEmojisMap.put(Food_3.BURRITO, R.drawable.e0529);
        //5
        sEmojisMap.put(Food_3.STEAMING_BOWL, R.drawable.e0574);
//        sSoftbanksMap.put(0xe340, R.drawable.e0574);
        sEmojisMap.put(Food_3.POT_OF_FOOD, R.drawable.e0596);
//        sSoftbanksMap.put(0xe34d, R.drawable.e0596);
        sEmojisMap.put(Food_3.FISH_CAKE, R.drawable.e0583);
        sEmojisMap.put(Food_3.SUSHI, R.drawable.e0581);
//        sSoftbanksMap.put(0xe344, R.drawable.e0581);
        sEmojisMap.put(Food_3.BENTO_BOX, R.drawable.e0595);
//        sSoftbanksMap.put(0xe34c, R.drawable.e0595);
        sEmojisMap.put(Food_3.CURRY_AND_RICE, R.drawable.e0573);
//        sSoftbanksMap.put(0xe341, R.drawable.e0573);
        sEmojisMap.put(Food_3.RICE_BALL, R.drawable.e0571);
//        sSoftbanksMap.put(0xe342, R.drawable.e0571);
        sEmojisMap.put(Food_3.COOKED_RICE, R.drawable.e0572);
//        sSoftbanksMap.put(0xe33e, R.drawable.emoji_1f35a);
        //6
        sEmojisMap.put(Food_3.RICE_CRACKER, R.drawable.e0570);
//        sSoftbanksMap.put(0xe33d, R.drawable.e0570);
        sEmojisMap.put(Food_3.ODEN, R.drawable.e0580);
//        sSoftbanksMap.put(0xe343, R.drawable.e0580);
        sEmojisMap.put(Food_3.DANGO, R.drawable.e0579);
//        sSoftbanksMap.put(0xe33c, R.drawable.e0579);
        sEmojisMap.put(Food_3.SHAVED_ICE, R.drawable.e0585);
//        sSoftbanksMap.put(0xe43f, R.drawable.e0585);
        sEmojisMap.put(Food_3.ICE_CREAM, R.drawable.e0586);
        sEmojisMap.put(Food_3.SOFT_ICE_CREAM, R.drawable.e0584);
//        sSoftbanksMap.put(0xe33a, R.drawable.e0584);
        sEmojisMap.put(Food_3.SHORT_CAKE, R.drawable.e0594);
//        sSoftbanksMap.put(0xe046, R.drawable.e0594);
        sEmojisMap.put(Food_3.BIRTHDAY_CAKE, R.drawable.e0612);
//        sSoftbanksMap.put(0xe34b, R.drawable.emoji_1f382);
        //7
        sEmojisMap.put(Food_3.CUSTARD, R.drawable.e0592);
        sEmojisMap.put(Food_3.CANDY, R.drawable.e0590);
        sEmojisMap.put(Food_3.LOLLIPOP, R.drawable.e0591);
        sEmojisMap.put(Food_3.CHOCOLATE_BAR, R.drawable.e0589);
        sEmojisMap.put(Food_3.POPCORN, R.drawable.e0609);
        sEmojisMap.put(Food_3.DOUGHNUT, R.drawable.e0587);
        sEmojisMap.put(Food_3.COOKIE, R.drawable.e0588);
        sEmojisMap.put(Food_3.BEAR_MUG, R.drawable.e0604);
//        sSoftbanksMap.put(0xe047, R.drawable.e0604);
        //8
        sEmojisMap.put(Food_3.CLINKING_BEER_MUGS, R.drawable.e0605);
//        sSoftbanksMap.put(0xe30c, R.drawable.e0605);
        sEmojisMap.put(Food_3.WINE_GLASS, R.drawable.e0601);
//        sSoftbanksMap.put(0xe044, R.drawable.e0601);
        sEmojisMap.put(Food_3.COCKTAIL_GLASS, R.drawable.e0602);
        sEmojisMap.put(Food_3.TROPICAL_DRINK, R.drawable.e0603);
        sEmojisMap.put(Food_3.BOTTLE_WITH_POPPING_CORK, R.drawable.e0608);
        sEmojisMap.put(Food_3.SAKE_BOTTLE_AND_CUP, R.drawable.e0600);
//        sSoftbanksMap.put(0xe30b, R.drawable.e0600);
        sEmojisMap.put(Food_3.TEA_CUP_WITHOUT_HANDLE, R.drawable.e0599);
//        sSoftbanksMap.put(0xe338, R.drawable.e0599);
        sEmojisMap.put(Food_3.HOT_BEVERAGE, R.drawable.e0060);
//        sSoftbanksMap.put(0xe045, R.drawable.e0060);
        //9
        sEmojisMap.put(Food_3.BABY_BOTTLE, R.drawable.e0606);
        sEmojisMap.put(Food_3.FORK_AND_KNIFE, R.drawable.e0598);
//        sSoftbanksMap.put(0xe043, R.drawable.e0598);
        sEmojisMap.put(Food_3.FORK_AND_KNIFE_WITH_PLATE, R.drawable.e0607);


//__________________________________________________________________________________________________ Activity_4
        //1
        sEmojisMap.put(Activity_4.SOCCER_BALL, R.drawable.e0111);
        sEmojisMap.put(Activity_4.BASKET_BALL, R.drawable.e0674);
        sEmojisMap.put(Activity_4.AMERICAN_FOOTBALL, R.drawable.e0697);
        sEmojisMap.put(Activity_4.BASEBALL, R.drawable.e0112);
        sEmojisMap.put(Activity_4.TENNIS_RACQUET_BALL, R.drawable.e0672);
        sEmojisMap.put(Activity_4.VOLLEY_BALL, R.drawable.e0715);
        sEmojisMap.put(Activity_4.RUGBY_FOOTBALL, R.drawable.e0698);
        sEmojisMap.put(Activity_4.BILLIARDS, R.drawable.e0659);
        //2
        sEmojisMap.put(Activity_4.FLAG_IN_HOLE, R.drawable.e0126);
        sEmojisMap.put(Activity_4.GOLFER, R.drawable.e0711);
        sEmojisMap.put(Activity_4.TABLE_TENNIS_PADDLE_BALL, R.drawable.e0718);
        sEmojisMap.put(Activity_4.BADMINTON_RACQUET_SHUTTLE_COCK, R.drawable.e0752);
        sEmojisMap.put(Activity_4.ICE_HOCKEY_STICK_PUCK, R.drawable.e0717);
        sEmojisMap.put(Activity_4.FIELD_HOCKEY_STICK_BALL, R.drawable.e0716);
        sEmojisMap.put(Activity_4.CRICKET_BAT_BALL, R.drawable.e0714);
        sEmojisMap.put(Activity_4.SKI_AND_SKI_BOOT, R.drawable.e0673);
        //3
        sEmojisMap.put(Activity_4.SKIER, R.drawable.e0129);
        sEmojisMap.put(Activity_4.SNOW_BOARDER, R.drawable.e0676);
        sEmojisMap.put(Activity_4.ICE_SKATE, R.drawable.e0130);
        sEmojisMap.put(Activity_4.BOW_AND_ARROW, R.drawable.e0753);
        sEmojisMap.put(Activity_4.FISHING_POLE_AND_FISH, R.drawable.e0645);
        sEmojisMap.put(Activity_4.ROW_BOAT, R.drawable.e1513);
        sEmojisMap.put(Activity_4.SWIMMER, R.drawable.e0699);
        sEmojisMap.put(Activity_4.SURFER, R.drawable.e0683);
        //4
        sEmojisMap.put(Activity_4.BATH, R.drawable.e1562);
        sEmojisMap.put(Activity_4.PERSON_WITH_BALL, R.drawable.e0131);
        sEmojisMap.put(Activity_4.WEIGHT_LIFTER, R.drawable.e0705);
        sEmojisMap.put(Activity_4.BICYCLIST, R.drawable.e1535);
        sEmojisMap.put(Activity_4.MOUNTAIN_BICYCLIST, R.drawable.e1542);
        sEmojisMap.put(Activity_4.HORSE_RACING, R.drawable.e0691);
        sEmojisMap.put(Activity_4.MAIN_BUSINESS_SUIT_LEVITATING, R.drawable.e1304);
        sEmojisMap.put(Activity_4.TROPHY, R.drawable.e0690);
        //5
        sEmojisMap.put(Activity_4.RUNNING_SHIRT_WITH_SASH, R.drawable.e0671);
        sEmojisMap.put(Activity_4.SPORTS_MEDAL, R.drawable.e0689);
        sEmojisMap.put(Activity_4.MILITARY_MEDAL, R.drawable.e0635);
        sEmojisMap.put(Activity_4.REMINDER_RIBBON, R.drawable.e0636);
        sEmojisMap.put(Activity_4.ROSETTE, R.drawable.e0750);
        sEmojisMap.put(Activity_4.TICKET, R.drawable.e0653);
        sEmojisMap.put(Activity_4.ADMISSION_TICKETS, R.drawable.e0641);
        sEmojisMap.put(Activity_4.PERFORMING_ARTS, R.drawable.e0655);
        //6
        sEmojisMap.put(Activity_4.ARTIST_PALETTE, R.drawable.e0650);
        sEmojisMap.put(Activity_4.CIRCUS_TENT, R.drawable.e0652);
        sEmojisMap.put(Activity_4.MICROPHONE, R.drawable.e0646);
        sEmojisMap.put(Activity_4.HEADPHONE, R.drawable.e0649);
        sEmojisMap.put(Activity_4.MUSICAL_SCORE, R.drawable.e0670);
        sEmojisMap.put(Activity_4.MUSICAL_KEYBOARD, R.drawable.e0667);
        sEmojisMap.put(Activity_4.SAXOPHONE, R.drawable.e0665);
        sEmojisMap.put(Activity_4.TRUMPET, R.drawable.e0668);
        //7
        sEmojisMap.put(Activity_4.GUITAR, R.drawable.e0666);
        sEmojisMap.put(Activity_4.VIOLIN, R.drawable.e0669);
        sEmojisMap.put(Activity_4.CLAPPER_BOARD, R.drawable.e0654);
        sEmojisMap.put(Activity_4.VIDEO_GAME, R.drawable.e0656);
        sEmojisMap.put(Activity_4.ALIEN_MONSTER, R.drawable.e1041);
        sEmojisMap.put(Activity_4.DIRECT_HIT, R.drawable.e0657);
        sEmojisMap.put(Activity_4.GAME_DIE, R.drawable.e0660);
        sEmojisMap.put(Activity_4.SLOT_MACHINE, R.drawable.e0658);
        //8
        sEmojisMap.put(Activity_4.BOWLING, R.drawable.e0661);

//__________________________________________________________________________________________________ Travel_5
        //1
        sEmojisMap.put(Travel_5.AUTOMOBILE, R.drawable.e1501);
//        sSoftbanksMap.put(0xe01b, R.drawable.e1501);
        sEmojisMap.put(Travel_5.TAXI, R.drawable.e1499);
//        sSoftbanksMap.put(0xe15a, R.drawable.e1499);
        sEmojisMap.put(Travel_5.RECREATIONAL_VEHICLE, R.drawable.e1503);
//        sSoftbanksMap.put(0xe42e, R.drawable.e1503);
        sEmojisMap.put(Travel_5.BUS, R.drawable.e1490);
//        sSoftbanksMap.put(0xe159, R.drawable.e1490);
        sEmojisMap.put(Travel_5.TROLLEY_BUS, R.drawable.e1492);
        sEmojisMap.put(Travel_5.RACING_CAR, R.drawable.e0713);
        sEmojisMap.put(Travel_5.POLICE_CAR, R.drawable.e1497);
//        sSoftbanksMap.put(0xe432, R.drawable.e1497);
        sEmojisMap.put(Travel_5.AMBULANCE, R.drawable.e1495);
//        sSoftbanksMap.put(0xe431, R.drawable.e1495);
        //2
        sEmojisMap.put(Travel_5.FIRE_ENGINE, R.drawable.e1496);
//        sSoftbanksMap.put(0xe430, R.drawable.e1496);
        sEmojisMap.put(Travel_5.MINIBUS, R.drawable.e1494);
        sEmojisMap.put(Travel_5.DELIVERY_TRUCK, R.drawable.e1504);
//        sSoftbanksMap.put(0xe42f, R.drawable.e1504);
        sEmojisMap.put(Travel_5.ARTICULATED_LORRY, R.drawable.e1505);
        sEmojisMap.put(Travel_5.TRACTOR, R.drawable.e1506);
        sEmojisMap.put(Travel_5.RACING_MOTORCYCLE, R.drawable.e0712);
        sEmojisMap.put(Travel_5.BICYCLE, R.drawable.e1533);
//        sSoftbanksMap.put(0xe136, R.drawable.e1533);
        sEmojisMap.put(Travel_5.POLICE_LIGHT, R.drawable.e1523);
        //3
        sEmojisMap.put(Travel_5.ON_COMING_POLICE_CAR, R.drawable.e1498);
        sEmojisMap.put(Travel_5.ON_COMING_BUS, R.drawable.e1491);
        sEmojisMap.put(Travel_5.ON_COMING_AUTOMOBILE, R.drawable.e1502);
        sEmojisMap.put(Travel_5.ON_COMING_TAXI, R.drawable.e1500);
        sEmojisMap.put(Travel_5.AERIAL_TRAMWAY, R.drawable.e1511);
        sEmojisMap.put(Travel_5.MOUNTAIN_CABLEWAY, R.drawable.e1510);
        sEmojisMap.put(Travel_5.SUSPENSION_RAILWAY, R.drawable.e1509);
        sEmojisMap.put(Travel_5.RAILWAY_CAR, R.drawable.e1481);
//        sSoftbanksMap.put(0xe01e, R.drawable.e1481);
        //4
        sEmojisMap.put(Travel_5.TRAMCAR, R.drawable.e1489);
        sEmojisMap.put(Travel_5.MONORAIL, R.drawable.e1507);
        sEmojisMap.put(Travel_5.HIGH_SPEED_TRAIN, R.drawable.e1482);
//        sSoftbanksMap.put(0xe435, R.drawable.e1482);
        sEmojisMap.put(Travel_5.HIGH_SPEED_TRAIN_BULLET_NOSE, R.drawable.e1483);
//        sSoftbanksMap.put(0xe01f, R.drawable.e1483);
        sEmojisMap.put(Travel_5.LIGHT_RAIL, R.drawable.e1486);
        sEmojisMap.put(Travel_5.MOUNTAIN_RAILWAY, R.drawable.e1508);
        sEmojisMap.put(Travel_5.STEAM_LOCOMOTIVE, R.drawable.e1480);
        sEmojisMap.put(Travel_5.TRAIN, R.drawable.e1484);
        //5
        sEmojisMap.put(Travel_5.METRO, R.drawable.e1485);
//        sSoftbanksMap.put(0xe434, R.drawable.e1485);
        sEmojisMap.put(Travel_5.TRAM, R.drawable.e1488);
        sEmojisMap.put(Travel_5.STATION, R.drawable.e1487);
//        sSoftbanksMap.put(0xe039, R.drawable.e1487);
        sEmojisMap.put(Travel_5.HELICOPTER, R.drawable.e1479);
        sEmojisMap.put(Travel_5.SMALL_AIRPLANE, R.drawable.e1585);
        sEmojisMap.put(Travel_5.AIRPLANE, R.drawable.e0141);
//        sSoftbanksMap.put(0xe01d, R.drawable.e0141);
        sEmojisMap.put(Travel_5.AIRPLANE_DEPARTURE, R.drawable.e1586);
        sEmojisMap.put(Travel_5.AIRPLANE_ARRIVING, R.drawable.e1587);
        //6
        sEmojisMap.put(Travel_5.SAILBOT , R.drawable.e0128);
//        sSoftbanksMap.put(0xe01c, R.drawable.e0128);
        sEmojisMap.put(Travel_5.MOTORBOAT , R.drawable.e1584);
        sEmojisMap.put(Travel_5.SPEEDBOAT, R.drawable.e1519);
//        sSoftbanksMap.put(0xe135, R.drawable.e1519);
        sEmojisMap.put(Travel_5.FERRY, R.drawable.e0127);
        sEmojisMap.put(Travel_5.PASSENGER_SHIP, R.drawable.e1589);
        sEmojisMap.put(Travel_5.ROCKET, R.drawable.e1478);
//        sSoftbanksMap.put(0xe10d, R.drawable.e1478);
        sEmojisMap.put(Travel_5.SATELLITE, R.drawable.e1588);
        sEmojisMap.put(Travel_5.SEAT, R.drawable.e1140);
//        sSoftbanksMap.put(0xe11f, R.drawable.e1140);
        //7
        sEmojisMap.put(Travel_5.ANCHOR , R.drawable.e0098);
        sEmojisMap.put(Travel_5.CONSTRUCTION_SIGN, R.drawable.e1522);
//        sSoftbanksMap.put(0xe137, R.drawable.e1522);
        sEmojisMap.put(Travel_5.FUEL_PUMP , R.drawable.e0138);
//        sSoftbanksMap.put(0xe03a, R.drawable.e0138);
        sEmojisMap.put(Travel_5.BUS_STOP , R.drawable.e1493);
//        sSoftbanksMap.put(0xe150, R.drawable.e1493);
        sEmojisMap.put(Travel_5.VERTICAL_TRAFFIC_LIGHT, R.drawable.e1521);
        sEmojisMap.put(Travel_5.HORIZONTAL_TRAFFIC_LIGHT, R.drawable.e1520);
//        sSoftbanksMap.put(0xe14e, R.drawable.e1520);
        sEmojisMap.put(Travel_5.CHEQUERED_FLAG, R.drawable.e0675);
//        sSoftbanksMap.put(0xe132, R.drawable.e0675);
        sEmojisMap.put(Travel_5.SHIP, R.drawable.e1512);
//        sSoftbanksMap.put(0xe202, R.drawable.e1512);
        //8
        sEmojisMap.put(Travel_5.FERRIS_WHEEL, R.drawable.e0643);
//        sSoftbanksMap.put(0xe124, R.drawable.e0643);
        sEmojisMap.put(Travel_5.ROLLER_COASTER, R.drawable.e0644);
//        sSoftbanksMap.put(0xe433, R.drawable.e0644);
        sEmojisMap.put(Travel_5.CAROUSEL_HORSE, R.drawable.e0642);
        sEmojisMap.put(Travel_5.BUILDING_CONSTRUCTION, R.drawable.e0722);
        sEmojisMap.put(Travel_5.FOGGY, R.drawable.e0485);
        sEmojisMap.put(Travel_5.TOKYO_TOWER, R.drawable.e1354);
//        sSoftbanksMap.put(0xe509, R.drawable.e1354);
        sEmojisMap.put(Travel_5.FACTORY, R.drawable.e0744);
//        sSoftbanksMap.put(0xe508, R.drawable.e0744);
        sEmojisMap.put(Travel_5.FOUNTAIN, R.drawable.e0125);
//        sSoftbanksMap.put(0xe121, R.drawable.e0125);
        //9
        sEmojisMap.put(Travel_5.MOON_VIEWING_CEREMONY, R.drawable.e0632);
//        sSoftbanksMap.put(0xe446, R.drawable.e0632);
        sEmojisMap.put(Travel_5.MOUNTAIN, R.drawable.e0123);
        sEmojisMap.put(Travel_5.SNOW_CAPPED_MOUNTAIN, R.drawable.e0719);
        sEmojisMap.put(Travel_5.MOUNT_FUJI, R.drawable.e1353);
//        sSoftbanksMap.put(0xe03b, R.drawable.e1353);
        sEmojisMap.put(Travel_5.VOLCANO, R.drawable.e0495);
        sEmojisMap.put(Travel_5.SILHOUETTE_OF_JAPAN, R.drawable.e1356);
        sEmojisMap.put(Travel_5.CAMPING, R.drawable.e0720);
        sEmojisMap.put(Travel_5.TENT, R.drawable.e0137);
//        sSoftbanksMap.put(0xe122, R.drawable.emoji_26fa);
        //10
        sEmojisMap.put(Travel_5.NATIONAL_PARK, R.drawable.e0729);
        sEmojisMap.put(Travel_5.MOTORWAY, R.drawable.e1582);
        sEmojisMap.put(Travel_5.RAILWAY_TRACK, R.drawable.e1583);
        sEmojisMap.put(Travel_5.SUNRISE, R.drawable.e0489);
//        sSoftbanksMap.put(0xe449, R.drawable.e0489);
        sEmojisMap.put(Travel_5.SUNRISE_OVER_MOUNTAIN, R.drawable.e0488);
//        sSoftbanksMap.put(0xe04d, R.drawable.e0488);
        sEmojisMap.put(Travel_5.DESERT, R.drawable.e0727);
        sEmojisMap.put(Travel_5.BEACH_WITH_UMBRELLA, R.drawable.e0721);
        sEmojisMap.put(Travel_5.DESERT_ISLAND, R.drawable.e0728);
        //11
        sEmojisMap.put(Travel_5.SUNSET_OVER_BUILDINGS, R.drawable.e0491);
//        sSoftbanksMap.put(0xe44a, R.drawable.e0491);
        sEmojisMap.put(Travel_5.CITY_SCAPE_AT_DUSK, R.drawable.e0490);
//        sSoftbanksMap.put(0xe146, R.drawable.e0490);
        sEmojisMap.put(Travel_5.CITY_SCAPE, R.drawable.e0724);
        sEmojisMap.put(Travel_5.NIGHT_WITH_STARS, R.drawable.e0487);
        sEmojisMap.put(Travel_5.BRIDGE_AT_NIGHT, R.drawable.e0493);
        sEmojisMap.put(Travel_5.MILKY_WAY, R.drawable.e0496);
//        sSoftbanksMap.put(0xe44b, R.drawable.e0496);
        sEmojisMap.put(Travel_5.SHOOTING_STAR, R.drawable.e0516);
//        sSoftbanksMap.put(0xe44b, R.drawable.e0516);
        sEmojisMap.put(Travel_5.FIRE_WORK_SPARKLER, R.drawable.e0622);
//        sSoftbanksMap.put(0xe440, R.drawable.e0622);
        //12
        sEmojisMap.put(Travel_5.FIREWORKS, R.drawable.e0621);
//        sSoftbanksMap.put(0xe117, R.drawable.e0621);
        sEmojisMap.put(Travel_5.RAINBOW, R.drawable.e0492);
//        sSoftbanksMap.put(0xe44c, R.drawable.e0492);
        sEmojisMap.put(Travel_5.HOUSE_BUILDINGS, R.drawable.e0723);
        sEmojisMap.put(Travel_5.EUROPEAN_CASTLE, R.drawable.e0747);
//        sSoftbanksMap.put(0xe506, R.drawable.e0747);
        sEmojisMap.put(Travel_5.JAPANESE_CASTLE, R.drawable.e0746);
//        sSoftbanksMap.put(0xe505, R.drawable.e0746);
        sEmojisMap.put(Travel_5.STADIUM, R.drawable.e0730);
        sEmojisMap.put(Travel_5.STATUE_OF_LIBERTY, R.drawable.e1355);
//        sSoftbanksMap.put(0xe51d, R.drawable.e1355);
        sEmojisMap.put(Travel_5.HOUSE_BUILDING, R.drawable.e0731);
//        sSoftbanksMap.put(0xe036, R.drawable.e0731);
        //13
        sEmojisMap.put(Travel_5.HOUSE_WITH_GARDEN, R.drawable.e0732);
        sEmojisMap.put(Travel_5.DERELICT_HOUSE_BUILDING, R.drawable.e0725);
        sEmojisMap.put(Travel_5.OFFICE_BUILDING, R.drawable.e0733);
//        sSoftbanksMap.put(0xe038, R.drawable.e0733);
        sEmojisMap.put(Travel_5.DEPARTMENT_STORE, R.drawable.e0743);
//        sSoftbanksMap.put(0xe504, R.drawable.e0743);
        sEmojisMap.put(Travel_5.JAPANESE_POST_OFFICE, R.drawable.e0734);
//        sSoftbanksMap.put(0xe153, R.drawable.e0734);
        sEmojisMap.put(Travel_5.EUROPEAN_POST_OFFICE, R.drawable.e0735);
        sEmojisMap.put(Travel_5.HOSPITAL, R.drawable.e0736);
//        sSoftbanksMap.put(0xe155, R.drawable.e0736);
        sEmojisMap.put(Travel_5.BANK, R.drawable.e0737);
//        sSoftbanksMap.put(0xe14d, R.drawable.e0737);
        //14
        sEmojisMap.put(Travel_5.HOTEL, R.drawable.e0739);
//        sSoftbanksMap.put(0xe158, R.drawable.e0739);
        sEmojisMap.put(Travel_5.CONVENIENCE_STORE, R.drawable.e0741);
//        sSoftbanksMap.put(0xe156, R.drawable.e0741);
        sEmojisMap.put(Travel_5.SCHOOL, R.drawable.e0742);
//        sSoftbanksMap.put(0xe157, R.drawable.e0742);
        sEmojisMap.put(Travel_5.LOVE_HOTEL, R.drawable.e0740);
//        sSoftbanksMap.put(0xe501, R.drawable.e0740);
        sEmojisMap.put(Travel_5.WEDDING, R.drawable.e1095);
//        sSoftbanksMap.put(0xe43d, R.drawable.e1095);
        sEmojisMap.put(Travel_5.CLASSICAL_BUILDING, R.drawable.e0726);
        sEmojisMap.put(Travel_5.CHURCH, R.drawable.e0122);
//        sSoftbanksMap.put(0xe037, R.drawable.e0122);
        sEmojisMap.put(Travel_5.MOSQUE, R.drawable.e1274);
        //15
        sEmojisMap.put(Travel_5.SYNAGOGUE, R.drawable.e1275);
        sEmojisMap.put(Travel_5.KAABA, R.drawable.e1273);
        sEmojisMap.put(Travel_5.SHINTO_SHRINE, R.drawable.e0121);

//__________________________________________________________________________________________________ Objects_6
        //1
        sEmojisMap.put(Objects_6.WATCH, R.drawable.e0026);
        sEmojisMap.put(Objects_6.MOBILE_PHONE, R.drawable.e1195);
        sEmojisMap.put(Objects_6.MOBILE_PHONE_WITH_RIGHT_ARROW, R.drawable.e1196);
        sEmojisMap.put(Objects_6.PERSONAL_COMPUTER, R.drawable.e1141);
        sEmojisMap.put(Objects_6.KEYBOARD, R.drawable.e0028);
        sEmojisMap.put(Objects_6.DESKTOP_COMPUTER, R.drawable.e1333);
        sEmojisMap.put(Objects_6.PRINTER, R.drawable.e1334);
        sEmojisMap.put(Objects_6.THREE_BUTTON_MOUSE, R.drawable.e1335);
        //2
        sEmojisMap.put(Objects_6.TRACK_BALL, R.drawable.e1336);
        sEmojisMap.put(Objects_6.JOYSTICK, R.drawable.e1309);
        sEmojisMap.put(Objects_6.COMPRESSION, R.drawable.e1344);
        sEmojisMap.put(Objects_6.MINI_DISC, R.drawable.e1143);
        sEmojisMap.put(Objects_6.FLOPPY_DISK, R.drawable.e1144);
        sEmojisMap.put(Objects_6.OPTICAL_DISC, R.drawable.e1145);
        sEmojisMap.put(Objects_6.DVD, R.drawable.e1146);
        sEmojisMap.put(Objects_6.VIDEO_CASSETTE, R.drawable.e1206);
        //3
        sEmojisMap.put(Objects_6.CAMERA, R.drawable.e1201);
        sEmojisMap.put(Objects_6.CAMERA_WITH_FLASH, R.drawable.e1202);
        sEmojisMap.put(Objects_6.VIDEO_CAMERA, R.drawable.e1203);
        sEmojisMap.put(Objects_6.MOVIE_CAMERA, R.drawable.e0647);
        sEmojisMap.put(Objects_6.FILM_PROJECTOR, R.drawable.e1207);
        sEmojisMap.put(Objects_6.FILM_FRAMES, R.drawable.e0640);
        sEmojisMap.put(Objects_6.TELEPHONE_RECEIVER, R.drawable.e1176);
        sEmojisMap.put(Objects_6.BLACK_TELEPHONE, R.drawable.e0057);
        //4
        sEmojisMap.put(Objects_6.PAGER, R.drawable.e1177);
        sEmojisMap.put(Objects_6.FAX_MACHINE, R.drawable.e1178);
        sEmojisMap.put(Objects_6.TELEVISION, R.drawable.e1204);
        sEmojisMap.put(Objects_6.RADIO, R.drawable.e1205);
        sEmojisMap.put(Objects_6.STUDIO_MICROPHONE, R.drawable.e0637);
        sEmojisMap.put(Objects_6.LEVEL_SLIDER, R.drawable.e0638);
        sEmojisMap.put(Objects_6.CONTROL_KNOBS, R.drawable.e0639);
        sEmojisMap.put(Objects_6.STOP_WATCH, R.drawable.e0037);
        //5
        sEmojisMap.put(Objects_6.TIMER_CLOCK, R.drawable.e0038);
        sEmojisMap.put(Objects_6.ALARM_CLOCK, R.drawable.e0036);
        sEmojisMap.put(Objects_6.MANTEL_PIECE_CLOCK, R.drawable.e1302);
        sEmojisMap.put(Objects_6.HOUR_GLASS_WITH_FLOWING_STAND, R.drawable.e0039);
        sEmojisMap.put(Objects_6.HOUR_GLASS, R.drawable.e0027);
        sEmojisMap.put(Objects_6.SATELLITE_ANTENNA, R.drawable.e1179);
        sEmojisMap.put(Objects_6.BATTERY, R.drawable.e1220);
        sEmojisMap.put(Objects_6.ELECTRIC_PLUG, R.drawable.e1221);
        //6
        sEmojisMap.put(Objects_6.ELECTRIC_LIGHT_BULB, R.drawable.e1110);
        sEmojisMap.put(Objects_6.ELECTRIC_TORCH, R.drawable.e1247);
        sEmojisMap.put(Objects_6.CANDLE, R.drawable.e1301);
        sEmojisMap.put(Objects_6.WASTE_BASKET, R.drawable.e1341);
        sEmojisMap.put(Objects_6.OIL_DRUM, R.drawable.e1581);
        sEmojisMap.put(Objects_6.MONEY_WITH_WINGS, R.drawable.e1138);
        sEmojisMap.put(Objects_6.BANK_NOTE_WITH_DOLLAR_SIGN, R.drawable.e1135);
        sEmojisMap.put(Objects_6.BANK_NOTE_WITH_YEN_SIGN, R.drawable.e1134);
        //7
        sEmojisMap.put(Objects_6.BANK_NOTE_WITH_EURO_SIGN, R.drawable.e1136);
        sEmojisMap.put(Objects_6.BANK_NOTE_WITH_POUNDS_SIGN, R.drawable.e1137);
        sEmojisMap.put(Objects_6.MONEY_BAG, R.drawable.e1130);
        sEmojisMap.put(Objects_6.CREDIT_CARD, R.drawable.e1133);
        sEmojisMap.put(Objects_6.GEM_STONE, R.drawable.e1087);
        sEmojisMap.put(Objects_6.SCALES, R.drawable.e0100);
        sEmojisMap.put(Objects_6.WRENCH, R.drawable.e1248);
        sEmojisMap.put(Objects_6.HAMMER, R.drawable.e1249);
        //8
        sEmojisMap.put(Objects_6.HAMMER_AND_PICK, R.drawable.e0097);
        sEmojisMap.put(Objects_6.HAMMER_AND_WRENCH, R.drawable.e1579);
        sEmojisMap.put(Objects_6.PICK, R.drawable.e0117);
        sEmojisMap.put(Objects_6.NUT_AND_BOLT, R.drawable.e1250);
        sEmojisMap.put(Objects_6.GEAR, R.drawable.e0102);
        sEmojisMap.put(Objects_6.CHAINS, R.drawable.e0119);
        sEmojisMap.put(Objects_6.PISTOL, R.drawable.e1252);
        sEmojisMap.put(Objects_6.BOMB, R.drawable.e1112);
        //9
        sEmojisMap.put(Objects_6.HOCHO, R.drawable.e1251);
        sEmojisMap.put(Objects_6.DAGGER_KNIFE, R.drawable.e1347);
        sEmojisMap.put(Objects_6.CROSSED_WORDS, R.drawable.e0099);
        sEmojisMap.put(Objects_6.SHIELD, R.drawable.e1580);
        sEmojisMap.put(Objects_6.SMOKING_SYMBOL, R.drawable.e1527);
        sEmojisMap.put(Objects_6.SKULL_AND_CROSS_BONES, R.drawable.e0068);
        sEmojisMap.put(Objects_6.COFFIN, R.drawable.e0109);
        sEmojisMap.put(Objects_6.FUNERAL_URN, R.drawable.e0110);
        //10
        sEmojisMap.put(Objects_6.AMPHORA, R.drawable.e0754);
        sEmojisMap.put(Objects_6.CRYSTAL_BALL, R.drawable.e1255);
        sEmojisMap.put(Objects_6.PRAYER_BEADS, R.drawable.e1208);
        sEmojisMap.put(Objects_6.BARBER_POLE, R.drawable.e1081);
        sEmojisMap.put(Objects_6.ALEMBIC, R.drawable.e0101);
        sEmojisMap.put(Objects_6.TELESCOPE, R.drawable.e1254);
        sEmojisMap.put(Objects_6.MICROSCOPE, R.drawable.e1253);
        sEmojisMap.put(Objects_6.HOLE, R.drawable.e1303);
        //11
        sEmojisMap.put(Objects_6.PILL, R.drawable.e1083);
        sEmojisMap.put(Objects_6.SYRINGE, R.drawable.e1082);
        sEmojisMap.put(Objects_6.THERMOMETER, R.drawable.e0517);
        sEmojisMap.put(Objects_6.LABEL, R.drawable.e0751);
        sEmojisMap.put(Objects_6.BOOKMARK, R.drawable.e1231);
        sEmojisMap.put(Objects_6.TOILET, R.drawable.e1559);
        sEmojisMap.put(Objects_6.SHOWER, R.drawable.e1561);
        sEmojisMap.put(Objects_6.BATH_TUB, R.drawable.e1568);
        //12
        sEmojisMap.put(Objects_6.KEY, R.drawable.e1226);
        sEmojisMap.put(Objects_6.OLD_KEY, R.drawable.e1345);
        sEmojisMap.put(Objects_6.COUCH_AND_LAMP, R.drawable.e1573);
        sEmojisMap.put(Objects_6.SLEEPING_ACCOMMODATION, R.drawable.e1574);
        sEmojisMap.put(Objects_6.BED, R.drawable.e1577);
        sEmojisMap.put(Objects_6.DOOR, R.drawable.e1525);
        sEmojisMap.put(Objects_6.BELL_HOP_BELL, R.drawable.e1576);
        sEmojisMap.put(Objects_6.FRAME_WITH_PICTURE, R.drawable.e1337);
        //13
        sEmojisMap.put(Objects_6.WORLD_MAP, R.drawable.e1352);
        sEmojisMap.put(Objects_6.UMBRELLA_ON_GROUND, R.drawable.e0124);
        sEmojisMap.put(Objects_6.MOYAI, R.drawable.e1357);
        sEmojisMap.put(Objects_6.SHOPPING_BAGS, R.drawable.e1575);
        sEmojisMap.put(Objects_6.BALLOON, R.drawable.e0623);
        sEmojisMap.put(Objects_6.CARP_STREAMER, R.drawable.e0630);
        sEmojisMap.put(Objects_6.RIBBON, R.drawable.e0610);
        sEmojisMap.put(Objects_6.WRAPPED_PRESENT, R.drawable.e0611);
        //14
        sEmojisMap.put(Objects_6.CONFETTI_BALL, R.drawable.e0625);
        sEmojisMap.put(Objects_6.PARTY_POPPER, R.drawable.e0624);
        sEmojisMap.put(Objects_6.JAPANESE_DOLLS, R.drawable.e0629);
        sEmojisMap.put(Objects_6.WIND_CHIME, R.drawable.e0631);
        sEmojisMap.put(Objects_6.CROSSED_FLAGS, R.drawable.e0627);
        sEmojisMap.put(Objects_6.IZAKAYA_LANTERN, R.drawable.e0745);
        sEmojisMap.put(Objects_6.ENVELOPE, R.drawable.e0142);
        sEmojisMap.put(Objects_6.ENVELOPE_WITH_DOWN_ARROW, R.drawable.e1187);
        //15
        sEmojisMap.put(Objects_6.INCOMING_ENVELOPE, R.drawable.e1186);
        sEmojisMap.put(Objects_6.EMAIL_SYMBOL, R.drawable.e1185);
        sEmojisMap.put(Objects_6.LOVE_LETTER, R.drawable.e1085);
        sEmojisMap.put(Objects_6.POST_BOX, R.drawable.e1192);
        sEmojisMap.put(Objects_6.CLOSED_MAIL_BOX_WITH_LOWERED_FLAG, R.drawable.e1188);
        sEmojisMap.put(Objects_6.CLOSED_MAIL_BOX_WITH_RAISED_FLAG, R.drawable.e1189);
        sEmojisMap.put(Objects_6.OPEN_MAIL_BOX_WITH_RAISED_FLAG, R.drawable.e1190);
        sEmojisMap.put(Objects_6.OPEN_MAIL_BOX_WITH_LOWERED_FLAG, R.drawable.e1191);
        //16
        sEmojisMap.put(Objects_6.PACKAGE, R.drawable.e1184);
        sEmojisMap.put(Objects_6.POSTAL_HORN, R.drawable.e1193);
        sEmojisMap.put(Objects_6.INBOX_TRAY, R.drawable.e1183);
        sEmojisMap.put(Objects_6.OUTBOX_TRAY, R.drawable.e1182);
        sEmojisMap.put(Objects_6.SCROLL, R.drawable.e1174);
        sEmojisMap.put(Objects_6.PAGE_WITH_CURL, R.drawable.e1149);
        sEmojisMap.put(Objects_6.BOOKMARK_TABS, R.drawable.e1163);
        sEmojisMap.put(Objects_6.BAR_CHART, R.drawable.e1156);
        //17
        sEmojisMap.put(Objects_6.CHART_WITH_UPWARDS_TREND, R.drawable.e1154);
        sEmojisMap.put(Objects_6.CHART_WITH_DOWNWARDS_TREND, R.drawable.e1155);
        sEmojisMap.put(Objects_6.PAGE_FACING_UP, R.drawable.e1150);
        sEmojisMap.put(Objects_6.CALENDER, R.drawable.e1151);
        sEmojisMap.put(Objects_6.TEAR_OFF_CALENDAR, R.drawable.e1152);
        sEmojisMap.put(Objects_6.SPIRAL_CALENDAR_PAD, R.drawable.e1343);
        sEmojisMap.put(Objects_6.CARD_INDEX, R.drawable.e1153);
        sEmojisMap.put(Objects_6.CARD_FILE_BOX, R.drawable.e1339);
        //18
        sEmojisMap.put(Objects_6.BALLOT_BOX_WITH_BALLOT, R.drawable.e1351);
        sEmojisMap.put(Objects_6.FILE_CABINET, R.drawable.e1340);
        sEmojisMap.put(Objects_6.CLIP_BOARD, R.drawable.e1157);
        sEmojisMap.put(Objects_6.SPIRAL_NOTEPAD, R.drawable.e1342);
        sEmojisMap.put(Objects_6.FILE_FOLDER, R.drawable.e1147);
        sEmojisMap.put(Objects_6.OPEN_FILE_FOLDER, R.drawable.e1148);
        sEmojisMap.put(Objects_6.CARD_INDEX_DIVIDERS, R.drawable.e1338);
        sEmojisMap.put(Objects_6.ROLLED_UP_NEWSPAPER, R.drawable.e1346);
        //19
        sEmojisMap.put(Objects_6.NEWSPAPER, R.drawable.e1194);
        sEmojisMap.put(Objects_6.NOTEBOOK, R.drawable.e1165);
        sEmojisMap.put(Objects_6.CLOSED_BOOK, R.drawable.e1167);
        sEmojisMap.put(Objects_6.GREEN_BOOK, R.drawable.e1169);
        sEmojisMap.put(Objects_6.BLUE_BOOK, R.drawable.e1170);
        sEmojisMap.put(Objects_6.ORANGE_BOOK, R.drawable.e1171);
        sEmojisMap.put(Objects_6.NOTEBOOK_WITH_DECORATIVE_COVER, R.drawable.e1166);
        sEmojisMap.put(Objects_6.LEDGER, R.drawable.e1164);
        //20
        sEmojisMap.put(Objects_6.BOOKS, R.drawable.e1172);
        sEmojisMap.put(Objects_6.OPEN_BOOK, R.drawable.e1168);
        sEmojisMap.put(Objects_6.LINK_SYMBOL, R.drawable.e1232);
        sEmojisMap.put(Objects_6.PAPER_CLIP, R.drawable.e1160);
        sEmojisMap.put(Objects_6.LINKED_PAPER_CLIPS, R.drawable.e1310);
        sEmojisMap.put(Objects_6.BLACK_SCISSORS, R.drawable.e0139);
        sEmojisMap.put(Objects_6.TRIANGULAR_RULER, R.drawable.e1162);
        sEmojisMap.put(Objects_6.STRAIGHT_RULER, R.drawable.e1161);
        //21
        sEmojisMap.put(Objects_6.PUSHPIN, R.drawable.e1158);
        sEmojisMap.put(Objects_6.ROUND_PUSHPIN, R.drawable.e1159);
        sEmojisMap.put(Objects_6.TRIANGULAR_FLAG_POST, R.drawable.e1524);
        sEmojisMap.put(Objects_6.WAVING_WHITE_FLAG, R.drawable.e0748);
        sEmojisMap.put(Objects_6.WAVING_BLACK_FLAG, R.drawable.e0749);
        sEmojisMap.put(Objects_6.CLOSED_LOCK_WITH_KEY, R.drawable.e1225);
        sEmojisMap.put(Objects_6.LOCK, R.drawable.e1227);
        sEmojisMap.put(Objects_6.OPEN_LOCK, R.drawable.e1228);
        //22
        sEmojisMap.put(Objects_6.LOCK_WITH_INK_PEN, R.drawable.e1224);
        sEmojisMap.put(Objects_6.LOWER_LEFT_BALL_POINT_PEN, R.drawable.e1311);
        sEmojisMap.put(Objects_6.LOWER_LEFT_FOUNTAIN_PEN, R.drawable.e1312);
        sEmojisMap.put(Objects_6.BLACK_NIB, R.drawable.e0168);
        sEmojisMap.put(Objects_6.MEMO, R.drawable.e1175);
        sEmojisMap.put(Objects_6.PENCIL, R.drawable.e0167);
        sEmojisMap.put(Objects_6.LOWER_LEFT_CRAYON, R.drawable.e1314);
        sEmojisMap.put(Objects_6.LOWER_LEFT_PAINT_BRUSH, R.drawable.e1313);
        //23
        sEmojisMap.put(Objects_6.LEFT_POINTING_MAGNIFYING_GLASS, R.drawable.e1222);
        sEmojisMap.put(Objects_6.RIGHT_POINTING_MAGNIFYING_GLASS, R.drawable.e1223);

//__________________________________________________________________________________________________ Symbols_7
        //1
        sEmojisMap.put(Symbols_7.HEAVY_BLACK_HEART, R.drawable.e0185);
        sEmojisMap.put(Symbols_7.YELLOW_HEART, R.drawable.e1104);
        sEmojisMap.put(Symbols_7.GREEN_HEART, R.drawable.e1103);
        sEmojisMap.put(Symbols_7.BLUE_HEART, R.drawable.e1102);
        sEmojisMap.put(Symbols_7.PURPLE_HEART, R.drawable.e1105);
        sEmojisMap.put(Symbols_7.BROKEN_HEART, R.drawable.e1097);
        sEmojisMap.put(Symbols_7.HEAVY_HEART_EXCLAMATION_MARK_ORNAMENT, R.drawable.e0184);
        sEmojisMap.put(Symbols_7.TWO_HEARTS, R.drawable.e1098);
        //2
        sEmojisMap.put(Symbols_7.REVOLVING_HEARTS, R.drawable.e1107);
        sEmojisMap.put(Symbols_7.BEATING_HEART, R.drawable.e1096);
        sEmojisMap.put(Symbols_7.GROWING_HEART, R.drawable.e1100);
        sEmojisMap.put(Symbols_7.SPARKLING_HEART, R.drawable.e1099);
        sEmojisMap.put(Symbols_7.HEART_WITH_ARROW, R.drawable.e1101);
        sEmojisMap.put(Symbols_7.HEART_WITH_RIBBON, R.drawable.e1106);
        sEmojisMap.put(Symbols_7.HEART_DECORATION, R.drawable.e1108);
        sEmojisMap.put(Symbols_7.PEACE_SYMBOL, R.drawable.e0073);
        //3
        sEmojisMap.put(Symbols_7.LATIN_CROSS, R.drawable.e0171);
        sEmojisMap.put(Symbols_7.STAR_AND_CRESCENT, R.drawable.e0072);
        sEmojisMap.put(Symbols_7.OM_SYMBOL, R.drawable.e1271);
        sEmojisMap.put(Symbols_7.WHEEL_OF_DHARMA, R.drawable.e0075);
        sEmojisMap.put(Symbols_7.STAR_OF_DAVID, R.drawable.e0172);
        sEmojisMap.put(Symbols_7.SIX_POINTED_STAR_WITH_MIDDLE_DOT, R.drawable.e1256);
        sEmojisMap.put(Symbols_7.MENORAH_WITH_NINE_BRANCHES, R.drawable.e1276);
        sEmojisMap.put(Symbols_7.YIN_YANG, R.drawable.e0074);
        //4
        sEmojisMap.put(Symbols_7.ORTHODOX_CROSS, R.drawable.e0071);
        sEmojisMap.put(Symbols_7.PLACE_OF_WORSHIP, R.drawable.e1578);
        sEmojisMap.put(Symbols_7.OPHIUCHUS, R.drawable.e0116);
        sEmojisMap.put(Symbols_7.ARIES, R.drawable.e0078);
        sEmojisMap.put(Symbols_7.TAURUS, R.drawable.e0079);
        sEmojisMap.put(Symbols_7.GEMINI, R.drawable.e0080);
        sEmojisMap.put(Symbols_7.CANCER, R.drawable.e0081);
        sEmojisMap.put(Symbols_7.LEO, R.drawable.e0082);
        //5
        sEmojisMap.put(Symbols_7.VIRGO, R.drawable.e0083);
        sEmojisMap.put(Symbols_7.LIBRA, R.drawable.e0084);
        sEmojisMap.put(Symbols_7.SCORPIUS, R.drawable.e0085);
        sEmojisMap.put(Symbols_7.SAGITTARIUS, R.drawable.e0086);
        sEmojisMap.put(Symbols_7.CAPRICORN, R.drawable.e0087);
        sEmojisMap.put(Symbols_7.AQUARIUS, R.drawable.e0088);
        sEmojisMap.put(Symbols_7.PISCES, R.drawable.e0089);
        sEmojisMap.put(Symbols_7.SQUARED_ID, R.drawable.e0215);
        //6
        sEmojisMap.put(Symbols_7.ATOM_SYMBOL, R.drawable.e0103);
        sEmojisMap.put(Symbols_7.SQUARED_CJK_UNIFIED_IDEOGRAPH_7A7A, R.drawable.e0474);
        sEmojisMap.put(Symbols_7.SQUARED_CJK_UNIFIED_IDEOGRAPH_5272, R.drawable.e0480);
        sEmojisMap.put(Symbols_7.RADIOACTIVE_SIGN, R.drawable.e0069);
        sEmojisMap.put(Symbols_7.BIOHAZARD_SIGN, R.drawable.e0070);
        sEmojisMap.put(Symbols_7.MOBILE_PHONE_OFF, R.drawable.e1198);
        sEmojisMap.put(Symbols_7.VIBRATION_MODE, R.drawable.e1197);
        sEmojisMap.put(Symbols_7.SQUARED_CJK_UNIFIED_IDEOGRAPH_6709, R.drawable.e0477);
        //7
        sEmojisMap.put(Symbols_7.SQUARED_CJK_UNIFIED_IDEOGRAPH_7121, R.drawable.e0471);
        sEmojisMap.put(Symbols_7.SQUARED_CJK_UNIFIED_IDEOGRAPH_7533, R.drawable.e0479);
        sEmojisMap.put(Symbols_7.SQUARED_CJK_UNIFIED_IDEOGRAPH_55B6, R.drawable.e0481);
        sEmojisMap.put(Symbols_7.SQUARED_CJK_UNIFIED_IDEOGRAPH_6708, R.drawable.e0478);
        sEmojisMap.put(Symbols_7.EIGHT_POINTED_BLACK_STAR, R.drawable.e0175);
        sEmojisMap.put(Symbols_7.SQUARED_VS, R.drawable.e0221);
        sEmojisMap.put(Symbols_7.CIRCLED_IDEOGRAPH_ACCEPT, R.drawable.e0483);
        sEmojisMap.put(Symbols_7.WHITE_FLOWER, R.drawable.e1128);
        //8
        sEmojisMap.put(Symbols_7.CIRCLED_IDEOGRAPH_ADVANTAGE, R.drawable.e0482);
        sEmojisMap.put(Symbols_7.CIRCLED_IDEOGRAPH_SECRET, R.drawable.e0204);
        sEmojisMap.put(Symbols_7.CIRCLED_IDEOGRAPH_CONGRATULATION, R.drawable.e0203);
        sEmojisMap.put(Symbols_7.SQUARED_CJK_UNIFIED_IDEOGRAPH_5408, R.drawable.e0475);
        sEmojisMap.put(Symbols_7.SQUARED_CJK_UNIFIED_IDEOGRAPH_6E80, R.drawable.e0476);
        sEmojisMap.put(Symbols_7.SQUARED_CJK_UNIFIED_IDEOGRAPH_7981, R.drawable.e0473);
        sEmojisMap.put(Symbols_7.NEGATIVE_SQUARED_LATIN_CAPITAL_LETTER_A, R.drawable.e0207);
        sEmojisMap.put(Symbols_7.NEGATIVE_SQUARED_LATIN_CAPITAL_LETTER_B, R.drawable.e0208);
        //9
        sEmojisMap.put(Symbols_7.NEGATIVE_SQUARED_AB, R.drawable.e0211);
        sEmojisMap.put(Symbols_7.SQUARED_CL, R.drawable.e0212);
        sEmojisMap.put(Symbols_7.NEGATIVE_SQUARED_LATIN_CAPITAL_LETTER_O, R.drawable.e0209);
        sEmojisMap.put(Symbols_7.SQUARED_SOS, R.drawable.e0219);
        sEmojisMap.put(Symbols_7.NO_ENTRY, R.drawable.e0120);
        sEmojisMap.put(Symbols_7.NAME_BADGE, R.drawable.e1173);
        sEmojisMap.put(Symbols_7.NO_ENTRY_SIGN, R.drawable.e1526);
        sEmojisMap.put(Symbols_7.CROSS_MARK, R.drawable.e0178);
        //10
        sEmojisMap.put(Symbols_7.HEAVY_LARGE_CIRCLE, R.drawable.e0200);
        sEmojisMap.put(Symbols_7.ANGER_SYMBOL, R.drawable.e1111);
        sEmojisMap.put(Symbols_7.HOT_SPRINGS, R.drawable.e0094);
        sEmojisMap.put(Symbols_7.NO_PEDESTRIANS, R.drawable.e1553);
        sEmojisMap.put(Symbols_7.DO_NOT_LITTER_SYMBOL, R.drawable.e1530);
        sEmojisMap.put(Symbols_7.NO_BI_CYCLES, R.drawable.e1534);
        sEmojisMap.put(Symbols_7.NON_POTABLE_WATER_SYMBOL, R.drawable.e1532);
        sEmojisMap.put(Symbols_7.NO_ONE_UNDER_EIGHTEEN_SYMBOL, R.drawable.e1239);
        //11
        sEmojisMap.put(Symbols_7.NO_MOBILE_PHONES, R.drawable.e1199);
        sEmojisMap.put(Symbols_7.HEAVY_EXCLAMATION_MARK_SYMBOL, R.drawable.e0183);
        sEmojisMap.put(Symbols_7.WHITE_EXCLAMATION_MARK_ORNAMENT, R.drawable.e0182);
        sEmojisMap.put(Symbols_7.BLACK_QUESTION_MARK_ORNAMENT, R.drawable.e0180);
        sEmojisMap.put(Symbols_7.WHITE_QUESTION_MARK_ORNAMENT, R.drawable.e0181);
        sEmojisMap.put(Symbols_7.DOUBLE_EXCLAMATION_MARK, R.drawable.e0014);
        sEmojisMap.put(Symbols_7.EXCLAMATION_QUESTION_MARK, R.drawable.e0015);
        sEmojisMap.put(Symbols_7.HUNDRED_POINTS_SYMBOL, R.drawable.e1129);
        //12
        sEmojisMap.put(Symbols_7.LOW_BRIGHTNESS_SYMBOL, R.drawable.e1214);
        sEmojisMap.put(Symbols_7.HIGH_BRIGHTNESS_SYMBOL, R.drawable.e1215);
        sEmojisMap.put(Symbols_7.TRIDENT_EMBLEM, R.drawable.e1258);
        sEmojisMap.put(Symbols_7.FLEUR_DE_LIS, R.drawable.e0104);
        sEmojisMap.put(Symbols_7.PART_ALTERNATION_MARK, R.drawable.e0202);
        sEmojisMap.put(Symbols_7.WARNING_SIGN, R.drawable.e0105);
        sEmojisMap.put(Symbols_7.CHILDREN_CROSSING, R.drawable.e1554);
        sEmojisMap.put(Symbols_7.JAPANESE_SYMBOL_FOR_BEGINNER, R.drawable.e1257);
        //13
        sEmojisMap.put(Symbols_7.BLACK_UNIVERSAL_RECYCLING_SYMBOL, R.drawable.e0095);
        sEmojisMap.put(Symbols_7.SQUARED_CJK_UNIFIED_IDEOGRAPH_6307, R.drawable.e0472);
        sEmojisMap.put(Symbols_7.CHART_WITH_UPWARDS_TREND_AND_YEN_SIGN, R.drawable.e1139);
        sEmojisMap.put(Symbols_7.SPARKLE, R.drawable.e0177);
        sEmojisMap.put(Symbols_7.EIGHT_SPOKED_ASTERISK, R.drawable.e0174);
        sEmojisMap.put(Symbols_7.NEGATIVE_SQUARED_CROSSMARK, R.drawable.e0179);
        sEmojisMap.put(Symbols_7.WHITE_HEAVY_CHECKMARK, R.drawable.e0140);
        sEmojisMap.put(Symbols_7.DIAMOND_SHAPE_WITH_A_DOT_INSIDE, R.drawable.e1109);
        //14
        sEmojisMap.put(Symbols_7.CYCLONE, R.drawable.e0484);
        sEmojisMap.put(Symbols_7.DOUBLE_CURLY_LOOP, R.drawable.e0191);
        sEmojisMap.put(Symbols_7.GLOBE_WITH_MERIDIANS, R.drawable.e0500);
        sEmojisMap.put(Symbols_7.CIRCLED_LATIN_CAPITAL_LETTER_M, R.drawable.e0043);
        sEmojisMap.put(Symbols_7.AUTOMATED_TELLER_MACHINE, R.drawable.e0738);
        sEmojisMap.put(Symbols_7.SQUARED_KATAKANASA, R.drawable.e0470);
        sEmojisMap.put(Symbols_7.PASSPORT_CONTROL, R.drawable.e1569);
        sEmojisMap.put(Symbols_7.CUSTOMS, R.drawable.e1570);
        //15
        sEmojisMap.put(Symbols_7.BAGGAGE_CLAIM, R.drawable.e1571);
        sEmojisMap.put(Symbols_7.LEFT_LUGGAGE, R.drawable.e1572);
        sEmojisMap.put(Symbols_7.WHEEL_CHAIR_SYMBOL, R.drawable.e0096);
        sEmojisMap.put(Symbols_7.NO_SMOKING_SYMBOL, R.drawable.e1528);
        sEmojisMap.put(Symbols_7.WATER_CLOSET, R.drawable.e1560);
        sEmojisMap.put(Symbols_7.NEGATIVE_SQUARED_LETTER_P, R.drawable.e0210);
        sEmojisMap.put(Symbols_7.POTABLE_WATER_SYMBOL, R.drawable.e1531);
        sEmojisMap.put(Symbols_7.MENS_SYMBOL, R.drawable.e1555);
        //16
        sEmojisMap.put(Symbols_7.WOMENS_SYMBOL, R.drawable.e1556);
        sEmojisMap.put(Symbols_7.BABY_SYMBOL, R.drawable.e1558);
        sEmojisMap.put(Symbols_7.RESTROOM, R.drawable.e1557);
        sEmojisMap.put(Symbols_7.PUT_LITTER_IN_ITS_PLACE, R.drawable.e1529);
        sEmojisMap.put(Symbols_7.CINEMA, R.drawable.e0648);
        sEmojisMap.put(Symbols_7.ANTENNA_WITH_BARS, R.drawable.e1200);
        sEmojisMap.put(Symbols_7.SQUARED_KATAKANA_KOKO, R.drawable.e0469);
        sEmojisMap.put(Symbols_7.SQUARED_NG, R.drawable.e0217);
        //17
        sEmojisMap.put(Symbols_7.SQUARED_OK, R.drawable.e0218);
        sEmojisMap.put(Symbols_7.SQUARED_EXCLAMATION_MARK, R.drawable.e0220);
        sEmojisMap.put(Symbols_7.SQUARED_COOL, R.drawable.e0213);
        sEmojisMap.put(Symbols_7.SQUARED_NEW, R.drawable.e0216);
        sEmojisMap.put(Symbols_7.SQUARED_FREE, R.drawable.e0214);
        //18
        sEmojisMap.put(Symbols_7.KEYCAP_TEN, R.drawable.e1240);
        //19
        sEmojisMap.put(Symbols_7.INPUT_SYMBOL_FOR_NUMBERS, R.drawable.e1243);
        sEmojisMap.put(Symbols_7.BLACK_RIGHT_POINTING_TRIANGLE, R.drawable.e0046);
        sEmojisMap.put(Symbols_7.DOUBLE_VERTICAL_BAR, R.drawable.e0040);
        sEmojisMap.put(Symbols_7.BLK_RGT_POINT_TRIANGLE_DBL_VERTICAL_BAR, R.drawable.e0035);
        sEmojisMap.put(Symbols_7.BLACK_SQUARE_FOR_STOP, R.drawable.e0041);
        sEmojisMap.put(Symbols_7.BLACK_CIRCLE_FOR_RECORD, R.drawable.e0042);
        sEmojisMap.put(Symbols_7.BLK_RGT_POINT_DBL_TRIANGLE_VERTICAL_BAR, R.drawable.e0033);
        sEmojisMap.put(Symbols_7.BLK_LFT_POINT_DBL_TRIANGLE_VERTICAL_BAR, R.drawable.e0034);
        //20
        sEmojisMap.put(Symbols_7.BLK_RGT_POINT_DBL_TRIANGLE, R.drawable.e0029);
        sEmojisMap.put(Symbols_7.BLK_LFT_POINT_DBL_TRIANGLE, R.drawable.e0030);
        sEmojisMap.put(Symbols_7.TWISTED_RIGHTWARDS_ARROWS, R.drawable.e1209);
        sEmojisMap.put(Symbols_7.CWISE_RGT_LFT_OPEN_CIRCLE_ARROW, R.drawable.e1210);
        sEmojisMap.put(Symbols_7.CWISE_RGT_LFT_OPEN_CIRCLE_ARROW_OVERLAY, R.drawable.e1211);
        sEmojisMap.put(Symbols_7.BLK_LFT_POINT_TRIANGLE, R.drawable.e0047);
        sEmojisMap.put(Symbols_7.UP_POINT_SMALL_RED_TRIANGLE, R.drawable.e1269);
        sEmojisMap.put(Symbols_7.DOWN_POINT_SMALL_RED_TRIANGLE, R.drawable.e1270);
        //21
        sEmojisMap.put(Symbols_7.BLK_UP_POINT_DOUBLE_TRIANGLE, R.drawable.e0031);
        sEmojisMap.put(Symbols_7.BLK_DOWN_POINT_DOUBLE_TRIANGLE, R.drawable.e0032);
        sEmojisMap.put(Symbols_7.BLACK_RIGHTWARDS_ARROW, R.drawable.e0189);
        sEmojisMap.put(Symbols_7.LEFTWARDS_BLACK_ARROW, R.drawable.e0194);
        sEmojisMap.put(Symbols_7.UPWARDS_BLACK_ARROW, R.drawable.e0195);
        sEmojisMap.put(Symbols_7.DOWNWARDS_BLACK_ARROW, R.drawable.e0196);
        sEmojisMap.put(Symbols_7.NORTHEAST_ARROW, R.drawable.e0021);
        sEmojisMap.put(Symbols_7.SOUTHEAST_ARROW, R.drawable.e0022);
        //22
        sEmojisMap.put(Symbols_7.SOUTH_WEST_ARROW, R.drawable.e0023);
        sEmojisMap.put(Symbols_7.NORTH_WEST_ARROW, R.drawable.e0020);
        sEmojisMap.put(Symbols_7.UP_DOWN_ARROW, R.drawable.e0019);
        sEmojisMap.put(Symbols_7.LEFT_RIGHT_ARROW, R.drawable.e0018);
        sEmojisMap.put(Symbols_7.ACWISE_DOWN_UP_OPEN_CIRCLE_ARROW, R.drawable.e1213);
        sEmojisMap.put(Symbols_7.RIGHTWARDS_ARROW_WITH_HOOK, R.drawable.e0025);
        sEmojisMap.put(Symbols_7.LEFTWARDS_ARROW_WITH_HOOK, R.drawable.e0024);
        sEmojisMap.put(Symbols_7.ARROW_POINT_RGT_THEN_CURVING_UP, R.drawable.e0192);
        //23
        sEmojisMap.put(Symbols_7.ARROW_POINT_RGT_THEN_CURVING_DOWN, R.drawable.e0193);
        sEmojisMap.put(Symbols_7.INFORMATION_SOURCE, R.drawable.e0017);
        sEmojisMap.put(Symbols_7.INPUT_SYMBOL_FOR_LATIN_LETTERS, R.drawable.e1245);
        sEmojisMap.put(Symbols_7.INPUT_SYMBOL_LATIN_SMALL_LETTERS, R.drawable.e1242);
        sEmojisMap.put(Symbols_7.INPUT_SYMBOL_LATIN_CAPITAL_LETTERS, R.drawable.e1241);
        sEmojisMap.put(Symbols_7.INPUT_SYMBOL_SYMBOLS, R.drawable.e1244);
        //24
        sEmojisMap.put(Symbols_7.MUSICAL_NOTE, R.drawable.e0663);
        sEmojisMap.put(Symbols_7.MULTIPLE_MUSICAL_NOTES, R.drawable.e0664);
        sEmojisMap.put(Symbols_7.WAVY_DASH, R.drawable.e0201);
        sEmojisMap.put(Symbols_7.CURLY_LOOP, R.drawable.e0190);
        sEmojisMap.put(Symbols_7.HEAVY_CHECK_MARK, R.drawable.e0169);
        sEmojisMap.put(Symbols_7.CWISE_DOWN_UP_OPEN_CIRCLE_ARROWS, R.drawable.e1212);
        sEmojisMap.put(Symbols_7.HEAVY_PLUS_SIGN, R.drawable.e0186);
        sEmojisMap.put(Symbols_7.HEAVY_MINUS_SIGN, R.drawable.e0187);
        //25
        sEmojisMap.put(Symbols_7.HEAVY_DIVISION_SIGN, R.drawable.e0188);
        sEmojisMap.put(Symbols_7.HEAVY_MULTIPLICATION_X, R.drawable.e0170);
        sEmojisMap.put(Symbols_7.HEAVY_DOLLAR_SIGN, R.drawable.e1132);
        sEmojisMap.put(Symbols_7.CURRENCY_EXCHANGE, R.drawable.e1131);
        sEmojisMap.put(Symbols_7.COPYRIGHT_SIGN, R.drawable.e0012);
        sEmojisMap.put(Symbols_7.REGISTERED_SIGN, R.drawable.e0013);
        sEmojisMap.put(Symbols_7.TRADEMARK_SIGN, R.drawable.e0016);
        sEmojisMap.put(Symbols_7.END_WITH_LFT_ARROW_ABOVE, R.drawable.e1235);
        //26
        sEmojisMap.put(Symbols_7.BACK_WITH_LFT_ARROW_ABOVE, R.drawable.e1234);
        sEmojisMap.put(Symbols_7.ON_EXCLAMATION_LFT_RGT_ARROW, R.drawable.e1236);
        sEmojisMap.put(Symbols_7.TOP_WITH_UP_ARROW_ABOVE, R.drawable.e1238);
        sEmojisMap.put(Symbols_7.SOON_RIGHT_ARROW_ABOVE, R.drawable.e1237);
        sEmojisMap.put(Symbols_7.BALLOT_BOX_WITH_CHECK, R.drawable.e0058);
        sEmojisMap.put(Symbols_7.RADIO_BUTTON, R.drawable.e1233);
        sEmojisMap.put(Symbols_7.MEDIUM_WHITE_CIRCLE, R.drawable.e0107);
        sEmojisMap.put(Symbols_7.MEDIUM_BLACK_CIRCLE, R.drawable.e0108);
        //27
        sEmojisMap.put(Symbols_7.LARGE_RED_CIRCLE, R.drawable.e1261);
        sEmojisMap.put(Symbols_7.LARGE_BLUE_CIRCLE, R.drawable.e1262);
        sEmojisMap.put(Symbols_7.SMALL_ORANGE_DIAMOND, R.drawable.e1265);
        sEmojisMap.put(Symbols_7.SMALL_BLUE_DIAMOND, R.drawable.e1266);
        sEmojisMap.put(Symbols_7.LARGE_ORANGE_DIAMOND, R.drawable.e1263);
        sEmojisMap.put(Symbols_7.LARGE_BLUE_DIAMOND, R.drawable.e1264);
        sEmojisMap.put(Symbols_7.UP_POINT_RED_TRIANGLE, R.drawable.e1267);
        sEmojisMap.put(Symbols_7.BLACK_SMALL_SQUARE, R.drawable.e0044);
        //28
        sEmojisMap.put(Symbols_7.WHITE_SMALL_SQUARE, R.drawable.e0045);
        sEmojisMap.put(Symbols_7.BLACK_LARGE_SQUARE, R.drawable.e0197);
        sEmojisMap.put(Symbols_7.WHITE_LARGE_SQUARE, R.drawable.e0198);
        sEmojisMap.put(Symbols_7.DOWN_POINT_RED_TRIANGLE, R.drawable.e1268);
        sEmojisMap.put(Symbols_7.BLACK_MEDIUM_SQUARE, R.drawable.e0049);
        sEmojisMap.put(Symbols_7.WHITE_MEDIUM_SQUARE, R.drawable.e0048);
        sEmojisMap.put(Symbols_7.BLACK_MEDIUM_SMALL_SQUARE, R.drawable.e0051);
        sEmojisMap.put(Symbols_7.WHITE_MEDIUM_SMALL_SQUARE, R.drawable.e0050);
        //29
        sEmojisMap.put(Symbols_7.BLACK_SQUARE_BUTTON, R.drawable.e1259);
        sEmojisMap.put(Symbols_7.WHITE_SQUARE_BUTTON, R.drawable.e1260);
        sEmojisMap.put(Symbols_7.SPEAKER, R.drawable.e1217);
        sEmojisMap.put(Symbols_7.SPEAKER_ONE_SOUND_WAVE, R.drawable.e1218);
        sEmojisMap.put(Symbols_7.SPEAKER_THREE_SOUND_WAVES, R.drawable.e1219);
        sEmojisMap.put(Symbols_7.SPEAKER_CANCELLATION_STROKE, R.drawable.e1216);
        sEmojisMap.put(Symbols_7.CHEERING_MEGAPHONE, R.drawable.e1181);
        sEmojisMap.put(Symbols_7.PUBLIC_ADDRESS_LOUDSPEAKER, R.drawable.e1180);
        //30
        sEmojisMap.put(Symbols_7.BELL, R.drawable.e1229);
        sEmojisMap.put(Symbols_7.BELL_WITH_CANCELLATION_STROKE, R.drawable.e1230);
        sEmojisMap.put(Symbols_7.PLAYING_CARD_BLACK_JOKER, R.drawable.e0206);
        sEmojisMap.put(Symbols_7.MAHJONG_TILE_RED_DRAGON, R.drawable.e0205);
        sEmojisMap.put(Symbols_7.BLACK_SPADE_SUIT, R.drawable.e0090);
        sEmojisMap.put(Symbols_7.BLACK_CLUB_SUIT, R.drawable.e0091);
        sEmojisMap.put(Symbols_7.BLACK_HEART_SUIT, R.drawable.e0092);
        sEmojisMap.put(Symbols_7.BLACK_DIAMOND_SUIT, R.drawable.e0093);
        //31
        sEmojisMap.put(Symbols_7.FLOWER_PLAYING_CARDS, R.drawable.e0662);
        sEmojisMap.put(Symbols_7.THOUGHT_BALLOON, R.drawable.e1127);
        sEmojisMap.put(Symbols_7.RIGHT_ANGER_BUBBLE, R.drawable.e1350);
        sEmojisMap.put(Symbols_7.SPEECH_BALLOON, R.drawable.e1126);
        sEmojisMap.put(Symbols_7.CLOCK_FACE_ONE_O_CLOCK, R.drawable.e1277);
        sEmojisMap.put(Symbols_7.CLOCK_FACE_TWO_O_CLOCK, R.drawable.e1278);
        sEmojisMap.put(Symbols_7.CLOCK_FACE_THREE_O_CLOCK, R.drawable.e1279);
        //32
        sEmojisMap.put(Symbols_7.CLOCK_FACE_FOUR_O_CLOCK, R.drawable.e1280);
        sEmojisMap.put(Symbols_7.CLOCK_FACE_FIVE_O_CLOCK, R.drawable.e1281);
        sEmojisMap.put(Symbols_7.CLOCK_FACE_SIX_O_CLOCK, R.drawable.e1282);
        sEmojisMap.put(Symbols_7.CLOCK_FACE_SEVEN_O_CLOCK, R.drawable.e1283);
        sEmojisMap.put(Symbols_7.CLOCK_FACE_EIGHT_O_CLOCK, R.drawable.e1284);
        sEmojisMap.put(Symbols_7.CLOCK_FACE_NINE_O_CLOCK, R.drawable.e1285);
        sEmojisMap.put(Symbols_7.CLOCK_FACE_TEN_O_CLOCK, R.drawable.e1286);
        sEmojisMap.put(Symbols_7.CLOCK_FACE_ELEVEN_O_CLOCK, R.drawable.e1287);
        //33
        sEmojisMap.put(Symbols_7.CLOCK_FACE_TWELVE_O_CLOCK, R.drawable.e1288);
        sEmojisMap.put(Symbols_7.CLOCK_FACE_ONE_THIRTY, R.drawable.e1289);
        sEmojisMap.put(Symbols_7.CLOCK_FACE_TWO_THIRTY, R.drawable.e1290);
        sEmojisMap.put(Symbols_7.CLOCK_FACE_THREE_THIRTY, R.drawable.e1291);
        sEmojisMap.put(Symbols_7.CLOCK_FACE_FOUR_THIRTY, R.drawable.e1292);
        sEmojisMap.put(Symbols_7.CLOCK_FACE_FIVE_THIRTY, R.drawable.e1293);
        sEmojisMap.put(Symbols_7.CLOCK_FACE_SIX_THIRTY, R.drawable.e1294);
        sEmojisMap.put(Symbols_7.CLOCK_FACE_SEVEN_THIRTY, R.drawable.e1295);
        //34
        sEmojisMap.put(Symbols_7.CLOCK_FACE_EIGHT_THIRTY, R.drawable.e1296);
        sEmojisMap.put(Symbols_7.CLOCK_FACE_NINE_THIRTY, R.drawable.e1297);
        sEmojisMap.put(Symbols_7.CLOCK_FACE_TEN_THIRTY, R.drawable.e1298);
        sEmojisMap.put(Symbols_7.CLOCK_FACE_ELEVEN_THIRTY, R.drawable.e1299);
        sEmojisMap.put(Symbols_7.CLOCK_FACE_TWELVE_THIRTY, R.drawable.e1300);
    }


//__________________________________________________________________________________________________
    private static int get1Emoji(int codePoint) {
        return sEmojisMap.get(codePoint);
    }

    private static int getSymbol2Emoji(int[] codePoints) {
        if(Arrays.equals(codePoints, Symbols_7.KEYCAP_DIGIT_ZERO)) return R.drawable.e0002;
        else if(Arrays.equals(codePoints, Symbols_7.KEYCAP_DIGIT_ONE)) return R.drawable.e0003;
        else if(Arrays.equals(codePoints, Symbols_7.KEYCAP_DIGIT_TWO)) return R.drawable.e0004;
        else if(Arrays.equals(codePoints, Symbols_7.KEYCAP_DIGIT_THREE)) return R.drawable.e0005;
        else if(Arrays.equals(codePoints, Symbols_7.KEYCAP_DIGIT_FOUR)) return R.drawable.e0006;
        else if(Arrays.equals(codePoints, Symbols_7.KEYCAP_DIGIT_FIVE)) return R.drawable.e0007;
        else if(Arrays.equals(codePoints, Symbols_7.KEYCAP_DIGIT_SIX)) return R.drawable.e0008;
        else if(Arrays.equals(codePoints, Symbols_7.KEYCAP_DIGIT_SEVEN)) return R.drawable.e0009;
        else if(Arrays.equals(codePoints, Symbols_7.KEYCAP_DIGIT_EIGHT)) return R.drawable.e0010;
        else if(Arrays.equals(codePoints, Symbols_7.KEYCAP_DIGIT_NINE)) return R.drawable.e0011;
        else if(Arrays.equals(codePoints, Symbols_7.KEYCAP_NUMBER_SIGN)) return R.drawable.e0000;
        else if(Arrays.equals(codePoints, Symbols_7.KEYCAP_ASTERISK)) return R.drawable.e0001;
        return 0;
    }

    //TODO can be done more efficiently by using custom HashMap and implementing .equals() function
    //http://stackoverflow.com/questions/14614928/comparing-integer-arrays-in-java-why-does-not-work
    private static int getFlag2Emoji(int[] codePoints) {
        if(Arrays.equals(codePoints, Flags_8.AFGHANISTAN)) return R.drawable.e0224;
        else if(Arrays.equals(codePoints, Flags_8.LAND_ISLAND)) return R.drawable.e0236;
        else if(Arrays.equals(codePoints, Flags_8.ALBANIA)) return R.drawable.e0227;
        else if(Arrays.equals(codePoints, Flags_8.ALGERIA)) return R.drawable.e0282;
        else if(Arrays.equals(codePoints, Flags_8.AMERICAN_SAMOA)) return R.drawable.e0232;
        else if(Arrays.equals(codePoints, Flags_8.ANDORRA)) return R.drawable.e0222;
        else if(Arrays.equals(codePoints, Flags_8.ANGOLA)) return R.drawable.e0229;
        else if(Arrays.equals(codePoints, Flags_8.ANGUILLA)) return R.drawable.e0226;

        else if(Arrays.equals(codePoints, Flags_8.ANTARCTICA)) return R.drawable.e0230;
        else if(Arrays.equals(codePoints, Flags_8.ANTIGUA_AND_BARBUDA)) return R.drawable.e0225;
        else if(Arrays.equals(codePoints, Flags_8.ARGENTINA)) return R.drawable.e0231;
        else if(Arrays.equals(codePoints, Flags_8.ARMENIA)) return R.drawable.e0228;
        else if(Arrays.equals(codePoints, Flags_8.ARUBA)) return R.drawable.e0235;
        else if(Arrays.equals(codePoints, Flags_8.AUSTRALIA)) return R.drawable.e0234;
        else if(Arrays.equals(codePoints, Flags_8.AUSTRIA)) return R.drawable.e0233;
        else if(Arrays.equals(codePoints, Flags_8.AZERBAIJAN)) return R.drawable.e0237;

        else if(Arrays.equals(codePoints, Flags_8.BAHAMAS)) return R.drawable.e0253;
        else if(Arrays.equals(codePoints, Flags_8.BAHRAIN)) return R.drawable.e0244;
        else if(Arrays.equals(codePoints, Flags_8.BANGLADESH)) return R.drawable.e0240;
        else if(Arrays.equals(codePoints, Flags_8.BARBADOS)) return R.drawable.e0239;
        else if(Arrays.equals(codePoints, Flags_8.BELARUS)) return R.drawable.e0256;
        else if(Arrays.equals(codePoints, Flags_8.BELGIUM)) return R.drawable.e0241;
        else if(Arrays.equals(codePoints, Flags_8.BELIZE)) return R.drawable.e0257;
        else if(Arrays.equals(codePoints, Flags_8.BENIN)) return R.drawable.e0246;

        else if(Arrays.equals(codePoints, Flags_8.BERMUDA)) return R.drawable.e0248;
        else if(Arrays.equals(codePoints, Flags_8.BHUTAN)) return R.drawable.e0254;
        else if(Arrays.equals(codePoints, Flags_8.BOLIVIA)) return R.drawable.e0250;
        else if(Arrays.equals(codePoints, Flags_8.CARIBBEAN_NETHERLANDS)) return R.drawable.e0251;
        else if(Arrays.equals(codePoints, Flags_8.BOSNIA_AND_HERZEGOVINA)) return R.drawable.e0238;
        else if(Arrays.equals(codePoints, Flags_8.BOTSWANA)) return R.drawable.e0255;
        else if(Arrays.equals(codePoints, Flags_8.BRAZIL)) return R.drawable.e0252;
        else if(Arrays.equals(codePoints, Flags_8.BRITISH_INDIAN_OCEAN_TERRITORY)) return R.drawable.e0327;

        else if(Arrays.equals(codePoints, Flags_8.BRITISH_VIRGIN_ISLANDS)) return R.drawable.e0457;
        else if(Arrays.equals(codePoints, Flags_8.BRUNEI)) return R.drawable.e0249;
        else if(Arrays.equals(codePoints, Flags_8.BULGARIA)) return R.drawable.e0243;
        else if(Arrays.equals(codePoints, Flags_8.BURKINA_FASO)) return R.drawable.e0242;
        else if(Arrays.equals(codePoints, Flags_8.BURUNDI)) return R.drawable.e0245;
        else if(Arrays.equals(codePoints, Flags_8.CAPE_VERDE)) return R.drawable.e0272;
        else if(Arrays.equals(codePoints, Flags_8.CAMBODIA)) return R.drawable.e0338;
        else if(Arrays.equals(codePoints, Flags_8.CAMEROON)) return R.drawable.e0267;

        else if(Arrays.equals(codePoints, Flags_8.CANADA)) return R.drawable.e0258;
        else if(Arrays.equals(codePoints, Flags_8.CANARY_ISLANDS)) return R.drawable.e0321;
        else if(Arrays.equals(codePoints, Flags_8.CAYMAN_ISLANDS)) return R.drawable.e0345;
        else if(Arrays.equals(codePoints, Flags_8.CENTRAL_AFRICAN_REPUBLIC)) return R.drawable.e0261;
        else if(Arrays.equals(codePoints, Flags_8.CHAD)) return R.drawable.e0409;
        else if(Arrays.equals(codePoints, Flags_8.CHILE)) return R.drawable.e0266;
        else if(Arrays.equals(codePoints, Flags_8.CHINA)) return R.drawable.e0268;
        else if(Arrays.equals(codePoints, Flags_8.CHRISTMAS_ISLAND)) return R.drawable.e0274;

        else if(Arrays.equals(codePoints, Flags_8.COCOS_KEELING_ISLAND)) return R.drawable.e0259;
        else if(Arrays.equals(codePoints, Flags_8.COLOMBIA)) return R.drawable.e0269;
        else if(Arrays.equals(codePoints, Flags_8.COMOROS)) return R.drawable.e0340;
        else if(Arrays.equals(codePoints, Flags_8.CONGO_BRAZZAVILLE)) return R.drawable.e0262;
        else if(Arrays.equals(codePoints, Flags_8.CONGO_KINGSHASA)) return R.drawable.e0260;
        else if(Arrays.equals(codePoints, Flags_8.COOK_ISLANDS)) return R.drawable.e0265;
        else if(Arrays.equals(codePoints, Flags_8.COSTA_RICA)) return R.drawable.e0270;
        else if(Arrays.equals(codePoints, Flags_8.CROATIA)) return R.drawable.e0318;

        else if(Arrays.equals(codePoints, Flags_8.CUBA)) return R.drawable.e0271;
        else if(Arrays.equals(codePoints, Flags_8.CURAAO)) return R.drawable.e0273;
        else if(Arrays.equals(codePoints, Flags_8.CYPRUS)) return R.drawable.e0275;
        else if(Arrays.equals(codePoints, Flags_8.CZECH_REPUBLIC)) return R.drawable.e0276;
        else if(Arrays.equals(codePoints, Flags_8.DENMARK)) return R.drawable.e0279;
        else if(Arrays.equals(codePoints, Flags_8.DJIBOUTI)) return R.drawable.e0278;
        else if(Arrays.equals(codePoints, Flags_8.DOMINICA)) return R.drawable.e0280;
        else if(Arrays.equals(codePoints, Flags_8.DOMINICAN_REPUBLIC)) return R.drawable.e0281;

        else if(Arrays.equals(codePoints, Flags_8.ECUADOR)) return R.drawable.e0283;
        else if(Arrays.equals(codePoints, Flags_8.EGYPT)) return R.drawable.e0285;
        else if(Arrays.equals(codePoints, Flags_8.EL_SALVADOR)) return R.drawable.e0429;
        else if(Arrays.equals(codePoints, Flags_8.EQUATORIAL_GUINEA)) return R.drawable.e0309;
        else if(Arrays.equals(codePoints, Flags_8.ERITREA)) return R.drawable.e0287;
        else if(Arrays.equals(codePoints, Flags_8.ESTONIA)) return R.drawable.e0284;
        else if(Arrays.equals(codePoints, Flags_8.ETHIOPIA)) return R.drawable.e0289;
        else if(Arrays.equals(codePoints, Flags_8.EUROPEAN_UNION)) return R.drawable.e0290;

        else if(Arrays.equals(codePoints, Flags_8.FALKLAND_ISLANDS)) return R.drawable.e0293;
        else if(Arrays.equals(codePoints, Flags_8.FAROE_ISLANDS)) return R.drawable.e0295;
        else if(Arrays.equals(codePoints, Flags_8.FIJI)) return R.drawable.e0292;
        else if(Arrays.equals(codePoints, Flags_8.FINLAND)) return R.drawable.e0291;
        else if(Arrays.equals(codePoints, Flags_8.FRANCE)) return R.drawable.e0296;
        else if(Arrays.equals(codePoints, Flags_8.FRENCH_GUIANA)) return R.drawable.e0301;
        else if(Arrays.equals(codePoints, Flags_8.FRENCH_POLYNESIA)) return R.drawable.e0395;
        else if(Arrays.equals(codePoints, Flags_8.FRENCH_SOUTHERN_TERRITORIES)) return R.drawable.e0435;

        else if(Arrays.equals(codePoints, Flags_8.GABON)) return R.drawable.e0297;
        else if(Arrays.equals(codePoints, Flags_8.GAMBIA)) return R.drawable.e0306;
        else if(Arrays.equals(codePoints, Flags_8.GEORGIA)) return R.drawable.e0300;
        else if(Arrays.equals(codePoints, Flags_8.GERMANY)) return R.drawable.e0277;
        else if(Arrays.equals(codePoints, Flags_8.GHANA)) return R.drawable.e0303;
        else if(Arrays.equals(codePoints, Flags_8.GIBRALTAR)) return R.drawable.e0304;
        else if(Arrays.equals(codePoints, Flags_8.GREECE)) return R.drawable.e0310;
        else if(Arrays.equals(codePoints, Flags_8.GREENLAND)) return R.drawable.e0305;

        else if(Arrays.equals(codePoints, Flags_8.GRENADA)) return R.drawable.e0299;
        else if(Arrays.equals(codePoints, Flags_8.GUADELOUPE)) return R.drawable.e0308;
        else if(Arrays.equals(codePoints, Flags_8.GUAM)) return R.drawable.e0313;
        else if(Arrays.equals(codePoints, Flags_8.GUATEMALA)) return R.drawable.e0312;
        else if(Arrays.equals(codePoints, Flags_8.GUERNSEY)) return R.drawable.e0302;
        else if(Arrays.equals(codePoints, Flags_8.GUINEA)) return R.drawable.e0307;
        else if(Arrays.equals(codePoints, Flags_8.GUINEA_BISSAU)) return R.drawable.e0314;
        else if(Arrays.equals(codePoints, Flags_8.GUYANA)) return R.drawable.e0315;

        else if(Arrays.equals(codePoints, Flags_8.HAITI)) return R.drawable.e0319;
        else if(Arrays.equals(codePoints, Flags_8.HONDURAS)) return R.drawable.e0317;
        else if(Arrays.equals(codePoints, Flags_8.HONG_KONG)) return R.drawable.e0316;
        else if(Arrays.equals(codePoints, Flags_8.HUNGARY)) return R.drawable.e0320;
        else if(Arrays.equals(codePoints, Flags_8.ICELAND)) return R.drawable.e0330;
        else if(Arrays.equals(codePoints, Flags_8.INDIA)) return R.drawable.e0326;
        else if(Arrays.equals(codePoints, Flags_8.INDONESIA)) return R.drawable.e0322;
        else if(Arrays.equals(codePoints, Flags_8.IRAN)) return R.drawable.e0329;

        else if(Arrays.equals(codePoints, Flags_8.IRAQ)) return R.drawable.e0328;
        else if(Arrays.equals(codePoints, Flags_8.IRELAND)) return R.drawable.e0323;
        else if(Arrays.equals(codePoints, Flags_8.ISLE_OF_MAN)) return R.drawable.e0325;
        else if(Arrays.equals(codePoints, Flags_8.ISRAEL)) return R.drawable.e0324;
        else if(Arrays.equals(codePoints, Flags_8.ITALY)) return R.drawable.e0331;
        else if(Arrays.equals(codePoints, Flags_8.CTEDIVOIRE)) return R.drawable.e0264;
        else if(Arrays.equals(codePoints, Flags_8.JAMAICA)) return R.drawable.e0333;
        else if(Arrays.equals(codePoints, Flags_8.JAPAN)) return R.drawable.e0335;

        else if(Arrays.equals(codePoints, Flags_8.JERSEY)) return R.drawable.e0332;
        else if(Arrays.equals(codePoints, Flags_8.JORDAN)) return R.drawable.e0334;
        else if(Arrays.equals(codePoints, Flags_8.KAZAKHSTAN)) return R.drawable.e0346;
        else if(Arrays.equals(codePoints, Flags_8.KENYA)) return R.drawable.e0336;
        else if(Arrays.equals(codePoints, Flags_8.KIRIBATI)) return R.drawable.e0339;
        else if(Arrays.equals(codePoints, Flags_8.KOSOVO)) return R.drawable.e0463;
        else if(Arrays.equals(codePoints, Flags_8.KUWAIT)) return R.drawable.e0344;
        else if(Arrays.equals(codePoints, Flags_8.KYRGYZSTAN)) return R.drawable.e0337;

        else if(Arrays.equals(codePoints, Flags_8.LAOS)) return R.drawable.e0347;
        else if(Arrays.equals(codePoints, Flags_8.LATVIA)) return R.drawable.e0356;
        else if(Arrays.equals(codePoints, Flags_8.LEBANON)) return R.drawable.e0348;
        else if(Arrays.equals(codePoints, Flags_8.LESOTHO)) return R.drawable.e0353;
        else if(Arrays.equals(codePoints, Flags_8.LIBERIA)) return R.drawable.e0352;
        else if(Arrays.equals(codePoints, Flags_8.LIBYA)) return R.drawable.e0357;
        else if(Arrays.equals(codePoints, Flags_8.LIECHTENSTEIN)) return R.drawable.e0350;
        else if(Arrays.equals(codePoints, Flags_8.LITHUANIA)) return R.drawable.e0354;

        else if(Arrays.equals(codePoints, Flags_8.LUXEMBOURG)) return R.drawable.e0355;
        else if(Arrays.equals(codePoints, Flags_8.MACAU)) return R.drawable.e0368;
        else if(Arrays.equals(codePoints, Flags_8.MACEDONIA)) return R.drawable.e0364;
        else if(Arrays.equals(codePoints, Flags_8.MADAGASCAR)) return R.drawable.e0362;
        else if(Arrays.equals(codePoints, Flags_8.MALAWI)) return R.drawable.e0376;
        else if(Arrays.equals(codePoints, Flags_8.MALAYSIA)) return R.drawable.e0378;
        else if(Arrays.equals(codePoints, Flags_8.MALDIVES)) return R.drawable.e0375;
        else if(Arrays.equals(codePoints, Flags_8.MALI)) return R.drawable.e0365;

        else if(Arrays.equals(codePoints, Flags_8.MALTA)) return R.drawable.e0373;
        else if(Arrays.equals(codePoints, Flags_8.MARSHALL_ISLANDS)) return R.drawable.e0363;
        else if(Arrays.equals(codePoints, Flags_8.MARTINIQUE)) return R.drawable.e0370;
        else if(Arrays.equals(codePoints, Flags_8.MAURITANIA)) return R.drawable.e0371;
        else if(Arrays.equals(codePoints, Flags_8.MAURITIUS)) return R.drawable.e0374;
        else if(Arrays.equals(codePoints, Flags_8.MAYOTTE)) return R.drawable.e0465;
        else if(Arrays.equals(codePoints, Flags_8.MEXICO)) return R.drawable.e0377;
        else if(Arrays.equals(codePoints, Flags_8.MICRONESIA)) return R.drawable.e0294;

        else if(Arrays.equals(codePoints, Flags_8.MOLDOVA)) return R.drawable.e0360;
        else if(Arrays.equals(codePoints, Flags_8.MONACO)) return R.drawable.e0359;
        else if(Arrays.equals(codePoints, Flags_8.MONGOLIA)) return R.drawable.e0367;
        else if(Arrays.equals(codePoints, Flags_8.MONTENEGRO)) return R.drawable.e0361;
        else if(Arrays.equals(codePoints, Flags_8.MONTSERRAT)) return R.drawable.e0372;
        else if(Arrays.equals(codePoints, Flags_8.MOROCCO)) return R.drawable.e0358;
        else if(Arrays.equals(codePoints, Flags_8.MOZAMBIQUE)) return R.drawable.e0379;
        else if(Arrays.equals(codePoints, Flags_8.MYANMAR_BURMA)) return R.drawable.e0366;

        else if(Arrays.equals(codePoints, Flags_8.NAMIBIA)) return R.drawable.e0380;
        else if(Arrays.equals(codePoints, Flags_8.NAURU)) return R.drawable.e0389;
        else if(Arrays.equals(codePoints, Flags_8.NEPAL)) return R.drawable.e0388;
        else if(Arrays.equals(codePoints, Flags_8.NETHERLANDS)) return R.drawable.e0386;
        else if(Arrays.equals(codePoints, Flags_8.NEW_CALEDONIA)) return R.drawable.e0381;
        else if(Arrays.equals(codePoints, Flags_8.NEW_ZEALAND)) return R.drawable.e0391;
        else if(Arrays.equals(codePoints, Flags_8.NICARAGUA)) return R.drawable.e0385;
        else if(Arrays.equals(codePoints, Flags_8.NIGER)) return R.drawable.e0382;

        else if(Arrays.equals(codePoints, Flags_8.NIGERIA)) return R.drawable.e0384;
        else if(Arrays.equals(codePoints, Flags_8.NIUE)) return R.drawable.e0390;
        else if(Arrays.equals(codePoints, Flags_8.NORFOLK_ISLAND)) return R.drawable.e0383;
        else if(Arrays.equals(codePoints, Flags_8.NORTHERN_MARIANA_ISLANDS)) return R.drawable.e0369;
        else if(Arrays.equals(codePoints, Flags_8.NORTH_KOREA)) return R.drawable.e0342;
        else if(Arrays.equals(codePoints, Flags_8.NORWAY)) return R.drawable.e0387;
        else if(Arrays.equals(codePoints, Flags_8.OMAN)) return R.drawable.e0392;
        else if(Arrays.equals(codePoints, Flags_8.PAKISTAN)) return R.drawable.e0398;

        else if(Arrays.equals(codePoints, Flags_8.PALAU)) return R.drawable.e0405;
        else if(Arrays.equals(codePoints, Flags_8.PALESTINIAN_TERRITORIES)) return R.drawable.e0403;
        else if(Arrays.equals(codePoints, Flags_8.PANAMA)) return R.drawable.e0393;
        else if(Arrays.equals(codePoints, Flags_8.PAPUA_NEW_GUINEA)) return R.drawable.e0396;
        else if(Arrays.equals(codePoints, Flags_8.PARAGUAY)) return R.drawable.e0406;
        else if(Arrays.equals(codePoints, Flags_8.PERU)) return R.drawable.e0394;
        else if(Arrays.equals(codePoints, Flags_8.PHILIPPINES)) return R.drawable.e0397;
        else if(Arrays.equals(codePoints, Flags_8.PITCAIRN_ISLANDS)) return R.drawable.e0401;

        else if(Arrays.equals(codePoints, Flags_8.POLAND)) return R.drawable.e0399;
        else if(Arrays.equals(codePoints, Flags_8.PORTUGAL)) return R.drawable.e0404;
        else if(Arrays.equals(codePoints, Flags_8.PUERTO_RICO)) return R.drawable.e0402;
        else if(Arrays.equals(codePoints, Flags_8.QATAR)) return R.drawable.e0407;
        else if(Arrays.equals(codePoints, Flags_8.REUNION)) return R.drawable.e0408;
        else if(Arrays.equals(codePoints, Flags_8.ROMANIA)) return R.drawable.e0409;
        else if(Arrays.equals(codePoints, Flags_8.RUSSIA)) return R.drawable.e0411;
        else if(Arrays.equals(codePoints, Flags_8.RWANDA)) return R.drawable.e0412;

        else if(Arrays.equals(codePoints, Flags_8.SAINT_BARTHLEMY)) return R.drawable.e0247;
        else if(Arrays.equals(codePoints, Flags_8.SAINT_HELENA)) return R.drawable.e0419;
        else if(Arrays.equals(codePoints, Flags_8.SAINT_KITTS_AND_NEVIS)) return R.drawable.e0341;
        else if(Arrays.equals(codePoints, Flags_8.SAINT_LUCIA)) return R.drawable.e0349;
        else if(Arrays.equals(codePoints, Flags_8.SAINT_PIERRE_AND_MIQUELON)) return R.drawable.e0400;
        else if(Arrays.equals(codePoints, Flags_8.ST_VINCENT_GRENADINES)) return R.drawable.e0455;
        else if(Arrays.equals(codePoints, Flags_8.SAMOA)) return R.drawable.e0462;
        else if(Arrays.equals(codePoints, Flags_8.SAN_MARINO)) return R.drawable.e0423;

        else if(Arrays.equals(codePoints, Flags_8.SOTOM_AND_PRNCIPE)) return R.drawable.e0428;
        else if(Arrays.equals(codePoints, Flags_8.SAUDI_ARABIA)) return R.drawable.e0413;
        else if(Arrays.equals(codePoints, Flags_8.SENEGAL)) return R.drawable.e0424;
        else if(Arrays.equals(codePoints, Flags_8.SERBIA)) return R.drawable.e0410;
        else if(Arrays.equals(codePoints, Flags_8.SEYCHELLES)) return R.drawable.e0415;
        else if(Arrays.equals(codePoints, Flags_8.SIERRA_LEONE)) return R.drawable.e0422;
        else if(Arrays.equals(codePoints, Flags_8.SINGAPORE)) return R.drawable.e0418;
        else if(Arrays.equals(codePoints, Flags_8.SINT_MAARTEN)) return R.drawable.e0430;

        else if(Arrays.equals(codePoints, Flags_8.SLOVAKIA)) return R.drawable.e0421;
        else if(Arrays.equals(codePoints, Flags_8.SLOVENIA)) return R.drawable.e0420;
        else if(Arrays.equals(codePoints, Flags_8.SOLOMON_ISLANDS)) return R.drawable.e0414;
        else if(Arrays.equals(codePoints, Flags_8.SOMALIA)) return R.drawable.e0425;
        else if(Arrays.equals(codePoints, Flags_8.SOUTH_AFRICA)) return R.drawable.e0466;
        else if(Arrays.equals(codePoints, Flags_8.SOUTH_GEORGIA_SOUTH_SANDWICH_ISLANDS)) return R.drawable.e0311;
        else if(Arrays.equals(codePoints, Flags_8.SOUTH_KOREA)) return R.drawable.e0343;
        else if(Arrays.equals(codePoints, Flags_8.SOUTH_SUDAN)) return R.drawable.e0427;

        else if(Arrays.equals(codePoints, Flags_8.SPAIN)) return R.drawable.e0288;
        else if(Arrays.equals(codePoints, Flags_8.SRI_LANKA)) return R.drawable.e0351;
        else if(Arrays.equals(codePoints, Flags_8.SUDAN)) return R.drawable.e0416;
        else if(Arrays.equals(codePoints, Flags_8.SURINAME)) return R.drawable.e0426;
        else if(Arrays.equals(codePoints, Flags_8.SWAZILAND)) return R.drawable.e0432;
        else if(Arrays.equals(codePoints, Flags_8.SWEDEN)) return R.drawable.e0417;
        else if(Arrays.equals(codePoints, Flags_8.SWITZERLAND)) return R.drawable.e0263;
        else if(Arrays.equals(codePoints, Flags_8.SYRIA)) return R.drawable.e0431;

        else if(Arrays.equals(codePoints, Flags_8.TAIWAN)) return R.drawable.e0447;
        else if(Arrays.equals(codePoints, Flags_8.TAJIKISTAN)) return R.drawable.e0438;
        else if(Arrays.equals(codePoints, Flags_8.TANZANIA)) return R.drawable.e0448;
        else if(Arrays.equals(codePoints, Flags_8.THAILAND)) return R.drawable.e0437;
        else if(Arrays.equals(codePoints, Flags_8.TIMORLESTE)) return R.drawable.e0440;
        else if(Arrays.equals(codePoints, Flags_8.TOGO)) return R.drawable.e0436;
        else if(Arrays.equals(codePoints, Flags_8.TOKELAU)) return R.drawable.e0439;
        else if(Arrays.equals(codePoints, Flags_8.TONGA)) return R.drawable.e0443;

        else if(Arrays.equals(codePoints, Flags_8.TRINIDAD_AND_TOBAGO)) return R.drawable.e0445;
        else if(Arrays.equals(codePoints, Flags_8.TUNISIA)) return R.drawable.e0442;
        else if(Arrays.equals(codePoints, Flags_8.TURKEY)) return R.drawable.e0444;
        else if(Arrays.equals(codePoints, Flags_8.TURKMENISTAN)) return R.drawable.e0441;
        else if(Arrays.equals(codePoints, Flags_8.TURKS_AND_CAICOS_ISLANDS)) return R.drawable.e0433;
        else if(Arrays.equals(codePoints, Flags_8.TUVALU)) return R.drawable.e0446;
        else if(Arrays.equals(codePoints, Flags_8.UGANDA)) return R.drawable.e0450;
        else if(Arrays.equals(codePoints, Flags_8.UKRAINE)) return R.drawable.e0449;

        else if(Arrays.equals(codePoints, Flags_8.UNITED_ARAB_EMIRATES)) return R.drawable.e0223;
        else if(Arrays.equals(codePoints, Flags_8.UNITED_KINGDOM)) return R.drawable.e0298;
        else if(Arrays.equals(codePoints, Flags_8.UNITED_STATES)) return R.drawable.e0451;
        else if(Arrays.equals(codePoints, Flags_8.US_VIRGIN_ISLANDS)) return R.drawable.e0458;
        else if(Arrays.equals(codePoints, Flags_8.URUGUAY)) return R.drawable.e0452;
        else if(Arrays.equals(codePoints, Flags_8.UZBEKISTAN)) return R.drawable.e0453;
        else if(Arrays.equals(codePoints, Flags_8.VANUATU)) return R.drawable.e0460;
        else if(Arrays.equals(codePoints, Flags_8.VATICAN_CITY)) return R.drawable.e0454;

        else if(Arrays.equals(codePoints, Flags_8.VENEZUELA)) return R.drawable.e0456;
        else if(Arrays.equals(codePoints, Flags_8.VIETNAM)) return R.drawable.e0459;
        else if(Arrays.equals(codePoints, Flags_8.WALLIS_AND_FUTUNA)) return R.drawable.e0461;
        else if(Arrays.equals(codePoints, Flags_8.WESTERN_SAHARA)) return R.drawable.e0286;
        else if(Arrays.equals(codePoints, Flags_8.YEMEN)) return R.drawable.e0464;
        else if(Arrays.equals(codePoints, Flags_8.ZAMBIA)) return R.drawable.e0467;
        else if(Arrays.equals(codePoints, Flags_8.ZIMBABWE)) return R.drawable.e0468;
        else if(Arrays.equals(codePoints, Flags_8.ENGLAND)) return R.drawable.e1610;

        return 0;
    }

    private static int getEye3Emoji(int[] codePoints) {
        if (Arrays.equals(codePoints, Symbols_7.EYE_IN_SPEECH_BUBBLE)) return R.drawable.e0826;
        return 0;
    }

    private static int getPeople5Emoji(int[] codePoints) {
        if (Arrays.equals(codePoints, People_1.FAMILY_MAN_WOMEN_GIRL)) return R.drawable.e0959;
        else if (Arrays.equals(codePoints, People_1.FAMILY_WOMAN_WOMEN_BOY)) return R.drawable.e0962;
        else if (Arrays.equals(codePoints, People_1.FAMILY_WOMAN_WOMEN_GIRL)) return R.drawable.e0964;
        else if (Arrays.equals(codePoints, People_1.FAMILY_MAN_MAN_BOY)) return R.drawable.e0953;
        else if (Arrays.equals(codePoints, People_1.FAMILY_MAN_MAN_GIRL)) return R.drawable.e0955;
        return 0;
    }

    private static int getPeople6Emoji(int[] codePoints) {
        if (Arrays.equals(codePoints, People_1.COUPLE_WITH_HEART_WOMAN)) return R.drawable.e1094;
        else if(Arrays.equals(codePoints, People_1.COUPLE_WITH_HEART_MAN)) return R.drawable.e1093;
        return 0;
    }

    private static int getPeople7Emoji(int[] codePoints) {
        if (Arrays.equals(codePoints, People_1.FAMILY_MAN_WOMEN_GIRL_BOY)) return R.drawable.e0960;
        else if (Arrays.equals(codePoints, People_1.FAMILY_MAN_WOMEN_BOY_BOY)) return R.drawable.e0958;
        else if (Arrays.equals(codePoints, People_1.FAMILY_MAN_WOMEN_GIRL_GIRL)) return R.drawable.e0961;
        else if (Arrays.equals(codePoints, People_1.FAMILY_WOMAN_WOMEN_GIRL_BOY)) return R.drawable.e0965;
        else if (Arrays.equals(codePoints, People_1.FAMILY_WOMAN_WOMEN_BOY_BOY)) return R.drawable.e0963;
        else if (Arrays.equals(codePoints, People_1.FAMILY_WOMAN_WOMEN_GIRL_GIRL)) return R.drawable.e0966;
        else if (Arrays.equals(codePoints, People_1.FAMILY_MAN_MAN_GIRL_BOY)) return R.drawable.e0956;
        else if (Arrays.equals(codePoints, People_1.FAMILY_MAN_MAN_BOY_BOY)) return R.drawable.e0954;
        else if (Arrays.equals(codePoints, People_1.FAMILY_MAN_MAN_GIRL_GIRL)) return R.drawable.e0957;
        return 0;
    }

    private static int getPeople8Emoji(int[] codePoints) {
        if (Arrays.equals(codePoints, People_1.KISS_WOMAN)) return R.drawable.e1090;
        else if(Arrays.equals(codePoints, People_1.KISS_MAN)) return R.drawable.e1089;
        return 0;
    }

    private static int getOlympic9Emoji(int[] codePoints) {
        if (Arrays.equals(codePoints, Activity_4.OLYMPIC_RINGS)) return R.drawable.e1611;
        return 0;
    }

//__________________________________________________________________________________________________
    public static void addEmojis(Context context, Spannable text, int emojiSize) {
        addEmojis(context, text, emojiSize, 0, -1);
    }

    static void addEmojis(Context context, Spannable text, int emojiSize, int index, int length) {
        //LogUtil.e(text.toString(), index + ":" + length + ":" + text.length());
        //heart                  , 0:-1:1
        //heart,heart            , 0:-1:2
        //heart,heart,heart      , 0:-1:3

        //LogUtil.e(text.toString(), textLength + ":" + textLengthToProcessMax + ":" + textLengthToProcess + ":");
        //textLength = 1 for char
        //textLength = 2 for int
        //textLength = 2+ for typeface and int[]
        //while skip=1 for char skip=2 for int

        //FAMILY_MAN_WOMEN_GIRL_GIRL = {0x1F468, ZERO_WIDTH_JOINER, 0x1F469, ZERO_WIDTH_JOINER, 0x1F467, ZERO_WIDTH_JOINER, 0x1F467}; //http://emojipedia.org/family-man-woman-girl-girl/
        //textLength=9, skip=2,1,2,1,2,1,2
        /*
             i:skip:textLength : unicode;
         1   0:2   :11         : 1F468
         2   2:1   :11         : 200D
         3   3:2   :11         : 1F469
         4   5:1   :11         : 200D
         5   6:2   :11         : 1F467
         6   8:1   :11         : 200D
         7   9:2   :11         : 1F467
         */

        int textLength = text.length();
        int textLengthToProcessMax = textLength - index;
        int textLengthToProcess = length < 0 || length >= textLengthToProcessMax ? textLength : (length+index);

        // remove spans throughout all text
        EmojiconSpan[] oldSpans = text.getSpans(0, textLength, EmojiconSpan.class);
        for (EmojiconSpan oldSpan : oldSpans) {
            text.removeSpan(oldSpan);
        }

        int skip;
        for (int i = index; i < textLengthToProcess; i += skip) {
            int icon = 0;

            //1
            int unicode = Character.codePointAt(text, i);
            skip = Character.charCount(unicode);

            if (unicode > 0xff)
                icon = get1Emoji(unicode);

            if(icon != 0) //check only if icon has something therefore EXTRA_CHECK
                if (i+skip<textLengthToProcess) {
                    int nextUnicode = Character.codePointAt(text, i+skip);
                    if (nextUnicode == People_1.ZERO_WIDTH_JOINER)
                        icon =0;
                    else if(isTypeFaceEmoji(nextUnicode))
                        icon =0;
                    //else if (isFlagEmoji(nextUnicode))
                        //icon =0;
                    //icon=0 meaning let the loop continue
                }

            if (icon==0 && i+skip<textLengthToProcess) {
                //2
                int secondUnicode = Character.codePointAt(text, i+skip);
                int secondSkip = Character.charCount(secondUnicode);
                int[] c2 = {unicode, secondUnicode};
                icon = getSymbol2Emoji(c2);
                if(icon==0)
                    icon = getFlag2Emoji(c2);
                if(icon==0)
                    icon = getTypeFaceIcon(unicode, c2);

                //icon found
                if(icon!=0)
                    skip += secondSkip;
                else if (i+skip+secondSkip<textLengthToProcess) {
                    //3
                    int thirdUnicode = Character.codePointAt(text, i+skip+secondSkip);
                    int thirdSkip = Character.charCount(thirdUnicode);
                    int[] c3 = {unicode, secondUnicode, thirdUnicode};
                    icon = getEye3Emoji(c3);

                    //icon found
                    if (icon != 0)
                        skip += (secondSkip + thirdSkip);
                    else if (i+skip+secondSkip+thirdSkip<textLengthToProcess) {
                        //4
                        int fourthUnicode = Character.codePointAt(text, i+skip+secondSkip+thirdSkip);
                        int fourthSkip = Character.charCount(fourthUnicode);
                        //int[] c4 = {unicode, secondUnicode, thirdUnicode, fourthUnicode};
                        //icon = getPeople4Emoji(c4);

                        //icon found
                        if (icon != 0)
                            skip += (secondSkip + thirdSkip + fourthSkip);
                        else if (i+skip+secondSkip+thirdSkip+fourthSkip<textLengthToProcess) {
                            //5
                            int fifthUnicode = Character.codePointAt(text, i+skip+secondSkip+thirdSkip+fourthSkip);
                            int fifthSkip = Character.charCount(fifthUnicode);
                            int[] c5 = {unicode, secondUnicode, thirdUnicode, fourthUnicode, fifthUnicode};
                            icon = getPeople5Emoji(c5);

                            //5 and 7 have same start, for example
                            //5 - People_1.FAMILY_MAN_WOMEN_GIRL
                            //7 - People_1.FAMILY_MAN_WOMEN_GIRL_BOY
                            //therefore EXTRA_CHECK
                            if (i+skip+secondSkip+thirdSkip+fourthSkip+fifthSkip<textLengthToProcess) {
                                int nextUnicode = Character.codePointAt(text, i+skip+secondSkip+thirdSkip+fourthSkip+fifthSkip);
                                if (nextUnicode == People_1.ZERO_WIDTH_JOINER)
                                    icon = 0;
                                //icon=0 meaning let the loop run for 7
                            }

                            //icon found
                            if (icon != 0)
                                skip += (secondSkip + thirdSkip + fourthSkip + fifthSkip);
                            else if (i+skip+secondSkip+thirdSkip+fourthSkip+fifthSkip<textLengthToProcess) {
                                //6
                                int sixthUnicode = Character.codePointAt(text, i+skip+secondSkip+thirdSkip+fourthSkip+fifthSkip);
                                int sixthSkip = Character.charCount(sixthUnicode);
                                int[] c6 = {unicode, secondUnicode, thirdUnicode, fourthUnicode, fifthUnicode, sixthUnicode};
                                icon = getPeople6Emoji(c6);

                                //icon found
                                if (icon != 0)
                                    skip += (secondSkip + thirdSkip + fourthSkip + fifthSkip + sixthSkip);
                                else if (i+skip+secondSkip+thirdSkip+fourthSkip+fifthSkip+sixthSkip<textLengthToProcess) {
                                    //7
                                    int seventhUnicode = Character.codePointAt(text, i+skip+secondSkip+thirdSkip+fourthSkip+fifthSkip+sixthSkip);
                                    int seventhSkip = Character.charCount(seventhUnicode);
                                    int[] c7 = {unicode, secondUnicode, thirdUnicode, fourthUnicode, fifthUnicode, sixthUnicode, seventhUnicode};
                                    icon = getPeople7Emoji(c7);

                                    //icon found
                                    if (icon != 0)
                                        skip += (secondSkip + thirdSkip + fourthSkip + fifthSkip + sixthSkip + seventhSkip);
                                    else if (i+skip+secondSkip+thirdSkip+fourthSkip+fifthSkip+sixthSkip+seventhSkip<textLengthToProcess) {
                                        //8
                                        int eightUnicode = Character.codePointAt(text, i+skip+secondSkip+thirdSkip+fourthSkip+fifthSkip+sixthSkip+seventhSkip);
                                        int eightSkip = Character.charCount(eightUnicode);
                                        int[] c8 = {unicode, secondUnicode, thirdUnicode, fourthUnicode, fifthUnicode, sixthUnicode, seventhUnicode, eightUnicode};
                                        icon = getPeople8Emoji(c8);

                                        //icon found
                                        if (icon != 0)
                                            skip += (secondSkip + thirdSkip + fourthSkip + fifthSkip + sixthSkip + seventhSkip + eightSkip);
                                        else if (i+skip+secondSkip+thirdSkip+fourthSkip+fifthSkip+sixthSkip+seventhSkip+eightSkip<textLengthToProcess) {
                                            //9
                                            int ninthUnicode = Character.codePointAt(text, i+skip+secondSkip+thirdSkip+fourthSkip+fifthSkip+sixthSkip+seventhSkip+eightSkip);
                                            int ninthSkip = Character.charCount(ninthUnicode);
                                            int[] c9 = {unicode, secondUnicode, thirdUnicode, fourthUnicode, fifthUnicode, sixthUnicode, seventhUnicode, eightUnicode, ninthUnicode};
                                            icon = getOlympic9Emoji(c9);

                                            //icon found
                                            if (icon != 0)
                                                skip += (secondSkip + thirdSkip + fourthSkip + fifthSkip + sixthSkip + seventhSkip + eightSkip + ninthSkip);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (icon > 0) {
                text.setSpan(new EmojiconSpan(context, icon, emojiSize), i, i+skip, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }


//    //old: was trying to do using ArrayList
//    public static void addEmojis(Context context, Spannable text, int emojiSize, int index, int length) {
//        int textLength = text.length();
//        int textLengthToProcessMax = textLength - index;
//        int textLengthToProcess = length < 0 || length >= textLengthToProcessMax ? textLength : (length+index);
//
//        // remove spans throughout all text
//        EmojiconSpan[] oldSpans = text.getSpans(0, textLength, EmojiconSpan.class);
//        for (EmojiconSpan oldSpan : oldSpans) {
//            text.removeSpan(oldSpan);
//        }
//
//        boolean isSingleEmoji = true;
//        int skip;
//        for (int i = index; i < textLengthToProcess; i += skip) {
//            int icon = 0;
//            int unicode = Character.codePointAt(text, i);
//            skip = Character.charCount(unicode);
//
//            if(i+skip>=textLengthToProcess) {
//                //single symbol
//                if (unicode > 0xff)
//                    icon = get1Emoji(unicode);
//            } else {
//                //more than 1 symbol
//                int secondUnicode = Character.codePointAt(text, i+skip);
//                if (isTypeFaceEmoji(unicode))
//                    if (!isTypeFaceEmoji(secondUnicode))
//                        isSingleEmoji = false;
//                else if (i+skip<textLengthToProcess) {
//                    if (secondUnicode == People_1.ZERO_WIDTH_JOINER)
//                        isSingleEmoji = false;
//                }
//
//                //meaning more than 1 symbol but of single character (like Smile, cap etc)
//                if(isSingleEmoji)
//                    if (unicode > 0xff)
//                        icon = get1Emoji(unicode);
//
//            }
//
//            if (icon==0 && i+skip<textLengthToProcess) {
//                //2
//                int secondUnicode = Character.codePointAt(text, i+skip);
//                int secondSkip = Character.charCount(secondUnicode);
//                int[] c2 = {unicode, secondUnicode};
//                icon = getSymbol2Emoji(c2);
//                if(icon==0)
//                    icon = getFlag2Emoji(c2);
//                if(icon==0)
//                    icon = getTypeFaceIcon(unicode, c2);
//                skip += secondSkip;
//
//                if (icon==0 && i+skip<textLengthToProcess) {
//                    //3
//                    int thirdUnicode = Character.codePointAt(text, i+skip);
//                    int thirdSkip = Character.charCount(thirdUnicode);
//                    int[] c3 = {unicode, secondUnicode, thirdUnicode};
//                    icon = getEye3Emoji(c3);
//                    skip += thirdSkip;
//
//                    if (icon==0 && i+skip<textLengthToProcess) {
//                        //4
//                        int fourthUnicode = Character.codePointAt(text, i+skip);
//                        int fourthSkip = Character.charCount(fourthUnicode);
//                        int[] c4 = {unicode, secondUnicode, thirdUnicode, fourthUnicode};
//                        //icon = getPeople4Emoji(c4);
//                        skip += fourthSkip;
//
//                        if (icon==0 && i+skip<textLengthToProcess) {
//                            //5
//                            int fifthUnicode = Character.codePointAt(text, i+skip);
//                            int fifthSkip = Character.charCount(fifthUnicode);
//                            int[] c5 = {unicode, secondUnicode, thirdUnicode, fourthUnicode, fifthUnicode};
//                            icon = getPeople5Emoji(c5);
//                            skip += fifthSkip;
//
//                            //5 and 7 have same start, for example
//                            //5 - People_1.FAMILY_MAN_WOMEN_GIRL
//                            //7 - People_1.FAMILY_MAN_WOMEN_GIRL_BOY
//                            //therefore extra check
//                            if (i+skip<textLengthToProcess) {
//                                int nextUnicode = Character.codePointAt(text, i + skip);
//                                if (nextUnicode == People_1.ZERO_WIDTH_JOINER)
//                                    icon = 0;
//                                //icon=0 meaning let the loop run for 7
//                            }
//
//                            if (icon==0 && i+skip<textLengthToProcess) {
//                                //6
//                                int sixthUnicode = Character.codePointAt(text, i+skip);
//                                int sixthSkip = Character.charCount(sixthUnicode);
//                                int[] c6 = {unicode, secondUnicode, thirdUnicode, fourthUnicode, fifthUnicode, sixthUnicode};
//                                icon = getPeople6Emoji(c6);
//                                skip += sixthSkip;
//
//                                if (icon==0 && i+skip<textLengthToProcess) {
//                                    //7
//                                    int seventhUnicode = Character.codePointAt(text, i+skip);
//                                    int seventhSkip = Character.charCount(seventhUnicode);
//                                    int[] c7 = {unicode, secondUnicode, thirdUnicode, fourthUnicode, fifthUnicode, sixthUnicode, seventhUnicode};
//                                    icon = getPeople7Emoji(c7);
//                                    skip += seventhSkip;
//
//                                    if (icon==0 && i+skip<textLengthToProcess) {
//                                        //8
//                                        int eightUnicode = Character.codePointAt(text, i+skip);
//                                        int eightSkip = Character.charCount(eightUnicode);
//                                        int[] c8 = {unicode, secondUnicode, thirdUnicode, fourthUnicode, fifthUnicode, sixthUnicode, seventhUnicode, eightUnicode};
//                                        icon = getPeople8Emoji(c8);
//                                        skip += eightSkip;
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//
//
////                    //LogUtil.e("3--=-->" + i + "|", thirdSkip +":" + dec2m(thirdUnicode) + ":");
////
////                    if(thirdUnicode == Symbols_7.EYE_IN_SPEECH_BUBBLE[2]) {
////                        LogUtil.e("-----", "eye---");
////                        int[] codePoints = {unicode, secondUnicode, thirdUnicode};
////                        if (Arrays.equals(codePoints, Symbols_7.EYE_IN_SPEECH_BUBBLE))
////                            icon = R.drawable.e0826;
////                        else
////                            thirdSkip=0;
//            }
//
//            if (icon > 0) {
//                text.setSpan(new EmojiconSpan(context, icon, emojiSize), i, i + skip, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            }
//        }
//    }


//OLD METHOD using ArrayList
//    public static void addEmojis(Context context, Spannable text, int emojiSize, int index, int length) {
//
//        ArrayList<Integer> codePoints = new ArrayList<>();
//        int arrayIndex = 0;
//        int end = 0;
//        for (int i=index; i<textLengthToProcess; i+=skip) {
//            codePoints.add(Character.codePointAt(text, i));
//            skip = Character.charCount(codePoints.get(arrayIndex));
////            if (text.toString().equals("\uD83D\uDC68\u200D\uD83D\uDC69\u200D\uD83D\uDC67\u200D\uD83D\uDC67"))
////                LogUtil.e("_|_|_|_", "F : " + i + ":" + skip + ":" + textLength + " : " + dec2m(codePoints.get(arrayIndex)));
//            end = i+skip;
//            arrayIndex++;
//        }
//
//        int icon = 0;
//        switch(codePoints.size()) {
//            //1,2,3,4,5,6,7 are basically count, like 2 for PAKISTAN = {0x1f1f5, 0x1f1f0}
//            case 1:
//                icon = get1Emoji(codePoints.get(0));
//                break;
//            case 2:
//                int[] c2 = {codePoints.get(0), codePoints.get(1)};
//                icon = getSymbol2Emoji(c2);
//                if(icon==0)
//                    icon = getFlag2Emoji(c2);
//                if(icon==0)
//                    icon = getTypeFaceIcon(codePoints.get(0), c2);
//                break;
//            case 3:
//                int[] c3 = {codePoints.get(0), codePoints.get(1), codePoints.get(2)};
//                icon = getEye3Emoji(c3);
//                break;
////            case 4:
////                int[] c4 = {codePoints.get(0), codePoints.get(1), codePoints.get(3), codePoints.get(4)};
////                icon = (c4);
////                break;
//            case 5:
//                int[] c5 = {codePoints.get(0), codePoints.get(1), codePoints.get(2), codePoints.get(3), codePoints.get(4)};
//                icon = getPeople5Emoji(c5);
//                break;
//            case 6:
//                int[] c6 = {codePoints.get(0), codePoints.get(1), codePoints.get(2), codePoints.get(3), codePoints.get(4), codePoints.get(5)};
//                icon = getPeople6Emoji(c6);
//                break;
//            case 7:
//                int[] c7 = {codePoints.get(0), codePoints.get(1), codePoints.get(2), codePoints.get(3), codePoints.get(4), codePoints.get(5), codePoints.get(6)};
//                icon = getPeople7Emoji(c7);
//                break;
//        }
//        //safety check [if emoji not available draw from system or blank box]
//        if(icon>0)
//            text.setSpan(new EmojiconSpan(context, icon, emojiSize), 0, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//}


//__________________________________________________________________________________________________
    //[emoji_: when adding typeface emoji, make sure to enter it here]
//    private static boolean isTypeFaceEmoji(int unicode) {
//        switch (unicode) {
//            //Tab: People_1
//            case People_1.PERSON_BOTH_HAND_CELEBRATION:
//            //12
//            case People_1.CLAPPING_HAND:
//            case People_1.WAVING_HANDS:
//            case People_1.THUMBS_UP:
//            case People_1.THUMBS_DOWN:
//            case People_1.FIST_HAND:
//            case People_1.RAISED_FIST:
//            case People_1.VICTORY_HAND:
//            case People_1.OK_HAND:
//            //13
//            case People_1.RAISED_HAND:
//            case People_1.OPEN_HAND:
//            case People_1.FLEXED_BICEPS:
//            case People_1.FOLDED_HANDS:
//            case People_1.UP_POINTING_INDEX:
//            case People_1.UP_POINTING_BACKHAND_INDEX:
//            case People_1.DOWN_POINTING_BACKHAND_INDEX:
//            case People_1.LEFT_POINTING_BACKHAND_INDEX:
//            //14
//            case People_1.RIGHT_POINTING_BACKHAND_INDEX:
//            case People_1.REVERSE_MIDDLE_FINGER:
//            case People_1.RAISED_HAND_FINGERS_SPLAYED:
//            case People_1.SIGN_OF_HORN:
//            case People_1.RAISED_HAND_PART_BETWEEN_MIDDLE_RING:
//            case People_1.WRITING_HAND:
//            case People_1.NAIL_POLISH:
//            //15
//            case People_1.EAR:
//            case People_1.NOSE:
//            //16
//            case People_1.BABY:
//            case People_1.BOY:
//            case People_1.GIRL:
//            case People_1.MAN:
//            case People_1.WOMEN:
//            case People_1.PERSON_WITH_BLOND_HAIR:
//            case People_1.OLDER_MAN:
//            case People_1.OLDER_WOMEN:
//            //17
//            case People_1.MAN_WITH_GUA_PI_MAO:
//            case People_1.MAN_WITH_TURBAN:
//            case People_1.POLICE_OFFICER:
//            case People_1.CONSTRUCTION_WORKER:
//            case People_1.GUARDS_MAN:
//            case People_1.FATHER_CHRISTMAS:
//            case People_1.BABY_ANGEL:
//            //18
//            case People_1.PRINCESS:
//            case People_1.BRIDE_WITH_VEIL:
//            case People_1.PEDESTRIAN:
//            case People_1.RUNNER:
//            case People_1.DANCER:
//            //19
//            case People_1.PERSON_BOWING_DEEPLY:
//            case People_1.INFORMATION_DESK_PERSON:
//            case People_1.FACE_WITH_NO_GOOD_GESTURE:
//            case People_1.FACE_WITH_OK_GESTURE:
//            case People_1.HAPPY_PERSON_RAISE_ONE_HAND:
//            case People_1.PERSON_WITH_POUTING_FACE:
//            case People_1.PERSON_FROWNING:
//            //20
//            case People_1.HAIRCUT:
//            case People_1.FACE_MASSAGE:
//            //Tab: Activity_4
//            case Activity_4.ROW_BOAT:
//            case Activity_4.SWIMMER:
//            case Activity_4.SURFER:
//            case Activity_4.BATH:
//            case Activity_4.PERSON_WITH_BALL:
//            case Activity_4.WEIGHT_LIFTER:
//            case Activity_4.BICYCLIST:
//            case Activity_4.MOUNTAIN_BICYCLIST:
//            case Activity_4.HORSE_RACING:
//                return true;
//        }
//        return false;
//    }


    private static final int TYPE_1_2 = 0x1F3FB;
    private static final int TYPE_3 = 0x1F3FC;
    private static final int TYPE_4 = 0x1F3FD;
    private static final int TYPE_5 = 0x1F3FE;
    private static final int TYPE_6 = 0x1F3FF;
    private static boolean isTypeFaceEmoji(int unicode) {
        switch (unicode) {
            case TYPE_1_2:
            case TYPE_3:
            case TYPE_4:
            case TYPE_5:
            case TYPE_6:
            return true;
        }
        return false;
    }

//    public static final int A = 0x1f1e6;
//    public static final int B = 0x1f1e7;
//    public static final int C = 0x1f1e8;
//    public static final int D = 0x1f1e9;
//    public static final int E = 0x1f1ea;
//    public static final int F = 0x1f1eb;
//    public static final int G = 0x1f1ec;
//    public static final int H = 0x1f1ed;
//    public static final int I = 0x1f1ee;
//    public static final int J = 0x1f1ef;
//    public static final int K = 0x1f1f0;
//    public static final int L = 0x1f1f1;
//    public static final int M = 0x1f1f2;
//    public static final int N = 0x1f1f3;
//    public static final int O = 0x1f1f4;
//    public static final int P = 0x1f1f5;
//    public static final int Q = 0x1f1f6;
//    public static final int R = 0x1f1f7;
//    public static final int S = 0x1f1f8;
//    public static final int T = 0x1f1f9;
//    public static final int U = 0x1f1fa;
//    public static final int V = 0x1f1fb;
//    public static final int W = 0x1f1fc;
//    public static final int X = 0x1f1fd;
//    public static final int Y = 0x1f1fe;
//    public static final int Z = 0x1f1ff;
//    private static boolean isFlagEmoji(int unicode) {
//        switch (unicode) {
//            case A:
//            case B:
//            case C:
//            case D:
//            case E:
//            case F:
//            case G:
//            case H:
//            case I:
//            case J:
//            case K:
//            case L:
//            case M:
//            case N:
//            case O:
//            case P:
//            case Q:
//            case R:
//            case S:
//            case T:
//            case U:
//            case V:
//            case W:
//            case X:
//            case Y:
//            case Z:
//                return true;
//        }
//        return false;
//    }

    //[emoji_: when adding typeface emoji, make sure to enter it here]
    private static int getTypeFaceIcon(int unicode, int[] codePoints) {
        switch(unicode) {
            //Tab: People_1
            case People_1.PERSON_BOTH_HAND_CELEBRATION:
                return getTypeFacePersonBothHandCelebration(codePoints);
            //12
            case People_1.CLAPPING_HAND:
                return getTypeFaceClappingHands(codePoints);
            case People_1.WAVING_HANDS:
                return getTypeFaceWavingHands(codePoints);
            case People_1.THUMBS_UP:
                return getTypeFaceThumbsUp(codePoints);
            case People_1.THUMBS_DOWN:
                return getTypeFaceThumbsDown(codePoints);
            case People_1.FIST_HAND:
                return getTypeFaceFistHand(codePoints);
            case People_1.RAISED_FIST:
                return getTypeFaceRaisedFist(codePoints);
            case People_1.VICTORY_HAND:
                return getTypeFaceVictoryHand(codePoints);
            case People_1.OK_HAND:
                return getTypeFaceOKHand(codePoints);
            //13
            case People_1.RAISED_HAND:
                return getTypeFaceRaisedHand(codePoints);
            case People_1.OPEN_HAND:
                return getTypeFaceOpenHand(codePoints);
            case People_1.FLEXED_BICEPS:
                return getTypeFaceFlexedBiceps(codePoints);
            case People_1.FOLDED_HANDS:
                return getTypeFaceFoldedHands(codePoints);
            case People_1.UP_POINTING_INDEX:
                return getTypeFaceUpPointingIndex(codePoints);
            case People_1.UP_POINTING_BACKHAND_INDEX:
                return getTypeFaceUpPointingBackhandIndex(codePoints);
            case People_1.DOWN_POINTING_BACKHAND_INDEX:
                return getTypeFaceDownPointingBackhandIndex(codePoints);
            case People_1.LEFT_POINTING_BACKHAND_INDEX:
                return getTypeFaceLeftPointingBackhandIndex(codePoints);
            //14
            case People_1.RIGHT_POINTING_BACKHAND_INDEX:
                return getTypeFaceRightPointingBackhandIndex(codePoints);
            case People_1.REVERSE_MIDDLE_FINGER:
                return getTypeFaceReverseMiddleFinger(codePoints);
            case People_1.RAISED_HAND_FINGERS_SPLAYED:
                return getTypeFaceRaisedHandFingersSplayed(codePoints);
            case People_1.SIGN_OF_HORN:
                return getTypeFaceSignOfHorn(codePoints);
            case People_1.RAISED_HAND_PART_BETWEEN_MIDDLE_RING:
                return getTypeFaceRaisedHandPartBetweenMiddleRing(codePoints);
            case People_1.WRITING_HAND:
                return getTypeFaceWritingHand(codePoints);
            case People_1.NAIL_POLISH:
                return getTypeFaceNailPolish(codePoints);
            //15
            case People_1.EAR:
                return getTypeFaceEar(codePoints);
            case People_1.NOSE:
                return getTypeFaceNose(codePoints);
            //16
            case People_1.BABY:
                return getTypeFaceBaby(codePoints);
            case People_1.BOY:
                return getTypeFaceBoy(codePoints);
            case People_1.GIRL:
                return getTypeFaceGirl(codePoints);
            case People_1.MAN:
                return getTypeFaceMan(codePoints);
            case People_1.WOMEN:
                return getTypeFaceWomen(codePoints);
            case People_1.PERSON_WITH_BLOND_HAIR:
                return getTypeFacePersonWithBlondHair(codePoints);
            case People_1.OLDER_MAN:
                return getTypeFaceOlderMan(codePoints);
            case People_1.OLDER_WOMEN:
                return getTypeFaceOlderWomen(codePoints);
            //17
            case People_1.MAN_WITH_GUA_PI_MAO:
                return getTypeFaceManWithGuaPiMao(codePoints);
            case People_1.MAN_WITH_TURBAN:
                return getTypeFaceManWithTurban(codePoints);
            case People_1.POLICE_OFFICER:
                return getTypeFacePoliceOfficer(codePoints);
            case People_1.CONSTRUCTION_WORKER:
                return getTypeFaceConstructionWorker(codePoints);
            case People_1.GUARDS_MAN:
                return getTypeFaceGuardsMan(codePoints);
            case People_1.FATHER_CHRISTMAS:
                return getTypeFaceFatherChristmas(codePoints);
            case People_1.BABY_ANGEL:
                return getTypeFaceBabyAngel(codePoints);
            //18
            case People_1.PRINCESS:
                return getTypeFacePrincess(codePoints);
            case People_1.BRIDE_WITH_VEIL:
                return getTypeFaceBrideWithVeil(codePoints);
            case People_1.PEDESTRIAN:
                return getTypeFacePedestrian(codePoints);
            case People_1.RUNNER:
                return getTypeFaceRunner(codePoints);
            case People_1.DANCER:
                return getTypeFaceDancer(codePoints);
            //19
            case People_1.PERSON_BOWING_DEEPLY:
                return getTypeFacePersonBowingDeeply(codePoints);
            case People_1.INFORMATION_DESK_PERSON:
                return getTypeFaceInformationDeskPerson(codePoints);
            case People_1.FACE_WITH_NO_GOOD_GESTURE:
                return getTypeFaceFaceWithNoGoodGesture(codePoints);
            case People_1.FACE_WITH_OK_GESTURE:
                return getTypeFaceFaceWithOkGesture(codePoints);
            case People_1.HAPPY_PERSON_RAISE_ONE_HAND:
                return getTypeFacePersonRaiseOneHand(codePoints);
            case People_1.PERSON_WITH_POUTING_FACE:
                return getTypeFacePersonWithPoutingFace(codePoints);
            case People_1.PERSON_FROWNING:
                return getTypeFacePersonFrowning(codePoints);
            //20
            case People_1.HAIRCUT:
                return getTypeFaceHairCut(codePoints);
            case People_1.FACE_MASSAGE:
                return getTypeFaceFaceMassage(codePoints);
            //Tab: Activity_4
            case Activity_4.ROW_BOAT:
                return getTypeFaceRowBoat(codePoints);
            case Activity_4.SWIMMER:
                return getTypeFaceSwimmer(codePoints);
            case Activity_4.SURFER:
                return getTypeFaceSurfer(codePoints);
            case Activity_4.BATH:
                return getTypeFaceBath(codePoints);
            case Activity_4.PERSON_WITH_BALL:
                return getTypeFacePersonWithBall(codePoints);
            case Activity_4.WEIGHT_LIFTER:
                return getTypeFaceWeightLifter(codePoints);
            case Activity_4.BICYCLIST:
                return getTypeFaceBicyclist(codePoints);
            case Activity_4.MOUNTAIN_BICYCLIST:
                return getTypeFaceMountainBicyclist(codePoints);
            case Activity_4.HORSE_RACING:
                return getTypeFaceHorseRacing(codePoints);
        }
        return 0;
    }

    //[emoji_: when adding typeface emoji, make sure to enter it here]
    private static int getTypeFacePersonBothHandCelebration(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.PERSON_BOTH_HAND_CELEBRATION_TYPE_1_2)) return R.drawable.e1455;
        else if(Arrays.equals(codePoints, People_1.PERSON_BOTH_HAND_CELEBRATION_TYPE_3)) return R.drawable.e1456;
        else if(Arrays.equals(codePoints, People_1.PERSON_BOTH_HAND_CELEBRATION_TYPE_4)) return R.drawable.e1457;
        else if(Arrays.equals(codePoints, People_1.PERSON_BOTH_HAND_CELEBRATION_TYPE_5)) return R.drawable.e1458;
        else if(Arrays.equals(codePoints, People_1.PERSON_BOTH_HAND_CELEBRATION_TYPE_6)) return R.drawable.e1459;
        return 0;
    }

    private static int getTypeFaceClappingHands(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.CLAPPING_HAND_TYPE_1_2)) return R.drawable.e0896;
        else if(Arrays.equals(codePoints, People_1.CLAPPING_HAND_TYPE_3)) return R.drawable.e0897;
        else if(Arrays.equals(codePoints, People_1.CLAPPING_HAND_TYPE_4)) return R.drawable.e0898;
        else if(Arrays.equals(codePoints, People_1.CLAPPING_HAND_TYPE_5)) return R.drawable.e0899;
        else if(Arrays.equals(codePoints, People_1.CLAPPING_HAND_TYPE_6)) return R.drawable.e0900;
        return 0;
    }

    private static int getTypeFaceWavingHands(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.WAVING_HANDS_TYPE_1_2)) return R.drawable.e0872;
        else if(Arrays.equals(codePoints, People_1.WAVING_HANDS_TYPE_3)) return R.drawable.e0873;
        else if(Arrays.equals(codePoints, People_1.WAVING_HANDS_TYPE_4)) return R.drawable.e0874;
        else if(Arrays.equals(codePoints, People_1.WAVING_HANDS_TYPE_5)) return R.drawable.e0875;
        else if(Arrays.equals(codePoints, People_1.WAVING_HANDS_TYPE_6)) return R.drawable.e0876;
        return 0;
    }

    private static int getTypeFaceThumbsUp(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.THUMBS_UP_TYPE_1_2)) return R.drawable.e0884;
        else if(Arrays.equals(codePoints, People_1.THUMBS_UP_TYPE_3)) return R.drawable.e0885;
        else if(Arrays.equals(codePoints, People_1.THUMBS_UP_TYPE_4)) return R.drawable.e0886;
        else if(Arrays.equals(codePoints, People_1.THUMBS_UP_TYPE_5)) return R.drawable.e0887;
        else if(Arrays.equals(codePoints, People_1.THUMBS_UP_TYPE_6)) return R.drawable.e0888;
        return 0;
    }

    private static int getTypeFaceThumbsDown(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.THUMBS_DOWN_TYPE_1_2)) return R.drawable.e0890;
        else if(Arrays.equals(codePoints, People_1.THUMBS_DOWN_TYPE_3)) return R.drawable.e0891;
        else if(Arrays.equals(codePoints, People_1.THUMBS_DOWN_TYPE_4)) return R.drawable.e0892;
        else if(Arrays.equals(codePoints, People_1.THUMBS_DOWN_TYPE_5)) return R.drawable.e0893;
        else if(Arrays.equals(codePoints, People_1.THUMBS_DOWN_TYPE_6)) return R.drawable.e0894;
        return 0;
    }

    private static int getTypeFaceFistHand(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.FIST_HAND_TYPE_1_2)) return R.drawable.e0866;
        else if(Arrays.equals(codePoints, People_1.FIST_HAND_TYPE_3)) return R.drawable.e0867;
        else if(Arrays.equals(codePoints, People_1.FIST_HAND_TYPE_4)) return R.drawable.e0868;
        else if(Arrays.equals(codePoints, People_1.FIST_HAND_TYPE_5)) return R.drawable.e0869;
        else if(Arrays.equals(codePoints, People_1.FIST_HAND_TYPE_6)) return R.drawable.e0870;
        return 0;
    }

    private static int getTypeFaceRaisedFist(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.RAISED_FIST_TYPE_1_2)) return R.drawable.e0144;
        else if(Arrays.equals(codePoints, People_1.RAISED_FIST_TYPE_3)) return R.drawable.e0145;
        else if(Arrays.equals(codePoints, People_1.RAISED_FIST_TYPE_4)) return R.drawable.e0146;
        else if(Arrays.equals(codePoints, People_1.RAISED_FIST_TYPE_5)) return R.drawable.e0147;
        else if(Arrays.equals(codePoints, People_1.RAISED_FIST_TYPE_6)) return R.drawable.e0148;
        return 0;
    }

    private static int getTypeFaceVictoryHand(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.VICTORY_HAND_TYPE_1_2)) return R.drawable.e0156;
        else if(Arrays.equals(codePoints, People_1.VICTORY_HAND_TYPE_3)) return R.drawable.e0157;
        else if(Arrays.equals(codePoints, People_1.VICTORY_HAND_TYPE_4)) return R.drawable.e0158;
        else if(Arrays.equals(codePoints, People_1.VICTORY_HAND_TYPE_5)) return R.drawable.e0159;
        else if(Arrays.equals(codePoints, People_1.VICTORY_HAND_TYPE_6)) return R.drawable.e0160;
        return 0;
    }

    private static int getTypeFaceOKHand(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.OK_HAND_TYPE_1_2)) return R.drawable.e0878;
        else if(Arrays.equals(codePoints, People_1.OK_HAND_TYPE_3)) return R.drawable.e0879;
        else if(Arrays.equals(codePoints, People_1.OK_HAND_TYPE_4)) return R.drawable.e0880;
        else if(Arrays.equals(codePoints, People_1.OK_HAND_TYPE_5)) return R.drawable.e0881;
        else if(Arrays.equals(codePoints, People_1.OK_HAND_TYPE_6)) return R.drawable.e0882;
        return 0;
    }

    private static int getTypeFaceRaisedHand(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.RAISED_HAND_TYPE_1_2)) return R.drawable.e0150;
        else if(Arrays.equals(codePoints, People_1.RAISED_HAND_TYPE_3)) return R.drawable.e0151;
        else if(Arrays.equals(codePoints, People_1.RAISED_HAND_TYPE_4)) return R.drawable.e0152;
        else if(Arrays.equals(codePoints, People_1.RAISED_HAND_TYPE_5)) return R.drawable.e0153;
        else if(Arrays.equals(codePoints, People_1.RAISED_HAND_TYPE_6)) return R.drawable.e0154;
        return 0;
    }

    private static int getTypeFaceOpenHand(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.OPEN_HAND_TYPE_1_2)) return R.drawable.e0902;
        else if(Arrays.equals(codePoints, People_1.OPEN_HAND_TYPE_3)) return R.drawable.e0903;
        else if(Arrays.equals(codePoints, People_1.OPEN_HAND_TYPE_4)) return R.drawable.e0904;
        else if(Arrays.equals(codePoints, People_1.OPEN_HAND_TYPE_5)) return R.drawable.e0905;
        else if(Arrays.equals(codePoints, People_1.OPEN_HAND_TYPE_6)) return R.drawable.e0906;
        return 0;
    }

    private static int getTypeFaceFlexedBiceps(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.FLEXED_BICEPS_TYPE_1_2)) return R.drawable.e1120;
        else if(Arrays.equals(codePoints, People_1.FLEXED_BICEPS_TYPE_3)) return R.drawable.e1121;
        else if(Arrays.equals(codePoints, People_1.FLEXED_BICEPS_TYPE_4)) return R.drawable.e1122;
        else if(Arrays.equals(codePoints, People_1.FLEXED_BICEPS_TYPE_5)) return R.drawable.e1123;
        else if(Arrays.equals(codePoints, People_1.FLEXED_BICEPS_TYPE_6)) return R.drawable.e1124;
        return 0;
    }

    private static int getTypeFaceFoldedHands(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.FOLDED_HANDS_TYPE_1_2)) return R.drawable.e1473;
        else if(Arrays.equals(codePoints, People_1.FOLDED_HANDS_TYPE_3)) return R.drawable.e1474;
        else if(Arrays.equals(codePoints, People_1.FOLDED_HANDS_TYPE_4)) return R.drawable.e1475;
        else if(Arrays.equals(codePoints, People_1.FOLDED_HANDS_TYPE_5)) return R.drawable.e1476;
        else if(Arrays.equals(codePoints, People_1.FOLDED_HANDS_TYPE_6)) return R.drawable.e1477;
        return 0;
    }

    private static int getTypeFaceUpPointingIndex(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.UP_POINTING_INDEX_TYPE_1_2)) return R.drawable.e0063;
        else if(Arrays.equals(codePoints, People_1.UP_POINTING_INDEX_TYPE_3)) return R.drawable.e0064;
        else if(Arrays.equals(codePoints, People_1.UP_POINTING_INDEX_TYPE_4)) return R.drawable.e0065;
        else if(Arrays.equals(codePoints, People_1.UP_POINTING_INDEX_TYPE_5)) return R.drawable.e0066;
        else if(Arrays.equals(codePoints, People_1.UP_POINTING_INDEX_TYPE_6)) return R.drawable.e0067;
        return 0;
    }

    private static int getTypeFaceUpPointingBackhandIndex(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.UP_POINTING_BACKHAND_INDEX_TYPE_1_2)) return R.drawable.e0842;
        else if(Arrays.equals(codePoints, People_1.UP_POINTING_BACKHAND_INDEX_TYPE_3)) return R.drawable.e0843;
        else if(Arrays.equals(codePoints, People_1.UP_POINTING_BACKHAND_INDEX_TYPE_4)) return R.drawable.e0844;
        else if(Arrays.equals(codePoints, People_1.UP_POINTING_BACKHAND_INDEX_TYPE_5)) return R.drawable.e0845;
        else if(Arrays.equals(codePoints, People_1.UP_POINTING_BACKHAND_INDEX_TYPE_6)) return R.drawable.e0846;
        return 0;
    }

    private static int getTypeFaceDownPointingBackhandIndex(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.DOWN_POINTING_BACKHAND_INDEX_TYPE_1_2)) return R.drawable.e0848;
        else if(Arrays.equals(codePoints, People_1.DOWN_POINTING_BACKHAND_INDEX_TYPE_3)) return R.drawable.e0849;
        else if(Arrays.equals(codePoints, People_1.DOWN_POINTING_BACKHAND_INDEX_TYPE_4)) return R.drawable.e0850;
        else if(Arrays.equals(codePoints, People_1.DOWN_POINTING_BACKHAND_INDEX_TYPE_5)) return R.drawable.e0851;
        else if(Arrays.equals(codePoints, People_1.DOWN_POINTING_BACKHAND_INDEX_TYPE_6)) return R.drawable.e0852;
        return 0;
    }

    private static int getTypeFaceLeftPointingBackhandIndex(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.LEFT_POINTING_BACKHAND_INDEX_TYPE_1_2)) return R.drawable.e0854;
        else if(Arrays.equals(codePoints, People_1.LEFT_POINTING_BACKHAND_INDEX_TYPE_3)) return R.drawable.e0855;
        else if(Arrays.equals(codePoints, People_1.LEFT_POINTING_BACKHAND_INDEX_TYPE_4)) return R.drawable.e0856;
        else if(Arrays.equals(codePoints, People_1.LEFT_POINTING_BACKHAND_INDEX_TYPE_5)) return R.drawable.e0857;
        else if(Arrays.equals(codePoints, People_1.LEFT_POINTING_BACKHAND_INDEX_TYPE_6)) return R.drawable.e0858;
        return 0;
    }

    private static int getTypeFaceRightPointingBackhandIndex(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.RIGHT_POINTING_BACKHAND_INDEX_TYPE_1_2)) return R.drawable.e0860;
        else if(Arrays.equals(codePoints, People_1.RIGHT_POINTING_BACKHAND_INDEX_TYPE_3)) return R.drawable.e0861;
        else if(Arrays.equals(codePoints, People_1.RIGHT_POINTING_BACKHAND_INDEX_TYPE_4)) return R.drawable.e0862;
        else if(Arrays.equals(codePoints, People_1.RIGHT_POINTING_BACKHAND_INDEX_TYPE_5)) return R.drawable.e0863;
        else if(Arrays.equals(codePoints, People_1.RIGHT_POINTING_BACKHAND_INDEX_TYPE_6)) return R.drawable.e0864;
        return 0;
    }

    private static int getTypeFaceReverseMiddleFinger(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.REVERSE_MIDDLE_FINGER_TYPE_1_2)) return R.drawable.e1322;
        else if(Arrays.equals(codePoints, People_1.REVERSE_MIDDLE_FINGER_TYPE_3)) return R.drawable.e1323;
        else if(Arrays.equals(codePoints, People_1.REVERSE_MIDDLE_FINGER_TYPE_4)) return R.drawable.e1324;
        else if(Arrays.equals(codePoints, People_1.REVERSE_MIDDLE_FINGER_TYPE_5)) return R.drawable.e1325;
        else if(Arrays.equals(codePoints, People_1.REVERSE_MIDDLE_FINGER_TYPE_6)) return R.drawable.e1326;
        return 0;
    }

    private static int getTypeFaceRaisedHandFingersSplayed(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.RAISED_HAND_FINGERS_SPLAYED_TYPE_1_2)) return R.drawable.e1316;
        else if(Arrays.equals(codePoints, People_1.RAISED_HAND_FINGERS_SPLAYED_TYPE_3)) return R.drawable.e1317;
        else if(Arrays.equals(codePoints, People_1.RAISED_HAND_FINGERS_SPLAYED_TYPE_4)) return R.drawable.e1318;
        else if(Arrays.equals(codePoints, People_1.RAISED_HAND_FINGERS_SPLAYED_TYPE_5)) return R.drawable.e1319;
        else if(Arrays.equals(codePoints, People_1.RAISED_HAND_FINGERS_SPLAYED_TYPE_6)) return R.drawable.e1320;
        return 0;
    }

    private static int getTypeFaceSignOfHorn(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.SIGN_OF_HORN_TYPE_1_2)) return R.drawable.e1599;
        else if(Arrays.equals(codePoints, People_1.SIGN_OF_HORN_TYPE_3)) return R.drawable.e1600;
        else if(Arrays.equals(codePoints, People_1.SIGN_OF_HORN_TYPE_4)) return R.drawable.e1601;
        else if(Arrays.equals(codePoints, People_1.SIGN_OF_HORN_TYPE_5)) return R.drawable.e1602;
        else if(Arrays.equals(codePoints, People_1.SIGN_OF_HORN_TYPE_6)) return R.drawable.e1603;
        return 0;
    }

    private static int getTypeFaceRaisedHandPartBetweenMiddleRing(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.RAISED_HAND_PART_BETWEEN_MIDDLE_RING_TYPE_1_2)) return R.drawable.e1328;
        else if(Arrays.equals(codePoints, People_1.RAISED_HAND_PART_BETWEEN_MIDDLE_RING_TYPE_3)) return R.drawable.e1329;
        else if(Arrays.equals(codePoints, People_1.RAISED_HAND_PART_BETWEEN_MIDDLE_RING_TYPE_4)) return R.drawable.e1330;
        else if(Arrays.equals(codePoints, People_1.RAISED_HAND_PART_BETWEEN_MIDDLE_RING_TYPE_5)) return R.drawable.e1331;
        else if(Arrays.equals(codePoints, People_1.RAISED_HAND_PART_BETWEEN_MIDDLE_RING_TYPE_6)) return R.drawable.e1332;
        return 0;
    }

    private static int getTypeFaceWritingHand(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.WRITING_HAND_TYPE_1_2)) return R.drawable.e0162;
        else if(Arrays.equals(codePoints, People_1.WRITING_HAND_TYPE_3)) return R.drawable.e0163;
        else if(Arrays.equals(codePoints, People_1.WRITING_HAND_TYPE_4)) return R.drawable.e0164;
        else if(Arrays.equals(codePoints, People_1.WRITING_HAND_TYPE_5)) return R.drawable.e0165;
        else if(Arrays.equals(codePoints, People_1.WRITING_HAND_TYPE_6)) return R.drawable.e0166;
        return 0;
    }

    private static int getTypeFaceNailPolish(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.NAIL_POLISH_TYPE_1_2)) return R.drawable.e1064;
        else if(Arrays.equals(codePoints, People_1.NAIL_POLISH_TYPE_3)) return R.drawable.e1065;
        else if(Arrays.equals(codePoints, People_1.NAIL_POLISH_TYPE_4)) return R.drawable.e1066;
        else if(Arrays.equals(codePoints, People_1.NAIL_POLISH_TYPE_5)) return R.drawable.e1067;
        else if(Arrays.equals(codePoints, People_1.NAIL_POLISH_TYPE_6)) return R.drawable.e1068;
        return 0;
    }

    private static int getTypeFaceEar(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.EAR_TYPE_1_2)) return R.drawable.e0828;
        else if(Arrays.equals(codePoints, People_1.EAR_TYPE_3)) return R.drawable.e0829;
        else if(Arrays.equals(codePoints, People_1.EAR_TYPE_4)) return R.drawable.e0830;
        else if(Arrays.equals(codePoints, People_1.EAR_TYPE_5)) return R.drawable.e0831;
        else if(Arrays.equals(codePoints, People_1.EAR_TYPE_6)) return R.drawable.e0832;
        return 0;
    }

    private static int getTypeFaceNose(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.NOSE_TYPE_1_2)) return R.drawable.e0834;
        else if(Arrays.equals(codePoints, People_1.NOSE_TYPE_3)) return R.drawable.e0835;
        else if(Arrays.equals(codePoints, People_1.NOSE_TYPE_4)) return R.drawable.e0836;
        else if(Arrays.equals(codePoints, People_1.NOSE_TYPE_5)) return R.drawable.e0837;
        else if(Arrays.equals(codePoints, People_1.NOSE_TYPE_6)) return R.drawable.e0838;
        return 0;
    }

    private static int getTypeFaceBaby(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.BABY_TYPE_1_2)) return R.drawable.e1014;
        else if(Arrays.equals(codePoints, People_1.BABY_TYPE_3)) return R.drawable.e1015;
        else if(Arrays.equals(codePoints, People_1.BABY_TYPE_4)) return R.drawable.e1016;
        else if(Arrays.equals(codePoints, People_1.BABY_TYPE_5)) return R.drawable.e1017;
        else if(Arrays.equals(codePoints, People_1.BABY_TYPE_6)) return R.drawable.e1018;
        return 0;
    }

    private static int getTypeFaceBoy(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.BOY_TYPE_1_2)) return R.drawable.e0929;
        else if(Arrays.equals(codePoints, People_1.BOY_TYPE_3)) return R.drawable.e0930;
        else if(Arrays.equals(codePoints, People_1.BOY_TYPE_4)) return R.drawable.e0931;
        else if(Arrays.equals(codePoints, People_1.BOY_TYPE_5)) return R.drawable.e0932;
        else if(Arrays.equals(codePoints, People_1.BOY_TYPE_6)) return R.drawable.e0933;
        return 0;
    }

    private static int getTypeFaceGirl(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.GIRL_TYPE_1_2)) return R.drawable.e0935;
        else if(Arrays.equals(codePoints, People_1.GIRL_TYPE_3)) return R.drawable.e0936;
        else if(Arrays.equals(codePoints, People_1.GIRL_TYPE_4)) return R.drawable.e0937;
        else if(Arrays.equals(codePoints, People_1.GIRL_TYPE_5)) return R.drawable.e0938;
        else if(Arrays.equals(codePoints, People_1.GIRL_TYPE_6)) return R.drawable.e0939;
        return 0;
    }

    private static int getTypeFaceMan(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.MAN_TYPE_1_2)) return R.drawable.e0941;
        else if(Arrays.equals(codePoints, People_1.MAN_TYPE_3)) return R.drawable.e0942;
        else if(Arrays.equals(codePoints, People_1.MAN_TYPE_4)) return R.drawable.e0943;
        else if(Arrays.equals(codePoints, People_1.MAN_TYPE_5)) return R.drawable.e0944;
        else if(Arrays.equals(codePoints, People_1.MAN_TYPE_6)) return R.drawable.e0945;
        return 0;
    }

    private static int getTypeFaceWomen(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.WOMEN_TYPE_1_2)) return R.drawable.e0947;
        else if(Arrays.equals(codePoints, People_1.WOMEN_TYPE_3)) return R.drawable.e0948;
        else if(Arrays.equals(codePoints, People_1.WOMEN_TYPE_4)) return R.drawable.e0949;
        else if(Arrays.equals(codePoints, People_1.WOMEN_TYPE_5)) return R.drawable.e0950;
        else if(Arrays.equals(codePoints, People_1.WOMEN_TYPE_6)) return R.drawable.e0951;
        return 0;
    }

    private static int getTypeFacePersonWithBlondHair(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.PERSON_WITH_BLOND_HAIR_TYPE_1_2)) return R.drawable.e0984;
        else if(Arrays.equals(codePoints, People_1.PERSON_WITH_BLOND_HAIR_TYPE_3)) return R.drawable.e0985;
        else if(Arrays.equals(codePoints, People_1.PERSON_WITH_BLOND_HAIR_TYPE_4)) return R.drawable.e0986;
        else if(Arrays.equals(codePoints, People_1.PERSON_WITH_BLOND_HAIR_TYPE_5)) return R.drawable.e0987;
        else if(Arrays.equals(codePoints, People_1.PERSON_WITH_BLOND_HAIR_TYPE_6)) return R.drawable.e0988;
        return 0;
    }

    private static int getTypeFaceOlderMan(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.OLDER_MAN_TYPE_1_2)) return R.drawable.e1002;
        else if(Arrays.equals(codePoints, People_1.OLDER_MAN_TYPE_3)) return R.drawable.e1003;
        else if(Arrays.equals(codePoints, People_1.OLDER_MAN_TYPE_4)) return R.drawable.e1004;
        else if(Arrays.equals(codePoints, People_1.OLDER_MAN_TYPE_5)) return R.drawable.e1005;
        else if(Arrays.equals(codePoints, People_1.OLDER_MAN_TYPE_6)) return R.drawable.e1006;
        return 0;
    }

    private static int getTypeFaceOlderWomen(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.OLDER_WOMEN_TYPE_1_2)) return R.drawable.e1008;
        else if(Arrays.equals(codePoints, People_1.OLDER_WOMEN_TYPE_3)) return R.drawable.e1009;
        else if(Arrays.equals(codePoints, People_1.OLDER_WOMEN_TYPE_4)) return R.drawable.e1010;
        else if(Arrays.equals(codePoints, People_1.OLDER_WOMEN_TYPE_5)) return R.drawable.e1011;
        else if(Arrays.equals(codePoints, People_1.OLDER_WOMEN_TYPE_6)) return R.drawable.e1012;
        return 0;
    }

    private static int getTypeFaceManWithGuaPiMao(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.MAN_WITH_GUA_PI_MAO_TYPE_1_2)) return R.drawable.e0990;
        else if(Arrays.equals(codePoints, People_1.MAN_WITH_GUA_PI_MAO_TYPE_3)) return R.drawable.e0991;
        else if(Arrays.equals(codePoints, People_1.MAN_WITH_GUA_PI_MAO_TYPE_4)) return R.drawable.e0992;
        else if(Arrays.equals(codePoints, People_1.MAN_WITH_GUA_PI_MAO_TYPE_5)) return R.drawable.e0993;
        else if(Arrays.equals(codePoints, People_1.MAN_WITH_GUA_PI_MAO_TYPE_6)) return R.drawable.e0994;
        return 0;
    }

    private static int getTypeFaceManWithTurban(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.MAN_WITH_TURBAN_TYPE_1_2)) return R.drawable.e0996;
        else if(Arrays.equals(codePoints, People_1.MAN_WITH_TURBAN_TYPE_3)) return R.drawable.e0997;
        else if(Arrays.equals(codePoints, People_1.MAN_WITH_TURBAN_TYPE_4)) return R.drawable.e0998;
        else if(Arrays.equals(codePoints, People_1.MAN_WITH_TURBAN_TYPE_5)) return R.drawable.e0999;
        else if(Arrays.equals(codePoints, People_1.MAN_WITH_TURBAN_TYPE_6)) return R.drawable.e1000;
        return 0;
    }

    private static int getTypeFacePoliceOfficer(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.POLICE_OFFICER_TYPE_1_2)) return R.drawable.e0971;
        else if(Arrays.equals(codePoints, People_1.POLICE_OFFICER_TYPE_3)) return R.drawable.e0972;
        else if(Arrays.equals(codePoints, People_1.POLICE_OFFICER_TYPE_4)) return R.drawable.e0973;
        else if(Arrays.equals(codePoints, People_1.POLICE_OFFICER_TYPE_5)) return R.drawable.e0974;
        else if(Arrays.equals(codePoints, People_1.POLICE_OFFICER_TYPE_6)) return R.drawable.e0975;
        return 0;
    }

    private static int getTypeFaceConstructionWorker(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.CONSTRUCTION_WORKER_TYPE_1_2)) return R.drawable.e1020;
        else if(Arrays.equals(codePoints, People_1.CONSTRUCTION_WORKER_TYPE_3)) return R.drawable.e1021;
        else if(Arrays.equals(codePoints, People_1.CONSTRUCTION_WORKER_TYPE_4)) return R.drawable.e1022;
        else if(Arrays.equals(codePoints, People_1.CONSTRUCTION_WORKER_TYPE_5)) return R.drawable.e1023;
        else if(Arrays.equals(codePoints, People_1.CONSTRUCTION_WORKER_TYPE_6)) return R.drawable.e1024;
        return 0;
    }

    private static int getTypeFaceGuardsMan(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.GUARDS_MAN_TYPE_1_2)) return R.drawable.e1051;
        else if(Arrays.equals(codePoints, People_1.GUARDS_MAN_TYPE_3)) return R.drawable.e1052;
        else if(Arrays.equals(codePoints, People_1.GUARDS_MAN_TYPE_4)) return R.drawable.e1053;
        else if(Arrays.equals(codePoints, People_1.GUARDS_MAN_TYPE_5)) return R.drawable.e1054;
        else if(Arrays.equals(codePoints, People_1.GUARDS_MAN_TYPE_6)) return R.drawable.e1055;
        return 0;
    }

    private static int getTypeFaceFatherChristmas(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.FATHER_CHRISTMAS_TYPE_1_2)) return R.drawable.e0616;
        else if(Arrays.equals(codePoints, People_1.FATHER_CHRISTMAS_TYPE_3)) return R.drawable.e0617;
        else if(Arrays.equals(codePoints, People_1.FATHER_CHRISTMAS_TYPE_4)) return R.drawable.e0618;
        else if(Arrays.equals(codePoints, People_1.FATHER_CHRISTMAS_TYPE_5)) return R.drawable.e0619;
        else if(Arrays.equals(codePoints, People_1.FATHER_CHRISTMAS_TYPE_6)) return R.drawable.e0620;
        return 0;
    }

    private static int getTypeFaceBabyAngel(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.BABY_ANGEL_TYPE_1_2)) return R.drawable.e1035;
        else if(Arrays.equals(codePoints, People_1.BABY_ANGEL_TYPE_3)) return R.drawable.e1036;
        else if(Arrays.equals(codePoints, People_1.BABY_ANGEL_TYPE_4)) return R.drawable.e1037;
        else if(Arrays.equals(codePoints, People_1.BABY_ANGEL_TYPE_5)) return R.drawable.e1038;
        else if(Arrays.equals(codePoints, People_1.BABY_ANGEL_TYPE_6)) return R.drawable.e1039;
        return 0;
    }

    private static int getTypeFacePrincess(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.PRINCESS_TYPE_1_2)) return R.drawable.e1026;
        else if(Arrays.equals(codePoints, People_1.PRINCESS_TYPE_3)) return R.drawable.e1027;
        else if(Arrays.equals(codePoints, People_1.PRINCESS_TYPE_4)) return R.drawable.e1028;
        else if(Arrays.equals(codePoints, People_1.PRINCESS_TYPE_5)) return R.drawable.e1029;
        else if(Arrays.equals(codePoints, People_1.PRINCESS_TYPE_6)) return R.drawable.e1030;
        return 0;
    }

    private static int getTypeFaceBrideWithVeil(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.BRIDE_WITH_VEIL_TYPE_1_2)) return R.drawable.e0978;
        else if(Arrays.equals(codePoints, People_1.BRIDE_WITH_VEIL_TYPE_3)) return R.drawable.e0979;
        else if(Arrays.equals(codePoints, People_1.BRIDE_WITH_VEIL_TYPE_4)) return R.drawable.e0980;
        else if(Arrays.equals(codePoints, People_1.BRIDE_WITH_VEIL_TYPE_5)) return R.drawable.e0981;
        else if(Arrays.equals(codePoints, People_1.BRIDE_WITH_VEIL_TYPE_6)) return R.drawable.e0982;
        return 0;
    }

    private static int getTypeFacePedestrian(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.PEDESTRIAN_TYPE_1_2)) return R.drawable.e1548;
        else if(Arrays.equals(codePoints, People_1.PEDESTRIAN_TYPE_3)) return R.drawable.e1549;
        else if(Arrays.equals(codePoints, People_1.PEDESTRIAN_TYPE_4)) return R.drawable.e1550;
        else if(Arrays.equals(codePoints, People_1.PEDESTRIAN_TYPE_5)) return R.drawable.e1551;
        else if(Arrays.equals(codePoints, People_1.PEDESTRIAN_TYPE_6)) return R.drawable.e1552;
        return 0;
    }

    private static int getTypeFaceRunner(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.RUNNER_TYPE_1_2)) return R.drawable.e0678;
        else if(Arrays.equals(codePoints, People_1.RUNNER_TYPE_3)) return R.drawable.e0679;
        else if(Arrays.equals(codePoints, People_1.RUNNER_TYPE_4)) return R.drawable.e0680;
        else if(Arrays.equals(codePoints, People_1.RUNNER_TYPE_5)) return R.drawable.e0681;
        else if(Arrays.equals(codePoints, People_1.RUNNER_TYPE_6)) return R.drawable.e0682;
        return 0;
    }

    private static int getTypeFaceDancer(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.DANCER_TYPE_1_2)) return R.drawable.e1057;
        else if(Arrays.equals(codePoints, People_1.DANCER_TYPE_3)) return R.drawable.e1058;
        else if(Arrays.equals(codePoints, People_1.DANCER_TYPE_4)) return R.drawable.e1059;
        else if(Arrays.equals(codePoints, People_1.DANCER_TYPE_5)) return R.drawable.e1060;
        else if(Arrays.equals(codePoints, People_1.DANCER_TYPE_6)) return R.drawable.e1061;
        return 0;
    }

    private static int getTypeFacePersonBowingDeeply(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.PERSON_BOWING_DEEPLY_TYPE_1_2)) return R.drawable.e1440;
        else if(Arrays.equals(codePoints, People_1.PERSON_BOWING_DEEPLY_TYPE_3)) return R.drawable.e1441;
        else if(Arrays.equals(codePoints, People_1.PERSON_BOWING_DEEPLY_TYPE_4)) return R.drawable.e1442;
        else if(Arrays.equals(codePoints, People_1.PERSON_BOWING_DEEPLY_TYPE_5)) return R.drawable.e1443;
        else if(Arrays.equals(codePoints, People_1.PERSON_BOWING_DEEPLY_TYPE_6)) return R.drawable.e1444;
        return 0;
    }

    private static int getTypeFaceInformationDeskPerson(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.INFORMATION_DESK_PERSON_TYPE_1_2)) return R.drawable.e1045;
        else if(Arrays.equals(codePoints, People_1.INFORMATION_DESK_PERSON_TYPE_3)) return R.drawable.e1046;
        else if(Arrays.equals(codePoints, People_1.INFORMATION_DESK_PERSON_TYPE_4)) return R.drawable.e1047;
        else if(Arrays.equals(codePoints, People_1.INFORMATION_DESK_PERSON_TYPE_5)) return R.drawable.e1048;
        else if(Arrays.equals(codePoints, People_1.INFORMATION_DESK_PERSON_TYPE_6)) return R.drawable.e1049;
        return 0;
    }

    private static int getTypeFaceFaceWithNoGoodGesture(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.FACE_WITH_NO_GOOD_GESTURE_TYPE_1_2)) return R.drawable.e1428;
        else if(Arrays.equals(codePoints, People_1.FACE_WITH_NO_GOOD_GESTURE_TYPE_3)) return R.drawable.e1429;
        else if(Arrays.equals(codePoints, People_1.FACE_WITH_NO_GOOD_GESTURE_TYPE_4)) return R.drawable.e1430;
        else if(Arrays.equals(codePoints, People_1.FACE_WITH_NO_GOOD_GESTURE_TYPE_5)) return R.drawable.e1431;
        else if(Arrays.equals(codePoints, People_1.FACE_WITH_NO_GOOD_GESTURE_TYPE_6)) return R.drawable.e1432;
        return 0;
    }

    private static int getTypeFaceFaceWithOkGesture(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.FACE_WITH_OK_GESTURE_TYPE_1_2)) return R.drawable.e1434;
        else if(Arrays.equals(codePoints, People_1.FACE_WITH_OK_GESTURE_TYPE_3)) return R.drawable.e1435;
        else if(Arrays.equals(codePoints, People_1.FACE_WITH_OK_GESTURE_TYPE_4)) return R.drawable.e1436;
        else if(Arrays.equals(codePoints, People_1.FACE_WITH_OK_GESTURE_TYPE_5)) return R.drawable.e1437;
        else if(Arrays.equals(codePoints, People_1.FACE_WITH_OK_GESTURE_TYPE_6)) return R.drawable.e1438;
        return 0;
    }

    private static int getTypeFacePersonRaiseOneHand(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.HAPPY_PERSON_RAISE_ONE_HAND_TYPE_1_2)) return R.drawable.e1449;
        else if(Arrays.equals(codePoints, People_1.HAPPY_PERSON_RAISE_ONE_HAND_TYPE_3)) return R.drawable.e1450;
        else if(Arrays.equals(codePoints, People_1.HAPPY_PERSON_RAISE_ONE_HAND_TYPE_4)) return R.drawable.e1451;
        else if(Arrays.equals(codePoints, People_1.HAPPY_PERSON_RAISE_ONE_HAND_TYPE_5)) return R.drawable.e1452;
        else if(Arrays.equals(codePoints, People_1.HAPPY_PERSON_RAISE_ONE_HAND_TYPE_6)) return R.drawable.e1453;
        return 0;
    }

    private static int getTypeFacePersonWithPoutingFace(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.PERSON_WITH_POUTING_FACE_TYPE_1_2)) return R.drawable.e1467;
        else if(Arrays.equals(codePoints, People_1.PERSON_WITH_POUTING_FACE_TYPE_3)) return R.drawable.e1468;
        else if(Arrays.equals(codePoints, People_1.PERSON_WITH_POUTING_FACE_TYPE_4)) return R.drawable.e1469;
        else if(Arrays.equals(codePoints, People_1.PERSON_WITH_POUTING_FACE_TYPE_5)) return R.drawable.e1470;
        else if(Arrays.equals(codePoints, People_1.PERSON_WITH_POUTING_FACE_TYPE_6)) return R.drawable.e1471;
        return 0;
    }

    private static int getTypeFacePersonFrowning(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.PERSON_FROWNING_TYPE_1_2)) return R.drawable.e1461;
        else if(Arrays.equals(codePoints, People_1.PERSON_FROWNING_TYPE_3)) return R.drawable.e1462;
        else if(Arrays.equals(codePoints, People_1.PERSON_FROWNING_TYPE_4)) return R.drawable.e1463;
        else if(Arrays.equals(codePoints, People_1.PERSON_FROWNING_TYPE_5)) return R.drawable.e1464;
        else if(Arrays.equals(codePoints, People_1.PERSON_FROWNING_TYPE_6)) return R.drawable.e1465;
        return 0;
    }

    private static int getTypeFaceHairCut(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.HAIRCUT_TYPE_1_2)) return R.drawable.e1076;
        else if(Arrays.equals(codePoints, People_1.HAIRCUT_TYPE_3)) return R.drawable.e1077;
        else if(Arrays.equals(codePoints, People_1.HAIRCUT_TYPE_4)) return R.drawable.e1078;
        else if(Arrays.equals(codePoints, People_1.HAIRCUT_TYPE_5)) return R.drawable.e1079;
        else if(Arrays.equals(codePoints, People_1.HAIRCUT_TYPE_6)) return R.drawable.e1080;
        return 0;
    }

    private static int getTypeFaceFaceMassage(int[] codePoints) {
        if(Arrays.equals(codePoints, People_1.FACE_MASSAGE_TYPE_1_2)) return R.drawable.e1070;
        else if(Arrays.equals(codePoints, People_1.FACE_MASSAGE_TYPE_3)) return R.drawable.e1071;
        else if(Arrays.equals(codePoints, People_1.FACE_MASSAGE_TYPE_4)) return R.drawable.e1072;
        else if(Arrays.equals(codePoints, People_1.FACE_MASSAGE_TYPE_5)) return R.drawable.e1073;
        else if(Arrays.equals(codePoints, People_1.FACE_MASSAGE_TYPE_6)) return R.drawable.e1074;
        return 0;
    }

    private static int getTypeFaceRowBoat(int[] codePoints) {
        if(Arrays.equals(codePoints, Activity_4.ROW_BOAT_TYPE_1_2)) return R.drawable.e1514;
        else if(Arrays.equals(codePoints, Activity_4.ROW_BOAT_TYPE_3)) return R.drawable.e1515;
        else if(Arrays.equals(codePoints, Activity_4.ROW_BOAT_TYPE_4)) return R.drawable.e1516;
        else if(Arrays.equals(codePoints, Activity_4.ROW_BOAT_TYPE_5)) return R.drawable.e1517;
        else if(Arrays.equals(codePoints, Activity_4.ROW_BOAT_TYPE_6)) return R.drawable.e1518;
        return 0;
    }

    private static int getTypeFaceSwimmer(int[] codePoints) {
        if(Arrays.equals(codePoints, Activity_4.SWIMMER_TYPE_1_2)) return R.drawable.e0700;
        else if(Arrays.equals(codePoints, Activity_4.SWIMMER_TYPE_3)) return R.drawable.e0701;
        else if(Arrays.equals(codePoints, Activity_4.SWIMMER_TYPE_4)) return R.drawable.e0702;
        else if(Arrays.equals(codePoints, Activity_4.SWIMMER_TYPE_5)) return R.drawable.e0703;
        else if(Arrays.equals(codePoints, Activity_4.SWIMMER_TYPE_6)) return R.drawable.e0704;
        return 0;
    }

    private static int getTypeFaceSurfer(int[] codePoints) {
        if(Arrays.equals(codePoints, Activity_4.SURFER_TYPE_1_2)) return R.drawable.e0684;
        else if(Arrays.equals(codePoints, Activity_4.SURFER_TYPE_3)) return R.drawable.e0685;
        else if(Arrays.equals(codePoints, Activity_4.SURFER_TYPE_4)) return R.drawable.e0686;
        else if(Arrays.equals(codePoints, Activity_4.SURFER_TYPE_5)) return R.drawable.e0687;
        else if(Arrays.equals(codePoints, Activity_4.SURFER_TYPE_6)) return R.drawable.e0688;
        return 0;
    }

    private static int getTypeFaceBath(int[] codePoints) {
        if(Arrays.equals(codePoints, Activity_4.BATH_TYPE_1_2)) return R.drawable.e1563;
        else if(Arrays.equals(codePoints, Activity_4.BATH_TYPE_3)) return R.drawable.e1564;
        else if(Arrays.equals(codePoints, Activity_4.BATH_TYPE_4)) return R.drawable.e1565;
        else if(Arrays.equals(codePoints, Activity_4.BATH_TYPE_5)) return R.drawable.e1566;
        else if(Arrays.equals(codePoints, Activity_4.BATH_TYPE_6)) return R.drawable.e1567;
        return 0;
    }

    private static int getTypeFacePersonWithBall(int[] codePoints) {
        if(Arrays.equals(codePoints, Activity_4.PERSON_WITH_BALL_TYPE_1_2)) return R.drawable.e0132;
        else if(Arrays.equals(codePoints, Activity_4.PERSON_WITH_BALL_TYPE_3)) return R.drawable.e0133;
        else if(Arrays.equals(codePoints, Activity_4.PERSON_WITH_BALL_TYPE_4)) return R.drawable.e0134;
        else if(Arrays.equals(codePoints, Activity_4.PERSON_WITH_BALL_TYPE_5)) return R.drawable.e0135;
        else if(Arrays.equals(codePoints, Activity_4.PERSON_WITH_BALL_TYPE_6)) return R.drawable.e0136;
        return 0;
    }

    private static int getTypeFaceWeightLifter(int[] codePoints) {
        if(Arrays.equals(codePoints, Activity_4.WEIGHT_LIFTER_TYPE_1_2)) return R.drawable.e0706;
        else if(Arrays.equals(codePoints, Activity_4.WEIGHT_LIFTER_TYPE_3)) return R.drawable.e0707;
        else if(Arrays.equals(codePoints, Activity_4.WEIGHT_LIFTER_TYPE_4)) return R.drawable.e0708;
        else if(Arrays.equals(codePoints, Activity_4.WEIGHT_LIFTER_TYPE_5)) return R.drawable.e0709;
        else if(Arrays.equals(codePoints, Activity_4.WEIGHT_LIFTER_TYPE_6)) return R.drawable.e0710;
        return 0;
    }

    private static int getTypeFaceBicyclist(int[] codePoints) {
        if(Arrays.equals(codePoints, Activity_4.BICYCLIST_TYPE_1_2)) return R.drawable.e1536;
        else if(Arrays.equals(codePoints, Activity_4.BICYCLIST_TYPE_3)) return R.drawable.e1537;
        else if(Arrays.equals(codePoints, Activity_4.BICYCLIST_TYPE_4)) return R.drawable.e1538;
        else if(Arrays.equals(codePoints, Activity_4.BICYCLIST_TYPE_5)) return R.drawable.e1539;
        else if(Arrays.equals(codePoints, Activity_4.BICYCLIST_TYPE_6)) return R.drawable.e1540;
        return 0;
    }

    private static int getTypeFaceMountainBicyclist(int[] codePoints) {
        if(Arrays.equals(codePoints, Activity_4.MOUNTAIN_BICYCLIST_TYPE_1_2)) return R.drawable.e1541;
        else if(Arrays.equals(codePoints, Activity_4.MOUNTAIN_BICYCLIST_TYPE_3)) return R.drawable.e1542;
        else if(Arrays.equals(codePoints, Activity_4.MOUNTAIN_BICYCLIST_TYPE_4)) return R.drawable.e1543;
        else if(Arrays.equals(codePoints, Activity_4.MOUNTAIN_BICYCLIST_TYPE_5)) return R.drawable.e1544;
        else if(Arrays.equals(codePoints, Activity_4.MOUNTAIN_BICYCLIST_TYPE_6)) return R.drawable.e1545;
        return 0;
    }

    private static int getTypeFaceHorseRacing(int[] codePoints) {
        if(Arrays.equals(codePoints, Activity_4.HORSE_RACING_TYPE_1_2)) return R.drawable.e0692;
        else if(Arrays.equals(codePoints, Activity_4.HORSE_RACING_TYPE_3)) return R.drawable.e0693;
        else if(Arrays.equals(codePoints, Activity_4.HORSE_RACING_TYPE_4)) return R.drawable.e0694;
        else if(Arrays.equals(codePoints, Activity_4.HORSE_RACING_TYPE_5)) return R.drawable.e0695;
        else if(Arrays.equals(codePoints, Activity_4.HORSE_RACING_TYPE_6)) return R.drawable.e0696;
        return 0;
    }
}