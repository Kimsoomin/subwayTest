package com.dabeeo.hangouyou.beans;

import java.util.ArrayList;

import org.json.JSONObject;

import android.util.Log;

public class StationBean
{
	public String nameCn;
	public String nameKo;
	public String nameEn;
	public String stationId;
	public String line;
	public double lon = -1, lat = -1;
	
	public String beforeLine, afterLine;
	
	public ArrayList<StationExitBean> exits = new ArrayList<>();
	
	
	//	"exit_locations":{"1":{"latitude":"37.6757282","longitude":"126.7477441"},"2":{"latitude":"37.6757239","longitude":"126.7472398"},"3":{"latitude":"37.6760127","longitude":"126.7471969"},"4":{"latitude":"37.6763523","longitude":"126.7475241"},"5":{"latitude":"37.6764118","longitude":"126.7477870"},"6":{"latitude":"37.6761613","longitude":"126.7479157"}}},
	public void setJSONObject(JSONObject object)
	{
		try
		{
			this.nameCn = object.getString("name");
			this.nameKo = object.getString("name_ko");
			this.nameEn = object.getString("name_en");
			this.stationId = object.getString("id");
			this.line = object.getString("line");
			this.lat = object.getDouble("lat");
			this.lon = object.getDouble("lat");
			
			if (object.has("exit_locations"))
			{
				JSONObject exitObject = object.getJSONObject("exit_locations");
				for (int i = 1; i < 12; i++)
				{
					if (exitObject.has(Integer.toString(i)))
					{
						Log.w("WARN", "!!!!:" + i + "번째 출구있음!");
						StationExitBean bean = new StationExitBean();
						bean.exitTitle = i;
						bean.lat = exitObject.getJSONObject(Integer.toString(i)).getDouble("latitude");
						bean.lon = exitObject.getJSONObject(Integer.toString(i)).getDouble("longitude");
						exits.add(bean);
					}
				}
			}
		} catch (Exception e)
		{
		}
	}
	
	public class StationExitBean
	{
		public int exitTitle;
		public double lat, lon;
		public float distance;
	}
}
