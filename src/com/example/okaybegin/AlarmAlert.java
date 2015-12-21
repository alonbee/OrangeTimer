package com.example.okaybegin;


import java.io.FileInputStream;
import java.util.Calendar;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;

public class AlarmAlert extends Activity
{
	public String readFileData(String fileName){ 
        String res=""; 
        try{ 
         FileInputStream fin = openFileInput(fileName); 
         int length = fin.available(); 
         byte [] buffer = new byte[length]; 
         fin.read(buffer);     
         res = EncodingUtils.getString(buffer, "UTF-8"); 
         fin.close();     
        } 
        catch(Exception e){ 
         e.printStackTrace(); 
        } 
        return res; 
    }   
  @Override
  protected void onCreate(Bundle savedInstanceState) 
  {
	
    super.onCreate(savedInstanceState);
    String alarmFileName;
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());
    alarmFileName=String.valueOf(calendar.get(Calendar.YEAR))
    		+String.valueOf(calendar.get(Calendar.MONTH)+1)+"-"
    		+String.valueOf(calendar.get(Calendar.DATE))
    		+String.valueOf(calendar.get(Calendar.HOUR_OF_DAY))
    		+String.valueOf(calendar.get(Calendar.MINUTE)+".alarm");
    
    PowerManager pm = (PowerManager) AlarmAlert.this.getSystemService(Context.POWER_SERVICE);
    //点亮屏幕
    WakeLock mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "SimpleTimer");
    mWakeLock.acquire(10000);
    //震动
    Vibrator mVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);//获取振动器
    mVibrator.vibrate(1000);
    new AlertDialog.Builder(AlarmAlert.this)
        .setTitle(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY))+"点"
        		+String.valueOf(calendar.get(Calendar.MINUTE))+"分")
        .setMessage(readFileData(alarmFileName))
        .setPositiveButton("确认",
         new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface dialog, int whichButton)
          {
            AlarmAlert.this.finish();
          }
        })
        .show(); 
  } 
}