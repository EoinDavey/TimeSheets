package com.powerblock.timesheets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

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
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.powerblock.timesheets.signatures.SignatureActivity;
import com.powerblock.timesheets.signatures.SignatureView;

public class ExcelHandler {

	public static final String workingTemplateTempFileName = "output.xls";
	private File workingTemplateFile;
	private File workingTemplateTemp;
	private static final String root = Environment.getExternalStorageDirectory().toString();
	private static File custSig = new File(root, SignatureView.custSigLoc);
	private static File empSig = new File(root, SignatureView.empSigLoc);
	private Workbook mOriginalWorkingTemplate;
	private static File[] fSigFiles = {custSig, empSig};

	//Section constants
	public static final String EXCEL_SECTION_JOB_SETUP = "JobSetup";
	public static final String EXCEL_SECTION_EQUIPMENT = "Equipment";
	public static final String EXCEL_SECTION_MATERIALS = "Materials";
	public static final String EXCEL_SECTION_TESTING = "Testing";
	public static final String EXCEL_SECTION_TIME = "Time";
	public static final String EXCEL_SECTION_MATERIALS_LIGHTING = "Lighting";
	public static final String EXCEL_SECTION_MATERIALS_POWER = "Power";
	public static final String EXCEL_SECTION_MATERIALS_DATA = "Data";
	public static final String EXCEL_SECTION_MATERIALS_CONTAINMENT = "Containment";
	public static final String EXCEL_SECTION_MATERIALS_CABLE = "Cable";

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
	
	private final static int[] cSigImage = {1,2};


	public ExcelHandler(){
		File myDir = new File(root + MainActivity.workingTemplateDir);
		workingTemplateFile = new File(myDir, MainActivity.workingTemplateFileName);
		workingTemplateTemp = new File(myDir, workingTemplateTempFileName);
	}
	
	public ArrayList<int[][]> getCells(String section){
		int[][] cells;
		int[][] ids = new int[1][];
		List<int[][]> list = new ArrayList<int[][]>();
		if(section.equalsIgnoreCase(EXCEL_SECTION_JOB_SETUP)){
			cells = cJobSetupCells;
			ids[0] = rJobSetupIds;
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
		} else {
			return null;
		}
		list.add(cells);
		list.add(ids);
		return (ArrayList<int[][]>) list;
	}

	public void write(String section, View givenView){
		ArrayList<int[][]> list = getCells(section);
		int[][] cells = list.get(0);
		int[] ids = list.get(1)[0];
		try{
			WritableWorkbook w = getWritableWorkbook();
			WritableSheet s = w.getSheet(0);
			WritableCell cell = null;
			PBSpinner spinner = null;
			for(int i = 0; i < cells.length; i ++){
				cell = s.getWritableCell(cells[i][0], cells[i][1]);
				spinner = (PBSpinner) givenView.findViewById(ids[i]);
				if(isLabel(cell)){
					Label l = (Label) cell;
					l.setString(spinner.getString());
				} else {
					Label l = new Label(cells[i][0], cells[i][1],spinner.getString());
					s.addCell(l);
				}
			}
			saveWritableWorkbook(w);
			} catch(RowsExceededException e){
				e.printStackTrace();
			} catch (WriteException e) {
				e.printStackTrace();
			}
	}

	/*public View readJobSetup(LayoutInflater inflater, ViewGroup container){
		Workbook w = getWorkbook();
		Sheet s = w.getSheet(0);
		View v = inflater.inflate(R.layout.job_setup_fragment, container, false);
		Cell cell = null;
		CustomSpinner spinner = null;
		int columns = s.getColumns();
		Log.v("Test","Columns = " + String.valueOf(columns));
		if(columns > 1){
			for(int i = 0; i < cJobSetupCells.length; i ++){
				cell = s.getCell(cJobSetupCells[i][0], cJobSetupCells[i][1]);
				spinner = (CustomSpinner) v.findViewById(rJobSetupIds[i]);
				if(isLabel(cell)){
					spinner.select(cell.getContents());
				}
			}
		}
		return v;
	}*/

	public View read(LayoutInflater inflater, ViewGroup container, int layout, String section){
		ArrayList<int[][]> list = getCells(section);
		int[][] cells = list.get(0);
		int[] ids = list.get(1)[0];
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

	private void saveWritableWorkbook(WritableWorkbook w){
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
	}

	public void saveSignature(String fileLoc, String type){
		Log.v("Test","Saving");
		int iType = 0;
		if(type.equalsIgnoreCase(SignatureActivity.SIG_IDENTIFIER_CUST)){
			Log.v("Test","iType = 1");
			iType = 1;
		} else if(type.equalsIgnoreCase(SignatureActivity.SIG_IDENTIFIER_EMP)){
			Log.v("Test","iType = 2");
			iType = 2;
		}
		WritableWorkbook w = getWritableWorkbook();
		WritableSheet s = w.getSheet(iType);
		Log.v("Test","Image = " + fSigFiles[iType - 1].toString());
		WritableImage i = new WritableImage(cSigImage[0], cSigImage[1], 6, 6, fSigFiles[iType - 1]);
		s.addImage(i);
		saveWritableWorkbook(w);
	}
}
