package com.powerblock.timesheets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.powerblock.timesheets.fragments.MainFragment;
import com.powerblock.timesheets.fragments.MaterialsFragment;
import com.powerblock.timesheets.fragments.SafetyFragment;
import com.powerblock.timesheets.fragments.SectionTemplate;
import com.powerblock.timesheets.fragments.TimeFragment;

@SuppressLint("InflateParams")
public class MainActivity extends ActionBarActivity {
	
	public static final String root = Environment.getExternalStorageDirectory().toString();
	public final static String workingTemplateDir = "/.workingTemplate";
	public final static String workingTemplateFileName="workingTemplate.xls";
	public final static String outputDir = root + "/Time Sheets";
	private static ExcelHandler mExcelHandler;
	private static XmlHandler mXmlHandler;
	private static Activity thisActivity;
	private File workingTemplate;
	private File timeSheets[];
	private String timeSheetNames[];
	private String openFileName = "NoFileOpen";
	private ProgressDialog mProgressDialog;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		thisActivity = this;
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new MainFragment()).commit();
		}
		File myDir = new File(root + workingTemplateDir);
		if(!myDir.exists()){
			myDir.mkdirs();
		}
		workingTemplate = new File(myDir, workingTemplateFileName);
		checkAndCreateTemplate();
		mExcelHandler = new ExcelHandler(this);
		mXmlHandler = new XmlHandler(this);
	}
	
	public static XmlHandler getXmlHandler(){
		if(mXmlHandler != null){
			return mXmlHandler;
		} else {
			toastError();
			return null;
		}
	}
	
	public static ExcelHandler getExcelHandler(){
		if(mExcelHandler != null){
			return mExcelHandler;
		} else {
			toastError();
			return null;
		}
	}

	public static void toastError() {
		Toast.makeText(thisActivity, "Error, please contact developer", Toast.LENGTH_LONG).show();
	}
	
	public void updateCurrentStrings(){
		final DownloadTask downTask = new DownloadTask(this);
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setMessage("Updating");
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgressDialog.setCancelable(true);
		mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				downTask.cancel(true);
			}
		});
		downTask.execute("https://docs.google.com/uc?export=download&id=0B60096yO2RWEelR6RmdsZk1GbXZnbGxNX0ZucEp2R0dyb2JV");
		//downTask.execute("https://docs.google.com/uc?export=download&id=0B2yJeDqIJEEhNzcwTEpNTGVkRW8");
		//https://docs.google.com/uc?export=download&id=0B7ZA1yW79zKtdlYxTWpNemcwN2c
		workingTemplate.delete();
		checkAndCreateTemplate();
	}
	
	public void checkAndCreateTemplate(){
		InputStream in = null;
		OutputStream out = null;
		if(!workingTemplate.exists()){
			try{
				in = getResources().openRawResource(R.raw.time_sheets_template);
				out = new FileOutputStream(workingTemplate);
				
				byte[] buffer = new byte[1024];
				int read;
				while((read = in.read(buffer)) != - 1){
					out.write(buffer, 0, read);
				}
				in.close();
				in = null;
				out.flush();
				out.close();
				out = null;
			} catch(Exception e){
				
			}
		}
	}
	
	public void showTable(Fragment frag, String back){
		getSupportFragmentManager().beginTransaction().replace(R.id.container, frag).addToBackStack(back).commit();
	}
	
	public void show(String section){
		getSupportFragmentManager().beginTransaction().replace(R.id.container, new SectionTemplate(section)).addToBackStack(section).commit();
	}
	
	public void showJobSetup(View v){
		show(ExcelHandler.EXCEL_SECTION_JOB_SETUP);
	}
	
	public void showSafety(View v){
		showTable(new SafetyFragment(), ExcelHandler.EXCEL_SECTION_SAFETY);
	}
	
	public void showTime(View v){
		showTable(new TimeFragment(),ExcelHandler.EXCEL_SECTION_TIME);
	}
	
	public void showEquipment(View v){
		show(ExcelHandler.EXCEL_SECTION_EQUIPMENT);
	}
	
	public void showMaterials(View v){
		showTable(new MaterialsFragment(), ExcelHandler.EXCEL_SECTION_MATERIALS);
	}
	
	public void showTesting(View v){
		show(ExcelHandler.EXCEL_SECTION_TESTING);
	}
	
	public void showMaterialsLighting(View v){
		show(ExcelHandler.EXCEL_SECTION_MATERIALS_LIGHTING);
	}
	
	public void showMaterialsPower(View v){
		show(ExcelHandler.EXCEL_SECTION_MATERIALS_POWER);
	}
	
	public void showMaterialsData(View v){
		show(ExcelHandler.EXCEL_SECTION_MATERIALS_DATA);
	}
	
	public void showMaterialsContainment(View v){
		show(ExcelHandler.EXCEL_SECTION_MATERIALS_CONTAINMENT);
	}
	
	public void showMaterialsCable(View v){
		show(ExcelHandler.EXCEL_SECTION_MATERIALS_CABLE);
	}
	
	public void showMaterialsMCBs(View v){
		show(ExcelHandler.EXCEL_SECTION_MATERIALS_MCB);
	}
	
	public void showSiteConditions(View v){
		show(ExcelHandler.EXCEL_SECTION_SAFETY_SITE_CONDITIONS);
	}
	
	public void showPPE(View v){
		show(ExcelHandler.EXCEL_SECTION_SAFETY_PPE);
	}
	
	public void showLockOut(View v){
		show(ExcelHandler.EXCEL_SECTION_SAFETY_LOCK_OUT);
	}
	
	public void showSafetyOther(View v){
		show(ExcelHandler.EXCEL_SECTION_SAFETY_WELFARE);
	}
	
	public void showManualHandling(View v){
		show(ExcelHandler.EXCEL_SECTION_SAFETY_MANUAL_HANDLING);
	}
	
	public void showWorkingAtHeight(View v){
		show(ExcelHandler.EXCEL_SECTION_SAFETY_WORKING_AT_HEIGHT);
	}
	
	public void showTestType(View v){
		show(ExcelHandler.EXCEL_SECTION_TESTING_TYPE);
	}
	
	public void showDBDetails(View v){
		show(ExcelHandler.EXCEL_SECTION_TESTING_DBDETAILS);
	}
	
	public void showDescription(View v){
		show(ExcelHandler.EXCEL_SECTION_JOB_SETUP_DESCRIPTION);
	}
	
	public void showQuoteNo(View v){
		show(ExcelHandler.EXCEL_SECTION_JOB_SETUP_QUOTE_NO);
	}
	
	public void showPreConnection(View v){
		show(ExcelHandler.EXCEL_SECTION_TESTING_PRECONNECTION);
	}
	
	public void showPostConnection(View v){
		show(ExcelHandler.EXCEL_SECTION_TESTING_POSTCONNECTION);
	}
	
	public void saveTimeSheet(String fileName){
		if(fileName.equalsIgnoreCase("Error")){
			Toast.makeText(this, "Error, please contact developer", Toast.LENGTH_LONG).show();
			return;
		}
		Log.v("Test","Save called, filename = " + fileName);
		InputStream in = null;
		OutputStream out = null;
		File outputDir = new File(MainActivity.outputDir);
		File output = new File(outputDir, fileName);
		Log.v("Test", "Output = " + output.toString());
		if(!outputDir.exists()){
			outputDir.mkdirs();
		}
		try{
			in = new FileInputStream(workingTemplate);
			out = new FileOutputStream(output);
			
			byte[] buffer = new byte[1024];
			int read;
			while((read = in.read(buffer)) != - 1){
				out.write(buffer, 0, read);
			}
			in.close();
			in = null;
			out.flush();
			out.close();
			out = null;
			workingTemplate.delete();
			Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		checkAndCreateTemplate();
		openFileName="NoFileOpen";
	}
	
	public void showSaveDialog(View view){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final View v = getLayoutInflater().inflate(R.layout.save_dialog, null);
		if(!openFileName.equalsIgnoreCase("NoFileOpen")){
			EditText et = (EditText) v.findViewById(R.id.save_dialog_edit_text);
			et.setText(openFileName);
		}
		builder.setTitle("Enter File Name");
		builder.setView(v).setPositiveButton("Save", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.v("Test","Save clicked");
				String fileName = "Error";
				EditText editText = (EditText) v.findViewById(R.id.save_dialog_edit_text);
				fileName = editText.getText().toString() + ".xls";
				if(fileName.length() < 5){
					Toast.makeText(getApplication(), "Error, please try again", Toast.LENGTH_LONG).show();
					return;
				}
				Log.v("Test","Filename = " + fileName);
				saveTimeSheet(fileName);
			}
		}).show();
		Log.v("Test","Dialog Shown");
	}
	
	public void listTimeSheets(DialogInterface.OnClickListener listener){
		File dir = new File(outputDir);
		timeSheets = dir.listFiles();
		if(timeSheets != null){
			timeSheetNames = new String[timeSheets.length];
			for(int i = 0; i < timeSheets.length; i ++){
				String name = timeSheets[i].getName();
				Log.v("Test","Filename = " + name);
				timeSheetNames[i] = name.substring(0, name.length()-4);
				Log.v("Test","Final name = " + timeSheetNames[i]);
			}
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Choose File").setAdapter(new ArrayAdapter<String>(this, R.layout.time_sheet_list_text_view, timeSheetNames), listener)
			.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			}).show();
		} else {
			Toast.makeText(this, "No Files to load", Toast.LENGTH_LONG).show();
		}
	}
	
	public void showLoadDialog(View v){
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.v("Test","Selected File = " + timeSheets[which].getName());
				loadTimeSheet(timeSheets[which].getName());
				openFileName = timeSheetNames[which];
			}
		};
		try{
			listTimeSheets(listener);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void loadTimeSheet(String fileName){
		File input = new File(outputDir, fileName);
		workingTemplate.delete();
		InputStream in = null;
		OutputStream out = null;
		try{
			in = new FileInputStream(input);
			out = new FileOutputStream(workingTemplate);
			byte[] buffer = new byte[1024];
			int read;
			while((read = in.read(buffer)) != - 1){
				out.write(buffer, 0, read);
			}
			in.close();
			in = null;
			out.flush();
			out.close();
			out = null;
		}catch(Exception e){
			e.printStackTrace();
		}
		checkAndCreateTemplate();
	}
	
	public void emailFile(String filename){
		Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "emcgoffice@gmail.com", null));
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "File: " + filename);
		File timeSheet = new File(outputDir, filename);
		Uri uri = Uri.parse("file://" + timeSheet.toString());
		emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
		startActivity(Intent.createChooser(emailIntent, "Send Email.."));
	}
	
	public void showEmailDialog(View v){
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				emailFile(timeSheets[which].getName());
			}
		};
		listTimeSheets(listener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_update) {
			updateCurrentStrings();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class DownloadTask extends AsyncTask<String, Integer, String>{
		private Context context;
		private PowerManager.WakeLock mWakeLock;
		
		public DownloadTask(Context context){
			this.context = context;
		}
		
		@SuppressWarnings("resource")
		@Override
		protected String doInBackground(String... sUrl){
			InputStream input = null;
			OutputStream output = null;
			HttpURLConnection connection = null;
			try{
				URL url = new URL(sUrl[0]);
				connection = (HttpURLConnection) url.openConnection();
				connection.connect();
				if(connection.getResponseCode() != HttpURLConnection.HTTP_OK){
					return "Server returned HTTP " + connection.getResponseCode()
	                        + " " + connection.getResponseMessage();
				}
				int fileLength = connection.getContentLength();
				File dir = new File(root + MainActivity.workingTemplateDir);
				if(!dir.exists()){
					dir.mkdirs();
				}
				File outputFile = new File(dir, "current.xml");
				input = connection.getInputStream();
				output = new FileOutputStream(outputFile);
				byte data[] = new byte[4096];
				long total = 0;
				int count;
				while ((count = input.read(data)) != -1) {
	                // allow canceling with back button
	                if (isCancelled()) {
	                    input.close();
	                    return null;
	                }
	                total += count;
	                // publishing the progress....
	                if (fileLength > 0) // only if total length is known
	                    publishProgress((int) (total * 100 / fileLength));
	                output.write(data, 0, count);
	            }
				mXmlHandler.updateSystem();
				publishProgress(((int) total * 100/fileLength) + 20);
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				try {
					output.close();
					input.close();
				} catch(Exception e){
					e.printStackTrace();
				}
				connection.disconnect();
			}
			return null;
		}
		@Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        // take CPU lock to prevent CPU from going off if the user 
	        // presses the power button during download
	        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
	        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
	             getClass().getName());
	        mWakeLock.acquire();
	        mProgressDialog.show();
	    }
		@Override
	    protected void onProgressUpdate(Integer... progress) {
	        super.onProgressUpdate(progress);
	        // if we get here, length is known, now set indeterminate to false
	        mProgressDialog.setIndeterminate(false);
	        mProgressDialog.setMax(120);
	        mProgressDialog.setProgress(progress[0]);
	    }

	    @Override
	    protected void onPostExecute(String result) {
	        mWakeLock.release();
	        mProgressDialog.dismiss();
	        if (result != null)
	            Toast.makeText(context,"Download error: "+result, Toast.LENGTH_LONG).show();
	        else
	            Toast.makeText(context,"File downloaded", Toast.LENGTH_SHORT).show();
	    }
	}

}
