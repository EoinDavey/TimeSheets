package com.powerblock.timesheets;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.CheckBox;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class CustomCheckBox extends CheckBox implements PBSpinner {
	private Drawable mIcon;
	private boolean mNormalMode = false;

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
	
	@SuppressWarnings("deprecation")
	private void init(AttributeSet attrs){
		TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomCheckBox);
		mIcon = a.getDrawable(R.styleable.CustomCheckBox_icon_image);
		a.recycle();
		if(mIcon == null){
			mNormalMode = true;
		} else {
			if(android.os.Build.VERSION.SDK_INT < 16){
				this.setBackgroundDrawable(mIcon);
			} else {
				this.setBackground(mIcon);
			}
			setButtonDrawable(new ColorDrawable(Color.WHITE));
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void setChecked(boolean checked) {
		if(mNormalMode){
			super.setChecked(checked);
		} else {
			if(checked){
				Drawable d = getResources().getDrawable(R.drawable.checked);
				if(android.os.Build.VERSION.SDK_INT < 16){
					this.setBackgroundDrawable(d);
				} else {
					this.setBackground(d);
				}
				
			} else {
				if(android.os.Build.VERSION.SDK_INT < 16){
					this.setBackgroundDrawable(mIcon);
				} else {
					this.setBackground(mIcon);
				}
			}
			super.setChecked(checked);
		}
	}

	@Override
	public String getString() {
		String result;
		if(this.isChecked()){
			result = "YES";
		} else {
			result = "NO";
		}
		return result;
	}

	@Override
	public void select(String s) {
		if(s.equalsIgnoreCase("YES")){
			this.setChecked(true);
		}
	}

}
