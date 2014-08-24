package com.powerblock.timesheets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

@SuppressLint("InflateParams")
public class CustomSpinner extends Spinner implements PBSpinner {
	private XmlHandler mXmlHandler;
	private ArrayList<String> items;
	private Context ctx;

	public CustomSpinner(Context context) {
		super(context);
		ctx = context;
	}
	
	public CustomSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		ctx = context;
		init(attrs);
	}

	public CustomSpinner(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		ctx = context;
		init(attrs);
	}
	
	private void init(AttributeSet attrs){
		mXmlHandler = MainActivity.getXmlHandler();
		TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomSpinner);
		CharSequence entry = a.getText(R.styleable.CustomSpinner_entries);
		Log.v("Test","Entry = "+entry);
		a.recycle();
		String[] returnedStrings = mXmlHandler.getStrings(entry.toString());
		if(entry != null && returnedStrings != null){
			items =  new ArrayList<String>(Arrays.asList(returnedStrings));
			items.add("Enter Text");
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, items);
			super.setAdapter(adapter);
		}
		this.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if(items.get(arg2).equalsIgnoreCase("Enter Text")){
					enterNew();
				} else {
					setSelection(arg2);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				setSelection(0);
			}
		});
		
	}
	
	private void enterNew(){
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		LayoutInflater li = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View v = li.inflate(R.layout.new_spinner_dialog, null);
		final EditText et = (EditText) v.findViewById(R.id.spinner_dialog_edit_text);
		builder.setTitle("New Entry");
		builder.setView(v).setPositiveButton("Save", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				addNew(et.getText().toString());
			}
		}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).show();
	}
	
	private void addNew(String s){
		items.set(items.size() - 1, s);
		items.add("Enter Text");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, items);
		super.setAdapter(adapter);
		setSelection(items.size() - 2);
	}
	
	public void select(String s){
		int position = 0;
		for(int i = 0; i < items.size(); i ++){
			if(items.get(i).equalsIgnoreCase(s)){
				position = i;
				break;
			}
		}
		
		if(position == 0){
			items.add(s);
			position = items.size() - 1;
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
