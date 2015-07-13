package org.aurona.lib.share;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

import org.aurona.lib.io.BitmapIoCache;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.widget.Toast;

public class ShareOtherApp {
	

	public interface ShareCallBack{
		public void onResult(boolean isInstall, boolean isSucc);
	}
	
	static String shareText = "";
	
	public static void SetShareText(String text){
		shareText = text;
	}
	
	public static boolean saveToCameraRoll(Context activity,String folder,String filename,Bitmap bitmap){
		boolean bHaveSdcard = false;
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			bHaveSdcard = true;
		}

		String path = "";
		if (bHaveSdcard) {
			String extStorageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
			path = extStorageDirectory;
			if(folder != null){
				path += "/" + folder;
			}
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
		}else{
			//Toast.makeText(activity, "Need SD Card", Toast.LENGTH_LONG).show();
			return false;
		}

		Date dt = new Date();
		String filepathname = path + "/" + filename;
		FileOutputStream fos = null;
		
//		if(bitmap == null){
//			FlurryAgent.logEvent("SaveBitmapNull");
//		}
		
		int fsize = 0;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		
		if(bitmap == null || bitmap.isRecycled()) return false;
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] data = baos.toByteArray();

		try{
			fsize = data.length;
			fos = new FileOutputStream(filepathname);
			fos.write(data, 0, fsize);
			fos.close();
			
			MediaScannerConnection.scanFile(activity,
					new String[] { filepathname }, null,
					new MediaScannerConnection.OnScanCompletedListener() {
						@Override
						public void onScanCompleted(String path, Uri uri) {
						}
					});
		}catch(Exception e){
			//FlurryAgent.logEvent("SaveException");
		}

		return true;
	}
	
	public static boolean saveToCameraRoll(Context activity,String folder,String filename,Bitmap bitmap,Bitmap.CompressFormat format){
		boolean bHaveSdcard = false;
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			bHaveSdcard = true;
		}
		
		String path = "";
		if (bHaveSdcard) {
			String extStorageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
			path = extStorageDirectory;
			if(folder != null){
				path += "/" + folder;
			}
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
		}else{
			//Toast.makeText(activity, "Need SD Card", Toast.LENGTH_LONG).show();
			return false;
		}

		Date dt = new Date();
		String filepathname = path + "/" + filename;
		FileOutputStream fos = null;
		
		int fsize = 0;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if(format == null) format = Bitmap.CompressFormat.JPEG;

		try{

			if(bitmap == null || bitmap.isRecycled())
			{
				return false;
			}
			bitmap.compress(format, 100, baos);
			byte[] data = baos.toByteArray();
			
			fsize = data.length;
			fos = new FileOutputStream(filepathname);
			fos.write(data, 0, fsize);
			fos.close();
			
			MediaScannerConnection.scanFile(activity,
					new String[] { filepathname }, null,
					new MediaScannerConnection.OnScanCompletedListener() {
						@Override
						public void onScanCompleted(String path, Uri uri) {
						}
					});
		}catch(Exception e){
			//FlurryAgent.logEvent("SaveException");
			return false;
		}
		return true;
	}

	public static boolean saveToCameraRoll(Context activity,String folder,Bitmap bitmap){
		if(bitmap == null || bitmap.isRecycled())
		{
			return false;
		}
		Date dt = new Date();
		long ldt = dt.getTime();
		String filename = "img" + String.valueOf(ldt) + ".jpg";
		return saveToCameraRoll(activity,folder,filename,bitmap);
	}
	
	public static  boolean isAppInstall(Context context, String packageName){
    	boolean bInstalled = false;
    	if(packageName == null) return false;
		PackageInfo packageInfo = null;
		
    	try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (NameNotFoundException e) {
            packageInfo = null;
        }
    	
        if(packageInfo ==null){
        	bInstalled = false;
        }else{
        	bInstalled = true;
        }
        
    	return bInstalled;
}
	
	/* Bmp Share To Other App*/
	public static boolean toOtherApp(Activity activity,String packageName,String title,String text,Bitmap bmp){
		try{
			//String path = FileCache.getInstance().cacheJPG(title+".jpg", bmp);
			if(text == "")
			{
				text = shareText;
			}
			if(bmp == null || bmp.isRecycled())
			{
				return false;
			}
			String path = BitmapIoCache.putJPG(title+".jpg", bmp);
			File f = new File(path);
			Uri uri = Uri.fromFile(f);					
			Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
		    shareIntent.setType("image/*"); // set mime type 
		    shareIntent.putExtra(Intent.EXTRA_STREAM,uri); // set uri 
		    shareIntent.putExtra(Intent.EXTRA_TITLE, title);
		    shareIntent.putExtra(Intent.EXTRA_TEXT, text);
		    
		    if(packageName != null){
			    shareIntent.setPackage(packageName);		    	
		    }
		    
		    activity.startActivity(shareIntent);
		    return true;
		}catch(Exception ex){
			return false;
		}
	}
	
	public static boolean toOtherAppFromUri(Activity activity,String packageName,String title,String text,Uri bmpPath){
		try{
			//String path = FileCache.getInstance().cacheJPG(title+".jpg", bmp);
			if(text == "")
			{
				text = shareText;
			}
			if(bmpPath == null)
			{
				return false;
			}

			Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
		    shareIntent.setType("image/*"); // set mime type 
		    shareIntent.putExtra(Intent.EXTRA_STREAM,bmpPath); // set uri 
		    shareIntent.putExtra(Intent.EXTRA_TITLE, title);
		    shareIntent.putExtra(Intent.EXTRA_TEXT, text);
		    
		    if(packageName != null){
			    shareIntent.setPackage(packageName);		    	
		    }
		    
		    activity.startActivity(shareIntent);
		    return true;
		}catch(Exception ex){
			return false;
		}
	}

	/* Text Share To Other App*/
	public static boolean toOtherApp(Activity activity,String packageName,String title,String text){
		try{
			Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
		    shareIntent.setType("text/plain"); // set mime type 
		    shareIntent.putExtra(Intent.EXTRA_SUBJECT,title); // set uri 
		    shareIntent.putExtra(Intent.EXTRA_TEXT, text);
		    
		    if(packageName != null){
			    shareIntent.setPackage(packageName);		    	
		    }
		    
		    activity.startActivity(shareIntent);
		    return true;
		}catch(Exception ex){
			return false;
		}
	}


	public static void thirdPartAppIterator(Context context){
		Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
		//shareIntent.setType("image/*"); // set mime type
		
		shareIntent.setType("*");
		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
		
		for (final ResolveInfo app : activityList) {
			  String t_name = app.activityInfo.name;	 
			  if ((app.activityInfo.name).contains("sina")) { // search for instagram from app list
				  final ActivityInfo activity = app.activityInfo;
				  final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
			  }
		}
	}
	
	
	public static void toInstagram(final Activity activity,final Bitmap bmp,String text, int index){
		String pk = "com.instagram.android";
		
		boolean isShare = toOtherApp(activity,pk,String.valueOf(index),text,bmp);	
		if(isShare){
			//Toast.makeText(activity, "share success", Toast.LENGTH_LONG).show();
		}
	}

		
	public static void toInstagram(final Activity activity,final Bitmap bmp, int index){
		String pk = "com.instagram.android";
		boolean isShare = toOtherApp(activity,pk,String.valueOf(index),shareText,bmp);	
		if(isShare){
			//Toast.makeText(activity, "share success", Toast.LENGTH_LONG).show();
		}
	}
		
	public static boolean isInstagramInstall(Context context){
		String pk = "com.instagram.android";
		return isAppInstall(context,pk);
	}
	
	public static void shareFacebook(final Activity activity,final Bitmap bmp, int index){
		String pk = "com.facebook.katana";
		boolean isShare = toOtherApp(activity,pk,String.valueOf(index),shareText,bmp);	
		if(isShare){
			//Toast.makeText(activity, "share success", Toast.LENGTH_LONG).show();
		}
	}

	public static boolean isFacebookInstall(Context context){
		String pk = "com.facebook.katana";
		return isAppInstall(context,pk);
	}
	
	public static void shareTwitter(final Activity activity,final Bitmap bmp, int index){
		String pk = "com.twitter.android";
		boolean isShare = toOtherApp(activity,pk,String.valueOf(index),shareText,bmp);	
		if(isShare){
			//Toast.makeText(activity, "share success", Toast.LENGTH_LONG).show();
		}
	}

	public static boolean isTwitterInstall(Context context){
		String pk = "com.twitter.android";
		return isAppInstall(context,pk);
	}
	
	public static void shareLine(final Activity activity,final Bitmap bmp, int index){
		String pk = "jp.naver.line.android";
		boolean isShare = toOtherApp(activity,pk,String.valueOf(index),shareText,bmp);	
		if(isShare){
			//Toast.makeText(activity, "share success", Toast.LENGTH_LONG).show();
		}
	}
	
	public static boolean isLineInstall(Context context){
		String pk = "jp.naver.line.android";
		return isAppInstall(context,pk);
	}
	
	public static void shareTumblr(final Activity activity,final Bitmap bmp, int index){
		String pk = "com.tumblr";
		boolean isShare = toOtherApp(activity,pk,String.valueOf(index),shareText,bmp);	
		if(isShare){
			//Toast.makeText(activity, "share success", Toast.LENGTH_LONG).show();
		}
	}
	
	public static boolean isTumblrInstall(Context context){
		String pk = "com.tumblr";
		return isAppInstall(context,pk);
	}
	

	public static void shareWhatsApp(final Activity activity,final Bitmap bmp, int index){
		String pk = "com.whatsapp";
		boolean isShare = toOtherApp(activity,pk,String.valueOf(index),shareText,bmp);	
		if(isShare){
			//Toast.makeText(activity, "share success", Toast.LENGTH_LONG).show();
		}
	}
	
	public static boolean isWhatsAppInstall(Context context){
		String pk = "com.whatsapp";
		return isAppInstall(context,pk);
	}
	
	public static void shareSina(final Activity activity,final Bitmap bmp, int index){
		String pk = "com.sina.weibo";
		boolean isShare = toOtherApp(activity,pk,String.valueOf(index),  shareText+"#",bmp);	
		if(isShare){
			//Toast.makeText(activity, "share success", Toast.LENGTH_LONG).show();
		}
	}
	
	public static boolean isSinaInstall(Context context){
		String pk = "com.sina.weibo";
		return isAppInstall(context,pk);
	}

	public static void shareQQZone(final Activity activity,final Bitmap bmp, int index){
		String pk = "com.qzone";
		boolean isShare = toOtherApp(activity,pk,String.valueOf(index),shareText,bmp);	
		if(isShare){
			//Toast.makeText(activity, "share success", Toast.LENGTH_LONG).show();
		}
	}
	
	public static boolean isQQZoneInstall(Context context){
		String pk = "com.qzone";
		return isAppInstall(context,pk);
	}

	public static void shareQQ(final Activity activity,final Bitmap bmp, int index){
		String pk = "com.tencent.mobileqq";
		boolean isShare = toOtherApp(activity,pk,String.valueOf(index),shareText,bmp);	
		if(isShare){
			//Toast.makeText(activity, "share success", Toast.LENGTH_LONG).show();
		}
	}

	public static boolean isQQInstall(Context context){
		String pk = "com.tencent.mobileqq";
		return isAppInstall(context,pk);
	}
