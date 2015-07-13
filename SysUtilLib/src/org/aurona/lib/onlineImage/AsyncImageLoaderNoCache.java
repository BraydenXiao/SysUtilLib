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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.aurona.lib.sysutillib.PreferencesUtil;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

public class AsyncImageLoaderNoCache {

	public interface ImageCallback {
		// 注意 此方法是用来设置目标对象的图像资源
		public void imageLoaded(Bitmap imageDrawable);
		public void imageLoadedError(Exception e);
	}
		
	
	private ExecutorService executorService = Executors.newFixedThreadPool(2); // 固定五个线程来执行任务
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
	
	public Bitmap loadImageBitamp(final Context context,final String imageUrl,
			final ImageCallback callback) {
		Log.w("AsyncImageLoader","loadImageBitmap");
		
		executorService.submit(new Runnable() {
			public void run() {
				try{

					final Bitmap bmp = bitmapFromNetUrl(imageUrl);
					handler.post(new Runnable() {
						public void run() {
							if(callback != null){
								callback.imageLoaded(bmp);
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
		
		return null;
	}
	
	
	// 从网络上取数据方法
	public Bitmap loadImageFromUrl(String imageUrl) {
		try {
			Log.w("AsyncImageLoader","loadImageFromUrl "+imageUrl);
//			URL url = new URL(imageUrl);
//			Bitmap result = BitmapFactory.decodeStream(url.openStream());
//			return result;
			HttpClient client = new DefaultHttpClient();  
			HttpParams params = client.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 2000);  
	        HttpConnectionParams.setSoTimeout(params, 3000); 
	        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 2000);
	        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,  3000);
	        HttpGet get = new HttpGet(imageUrl);
			HttpResponse httpResponse;
			byte[] data;
			httpResponse = client.execute(get);
			int resStatu = httpResponse.getStatusLine().getStatusCode();
			Log.w("AsyncImageLoader","connected");
			if(resStatu == HttpStatus.SC_OK){
				HttpEntity httpEntity = httpResponse.getEntity();
				if(httpEntity != null){
					data = EntityUtils.toByteArray(httpEntity);
					Bitmap result = BitmapFactory.decodeByteArray(data, 0, data.length);
					Log.w("AsyncImageLoader","get bitmap");
					return result;
				}
				else return null;
			}
			else return null;
		} catch (Exception e) {
			//throw new RuntimeException(e);
			Log.w("AsyncImageLoader",e.toString());
			return null;
		}
	}
	
	public Bitmap bitmapFromNetUrl(String imageUrl) throws Exception {

		URL url = new URL(imageUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		//conn.setDoInput(true);
		if (conn.getResponseCode() == 200) {
			Log.w("AsyncImageLoader", "success " + imageUrl);
			InputStream is = conn.getInputStream();

			Bitmap bmp = BitmapFactory.decodeStream(is);
			is.close();
			return bmp;
		} else {
			Log.w("AsyncImageLoader", "failed");
			throw new RuntimeException(String.valueOf(conn.getResponseCode()));
		}
	}
	
	public void  saveToDiskFromUrl(String imageUrl, String filename) throws Exception {  
		URL url = new URL(imageUrl);      
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
        conn.setConnectTimeout(5000);  
        conn.setRequestMethod("GET");  
        conn.setDoInput(true);  
        if (conn.getResponseCode() == 200) {  
        	Log.w("AsyncImageLoader","success "+imageUrl);
        	InputStream is = conn.getInputStream();  
                FileOutputStream fos = new FileOutputStream(filename);  
                byte[] buffer = new byte[1024];  
                int len = 0;  
                while ((len = is.read(buffer)) != -1) {  
                    fos.write(buffer, 0, len);  
                }  
                is.close();  
                fos.close();  
       }else{
    	   Log.w("AsyncImageLoader","failed");
    	   throw new RuntimeException(String.valueOf(conn.getResponseCode()));
       }  
    }  
	
	
}
