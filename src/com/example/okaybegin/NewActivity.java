package com.example.okaybegin;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;

import org.apache.http.util.EncodingUtils;

import android.R.string;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewActivity extends Activity
{
	
	boolean alarmyes;
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
    public void writeFileData(String fileName,String message){ 
	       try{ 
	        FileOutputStream fout =openFileOutput(fileName, MODE_APPEND);
	        byte [] bytes = message.getBytes(); 
	        fout.write(bytes); 
	         fout.close(); 
	        } 
	       catch(Exception e){ 
	        e.printStackTrace(); 
	       } 
	   }   
	 public void writeFileDataOver(String fileName,String message){ 
	       try{ 
	        FileOutputStream fout =openFileOutput(fileName, MODE_PRIVATE);
	        byte [] bytes = message.getBytes(); 
	        fout.write(bytes); 
	         fout.close(); 
	        } 
	       catch(Exception e){ 
	        e.printStackTrace(); 
	       } 
	   }   
	 
	    //防止重复toast通知
		Toast toastQ = null;
		private void showTextToast(String msg,int yoff) {
	    if (toastQ == null) {
	        toastQ = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
	    } else {
	        toastQ.setText(msg);
	    }
     toastQ.setGravity(Gravity.CENTER, 0, yoff); 
	    toastQ.show();
		}    
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newnote);
		String tempst;
//		int hour_n, min_n;
		Calendar cal=Calendar.getInstance();
		final int hour_n=cal.get(Calendar.HOUR_OF_DAY);
		final int min_n=cal.get(Calendar.MINUTE);
		final int year_n=cal.get(Calendar.YEAR);
		final int month_n=cal.get(Calendar.MONTH)+1;
		final int day_n=cal.get(Calendar.DATE);
		final Button bnmd=(Button)findViewById(R.id.n_monthday);
		final Button bnhour=(Button)findViewById(R.id.n_hour);
		final Button bnmin=(Button)findViewById(R.id.n_minute);
		final Button bnstart=(Button)findViewById(R.id.n_startbutton);
		final Button alarmyn=(Button)findViewById(R.id.n_setalert);
		final EditText nwrite=(EditText)findViewById(R.id.n_write);
		bnhour.setText(String.valueOf(hour_n));
		bnmin.setText(String.valueOf(min_n));
		bnmd.setText(String.valueOf(month_n)+"-"+String.valueOf(day_n));

		alarmyn.setBackgroundResource(R.drawable.n_no);
		alarmyes=false;
		alarmyn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (alarmyes)
				{
					alarmyes=false;
					alarmyn.setBackgroundResource(R.drawable.n_no);
				}
				else
				{
					alarmyes=true;
					alarmyn.setBackgroundResource(R.drawable.n_yes);
				}
				if (alarmyes)
					showTextToast("提醒功能:开启",0);
				else
					showTextToast("提醒功能:关闭",0);
			}
		});
		bnhour.setOnClickListener(new OnClickListener()
		{	
			@Override
			public void onClick(View v)
			{
			    String numst=(String) bnhour.getText();
			    int i=Integer.parseInt(numst);
			    if (i==23)
			    	i=0;
			    else
			    	i++;
			    numst=String.valueOf(i);
			    bnhour.setText(numst);
			}
		});
		
		bnmin.setOnClickListener(new OnClickListener()
		{	
			@Override
			public void onClick(View v)
			{
				 String numst=(String) bnmin.getText();
				 int i=Integer.parseInt(numst)/10*10;
				 if (i==50)
				    	i=0;
				    else
				    	i=i+10;
				 numst=String.valueOf(i);
				 bnmin.setText(numst);
			}
		});
		
		
		bnmd.setOnClickListener(new OnClickListener()
		{	
			@Override
			public void onClick(View v)
			{

			    int[] dayofmonth={0,31,28,31,30,31,30,31,31,30,31,30,31};
			    int month,day,p;
			    String st=(String) bnmd.getText();
			    p=st.indexOf("-");
			    month=Integer.parseInt(st.substring(0,p));
			    day=Integer.parseInt(st.substring(p+1));
			    if (year_n%4==0) 
			    	dayofmonth[2]++;
			    if (++day>dayofmonth[month])
			    {
			    	day=1;
			    	if (++month>12)
			    		month=1;
			    }
			    String mdst=String.valueOf(month)+"-"+String.valueOf(day);
			    bnmd.setText(mdst);
			}
		}); 
		
		bnstart.setOnClickListener(new OnClickListener()
		{	
			@Override
			public void onClick(View v)
			{
				String st=nwrite.getText().toString();
				if (st.length()<1)
					showTextToast("事件不能为空。",0);
				else if (st.indexOf("|")>=0)
					showTextToast("事件不能含有字符'|'",0);
				else
				{
					nwrite.setText("");
					String fname1=String.valueOf(year_n)+(String) bnmd.getText()
									+".txt";
					String fcontext="|"+st+"|"+(String) bnhour.getText()
							+":"+(String) bnmin.getText()+"| ";
					String hintText="事件添加成功";
					writeFileData(fname1,fcontext);		
				    int month,day,p,hour,min;
				    String stmd=(String) bnmd.getText();
				    p=stmd.indexOf("-");
				    month=Integer.parseInt(stmd.substring(0,p));
				    day=Integer.parseInt(stmd.substring(p+1));
				    hour=Integer.parseInt((String) bnhour.getText());
				    min=Integer.parseInt((String) bnmin.getText());
					//闹钟
					if (alarmyes&&min+hour*100+day*10000+month*1000000
							>min_n+hour_n*100+day_n*10000+month_n*1000000)
					{
						hintText=hintText+"\n提醒时间:"+bnhour.getText()+"点"
						+bnmin.getText()+"分";
						int alarmCount;
						alarmCount=readFileData("alarmCount.act").length()+1;
						writeFileData("alarmCount.act","*");
						if (alarmCount>99)
						{
							alarmCount=1;
							writeFileDataOver("alarmCount.act","*");
						}
		            	String alarmFileName;
			            alarmFileName=String.valueOf(year_n)+(String) bnmd.getText()
			            		+(String) bnhour.getText()+(String) bnmin.getText()
			            		+".alarm";

			            Calendar calendar = Calendar.getInstance();
			            calendar.setTimeInMillis(System.currentTimeMillis());
				        Intent intent = new Intent(NewActivity.this, OneAlarm.class);
				        PendingIntent sender = PendingIntent.getBroadcast(NewActivity.this,
				                   alarmCount, intent, 0);
				       alarmCount++;
			           writeFileData(alarmFileName,st+"\n"); 	
			           calendar.set(Calendar.MONTH, month);
			           calendar.set(Calendar.DATE, day);
			           calendar.set(Calendar.HOUR_OF_DAY,hour);
			           calendar.set(Calendar.MINUTE,min);
			           calendar.set(Calendar.SECOND, 0);
			           AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
			           am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);

						
					}
					showTextToast(hintText,0);		
					
				}
			}
		}); 

	}
}