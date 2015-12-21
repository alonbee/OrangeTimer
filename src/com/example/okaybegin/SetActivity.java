package com.example.okaybegin;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SetActivity extends Activity 
{
	String ynm,ynp;
	Toast toastQ = null;
	AlertDialog builder = null;
  
	private void showTextToast(String msg,int yoff) {
    if (toastQ == null) {
        toastQ = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT );
    } else {
        toastQ.setText(msg);
    }
    toastQ.setGravity(Gravity.CENTER, 0, yoff); 
    toastQ.show();
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
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set);
		final Button morning = (Button)findViewById(R.id.s_unsure);
		final Button planning = (Button)findViewById(R.id.s_sure);
		final Button sday = (Button)findViewById(R.id.s_sdaycount);
		final Button gday = (Button)findViewById(R.id.s_gdaycount);
		sday.setText(String.valueOf(readFileData("sdaycount").length()));
		gday.setText(String.valueOf(readFileData("gdaycount").length()));
		ynm=readFileData("morning.txt");

		int YN,PL;
		if (ynm.length()<10)
			YN=1;
		else
			YN=0;
		
		if (YN!=0)
		morning.setBackgroundResource(R.drawable.s_sure);
		else
		morning.setBackgroundResource(R.drawable.s_unsure);
		
		morning.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ynm=readFileData("morning.txt");
				int YN;
				if (ynm.length()<10)
					YN=1;
				else
					YN=0;
				if (YN!=0)
				{
					showTextToast("早安功能:关闭",0);
					morning.setBackgroundResource(R.drawable.s_unsure);
					YN=0;
					writeFileDataOver("morning.txt","TheMorningWelcomeOff");
				}
				else
				{
					showTextToast("早安功能:开启",0);
					morning.setBackgroundResource(R.drawable.s_sure);
					YN=1;
					writeFileDataOver("morning.txt","On");
				}
				
			}
		}); 
		ynp=readFileData("planning.txt");
		if (ynp.length()<10)
			PL=1;
		else
			PL=0;
		if (PL!=0)
		planning.setBackgroundResource(R.drawable.s_unsure);
		else
		planning.setBackgroundResource(R.drawable.s_sure);
		planning.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ynp=readFileData("planning.txt");
				int PL;
				if (ynp.length()<10)
					PL=1;
				else
					PL=0;
				if (PL!=0)
				{
					showTextToast("次日规划提醒:开启",0);
					planning.setBackgroundResource(R.drawable.s_sure);
					writeFileDataOver("planning.txt","PrePlanningON");
					PL=0;
				}
				else
				{
					showTextToast("次日规划提醒:关闭",0);
					planning.setBackgroundResource(R.drawable.s_unsure);
					writeFileDataOver("planning.txt","Off");
					PL=1;
				}
				
			}
		}); 
		
		//自定义事件
		final TextView owndesign = (TextView)findViewById(R.id.s_owndesign);
		    owndesign.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
			    LayoutInflater layoutInflater = LayoutInflater.from(SetActivity.this);
				builder = new AlertDialog.Builder(SetActivity.this)
				.setTitle("自定义事件")
				.setView(layoutInflater.inflate(R.layout.onwdesignd, null))
				.setPositiveButton("确认",
				 new DialogInterface.OnClickListener()
        {
			public void onClick(DialogInterface dialog, int whichButton)
			{
				TextView ownEvent =(TextView)builder.findViewById(R.id.editEvent);
				TextView ownTire  =(TextView)builder.findViewById(R.id.editTire);
				String et=String.valueOf(ownEvent.getText());
				String ec=String.valueOf(ownTire.getText());
				if (ec.length()>0&&et.length()>0&&et.indexOf("|")<0)
				{
				int tire=Integer.parseInt(ec);
				if (tire>50)
					tire=50;
				if (tire<-50)
					tire=-50;
				ec=String.valueOf(tire);
				writeFileData("UserEvent.txt","$|"+et+"|0|23|"+ec+"|");
				} 
				dialog.dismiss();
			}
        })
        .setNegativeButton("取消",
				 new DialogInterface.OnClickListener()
        {
			public void onClick(DialogInterface dialog, int whichButton)
			{
				dialog.cancel();
			}
        })
				.show();
			}
		});
		
		final TextView aboutus=(TextView)findViewById(R.id.s_aboutus);
		aboutus.setOnClickListener(new OnClickListener()
		{	
			@Override
			public void onClick(View v)
			{
				showTextToastLong("OrangeTimer开发组"
						+"\n"+"编程:赵毅 田凯"+"\n"+"界面设计:吴驰冕"
						+"\n"+"数据库设计:廖振宇"+"\n"+"项目统筹:刘林东",0);
			}
		});
	}

}