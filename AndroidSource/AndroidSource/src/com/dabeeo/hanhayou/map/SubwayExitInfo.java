package com.dabeeo.hanhayou.map;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;

public class SubwayExitInfo 
{
	public String idx;	
	public String exit;
	public Map<String, ExitInfo> exitList = new HashMap<>();

	public void setSubwayExitJSON(JSONObject object) 
	{
		try 
		{
			Iterator<String> keys = object.keys();
			while (keys.hasNext()) 
			{
				String key = keys.next();
				ExitInfo exitInfo = new ExitInfo();
				exitInfo.exit = key;
				JSONObject data = new JSONObject(object.getString(key));
				exitInfo.lat = Float.parseFloat(data.getString("latitude"));
				exitInfo.lng = Float.parseFloat(data.getString("longitude"));
				exitList.put(exitInfo.exit, exitInfo);
			}
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	public class ExitInfo
	{
		public String exit;
		public float lat, lng;
	}
}
