package org.aurona.lib.bitmap.output.share;

import org.aurona.lib.packages.AppPackages;
import org.aurona.lib.packages.OtherAppPackages;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import org.aurona.lib.sysutillib.R;

public class ShareToWeibo {

	
	public static void shareImage(Activity activity, Bitmap bmp){
		ShareToApp.shareImage(activity,OtherAppPackages.weiboPackage,"sharewb",ShareTag.getDefaultTag(activity),bmp);	
	}
	
	public static void shareImageFromUri(Activity activity, Uri bitmapUri){
		ShareToApp.shareImageFromUri(activity,OtherAppPackages.weiboPackage,"sharewb",ShareTag.getDefaultTag(activity),bitmapUri);
	}
}
