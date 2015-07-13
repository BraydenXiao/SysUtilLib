package org.aurona.lib.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;

public class AsyncBitmapCrop extends AsyncTask<String, Void, Bitmap>{

	private static int FromURI      = 0x01;
	private static int FfromDrawbale = 0x02;
	private Context context;
	private Uri itemUri;
	private int maxSize;
	private int type = FromURI;
	
	private int resId;
	OnBitmapCropListener listener;
	
	
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
	
	@Override
	protected Bitmap doInBackground(String... params) {
		Bitmap bmp = null;
		if(type == FfromDrawbale){
			 bmp = BitmapCrop.cropDrawableImage(context, resId, maxSize);
		}else{
			bmp = BitmapCrop.CropItemImage(this.context, itemUri, maxSize);
		}
		return bmp;
	}
	
	@Override
	protected void onPostExecute(Bitmap result) {
		if(listener != null){
			listener.onBitmapCropFinish(result);
		}
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
