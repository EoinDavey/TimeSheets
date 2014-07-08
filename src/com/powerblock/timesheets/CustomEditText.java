package com.powerblock.timesheets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

public class CustomEditText extends EditText implements PBSpinner {

	public CustomEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public String getString() {
		return this.getText().toString();
	}

	@Override
	public void select(String s) {
		this.setText(s);
	}

}
