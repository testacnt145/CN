package com.chattynotes.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;
import com.chattynotes.application.App;

public class NetworkUtil {

	public static Boolean checkInternet() {
    	ConnectivityManager cm = (ConnectivityManager) App.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
	}

	public static void internetNotAvailableDialog(Context _ctx) {
		new AlertDialog.Builder(_ctx).setTitle("Connectivity Error")
		.setMessage("Internet not available")
		.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		})
		.show();
	}

	public static void internetNotAvailableToast() {
		Toast.makeText(App.applicationContext, "Internet not available", Toast.LENGTH_SHORT).show();
	}

	public static void NoResponseToast() {
		Toast.makeText(App.applicationContext, "No response from server. Please try again.", Toast.LENGTH_SHORT).show();
	}

	public static void fileNotFoundToast() {
		Toast.makeText(App.applicationContext, "File not found.", Toast.LENGTH_SHORT).show();
	}
}
