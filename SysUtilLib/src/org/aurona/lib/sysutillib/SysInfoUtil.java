package org.aurona.lib.sysutillib;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class SysInfoUtil {
	public static String getVersion(Context context){
		//取得Version信息
		PackageInfo pInfo = null;
		try {
			pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		String version = null;
		if (pInfo != null) {
			version = pInfo.versionName;
		}
		return version;
	}
	
	public static  boolean isAppInstalled(Context context, String packageName){
    	boolean bInstalled = false;
    	if(packageName == null) return false;
		PackageInfo packageInfo = null;
		
    	try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if(packageInfo ==null){
        	bInstalled = false;
        }else{
        	bInstalled = true;
        }
    	return bInstalled;
	}
	
	//检测Web是否有可能不能打开
	public static final boolean isWebViewMayNotOpen(Context context) {
	    try {
	        SQLiteDatabase cacheDb = context.openOrCreateDatabase("webviewCache.db", 0, null);

	        if (cacheDb != null) {
	            cacheDb.close();
	            return false;
	        }
	    } catch (Exception t) {
	        //Log.w(TAG, t);
	    }
	    
	    return true;
	}
	
	public static boolean isConnectingToInternet(Context context) {
		
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return (info != null) && info.isAvailable();

    }

}
