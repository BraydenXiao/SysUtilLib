package org.aurona.lib.view.redraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public abstract class ReDrawView extends View {
	
	protected Paint mPaint = new Paint();
	protected PorterDuffXfermode clearXfermode = new PorterDuffXfermode(Mode.CLEAR);
	protected PorterDuffXfermode dstInXfermode = new PorterDuffXfermode(Mode.DST_IN);
	protected PorterDuffXfermode xorXfermode = new PorterDuffXfermode(Mode.XOR);
	protected PorterDuffXfermode srcInXfermode = new PorterDuffXfermode(Mode.SRC_IN);
	protected PorterDuffXfermode dstOutXfermode = new PorterDuffXfermode(Mode.DST_OUT);
	protected PorterDuffXfermode srcOutXfermode = new PorterDuffXfermode(Mode.SRC_OUT);
	
	public ReDrawView(Context context) {
		super(context);
		init();
	}

	public ReDrawView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ReDrawView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init(){
		mPaint.setDither(true);
		mPaint.setAntiAlias(true);
		mPaint.setFilterBitmap(true);
		setWillNotDraw(false);
	}
	
	protected int canvasWidth = 0;
	protected int canvasHeight = 0;
	protected Rect canvasRect = new Rect();
	
//	private PaintFlagsDrawFilter pdf=new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
	@Override
	protected void onDraw(Canvas canvas) {
//		canvas.setDrawFilter(pdf);
		canvasWidth = getWidth();
		canvasHeight = getHeight();
		canvasRect.set(0, 0, canvasWidth, canvasHeight);
		drawView(canvas);
	}
	
	public abstract void drawView(Canvas canvas);

}
