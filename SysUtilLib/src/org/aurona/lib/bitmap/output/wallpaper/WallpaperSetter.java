package org.aurona.lib.bitmap.output.wallpaper;

import org.aurona.lib.sysutillib.ScreenInfoUtil;

import org.aurona.lib.sysutillib.R;

import android.R.integer;
import android.app.Activity;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.widget.Toast;

public class WallpaperSetter {

	public void setImage(Activity activity, Bitmap bitmap){
		try {
			if(bitmap == null || bitmap.isRecycled()){
				Toast.makeText(activity, R.string.warning_no_image, Toast.LENGTH_LONG).show();
				return;
			}
			int screenWidth = ScreenInfoUtil.screenWidth(activity);
			int screenHeight = ScreenInfoUtil.screenHeight(activity);
			WallpaperManager wallpaperManager = WallpaperManager.getInstance(activity);
			wallpaperManager.setBitmap(bitmap);
		} catch (Throwable e) {
			Toast.makeText(activity, R.string.warning_failed_wallpaper, Toast.LENGTH_LONG).show();
		}
	}
}
