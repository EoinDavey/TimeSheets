package com.powerblock.timesheets.signatures;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.powerblock.timesheets.R;

public class SignatureActivity extends ActionBarActivity {
	private SignatureView view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signature_layout);
	}
	
	@Override
	protected void onStart() {
		view = (SignatureView) findViewById(R.id.signature_view);
		super.onStart();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.signature_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.signature_clear){
			view.Clear();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
