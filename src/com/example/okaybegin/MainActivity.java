package com.example.okaybegin;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Handler;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TimePickerDialog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;  

public class MainActivity extends Activity
{
	  	TextView setTime1;
	  	Button mButton1;
	  	String time1String = null;
	    Calendar c=Calendar.getInstance();
	    class TaskContent{
		private int taskhour,taskmin,taskcost;
		private String taskname;
		public void setname(String name)
		{
			this.taskname=name;
		}
		public void sethour(int hour)
		{
			this.taskhour=hour;
		}
		public void setmin(int min)
		{
			this.taskmin=min;
		}
		public void setcost(int cost)
		{
			this.taskcost=cost;
		}
		public String getname()
		{
			return this.taskname;
		}
		public int gethour()
		{
			return this.taskhour;
		}
		public int getmin()
		{
			return this.taskmin;
		}
		public int getcost()
		{
			return this.taskcost;
		}
	}
	    //防止重复toast通知
		Toast toastQ = null;
		private void showTextToast(String msg,int yoff) {
	    if (toastQ == null) {
	        toastQ = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT );
	    } else {
	        toastQ.setText(msg);
	    }
        toastQ.setGravity(Gravity.CENTER, 0, yoff); 
	    toastQ.show();
		}    
		Toast toastLong = null;
		private void showTextToastLong(String msg,int yoff) {
	    if (toastLong == null) {
	        toastLong = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG );
	    } else {
	        toastLong.setText(msg);
	    }
        toastLong.setGravity(Gravity.CENTER, 0, yoff); 
	    toastLong.show();
		} 
	public String readSource() {
		  Resources res = this.getResources();
		  InputStream in = null;
		  BufferedReader br = null;
		  StringBuffer sb = new StringBuffer();
		  sb.append("");
		  try {
		   in = res.openRawResource(R.raw.tdata);
		   String str;
		   br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		   while ((str = br.readLine()) != null) {
		    sb.append(str);
		    sb.append("\n");
		   }
		  } catch (NotFoundException e) {
			   Toast.makeText(this, "文本文件不存在", 100).show();
			   e.printStackTrace();
			  } catch (UnsupportedEncodingException e) {
			   Toast.makeText(this, "文本编码出现异常", 100).show();
			   e.printStackTrace();
			  } catch (IOException e) {
			   Toast.makeText(this, "文件读取错误", 100).show();
			   e.printStackTrace();
			  } finally {
		   try {
		    if (in != null) {
		     in.close();
		    }
		    if (br != null) {
		     br.close();
		    }
		   } catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		   }
		  }
		  return sb.toString();
		}
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{ 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final String eventData=readFileData("UserEvent.txt")+readSource();
		final Button button=(Button)findViewById(R.id.add);
		button.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent=new Intent();
				intent.setClass(MainActivity.this,NewActivity.class);
				startActivity(intent);
			}
		});
		final Button button1=(Button)findViewById(R.id.trashcan);
		button1.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent=new Intent();
				intent.setClass(MainActivity.this,RecycleActivity.class);
				startActivity(intent);
			}
		});
		final Button button2=(Button)findViewById(R.id.set);
		button2.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent=new Intent();
				intent.setClass(MainActivity.this,SetActivity.class);
				startActivity(intent);
			}
		});
		final TaskContent tasktoday[]=new TaskContent[1000];
		String etask[]= new String[1900];
		int eindex[]= new int[1900];
		TaskContent tmp=new TaskContent();
		String tc,sttime;
		int elength,ecount=0,taskcount=0,t=0,minp=0,es=0,et=0,time1,time2,cost;
		boolean notend=true;
	
		//数据库定位建立
		es=eventData.indexOf("$");
		elength=eventData.length();
		while (es>=0&&es<elength-8)
		{
			es=es+2;	
			et=eventData.indexOf("|",es);
			etask[ecount]=eventData.substring(es, et);
			eindex[ecount]=es;
			ecount++;
			es=eventData.indexOf("$",et);
		} 
		Calendar cal=Calendar.getInstance();
		int hour_n=cal.get(Calendar.HOUR_OF_DAY);
		int min_n=cal.get(Calendar.MINUTE);
		final int year_n=cal.get(Calendar.YEAR);
		final int month_n=cal.get(Calendar.MONTH)+1;
		final int day_n=cal.get(Calendar.DATE);
		//将当日事件读入
		tc=readFileData(String.valueOf(year_n)+String.valueOf(month_n)
				+"-"+String.valueOf(day_n)+".txt");
		int s=tc.indexOf('|',t);
		while (s>=0)
		{
			tasktoday[taskcount]=new TaskContent();
			t=tc.indexOf('|',s+1);
			tasktoday[taskcount].setname(tc.substring(s+1,t));
			s=t;
			t=tc.indexOf(':',s+1);
			tasktoday[taskcount].sethour(Integer.parseInt(tc.substring(s+1,t)));
			s=t;
			t=tc.indexOf('|',s+1);
			tasktoday[taskcount].setmin(Integer.parseInt(tc.substring(s+1,t))
					+tasktoday[taskcount].gethour()*100);
			s=tc.indexOf('|', t+1);
			//疲劳值对应
			for (int i=0;i<ecount;i++)
			{
				es=tasktoday[taskcount].getname().indexOf(etask[i]);
				if (es>=0) 
				{
						es=eindex[i];
						break;
				}
			}
			if (es<0)
			tasktoday[taskcount].setcost(10);
			else
			{
				notend=true;
				et=eventData.indexOf("|", es);
				es=eventData.indexOf("$",et);
				sttime=eventData.substring(et,es);
				es=1;
				while (notend){
					et=sttime.indexOf("|",es);
					time1=Integer.valueOf(sttime.substring(es,et));
					es=et+1;
					et=sttime.indexOf("|",es);
					time2=Integer.valueOf(sttime.substring(es,et));
					es=et+1;
					et=sttime.indexOf("|",es);
					cost=Integer.valueOf(sttime.substring(es,et));
					if (tasktoday[taskcount].gethour()>=time1&&
							tasktoday[taskcount].gethour()<=time2)
					{
						tasktoday[taskcount].setcost(cost);
						notend=false;
					}
					es=et+1;
					if (sttime.indexOf("|",es)<0)
						notend=false;
				}
			}
            taskcount++;   
		} 
		int done=0,notdone,orange=0,orangeT=0;
		for (int i=0;i<taskcount;i++)
		{
			minp=i;
			for (int j=i+1;j<taskcount;j++)
				if (tasktoday[minp].getmin()>tasktoday[j].getmin())
				  minp=j;
			if (i!=minp)
			{
				tmp=tasktoday[minp];
				tasktoday[minp]=tasktoday[i];
				tasktoday[i]=tmp;
			}
			tasktoday[i].setmin(tasktoday[i].getmin()%100);
			if (hour_n*100+min_n>=tasktoday[i].gethour()*100+tasktoday[i].getmin())
			{
				done++;
				orange=orange+tasktoday[i].getcost();
			}
			orangeT=orangeT+tasktoday[i].getcost();
		} 
	    int[] dayofmonth={0,31,28,31,30,31,30,31,31,30,31,30,31};
	    int tmonth=month_n,tday=day_n,tyear=year_n;
	    if (tyear%4==0) 
	    	dayofmonth[2]++;
	    if (++tday>dayofmonth[tmonth])
	    {
	    	tday=1;
	    	if (++tmonth>12)
	    	{
	    		tmonth=1;
	    		tyear++;
	    	}
	    }
	    //使用天数计数
	    if (readFileData(String.valueOf(year_n)+
				String.valueOf(month_n)
				+String.valueOf(day_n)+".sone").length()<5)
			{
	    		writeFileData("sdaycount","*");
	    		writeFileData(String.valueOf(year_n)+
				String.valueOf(month_n)
				+String.valueOf(day_n)+".sone","TodayDone");
			}
	    //疲劳天数计数
	    if (orangeT>=70&&readFileData(String.valueOf(year_n)+
				String.valueOf(month_n)
				+String.valueOf(day_n)+".gone").length()<5)
			{
	    		writeFileData("gdaycount","*");
	    		writeFileData(String.valueOf(year_n)+
				String.valueOf(month_n)
				+String.valueOf(day_n)+".gone","TodayDone");
			}
	    	//起始提醒
			if (hour_n>=6&&hour_n<12&&taskcount==0&&readFileData("morning.txt").length()<10)
			showTextToast("新的一天开始了，规划下今天的日程吧",0);
			//次日提醒
			if (hour_n>=22&&readFileData("planning.txt").length()>=10
			&&readFileData(String.valueOf(tyear)+String.valueOf(tmonth)
					+"-"+String.valueOf(tday)+".txt").length()<3)
			{
		    showTextToast("睡前规划下明天的日程吧",0);
			}
			
		notdone=taskcount-done;
		final TextView donetv=(TextView)findViewById(R.id.done);
		final TextView notdonetv=(TextView)findViewById(R.id.undone);
		final TextView orangevalue=(TextView)findViewById(R.id.numberuptrashcan);
		donetv.setText(String.valueOf(done));
		notdonetv.setText(String.valueOf(notdone));
		orangevalue.setText(String.valueOf(orange));
		String aline;
        final ArrayList<HashMap<String, Object>> listItem= new ArrayList<HashMap<String, Object>>();  
        for(int i=0;i<taskcount;i++)  
        {  
            HashMap<String, Object> map = new HashMap<String, Object>();
            aline=tasktoday[i].getname();      
            int lennow=aline.length();
            if (lennow>7)
            {
            	lennow=8;
            	aline=aline.substring(0,8)+"..";
            }
            int[] spacen={36,35,31,27,23,19,15,11,4};
            for (int j=0;j<spacen[lennow];j++)
            	aline=aline+" ";
            aline=aline+String.valueOf(tasktoday[i].gethour())+':';
            if (tasktoday[i].getmin()<10)
            	aline=aline+"0";
            aline=aline+String.valueOf(tasktoday[i].getmin());
            map.put("ItemText",aline);  
            listItem.add(map); 
        }  


		final ListView listView=(ListView)findViewById(R.id.lv);
        final SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,//数据源   
                R.layout.listitem,//ListItem的XML实现  
                //动态数组与ImageItem对应的子项          
                new String[] {"ItemText"},   
                //ImageItem的XML文件里面的一个ImageView,两个TextView ID  
                new int[] {R.id.ItemText}  
            );  
		listView.setAdapter(listItemAdapter);
		listView.setOnItemLongClickListener(new OnItemLongClickListener(){
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
			  //刷新当前疲劳值，done,undone
				Calendar caltmp=Calendar.getInstance();
				int hournow=caltmp.get(Calendar.HOUR_OF_DAY);
				int minnow=caltmp.get(Calendar.MINUTE);
				if (hournow*100+minnow>tasktoday[arg2].gethour()*100
						+tasktoday[arg2].getmin())
				{
					int donei=Integer.parseInt((String) donetv.getText())-1;
					donetv.setText(String.valueOf(donei));
					int orangenew=Integer.parseInt((String) orangevalue.getText());
					orangenew=orangenew-tasktoday[arg2].getcost();
					orangevalue.setText(String.valueOf(orangenew));
				}
				else
				{
					int notdonei=Integer.parseInt((String) notdonetv.getText())-1;
					notdonetv.setText(String.valueOf(notdonei));
				}
				
			  //移除此行
			   listItem.remove(arg2);
			   TaskContent theTask;
			   int tStart,fs,ft=0;
			   String ChangedToday=readFileData(String.valueOf(year_n)+String.valueOf(month_n)
						+"-"+String.valueOf(day_n)+".txt");
			   fs=ChangedToday.indexOf('|',ft);
				while (fs>=0)
				{
					tStart=fs;
					theTask=new TaskContent();
					ft=ChangedToday.indexOf('|',fs+1);
					theTask.setname(ChangedToday.substring(fs+1,ft));
					fs=ft;
					ft=ChangedToday.indexOf(':',fs+1);
					theTask.sethour(Integer.parseInt(ChangedToday.substring(fs+1,ft)));
					fs=ft;
					ft=ChangedToday.indexOf('|',fs+1);
					theTask.setmin(Integer.parseInt(ChangedToday.substring(fs+1,ft)));
					fs=ChangedToday.indexOf('|', ft+1);
					if (theTask.getname().equals(tasktoday[arg2].getname())
						&&theTask.gethour()==tasktoday[arg2].gethour()
						&&theTask.getmin()==tasktoday[arg2].getmin())
					{
						int nextSpace=ChangedToday.indexOf(" ",tStart)+1;
						String newTaskData=ChangedToday.substring(0,tStart);
						if (nextSpace>0&&nextSpace<ChangedToday.length()-3)
						newTaskData=newTaskData+ChangedToday.substring(nextSpace);
						writeFileDataOver(String.valueOf(year_n)+String.valueOf(month_n)
								+"-"+String.valueOf(day_n)+".txt",newTaskData);
						break;
					}
				}
			   listItemAdapter.notifyDataSetChanged();	
	           Vibrator mVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);//获取振动器
	           mVibrator.vibrate(500);        
				return true;
			}  
	         
	        });  
        listView.setOnItemClickListener(new OnItemClickListener() {  
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
                    long arg3) {  
            	String definev;
            	if (tasktoday[arg2].getcost()<0)
            		definev="为您恢复精力";
            	else
            		definev="增加您的疲劳";
            	int yoff=-120+arg2*40;
            	if (yoff>120) yoff=120;
            	definev=tasktoday[arg2].getname()+definev+
            			String.valueOf(Math.abs(tasktoday[arg2].getcost()))+"点";
            	showTextToast(definev,yoff);
            }  

        });
	}


	
	protected void onRestart() { 
		super.onRestart();   
		final String eventData=readFileData("UserEvent.txt")+readSource();
		final TaskContent tasktoday[]=new TaskContent[1000];
		String etask[]= new String[1900];
		int eindex[]= new int[1900];
		TaskContent tmp=new TaskContent();
		String tc,sttime;
		int elength,ecount=0,taskcount=0,t=0,minp=0,es=0,et=0,time1,time2,cost;
		boolean notend=true;
	
		//数据库定位建立
		es=eventData.indexOf("$");
		elength=eventData.length();
		while (es>=0&&es<elength-8)
		{
			es=es+2;	
			et=eventData.indexOf("|",es);
			etask[ecount]=eventData.substring(es, et);
			eindex[ecount]=es;
			ecount++;
			es=eventData.indexOf("$",et);
		} 
		Calendar cal=Calendar.getInstance();
		int hour_n=cal.get(Calendar.HOUR_OF_DAY);
		int min_n=cal.get(Calendar.MINUTE);
		final int year_n=cal.get(Calendar.YEAR);
		final int month_n=cal.get(Calendar.MONTH)+1;
		final int day_n=cal.get(Calendar.DATE);
		//将当日事件读入
		tc=readFileData(String.valueOf(year_n)+String.valueOf(month_n)
				+"-"+String.valueOf(day_n)+".txt");
		int s=tc.indexOf('|',t);
		while (s>=0)
		{
			tasktoday[taskcount]=new TaskContent();
			t=tc.indexOf('|',s+1);
			tasktoday[taskcount].setname(tc.substring(s+1,t));
			s=t;
			t=tc.indexOf(':',s+1);
			tasktoday[taskcount].sethour(Integer.parseInt(tc.substring(s+1,t)));
			s=t;
			t=tc.indexOf('|',s+1);
			tasktoday[taskcount].setmin(Integer.parseInt(tc.substring(s+1,t))
					+tasktoday[taskcount].gethour()*100);
			s=tc.indexOf('|', t+1);
			//疲劳值对应
			for (int i=0;i<ecount;i++)
			{
				es=tasktoday[taskcount].getname().indexOf(etask[i]);
				if (es>=0) 
				{
						es=eindex[i];
						break;
				}
			}
			if (es<0)
			tasktoday[taskcount].setcost(10);
			else
			{
				notend=true;
				et=eventData.indexOf("|", es);
				es=eventData.indexOf("$",et);
				sttime=eventData.substring(et,es);
				es=1;
				while (notend){
					et=sttime.indexOf("|",es);
					time1=Integer.valueOf(sttime.substring(es,et));
					es=et+1;
					et=sttime.indexOf("|",es);
					time2=Integer.valueOf(sttime.substring(es,et));
					es=et+1;
					et=sttime.indexOf("|",es);
					cost=Integer.valueOf(sttime.substring(es,et));
					if (tasktoday[taskcount].gethour()>=time1&&
							tasktoday[taskcount].gethour()<=time2)
					{
						tasktoday[taskcount].setcost(cost);
						notend=false;
					}
					es=et+1;
					if (sttime.indexOf("|",es)<0)
						notend=false;
				}
			}
            taskcount++;   
		} 
		int done=0,notdone,orange=0,orangeT=0;
		boolean clocknotset=true;
		for (int i=0;i<taskcount;i++)
		{
			minp=i;
			for (int j=i+1;j<taskcount;j++)
				if (tasktoday[minp].getmin()>tasktoday[j].getmin())
				  minp=j;
			if (i!=minp)
			{
				tmp=tasktoday[minp];
				tasktoday[minp]=tasktoday[i];
				tasktoday[i]=tmp;
			}
			tasktoday[i].setmin(tasktoday[i].getmin()%100);
			if (hour_n*100+min_n>=tasktoday[i].gethour()*100+tasktoday[i].getmin())
			{
				done++;
				orange=orange+tasktoday[i].getcost();
			}
			orangeT=orangeT+tasktoday[i].getcost();
			if (clocknotset&&orangeT>=70)
			{
				//疲劳值到达70的闹钟
				clocknotset=false;
		    	String alarmFileName;
		        Intent intent = new Intent(MainActivity.this, OneAlarm.class);
		        PendingIntent sender = PendingIntent.getBroadcast(MainActivity.this,
		               0, intent, 0);
		        // We want the alarm to go off 30 seconds from now.
		        Calendar calendar = Calendar.getInstance();
		        calendar.setTimeInMillis(System.currentTimeMillis());
		        if (tasktoday[i].taskhour*100+tasktoday[i].taskmin
		        		>calendar.get(Calendar.HOUR_OF_DAY)*100
		        		+calendar.get(Calendar.MINUTE))
		        {
		        	int minpp=tasktoday[i].getmin()+1;
		        alarmFileName=String.valueOf(year_n)+String.valueOf(month_n)+"-"
		        		+String.valueOf(day_n)+String.valueOf(tasktoday[i].gethour())
		        		+String.valueOf(minpp)+".alarm";
		       writeFileDataOver(alarmFileName,"你已经非常疲惫了，快去休息吧"); 	
		       calendar.set(Calendar.HOUR_OF_DAY, tasktoday[i].gethour());
		       calendar.set(Calendar.MINUTE, minpp);
		       calendar.set(Calendar.SECOND, 0);
		       AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
		       am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
		        }
			}
		} 
		notdone=taskcount-done;
		

       
		final TextView donetv=(TextView)findViewById(R.id.done);
		final TextView notdonetv=(TextView)findViewById(R.id.undone);
		final TextView orangevalue=(TextView)findViewById(R.id.numberuptrashcan);
		donetv.setText(String.valueOf(done));
		notdonetv.setText(String.valueOf(notdone));
		orangevalue.setText(String.valueOf(orange));
		String aline;
        final ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();  
        for(int i=0;i<taskcount;i++)  
        {  
            HashMap<String, Object> map = new HashMap<String, Object>();
            aline=tasktoday[i].getname();      
            int lennow=aline.length();
            if (lennow>7)
            {
            	lennow=8;
            	aline=aline.substring(0,8)+"..";
            }
            int[] spacen={36,35,31,27,23,19,15,11,4};
            for (int j=0;j<spacen[lennow];j++)
            	aline=aline+" ";
            aline=aline+String.valueOf(tasktoday[i].gethour())+':';
            if (tasktoday[i].getmin()<10)
            	aline=aline+"0";
            aline=aline+String.valueOf(tasktoday[i].getmin());
            map.put("ItemText",aline);  
            listItem.add(map); 
        }  
		final ListView listView=(ListView)findViewById(R.id.lv);
//		ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.list, R.layout.listitem);
        final SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,//数据源   
                R.layout.listitem,//ListItem的XML实现  
                //动态数组与ImageItem对应的子项          
                new String[] {"ItemText"},   
                //ImageItem的XML文件里面的一个ImageView,两个TextView ID  
                new int[] {R.id.ItemText}  
            );   
		listView.setAdapter(listItemAdapter);
		listView.setOnItemLongClickListener(new OnItemLongClickListener(){
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
			  //刷新当前疲劳值，done,undone
				Calendar caltmp=Calendar.getInstance();
				int hournow=caltmp.get(Calendar.HOUR_OF_DAY);
				int minnow=caltmp.get(Calendar.MINUTE);
				if (hournow*100+minnow>tasktoday[arg2].gethour()*100
						+tasktoday[arg2].getmin())
				{
					int donei=Integer.parseInt((String) donetv.getText())-1;
					donetv.setText(String.valueOf(donei));
					int orangenew=Integer.parseInt((String) orangevalue.getText());
					orangenew=orangenew-tasktoday[arg2].getcost();
					orangevalue.setText(String.valueOf(orangenew));
				}
				else
				{
					int notdonei=Integer.parseInt((String) notdonetv.getText())-1;
					notdonetv.setText(String.valueOf(notdonei));
				}
				
			  //移除此行
			   listItem.remove(arg2);
			   TaskContent theTask;
			   int tStart,fs,ft=0;
			   String ChangedToday=readFileData(String.valueOf(year_n)+String.valueOf(month_n)
						+"-"+String.valueOf(day_n)+".txt");
			   fs=ChangedToday.indexOf('|',ft);
				while (fs>=0)
				{
					tStart=fs;
					theTask=new TaskContent();
					ft=ChangedToday.indexOf('|',fs+1);
					theTask.setname(ChangedToday.substring(fs+1,ft));
					fs=ft;
					ft=ChangedToday.indexOf(':',fs+1);
					theTask.sethour(Integer.parseInt(ChangedToday.substring(fs+1,ft)));
					fs=ft;
					ft=ChangedToday.indexOf('|',fs+1);
					theTask.setmin(Integer.parseInt(ChangedToday.substring(fs+1,ft)));
					fs=ChangedToday.indexOf('|', ft+1);
					if (theTask.getname().equals(tasktoday[arg2].getname())
						&&theTask.gethour()==tasktoday[arg2].gethour()
						&&theTask.getmin()==tasktoday[arg2].getmin())
					{
						int nextSpace=ChangedToday.indexOf(" ",tStart)+1;
						String newTaskData=ChangedToday.substring(0,tStart);
						if (nextSpace>0&&nextSpace<ChangedToday.length()-3)
						newTaskData=newTaskData+ChangedToday.substring(nextSpace);
						writeFileDataOver(String.valueOf(year_n)+String.valueOf(month_n)
								+"-"+String.valueOf(day_n)+".txt",newTaskData);
						break;
					}
				}
			   listItemAdapter.notifyDataSetChanged();	
	           Vibrator mVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);//获取振动器
	           mVibrator.vibrate(500);        
				return true;
			}  
	         
	        });  
        listView.setOnItemClickListener(new OnItemClickListener() {  
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
                    long arg3) {  
            	String definev;
            	if (tasktoday[arg2].getcost()<0)
            		definev="为您恢复精力";
            	else
            		definev="增加您的疲劳";
            	int yoff=-120+arg2*40;
            	if (yoff>120) yoff=120;
            	definev=tasktoday[arg2].getname()+definev+
            			String.valueOf(Math.abs(tasktoday[arg2].getcost()))+"点";
            	showTextToast(definev,yoff);
            }  

        });  
	}

}