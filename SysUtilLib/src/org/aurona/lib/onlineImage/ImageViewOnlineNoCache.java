package org.aurona.lib.onlineImage;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ImageViewOnlineNoCache extends ImageView{
	
	public interface OnImageLoadListener{
		public void onLoadSucc();
		public void onLoadFail();
		
	};
	private AsyncImageLoaderNoCache loader = new AsyncImageLoaderNoCache();
	private Bitmap bitmap;
	
	public ImageViewOnlineNoCache(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
		
	/*设置ImageView With Url*/
	public void setImageBitmapFromUrl(String url){
		setImageBitmapFromUrl(url,null);
	}
	
	public void setImageBitmapFromUrl(String url,final OnImageLoadListener listener){
		final Bitmap bm1 =  this.loader.loadImageBitamp(this.getContext(), url, new AsyncImageLoaderNoCache.ImageCallback(){
			@Override
			public void imageLoaded(Bitmap imageDrawable) {	
				clearBitmap();
				bitmap = imageDrawable;
				setImageBitmap(bitmap);
				if(listener != null){
					listener.onLoadSucc();
				}
			}

			@Override
			public void imageLoadedError(Exception e) {
				if(listener != null){
					listener.onLoadFail();
				}
			}
			
		});
		
		if(bm1 != null){
			clearBitmap();
			bitmap = bm1;
			this.setImageBitmap(bitmap);
			if(listener != null){
				listener.onLoadSucc();
			}
		}
	}
	
	public void clearBitmap(){
		super.setImageBitmap(null);
		if(bitmap != null && !bitmap.isRecycled()){
			bitmap.recycle();
			bitmap = null;
		}		
	}
	
	
}
