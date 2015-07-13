package org.aurona.lib.bitmap.multi;

import java.util.ArrayList;
import java.util.List;

import org.aurona.lib.bitmap.BitmapCrop;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;


public class AsyncMultiBitmapsCrop{
	
	public interface OnMultiBitmapCropListener{
		public void onMultiBitmapCropStart();
		public void onMultiBitmapCropSuccess(List<Bitmap> bmps);
		public void onMultiBitmapCriopFaile();
		
	}
	
	public static void AsyncMutiBitmapCropExecute(Context context,List<Uri> uris,int maxSize,OnMultiBitmapCropListener listener){
		AsyncMultiBitmapsCrop cropTask = new AsyncMultiBitmapsCrop(context,uris,maxSize);
		cropTask.setOnMultiBitmapCropListener(listener);
		cropTask.execute();
		
	}
	
	private Context context;
	private List<Uri> uris;
	private OnMultiBitmapCropListener listener;
	private int maxSize;
	
	public AsyncMultiBitmapsCrop( Context context,List<Uri> uris,int maxSize){
		this.context = context;
		this.uris = uris;
		this.maxSize = maxSize;
	}
	
	public void setOnMultiBitmapCropListener(OnMultiBitmapCropListener listener){
		this.listener = listener;
	}
	
	private final Handler handler = new Handler();
	public void execute(){
		new Thread(new Runnable() {  
            @Override  
            public void run() { 
            	try {
            		if(listener != null){
            			listener.onMultiBitmapCropStart();
            		}
            		final List<Bitmap> result = new ArrayList<Bitmap>();
            		for(Uri uri : uris){
            			String s_uri = uri.toString();
            			String fileName = "";
            			if(s_uri.startsWith("file://")){
            				fileName = uri.getPath();
            			}else{
            				fileName = BitmapDbUtil.imagelPathFromURI(context, uri);
            			}
            			
//            			Bitmap bitmap = BitmapUtil.sampeZoomFromFile(fileName, maxSize);
            			Bitmap bitmap = BitmapCrop.CropItemImage(context, uri, maxSize);
            			result.add(bitmap);
            		}
			
	                handler.post(new Runnable() {  
	                	@Override  
	                    public void run() {  
	                		if(listener != null){
	                			if(result != null)
	                			{
	                				listener.onMultiBitmapCropSuccess(result);
	                			}
	                		}
	                    }  
	                });  
            	} catch (Exception e) {
            		if(listener != null){
            			listener.onMultiBitmapCriopFaile();
            		}
				}
            }  
        }).start();  
	}
//	@Override
//	protected void onPreExecute() {
//		super.onPreExecute();
//		if(this.listener != null){
//			listener.onBitmapCropStart();
//		}
//	}
//
//
//
//	@Override
//	protected List<Bitmap> doInBackground(String... params) {
//		List<Bitmap> result = new ArrayList<Bitmap>();
//		for(Uri uri : uris){
//			String s_uri = uri.toString();
//			String fileName = "";
//			if(s_uri.startsWith("file://")){
//				fileName = uri.getPath();
//			}else{
//				fileName = BitmapDbUtil.imagelPathFromURI(context, uri);
//			}
//			
//			Bitmap bitmap = BitmapUtil.sampeZoomFromFile(fileName, maxSize);
//			result.add(bitmap);
//		}
//
//		return result;
//	}
//
//	
//	
//	@Override
//	protected void onPostExecute(List<Bitmap> result) {
//		if(listener != null){
//			if(result != null){
//				listener.onBitmapCropSuccess(result);
//			}else{
//				listener.onBitmapCriopFaile();
//			}
//		}
//	}
}
