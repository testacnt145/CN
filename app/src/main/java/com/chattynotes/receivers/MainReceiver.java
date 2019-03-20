//package com.chattynotes.receivers;
//
//		import com.chattynotes.util.AndServiceUtil;
//		import android.content.BroadcastReceiver;
//		import android.content.Context;
//		import android.content.Intent;
//		import android.net.ConnectivityManager;
//
//public class MainReceiver extends BroadcastReceiver {
//	public void onReceive(Context ctx, Intent intent) {
//		if(intent != null) {
//			switch(intent.getAction()) {
////______________________________________________________________________________________
//				case Intent.ACTION_BOOT_COMPLETED:
//					//@link http://stackoverflow.com/a/30970167/4754141
//					//(NOT OFFICIAL)
//					//doing it without a wake lock runs the risk of your Service getting killed before it is finished processing
//					AndServiceUtil.startServiceApp(ctx);
//					break;
////______________________________________________________________________________________
//				case ConnectivityManager.CONNECTIVITY_ACTION:
//					AndServiceUtil.startServiceApp(ctx);
//					break;
////______________________________________________________________________________________
//			}
//		}
//	}
//
//}
