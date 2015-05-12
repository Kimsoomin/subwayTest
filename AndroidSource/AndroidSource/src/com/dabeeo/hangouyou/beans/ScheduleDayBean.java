package com.dabeeo.hangouyou.beans;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class ScheduleDayBean
{
  public int type;
  public ArrayList<SpotBean> spots = new ArrayList<SpotBean>();
  
  
  public void setJSONObject(JSONObject obj)
  {
    try
    {
      if (obj.has("type"))
        type = obj.getInt("type");
      
      if (obj.has("spot"))
      {
        JSONArray spotArray = obj.getJSONArray("spot");
        for (int i = 0; i < spotArray.length(); i++)
        {
          JSONObject spotJSONObject = spotArray.getJSONObject(i);
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
