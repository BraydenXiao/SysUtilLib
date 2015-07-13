package org.aurona.lib.view.superimage;

import android.graphics.PointF;
import android.view.View;

public interface OnImageTransformChangedListener {
	public void scalePost(float scale);
	public void scalePost(float scale,PointF point,View view);
	public void translatePost(float dx, float dy);
}
