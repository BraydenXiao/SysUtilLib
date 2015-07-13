package org.aurona.lib.onlineImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class ImageButtonOnLine extends ImageButton{
	
	private AsyncImageLoader loader = new AsyncImageLoader();
	private Bitmap bitmap;
	
	public ImageButtonOnLine(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	/*设置ImageView With Url*/
	public void setImageBitmapFromUrl(String url){
	
		//this.clearBitmap();
		final Bitmap bm1 =  this.loader.loadImageBitamp(this.getContext(), url, new AsyncImageLoader.ImageCallback(){
			@Override
			public void imageLoaded(Bitmap imageDrawable) {	
				clearBitmap();
				bitmap = imageDrawable;
				setImageBitmap(bitmap);
			}

			@Override
			public void imageLoadedError(Exception e) {
			}
			
		});
		
		if(bm1 != null){
			clearBitmap();
			bitmap = bm1;
			this.setImageBitmap(bitmap);
		}
	}
	
	private void clearBitmap(){
		super.setImageBitmap(null);
		if(bitmap != null && !bitmap.isRecycled()){
			bitmap.recycle();
			bitmap = null;
		}		
	}
}
