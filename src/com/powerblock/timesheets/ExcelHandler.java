package com.powerblock.timesheets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.powerblock.timesheets.signatures.SignatureActivity;

public class ExcelHandler {

	public static final String workingTemplateTempFileName = "output.xls";
	private File workingTemplateFile;
	private File workingTemplateTemp;
	private static final String root = Environment.getExternalStorageDirectory().toString();
	private Workbook mOriginalWorkingTemplate;
	private Activity mParent;
	private ProgressDialog mProgressDialog;
	private ProgressDialog mImageProgressDialog;

	//Section constants
	public static final String EXCEL_SECTION_JOB_SETUP = "JobSetup";
	public static final String EXCEL_SECTION_EQUIPMENT = "Equipment";
	public static final String EXCEL_SECTION_MATERIALS = "Materials";
	public static final String EXCEL_SECTION_TESTING = "Testing";
	public static final String EXCEL_SECTION_TIME = "Time";
	public static final String EXCEL_SECTION_SAFETY = "Safety";
	public static final String EXCEL_SECTION_MATERIALS_LIGHTING = "Lighting";
	public static final String EXCEL_SECTION_MATERIALS_POWER = "Power";
	public static final String EXCEL_SECTION_MATERIALS_DATA = "Data";
	public static final String EXCEL_SECTION_MATERIALS_CONTAINMENT = "Containment";
	public static final String EXCEL_SECTION_MATERIALS_CABLE = "Cable";
	public static final String EXCEL_SECTION_SAFETY_SITE_CONDITIONS="Site Conditions";
	public static final String EXCEL_SECTION_SAFETY_PPE="PPE";
	public static final String EXCEL_SECTION_SAFETY_LOCK_OUT="Lock out";
	public static final String EXCEL_SECTION_SAFETY_MANUAL_HANDLING="Manual Handling";
	public static final String EXCEL_SECTION_SAFETY_WORKING_AT_HEIGHT="Working at height";
	public static final String EXCEL_SECTION_SAFETY_OTHER = "Other";
	public static final String EXCEL_SECTION_TESTING_TYPE = "Testing Type";
	public static final String EXCEL_SECTION_TESTING_DBDETAILS = "DB Details";
	public static final String EXCEL_SECTION_TESTING_PRECONNECTION = "pre-connection";
	public static final String EXCEL_SECTION_TESTING_POSTCONNECTION = "post-connection";
	public static final String EXCEL_SECTION_TESTING_SIGNATURE = "Testing Signature";
	public static final String EXCEL_SECTION_TESTING_QCNUMBER = "QC Number";

	//Cell coordinates
	private final static int[] cJobType = {1,1};
	private final static int[] cSiteName = {1,2};
	private final static int[] cSiteAddress = {1,3};
	private final static int[] cPIC = {1,4};
	private final static int[] cAPersonnel = {1,5};
	private final static int[][] cJobSetupCells = {cJobType,cSiteName,cSiteAddress, cPIC, cAPersonnel};
	private final static int[] rJobSetupIds = {R.id.job_setup_job_types, 
		R.id.job_setup_site_name, R.id.job_setup_site_address, R.id.job_setup_PIC, 
		R.id.job_setup_personnel};
	
	private final static int[] rSafetySiteConditionIds = {
		R.id.safety_site_conditions_work_area,
		R.id.safety_site_conditions_checkbox_1, R.id.safety_site_conditions_checkbox_2, R.id.safety_site_conditions_checkbox_3, R.id.safety_site_conditions_checkbox_4,
		R.id.safety_site_conditions_checkbox_5, R.id.safety_site_conditions_checkbox_6, R.id.safety_site_conditions_checkbox_7, R.id.safety_site_conditions_checkbox_8,
		R.id.safety_site_conditions_checkbox_9, R.id.safety_site_conditions_checkbox_10, R.id.safety_site_conditions_checkbox_11, R.id.safety_site_conditions_checkbox_12,
		R.id.safety_site_conditions_checkbox_13, R.id.safety_site_conditions_checkbox_14, R.id.safety_site_conditions_checkbox_15, R.id.safety_site_conditions_checkbox_16,
		R.id.safety_site_conditions_checkbox_17, R.id.safety_site_conditions_checkbox_18, R.id.safety_site_conditions_checkbox_19, R.id.safety_site_conditions_checkbox_20,
	};
	
