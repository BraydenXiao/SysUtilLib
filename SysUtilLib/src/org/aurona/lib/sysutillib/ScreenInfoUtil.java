package org.aurona.lib.sysutillib;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class ScreenInfoUtil {
	/**
	* 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	*/
	public static int dip2px(Context context, float dpValue) {
		Resources res = context.getResources();
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	* 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	*/
	public static int px2dip(Context context, float pxValue) {
	  final float scale = context.getResources().getDisplayMetrics().density;
	  return (int) (pxValue / scale + 0.5f);
	}
	
	
	
	public static int screenWidthDp(Context context){
		int width = context.getResources().getDisplayMetrics().widthPixels;
		return px2dip(context,width);
	}
	
	public static int screenHeightDp(Context context){
		int width = context.getResources().getDisplayMetrics().heightPixels;
		return px2dip(context,width);
	}
	
	public static float screenDensity(Context context){
        final float scale = context.getResources().getDisplayMetrics().density; 
        return scale;
	}
	
	public static int screenWidth(Context context){
		int width = context.getResources().getDisplayMetrics().widthPixels;
		return width;
	}
	
	public static int screenHeight(Context context){
		int width = context.getResources().getDisplayMetrics().heightPixels;
		return width;
	}
	
	public static void resizeView(View v,float rateW,float rateH){
		ViewGroup.LayoutParams layout = v.getLayoutParams();
		if(layout instanceof FrameLayout.LayoutParams){
			FrameLayout.LayoutParams f_layout= (FrameLayout.LayoutParams)layout;
			int n_margin_left   = (int)(f_layout.leftMargin  * rateW + 0.5);
			int n_margin_right  = (int)(f_layout.rightMargin * rateW + 0.5);
			int n_margin_top    = (int)(f_layout.topMargin   * rateH + 0.5);
			int n_margin_bottom = (int)(f_layout.bottomMargin   * rateH + 0.5);
			
			f_layout.setMargins(n_margin_left, n_margin_top, n_margin_right, n_margin_bottom);
			
			if(f_layout.width >= 0){
				f_layout.width = (int)(f_layout.width * rateW + 0.5);
			}
			
			if(f_layout.height >=0){
				f_layout.height = (int)(f_layout.height * rateH + 0.5);
			}
		}
		
		if(layout instanceof RelativeLayout.LayoutParams){
			RelativeLayout.LayoutParams f_layout= (RelativeLayout.LayoutParams)layout;
			int n_margin_left   = (int)(f_layout.leftMargin  * rateW + 0.5);
			int n_margin_right  = (int)(f_layout.rightMargin * rateW + 0.5);
			int n_margin_top    = (int)(f_layout.topMargin   * rateH + 0.5);
			int n_margin_bottom = (int)(f_layout.bottomMargin   * rateH + 0.5);
			
			f_layout.setMargins(n_margin_left, n_margin_top, n_margin_right, n_margin_bottom);
			
			if(f_layout.width >= 0){
				f_layout.width = (int)(f_layout.width * rateW + 0.5);
			}
			
			if(f_layout.height >=0){
				f_layout.height = (int)(f_layout.height * rateH + 0.5);
			}
		}
	} 
	
}
