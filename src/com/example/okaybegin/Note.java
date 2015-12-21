package com.example.okaybegin;

import java.sql.Date;

import android.view.View.OnClickListener;

public class Note
{
	private String infor;
	private Date date;
	void note()
	{
		
	}
	void note(String infor,Date date)
	{
		this.infor=infor;
		this.date=date;
	}
	void setInfor(String infor)
	{
		this.infor=infor;
	}
	String getInfor()
	{
		return this.infor;
	}
	Date setDate()
	{
		return this.date;
	}
	public String toString()
	{
		String information=infor+date;
		return information;
	}
	
	
}
