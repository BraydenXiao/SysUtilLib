package org.aurona.lib.view.image;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;

public class BorderImageView extends IgnoreRecycleImageView{
	
	int borderColor = Color.TRANSPARENT;
	float borderWidth = 5;
	
	Paint paint = new Paint();
	RectF boundRect = new RectF();
	boolean isShowBorder = false;
	Bitmap srcBitmap = null;
	boolean isCircle = false;//若只设置isCircle=true，不设置radius的值，则绘制圆形
	int radius = 0;//若设置isCircle为true，同时设置radius的值，则绘制圆角矩形
	
	boolean isFillet = false;
	
	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	int imageColor = Color.TRANSPARENT;
	
	public void setImageColor(int color)
	{
		imageColor = color;
	}
	
	public int getImageColor()
	{
		return imageColor;
	}
	
	public BorderImageView(Context context) {
		super(context);
	}

	public BorderImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BorderImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);	
	}
	
	@Override
	public void setImageBitmap(Bitmap bmp)
	{
		if(bmp == null || bmp.isRecycled())
		{
			super.setImageBitmap(null); 
		}else{
			this.srcBitmap = bmp;
			super.setImageBitmap(bmp);
		}
		this.invalidate();
	}
	
	public void setShowBorder(boolean showBorder){
		isShowBorder = showBorder;
	}
	
	public boolean isShowBorder(){
		return isShowBorder;
	}
		
	
	public void setBorderWidth(int width){
		borderWidth = width;
	}
	
	public void setFilletState(boolean state)
	{
		isFillet = state;
	}
	
	public boolean getFilletState()
	{
		return isFillet;
	}
	
	public void setCircleState(boolean state)
	{
		isCircle = state;
	}
	
	public boolean getCircleState()
	{
		return isCircle;
	}
	
	public void setBorderColor(int color){
		borderColor = color;
	}
	
	

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		if(!isCircle)
		{
			if(!isFillet)
			{
				if(srcBitmap != null && !srcBitmap.isRecycled())
				{
					super.onDraw(canvas);
				}
			}
			else {
				
				if(srcBitmap != null && !srcBitmap.isRecycled())
				{
					Bitmap b = getFilletBitmap(srcBitmap); 
					
					final Rect rectSrc = new Rect(0, 0, b.getWidth(), b.getHeight()); 
					Rect rectDest = new Rect(0,0,getWidth(),getWidth());
					if(getWidth() > getHeight())
					{
						rectDest = new Rect((getWidth() - getHeight())/2,0,getHeight() + (getWidth() - getHeight())/2,getHeight());
					}
					else {
						rectDest = new Rect(0,(getHeight() - getWidth())/2,getWidth(),getWidth() + (getHeight() - getWidth())/2);
					}
		            paint.reset();  
		            canvas.drawBitmap(b, rectSrc, rectDest, paint);  
		            
		            if(b != srcBitmap && b != null && !b.isRecycled())
		            {
		            	b.recycle();
		            	b = null;
		            }
				}
				else if(imageColor != Color.TRANSPARENT)
				{
					paint.setAntiAlias(true);  
			        canvas.drawARGB(0, 0, 0, 0);  
			        
			        paint.setColor(imageColor);  
			        Rect rect = new Rect(0, 0, getWidth(), getHeight()); 
			        canvas.drawRoundRect(new RectF(rect), 10f, 10f, paint); 
				}
			}
			if(this.isShowBorder){
				boundRect.left  = 0;
				boundRect.top   = 0;
				boundRect.right   = this.getWidth();
				boundRect.bottom  = this.getHeight();
				paint.reset(); 
				paint.setAntiAlias(true); 
				paint.setColor(borderColor);
				paint.setStyle(Paint.Style.STROKE);
				paint.setStrokeWidth(borderWidth);
				canvas.drawRect(boundRect, paint);
			}
		}
		else {
			if(srcBitmap != null && !srcBitmap.isRecycled())
			{
				
				Bitmap b;
				if(radius==0){
					b = getCircleBitmap(srcBitmap);
				}else{
					b = getRadiusRectBitmap(srcBitmap);
				}
				
				final Rect rectSrc = new Rect(0, 0, b.getWidth(), b.getHeight()); 
				Rect rectDest = new Rect(0,0,getWidth(),getWidth());
				if(getWidth() > getHeight())
				{
					rectDest = new Rect((getWidth() - getHeight())/2,0,getHeight() + (getWidth() - getHeight())/2,getHeight());
				}
				else {
					rectDest = new Rect(0,(getHeight() - getWidth())/2,getWidth(),getWidth() + (getHeight() - getWidth())/2);
				}
	            paint.reset();  
	            canvas.drawBitmap(b, rectSrc, rectDest, paint);  
	            
	            if(b != srcBitmap && b != null && !b.isRecycled())
	            {
	            	b.recycle();
	            	b = null;
	            }
			}
			else if(imageColor != Color.TRANSPARENT)
			{
				paint.setAntiAlias(true);  
		        canvas.drawARGB(0, 0, 0, 0);  
		        paint.setColor(imageColor);  
		        int x = getWidth();
		        if(x > getHeight())
		        {
		        	x= getHeight();
		        }
		        canvas.drawCircle(getWidth() / 2, getHeight() / 2, x / 2, paint); 
			}
		}
	}
	
	 private Bitmap getFilletBitmap(Bitmap bitmap) {  
	        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),  
	                bitmap.getHeight(), Config.ARGB_8888);  
	        Canvas canvas = new Canvas(output); final int color = 0xff424242; 
	        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());  
	        paint.reset(); 
	        paint.setAntiAlias(true);  
	        canvas.drawARGB(0, 0, 0, 0);  
	        paint.setColor(color);  
	        
	        canvas.drawRoundRect(new RectF(rect), 10f, 10f, paint);  
	        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));  
	        canvas.drawBitmap(bitmap, rect, rect, paint); 
	        return output;  
	        
	 }  

	 private Bitmap getCircleBitmap(Bitmap bitmap) {  
	        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),  
	                bitmap.getHeight(), Config.ARGB_8888);  
	        Canvas canvas = new Canvas(output); final int color = 0xff424242; 
	        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());  
	        paint.setAntiAlias(true);  
	        canvas.drawARGB(0, 0, 0, 0);  
	        paint.setColor(color); int x = bitmap.getWidth(); 
	        
	        canvas.drawCircle(x / 2, x / 2, x / 2, paint);  
	        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));  
	        canvas.drawBitmap(bitmap, rect, rect, paint); return output;  
	        
	 }  
	 
	 private Bitmap getRadiusRectBitmap(Bitmap bitmap) {  
	        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),  
	                bitmap.getHeight(), Config.ARGB_8888);  
	        Canvas canvas = new Canvas(output); final int color = 0xff424242; 
	        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());  
	        paint.setAntiAlias(true);  
	        canvas.drawARGB(0, 0, 0, 0);  
	        paint.setColor(color); int x = bitmap.getWidth(); 
	        
//	        canvas.drawCircle(x / 2, x / 2, x / 2, paint);
	        RectF rectf = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
	        canvas.drawRoundRect(rectf,(float)radius,(float)radius,paint);
	        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));  
	        canvas.drawBitmap(bitmap, rect, rect, paint); return output;  
	 }  
}
