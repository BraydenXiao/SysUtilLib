package org.aurona.lib.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;

public class AsyncRollSaveTempFileExecute {
	
	public static void executeAsyncTask(Context context,String folder,Bitmap bmp,String filename,OnCameraRollSaveEventListener listener,Bitmap.CompressFormat format){
		
//		if (Build.VERSION.SDK_INT  <= 10){
			AsyncRollSaveTempFile23 task = new AsyncRollSaveTempFile23(context,folder,filename,bmp,format);
			task.setOnRollSaveTempFileEventListener(listener);
			task.execute();
//		}else{
//			AsyncRollSaveTempFile task = new AsyncRollSaveTempFile(context,folder,filename,bmp,format);
//			task.setOnRollSaveTempFileEventListener(listener);
//			task.execute();
//		}
	}
}