//	
//	public static void shareFacebookItem(final Activity activity,final Bitmap bmp,String filterName,Object obj,String shareText,final ShareCallBack callback){
//		shareNormalItem(activity,bmp,"com.facebook.katana","Facebook",shareText,
//				activity.getString(R.string.shareError),"ShareFaceBook",activity.getString(R.string.shareFacebookNotInstall),callback);
//	}
//	
//	public static void shareTwitterItem(final Activity activity,final Bitmap bmp,String filterName,Object obj,String shareText,final ShareCallBack callback){
//		shareNormalItem(activity,bmp,"com.twitter.android","Twitter",shareText,activity.getString(R.string.shareError),"ShareTwitter",
//				activity.getString(R.string.shareTwitterNotInstall),callback);
//	}
//	
//	public static void shareLineItem(final Activity activity,final Bitmap bmp,String filterName,Object obj,final ShareCallBack callback,String shareText){
//		shareNormalItem(activity,bmp,"jp.naver.line.android","Line", shareText,
//				activity.getString(R.string.shareError),"ShareLine",activity.getString(R.string.shareFacebookNotInstall),callback);
//	}
//	
//	public static void shareInstagramItem(final Activity activity,final Bitmap bmp,String filterName,Object obj,String shareText,final ShareCallBack callback){
//		shareNormalItem(activity,bmp,"com.instagram.android","Instagram",shareText,
//				activity.getString(R.string.shareError),"ShareTwitter",activity.getString(R.string.shareInstagramNotInstall),callback);
//	}
//	
//	public static void shareTumblrItem(final Activity activity,final Bitmap bmp,String filterName,Object obj,String shareText,final ShareCallBack callback){
//		shareNormalItem(activity,bmp,"com.tumblr","Tumblr",shareText,
//				activity.getString(R.string.shareError),"ShareTumblr",activity.getString(R.string.shareTumblrNotInstall),callback);
//	}
//		
//	/*
//	 * Sina
//	 * */
//	public static void shareSinaItem(final Activity activity,final Bitmap bmp,String filterName,Object obj,String shareText,final ShareCallBack callback){
//		shareNormalItem(activity,bmp,"com.sina.weibo","Sina",shareText,
//				activity.getString(R.string.shareError),"ShareSina",activity.getString(R.string.shareSinaNotInstall),callback);
//	}
//	
//	
//	public static void shareQQZoneItem(final Activity activity,final Bitmap bmp,String filterName,Object obj,final ShareCallBack callback,String shareText){
//		shareNormalItem(activity,bmp,"com.qzone","QZone",shareText,
//				activity.getString(R.string.shareError),"ShareQzone",activity.getString(R.string.shareQzoneNotInstall),callback);
//	}
//	
//	public static void shareQQItem(final Activity activity,final Bitmap bmp,String filterName,Object obj,final ShareCallBack callback,String shareText){
//		shareNormalItem(activity,bmp,"com.tencent.mobileqq","QZone",shareText,
//				activity.getString(R.string.shareError),"ShareQQ",activity.getString(R.string.shareQQNotInstall),callback);
//	}
//	
//	public static void shareQQWeiboItem(final Activity activity,final Bitmap bmp,String filterName,Object obj,String shareText,final ShareCallBack callback){
//		shareNormalItem(activity,bmp,"com.tencent.WBlog","QQWeibo",shareText,
//				activity.getString(R.string.shareError),"ShareQQWeibo",activity.getString(R.string.shareQWeiboNotInstall),callback);
//	}
//	
	
