package org.aurona.lib.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;


public class AsyncCameraRollSaveExecute {
	public static void executeAsyncTask(Context context,String folder,Bitmap bmp,OnCameraRollSaveEventListener listener){
		
		AsyncCameraRollSave23 task = new AsyncCameraRollSave23(context,folder,bmp);
		task.setOnCameraRollSaveEventListener(listener);
		task.execute();
		
//		if (Build.VERSION.SDK_INT  <= 10){
//			AsyncCameraRollSave23 task = new AsyncCameraRollSave23(context,folder,bmp);
//			task.setOnCameraRollSaveEventListener(listener);
//			task.execute();
//		}else{
//			AsyncCameraRollSave task = new AsyncCameraRollSave(context,folder,bmp);
//			task.setOnCameraRollSaveEventListener(listener);
//			task.execute();
//		}

	}
}
