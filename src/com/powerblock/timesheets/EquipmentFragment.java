package com.powerblock.timesheets;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class EquipmentFragment extends Fragment {
	
	private ExcelHandler mExcelHandler;
	private View mView;
	
	public EquipmentFragment(){
		mExcelHandler = MainActivity.getExcelHandler();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		setHasOptionsMenu(true);
		mView = mExcelHandler.read(inflater, container, R.layout.equipment_fragment, ExcelHandler.EXCEL_SECTION_EQUIPMENT);
		if(mView == null){
			mView = inflater.inflate(R.layout.equipment_fragment, container, false);
		}
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
			mExcelHandler.write(ExcelHandler.EXCEL_SECTION_EQUIPMENT,mView);
			return true;
		}
		return false;
	}
}
