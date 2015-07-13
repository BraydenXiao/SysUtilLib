package org.aurona.lib.http;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.os.Handler;
import android.util.Log;

public class AsyncTextHttp{
	
	public interface AsyncTextHttpTaskListener {
		public void onRequestDidFailedStatus(Exception e);
		public void onRequestDidFinishLoad(String result);
	}
	
	private AsyncTextHttpTaskListener listener;
	private String url;
	int mConnectionTimeout = 5000;
	int mSocketTimeout = 5000;
	
	public  AsyncTextHttp(String url){
		this.url = url;
	}
	
	
	public static void asyncHttpRequest(String url,AsyncTextHttpTaskListener listener){
		AsyncTextHttp loader = new AsyncTextHttp(url);
		loader.setListener(listener);
		loader.execute();
	}
	
	
	public void setUrl(String url){
		this.url = url;
	}
	
	public AsyncTextHttpTaskListener getListener() {
		return listener;
	}

	public void setListener(AsyncTextHttpTaskListener listener) {
		this.listener = listener;
	}


	private final Handler handler = new Handler();
	public void execute(){
		new Thread(new Runnable() {  
            @Override  
            public void run() { 
            	String responseString = null;
            	
        		try {
	    			DefaultHttpClient httpClient = new DefaultHttpClient();
	    			HttpGet httpGet = new HttpGet(url);
	    			HttpParams httpParams = httpClient.getParams();
	    			HttpConnectionParams.setConnectionTimeout(httpParams, mConnectionTimeout);
	    			HttpConnectionParams.setSoTimeout(httpParams, mSocketTimeout);
	    
	    			ResponseHandler<String> responseHandler = new BasicResponseHandler();
	    			responseString = httpClient.execute(httpGet, responseHandler);
	    			Log.d("response", responseString);
	    			
	 
        		}catch (Exception e) {
//        			if(listener != null){
//        				listener.onRequestDidFailedStatus(e);
//        			}
        		}
        		
        		final String result = responseString;
                handler.post(new Runnable() {  
                	@Override  
                    public void run() {  
                		
                		if(listener != null){
                			if(result != null){
                    			listener.onRequestDidFinishLoad(result);
                			}else{
                				listener.onRequestDidFailedStatus(null);
                			}
                		}
                    }  
                });  
                
                
            }  
        }).start();  
	}

	
//	@Override
//	protected String doInBackground(String... arg0) {
//		try {
//			DefaultHttpClient httpClient = new DefaultHttpClient();
//			HttpGet httpGet = new HttpGet(url);
//			HttpParams httpParams = httpClient.getParams();
//			HttpConnectionParams.setConnectionTimeout(httpParams, mConnectionTimeout);
//			HttpConnectionParams.setSoTimeout(httpParams, mSocketTimeout);
//
//			ResponseHandler<String> responseHandler = new BasicResponseHandler();
//			String responseString = httpClient.execute(httpGet, responseHandler);
//			Log.d("response", responseString);
//			return responseString;
//			  
//		}catch (Exception e) {
//			if(listener != null){
//				this.listener.onRequestDidFailedStatus(e);
//			}
//		}
//		
//		return null;
//	}
//	
//
//	@Override
//	protected void onPreExecute() {
//		super.onPreExecute();
//	}
//
//	@Override
//	protected void onPostExecute(String result) {
//		super.onPostExecute(result);
//		if(listener != null){
//			this.listener.onRequestDidFinishLoad(result);
//		}
//	}
	
}
