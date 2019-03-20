package com.chattynotes.util;

import com.chattynotes.R;
import com.chattynotes.application.App;

public class WallpaperUtil {

    public final static int DEFAULT_WALLPAPER_NUMBER = -1;
    public final static int NONE_WALLPAPER_NUMBER = -2;

    //public final static String WALLPAPER_PICKER_PACKAGE_NAME  = App.PACKAGE_NAME + ".WallpaperPicker";
    public final static String WALLPAPER_DEFAULT_PACKAGE_NAME   = App.PACKAGE_NAME + ".WallpaperDefault";
    public final static String WALLPAPER_NONE_PACKAGE_NAME      = App.PACKAGE_NAME + ".WallpaperNone";
    public final static String WALLPAPER_COLOR_PACKAGE_NAME     = App.PACKAGE_NAME + ".WallpaperColor";

    //if you change here, change also in AndroidManifest.xml
    //public final static String WALLPAPER_PICKER_ACTION= App.PACKAGE_NAME + ".intent.action.WALLPAPER_PICKER";
    public final static String WALLPAPER_DEFAULT_ACTION = App.PACKAGE_NAME + ".intent.action.WALLPAPER_DEFAULT";
    public final static String WALLPAPER_NONE_ACTION    = App.PACKAGE_NAME + ".intent.action.WALLPAPER_NONE";
    public final static String WALLPAPER_COLOR_ACTION   = App.PACKAGE_NAME + ".intent.action.WALLPAPER_COLOR";

    public static int getWallpaperDrawable(int wallpaperNumber) {
        switch(wallpaperNumber) {
            case WallpaperUtil.DEFAULT_WALLPAPER_NUMBER:
                return R.drawable.background_0;
            //starts from 0
            case 0:
                return R.color.color1;
            case 1:
                return R.color.color2;
            case 2:
                return R.color.color3;
            case 3:
                return R.color.color4;
            case 4:
                return R.color.color5;
            case 5:
                return R.color.color6;
            case 6:
                return R.color.color7;
            case 7:
                return R.color.color8;
            case 8:
                return R.color.color9;
            case 9:
                return R.color.color10;
            case 10:
                return R.color.color11;
            case 11:
                return R.color.color12;
            case 12:
                return R.color.color13;
            case 13:
                return R.color.color14;
            case 14:
                return R.color.color15;
            default:
                return android.R.color.white;
        }
    }
}
