package org.aurona.lib.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Environment;

public class BitmapIoCache {
	
	public static String cachDir(){
		File  dir = new File(Environment.getExternalStorageDirectory().getPath()+ "/.tmpb/");
		if (!dir.exists()) {
			dir.mkdir();
		}
	
		return dir.getAbsolutePath();	
	}

	public static String putJPG(String key, Bitmap bmp) {
		try{
			synchronized(bmp){
			String filename = cachDir() + "/" + key;
			FileOutputStream out = new FileOutputStream(filename);
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
			return filename;
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return null;		
	}
	
	
	public static String putPNG(String key, Bitmap bmp) {
		try{
			synchronized(bmp){
			String filename = cachDir() + "/" + key;
			FileOutputStream out = new FileOutputStream(filename);
			bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.flush();
			out.close();
			return filename;
			}
		}catch(Exception ex){
			
		}
		return null;
	}

	public static Bitmap getBitmap(String key) {
		String filename = cachDir() + "/" + key;
		Options options = new BitmapFactory.Options();
		options.inScaled = false;
		Bitmap bm = BitmapFactory.decodeFile(filename, options);
		return bm;
	}

	public static Bitmap getBitmap(String key, BitmapFactory.Options op) {
		String filename = cachDir() + "/" + key;
		Bitmap bm = BitmapFactory.decodeFile(filename, op);
		return bm;
	}
	public static boolean hasBitmapExists(String key)
	{
		String filename = cachDir() + "/" + key;
		File f = new File(filename);
		return f.exists();
	}
	/*Key  Remove*/
	public static void remove(String key) {
		String filename = cachDir() + "/" + key;
		File f = new File(filename);
		if (f.exists()) {
			f.delete();
		}
	}


	

}
