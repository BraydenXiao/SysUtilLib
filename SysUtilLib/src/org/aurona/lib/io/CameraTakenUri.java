package org.aurona.lib.io;

import java.io.File;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

public class CameraTakenUri {
	
	public  static Uri uriFromCamera(Intent data){
		Uri resultUri = null;
		Bundle bundle = data.getExtras();
		Bitmap bitmaptmp = (Bitmap)bundle.get("data");
		String filename = BitmapIoCache.putJPG("ResultPic", bitmaptmp);
		if(filename == null || filename == "") return null;
		File file = new File(filename);
		resultUri = Uri.fromFile(file);

		if(resultUri != null && bitmaptmp != null && !bitmaptmp.isRecycled())
		{
			bitmaptmp.recycle();
		}
		bitmaptmp = null;
		return resultUri;
	}
	
	public  static Bitmap bitmapFromCamera(Intent data){
		Bundle bundle = data.getExtras();
		Bitmap result = (Bitmap)bundle.get("data");
		return result;
	}
}
