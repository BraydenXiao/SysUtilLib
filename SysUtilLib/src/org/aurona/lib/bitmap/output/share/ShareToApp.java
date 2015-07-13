package org.aurona.lib.bitmap.output.share;

import java.io.File;

import org.aurona.lib.io.BitmapIoCache;
import org.aurona.lib.otherapp.OtherApp;
import org.aurona.lib.packages.AppPackages;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;

import org.aurona.lib.sysutillib.R;

public class ShareToApp {

	public static boolean shareImage(Activity activity,String packageName,String title,String text,Bitmap bmp){
		try{
			if(bmp == null || bmp.isRecycled()){
				Toast.makeText(activity, activity.getResources().getString(R.string.warning_no_image), Toast.LENGTH_LONG).show();
				return false;
			}
			if(packageName != null){
				if(!OtherApp.isInstalled(activity, packageName)){
					Toast.makeText(activity, activity.getResources().getString(R.string.warning_no_installed), Toast.LENGTH_LONG).show();
					return false;
				}
			}
			
			String path = BitmapIoCache.putJPG(AppPackages.getAppName(activity.getPackageName())+".jpg", bmp);
			if(path == null) return false;
			
			File file = new File(path);
			Uri uri = Uri.fromFile(file);
			if(uri == null) return false;
			
			return shareImageFromUri(activity, packageName, title, text, uri);
		}catch(Exception ex){
			return false;
		}
	}
	
	public static boolean shareImageFromUri(Activity activity,String packageName,String title,String text,Uri bitmapUri){
		try{
			if(bitmapUri == null){
				Toast.makeText(activity, activity.getResources().getString(R.string.warning_no_image), Toast.LENGTH_LONG).show();
				return false;
			}
			if(packageName != null){
				if(!OtherApp.isInstalled(activity, packageName)){
					Toast.makeText(activity, activity.getResources().getString(R.string.warning_no_installed), Toast.LENGTH_LONG).show();
					return false;
				}
			}
			
			Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
		    shareIntent.setType("image/*");
		    shareIntent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
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
	
	public static boolean shareText(Activity activity,String packageName,String title,String text){
		try{
			if(!OtherApp.isInstalled(activity, packageName)){
				return false;
			}
			
			Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
		    shareIntent.setType("text/plain");
		    shareIntent.putExtra(Intent.EXTRA_SUBJECT,title);
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

}
