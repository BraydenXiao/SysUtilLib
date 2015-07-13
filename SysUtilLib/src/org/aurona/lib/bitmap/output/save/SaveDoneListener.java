package org.aurona.lib.bitmap.output.save;

import android.net.Uri;

public interface SaveDoneListener {
	public void onSaveDone(String filePath, Uri fileUri);
	public void onSavingException(Exception e);
}
