package com.powerblock.timesheets.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.powerblock.timesheets.ExcelHandler;
import com.powerblock.timesheets.MainActivity;
import com.powerblock.timesheets.R;

public class MaterialsFragment extends Fragment {
	
	private ExcelHandler mExcelHandler;
	private View mView;
	
	public MaterialsFragment(){
		mExcelHandler = MainActivity.getExcelHandler();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		setHasOptionsMenu(false);
		return inflater.inflate(R.layout.materials_fragment, container,false);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		super.onCreateOptionsMenu(menu, inflater);
	}

}
