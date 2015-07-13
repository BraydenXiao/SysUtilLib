package org.aurona.lib.bitmap.output.save;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import org.aurona.lib.io.DiskSpace;
import org.aurona.lib.packages.AppPackages;

import org.aurona.lib.sysutillib.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

public class SaveToSD {

	public static void saveImage(final Context context, Bitmap bitmap,
			final SaveDoneListener listener) {
		saveImage(context, bitmap, SaveDIR.DCIM, CompressFormat.JPEG, listener);
	}

	public static void saveImage(final Context context, Bitmap bitmap,
			SaveDIR dir, CompressFormat format, final SaveDoneListener listener) {
		if (context == null) {
			if (listener != null) {
				listener.onSavingException(new Exception("context is null"));
			}
			return;
		}

		String directory = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_DCIM).toString()
				+ "/camera";
		if (dir == SaveDIR.PICTURES) {
			directory = Environment.getExternalStoragePublicDirectory(
					Environment.DIRECTORY_PICTURES).toString();
		}
		if (dir == SaveDIR.SDROOT) {
			directory = Environment.getExternalStorageDirectory().getPath();
		}
		if (dir == SaveDIR.APPDIR) {
			String appName = AppPackages.getAppName(context.getPackageName());
			directory = Environment.getExternalStorageDirectory().getPath()
					+ "/" + appName;
		}
		if (dir == SaveDIR.PICTURESAPPDIR) {
			String appName = AppPackages.getAppName(context.getPackageName());
			directory = Environment.getExternalStoragePublicDirectory(
					Environment.DIRECTORY_PICTURES).toString()
					+ "/" + appName;
		}

		File file = new File(directory);
		if (!file.exists()) {
			file.mkdirs();
		}

		saveImage(context, bitmap, directory, format, listener);
	}

	public static void saveImage(final Context context, Bitmap bitmap,
			String saveDIR, CompressFormat format,
			final SaveDoneListener listener) {
		if (context == null) {
			if (listener != null) {
				listener.onSavingException(new Exception("context is null"));
			}
			return;
		}

		String appName = AppPackages.getAppName(context.getPackageName());

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		int yearNow = calendar.get(Calendar.YEAR);
		int monthNow = calendar.get(Calendar.MONTH) + 1;
		int dayNow = calendar.get(Calendar.DAY_OF_MONTH);
		int hourNow = calendar.get(Calendar.HOUR_OF_DAY);
		int minuteNow = calendar.get(Calendar.MINUTE);
		int secondNow = calendar.get(Calendar.SECOND);
		int millisecond = calendar.get(Calendar.MILLISECOND);
		// String detailName = Integer.toString(yearNow) + "_" +
		// Integer.toString(monthNow) + "_"
		// + Integer.toString(dayNow) + "_" + Integer.toString(hourNow) + "_"
		// + Integer.toString(minuteNow) + "_" + Integer.toString(secondNow) +
		// "_"
		// + Integer.toString(millisecond) + ".jpg";
		String detailName = Integer.toString(yearNow)
				+ Integer.toString(monthNow) + Integer.toString(dayNow)
				+ Integer.toString(hourNow) + Integer.toString(minuteNow)
				+ Integer.toString(secondNow) + Integer.toString(millisecond);
		String suffixName = ".jpg";
		if (format != null) {
			switch (format) {
			case JPEG:
				suffixName = ".jpg";
				break;
			case PNG:
				suffixName = ".png";
				break;
			case WEBP:
				suffixName = ".webp";
			}
		}
		detailName += suffixName;
		String fileName = appName + "_" + detailName;

		saveImage(context, bitmap, saveDIR, fileName, format, listener);
	}

	public static void saveImage(final Context context, final Bitmap bitmap,
			String saveDIR, String fileName, final CompressFormat format,
			final SaveDoneListener listener) {
		if (context == null) {
			if (listener != null) {
				listener.onSavingException(new Exception("context is null"));
			}
			return;
		}

		if (bitmap == null || bitmap.isRecycled()) {
			if (context != null) {
				Toast.makeText(
						context,
						context.getResources().getString(
								R.string.warning_no_image), Toast.LENGTH_LONG)
						.show();
			}
			if (listener != null) {
				listener.onSavingException(new Exception("bitmap is null"));
			}
			return;
		}
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			if (context != null) {
				Toast.makeText(
						context,
						context.getResources()
								.getString(R.string.warning_no_sd),
						Toast.LENGTH_LONG).show();
			}
			if (listener != null) {
				listener.onSavingException(new Exception("sd is null"));
			}
			return;
		}
		if (DiskSpace.sizeofSDCard() / 1024 < 10) {
			if (context != null) {
				Toast.makeText(
						context,
						context.getResources().getString(
								R.string.warning_no_sdmemory),
						Toast.LENGTH_LONG).show();
			}
			if (listener != null) {
				listener.onSavingException(new Exception("sd is full"));
			}
			return;
		}

		final String fileFullName = saveDIR + "/" + fileName;
		AsyncSaveToSdImpl.initLoader(context);

		
		AsyncSaveToSdImpl impl = AsyncSaveToSdImpl.getInstance();
		impl.setData(context, bitmap, fileFullName, format);
		impl.setOnSaveDoneListener(new SaveDoneListener(){

			@Override
			public void onSaveDone(String filePath, Uri fileUri) {
				AsyncSaveToSdImpl.shutdownLoder();
				
				if(listener != null){
					MediaScannerConnection.scanFile(
							context,
							new String[] { fileFullName },
							null,
							new MediaScannerConnection.OnScanCompletedListener() {
								@Override
								public void onScanCompleted(
										String path, Uri uri) {
									if (path == null || uri == null) {
									}
								}
							});
					
					listener.onSaveDone(filePath, fileUri);
				}
			}

			@Override
			public void onSavingException(Exception e) {
				AsyncSaveToSdImpl.shutdownLoder();
				if(listener != null){
					listener.onSavingException(e);
				}
			}
			
		});
		impl.execute();

	}
}
