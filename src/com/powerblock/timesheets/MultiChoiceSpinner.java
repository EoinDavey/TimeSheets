package com.powerblock.timesheets;

import java.util.ArrayList;
import java.util.Arrays;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

@SuppressLint("ClickableViewAccessibility")
public class MultiChoiceSpinner extends Spinner implements
		OnMultiChoiceClickListener, OnCancelListener, PBSpinner {
	private CharSequence[] items;
	private boolean[] selected;
	private String defaultText = "Choose Items";
	private MultiSpinnerListener listener;
	private static XmlHandler mXmlHandler;
	
	public MultiChoiceSpinner(Context context) {
		super(context);
	}
	
	public MultiChoiceSpinner(Context context, AttributeSet arg1){
		super(context,arg1);
		init(arg1);
	}
	
	public MultiChoiceSpinner(Context context, AttributeSet arg1, int arg2){
		super(context,arg1, arg2);
		init(arg1);
	}
	
	public CharSequence[] getSelectedItems(){
		int total = 0;
		for(int i = 0; i < selected.length; i ++){
			if(selected[i] == true){
				total += 1;
			}
		}
		int current = 0;
		CharSequence[] selectedItems = new CharSequence[total];
		for(int i = 0; i < selected.length; i++){
			if(selected[i] == true){
				selectedItems[current] = items[i];
				current += 1;
			}
		}
		
		return selectedItems;
	}
	
	public String getString(){
		StringBuffer b = new StringBuffer();
		for(int i = 0; i < selected.length; i++){
			if(selected[i] == true){
				b.append(items[i] + ",");
			}
		}
		
		return b.toString();
	}
	
	private void init(AttributeSet attrs){
		mXmlHandler = MainActivity.getXmlHandler();
		if(mXmlHandler == null){
			Log.v("Test", "Xml handler is null");
			return;
		}
		TypedArray a = getContext().obtainStyledAttributes(attrs,R.styleable.MultiSpinner);
		CharSequence entry = a.getText(R.styleable.MultiSpinner_multi_entries);
		a.recycle();
		CharSequence[] entries = mXmlHandler.getStrings(entry.toString());
		if(entries != null){
			selected = new boolean[entries.length];
			items = entries;
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, new String[]{defaultText});
			setAdapter(adapter);
		}
		listener = new MultiSpinnerListener() {
			
			@Override
			public void onItemsSelected(CharSequence[] selected) {
				
			}
		};
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		StringBuffer spinnerBuffer = new StringBuffer();
		boolean someSelected = false;
		String spinnerText = defaultText;
		if(items != null){
			for(int i = 0; i < items.length; i ++){
				if(selected[i] == true){
					spinnerBuffer.append(items[i]);
					spinnerBuffer.append(", ");
					someSelected = true;
				}
			}
			if (someSelected){
				spinnerText = spinnerBuffer.toString();
				if(spinnerText.length() > 2){
					spinnerText = spinnerText.substring(0, spinnerText.length() - 2);
				}
			} else {
				spinnerText = defaultText;
			}
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, new String[] {spinnerText});
		setAdapter(adapter);
		listener.onItemsSelected(getSelectedItems());
	}
	
	@Override
	public boolean performClick(){
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setMultiChoiceItems(items,selected, this);
		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.setOnCancelListener(this);
		builder.show();
		return true;
	}

	@Override
	public void onClick(DialogInterface dialog, int which, boolean isChecked) {
		if(isChecked)
			selected[which] = true;
		else
			selected[which] = false;

	}
	
	public void setItems(CharSequence[] items, String allText, MultiSpinnerListener listener){
		this.items = items;
		this.defaultText = allText;
		this.listener = listener;
		
		selected = new boolean[items.length];
		for(int i = 0; i <selected.length; i ++){
			selected[i] = false;
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, new String[]{allText});
		setAdapter(adapter);
	}
	
	public void setListener(MultiSpinnerListener listener){
		this.listener = listener;
	}
	
	public interface MultiSpinnerListener{
		public void onItemsSelected(CharSequence[] selected);
	}

	@Override
	public void select(String s) {
		selected = new boolean[items.length];
		ArrayList<String> l = new ArrayList<String>(Arrays.asList(s.split(",")));
		for(int i = 0; i < items.length; i ++){
			Log.v("Test", "given: " + l.get(i));
			if(l.contains(items[i])){
				selected[i] = true;
			}
		}
	}

}