	private final static int[][] cSafetySiteConditionCells = {
		{2,55},
		{2,57},{2,58},{2,59},{2,60},
		{4,57},{4,58},{4,59},{4,60},
		{6,57},{6,58},{6,59},{6,60},
		{8,57},{8,58},{8,59},{8,60},
		{10,57},{10,58},{10,59},{10,60}
	};
	
	private final static int[] rSafetyPPEIds = {
		R.id.safety_ppe_checkbox_1, R.id.safety_ppe_checkbox_2, R.id.safety_ppe_checkbox_3,
		R.id.safety_ppe_checkbox_4, R.id.safety_ppe_checkbox_5, R.id.safety_ppe_checkbox_6,
		R.id.safety_ppe_checkbox_7, R.id.safety_ppe_checkbox_8, R.id.safety_ppe_checkbox_9,
		R.id.safety_ppe_checkbox_10, R.id.safety_ppe_checkbox_11, R.id.safety_ppe_checkbox_12
	};
	
	private final static int[][] cSafetyPPECells = {
		{2,64},{2,65},{2,66},
		{4,64},{4,65},{4,66},
		{6,64},{6,65},{6,66},
		{8,64},{8,65},{8,66}
	};
	
	private final static int[] rSafetyLockOutIds = {
		R.id.safety_lock_out_circuits,
		R.id.safety_lock_out_checkbox_1, R.id.safety_lock_out_checkbox_2, R.id.safety_lock_out_checkbox_3,
		R.id.safety_lock_out_checkbox_5, R.id.safety_lock_out_checkbox_6, R.id.safety_lock_out_checkbox_7,
		R.id.safety_lock_out_checkbox_9, R.id.safety_lock_out_checkbox_10, R.id.safety_lock_out_checkbox_11,
		R.id.safety_lock_out_checkbox_13,R.id.safety_lock_out_checkbox_19, R.id.safety_lock_out_checkbox_20
	};
	
	private final static int[][] cSafetyLockOutCells = {
		{2,69},
		{2,70},{2,71},{2,72},
		{4,70},{4,71},{4,72},
		{6,70},{6,71},{6,72},
		{8,70},{8,71},{8,72},
	};
	
	private final static int[] rSafetyManualIds = {
		R.id.safety_manual_load_types,
		R.id.safety_manual_checkbox_1,R.id.safety_manual_checkbox_2,R.id.safety_manual_checkbox_3,
		R.id.safety_manual_checkbox_4,R.id.safety_manual_checkbox_5,R.id.safety_manual_checkbox_6,
		R.id.safety_manual_checkbox_7,R.id.safety_manual_checkbox_8,R.id.safety_manual_checkbox_9
	};
	
	private final static int[][] cSafetyManualCells = {
		{2,75},
		{2,76},{2,77},{2,78},
		{4,76},{4,77},{4,78},
		{6,76},{6,77},{6,78}
	};
	
	private final static int[] rSafetyHeightIds={
		R.id.safety_height_access,
		R.id.safety_height_checkbox_1,R.id.safety_height_checkbox_2,R.id.safety_height_checkbox_3,R.id.safety_height_checkbox_4,
		R.id.safety_height_checkbox_5,R.id.safety_height_checkbox_6,R.id.safety_height_checkbox_7,R.id.safety_height_checkbox_8,
		R.id.safety_height_checkbox_9,R.id.safety_height_checkbox_10,R.id.safety_height_checkbox_11,R.id.safety_height_checkbox_12,
		R.id.safety_height_checkbox_13,R.id.safety_height_checkbox_14,R.id.safety_height_checkbox_15,R.id.safety_height_checkbox_16,
	};
	
	private final static int[][] cSafetyHeightCells = {
		{2,81},
		{2,82},{2,83},{2,84},{2,85},
		{4,82},{4,83},{4,84},{4,85},
		{6,82},{6,83},{6,84},{6,85},
		{8,82},{8,83},{8,84},{8,85},
	};

