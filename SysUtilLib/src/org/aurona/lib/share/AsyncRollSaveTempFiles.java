package org.aurona.lib.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

public class AsyncRollSaveTempFiles  extends AsyncTask<String, Void, Boolean>{

	
	private Context context;
	private String folder;
	private String[] filename;
	private Bitmap[] bmp;
	private Bitmap.CompressFormat format;
	
	private OnCameraRollSaveEventListener listener;
	
	
	public static void executeAsyncTask(Context context,String folder,Bitmap[] bmp,String[] filename,OnCameraRollSaveEventListener listener,Bitmap.CompressFormat format){
		AsyncRollSaveTempFiles task = new AsyncRollSaveTempFiles(context,folder,filename,bmp,format);
		task.setOnRollSaveTempFileEventListener(listener);
		task.execute();
	}
	
	public AsyncRollSaveTempFiles(Context context,String folder,String[] filename,Bitmap[] bmp,Bitmap.CompressFormat format){
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
		boolean isSaved = true;
		boolean blntmp = false;
		for(int i = 0;i<filename.length;i++)
		{
			blntmp = ShareOtherApp.saveToCameraRoll(context,folder,filename[i],bmp[i],format);
			if(!blntmp)
				isSaved = false;	
		}
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
