package org.aurona.lib.bitmap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

public class BitmapUtil {
	
	public static Bitmap readDrawableWithStream(Context context, int resId){
		InputStream is =  context.getResources().openRawResource(resId);
		Bitmap btp =BitmapFactory.decodeStream(is,null, null);   
		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return btp;
	}
	
	
	public static Bitmap sampeMinZoomFromFile(String fileName,int minSize){
		Bitmap bmp = sampledBitmapFromFile(fileName,minSize,minSize);
		if(bmp == null){
			return null;
		}
		Bitmap newBitmap = sampeMinZoomFromBitmap(bmp,minSize);
		if(bmp != newBitmap){
			ourBitmapRecycle(bmp,true);
		}
		bmp = null;
		return newBitmap;
	}
	
	public static Bitmap sampeMinZoomFromBitmap(Bitmap bmp,int minSize){
		if(bmp == null){
			return null;
		}
		int width  = bmp.getWidth();
		int height = bmp.getHeight();
		
		float rateH  = (float)minSize  / (float)width ;
		float rateW  =  (float)minSize / (float)height;
		
		float rate = rateH < rateW ? rateW : rateH ;

		Matrix matrix = new Matrix();
			  
		matrix.postScale(rate, rate);	
		Bitmap newbm = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
		return newbm;
	}
	
	public static Bitmap sampeZoomFromFile(String fileName,int maxSize){
		Bitmap bmp = sampledBitmapFromFile(fileName,maxSize,maxSize);
		if(bmp == null){
			return null;
		}
		Bitmap newBitmap = sampeZoomFromBitmap(bmp,maxSize);
		if(bmp != newBitmap){
			ourBitmapRecycle(bmp,false);
		}
		bmp = null;
		return newBitmap;
	}
	
	
	public static Bitmap sampeZoomFromResource(Resources res, int resId,int maxSize){
		Bitmap bmp = sampledBitmapFromResource(res,resId,maxSize);
		
		Bitmap newBitmap = sampeZoomFromBitmap(bmp,maxSize);
		if(bmp != newBitmap){
			ourBitmapRecycle(bmp,false);
		}
		bmp = null;
		return newBitmap;
	}
	
	public static Bitmap sampeZoomFromBitmap(Bitmap bmp,int maxSize){
		int width  = bmp.getWidth();
		int height = bmp.getHeight();
		
		float rateH  = (float)maxSize  / (float)width ;
		float rateW  =  (float)maxSize / (float)height;
		
		float rate = rateH < rateW ? rateH : rateW;

		Matrix matrix = new Matrix();
			  
		matrix.postScale(rate, rate);	
		Bitmap newbm = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
		return newbm;
	}
	