	private final static int[][] cEquipmentCells = 
		{{1,7},{1,11},{1,12},{1,13},{1,8},{1,9}};
	
	private final static int[] rEquipmentIds = 
		{R.id.equipment_drills, 
		R.id.equipment_test_1, R.id.equipment_test_2, R.id.equipment_equipment, 
		R.id.equipment_leads, R.id.equipment_access};

	private final static int[][] cMaterialsLighting = {
		{0,20},{0,21},
		{1,21},{1,22},{1,23},{1,24},
		{2,21},{2,22},{2,23},{2,24},
		{3,21},{3,22},{3,23},{3,24}};
	
	private final static int[] rMaterialsLighting = {
		R.id.materials_cell_lighting_store, R.id.materials_cell_lighting_docket,
		R.id.materials_cell_lighting_quantity_1,R.id.materials_cell_lighting_quantity_2,
		R.id.materials_cell_lighting_quantity_3,R.id.materials_cell_lighting_quantity_4,
		R.id.materials_cell_lighting_material_1,R.id.materials_cell_lighting_material_2,
		R.id.materials_cell_lighting_material_3,R.id.materials_cell_lighting_material_4,
		R.id.materials_cell_lighting_size_1,R.id.materials_cell_lighting_size_2,
		R.id.materials_cell_lighting_size_3,R.id.materials_cell_lighting_size_4};
	
	private final static int[][] cMaterialsPower = {
		{0,26},{0,27},
		{1,27},{1,28},{1,29},{1,30},
		{2,27},{2,28},{2,29},{2,30},
		{3,27},{3,28},{3,29},{3,30}
	};
	
	private final static int[] rMaterialsPower = {
		R.id.materials_power_store, R.id.materials_power_docket,
		R.id.materials_cell_power_quantity_1,R.id.materials_cell_power_quantity_2,
		R.id.materials_cell_power_quantity_3,R.id.materials_cell_power_quantity_4,
		R.id.materials_cell_power_material_1,R.id.materials_cell_power_material_2,
		R.id.materials_cell_power_material_3,R.id.materials_cell_power_material_4,
		R.id.materials_cell_power_size_1,R.id.materials_cell_power_size_2,
		R.id.materials_cell_power_size_3,R.id.materials_cell_power_size_4
	};
	
	private final static int[][] cMaterialsData = {
		{0,32},{0,33},
		{1,33},{1,34},{1,35},{1,36},
		{2,33},{2,34},{2,35},{2,36},
		{3,33},{3,34},{3,35},{3,36}};
	
	private final static int[] rMaterialsData = {
		R.id.materials_cell_data_store, R.id.materials_cell_data_docket,
		R.id.materials_cell_data_quantity_1,R.id.materials_cell_data_quantity_2,
		R.id.materials_cell_data_quantity_3,R.id.materials_cell_data_quantity_4,
		R.id.materials_cell_data_material_1,R.id.materials_cell_data_material_2,
		R.id.materials_cell_data_material_3,R.id.materials_cell_data_material_4,
		R.id.materials_cell_data_size_1,R.id.materials_cell_data_size_2,
		R.id.materials_cell_data_size_3,R.id.materials_cell_data_size_4};
	
	private final static int[][] cMaterialsContainment = {
		{1,39},{1,40},{1,41},{1,42},
		{2,39},{2,40},{2,41},{2,42},
		{3,39},{3,40},{3,41},{3,42}
		};
	private final static int[] rMaterialsContainment = {
		R.id.materials_cell_containment_quantity_1,R.id.materials_cell_containment_quantity_3,
		R.id.materials_cell_containment_quantity_3,R.id.materials_cell_containment_quantity_4,
		R.id.materials_cell_containment_material_1,R.id.materials_cell_containment_material_2,
		R.id.materials_cell_containment_material_3,R.id.materials_cell_containment_material_4,
		R.id.materials_cell_containment_size_1,R.id.materials_cell_containment_size_3,
		R.id.materials_cell_containment_size_3,R.id.materials_cell_containment_size_4
		};
	
