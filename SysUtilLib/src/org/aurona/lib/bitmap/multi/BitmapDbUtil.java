package org.aurona.lib.bitmap.multi;

import java.io.IOException;
import java.io.InputStream;


import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

public class BitmapDbUtil {
	public  static int  imgageOrientation(Context context, Uri photoUri) {
		Cursor cursor = context.getContentResolver().query(photoUri,
				new String[] { MediaStore.Images.ImageColumns.ORIENTATION },
				null, null, null);

		if (cursor == null) {
			return -1;
		}

		try {
			if (cursor.moveToFirst()) {
				return cursor.getInt(0);
			} else {
				return -1;
			}
		} finally {
			cursor.close();
		}
	}
	
	public static String imagelPathFromURI(Context context, Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(contentUri,
        		proj,
				null, null, null);
        
        String path = "";
        if(cursor != null && cursor.moveToFirst()){
        	int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        	path = cursor.getString(column_index);
        }
        
        if(cursor != null) cursor.close(); 
        cursor = null;
        
        return path; 
    }
	
	 
    public static Bitmap getImageFromAssetsFile(Context context,String fileName)
    {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try
        {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return image;
    }
    

	public static Bitmap getImageFromAssetsFile(Resources res,String fileName)
    {
        Bitmap image = null;
        try
        {
            InputStream is = res.getAssets().open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return image;
    }
	 
    public static Bitmap getImageFromResourceFile(Resources res, int fileID){
    	Bitmap image = null;
        try
        {
            InputStream is = res.openRawResource(fileID);
            image = BitmapFactory.decodeStream(is);
            is.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return image;
    }

    public static Bitmap getImageFromResourceFile(Context context,int fileID){
    	Bitmap image = null;
        try
        {
        	Resources res = context.getResources();
            InputStream is = res.openRawResource(fileID);
            image = BitmapFactory.decodeStream(is);
            is.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return image;
    } 
}
