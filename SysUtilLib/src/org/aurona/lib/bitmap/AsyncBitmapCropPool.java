package org.aurona.lib.bitmap;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;

public class AsyncBitmapCropPool {

	private static int FromURI      = 0x01;
	private static int FfromDrawbale = 0x02;
	private Context context;
	private Uri itemUri;
	private int maxSize;
	private int type = FromURI;
	
	private int resId;
	OnBitmapCropListener listener;
	private ExecutorService executorService; // = Executors.newFixedThreadPool(4);

	private static AsyncBitmapCropPool loader;// = new AsyncImageLoader();
	private final Handler handler = new Handler();

	public static AsyncBitmapCropPool getInstance(){
		return loader;
	}
	
	
	public static void initCropLoader(Context context){
		if(loader == null){
			loader = new AsyncBitmapCropPool();
		}
		loader.initExecutor();
	}
	
	public static void shutdownCropLoder(){
		if(loader != null){
			loader.shutDownExecutor();
		}
		loader = null;
	}
	
	
	public void initExecutor(){
		if(executorService != null){
			shutDownExecutor();
		}
		
		executorService =  Executors.newFixedThreadPool(1);
	}
	
	public void shutDownExecutor(){
		if(executorService != null){
			executorService.shutdown();
		}
		context = null;
	}
	
	/**/
	public void setData(Context context,Uri itemUri,int maxSize){
		this.context = context;
		this.itemUri = itemUri;
		this.maxSize = maxSize;
		type = FromURI;
	}
	
	
	public void setData(Context context,int resId,int maxSize){
		this.context = context;
		this.resId = resId;
		this.maxSize = maxSize;
		type = FfromDrawbale;
	}
	
	
	public void setOnBitmapCropListener(OnBitmapCropListener listener){
		this.listener = listener;
	}
	
	public void execute(){
		executorService.submit(new Runnable() {
			public void run() {
				Bitmap bmp = null;
				if(type == FfromDrawbale){
					 bmp = BitmapCrop.cropDrawableImage(context, resId, maxSize);
				}else{
					bmp = BitmapCrop.CropItemImage(context, itemUri, maxSize);
				}
				final Bitmap result = bmp;
			    handler.post(new Runnable() {  
			        	@Override  
			            public void run() {  
			        		if(listener != null){
			        			listener.onBitmapCropFinish(result);
			        		}
			            }  
			   });  
			}
		});
	}
	
	
//	@Override
//	protected Bitmap doInBackground(String... params) {
//		Bitmap bmp = null;
//		if(type == FfromDrawbale){
//			 bmp = BitmapCrop.cropDrawableImage(context, resId, maxSize);
//		}else{
//			bmp = BitmapCrop.CropItemImage(this.context, itemUri, maxSize);
//		}
//		return bmp;
//	}
//	
//	@Override
//	protected void onPostExecute(Bitmap result) {
//		if(listener != null){
//			listener.onBitmapCropFinish(result);
//		}
//	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