	private final static int[][] cMaterialsCable = {
		{1,45},{1,46},{1,47},{1,48},
		{2,45},{2,46},{2,47},{2,48},
		{3,45},{3,46},{3,47},{3,48}
		};
	private final static int[] rMaterialsCable = {
		R.id.materials_cell_cable_quantity_1,R.id.materials_cell_cable_quantity_2,
		R.id.materials_cell_cable_quantity_3,R.id.materials_cell_cable_quantity_4,
		R.id.materials_cell_cable_material_1,R.id.materials_cell_cable_material_2,
		R.id.materials_cell_cable_material_3,R.id.materials_cell_cable_material_4,
		R.id.materials_cell_cable_size_1,R.id.materials_cell_cable_size_2,
		R.id.materials_cell_cable_size_3,R.id.materials_cell_cable_size_4
		};
	
	private final static int[][] cTime = {
		{1,51},{2,51},{3,51},{4,51},{5,51}
	};
	private final static int[] rTime = {
		R.id.time_cell_hours, R.id.time_cell_minutes, R.id.time_cell_complete,
		R.id.time_cell_testing_complete, R.id.time_cell_reason
	};
	
	private final static int[][] cSigImage = {{1,2},{1,10}};


	public ExcelHandler(Activity parent){
		File myDir = new File(root + MainActivity.workingTemplateDir);
		workingTemplateFile = new File(myDir, MainActivity.workingTemplateFileName);
		workingTemplateTemp = new File(myDir, workingTemplateTempFileName);
		mParent = parent;
	}
	
	public int[][][] getCells(String section){
		int[][] cells;
		int[][] ids = new int[1][];
		int[][] sheet = new int[1][];
		int[][][] list = new int[2][][];
		//List<int[][]> list = new ArrayList<int[][]>();
		if(section.equalsIgnoreCase(EXCEL_SECTION_JOB_SETUP)){
			cells = cJobSetupCells;
			ids[0] = rJobSetupIds;
			sheet[0][0] = 1;
		} else if(section.equalsIgnoreCase(EXCEL_SECTION_EQUIPMENT)){
			cells = cEquipmentCells;
			ids[0] = rEquipmentIds;
		} else if(section.equalsIgnoreCase(EXCEL_SECTION_TIME)){
			cells = cTime;
			ids[0] = rTime;
		} else if(section.equalsIgnoreCase(EXCEL_SECTION_MATERIALS_CONTAINMENT)){
			cells = cMaterialsContainment;
			ids[0] = rMaterialsContainment;
		} else if(section.equalsIgnoreCase(EXCEL_SECTION_MATERIALS_LIGHTING)){
			cells = cMaterialsLighting;
			ids[0] = rMaterialsLighting;
		} else if(section.equalsIgnoreCase(EXCEL_SECTION_MATERIALS_POWER)){
			cells = cMaterialsPower;
			ids[0] = rMaterialsPower;
		} else if(section.equalsIgnoreCase(EXCEL_SECTION_MATERIALS_DATA)){
			cells = cMaterialsData;
			ids[0] = rMaterialsData;
		} else if(section.equalsIgnoreCase(EXCEL_SECTION_MATERIALS_CABLE)){
			cells = cMaterialsCable;
			ids[0] = rMaterialsCable;
		} else if(section.equalsIgnoreCase(EXCEL_SECTION_SAFETY_SITE_CONDITIONS)){
			cells = cSafetySiteConditionCells;
			ids[0] = rSafetySiteConditionIds;
		} else if(section.equalsIgnoreCase(EXCEL_SECTION_SAFETY_PPE)){
			cells = cSafetyPPECells;
			ids[0] = rSafetyPPEIds;
		} else if(section.equalsIgnoreCase(EXCEL_SECTION_SAFETY_LOCK_OUT)){
			cells = cSafetyLockOutCells;
			ids[0] = rSafetyLockOutIds;
		} else if(section.equalsIgnoreCase(EXCEL_SECTION_SAFETY_MANUAL_HANDLING)){
			cells = cSafetyManualCells;
			ids[0] = rSafetyManualIds;
		} else if(section.equalsIgnoreCase(EXCEL_SECTION_SAFETY_WORKING_AT_HEIGHT)){
			cells = cSafetyHeightCells;
			ids[0] = rSafetyHeightIds;
		} else {
			return null;
		}
		list[0] = cells;
		list[1] = ids;
		//list.add(cells);
		//list.add(ids);
		return list;
	}

