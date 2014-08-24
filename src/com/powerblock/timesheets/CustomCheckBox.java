package com.powerblock.timesheets;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.CheckBox;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class CustomCheckBox extends CheckBox implements PBSpinner {
	private BitmapDrawable mIcon;
	private boolean noIcon = true;
	public CustomCheckBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}
	
	public CustomCheckBox(Context context, AttributeSet attrs, int i) {
		super(context, attrs);
		init(attrs);
	}
	
	@Override
	public void setButtonDrawable(Drawable d) {
		super.setButtonDrawable(d);
	}
	
	private void init(AttributeSet attrs){
		TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomCheckBox);
		mIcon = (BitmapDrawable) a.getDrawable(R.styleable.CustomCheckBox_icon_image);
		a.recycle();
		if(mIcon != null){
			noIcon = false;
			drawUncheckedIcon();
			setButtonDrawable(new ColorDrawable(Color.WHITE));
		}
	}
	
	@SuppressWarnings("deprecation")
	private void drawUncheckedIcon(){
		Drawable background = getResources().getDrawable(R.drawable.cell_background);
		background.setBounds(0, 0, 258, 258);
		if(mIcon != null){
			mIcon.setBounds(10,10,250,250);
			Bitmap b = Bitmap.createBitmap(258,258, Bitmap.Config.ARGB_8888);
			Canvas c = new Canvas(b);
			background.draw(c);
			mIcon.draw(c);
			if(android.os.Build.VERSION.SDK_INT < 16){
				this.setBackgroundDrawable(new BitmapDrawable(getResources(), b));
			} else {
				this.setBackground(new BitmapDrawable(getResources(), b));
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void setChecked(boolean checked) {
		if(!noIcon && checked){
			if(android.os.Build.VERSION.SDK_INT < 16){
				this.setBackgroundDrawable(drawCheckedIcon());
			} else {
				this.setBackground(drawCheckedIcon());
			}
			
		} else if(!noIcon && !checked) {
			drawUncheckedIcon();
		}
		super.setChecked(checked);
	}
	
	private Drawable drawCheckedIcon(){
		Drawable background = getResources().getDrawable(R.drawable.cell_background);
		Drawable d = getResources().getDrawable(R.drawable.checked_2);
		background.setBounds(0,0,258,258);
		d.setBounds(new Rect(10,10,250,250));
		mIcon.setBounds(10,10,250,250);
		Bitmap b = Bitmap.createBitmap(258, 258,Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(b);
		background.draw(c);
		mIcon.draw(c);
		d.draw(c);
		return new BitmapDrawable(getResources(),b);
	}

	@Override
	public String getString() {
		if(this.isChecked()){
			return "YES";
		} else {
			return "NO";
		}
	}

	@Override
	public void select(String s) {
		if(s.equalsIgnoreCase("YES")){
			this.setChecked(true);
		}
	}

}
