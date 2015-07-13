package org.aurona.lib.share;

import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;

public class AsyncCameraRollSave23{


	
	private Context context;
	private String folder;
	private Bitmap bmp;
	private OnCameraRollSaveEventListener listener;
	
	
	private final Handler handler = new Handler();
	

	
	public AsyncCameraRollSave23(Context context,String folder,Bitmap bmp){
		this.context = context;
		this.folder  = folder;
		this.bmp     = bmp;
	}
	
	
	public void setOnCameraRollSaveEventListener(OnCameraRollSaveEventListener listener){
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
            			Date dt = new Date();
            			long ldt = dt.getTime();
            			String filename = "img" + String.valueOf(ldt) + ".png";
            			isSaved = ShareOtherApp.saveToCameraRoll(context,folder,filename,bmp,Bitmap.CompressFormat.PNG);
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