	public void write(String section, View givenView){
			final SaveTask downTask = new SaveTask();
			mProgressDialog = new ProgressDialog(mParent);
			mProgressDialog.setMessage("Saving");
			mProgressDialog.setIndeterminate(true);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mProgressDialog.setCancelable(true);
			mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					downTask.cancel(true);
				}
			});
			PBStore store = new PBStore(section, givenView);
			downTask.execute(store);
	} 

	public View read(LayoutInflater inflater, ViewGroup container, int layout, String section){
		//ArrayList<int[][]> list = getCells(section);
		int[][][] list = getCells(section);
		if(list == null){
			return inflater.inflate(layout, container, false);
		}
		int[][] cells = list[0];
		int[] ids = list[1][0];
		Workbook w = getWorkbook();
		Sheet s = w.getSheet(0);
		View v = inflater.inflate(layout, container, false);
		Cell cell = null;
		PBSpinner spinner = null;
		int columns = s.getColumns();
		Log.v("Test","Columns = " + String.valueOf(columns));
		if(columns > 1){
			for(int i = 0; i < cells.length; i ++){
				cell = s.getCell(cells[i][0], cells[i][1]);
				spinner = (PBSpinner) v.findViewById(ids[i]);
				if(isLabel(cell)){
					spinner.select(cell.getContents());
				}
			}
		}
		return v;
	}

	private boolean isLabel(Cell cell){
		if(cell.getType() == CellType.LABEL){
			return true;
		} else {
			return false;
		}
	}

	private WritableWorkbook getWritableWorkbook(){
		WritableWorkbook w = null;
		try{
			mOriginalWorkingTemplate = Workbook.getWorkbook(workingTemplateFile);
			w = Workbook.createWorkbook(workingTemplateTemp, mOriginalWorkingTemplate);
			mOriginalWorkingTemplate.close();
			mOriginalWorkingTemplate = null;
		} catch(Exception e){
			e.printStackTrace();
		}
		return w;
	}

	private Workbook getWorkbook(){
		Workbook w = null;
		try{
			w = Workbook.getWorkbook(workingTemplateFile);
		} catch(Exception e){

		}
		return w;
	}

	public void saveImage(String fileLoc, String type){
		
		final ImageSaveTask downTask = new ImageSaveTask();
		mImageProgressDialog = new ProgressDialog(mParent);
		mImageProgressDialog.setMessage("Saving image");
		mImageProgressDialog.setIndeterminate(true);
		mImageProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mImageProgressDialog.setCancelable(true);
		mImageProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				downTask.cancel(true);
			}
		});
		downTask.execute(fileLoc,type);
	}
	
	private class ImageSaveTask extends AsyncTask<String, Integer, String>{
		
		@Override
		protected void onPreExecute() {
			mImageProgressDialog.show();
		}
		
		@Override
		protected void onPostExecute(String result) {
			mImageProgressDialog.dismiss();
			if(result == null){
				Toast.makeText(mParent, "Saved..", Toast.LENGTH_SHORT).show();
			}
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			mImageProgressDialog.setIndeterminate(false);
	        mImageProgressDialog.setMax(100);
	        mImageProgressDialog.setProgress(values[0]);
		}

		@Override
		protected String doInBackground(String... params) {
			String fileLoc = params[0];
			String type = params[1];
			int height = 6;
			Log.v("Test","Saving");
			int iType = 0;
			if(type.equalsIgnoreCase(SignatureActivity.SIG_IDENTIFIER_CUST)){
				Log.v("Test","iType = 1");
				iType = 1;
			} else if(type.equalsIgnoreCase(SignatureActivity.SIG_IDENTIFIER_EMP)){
				Log.v("Test","iType = 2");
				iType = 2;
			}
			publishProgress(20);
			WritableWorkbook w = getWritableWorkbook();
			WritableSheet s = w.getSheet(iType);
			WritableImage i = new WritableImage(cSigImage[0][0], cSigImage[0][1], 6, height, new File(fileLoc));
			s.addImage(i);
			WritableCell dateCell = s.getWritableCell(cSigImage[1][0], cSigImage[1][1]);
			Time now = new Time();
			now.setToNow();
			if(isLabel(dateCell)){
				Label l = (Label) dateCell;
				l.setString(now.format("%d-%m-%Y %k:%M"));
			} else {
				Label l = new Label(cSigImage[1][0], cSigImage[1][1],now.format("%d-%m-%Y %k:%M"));
				try {
					s.addCell(l);
				} catch (WriteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
			File myDir = new File(root + MainActivity.workingTemplateDir);
			workingTemplateFile = new File(myDir, MainActivity.workingTemplateFileName);
			workingTemplateTemp = new File(myDir, workingTemplateTempFileName);
			try{
				w.write();
				w.close();

				workingTemplateFile.delete();

				InputStream in = new FileInputStream(workingTemplateTemp);
				OutputStream out = new FileOutputStream(workingTemplateFile);
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
				workingTemplateTemp.delete();
			}catch(Exception e){
				e.printStackTrace();
			}
			
			return null;
		}
		
	}
	
	private class SaveTask extends AsyncTask<PBStore, Integer, String>{
		
		@Override
		protected void onPreExecute() {
			mProgressDialog.show();
		}
		
		@Override
		protected void onPostExecute(String result) {
			mProgressDialog.dismiss();
			if (result != null)
				MainActivity.toastError();
			else
				Toast.makeText(mParent, "Saved..", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		protected String doInBackground(PBStore... pb) {
			
			int[][][] list = getCells(pb[0].getSection());
			publishProgress(5);
			int[][] cells = list[0];
			int[] ids = list[1][0];
			WritableWorkbook w = null;
			try{
				w = getWritableWorkbook();
				publishProgress(10);
				WritableSheet s = w.getSheet(0);
				WritableCell cell = null;
				PBSpinner spinner = null;
				for(int i = 0; i < cells.length; i ++){
					cell = s.getWritableCell(cells[i][0], cells[i][1]);
					spinner = (PBSpinner) pb[0].getGivenView().findViewById(ids[i]);
					if(isLabel(cell)){
						Label l = (Label) cell;
						l.setString(spinner.getString());
					} else {
						Label l = new Label(cells[i][0], cells[i][1],spinner.getString());
						s.addCell(l);
					}
					publishProgress( (40 / cells.length) * i + 10  );
				}
				
			} catch(RowsExceededException e){
				e.printStackTrace();
			} catch (WriteException e) {
				e.printStackTrace();
			}
			
			if(w == null){
				return "ERROR";
			}
			
			
			File myDir = new File(root + MainActivity.workingTemplateDir);
			workingTemplateFile = new File(myDir, MainActivity.workingTemplateFileName);
			workingTemplateTemp = new File(myDir, workingTemplateTempFileName);
			publishProgress(55);
			try{
				w.write();
				w.close();

				workingTemplateFile.delete();

				InputStream in = new FileInputStream(workingTemplateTemp);
				OutputStream out = new FileOutputStream(workingTemplateFile);

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
				workingTemplateTemp.delete();
				publishProgress(100);
			}catch(Exception e){
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
	    protected void onProgressUpdate(Integer... progress) {
	        super.onProgressUpdate(progress);
	        mProgressDialog.setIndeterminate(false);
	        mProgressDialog.setMax(100);
	        mProgressDialog.setProgress(progress[0]);
	    }
		
	}
	
	private class PBStore{
		private String section;
		private View givenView;
		
		public PBStore(String section, View givenView){
			this.setSection(section);
			this.setGivenView(givenView);
		}

		public String getSection() {
			return section;
		}

		public void setSection(String section) {
			this.section = section;
		}

		public View getGivenView() {
			return givenView;
		}

		public void setGivenView(View givenView) {
			this.givenView = givenView;
		}
		
	}
}
