package org.aurona.lib.bitmap;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Log;

public class BitmapCrop {
	
	public static Bitmap CropItemImage(Context context,Uri itemUri,int maxSize) {		
		Uri selectedImageUri = itemUri;
		int orientation = -1;
		
		String targetScheme = selectedImageUri.getScheme();
		if(targetScheme != null){
		if (targetScheme.equalsIgnoreCase("file")) {
			 try {
				 ExifInterface exif = new ExifInterface(selectedImageUri.getPath());
				 orientation = (int) BitmapUtil.exifOrientationToDegrees(exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                     ExifInterface.ORIENTATION_NORMAL));	
			} catch (Exception e) {
			}
         }else if((targetScheme.equalsIgnoreCase("content")) ){
        	 
        	 //2014-12-03  更改，解决无法读取Oriential的问题，当读取异常的时候，读取文件Path, 然后去读取Exif信息
        	 try {
        		 orientation = BitmapUtil.getOrientation(context, itemUri);
        	 }catch(Exception e){
    			 try {
            		 String pathfile = BitmapUtil.imagelPathFromURI(context,itemUri);
    				 ExifInterface exif = new ExifInterface(pathfile);
    				 orientation = (int) BitmapUtil.exifOrientationToDegrees(exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                         ExifInterface.ORIENTATION_NORMAL));	
    			} catch (Exception e2) {
    			}
        	 }
         }}
	


		Bitmap retBmp = null;
		try {
			InputStream inputStream = context.getContentResolver().openInputStream(selectedImageUri);
			BitmapFactory.Options option = BitmapUtil.optionOfBitmapFromStream(context.getContentResolver().openInputStream(selectedImageUri));			 
			inputStream.close();

			
			InputStream inputStream2 = context.getContentResolver().openInputStream(selectedImageUri);
			Bitmap bitMap = BitmapUtil.sampledBitmapFromStream(inputStream2, option, maxSize, maxSize);
			inputStream2.close();
			
			retBmp = realCropFromBitmap(bitMap,orientation,maxSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retBmp;
	}
	
	
	public static Bitmap CropSquareImage(Context context, Uri itemUri,int size){
		Uri selectedImageUri = itemUri;
		int orientation = -1;
		
		String targetScheme = selectedImageUri.getScheme();
	
		if (targetScheme.equalsIgnoreCase("file")) {
			 try {
				 ExifInterface exif = new ExifInterface(selectedImageUri.getPath());
				 orientation = (int) BitmapUtil.exifOrientationToDegrees(exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                     ExifInterface.ORIENTATION_NORMAL));	
			} catch (Exception e) {
			}
         }else if((targetScheme.equalsIgnoreCase("content")) ){
        	 orientation = BitmapUtil.getOrientation(context, itemUri);
         }
	
		Bitmap retBmp     = null;
		Bitmap newRetBmp  = null;
		 
		try {
			InputStream inputStream = context.getContentResolver().openInputStream(selectedImageUri);
			BitmapFactory.Options option = BitmapUtil.optionOfBitmapFromStream(context.getContentResolver().openInputStream(selectedImageUri));			 
			inputStream.close();

			int hOri = option.outHeight;
			int wOir = option.outWidth;
			
			float ration = 1f;
			if(hOri > wOir) ration = (float) hOri / (float)wOir;
			else ration = (float) wOir / (float)hOri;
			
			int maxSize = (int) ration * size;
			
			InputStream inputStream2 = context.getContentResolver().openInputStream(selectedImageUri);
			Bitmap bitMap = BitmapUtil.sampledBitmapFromStream(inputStream2, option, maxSize, maxSize);
			inputStream2.close();
			
			retBmp = realCropFromBitmap(bitMap,orientation,maxSize);
			
		    int w = retBmp.getWidth(); // 得到图片的宽，高
	        int h = retBmp.getHeight();

	        int wh = w > h ? h : w;// 裁切后所取的正方形区域边长

	        int retX = w > h ? (w - h) / 2 : 0;//基于原图，取正方形左上角x坐标
	        int retY = w > h ? 0 : (h - w) / 2;

	        //下面这句是关键
	        newRetBmp  = Bitmap.createBitmap(retBmp, retX, retY, wh, wh, null, false);
	        if(retBmp != newRetBmp) retBmp.recycle(); 
	        retBmp = null;
	        
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newRetBmp;
		
	}

	public static Bitmap cropDrawableImage(Context context,int resId,int maxSize){
		BitmapFactory.Options option = BitmapUtil.bitmapOptionFromResource(context.getResources(),resId);	
				
		Bitmap retBmp = null;
		try {
			InputStream inputStream2 =  context.getResources().openRawResource(resId);
			Bitmap bitMap = BitmapUtil.sampledBitmapFromStream(inputStream2, option, maxSize, maxSize);
			inputStream2.close();
			retBmp = realCropFromBitmap(bitMap,0,maxSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retBmp;
		
		
	}
	
	protected static Bitmap realCropFromBitmap(Bitmap srcBitmap, int orientation,int maxSize) throws Exception{
		Bitmap retBmp = null;
		Bitmap bitMap = srcBitmap;
		if (orientation != -1 && orientation != 0) {
			Bitmap oriBmp = bitMap;
			Matrix m = new Matrix();
			m.setRotate(orientation, oriBmp.getWidth(), oriBmp.getHeight());
			Bitmap destBmp = Bitmap.createBitmap(oriBmp, 0, 0,oriBmp.getWidth(), oriBmp.getHeight(), m, true);
			if(destBmp != bitMap && !bitMap.isRecycled()){
				bitMap.recycle();
			}
			bitMap = destBmp;
			
		}
		
		retBmp = bitMap;

		int cropWidth = bitMap.getWidth();
		int cropHeight = bitMap.getHeight();
		//boolean isCropNeeded = true;

		if(bitMap != null && 
				(Math.max(bitMap.getWidth(), bitMap.getHeight())) >= maxSize) {
			float scale =
					((float)bitMap.getWidth()/(float)bitMap.getHeight());
			if(scale > 1){
				cropWidth = cropWidth > maxSize ? maxSize:cropWidth;
				cropHeight = (int)(cropWidth/scale);
			}else{
				cropHeight = cropHeight > maxSize ? maxSize:cropHeight;
				cropWidth = (int)(cropHeight*scale);
			}
		}
		
		try{
			retBmp = Bitmap.createScaledBitmap(bitMap, cropWidth,cropHeight, true);
		}catch(OutOfMemoryError e){
			try{
				int w = (int)(cropWidth  / 0.9f);
				int h = (int)(cropHeight / 0.9f);
				retBmp = Bitmap.createScaledBitmap(bitMap, w,h, true);
			}catch(OutOfMemoryError e2){
				try{
					int w = (int)(cropWidth  / 0.8f);
					int h = (int)(cropHeight / 0.8f);
					retBmp = Bitmap.createScaledBitmap(bitMap, w,h, true);
				}catch(OutOfMemoryError e3){
					int w = (int)(cropWidth  / 0.6f);
					int h = (int)(cropHeight / 0.6f);
					retBmp = Bitmap.createScaledBitmap(bitMap, w,h, true);
				}
			}
		}
		
		int w = retBmp.getWidth();
		int h = retBmp.getHeight();
		Log.v("t", String.valueOf(w) + String.valueOf(h));
		if (bitMap != retBmp) {
			bitMap.recycle();
		}
		return retBmp;
	}

	
	public static Bitmap cropCenterScaleBitmap(Bitmap src, int scaleWidth, int scaleHeight)
    {
	     if(null == src || src.isRecycled() || scaleWidth <= 0 || scaleHeight <= 0)
	     {
	          return  src;
	     }
	     
	     int srcWidth = src.getWidth();
	     int srcHeight = src.getHeight();
	     
	     float scaleWHRatio = (float)scaleWidth/(float)scaleHeight;
	     float srcWHRatio = (float)srcWidth/(float)srcHeight;
	     
	     Rect srcRect = new Rect(0, 0, srcWidth, srcHeight);
	
	     if(scaleWHRatio > srcWHRatio){
	    	 int dstHeight = (int)(srcWidth*(1/scaleWHRatio));
	    	 srcRect.top = (srcHeight-dstHeight)/2;
	    	 srcRect.bottom = srcRect.top + dstHeight;
	     }else if(scaleWHRatio < srcWHRatio){
	    	 int dstWidth = (int)(srcHeight*scaleWHRatio);
	    	 srcRect.left = (srcWidth-dstWidth)/2;
	    	 srcRect.right = srcRect.left + dstWidth;
	     }
	     
	     Bitmap dstScaleBitmap = Bitmap.createBitmap(scaleWidth, scaleHeight, Config.ARGB_8888);
	     Canvas dstScaleCanvas = new Canvas(dstScaleBitmap);
	     dstScaleCanvas.drawBitmap(src, srcRect, new Rect(0, 0, scaleWidth, scaleHeight), new Paint());
	   
	          
	     return dstScaleBitmap;
    }
}
