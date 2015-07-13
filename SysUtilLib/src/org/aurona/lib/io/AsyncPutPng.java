package org.aurona.lib.io;

import android.graphics.Bitmap;
import android.os.Handler;


public class AsyncPutPng{

	
	public interface OnPutPngListener{
		public void onPutPngProcessFinish(Boolean result);
	}
	
	private String key;
	private Bitmap bmp;
	private boolean isRecycle;
	private OnPutPngListener listener;
	
	public static void asyncPutPng(String key, Bitmap bmp,boolean isRecycle,OnPutPngListener listener){
		AsyncPutPng loader = new AsyncPutPng();
		loader.setData(key, bmp, isRecycle);
		loader.setOnPutPngListener(listener);
		loader.execute();
	}
	
	public void setData(String key, Bitmap bmp,boolean isRecycle){
		this.key = key;
		this.bmp = bmp;
		this.isRecycle = isRecycle;
	}
	
	public void setOnPutPngListener(OnPutPngListener listener){
		this.listener = listener;
	}
	
//	
//	@Override
//	protected Boolean doInBackground(String... params) {
//		String filename = BitmapIoCache.putPNG(key, bmp);
//		if(filename != null) return true;
//		return false;
//	}
//
//	
//	@Override
//	protected void onPostExecute(Boolean result) {
//		if(listener != null){
//			listener.onPutPngProcessFinish(result);
//		}
//	}
	

	private final Handler handler = new Handler();
	boolean isPut = false;
	public void execute(){
		new Thread(new Runnable() {  
            @Override  
            public void run() { 
            	isPut = false;
            	String filename = BitmapIoCache.putPNG(key, bmp);
        		if(filename != null)
        		{
        			isPut = true;
        		}
                handler.post(new Runnable() {  
                	@Override  
                    public void run() {  
                		if(listener != null){
                			listener.onPutPngProcessFinish(isPut);
                		}
                    }  
                });  
            }  
        }).start();  
	}
}
