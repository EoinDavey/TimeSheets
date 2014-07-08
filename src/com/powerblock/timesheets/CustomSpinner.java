package com.powerblock.timesheets;

import java.util.Scanner;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class CustomSpinner extends Spinner implements PBSpinner {
	private XmlHandler mXmlHandler;
	private String[] items;

	public CustomSpinner(Context context) {
		super(context);
	}
	
	public CustomSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	public CustomSpinner(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs);
	}
	
	private void init(AttributeSet attrs){
		mXmlHandler = MainActivity.getXmlHandler();
		TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomSpinner);
		CharSequence entry = a.getText(R.styleable.CustomSpinner_entries);
		Log.v("Test","Entry = "+entry);
		a.recycle();
		items = mXmlHandler.getStrings(entry.toString());
		if(entry != null && items != null){
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, items);
			super.setAdapter(adapter);
		}
		
	}
	public void select(String s){
		int position = 0;
		for(int i = 0; i < items.length; i ++){
			if(items[i].equalsIgnoreCase(s)){
				position = i;
				break;
			}
		}
		setSelection(position);
	}
	public String getString(){
		String s = (String) getSelectedItem();
		Scanner sc = new Scanner(s);
		if(sc.next().equalsIgnoreCase("Choose")){
			sc.close();
			sc = null;
			return "";
		} else{
			sc.close();
			sc = null;
			return s;
		}
	}

}
