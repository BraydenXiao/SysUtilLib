/*
 * 缓存的基本机制：
 *    @1：  磁盘缓存     @2： 按照URL缓存     @3： 缓存过期时间无限（短期内）
 * 
 * */

package org.aurona.lib.onlineImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpStatus;
import org.aurona.lib.sysutillib.PreferencesUtil;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;

public class AsyncImageLoader {

	public interface ImageCallback {
		// 注意 此方法是用来设置目标对象的图像资源
		public void imageLoaded(Bitmap imageDrawable);
		public void imageLoadedError(Exception e);
	}
	
	public interface OnLineImageToFileCallback {
		// 注意 此方法是用来设置目标对象的图像资源
		public void imageDownload(String filename);
		public void imageDownloadFaile(Exception e);
	}	
	
	private ExecutorService executorService = Executors.newFixedThreadPool(5); // 固定五个线程来执行任务
	private final Handler handler = new Handler();
	static String image_cache_key_perfix = "cache_";
	static String PrefsName = "AsyncImageLoader";
	
	public static String cachDir(){
		File  dir = new File(Environment.getExternalStorageDirectory().getPath()+ "/.tmp/");
		if (!dir.exists()) {
			dir.mkdir();
		}	
		return dir.getAbsolutePath();	
	}
	
	
	public void loadImageToFile(final Context context,final String imageUrl,final String filename,
			final OnLineImageToFileCallback callback){
		executorService.submit(new Runnable() {
			public void run() {			
				try{
					saveToDiskFromUrl(imageUrl,filename);
					handler.post(new Runnable() {
						public void run() {
							if(callback != null){			
								callback.imageDownload(filename);
							}
						}
					});
					
				}catch(Exception e){
					if(callback != null){
						callback.imageDownloadFaile(e);
					}
				}
			}
		});
		
	}
	
	
	public Bitmap loadImageBitamp(final Context context,final String imageUrl,
			final ImageCallback callback) {
		final String key = image_cache_key_perfix + imageUrl;
		String file_path = PreferencesUtil.get(context, PrefsName, key);
		if(file_path == null){		
			executorService.submit(new Runnable() {
				public void run() {
					String uuid = UUID.randomUUID().toString();
					final String file_name =  cachDir() + uuid;
					try{
						saveToDiskFromUrl(imageUrl,file_name);
						PreferencesUtil.save(context, PrefsName, key,file_name);
						handler.post(new Runnable() {
							public void run() {
								if(callback != null){
									final Bitmap bitmap = BitmapFactory.decodeFile(file_name);
									callback.imageLoaded(bitmap);
								}
							}
						});
						
					}catch(Exception e){
						if(callback != null){
							callback.imageLoadedError(e);
						}
					}
				}
			});
		}else{
			Bitmap bitmap = BitmapFactory.decodeFile(file_path);
			return bitmap;
		}
		
		return null;
	}
	
	
	// 从网络上取数据方法
	protected Bitmap loadImageFromUrl(String imageUrl) {
		try {
			Bitmap result = BitmapFactory.decodeStream(new URL(imageUrl).openStream());
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void  saveToDiskFromUrl(String imageUrl, String filename) throws Exception {  
		URL url = new URL(imageUrl);      
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
        conn.setConnectTimeout(5000);  
        conn.setRequestMethod("GET");  
        
        conn.setDoInput(true);  
        try {
        	if (conn.getResponseCode() == 200) {
        		int lenght = conn.getContentLength();
            	InputStream is = conn.getInputStream();  
                    FileOutputStream fos = new FileOutputStream(filename);  
                    byte[] buffer = new byte[4096];  
                    int len = 0;  
                    while ((len = is.read(buffer)) != -1) {  
                        fos.write(buffer, 0, len);  
                    }  
                    is.close();  
                    fos.close();  
           }else{
        	   throw new RuntimeException(String.valueOf(conn.getResponseCode()));
           }  
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
        
    }  
	
	public long getFileSize(String urlString) throws IOException,Exception{
		  long lenght = 0;
//		  String url =  UrlEncode(urlString, "UTF-8");
		  URL mUrl =  new URL(urlString);
//		  URL mUrl =  new URL(url);
		  HttpURLConnection conn = (HttpURLConnection) mUrl.openConnection();
		  conn.setConnectTimeout(5*1000);
		  conn.setRequestMethod("GET");
		  conn .setRequestProperty("Accept-Encoding", "identity"); 
//		  conn.setRequestProperty("Referer", url); 
		  conn.setRequestProperty("Referer", urlString); 
		  conn.setRequestProperty("Charset", "UTF-8");
		  conn.setRequestProperty("Connection", "Keep-Alive");
		  conn.connect();
		  
		     int responseCode  = conn.getResponseCode();
		     // 判断请求是否成功处理  
		     if (responseCode == HttpStatus.SC_OK) {  
		           lenght = conn.getContentLength();
		     }
		       
		  return lenght;
		 }
}
