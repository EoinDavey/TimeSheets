package com.powerblock.timesheets;

import com.powerblock.timesheets.signatures.SignatureActivity;

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

public class TimeFragment extends Fragment {
	public TimeFragment(){
		
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		setHasOptionsMenu(true);
		View v = inflater.inflate(R.layout.time_layout, container,false);
		Button b = (Button) v.findViewById(R.id.time_customer_signature_button);
		b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(),SignatureActivity.class);
				startActivity(i);
			}
		});
		return v;	
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
