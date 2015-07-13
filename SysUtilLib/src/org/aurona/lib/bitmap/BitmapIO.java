package org.aurona.lib.bitmap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Environment;

public class BitmapIO {

    public static InputStream getSDFileInputStream(Context context,String fileName){
	    boolean hasSD = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); 
		if(!hasSD) return null;
		
		String SDPATH = Environment.getExternalStorageDirectory().getPath(); 
		File file = new File(SDPATH + "//" + fileName); 
        try { 
            FileInputStream fis = new FileInputStream(file); 
            return fis;
        } catch (Exception e) { 

        } catch (Throwable e) { 

        } 
        return null;
    }
    
    public static Bitmap getImageFromSDFile(Context context,String fileName){
        Bitmap image = null;
        try
        {
            InputStream is = getSDFileInputStream(context,fileName);
            if(is != null){
               image = BitmapFactory.decodeStream(is);
               is.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return image; 
    }
    
    public static Bitmap getImageFromSDFile(Context context,String fileName, Options options){
        Bitmap image = null;
        try
        {
            InputStream is = getSDFileInputStream(context,fileName);
            if(is != null){
               image = BitmapFactory.decodeStream(is);
               is.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return image; 
    }
}
