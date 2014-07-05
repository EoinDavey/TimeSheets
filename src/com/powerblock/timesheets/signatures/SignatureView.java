package com.powerblock.timesheets.signatures;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

@SuppressLint("ClickableViewAccessibility")
public class SignatureView extends View {
	
	private Path mPath;
	private Paint drawPaint;
	private boolean moved = false;

	public SignatureView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setUpDrawing();
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		//canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		//drawCanvas = new Canvas(canvasBitmap);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		//canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
		canvas.drawPath(mPath, drawPaint);
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		float touchX = event.getX();
		float touchY = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			moved = false;
		    mPath.moveTo(touchX, touchY);
		    break;
		case MotionEvent.ACTION_MOVE:
			moved = true;
		    mPath.lineTo(touchX, touchY);
		    break;
		case MotionEvent.ACTION_UP:
			if(!moved){
				mPath.lineTo(touchX+1, touchY+1);
			}
			moved = false;
		    break;
		default:
		    return false;
		}
		invalidate();
		return true;
	}
	
	private void setUpDrawing(){
		mPath = new Path();
		drawPaint = new Paint();
		drawPaint.setColor(Color.parseColor("#000000"));
		drawPaint.setAntiAlias(true);
		drawPaint.setStrokeWidth(10);
		drawPaint.setStyle(Paint.Style.STROKE);
		drawPaint.setStrokeJoin(Paint.Join.ROUND);
		drawPaint.setStrokeCap(Paint.Cap.ROUND);
		//canvasPaint = new Paint(Paint.DITHER_FLAG);
	}
	
	public void Clear(){
		mPath.reset();
		invalidate();
	}

}
