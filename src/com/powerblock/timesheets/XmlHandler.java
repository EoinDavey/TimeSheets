package com.powerblock.timesheets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class XmlHandler {
	private Context mContext;
	private File xmlFile;
	private HashMap<String,String[]> mStringsMap;
	
	public XmlHandler(Context c){
		mContext = c;
		mStringsMap = new HashMap<String, String[]>();
		getFile();
		fillMap();
	}
	
	public void updateSystem(){
		mStringsMap.clear();
		fillMap();
	}
	
	private String[] parseListItem(XmlPullParser x){
		ArrayList<String> list = new ArrayList<String>();
		try {
			int eventType = x.next();
			while(eventType!=XmlPullParser.END_DOCUMENT){
				if(eventType != XmlPullParser.START_TAG){
					if(eventType == XmlPullParser.END_TAG && x.getName().equalsIgnoreCase("List")){
						return list.toArray(new String[list.size()]);
					}
					eventType = x.next();
					continue;
				}
				String name = x.getName();
				if(name.equals("item")){
					x.next();
					list.add(x.getText());
					Log.v("Test","Xml item = " + x.getText());
				}
				eventType = x.next();
			}
		} catch (XmlPullParserException | IOException e) {
			e.printStackTrace();
		}
		return list.toArray(new String[list.size()]);
	}
	
	private void fillMap(){
		XmlPullParser x = getFile();
		try {
			int eventType = x.getEventType();
			while(eventType != XmlPullParser.END_DOCUMENT){
				if(eventType == XmlPullParser.START_DOCUMENT){
					Log.v("Test","Xml: Document begins");
				}
				if(eventType != XmlPullParser.START_TAG){
					eventType = x.next();
					continue;
				}

				String name = x.getName();
				if(name.equalsIgnoreCase("List") && eventType == XmlPullParser.START_TAG){
					String attr = x.getAttributeValue(0);
					Log.v("Test","Xml: attr = " + attr);
					String[] strings = parseListItem(x);
					addToMap(attr, strings);
				}
				eventType = x.next();
			}
		} catch (XmlPullParserException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private void addToMap(String key, String[] strings){
		StringBuffer buffer = new StringBuffer("adding to map: key = " + key + " values = ");
		for(int i = 0; i < strings.length; i++){
			buffer.append(strings[i]);
		}
		Log.v("Test",buffer.toString());
		mStringsMap.put(key, strings);
	}
	
	public String[] getStrings(String tag){
		return mStringsMap.get(tag);
	}
	
	public XmlPullParser getFile(){
		if(xmlFile == null){
			String root = Environment.getExternalStorageDirectory().toString();
			File dir = new File(root + MainActivity.workingTemplateDir);
			xmlFile = new File(dir,"current.xml");
			if(!dir.exists()){
				dir.mkdirs();
			}
			if(!xmlFile.exists()){
				InputStream in = null;
				OutputStream out = null;
				try{
					in = mContext.getResources().openRawResource(R.raw.initial_strings);
					out = new FileOutputStream(xmlFile);
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
					e.printStackTrace();
				}
			}
		}
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser xpp = factory.newPullParser();
			InputStream in = new FileInputStream(xmlFile);
			xpp.setInput(in, null);
			return xpp;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
