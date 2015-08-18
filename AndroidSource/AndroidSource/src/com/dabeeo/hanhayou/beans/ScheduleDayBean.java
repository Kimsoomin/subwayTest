package com.dabeeo.hanhayou.beans;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class ScheduleDayBean implements Serializable
{
	/**
   * 
   */
  private static final long serialVersionUID = 1L;
  public int type;
	public String dayDist;
	public String dayTime;
	public ArrayList<SpotBean> spots = new ArrayList<SpotBean>();

	public void setJSONObject(JSONObject obj)
	{
		try
		{
			if (obj.has("type"))
				type = obj.getInt("type");

			if (obj.has("dayDist"))
				dayDist = obj.getString("dayDist");

			if (obj.has("dayTime"))
				dayTime = obj.getString("dayTime");

			if (obj.has("spot"))
			{
				JSONArray spotArray = obj.getJSONArray("spot");
				for (int i = 0; i < spotArray.length(); i++)
				{
					SpotBean spotBean = new SpotBean();
					spotBean.setJSONObject(spotArray.getJSONObject(i));
					spots.add(spotBean);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
