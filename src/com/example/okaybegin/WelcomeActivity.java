package com.example.okaybegin;

import java.security.PublicKey;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class WelcomeActivity extends Activity
{
	private String TAG="Welcome";
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		Intent intent=getIntent();
		setResult(01,intent);
		finish();
	}
	
}