	public static Bitmap zoomImg(Bitmap bm, int newWidth ,int newHeight,boolean rotate, int degree){
		   int width = bm.getWidth();
		   int height = bm.getHeight();
		   float scaleWidth = ((float) newWidth) / width;
		   float scaleHeight = ((float) newHeight) / height;
		   Matrix matrix = new Matrix();
		   if(rotate){
			   matrix.postScale(-scaleWidth, scaleHeight);
		   }else{
			   matrix.postScale(scaleWidth, scaleHeight);		   
		   }
		   matrix.postRotate(degree);
		   Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
		    return newbm;
	}
	
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		
		if (height > reqHeight || width > reqWidth) {
			
			float r1 = (float) height / (float) reqHeight;
			float r2 = (float) width / (float) reqWidth;
			
			int rateH = Math.round((float) height / (float) reqHeight);
			int rateW = Math.round((float) width / (float) reqWidth);
			
			inSampleSize = rateH > rateW? rateH:rateW;
//			if (width > height) {
//				inSampleSize = Math.round((float) height / (float) reqHeight);
//			} else {
//				inSampleSize = Math.round((float) width / (float) reqWidth);
//			}
		}
		return inSampleSize;
	}
	
	
	public static BitmapFactory.Options  bitmapOptionFromResource(Resources res, int resId){
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bm = BitmapFactory.decodeResource(res, resId,options);
		if(bm != null && !bm.isRecycled()){
			bm.recycle();
			bm = null;
		}
		return options;
	}

	public static BitmapFactory.Options  bitmapOptionFromFilename(String filename){
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bm = BitmapFactory.decodeFile(filename, options);
		if(bm != null && !bm.isRecycled()){
			bm.recycle();
			bm = null;
		}
		return options;
	}
	
	public static BitmapFactory.Options  bitmapOptionFromStream(InputStream is){
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bm = BitmapFactory.decodeStream(is,null,options);
		if(bm != null && !bm.isRecycled()){
			bm.recycle();
			bm = null;
		}
		return options;
	}
	
	public static Options bitmapOptionFromUri(Context context, Uri uri){
		InputStream inputStream = null;
		try {
			inputStream = context.getContentResolver().openInputStream(uri);
			if(inputStream == null) return null;
			Options option = BitmapUtil.optionOfBitmapFromStream(inputStream);	
			inputStream.close();
			return option;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Bitmap sampledBitmapFromResource(Resources res,int resId,
			int maxSize) {
		BitmapFactory.Options options = bitmapOptionFromResource(res,resId);
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, maxSize,
				maxSize);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}
	
	public static Bitmap sampledBitmapFromFile(String filename,
			int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filename, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		options.inPurgeable = true; 
		options.inInputShareable = true;
		return BitmapFactory.decodeFile(filename, options);
	}
	
	
	/**/
	public static BitmapFactory.Options optionOfBitmapFromStream(InputStream queryIs){
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(queryIs, null, options);
		return options;
	}
	
	public static Bitmap sampledBitmapFromStream(InputStream queryIs,BitmapFactory.Options options,int reqWidth, int reqHeight){
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		options.inPurgeable = true; 
		options.inInputShareable = true;
		return BitmapFactory.decodeStream(queryIs, null, options);
	}
	
	
	public static Bitmap sampledBitmapFromStream(InputStream queryIs,
			InputStream is, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(queryIs, null, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		options.inPurgeable = true; 
		options.inInputShareable = true;
		return BitmapFactory.decodeStream(is, null, options);
	}
	
	/*
	 * Bitmap Recycle； 正常情况不进行调用
	 * */
	public static void ourBitmapRecycle(Bitmap bmp,boolean force){
		if(bmp != null && !bmp.isRecycled()){
			bmp.recycle();
		}
		bmp = null;
	}
	
	public static int degreesToExifOrientation(float normalizedAngle) {
		if (normalizedAngle == 0.0f) {
			return ExifInterface.ORIENTATION_NORMAL;
	    }else if (normalizedAngle == 90.0f) {
	        return ExifInterface.ORIENTATION_ROTATE_90;
	    }else if (normalizedAngle == 180.0f) {
	        return ExifInterface.ORIENTATION_ROTATE_180;
	    }else if (normalizedAngle == 270.0f) {
	        return ExifInterface.ORIENTATION_ROTATE_270;
	        }
	        return ExifInterface.ORIENTATION_NORMAL;
	 }

	 public static float exifOrientationToDegrees(int exifOrientation) {
		 if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
	            return 90;
	      } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
	            return 180;
	      } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
	            return 270;
	      }
	      return 0;
	}
	 
	 
	public static  int getOrientation(Context context, Uri photoUri) {
		Cursor cursor = null;
		try {
			try {
				cursor = context.getContentResolver().query(photoUri,
						new String[] { MediaStore.Images.ImageColumns.ORIENTATION },
						null, null, null);
			} catch (android.database.sqlite.SQLiteException e) {
				return -1;
			}
			
			if (cursor == null) {
				return -1;
			}

			if (cursor.moveToFirst()) {
				return cursor.getInt(0);
			} else {
				return -1;
			}
		} finally {
			if(cursor != null)
				cursor.close();
		}
	}
	 
	
	public  static Bitmap readBitMap(Context context, int resId){  
		BitmapFactory.Options opt = new BitmapFactory.Options();  
		opt.inPreferredConfig = Bitmap.Config.ARGB_8888;   
       
		//获取资源图片  
		InputStream is = context.getResources().openRawResource(resId);  
		return BitmapFactory.decodeStream(is,null,opt);  
	}  
	

	public static Bitmap getImageThumbnail(String imagePath, int width, int height) {
		  Bitmap bitmap = null;
		  BitmapFactory.Options options = new BitmapFactory.Options();
		  options.inJustDecodeBounds = true;
		  // 获取这个图片的宽和高，注意此处的bitmap为null
		  bitmap = BitmapFactory.decodeFile(imagePath, options);
		  options.inJustDecodeBounds = false; // 设为 false
		  // 计算缩放比
		  int h = options.outHeight;
		  int w = options.outWidth;
		  int beWidth = w / width;
		  int beHeight = h / height;
		  int be = 1;
		  if (beWidth < beHeight) {
		   be = beWidth;
		  } else {
		   be = beHeight;
		  }
		  if (be <= 0) {
		   be = 1;
		  }
		  options.inSampleSize = be;
		  // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
		  bitmap = BitmapFactory.decodeFile(imagePath, options);
		  // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
		  bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
		    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		  return bitmap;
	 }
	
	  //获得圆角图片的方法  
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,float roundPx){  
          
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap  
                .getHeight(), Config.ARGB_8888);  
        Canvas canvas = new Canvas(output);  
   
        final int color = 0xff424242;  
        final Paint paint = new Paint();  
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());  
        final RectF rectF = new RectF(rect);  
   
        paint.setAntiAlias(true);  
        canvas.drawARGB(0, 0, 0, 0);  
        paint.setColor(color);  
        /** 
         * 画一个圆角矩形 
         * rectF: 矩形 
         * roundPx 圆角在x轴上或y轴上的半径 
         */  
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);  
        //设置两张图片相交时的模式  
        //setXfermode前的是 dst 之后的是src  
        //在正常的情况下，在已有的图像上绘图将会在其上面添加一层新的形状。   
        //如果新的Paint是完全不透明的，那么它将完全遮挡住下面的Paint；  
        //PorterDuffXfermode就可以来解决这个问题  
        //canvas原有的图片 可以理解为背景 就是dst  
        //新画上去的图片 可以理解为前景 就是src  
