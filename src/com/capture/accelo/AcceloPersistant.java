package com.capture.accelo;

import android.content.Context;
import android.content.SharedPreferences;

public class AcceloPersistant 
{
	private SharedPreferences settings;
	private SharedPreferences.Editor editor;
	
	public String prefRunning = "running";
	public String isMainAppOn = "inMainAppOn";
	public String prefKeydate = "lastupdatetime";
	public String timerLast = "timerAlarm";
	
	public String isConfigDone = "configDone";
	public String isLogEnable = "LogEnable";
	public String iswifiEnable = "wifiEnable";
	public String isIntrvlEnable = "intervalEnable";
	public String isInstantEnable = "instantEnable";
	
	public String urlphpKey = "urlphp";
	public String userIdentKey = "userident";
	public String passphpKey = "passphp";
	
	public long lastLogDate = 0;
	
	public final long curdatetime = System.currentTimeMillis();
	
	public AcceloPersistant(Context context)
	{
		settings = context.getSharedPreferences(AcceloDefines.SHARED_PREFERENCES_FILE_NAME, 0);
		lastLogDate  = settings.getLong(prefKeydate, 0);
 	}
	
	public void putStringValue(String keyString, String keyValue)
	{
    	editor = settings.edit();    	
		editor.putString(keyString, keyValue);
		editor.commit();
	}
	
	public void putIntValue(String keyString, int keyValue)
	{
    	editor = settings.edit();    	
		editor.putInt(keyString, keyValue);
		editor.commit();
	}	
	
	public void putLongValue(String keyString, Long keyValue)
	{
    	editor = settings.edit();    	
		editor.putLong(keyString, keyValue);
		editor.commit();
	}	
	public String getStringValue(String keyString)
	{
		return settings.getString(keyString, "");
	}
	
	public int getIntValue(String keyString)
	{
		return settings.getInt(keyString, 0); 
	}	
	
	public Long getLongValue(String keyString)
	{    	    	
		return settings.getLong(keyString, 0); 
	}	
}
