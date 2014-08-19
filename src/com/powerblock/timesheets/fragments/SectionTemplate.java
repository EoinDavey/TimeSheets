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

public class SectionTemplate extends Fragment {
	private ExcelHandler mExcelHandler;
	private View mView;
	private String sSection;
	
	public SectionTemplate(String section){
		mExcelHandler = MainActivity.getExcelHandler();
		sSection = section;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		setHasOptionsMenu(true);
		int layoutId = 0;
		
		
		switch(sSection){
			case ExcelHandler.EXCEL_SECTION_EQUIPMENT:
				layoutId = R.layout.equipment_fragment;
				break;
			case ExcelHandler.EXCEL_SECTION_JOB_SETUP:
				layoutId = R.layout.job_setup_fragment;
				break;
			case ExcelHandler.EXCEL_SECTION_SAFETY_LOCK_OUT:
				layoutId = R.layout.lock_out;
				break;
			case ExcelHandler.EXCEL_SECTION_SAFETY_MANUAL_HANDLING:
				layoutId = R.layout.safety_manual_handling;
				break;
			case ExcelHandler.EXCEL_SECTION_MATERIALS_CABLE:
				layoutId = R.layout.materials_cable;
				break;
			case ExcelHandler.EXCEL_SECTION_MATERIALS_CONTAINMENT:
				layoutId = R.layout.materials_containment;
				break;
			case ExcelHandler.EXCEL_SECTION_MATERIALS_DATA:
				layoutId = R.layout.materials_data;
				break;
			case ExcelHandler.EXCEL_SECTION_MATERIALS_LIGHTING:
				layoutId = R.layout.materials_lighting;
				break;
			case ExcelHandler.EXCEL_SECTION_MATERIALS_POWER:
				layoutId = R.layout.materials_power;
				break;
			case ExcelHandler.EXCEL_SECTION_SAFETY_PPE:
				layoutId = R.layout.ppe;
				break;
			case ExcelHandler.EXCEL_SECTION_SAFETY_SITE_CONDITIONS:
				layoutId = R.layout.safety_site_conditions;
				break;
			case ExcelHandler.EXCEL_SECTION_TESTING:
				layoutId = R.layout.testing_fragment;
				break;
			case ExcelHandler.EXCEL_SECTION_TIME:
				layoutId = R.layout.time_layout;
				break;
			case ExcelHandler.EXCEL_SECTION_SAFETY_WORKING_AT_HEIGHT:
				layoutId = R.layout.safety_height;
				break;
			case ExcelHandler.EXCEL_SECTION_SAFETY_OTHER:
				layoutId = R.layout.safety_other;
				break;
			case ExcelHandler.EXCEL_SECTION_TESTING_TYPE:
				layoutId = R.layout.testing_test_type;
				break;
			case ExcelHandler.EXCEL_SECTION_TESTING_DBDETAILS:
				layoutId = R.layout.testing_db_details;
				break;
		}
		
		mView = mExcelHandler.read(inflater, container, layoutId, sSection);
		if(mView == null)
			mView = inflater.inflate(layoutId, container,false);
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
			mExcelHandler.write(sSection,mView);
			getActivity().getSupportFragmentManager().popBackStack();
			return true;
		}
		return false;
	}
}