//	public static void shareWeChatItem(final Activity activity,final String filename,String filterName,Object obj,final ShareCallBack callback,String apiid){
//		final WeChatShareAssistant wechat = new WeChatShareAssistant(activity,apiid);
//
//		boolean isInstall = wechat.isWechatInstall();
//		if(!isInstall){
////			Toast.makeText(activity, activity.getString(R.string.shareWechatNotInstall), Toast.LENGTH_LONG).show();
//			if(callback != null){
//				callback.onResult(false, false);
//			}
//			return;
//		}
//		
//		boolean result = wechat.sendWeChat(filename, false);
////		FlurryAgent.logEvent("ShareWechat");
//		if(callback != null){
//			callback.onResult(true, result);
//		}
//	}
//	
//	
//	public static void shareMomentItem(final Activity activity,final String  filename,String filterName,Object obj,final ShareCallBack callback,String apiid){
//		final WeChatShareAssistant wechat = new WeChatShareAssistant(activity,apiid);
//
//		boolean isInstall = wechat.isWechatInstall();
//		if(!isInstall){
////			Toast.makeText(activity, activity.getString(R.string.shareMomentNotInstall), Toast.LENGTH_LONG).show();
//			if(callback != null){
//				callback.onResult(false, false);
//			}
//			return;
//		}
//		
//		boolean result = wechat.sendWeChat(filename, true);
////		FlurryAgent.logEvent("ShareWechatTimeLine");
//		if(callback != null){
//			callback.onResult(true, result);
//		}
//	}
	

