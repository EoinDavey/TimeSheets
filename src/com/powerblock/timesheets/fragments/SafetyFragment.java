package com.powerblock.timesheets.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.powerblock.timesheets.MultiChoiceSpinner.MultiSpinnerListener;
import com.powerblock.timesheets.R;

public class SafetyFragment extends Fragment implements MultiSpinnerListener{

	/**
	 * @param args
	 */
	public SafetyFragment(){
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.safety_fragment, container, false);
		setHasOptionsMenu(true);
		return v;
	}

	@Override
	public void onItemsSelected(CharSequence[] selected) {
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		inflater.inflate(R.menu.save_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		if(item.getItemId() == R.id.action_save){
			
			return true;
		}
		return false;
	}

}
