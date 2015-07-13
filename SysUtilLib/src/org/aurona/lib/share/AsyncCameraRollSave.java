package org.aurona.lib.share;

import org.aurona.lib.share.ShareOtherApp;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

public class AsyncCameraRollSave extends AsyncTask<String, Void, Boolean>{


	
	private Context context;
	private String folder;
	private Bitmap bmp;
	private OnCameraRollSaveEventListener listener;
	
	

	
	public AsyncCameraRollSave(Context context,String folder,Bitmap bmp){
		this.context = context;
		this.folder  = folder;
		this.bmp     = bmp;
	}
	
	
	public void setOnCameraRollSaveEventListener(OnCameraRollSaveEventListener listener){
		this.listener = listener;
	}
	
	
	@Override
	protected Boolean doInBackground(String... params) {
		if(bmp == null || bmp.isRecycled()) return false;
		synchronized (bmp) {
			boolean isSaved = ShareOtherApp.saveToCameraRoll(context,folder,bmp);
			return isSaved;
		}
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		if(listener != null){
			if(result){
				listener.onSaveFinish();
			}else{
				listener.onSaveFail();
			}
		}
	}

}
