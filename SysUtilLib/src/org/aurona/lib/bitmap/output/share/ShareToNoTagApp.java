package org.aurona.lib.bitmap.output.share;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;

public class ShareToNoTagApp {

	public static void shareImage(Activity activity, String packageName, Bitmap bmp){
		ShareToApp.shareImage(activity, packageName, "share", "", bmp);
	}
	
	public static void shareImageFromUri(Activity activity, String packageName, Uri bitmapUri){
		ShareToApp.shareImageFromUri(activity, packageName, "share", "", bitmapUri);
	}
	
	public static void shareImage(Activity activity, Bitmap bmp){
		ShareToApp.shareImage(activity, null, "share", "", bmp);
	}
	
	public static void shareImageFromUri(Activity activity, Uri bitmapUri){
		ShareToApp.shareImageFromUri(activity,null,"share","",bitmapUri);
	}
}
