package org.aurona.lib.view.superimage;

import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Path.Direction;
import android.graphics.RectF;

public class UIPath {
	
	private enum pathType{NONE,ARC,CIRCLE,OVAL,ROUNDRECT,LINE}
	
	private String points = "";
	private String innerPoints = "";
	private String[] values;
	private String[] innerValues;
	private float[] floatValues;
	private float[] floatInnerValues;
	
	private int designViewWidth = -1;
	private int designViewHeight = -1;
	private int designViewInnerWidth = -1;
	private int designViewInnerHeight = -1;
	
	private boolean innered = false;
	private float innerPercent = 0.0f;
	private pathType type = pathType.NONE;
	private float mScaleX = 1.0f;
	private float mScaleY = 1.0f;
	
//	private UIPath(String pointsString){
//		points = pointsString;
//		init();
//	}
	
	public UIPath(String pointsString, int designWidth, int designHeight){
		points = pointsString;
		designViewWidth = designWidth;
		designViewHeight = designHeight;
		init();
	}
	
//	private UIPath(String pointsString, String innerPointsString){
//		points = pointsString;
//		innerPoints = innerPointsString;
//		innered = true;
//		init();
//		initInner();
//	}
	
	public UIPath(String pointsString, String innerPointsString, int designWidth, int designHeight, int designInnerWidth, int designInnerHeight ){
		points = pointsString;
		designViewWidth = designWidth;
		designViewHeight = designHeight;
		designViewInnerWidth = designInnerWidth;
		designViewInnerHeight = designInnerHeight;
		innerPoints = innerPointsString;
		init();
		initInner();
	}
	
	public void init(){
		if(points == null) return;
		if(points.equals("")) return;
		
		if(points.startsWith("A:")){
			type = pathType.ARC;
			points = points.replace("A:", "");
		}else if(points.startsWith("C:")){
			type = pathType.CIRCLE;
			points = points.replace("C:", "");
		}else if(points.startsWith("O:")){
			type = pathType.OVAL;
			points = points.replace("O:", "");
		}else if(points.startsWith("R:")){
			type = pathType.ROUNDRECT;
			points = points.replace("R:", "");
		}else if(points.startsWith("L:")){
			type = pathType.LINE;
			points = points.replace("L:", "");
		}
		
		points = points.replace("{", "");
		points = points.replace("}", "");
		values = points.split(",");
		
		floatValues = new float[values.length];

		for (int i = 0; i < values.length; i++) {
			float value = Float.parseFloat(values[i]);
			floatValues[i] = value;
		}
	}
	
	public void initInner(){
		if(innerPoints == null) return;
		if(innerPoints.equals("")) return;
		
		if(innerPoints.startsWith("A:")){
			innerPoints = innerPoints.replace("A:", "");
		}else if(innerPoints.startsWith("C:")){
			innerPoints = innerPoints.replace("C:", "");
		}else if(innerPoints.startsWith("O:")){
			innerPoints = innerPoints.replace("O:", "");
		}else if(innerPoints.startsWith("R:")){
			innerPoints = innerPoints.replace("R:", "");
		}else if(innerPoints.startsWith("L:")){
			innerPoints = innerPoints.replace("L:", "");
		}
		
		innerPoints = innerPoints.replace("{", "");
		innerPoints = innerPoints.replace("}", "");
		innerValues = innerPoints.split(",");
		
		floatInnerValues = new float[innerValues.length];

		for (int i = 0; i < innerValues.length; i++) {
			float innerValue = Float.parseFloat(innerValues[i]);
			floatInnerValues[i] = innerValue;
		}
	}
	
	
	
	public void setScale(float scale){
		mScaleX = scale;
		mScaleY = scale;
	}
	
	public void setScale(float scaleX,float scaleY){
		mScaleX = scaleX;
		mScaleY = scaleY;
	}
	
	public void setInnerPercent(float percent){
		innerPercent = percent;
	}
	

	

	
	public Path getTestPath(Rect rect){
		Path path = new Path();
		path.moveTo(rect.exactCenterX(), rect.top);
		
		path.lineTo(rect.left, rect.bottom);
		path.lineTo(rect.right, rect.bottom);
		path.lineTo(rect.exactCenterX(), rect.top);
		path.close();
		
		return path;
	}
	
