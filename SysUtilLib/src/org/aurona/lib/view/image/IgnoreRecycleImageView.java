package org.aurona.lib.view.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

public class IgnoreRecycleImageView extends ImageView{

	public IgnoreRecycleImageView(Context context) {
		super(context);
	}

	public IgnoreRecycleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public IgnoreRecycleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public Bitmap image;
	@Override
	public void setImageBitmap(Bitmap bm){
		image = bm;
		super.setImageBitmap(bm);
	}
	
	@Override
	protected void onDraw(Canvas canvas){
		try {
			super.onDraw(canvas);
		} catch (Throwable e) {
		}
	}
	
	
}
