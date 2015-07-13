package org.aurona.lib.bitmap.output.share;

import org.aurona.lib.packages.OtherAppPackages;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;

public class ShareToFacebook {

	
	public static void shareImage(Activity activity, Bitmap bmp){
		ShareToApp.shareImage(activity,OtherAppPackages.facebookPackage,"sharefb",ShareTag.getDefaultTag(activity),bmp);	
	}
	
	public static void shareImageFromUri(Activity activity, Uri bitmapUri){
		ShareToApp.shareImageFromUri(activity,OtherAppPackages.facebookPackage,"sharefb",ShareTag.getDefaultTag(activity),bitmapUri);
	}
}
