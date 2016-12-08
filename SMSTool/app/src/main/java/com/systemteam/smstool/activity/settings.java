package com.systemteam.smstool.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.systemteam.smstool.R;

public class settings extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref);
	}
} 
