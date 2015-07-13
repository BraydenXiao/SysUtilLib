package org.aurona.lib.io;

import java.io.File;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

public class DiskSpace {
	public static long sizeofSDCard() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File sdcardDir = Environment.getExternalStorageDirectory();
			StatFs sf = new StatFs(sdcardDir.getPath());
			if (Build.VERSION.SDK_INT >= 18) {
				return getAvaiableKb(sf);
			} else {
				return getAvaiableKb_d(sf);
			}
		}
		return 0;
	}

	public static long sizeofSystem() {
		File root = Environment.getRootDirectory();
		StatFs sf = new StatFs(root.getPath());
		if (Build.VERSION.SDK_INT >= 18) {
			return getAvaiableKb(sf);
		} else {
			return getAvaiableKb_d(sf);
		}

	}

	@TargetApi(18)
	private static long getAvaiableKb(StatFs sf) {
		long blockSize = sf.getBlockSizeLong();
		long availCount = sf.getAvailableBlocksLong();
		return availCount * blockSize / 1024;
	}

	private static long getAvaiableKb_d(StatFs sf) {
		long blockSize = sf.getBlockSize();
		long blockCount = sf.getBlockCount();
		long availCount = sf.getAvailableBlocks();
		return availCount * blockSize / 1024;
	}
}
