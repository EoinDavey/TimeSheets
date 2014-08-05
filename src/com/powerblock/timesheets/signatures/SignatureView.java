package com.powerblock.timesheets.signatures;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

@SuppressLint("ClickableViewAccessibility")
public class SignatureView extends View {
	
	private Path mPath;
	private Paint drawPaint;
	private boolean moved = false;
	private static final String root = Environment.getExternalStorageDirectory().toString();
	public static final String custSigLoc = ".workingTemplate/custSigTemp.png";
	public static final String empSigLoc = ".workingTemplate/empSigTemp.png";
	private static File custSig;
	private static File empSig;
	private static String mSigType;

	public SignatureView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setUpDrawing();
		custSig = new File(root, custSigLoc);
		empSig = new File(root,empSigLoc);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	}
	
	public void setSigType(String s){
		mSigType = s;
	}
	
	public String getSigType(){
		return mSigType;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
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
	}
	
	public File saveSignature(){
		Log.v("Test","Save called");
		OutputStream out = null;
		this.setDrawingCacheEnabled(true);
		this.setDrawingCacheBackgroundColor(Color.WHITE);
		this.buildDrawingCache();
		Bitmap b = this.getDrawingCache();
		File sig = null;
		if(b == null){
			Log.v("Test","Bitmap is null");
		}
		try{
			if(mSigType.equalsIgnoreCase(SignatureActivity.SIG_IDENTIFIER_CUST)){
				sig = custSig;
			} else if(mSigType.equalsIgnoreCase(SignatureActivity.SIG_IDENTIFIER_EMP)){
				sig = empSig;
			}
			out = new FileOutputStream(sig);
			if(b.compress(Bitmap.CompressFormat.PNG, 90, out)){
				Log.v("Test","Compressed");
			}
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sig;
	}
	
	public void Clear(){
		mPath.reset();
		invalidate();
	}

}
