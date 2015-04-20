package com.dabeeo.hangouyou.map;

import android.annotation.SuppressLint;
import android.location.Location;

//6. 센서에 대한 업데이트관련 로직을 연결함.
@SuppressLint({ "DefaultLocale", "SimpleDateFormat" })
public class SensorUpdater
{
	SensorUpdater(SensorUpdaterCallback callback)
	{
		super();
		
		m_callback = callback;
	}
	
	public void SetCallback(SensorUpdaterCallback callback)
	{
		m_callback = callback;
	}
	
	public interface SensorUpdaterCallback
	{
		void UpdateDirection(final float fAngle);
		void UpdateLocation(final Location locCur);
		
		//void FinishUpdateSensor();
	}
	
	SensorUpdaterCallback m_callback = null;
	
}
