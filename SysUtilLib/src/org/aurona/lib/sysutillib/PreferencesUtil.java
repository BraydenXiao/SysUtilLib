package org.aurona.lib.sysutillib;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;


public class PreferencesUtil {
	
	public static void save(Context context,String PrefsName,String key,String val){
		SharedPreferences settings = context.getSharedPreferences(
				PrefsName, 0);
		
		SharedPreferences.Editor editor = settings.edit();	
		editor.putString(key, val);
		editor.commit();
	}

	public static Map<String,?> getAll(Context context,String PrefsName){
		SharedPreferences settings = context.getSharedPreferences(
				PrefsName, 0);
		return settings.getAll();
	}
	
	public static void remove(Context context,String PrefsName,String key){
		SharedPreferences settings = context.getSharedPreferences(
				PrefsName, 0);
		
		SharedPreferences.Editor editor = settings.edit();
		editor.remove(key);
		editor.commit();
	}

	public static void save(Context context,String PrefsName,HashMap<String,String> keyValues){
		SharedPreferences settings = context.getSharedPreferences(
				PrefsName, 0);
		
		SharedPreferences.Editor editor = settings.edit();

		Iterator iter = keyValues.entrySet().iterator(); 
		while (iter.hasNext()) { 
		    Map.Entry<String,String> entry = (Map.Entry<String,String>) iter.next(); 
		    String key = entry.getKey(); 
		    String val = entry.getValue(); 
		    editor.putString(key, val);
		} 
		editor.commit();
	}
	
	public static  String get(Context context,String PrefsName,String key){
		SharedPreferences sharedata = context.getSharedPreferences(
				PrefsName, 0);
		String val = sharedata.getString(key, null);
		return val;
	}

}
