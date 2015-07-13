package org.aurona.lib.view.superimage;

import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Paint.Style;
import android.graphics.Path.Direction;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class SuperImageView extends View{

	private Paint paint = new Paint();
	public SuperImageView(Context context) {
	    super(context);
		paint.setDither(true);
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		setWillNotDraw(false);
   }
	
	public SuperImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint.setDither(true);
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		setWillNotDraw(false);
	}
	
	private int viewWidth = 0;
	private int viewHeight = 0;
	private float viewRatio = 1.0f;
	private Rect viewRect = new Rect(0, 0, 0, 0);
	private Path viewPath = new Path();
	
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh){
		super.onSizeChanged(w, h, oldw, oldh);
		viewWidth = w;
		viewHeight = h;
		if(viewHeight > 0){
			viewRatio = (float)viewWidth/viewHeight;
		}
		viewRect.right = w;
		viewRect.bottom = h;
		viewPath.reset();
		viewPath.addRect(0, 0, w, h, Direction.CW);
		viewPath.close();
		if (backgroundImageDstRect.equals(new Rect(0, 0, 0, 0))) {
			backgroundImageDstRect = new Rect(0, 0, w, h);
		}
		measureImageRect();
		measureShapeRect();
		invalidate();
	}

	public void destroy(){
		if(backgroundPattern != null && !backgroundPattern.isRecycled()){
			backgroundPatternDrawable = null;
			backgroundPattern.recycle();
			backgroundPattern = null;
		}
		if(backgroundImage != null && !backgroundImage.isRecycled()){
			backgroundImage.recycle();
			backgroundImage = null;
		}
		if(shapeImage != null && !shapeImage.isRecycled()){
			shapeImage.recycle();
			shapeImage = null;
		}
	}

	
	
	
	
	private int viewAlpha = 255;
	public void setViewAlpha(int alpha){
		viewAlpha = alpha;
		invalidate();
	}
	
	private ColorFilter mColorFilter = null;
	public ColorFilter getColorFilter()
	{
		return mColorFilter;
	}
	public void setColorFilter(ColorFilter filter)
	{
		mColorFilter = filter;
	}
	
	private boolean mCanFingerScale = true;
	public boolean getCanFingerScale()
	{
		return mCanFingerScale;
	}
	
	public void setCanFingerScale(boolean value) {
		mCanFingerScale = value;
	}
	
	public enum BackgroundMode{BG_IS_NULL,BG_IS_COLOR,BG_IS_PATTERN,BG_IS_IMAGE,BG_IS_GRADIENT};
    private BackgroundMode bgMode = BackgroundMode.BG_IS_NULL;	
	public void setBackgroundMode(BackgroundMode mode){
		bgMode = mode;
		if (backgroundPattern != null && !backgroundPattern.isRecycled()) {
			backgroundPattern.recycle();
			backgroundPattern = null;
		}
		if (image != null && !image.isRecycled()) {
			image.recycle();
			image = null;
		}
	}
	public void drawBackgournd(Canvas canvas){
		if (bgMode == BackgroundMode.BG_IS_COLOR) {
			canvas.drawColor(backgroundColor);
		} else if (bgMode == BackgroundMode.BG_IS_PATTERN) {
			if (backgroundPattern == null || backgroundPattern.isRecycled()
					|| backgroundPatternDrawable == null) {
				return;
			}
			backgroundPatternDrawable.setBounds(0, 0, canvas.getWidth(),
					canvas.getHeight());
			backgroundPatternDrawable.draw(canvas);
		} else if (bgMode == BackgroundMode.BG_IS_IMAGE) {
			if (backgroundImage == null || backgroundImage.isRecycled()) {
				return;
			}
			canvas.drawBitmap(backgroundImage, null, new Rect(0, 0, canvas.getWidth(), canvas.getHeight()), paint);
		} else if (bgMode == BackgroundMode.BG_IS_GRADIENT) {
			if (backgroundGradientDrawable == null) {
				return;
			}
			backgroundGradientDrawable.setBounds(0, 0, canvas.getWidth(),
					canvas.getHeight());
			backgroundGradientDrawable.draw(canvas);
		}
	}
	
	private int backgroundColor = Color.WHITE;
	@Override
	public void setBackgroundColor(int color){
		backgroundColor = color;
		setBackgroundMode(BackgroundMode.BG_IS_COLOR);
		invalidate();
	}
	
	private Bitmap backgroundPattern = null;
	private BitmapDrawable backgroundPatternDrawable = null;
	public void setBackgroundPattern(Bitmap pattern){
		if (pattern == null || pattern.isRecycled()) {
			return;
		}  
		setBackgroundMode(BackgroundMode.BG_IS_PATTERN);
		backgroundPattern = pattern;
		backgroundPatternDrawable = new BitmapDrawable(getResources(),backgroundPattern);
		backgroundPatternDrawable.setTileModeXY(TileMode.REPEAT , TileMode.REPEAT );  
		backgroundPatternDrawable.setDither(true);
		invalidate();
	}
	
	private Bitmap backgroundImage = null;
	private Rect backgroundImageDstRect = new Rect(0, 0, 0, 0);
	public void setBackgroundImage(Bitmap bmp){
		setBackgroundImage(bmp, null);
	}
	public void setBackgroundImage(Bitmap bmp, Rect dstRect){
		if (bmp == null || bmp.isRecycled()) {
			return;
		}
		setBackgroundMode(BackgroundMode.BG_IS_IMAGE);
		
		backgroundImage = bmp;
		if(dstRect != null){
			if (!dstRect.equals(new Rect(0, 0, 0, 0))) {
				backgroundImageDstRect = dstRect;
			}
		}
		invalidate();
	}
	public Bitmap getBackgroundImage(){
		return backgroundImage;
	}
	
	private GradientDrawable backgroundGradientDrawable = null;
	public void setBackgroundGradient(GradientDrawable gradient){
		backgroundGradientDrawable = gradient;
		setBackgroundMode(BackgroundMode.BG_IS_GRADIENT);
		invalidate();
	}
	
	
	
	
	
	
	private Bitmap image = null;
	private int imgWidth = 0;
	private int imgHeight = 0;
	private float imgRatio = 1.0f;
	private int imgSrcRectWidth = 0;
	private int imgSrcRectHeight = 0;
	
	private float imgScale = 1.0f;
	private int scaleImgSrcRectWidth = 0;
	private int scaleImgSrcRectHeight = 0;
	private Rect imgSrcRect = new Rect(0, 0, 0, 0);
 	public void setImageBitmap(Bitmap bmp){
		image = bmp;
		if (bmp != null && !bmp.isRecycled()) {
			imgWidth = image.getWidth();
			imgHeight = image.getHeight();
			measureImageRect();
		}

		invalidate();
	}
 	public int getImageWidth(){
 		return imgWidth;
 	}
	private void measureImageRect(){	
		if(imgWidth == 0 || imgHeight == 0 || viewWidth == 0 || viewHeight == 0) return;
		imgSrcRectWidth = imgWidth;
		imgSrcRectHeight = imgHeight;

		imgSrcRect.left = 0;
		imgSrcRect.top = 0;
		imgSrcRect.right = imgWidth;
		imgSrcRect.bottom = imgHeight;
		
		
		imgRatio = (float)imgWidth/imgHeight;
		
		if(imgRatio == viewRatio){
			imgScale = (float)imgWidth/viewWidth;
		}
		if (imgRatio > viewRatio) {
			imgScale = (float) imgHeight / viewHeight;

			imgSrcRectWidth = (int) (viewWidth * imgScale);
			imgSrcRect.left = (imgWidth - imgSrcRectWidth) / 2;
			imgSrcRect.top = 0;
			imgSrcRect.right = imgSrcRect.left + imgSrcRectWidth;
			imgSrcRect.bottom = imgHeight;
		}
		if (imgRatio < viewRatio) {
			imgScale = (float) imgWidth / viewWidth;

			imgSrcRectHeight = (int) (viewHeight * imgScale);
			imgSrcRect.left = 0;
			imgSrcRect.top = (imgHeight - imgSrcRectHeight) / 2;
			imgSrcRect.right = imgWidth;
			imgSrcRect.bottom = imgSrcRect.top + imgSrcRectHeight;
		}

		scaleImgSrcRectWidth = imgSrcRectWidth;
		scaleImgSrcRectHeight = imgSrcRectHeight;
	}
	public void setImageBitmapKeepState(Bitmap bmp){
		if (bmp == null || bmp.isRecycled()) {
			return;
		}

		image = bmp;
		invalidate();
	}
	
	private boolean imgScrollable = false;
	public void setImageScrollable(boolean scrollable){
		imgScrollable = scrollable;
	}
	
	private boolean horizintal = false;
	private boolean vertical = false;
	public void setImageMirror(boolean horizintalFlip, boolean verticalFlip){
		horizintal = horizintalFlip;
		vertical = verticalFlip;
		invalidate();
	}
	public void setImageMirrorHorizintal(){
		horizintal = !horizintal;
		invalidate();
	}
	public void setImageMirrorVertical(){
		vertical = !vertical;
		invalidate();
	}

	public boolean getImageMirrorHorizintal(){
		return horizintal;
	}
	public boolean getImageMirrorVertical(){
		return vertical;
	}

	public Rect getImageRect(){
		return imgSrcRect;
	}
	
	public enum ShapeMode{SP_IS_NULL,SP_IS_PATH,SP_IS_IMAGE};
    private ShapeMode spMode = ShapeMode.SP_IS_NULL;	
	public void setShapeMode(ShapeMode mode){
		spMode = mode;
		if (shapeImage != null && !shapeImage.isRecycled()) {
			shapeImage.recycle();
			shapeImage = null;
		}
	}
	
	private Bitmap shapeImage = null;
	private int shapeWidth = 0;
	private int shapeHeight = 0;
	private float shapeRatio = 1.0f;
	
	private float shapeScale = 1.0f;
	private boolean shapeSmaller = false;
	private boolean shapeOuterSolid = true;
	private Rect shapeDstRect = new Rect(0, 0, 0, 0);
	public void setShapeImage(Bitmap shape, boolean outerSolid){
		if (shape == null || shape.isRecycled()) {
			return;
		}
		setShapeMode(ShapeMode.SP_IS_IMAGE);
		shapeImage = shape;
		shapeWidth = shapeImage.getWidth();
		shapeHeight = shapeImage.getHeight();
		shapeOuterSolid = outerSolid;
		measureShapeRect();
		invalidate();
	}
	private void measureShapeImageRect(){
		if(shapeWidth == 0 || shapeHeight == 0 || viewWidth == 0 || viewHeight == 0) return;
		
		shapeDstRect.left = 0;
		shapeDstRect.top = 0;
		shapeDstRect.right = viewWidth;
		shapeDstRect.bottom = viewHeight;
		shapeRatio = (float)shapeWidth/shapeHeight;
		
		if(shapeRatio == viewRatio){
			shapeScale = (float)shapeWidth/viewWidth;
		}else if (shapeRatio > viewRatio) {
			shapeScale = (float)shapeWidth/viewWidth;
			int newShapeHeight = (int)(shapeHeight * (float)viewWidth/shapeWidth);
			if(newShapeHeight < 1) shapeHeight = 1;
			if(Math.abs(newShapeHeight - viewHeight) < 2) newShapeHeight = viewHeight;
			
			shapeDstRect.left = 0;
			shapeDstRect.top = (int)(viewHeight - newShapeHeight) / 2;
			shapeDstRect.right = viewWidth;
			shapeDstRect.bottom = shapeDstRect.top + newShapeHeight;
		} else {
			shapeScale = (float)shapeHeight/viewHeight;
			int newShapeWidth = (int)(shapeWidth * (float)viewHeight/shapeHeight);
			if(newShapeWidth < 1) newShapeWidth = 1;
			if(Math.abs(newShapeWidth - viewWidth) < 2) newShapeWidth = viewWidth;
			
			shapeDstRect.left = (int)(viewWidth - newShapeWidth) / 2;
			shapeDstRect.top = 0;
			shapeDstRect.right = shapeDstRect.left + (int)newShapeWidth;
			shapeDstRect.bottom = viewHeight;
		}
		
		if(shapeDstRect.right < viewWidth || shapeDstRect.bottom < viewHeight){
			shapeSmaller = true;
		}
	}
	public Bitmap getShapeImage(){
		return shapeImage;
	}
	
	
	private UIPath uiPath = null;
	private Path shapePath = null;
	private Region shapeRegion = null;
	public UIPath getShapeUIPath(){
		return uiPath;
	}
	
	public void setShapePath(UIPath path, boolean outerSolid){
		if(path == null) return;
		setShapeMode(ShapeMode.SP_IS_PATH);
		uiPath = path;
		shapeOuterSolid = outerSolid;
		measureShapeRect();
		invalidate();
	}
	private void measureShapePathRect() {
		if(uiPath == null) return;
		if(viewWidth == 0 || viewHeight == 0) return;
		shapePath = uiPath.getPath(viewRect);
		RectF rectF = new RectF();
		shapePath.computeBounds(rectF, true);
		shapeRegion = new Region();
		shapeRegion.setPath(shapePath, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
	}

	private void measureShapeRect() {
		if(spMode == ShapeMode.SP_IS_IMAGE){
			measureShapeImageRect();
		}else if(spMode == ShapeMode.SP_IS_PATH){
			measureShapePathRect();
		}
	}
	
	private Boolean inverse = false;
	public void setInverse(Boolean ivs){
		inverse = ivs;
		invalidate();
	}

	
	
	
	private PathEffect pathEffect = null;
	public void setCornerPathEffect(float degree){
		pathEffect = new CornerPathEffect(degree);
		invalidate();
	}
	
	
	
	private Rect clear1 = new Rect();
	private Rect clear2 = new Rect();
	private PorterDuffXfermode clearXfermode = new PorterDuffXfermode(Mode.CLEAR);
	private PorterDuffXfermode dstInXfermode = new PorterDuffXfermode(Mode.DST_IN);
	private PorterDuffXfermode xorXfermode = new PorterDuffXfermode(Mode.XOR);
	private PorterDuffXfermode srcInXfermode = new PorterDuffXfermode(Mode.SRC_IN);
//	private BlurMaskFilter blurMaskFilter = new BlurMaskFilter(15, Blur.OUTER);
	@Override
	protected void onDraw(Canvas canvas){		
		//1 save layer
        int sc = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null,Canvas.ALL_SAVE_FLAG);

        //2 initlize paint alpha and effect
		paint.setAlpha(viewAlpha);
		if(pathEffect != null) paint.setPathEffect(pathEffect);
		
		//3 if shape is path, then draw path first
		if(spMode == ShapeMode.SP_IS_PATH){
			if (shapePath == null) {
				return;
			}
			paint.setStyle(Style.FILL);
//			paint.setColor(Color.BLACK);
//			paint.setMaskFilter(blurMaskFilter);
//			canvas.drawPath(shapePath, paint);
//			paint.setMaskFilter(null);
//			sc += canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null,Canvas.ALL_SAVE_FLAG);
			canvas.drawPath(shapePath, paint);
			paint.setPathEffect(null);
			if((shapeOuterSolid && !inverse) || (!shapeOuterSolid && inverse)){
				paint.setXfermode(xorXfermode);
			}else{
				paint.setXfermode(srcInXfermode);
			}
		}else{
			if (pathEffect != null) {
				paint.setStyle(Style.FILL);
				canvas.drawPath(viewPath, paint);
				paint.setXfermode(srcInXfermode);
			}
		}

		//4 draw background
		if(bgMode == BackgroundMode.BG_IS_COLOR){
			canvas.drawColor(backgroundColor);
		}else if(bgMode == BackgroundMode.BG_IS_PATTERN){
			if (backgroundPattern == null || backgroundPattern.isRecycled() || backgroundPatternDrawable == null) {
				return;
			}  
			backgroundPatternDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
			backgroundPatternDrawable.draw(canvas);
		}else if(bgMode == BackgroundMode.BG_IS_IMAGE){
			if(backgroundImage == null || backgroundImage.isRecycled()){
				return;
			}
			canvas.drawBitmap(backgroundImage, null, backgroundImageDstRect, paint);
			sc += canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null,Canvas.ALL_SAVE_FLAG);
		}else if(bgMode == BackgroundMode.BG_IS_GRADIENT){
			if(backgroundGradientDrawable == null){
				return;
			}
			backgroundGradientDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
			backgroundGradientDrawable.draw(canvas);
		}
		
		//5 draw image
		if(image != null && !image.isRecycled()){

			if(horizintal){
		        canvas.scale(-1, 1, (float)viewWidth / 2.0f, (float)viewHeight / 2.0f); 
			}
			if(vertical){
				canvas.scale(1, -1, (float)viewWidth / 2.0f, (float)viewHeight / 2.0f);
			}

			paint.setColorFilter(mColorFilter);
			
			canvas.drawBitmap(image, imgSrcRect, viewRect, paint);
			paint.setColorFilter(null);

			if(vertical){
				canvas.scale(1, -1, (float)viewWidth / 2.0f, (float)viewHeight / 2.0f);
			}
			if(horizintal){
		        canvas.scale(-1, 1, (float)viewWidth / 2.0f, (float)viewHeight / 2.0f); 
			}
		}

		//6 if without background and image, then draw white color
		if(spMode != ShapeMode.SP_IS_NULL && bgMode == BackgroundMode.BG_IS_NULL){
			if(image == null){
			  canvas.drawColor(Color.WHITE);
			}else{
			   if(image.isRecycled())
				  canvas.drawColor(Color.WHITE);
			}
		}

		//7 if shape is image, then draw image
		if(spMode == ShapeMode.SP_IS_IMAGE){
			if (shapeImage == null || shapeImage.isRecycled()) {
				return;
			}

			if(shapeSmaller){
				if((shapeOuterSolid && inverse) || (!shapeOuterSolid && !inverse)){
				   paint.setXfermode(clearXfermode);
					paint.setStyle(Style.FILL);
				    clear1.set(viewRect);
					clear2.set(viewRect);
					if(viewWidth > shapeDstRect.right){
						clear1.right = shapeDstRect.left;
						clear2.left = shapeDstRect.right;
					}else{
						clear1.bottom = shapeDstRect.top;
						clear2.top = shapeDstRect.bottom;
					}
					canvas.drawRect(clear1, paint);
					canvas.drawRect(clear2, paint);
				}
			}
			
			if(inverse || shapeOuterSolid){
				paint.setXfermode(xorXfermode);
			}else{
				paint.setXfermode(dstInXfermode);
			}
//			BlurMaskFilter bf = new BlurMaskFilter(20,BlurMaskFilter.Blur.OUTER);
//			paint.setColor(Color.GRAY);
//			paint.setMaskFilter(bf);
//			canvas.drawBitmap(shapeImage.extractAlpha(paint, null), null, new Rect(shapeDstRect.left+10, shapeDstRect.top+10, shapeDstRect.right+10, shapeDstRect.bottom+10), paint);
//			paint.setMaskFilter(null);
			canvas.drawBitmap(shapeImage, null, shapeDstRect, paint);
		}
		

		//8 after all drawing, reset paint's xfermode
		paint.setXfermode(null);
		
		//9 if this has been selected, then draw border
		if(touching && drawTouchingFrame){
			if(pathEffect != null) paint.setPathEffect(pathEffect);
			paint.setColor(touchingColor);
			paint.setStyle(Style.STROKE);
//			paint.setShadowLayer(10, 0, 0, Color.BLACK);
			paint.setStrokeWidth(2);
			
			if(spMode == ShapeMode.SP_IS_PATH && shapePath != null){
				canvas.drawPath(shapePath, paint);
			}else{
				canvas.drawPath(viewPath, paint);
			}

			paint.setStrokeWidth(1);
			paint.setColor(backgroundColor);
		}
		
		if(waiting){
			canvas.drawColor(Color.argb(100, 255, 255, 255), Mode.LIGHTEN);
		}
		
		//10 reset paint's patheffect
		paint.setPathEffect(null);
		
		//11 finally, restore canvas
        canvas.restoreToCount(sc);
	}

	public void drawForShapeImage(Canvas canvas, Rect drawInRect){
		//1 save layer
        int sc = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null,Canvas.ALL_SAVE_FLAG);

        //2 initlize paint alpha and effect
		paint.setAlpha(viewAlpha);

		
		//4 draw background
		if(bgMode == BackgroundMode.BG_IS_COLOR){
			paint.setStyle(Style.FILL);
			paint.setColor(backgroundColor);
			canvas.drawRect(drawInRect, paint);
		}else if(bgMode == BackgroundMode.BG_IS_PATTERN){
			if (backgroundPattern == null || backgroundPattern.isRecycled() || backgroundPatternDrawable == null) {
				return;
			}  
			backgroundPatternDrawable.setBounds(drawInRect);
			backgroundPatternDrawable.draw(canvas);
		}else if(bgMode == BackgroundMode.BG_IS_IMAGE){
			if(backgroundImage == null || backgroundImage.isRecycled()){
				return;
			}
			canvas.drawBitmap(backgroundImage, null, backgroundImageDstRect, paint);
			sc += canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null,Canvas.ALL_SAVE_FLAG);
		}else if(bgMode == BackgroundMode.BG_IS_GRADIENT){
			if(backgroundGradientDrawable == null){
				return;
			}
			backgroundGradientDrawable.setBounds(drawInRect);
			backgroundGradientDrawable.draw(canvas);
		}
		
		int drawWidth = drawInRect.right - drawInRect.left;
		int drawHeight = drawInRect.bottom - drawInRect.top;
		float drawInRectRatio = (float)drawWidth/drawHeight;
		
		//5 draw image
		if(image != null && !image.isRecycled()){

			PointF drawInRectCenter = new PointF();
			drawInRectCenter.x = drawInRect.left + (drawWidth/2.0f);
			drawInRectCenter.y = drawInRect.top + (drawHeight/2.0f);
			
			if(horizintal){
		        canvas.scale(-1, 1, drawInRectCenter.x, drawInRectCenter.y); 
			}
			if(vertical){
				canvas.scale(1, -1, drawInRectCenter.x, drawInRectCenter.y);
			}

			canvas.drawBitmap(image, imgSrcRect, drawInRect, paint);

			if(vertical){
				canvas.scale(1, -1, drawInRectCenter.x, drawInRectCenter.y);
			}
			if(horizintal){
		        canvas.scale(-1, 1, drawInRectCenter.x, drawInRectCenter.y); 
			}
		}

		//6 if without background and image, then draw white color
		if(spMode != ShapeMode.SP_IS_NULL && bgMode == BackgroundMode.BG_IS_NULL){
			if(image == null){
				paint.setStyle(Style.FILL);
				paint.setColor(Color.WHITE);
				canvas.drawRect(drawInRect, paint);
			}else{
			   if(image.isRecycled()){
					paint.setStyle(Style.FILL);
				    paint.setColor(Color.WHITE);
				    canvas.drawRect(drawInRect, paint);
			   }
			}
		}

		//7 if shape is image, then draw image
		if(spMode == ShapeMode.SP_IS_IMAGE){
			if (shapeImage == null || shapeImage.isRecycled()) {
				return;
			}
			

			Rect drawShapeDstRect = new Rect();
			
			if(shapeRatio == drawInRectRatio){
				drawShapeDstRect = drawInRect;
			}else if (shapeRatio > drawInRectRatio) {
				int newShapeHeight = (int)(shapeHeight * (float)drawWidth/shapeWidth);
				if(newShapeHeight < 1) shapeHeight = 1;
				if(Math.abs(newShapeHeight - drawHeight) < 2) newShapeHeight = drawHeight;
				
				drawShapeDstRect.left = drawInRect.left-1;
				drawShapeDstRect.top = (int)(drawHeight - newShapeHeight) / 2 + drawInRect.top-1;
				drawShapeDstRect.right = drawShapeDstRect.left + drawWidth +2;
				drawShapeDstRect.bottom = drawShapeDstRect.top + newShapeHeight+2;
			} else {
				int newShapeWidth = (int)(shapeWidth * (float)drawHeight/shapeHeight);
				if(newShapeWidth < 1) newShapeWidth = 1;
				if(Math.abs(newShapeWidth - drawWidth) < 2) newShapeWidth = drawWidth;
				
				drawShapeDstRect.left = (int)(drawWidth - newShapeWidth) / 2 +  drawInRect.left-1;
				drawShapeDstRect.top = drawInRect.top-1;
				drawShapeDstRect.right = drawShapeDstRect.left + newShapeWidth+2;
				drawShapeDstRect.bottom = drawShapeDstRect.top + drawHeight+2;
			}

			if(shapeSmaller){
				if((shapeOuterSolid && inverse) || (!shapeOuterSolid && !inverse)){
				   paint.setXfermode(clearXfermode);
				   
				    clear1.set(drawInRect);
					clear2.set(drawInRect);
					
					if(drawWidth > drawShapeDstRect.right){
						clear1.right = drawShapeDstRect.left;
						clear2.left = drawShapeDstRect.right;
					}else{
						clear1.bottom = drawShapeDstRect.top;
						clear2.top = drawShapeDstRect.bottom;
					}
					canvas.drawRect(clear1, paint);
					canvas.drawRect(clear2, paint);
				}
			}
			
			if(inverse || shapeOuterSolid){
				paint.setXfermode(xorXfermode);
			}else{
				paint.setXfermode(dstInXfermode);
			}
			canvas.drawBitmap(shapeImage, null, drawShapeDstRect, paint);
		}
		

		//8 after all drawing, reset paint's xfermode
		paint.setXfermode(null);

		//9 reset paint's patheffect
		paint.setPathEffect(null);
		
		//10 finally, restore canvas
        canvas.restoreToCount(sc);
	}

	
	
	
	
	
	
	
	
	
	private OnSuperImageViewTouchedListener touchedListener;
	public void setViewTouchedListener(OnSuperImageViewTouchedListener listener){
		touchedListener = listener;
	}
	private boolean touching = false;
	public void setTouchingState(boolean selected){
		this.touching = selected;
		invalidate();
	}
	public boolean getTouchingState(){
		return this.touching;
	}
	
	private boolean drawTouchingFrame = true;
	public void setDrawTouchingFrame(boolean isDraw){
		this.drawTouchingFrame = isDraw;
	}
	public boolean getDrawTouchingFrame(){
		return this.drawTouchingFrame;
	}
	
	private int touchingColor = Color.rgb(0, 200, 0);
	public void setTouchingColor(int color){
		touchingColor = color;
	}
	private boolean waiting = false;
	public void setWaitingState(boolean waiting){
		this.waiting = waiting;
		if(waiting){
		    this.touching = false;
		}else{
			this.touching = true;
		}
		invalidate();
	}
	
	
	private OnImageTransformChangedListener transformListener;
	public void setTransformedListener(OnImageTransformChangedListener listener){
		transformListener = listener;
	}
	
	protected static final int NONE = 0; 
	protected static final int DRAG = 1;
	protected static final int JUMP = 2;
	protected static final int ZOOM = 3; 
    protected int mode = NONE; 
    protected int moveCount = 0;
	protected PointF mStart= new PointF();
    protected PointF mMid = new PointF();
	protected PointF mCurPoint= new PointF();
    protected float oldDist;
    protected float oldDegree;
    protected Date touchDate = new Date();
    protected long touchDownTime = 0;
    protected boolean isMove = false;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (image == null || !imgScrollable)
			return false;
		if (image.isRecycled())
			return false;

		mCurPoint.set(event.getX(), event.getY());
		if (spMode == ShapeMode.SP_IS_IMAGE && mode == NONE) {
			if (shapeImage != null && !shapeImage.isRecycled()) {
				if (shapeDstRect.contains((int) mCurPoint.x, (int) mCurPoint.y)) {
					try {
						int shapePointX = (int) ((mCurPoint.x - shapeDstRect.left) * shapeScale);
						int shapePointY = (int) ((mCurPoint.y - shapeDstRect.top) * shapeScale);
						if (shapePointX < 0)
							shapePointX = 0;
						if (shapePointY < 0)
							shapePointY = 0;
						int pix = shapeImage.getPixel(shapePointX, shapePointY);
						if ((pix == 0 && !inverse) || (pix != 0 && inverse)) {
							setTouchingState(false);
//							if(touchedListener != null){
//								touchedListener.onTouched(false, this);
//							}
							return false;
						}
					} catch (Exception e) {
						System.out.println("error:" + e.getMessage());
					}
				}else{
					if(shapeSmaller){
						if((shapeOuterSolid && inverse) || (!shapeOuterSolid && !inverse)){
							setTouchingState(false);
//							if(touchedListener != null){
//								touchedListener.onTouched(false, this);
//							}
							return false;
						}
						
					}
				}
			}
		} else if (spMode == ShapeMode.SP_IS_PATH && mode == NONE) {
			boolean shapeContains = shapeRegion.contains((int) mCurPoint.x, (int) mCurPoint.y);
			if(shapeContains){
				if((shapeOuterSolid && !inverse) || (!shapeOuterSolid && inverse)){
					setTouchingState(false);
//					if(touchedListener != null){
//						touchedListener.onTouched(false, this);
//					}
					return false;
				}
			}else{
				if((shapeOuterSolid && inverse) || (!shapeOuterSolid && !inverse)){
					setTouchingState(false);
//					if(touchedListener != null){
//						touchedListener.onTouched(false, this);
//					}
					return false;
				}
			}
		}
		
		if(touchedListener != null){
			touchedListener.onTouching(false, this);
		}
	
		try {
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				mode = DRAG;
				touchDownTime = System.currentTimeMillis();
				mStart.set(mCurPoint.x, mCurPoint.y);
				isMove = false;
				break;
			case MotionEvent.ACTION_MOVE:
				float dx = mCurPoint.x - mStart.x;
				float dy = mCurPoint.y - mStart.y;
				if (mode == DRAG) {

					if(transformListener != null){
					    transformListener.translatePost(dx, dy);
					}else{
						postTranslate(dx, dy);
					}
					mStart.set(mCurPoint.x, mCurPoint.y);
				}
				if (mode == JUMP) {
					mode = DRAG;
					mStart.set(mCurPoint.x, mCurPoint.y);
				}
				if (mode == ZOOM) {
					moveCount++;
					float newDist = (float) spacing(event);
					if(mCanFingerScale)
					{
						midPoint(mMid, event);
						if (moveCount > 10) {
						
							float scale = newDist / oldDist;
							if (transformListener != null) {
								transformListener.scalePost(scale);
								transformListener.scalePost(scale, mMid, this);
							} else {
								postScale(scale);
							}
						}
					}
					oldDist = newDist;
				}
				if(Math.abs(dx) > 10   
	                    || Math.abs(dy) > 10)
				{
					isMove = true;
				}
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
	        	int idx = event.getActionIndex();
				if (idx >= 1) {
					oldDist = (float) spacing(event);
					if (oldDist > 10f) {
						mode = ZOOM;
						moveCount = 0;
					}
					if(mCanFingerScale)
						midPoint(mMid, event);
				}
				break;
			case MotionEvent.ACTION_POINTER_UP:
				mode = JUMP;
				break;
			case MotionEvent.ACTION_UP:
				mode = NONE;
				long touchUpTime = System.currentTimeMillis();
				if(touchUpTime - touchDownTime < 200){
					if(touchedListener != null){
						touchedListener.onTouching(true, this);
					}
				}
				if(!isMove)
				{
					if(!this.touching)
						setTouchingState(true);
					else {
						setTouchingState(false);
					}
				}
				isMove = false;
				break;
			}
		} catch (Exception e) {
//			System.out.println("error:" + e.getMessage());
			e.printStackTrace();
		}
		return true;
	}

	private double spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return Math.sqrt((double)(x * x + y * y));
	}

	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}
	
	public void postTranslate(float dx, float dy){
		if(horizintal) {
			dx = -dx;
		}
		if(vertical){
			dy = -dy;
		}
		imgSrcRect.left -= dx*imgScale;
		imgSrcRect.top -= dy*imgScale;
		
		fitImageRect();
		invalidate();
	}
	
	public void postScale(float scale) {
		if(mMid.x == 0 && mMid.y == 0){
			mMid.x = viewWidth/2;
			mMid.y = viewHeight/2;
		}
		
		float imgScaleMidX = mMid.x * imgScale + imgSrcRect.left;
		float imgScaleMidY = mMid.y * imgScale + imgSrcRect.top;
		
		int scaledHeight = (int)(scaleImgSrcRectHeight/scale);
		scaleImgSrcRectWidth = (int)(scaledHeight*viewRatio);
		scaleImgSrcRectHeight = scaledHeight;
		
		if(scaleImgSrcRectWidth > imgSrcRectWidth ||
		   scaleImgSrcRectHeight > imgSrcRectHeight){
			scaleImgSrcRectWidth = imgSrcRectWidth;
			scaleImgSrcRectHeight = imgSrcRectHeight;
		}

		if(imgRatio == viewRatio){
			imgScale = (float)scaleImgSrcRectWidth / viewWidth;
		}else if (imgRatio > viewRatio) {
			imgScale = (float)scaleImgSrcRectHeight / viewHeight;
		}else if (imgRatio < viewRatio) {
			imgScale = (float)scaleImgSrcRectWidth / viewWidth;
		}
		
		imgSrcRect.left = (int) (imgScaleMidX - mMid.x * imgScale);
		imgSrcRect.top = (int) (imgScaleMidY - mMid.y * imgScale);
		
		fitImageRect();
		invalidate();
	}
	
	public void postScale(float scale,PointF pointF,View view) {
		
		mMid.x = (float)view.getWidth() / getWidth() * pointF.x;
		mMid.y = (float)view.getHeight() / getHeight() * pointF.y;
		
		if(mMid.x == 0 && mMid.y == 0){
			mMid.x = viewWidth/2;
			mMid.y = viewHeight/2;
		}
		
		float imgScaleMidX = mMid.x * imgScale + imgSrcRect.left;
		float imgScaleMidY = mMid.y * imgScale + imgSrcRect.top;
		
		int scaledHeight = (int)(scaleImgSrcRectHeight/scale);
		scaleImgSrcRectWidth = (int)(scaledHeight*viewRatio);
		scaleImgSrcRectHeight = scaledHeight;
		
		if(scaleImgSrcRectWidth > imgSrcRectWidth ||
		   scaleImgSrcRectHeight > imgSrcRectHeight){
			scaleImgSrcRectWidth = imgSrcRectWidth;
			scaleImgSrcRectHeight = imgSrcRectHeight;
		}

		if(imgRatio == viewRatio){
			imgScale = (float)scaleImgSrcRectWidth / viewWidth;
		}else if (imgRatio > viewRatio) {
			imgScale = (float)scaleImgSrcRectHeight / viewHeight;
		}else if (imgRatio < viewRatio) {
			imgScale = (float)scaleImgSrcRectWidth / viewWidth;
		}
		
		imgSrcRect.left = (int) (imgScaleMidX - mMid.x * imgScale);
		imgSrcRect.top = (int) (imgScaleMidY - mMid.y * imgScale);
		
		fitImageRect();
		invalidate();
	}
	
	private void fitImageRect(){
		if(imgSrcRect.left < 0){
			imgSrcRect.left = 0;
		}
	
		if(imgSrcRect.top < 0){
			imgSrcRect.top = 0;
		}

		imgSrcRect.right = imgSrcRect.left+scaleImgSrcRectWidth;
		imgSrcRect.bottom = imgSrcRect.top+scaleImgSrcRectHeight;
		
		
		if(imgSrcRect.right > imgWidth){
			imgSrcRect.right = imgWidth;
			imgSrcRect.left = imgWidth - scaleImgSrcRectWidth;
		}
		if(imgSrcRect.bottom > imgHeight){
			imgSrcRect.bottom = imgHeight;
			imgSrcRect.top = imgHeight - scaleImgSrcRectHeight;
		}
	}

	

}
