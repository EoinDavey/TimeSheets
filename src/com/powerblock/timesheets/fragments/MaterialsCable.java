package com.powerblock.timesheets.fragments;

import com.powerblock.timesheets.ExcelHandler;
import com.powerblock.timesheets.MainActivity;
import com.powerblock.timesheets.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MaterialsCable extends Fragment {
	private ExcelHandler mExcelHandler;
	private View mView;

	public MaterialsCable(){
		mExcelHandler = MainActivity.getExcelHandler();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		setHasOptionsMenu(true);
		mView = mExcelHandler.read(inflater, container, R.layout.materials_cable, ExcelHandler.EXCEL_SECTION_MATERIALS_CABLE);
		if(mView == null)
			mView = inflater.inflate(R.layout.materials_cable, container,false);
		return mView;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		inflater.inflate(R.menu.save_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		if(item.getItemId() == R.id.action_save){
			mExcelHandler.write(ExcelHandler.EXCEL_SECTION_MATERIALS_CABLE,mView);
			getActivity().getSupportFragmentManager().popBackStack();
			return true;
		}
		return false;
	}
}
