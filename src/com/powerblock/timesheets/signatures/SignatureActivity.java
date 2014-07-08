package com.powerblock.timesheets.signatures;

import java.io.File;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.powerblock.timesheets.R;
import com.powerblock.timesheets.fragments.TimeFragment;

public class SignatureActivity extends ActionBarActivity {
	private SignatureView view;
	private File custSig;
	public static final String SIG_IDENTIFIER_FILE= "SigFile";
	public static final String SIG_IDENTIFIER_TYPE= "SigType";
	public static final String SIG_IDENTIFIER_CUST = "CustomerSig";
	public static final String SIG_IDENTIFIER_EMP = "EmpSig";
	public static String mSigType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signature_layout);
	}
	
	@Override
	protected void onStart() {
		mSigType = getIntent().getStringExtra(TimeFragment.TIME_FRAGMENT_SIGNATURE_CODE);
		view = (SignatureView) findViewById(R.id.signature_view);
		view.setSigType(mSigType);
		super.onStart();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.signature_menu, menu);
		return true;
	}
	
	private void finishWithResult(int resultcode){
		Intent i = new Intent();
		if(custSig != null){
			i.putExtra(SIG_IDENTIFIER_FILE, custSig.toString());
			i.putExtra(SIG_IDENTIFIER_TYPE, mSigType);
			setResult(resultcode, i);
			finish();
		} else {
			i.putExtra(SIG_IDENTIFIER_FILE, "Error");
			i.putExtra(SIG_IDENTIFIER_TYPE, mSigType);
			setResult(resultcode,i);
			finish();
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.signature_clear){
			view.Clear();
			return true;
		} else if(id == R.id.signature_save){
			custSig = view.saveSignature();
			finishWithResult(RESULT_OK);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
