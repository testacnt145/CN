package com.chattynotes.util;

import com.chattynotes.constant.AppConst;

import java.text.DecimalFormat;
import java.util.Locale;

public class ConversionUtil {

//________________________________________________________________________________________________
//	//http://stackoverflow.com/questions/1486295/replace-all-non-digits-with-an-empty-character-in-a-string
//	String removeNonDigitInString(String value) {
//		//923333101062@example.com  ==> 923333101062
//		return value.replaceAll("\\D+", "");
//	}
//
	public static int convertStringToInteger(String value) {
		//http://stackoverflow.com/questions/7355024/integer-valueof-vs-integer-parseint
		// YES	Integer.parseInt()	int
		// NO	Integer.valueOf()	Integer
		return Integer.parseInt(value);
	}

	public static long convertStringToLong(String value) {
		return Long.parseLong(value);
	}
	

//________________________________________________________________________________________________ TELEGRAM METHOD
	public static String formatFileSize(long size) {
        if (size < 1024) {
            return String.format(Locale.getDefault(), "%d B", size);
        } else if (size < 1024 * 1024) {
            //return String.format(Locale.getDefault(), "%.1f KB", size / 1024.0f); // 1 digit after decimal
            return String.format(Locale.getDefault(), "%.0f KB", size / 1024.0f);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format(Locale.getDefault(), "%.1f MB", size / 1024.0f / 1024.0f);
        } else {
            //return String.format(Locale.getDefault(), "%.1f GB", size / 1024.0f / 1024.0f / 1024.0f);
            return String.format(Locale.getDefault(), "%.0f GB", size / 1024.0f / 1024.0f / 1024.0f);
        }
    }
	
//__________________________________________________________________________________________________custom
	private static final float MB_IN_BITS = 1048576f; //1MB = 1048576f Bits i.e 1024 * 1024
	public static float bitsToMb(float _sizeInBits) {
		return _sizeInBits / 1024f / 1024f;
		//return _sizeInBits / 1048576f; 		//same as above
		//return _sizeInBits / 1024f * 1024f; 	//wrong
	}
	
	//16Mb is ? percent of 124Mb(Total Video Size)  =>  16/124 * 100 
	//But we will * it my 1 as we need progress in between 0.0 to 1.0
	public static float calculatePercentageForMaxStepSize(float _sizeInBits) {
		//_sizeInBits = Total Video Size
		return (AppConst.MAX_VIDEO_SIZE * MB_IN_BITS) / _sizeInBits * 1;
	} 
	
	//1second is ? percent of 124Seconds(Total Video Duration)  =>  1/124 * 100 
	public static float calculatePercentageForMinStepSize(float _durationInMilliseconds) {
		return (AppConst.MIN_VIDEO_TIME * 1000) / _durationInMilliseconds * 1;
	} 
	
	
	public static String formatSize(float _sizeInBits) {
		//return new DecimalFormat("#.#").format(_sizeInBits/1048576f);//show only 1 decimal places 15.2347325 => 15.2
		return new DecimalFormat("#").format(_sizeInBits / MB_IN_BITS);
	}
	
	
	public static String formatDuration(float _durationInMillis) {
		int sec  = (int)(_durationInMillis/ 1000) % 60 ;
		int min  = (int)((_durationInMillis/ (1000*60)) % 60);
		int hr   = (int)((_durationInMillis/ (1000*60*60)) % 24);
		if(hr == 0)
			return String.format(Locale.getDefault(), "%02d", min) + ":" + String.format(Locale.getDefault(), "%02d", sec);
		else
			return String.format(Locale.getDefault(), "%02d", hr) + ":" + String.format(Locale.getDefault(), "%02d", min) + ":" + String.format(Locale.getDefault(), "%02d", sec);
	}
	
	public static String formatDurationForFFMPEG(float _durationInMillis) {
		int sec  = (int)(_durationInMillis/ 1000) % 60 ;
		int min  = (int)((_durationInMillis/ (1000*60)) % 60);
		int hr   = (int)((_durationInMillis/ (1000*60*60)) % 24);
		return String.format(Locale.getDefault(), "%02d", hr) + ":" + String.format(Locale.getDefault(), "%02d", min) + ":" + String.format(Locale.getDefault(), "%02d", sec);
	}
  
}
