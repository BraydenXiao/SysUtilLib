package org.aurona.lib.bitmap.output.share;

import java.io.File;

import org.aurona.lib.bitmap.BitmapUtil;
import org.aurona.lib.io.BitmapIoCache;
import org.aurona.lib.otherapp.OtherApp;
import org.aurona.lib.packages.AppPackages;
import org.aurona.lib.packages.OtherAppPackages;

import org.aurona.lib.sysutillib.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.widget.Toast;

public class ShareToInstagram {


	private static void shareImage(Activity activity, Bitmap bmp) {
		ShareToApp.shareImage(activity, OtherAppPackages.instagramPackage,
				"shareig", ShareTag.getDefaultTag(activity), bmp);
	}

	private static void shareImageFromUri(Activity activity, Uri bitmapUri) {
		ShareToApp.shareImageFromUri(activity,
				OtherAppPackages.instagramPackage, "shareig",
				ShareTag.getDefaultTag(activity), bitmapUri);
	}

	public static void shareImageFromUri(Activity activity, Uri bitmapUri,
			boolean sized) {
		if (bitmapUri == null) {
			Toast.makeText(
					activity,
					activity.getResources()
							.getString(R.string.warning_no_image),
					Toast.LENGTH_LONG).show();
			return;
		}

//		if (caesarApp == null) {
//			initTag(activity);
//		}

		if (!sized) {
			shareImageFromUri(activity, bitmapUri);
			return;
		}

		Options options = BitmapUtil.bitmapOptionFromUri(activity, bitmapUri);
		if(options == null || (options.outWidth == options.outHeight)){
			shareImageFromUri(activity, bitmapUri);
		} else {
			Bitmap shareBitmap = BitmapUtil.getImageFromSDFile(activity,
					bitmapUri.getPath());
			shareImage(activity, shareBitmap, true);
			if (shareBitmap != null && !shareBitmap.isRecycled())
				shareBitmap.recycle();
			shareBitmap = null;
		}
	}

	public static void shareImage(Activity activity, Bitmap bmp, boolean sized) {

		if (bmp == null || bmp.isRecycled()) {
			Toast.makeText(
					activity,
					activity.getResources()
							.getString(R.string.warning_no_image),
					Toast.LENGTH_LONG).show();
			return;
		}

//		if (caesarApp == null) {
//			ShareTag.initTag(activity);
//		}

		if (!sized) {
			shareImage(activity, bmp);
			return;
		}
		AppPackages pack = new AppPackages();
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		if (width != height) {
			if (OtherApp.isInstalled(activity, pack.getInstasquarePackage())) {
				ShareToApp.shareImage(activity, pack.getInstasquarePackage(),
						"shareig", "", bmp);
			} else {
				Bitmap squareBitmap = null;
				try {
					int squareSize = width > height ? width : height;
					squareBitmap = Bitmap.createBitmap(squareSize, squareSize,
							Config.ARGB_8888);
					Canvas squareCanvas = new Canvas(squareBitmap);
					squareCanvas.drawColor(Color.WHITE);
					if (width > height) {
						squareCanvas.drawBitmap(bmp, 0, (width - height) / 2,
								new Paint());
					} else {
						squareCanvas.drawBitmap(bmp, (height - width) / 2, 0,
								new Paint());
					}

					String path = BitmapIoCache.putJPG(
							AppPackages.getAppName(activity.getPackageName()),
							squareBitmap);
					squareBitmap.isRecycled();
					squareBitmap = null;
					if (path == null)
						return;
					if (path.equals(""))
						return;

					File file = new File(path);
					Uri uri = Uri.fromFile(file);
					if (uri == null)
						return;
					ShareToApp.shareImageFromUri(activity,
							OtherAppPackages.instagramPackage, "shareig",
							ShareTag.getDefaultTag(activity), uri);

				} catch (Exception e) {
					if (squareBitmap != null) {
						squareBitmap.recycle();
						squareBitmap = null;
					}
				}
			}
		} else {
			ShareToApp.shareImage(activity, OtherAppPackages.instagramPackage,
					"shareig", ShareTag.getDefaultTag(activity), bmp);
		}
	}

}
