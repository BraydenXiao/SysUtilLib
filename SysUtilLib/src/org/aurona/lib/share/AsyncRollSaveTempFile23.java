package org.aurona.lib.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;

public class AsyncRollSaveTempFile23 {
	private Context context;
	private String folder;
	private String filename;
	private Bitmap bmp;
	private Bitmap.CompressFormat format;
	
	private OnCameraRollSaveEventListener listener;
	
	private final Handler handler = new Handler();
	
	public AsyncRollSaveTempFile23(Context context,String folder,String filename,Bitmap bmp,Bitmap.CompressFormat format){
		this.context = context;
		this.folder  = folder;
		this.bmp     = bmp;
		this.filename = filename;
		this.format = format;
	}
	
	
	public void setOnRollSaveTempFileEventListener(OnCameraRollSaveEventListener listener){
		this.listener = listener;
	}
	
	public void execute(){
		new Thread(new Runnable() {  
            @Override  
            public void run() { 
            	boolean isSaved = false;
            	if(bmp == null || bmp.isRecycled()){
            		
            	}else{
            		synchronized (bmp) {
            			isSaved = ShareOtherApp.saveToCameraRoll(context,folder,filename,bmp,format);
            		}
            	}

        		final boolean result = isSaved;
                handler.post(new Runnable() {  
                	@Override  
                    public void run() {  
                		if(listener != null){
                			if(result){
                				listener.onSaveFinish();
                			}else{
                				listener.onSaveFail();
                			}
                		}
                    }  
                });  
            }  
        }).start();  
	}

	
}
