package org.aurona.lib.bitmap.output.save;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.aurona.lib.bitmap.BitmapCrop;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Handler;

public class AsyncSaveToSdImpl {

	Bitmap mBitmap;
	String mFullName;
	CompressFormat format;
	private Context context;
	private final Handler handler = new Handler();
	private ExecutorService executorService; // = Executors.newFixedThreadPool(4);
	
	SaveDoneListener onSaveDoneListener;
	private static AsyncSaveToSdImpl loader;// = new AsyncImageLoader();
	
	public static AsyncSaveToSdImpl getInstance(){
		return loader;
	}
	
	public static void initLoader(Context context){
		if(loader == null){
			loader = new AsyncSaveToSdImpl();
		}
		loader.initExecutor();
	}
	
	public static void shutdownLoder(){
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
		mBitmap = null;
	}
	
	public void setData(Context context,Bitmap bitmap, String fullName,final CompressFormat format){
		this.mBitmap = bitmap;
		this.context = context;
		this.mFullName = fullName;
		this.format = format;
	}
	
	public void setOnSaveDoneListener(SaveDoneListener listener){
		this.onSaveDoneListener = listener;
	}
	
	public void execute(){
		executorService.submit(new Runnable() {
			public void run() {

				FileOutputStream fos = null;
				try {

					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					mBitmap.compress(format, 100, baos);
					byte[] data = baos.toByteArray();
					int fsize = data.length;

					fos = new FileOutputStream(mFullName);
					fos.write(data, 0, fsize);
					fos.close();
					fos = null;
					
					baos.close();
					baos = null;
					
					File file = new File(mFullName);
					final Uri uri = Uri.fromFile(file);
					if (onSaveDoneListener != null) {
						  handler.post(new Runnable() {  
					        	@Override  
					            public void run() {  
					        		onSaveDoneListener.onSaveDone(mFullName, uri);
					            }  
						  });  
						
					}
					
//					MediaScannerConnection
//							.scanFile(
//									context,
//									new String[] { mFullName },
//									null,
//									new MediaScannerConnection.OnScanCompletedListener() {
//										@Override
//										public void onScanCompleted(
//												String path, Uri uri) {
//											if (path == null || uri == null) {
//											}
//										}
//									});
				} catch (Exception e) {
					if (fos != null) {
						try {
							fos.close();
						} catch (IOException e1) {
						}
					}
					final Exception e_f = e;
					if (onSaveDoneListener != null) {
						  handler.post(new Runnable() {  
					        	@Override  
					            public void run() {  
					        		onSaveDoneListener.onSavingException(e_f);
					            }  
						  });  
					}
				}		
			}
		});
	}
	
}
