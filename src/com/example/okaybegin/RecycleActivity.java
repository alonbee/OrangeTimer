package com.example.okaybegin;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class RecycleActivity extends Activity 
{
	
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
	


	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recycle);
	 
		final String eventData=readSource();
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
		int year_n=cal.get(Calendar.YEAR);
		int month_n=cal.get(Calendar.MONTH)+1;
		int day_n=cal.get(Calendar.DATE);
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
		int done=0,notdone,orange=0;
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
			if (hour_n*100+min_n>=tasktoday[i].taskhour*100+tasktoday[i].taskmin)
			{
				done++;
				orange=orange+tasktoday[i].getcost();
			}
		} 
		notdone=taskcount-done;
		
        ArrayList<HashMap<String, Object>> listItem= new ArrayList<HashMap<String, Object>>();  
        HashMap<String, Object> bottom = new HashMap<String, Object>();
   //   暂时不用  bottom.put("ItemText","疲劳度:"+String.valueOf(orange));  
   //   同上  listItem.add(bottom);  
   //    bottom.clear();
   //   bottom被移至上方了
 
        int tire=orange/10;
        String aline;
        switch (tire){
        case 0: aline="非常轻松";
        		break;
        case 1: aline="很轻松";
        		break;
        case 2: aline="较轻松";
        		break;
        case 3:
        case 4: aline="有点疲惫";
        		break;
        case 5:
        case 6: aline="较疲惫";
        		break;
        case 7:
        case 8:
        case 9: 
        		if (hour_n>=22||hour_n<=6)
        			aline="非常疲惫，去睡觉吧";
        		else
        			aline="非常疲惫，休息一会吧";
        		break;
        default: if (hour_n>=22||hour_n<=6)
					aline="精疲力竭，睡个好觉吧";
				else
					aline="精疲力竭，好好休息吧";
				break;
        
        }
        bottom.put("ItemText","当前状态:"+aline);
        listItem.add(bottom);
        HashMap<String, Object> top = new HashMap<String, Object>();
        top.put("ItemText","你今天的成就:");
        listItem.add(top);
        for(int i=0;i<done;i++)  
        {  
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemText",tasktoday[i].getname());  
            listItem.add(map); 
        }  

        		
		final ListView listView=(ListView)findViewById(R.id.r_lv);
        SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,//数据源   
                R.layout.r_listitem,//ListItem的XML实现  
                //动态数组与ImageItem对应的子项          
                new String[] {"ItemText"},   
                //ImageItem的XML文件里面的一个ImageView,两个TextView ID  
                new int[] {R.id.ItemText}  
            );  
		listView.setAdapter(listItemAdapter);
	}
 
}