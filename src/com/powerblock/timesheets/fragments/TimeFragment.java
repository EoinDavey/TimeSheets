package com.powerblock.timesheets.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.powerblock.timesheets.ExcelHandler;
import com.powerblock.timesheets.MainActivity;
import com.powerblock.timesheets.R;
import com.powerblock.timesheets.signatures.SignatureActivity;

public class TimeFragment extends Fragment {
	
	private ExcelHandler mExcelHandler;
	static final int TIME_FRAGMENT_REQUEST_CODE = 1;
	public static final String TIME_FRAGMENT_SIGNATURE_CODE = "Signature Type";
	private View mView;
	
	public TimeFragment(){
		mExcelHandler = MainActivity.getExcelHandler();
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		setHasOptionsMenu(true);
		mView = mExcelHandler.read(inflater, container, R.layout.time_layout, ExcelHandler.EXCEL_SECTION_TIME);
		if(mView == null)
			mView = inflater.inflate(R.layout.time_layout, container,false);
		Button b = (Button) mView.findViewById(R.id.time_customer_signature_button);
		b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(),SignatureActivity.class);
				i.putExtra(TIME_FRAGMENT_SIGNATURE_CODE, SignatureActivity.SIG_IDENTIFIER_CUST);
				startActivityForResult(i,TIME_FRAGMENT_REQUEST_CODE);
			}
		});
		b = (Button) mView.findViewById(R.id.time_employee_signature_button);
		b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), SignatureActivity.class);
				i.putExtra(TIME_FRAGMENT_SIGNATURE_CODE, SignatureActivity.SIG_IDENTIFIER_EMP);
				startActivityForResult(i, TIME_FRAGMENT_REQUEST_CODE);
			}
		});
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
			mExcelHandler.write(ExcelHandler.EXCEL_SECTION_TIME,mView);
			getActivity().getSupportFragmentManager().popBackStack();
			return true;
		}
		return false;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == TIME_FRAGMENT_REQUEST_CODE){
			if(resultCode == Activity.RESULT_OK){
				String code = data.getStringExtra(SignatureActivity.SIG_IDENTIFIER_FILE);
				String type = data.getStringExtra(SignatureActivity.SIG_IDENTIFIER_TYPE);
				if(!code.equalsIgnoreCase("Error")){
					mExcelHandler.saveImage(code, type);
				}
			}
		}
	}
}
