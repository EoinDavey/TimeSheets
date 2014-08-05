package com.powerblock.timesheets.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.powerblock.timesheets.ExcelHandler;
import com.powerblock.timesheets.MainActivity;
import com.powerblock.timesheets.R;

public class JobSetupFragment extends Fragment {
	
	private ExcelHandler mExcelHandler;
	private View mView;

	public JobSetupFragment(){
		mExcelHandler = MainActivity.getExcelHandler();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		setHasOptionsMenu(true);
		mView = mExcelHandler.read(inflater, container, R.layout.job_setup_fragment, ExcelHandler.EXCEL_SECTION_JOB_SETUP);
		if(mView == null)
			mView = inflater.inflate(R.layout.job_setup_fragment, container,false);
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
			mExcelHandler.write(ExcelHandler.EXCEL_SECTION_JOB_SETUP,mView);
			getActivity().getSupportFragmentManager().popBackStack();
			return true;
		}
		return false;
	}

}
