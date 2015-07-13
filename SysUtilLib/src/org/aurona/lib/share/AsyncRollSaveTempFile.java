package org.aurona.lib.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

public class AsyncRollSaveTempFile extends AsyncTask<String, Void, Boolean>{

	private Context context;
	private String folder;
	private String filename;
	private Bitmap bmp;
	private Bitmap.CompressFormat format;
	
	private OnCameraRollSaveEventListener listener;
	
	
	
	public AsyncRollSaveTempFile(Context context,String folder,String filename,Bitmap bmp,Bitmap.CompressFormat format){
		this.context = context;
		this.folder  = folder;
		this.bmp     = bmp;
		this.filename = filename;
		this.format = format;
	}
	
	
	public void setOnRollSaveTempFileEventListener(OnCameraRollSaveEventListener listener){
		this.listener = listener;
	}
	
	
	@Override
	protected Boolean doInBackground(String... params) {
		
		boolean isSaved = ShareOtherApp.saveToCameraRoll(context,folder,filename,bmp,format);
		return isSaved;
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
