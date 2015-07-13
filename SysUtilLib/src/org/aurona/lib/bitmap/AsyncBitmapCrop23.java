package org.aurona.lib.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;

public class AsyncBitmapCrop23 {
	private static int FromURI      = 0x01;
	private static int FfromDrawbale = 0x02;
	private Context context;
	private Uri itemUri;
	private int maxSize;
	private int type = FromURI;
	
	private int resId;
	OnBitmapCropListener listener;
	private final Handler handler = new Handler();

	//MyRunnable m_runnable;  
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
	
	
	
	Bitmap cropBitmap = null;
	
	//myThread t;
	
	public void execute(){
		new myThread(this).start();
//		if(type == FromURI){
//			new myThread().setData(this.context, this.itemUri, this.maxSize).setOnBitmapCropListener(this.listener).start();
//		}else if(type == FfromDrawbale){
//			new myThread().setData(this.context, this.resId, this.maxSize).setOnBitmapCropListener(this.listener).start();
//		}
		
//		if(this.m_runnable == null){
//			this.m_runnable = new MyRunnable();
//		}
//		new Thread(){
//          @Override  
//          public void run() { 
////        	  AsyncBitmapCrop23.this.m_runnable.run();
////        	  handler.post(new Runnable() {  
////        		  @Override  
////        		  public void run() {  
////        			  if(listener != null){
////        				  listener.onBitmapCropFinish(cropBitmap);
////        			  }
////        		  }  
////        	  });  
//          }
//		}.start();
		
		
//		new Thread(new Runnable() {  
//            @Override  
//            public void run() { 
//
//            	
//                handler.post(new Runnable() {  
//                	@Override  
//                    public void run() {  
//               		if(listener != null){
//                			listener.onBitmapCropFinish(cropBitmap);
//                		}
//                    }  
//                });  
//    	
////            	cropBitmap = null;
////        		if(type == FfromDrawbale){
////        			cropBitmap = BitmapCrop.cropDrawableImage(context, resId, maxSize);
////        		}else{
////        			cropBitmap = BitmapCrop.CropItemImage(context, itemUri, maxSize);
////        		}
////
////                handler.post(new Runnable() {  
////                	@Override  
////                    public void run() {  
////                		if(listener != null){
////                			listener.onBitmapCropFinish(cropBitmap);
////                		}
////                    }  
////                });  
//            }  
//        }).start();  
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	

		
		
	public void run(){
		cropBitmap = null;
        if(type == FfromDrawbale){
        	cropBitmap = BitmapCrop.cropDrawableImage(context, resId, maxSize);
        }else{
        	cropBitmap = BitmapCrop.CropItemImage(context, itemUri, maxSize);
        }


	
        handler.post(new Runnable() {  
        		@Override  
        		public void run() {  
        			if(listener != null){
        				listener.onBitmapCropFinish(cropBitmap);
        				cropBitmap = null;
        			}
        			listener = null;
        		}  
        	});  
		}

	
    static class myThread extends Thread{       

    	AsyncBitmapCrop23 mAsy23;
    	public myThread(AsyncBitmapCrop23 asy23){
    		mAsy23 = asy23;
    	}

        public void run(){   
        	mAsy23.run();
        	//new AsyncBitmapCrop23().run();
//        	cropBitmap = null;
//        	if(type == FfromDrawbale){
//        		cropBitmap = BitmapCrop.cropDrawableImage(context, resId, maxSize);
//        	}else{
//        		cropBitmap = BitmapCrop.CropItemImage(context, itemUri, maxSize);
//        	}
        	
//        	handler.post(new Runnable() {  
//        		@Override  
//        		public void run() {  
//        			if(listener != null){
//        				listener.onBitmapCropFinish(cropBitmap);
//        				cropBitmap = null;
//        			}
//        		}  
//        	});  
        }   
    }  
    
	static class MyRunnable implements Runnable{   
           
        public void run(){   
//        	cropBitmap = null;
//    		if(type == FfromDrawbale){
//   			cropBitmap = BitmapCrop.cropDrawableImage(context, resId, maxSize);
//    		}else{
//    			cropBitmap = BitmapCrop.CropItemImage(context, itemUri, maxSize);
//    		}
        }   
    }   
	
}
