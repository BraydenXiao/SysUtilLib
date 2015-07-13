package org.aurona.lib.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;

public class AsyncBitmapCropExecute {
	
	public static void asyncBitmapCrop(Context context,Uri itemUri,int maxSize,final OnBitmapCropListener listener){
		AsyncBitmapCropPool.initCropLoader(context);
		AsyncBitmapCropPool loader = AsyncBitmapCropPool.getInstance();
		loader.setData(context, itemUri, maxSize);
		loader.setOnBitmapCropListener(new OnBitmapCropListener(){
			@Override
			public void onBitmapCropFinish(Bitmap bmp) {
				AsyncBitmapCropPool.shutdownCropLoder();
				listener.onBitmapCropFinish(bmp);
			}
		});
		loader.execute();
		
//		AsyncBitmapCrop23 loader = new AsyncBitmapCrop23();
//		loader.setData(context, itemUri, maxSize);
//		loader.setOnBitmapCropListener(listener);
//		loader.execute();
		
//		AsyncMediaDbScanPool loader = new AsyncMediaDbScanPool(context);
//		AsyncBitmapCrop23 loader = new AsyncBitmapCrop23();
//		loader.setData(context, itemUri, maxSize);
//		loader.setOnBitmapCropListener(listener);
//		loader.execute();
		
//		if (Build.VERSION.SDK_INT  <= 10){
//			AsyncBitmapCrop23 loader = new AsyncBitmapCrop23();
//			loader.setData(context, itemUri, maxSize);
//			loader.setOnBitmapCropListener(listener);
//			loader.execute();
//		}
//		else {
//			AsyncBitmapCrop loader = new AsyncBitmapCrop();
//			loader.setData(context, itemUri, maxSize);
//			loader.setOnBitmapCropListener(listener);
//			loader.execute();
//		}
	}
	
	public static void asyncDrawbaleBitmapCrop(Context context,int resId,int maxSize,OnBitmapCropListener listener){
		AsyncBitmapCrop23 loader = new AsyncBitmapCrop23();
		loader.setData(context, resId, maxSize);
		loader.setOnBitmapCropListener(listener);
		loader.execute();
		
//		if (Build.VERSION.SDK_INT  <= 10){
//			AsyncBitmapCrop23 loader = new AsyncBitmapCrop23();
//			loader.setData(context, resId, maxSize);
//			loader.setOnBitmapCropListener(listener);
//			loader.execute();
//		}
//		else {
//			AsyncBitmapCrop loader = new AsyncBitmapCrop();
//			loader.setData(context, resId, maxSize);
//			loader.setOnBitmapCropListener(listener);
//			loader.execute();
//		}
	}
}
