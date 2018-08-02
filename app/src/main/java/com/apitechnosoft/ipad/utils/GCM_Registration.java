package com.apitechnosoft.ipad.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class GCM_Registration {
	
	GoogleCloudMessaging gcm;
    public static String regid = "";
    String PROJECT_NUMBER = null;
    
    String isActive = "1";
    String isDeleted = "0";
    String deviceType = "Android";
    String empId = "";

        //CustomApplicationContext custAppContex;

	public void getRegId(final Context context){
		//PROJECT_NUMBER = context.getResources().getString(R.string.gcm_project_number);
        PROJECT_NUMBER = "78644343604";
		
		//custAppContex = (CustomApplicationContext) context.getApplicationContext();
		//empId = custAppContex.getEmpId();
		
        new AsyncTask<Void, Void, String>() {
        	
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    //gcm = new GoogleCloudMessaging();
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(PROJECT_NUMBER);
                    msg = "Device registered, registration ID=" + regid;
                    
                    RegisterWithServer(regid);
                    
                    Log.i("GCM",  msg);

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.v("RegId",msg + "\n");
            }
        }.execute(null, null, null);


    }
	
	public int RegisterWithServer(String regId){
		try { 
			
			String saveCallPlanner = "http://125.21.88.202:8029/DeviceRegistration/DeviceRegistration.svc/RMS_OrgEmployeeDeviceRegistration";
			saveCallPlanner += "?employeeId="+empId+"&deviceId="+regId+"&modelNumber=NA&emeiNumber=NA&simNumber=NA&deviceType=Android&isActive=true&isDeleted=false";
            // Replace it with your own WCF service path
            URL json = new URL(saveCallPlanner);
            URLConnection jc = json.openConnection();
            
            //Log.v("URL",accountNameServiceUrl);
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
            
            String line = reader.readLine();
            Log.v("line",line);
            
            JSONObject jsonResponse = new JSONObject(line);
            String notes = jsonResponse.getString("d");
            if(notes.equals("true")){
            	return 1;
            }
            else{
            	return 0;
            }
        } 
        catch(Exception e){
        	e.printStackTrace();
        	return 0;
            //Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
	}
}