	public Path getPath(Rect rect){
		Path path = new Path();
		if(rect == null) return path;
		
		if(points.equals("test")) return getTestPath(rect);
		
		Path rectPath = new Path();
		rectPath.addRect(new RectF(rect), Direction.CW);
		rectPath.close();
		if(values.length == 0){
			return rectPath;
		}
		
		int rectWidth = rect.right - rect.left;
		int rectHeight = rect.bottom - rect.top;
		
		float[] finalValues = floatValues.clone();
		if (innered) {
			if(floatValues.length != floatInnerValues.length) return rectPath;
			
			for (int i = 0; i < values.length; i++) {
				float value = floatValues[i];
				float innerValue = floatInnerValues[i];
				finalValues[i] = value - (value - innerValue) * innerPercent;
			}
		}
		
		
		try {
			if(type == pathType.ARC){
				
			}
			if(type == pathType.CIRCLE){
				if(values.length < 3) {
					return rectPath;
				}
				float x = finalValues[0]*mScaleX;
				float y = finalValues[1]*mScaleY;
				float radius = finalValues[2]*mScaleX;
				path.addCircle(x, y, radius, Direction.CW);
			}
			if(type == pathType.OVAL){
				if(values.length < 4) {
					return rectPath;
				}
				RectF rectF = new RectF();
				rectF.left = finalValues[0];
				rectF.top = finalValues[1];
				rectF.right = rectF.left + finalValues[2];
				rectF.bottom = rectF.top + finalValues[3];
				path.addOval(rectF, Direction.CW);
			}
			if(type == pathType.ROUNDRECT){
				if(values.length < 8) {
					return rectPath;
				}
				float[] radii = new float[8];
				radii[0] = finalValues[0]*mScaleX;
				radii[1] = finalValues[1]*mScaleY;
				radii[2] = finalValues[2]*mScaleX;
				radii[3] = finalValues[3]*mScaleY;
				radii[4] = finalValues[4]*mScaleX;
				radii[5] = finalValues[5]*mScaleY;
				radii[6] = finalValues[6]*mScaleX;
				radii[7] = finalValues[7]*mScaleY;
				RectF rectF = new RectF(rect);
				path.addRoundRect(rectF, radii, Direction.CW);
			}
			if(type == pathType.LINE){
				if(values.length < 4) {
					return rectPath;
				}
				
				int x = (int)(finalValues[0]*mScaleX + 0.9f) + rect.left;
				int y = (int)(finalValues[1]*mScaleY + 0.9f) + rect.top;
				if(designViewWidth != -1 && designViewHeight != -1){
					if(designViewInnerWidth != -1 && designViewInnerHeight != -1){
						float xo = floatValues[0] / designViewWidth;
						float xi = floatInnerValues[0] / designViewInnerWidth;
						x = (int) ((xo - (xo - xi) * innerPercent) * rectWidth + 0.4) + rect.left;
						float yo = floatValues[1] / designViewHeight;
						float yi = floatInnerValues[1] / designViewInnerHeight;
						y = (int) ((yo - (yo - yi) * innerPercent) * rectHeight + 0.4) + rect.top;
					}else{
						x = (int)(floatValues[0] / designViewWidth * rectWidth + 0.4) + rect.left;
						y = (int)(floatValues[1] / designViewHeight * rectHeight + 0.4) + rect.top;
					}
					
				}
				path.moveTo(x, y);
				
				for(int i = 2; i< finalValues.length-1; i += 2){
					int toX = (int)(finalValues[i]*mScaleX + 0.9f) + rect.left;
					int toY = (int)(finalValues[i + 1]*mScaleY + 0.9f) + rect.top;
					if(designViewWidth != -1 && designViewHeight != -1){
						if(designViewInnerWidth != -1 && designViewInnerHeight != -1){
							float xo = floatValues[i] / designViewWidth;
							float xi = floatInnerValues[i] / designViewInnerWidth;
							toX = (int) ((xo - (xo - xi) * innerPercent) * rectWidth + 0.4) + rect.left;
							float yo = floatValues[i + 1] / designViewHeight;
							float yi = floatInnerValues[i + 1] / designViewInnerHeight;
							toY = (int) ((yo - (yo - yi) * innerPercent) * rectHeight + 0.4) + rect.top;
						}else{
						    toX = (int)(floatValues[i] / designViewWidth * rectWidth + 0.4) + rect.left;
						    toY = (int)(floatValues[i + 1] / designViewHeight * rectHeight + 0.4) + rect.top;
						}
					}
					path.lineTo(toX, toY); 
				}
			}
		} catch (Exception e) {
			return rectPath;
		}
		
		path.close();

		return path;
	}

}