//      paint.setXfermode(new PorterDuffXfermode(Mode.SRC_OUT));  
        paint.setXfermode(new android.graphics.PorterDuffXfermode(Mode.SRC_IN));  
        canvas.drawBitmap(bitmap, rect, rect, paint);  
   
        return output;  
    }  
	 
    public static Bitmap getImageFromResourceFile(Resources res, int fileID){
    	Bitmap image = null;
        try
        {
            InputStream is = res.openRawResource(fileID);
            image = BitmapFactory.decodeStream(is);
            is.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return image;
    }
		 
    
    
    public static Bitmap getImageFromAssetsFile(Resources res,String fileName){
        Bitmap image = null;
        try{
            InputStream is = res.getAssets().open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return image;
    }

    public static Bitmap getImageFromAssetsFile(Resources res,String fileName,BitmapFactory.Options options){
    	 Bitmap image = null;
         try{
             InputStream is = res.getAssets().open(fileName);
             image = BitmapFactory.decodeStream(is,null,options);
             is.close();
         }catch (IOException e){
             e.printStackTrace();
         }
         return image;
    }

    public static Bitmap getImageFromAssetsFile(Resources res, String fileName, int sampleSize){
        Bitmap image = null;
        try
        {
            InputStream is = res.getAssets().open(fileName);
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = sampleSize;
    		options.inPurgeable = true; 
    		options.inInputShareable = true;
            image = BitmapFactory.decodeStream(is, null, options);
            is.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return image;
    }
    
    
    
    public static InputStream getSDFileInputStream(Context context,String fileName){
	    boolean hasSD = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); 
		if(!hasSD) return null;
		
		String SDPATH = Environment.getExternalStorageDirectory().getPath();
		if(fileName.contains(SDPATH)) fileName = fileName.replace(SDPATH, "");
		File file = new File(SDPATH + "//" + fileName); 
        try { 
            FileInputStream fis = new FileInputStream(file); 
            return fis;
        } catch (FileNotFoundException e) { 
            e.printStackTrace(); 

        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
        return null;
    }
    
    public static Bitmap getImageFromSDFile(Context context,String fileName, int sampleSize){
        Bitmap image = null;
        try
        {
            InputStream is = getSDFileInputStream(context,fileName);
            if(is != null){
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = sampleSize;
        		options.inPurgeable = true; 
        		options.inInputShareable = true;
                image = BitmapFactory.decodeStream(is, null, options);
               is.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return image; 
    }
    
    
    public static Bitmap getImageFromSDFile(Context context,String fileName){
        Bitmap image = null;
        try
        {
            InputStream is = getSDFileInputStream(context,fileName);
            if(is != null){
               image = BitmapFactory.decodeStream(is);
               is.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return image; 
    }
    
	public static String imagelPathFromURI(Context context, Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(contentUri,
        		proj,
				null, null, null);
        
        String path = "";
        if(cursor != null && cursor.moveToFirst()){
        	int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        	path = cursor.getString(column_index);
        }
        
        if(cursor != null) cursor.close(); 
        cursor = null;
        
        return path; 
    }
}
