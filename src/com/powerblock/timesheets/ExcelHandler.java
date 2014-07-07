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
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ExcelHandler {
	
	public static final String workingTemplateTempFileName = "output.xls";
	private File workingTemplateFile;
	private File workingTemplateTemp;
	private Workbook mOriginalWorkingTemplate;
	
	//Cell coordinates
	private final static int[] cJobType = {1,1};
	private final static int[] cSiteName = {1,2};
	private final static int[] cSiteAddress = {1,3};
	private final static int[] cPIC = {1,4};
	private final static int[] cAPersonnel = {1,5};
	private final static int[][] cJobSetupCells = {cJobType,cSiteName,cSiteAddress, cPIC, cAPersonnel};
	private final static int[] rJobSetupIds = {R.id.job_setup_job_types, R.id.job_setup_site_name, R.id.job_setup_site_address, R.id.job_setup_PIC, R.id.job_setup_personnel};
	
	public ExcelHandler(){
		String root = Environment.getExternalStorageDirectory().toString();
		File myDir = new File(root + MainActivity.workingTemplateDir);
		workingTemplateFile = new File(myDir, MainActivity.workingTemplateFileName);
		workingTemplateTemp = new File(myDir, workingTemplateTempFileName);
	}
	
	public void writeJobSetup(View givenView){
		try{
		WritableWorkbook w = getWritableWorkbook();
		WritableSheet s = w.getSheet(0);
		WritableCell cell = null;
		CustomSpinner spinner = null;
		for(int i = 0; i < cJobSetupCells.length; i ++){
			cell = s.getWritableCell(cJobSetupCells[i][0], cJobSetupCells[i][1]);
			spinner = (CustomSpinner) givenView.findViewById(rJobSetupIds[i]);
			if(isLabel(cell)){
				Label l = (Label) cell;
				l.setString(spinner.getString());
			} else {
				Label l = new Label(cJobSetupCells[i][0], cJobSetupCells[i][1],spinner.getString());
				s.addCell(l);
			}
		}
		saveWritableWorkbook(w);
		} catch(RowsExceededException e){
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public View readJobSetup(LayoutInflater inflater, ViewGroup container){
		Workbook w = getWorkbook();
		Sheet s = w.getSheet(0);
		View v = inflater.inflate(R.layout.job_setup_fragment, container, false);
		Cell cell = null;
		CustomSpinner spinner = null;
		for(int i = 0; i < cJobSetupCells.length; i ++){
			cell = s.getCell(cJobSetupCells[i][0], cJobSetupCells[i][1]);
			spinner = (CustomSpinner) v.findViewById(rJobSetupIds[i]);
			if(isLabel(cell)){
				spinner.select(cell.getContents());
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
			workingTemplateTemp = null;
		}catch(Exception e){
			
		}
	}
	
	public void test(){
		try {
			/*mOriginalWorkingTemplate = Workbook.getWorkbook(workingTemplateFile);
			WritableWorkbook writableWorkingTemplate = Workbook.createWorkbook(workingTemplateTemp, mOriginalWorkingTemplate);
			mOriginalWorkingTemplate.close();
			mOriginalWorkingTemplate = null;*/
			WritableWorkbook writableWorkingTemplate = getWritableWorkbook();
			WritableSheet writableSheet = writableWorkingTemplate.getSheet(0);
			WritableCell cell = writableSheet.getWritableCell(0, 0);
			if(cell.getType() == CellType.LABEL){
				Log.v("Test","Cell 0, 0 is a label");
				Label l = (Label) cell;
				l.setString("Test");
				Log.v("Test", "Cell 0, 0 reads " + cell.getContents());
			} else {
				Log.v("Test","Cell 0, 0 is not a label");
				Label l = new Label(0,0, "Test");
				writableSheet.addCell(l);
			}
			saveWritableWorkbook(writableWorkingTemplate);
		}catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}
	}
	
}