//	public static void shareWeChat(final Activity activity,final Bitmap bmp, int index,String apiid){
//
//		final WeChatShare wechat = new WeChatShare(activity,apiid);
//		boolean isInstall = wechat.isWechatInstall();
//		if(!isInstall){
////			Toast.makeText(activity, activity.getString(R.string.shareWechatNotInstall), Toast.LENGTH_LONG).show();
//			return;
//		}
//		wechat.sendWeChatSession(bmp);
//	}
//	
//
//	public static boolean isWechatInstall(final Activity activity,String apiid){
//		final WeChatShare wechat = new WeChatShare(activity,apiid);
//		return wechat.isWechatInstall();
//	}
//
//	public static void shareWeMoment(final Activity activity,final Bitmap bmp, int index,String apiid){
//
//		final WeChatShare wechat = new WeChatShare(activity,apiid);
//		boolean isInstall = wechat.isWechatInstall();
//		if(!isInstall){
////			Toast.makeText(activity, activity.getString(R.string.shareWechatNotInstall), Toast.LENGTH_LONG).show();
//			return;
//		}
//		wechat.sendWeChatTimeLine(bmp);
//	}

//	public static void shareWeChatItem(final Activity activity,final Bitmap bmp,String filterName,Object obj,final ShareCallBack callback,String apiid){
//		final WeChatShareAssistant wechat = new WeChatShareAssistant(activity,apiid);
//
//		boolean isInstall = wechat.isWechatInstall();
//		if(!isInstall){
////			Toast.makeText(activity, activity.getString(R.string.shareWechatNotInstall), Toast.LENGTH_LONG).show();
//			if(callback != null){
//				callback.onResult(false, false);
//			}
//			return;
//		}
//		
//		wechat.sendWeChatSession(bmp);
////		FlurryAgent.logEvent("ShareWechat");
//		if(callback != null){
//			callback.onResult(true, true);
//		}
//		
////		new Handler().postDelayed(new Runnable(){
////			@Override
////			public void run() {		
////				wechat.sendWeChatSession(bmp);
////				FlurryAgent.logEvent("ShareWechat");
////				if(callback != null){
////					callback.onResult(true, true);
////				}
////			}
////		},200);
//	}
//	
//	public static void shareMomentItem(final Activity activity,final Bitmap bmp,String filterName,Object obj,final ShareCallBack callback,String apiid){
//		final WeChatShareAssistant wechat = new WeChatShareAssistant(activity,apiid);
//
//		boolean isInstall = wechat.isWechatInstall();
//		if(!isInstall){
////			Toast.makeText(activity, activity.getString(R.string.shareMomentNotInstall), Toast.LENGTH_LONG).show();
//			if(callback != null){
//				callback.onResult(false, false);
//			}
//			return;
//		}
//		
//		wechat.sendWeChatTimeLine(bmp);
//		if(callback != null){
//			callback.onResult(true, true);
//		}
//	}
	
	
	protected static void shareNormalItem(final Activity activity,final Bitmap  bmp,final String thirdPackage,final String title,
			final String text,final String errorText, final String logEvent,final String notInstallText,final ShareCallBack callback){
		boolean isAppInstall = isAppInstall(activity, thirdPackage);
		if(!isAppInstall){
			Toast.makeText(activity, notInstallText, Toast.LENGTH_LONG).show();
			if(callback != null){
				callback.onResult(false, false);
			}
			return;
		}

		new Handler().postDelayed(new Runnable(){
			@Override
			public void run() {
				
				boolean isSucc = toOtherApp(activity, thirdPackage, title, text, bmp);
				if(!isSucc){
					Toast.makeText(activity, text, Toast.LENGTH_LONG).show();
					
					//activity.dismissProcessDialog();
					if(callback != null){
						callback.onResult(true, false);
					}
					return;
				}
//				FlurryAgent.logEvent(logEvent);
				if(callback != null){
					callback.onResult(true, true);
				}
			}					
		},200);
	}	

	public static String getSaveFolder(String folder)
	{
		boolean bHaveSdcard = false;
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			bHaveSdcard = true;
		}
		
		String path = "";
		if (bHaveSdcard) {
			String extStorageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
			path = extStorageDirectory;
			if(folder != null){
				path += "/" + folder;
			}
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
		}
		return path;
	}
}
