package org.aurona.lib.otherapp;

import org.aurona.lib.packages.OtherAppPackages;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

public class OtherApp {

	public static Intent playStoreIntent(String packageName){
		Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("market://details?id="+ packageName));
		intent.setPackage(OtherAppPackages.googleplayPackage);
		return intent;
	}
	
	public static Intent marketIntent(String packageName){
		Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("market://details?id="+ packageName));
		return intent;
	}
	
	public static Intent browserIntent(String packageName){
		Uri uri = Uri.parse("http://play.google.com/store/apps/details?id=" + packageName); //����� 
		Intent intent=new Intent(Intent.ACTION_VIEW,uri);
		return intent;
	}
	
	public static Intent appIntent(String packageName, String startActivityName){
		 Intent intent = new Intent(Intent.ACTION_VIEW);   
		 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
		 ComponentName comp = new ComponentName(packageName, startActivityName);    
		 intent.setComponent(comp);     
		 return intent;   
	}
	
	public static Boolean isInstalled(Context context, String packageName)
	{
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


	public static void openIntentOrInMarket(Context context, String packageName, String startActivityName){
		boolean isInstall = isInstalled(context, packageName);

		if (isInstall) {
			context.startActivity(appIntent(packageName, startActivityName));
		} else {
			try {
				context.startActivity(marketIntent(packageName));
			} catch (Exception e) {
				try {
					context.startActivity(browserIntent(packageName));
				} catch (Exception e2) {
				}
			}
		}
	}
	
	public static void openInMarket(Context context, String packageName){
		try {
			if(OtherApp.isInstalled(context, OtherAppPackages.googleplayPackage)){
				context.startActivity(playStoreIntent(packageName));
			}else{
				context.startActivity(marketIntent(packageName));
			}
		} catch (Exception e) {
			try {
				context.startActivity(browserIntent(packageName));
			} catch (Exception e2) {
			}catch (Throwable e2) {
			}
		}catch (Throwable e) {
			try {
				context.startActivity(browserIntent(packageName));
			} catch (Exception e2) {
			}catch (Throwable e2) {
			}
		}
	}
	
}
