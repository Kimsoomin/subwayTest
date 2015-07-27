package com.dabeeo.hanhayou.beans;

import org.json.JSONObject;

import com.dabeeo.hanhayou.map.BlinkingCommon;

public class PopularWishBean
{
  public String id;
  public String name;
  
  public void setJSONObject(JSONObject obj)
  {
    try
    {
      if(obj.has("id"))
        id = obj.getString("id");
      
      if(obj.has("name"))
        name = obj.getString("name");
    }
    catch (Exception e)
    {
      BlinkingCommon.smlLibPrintException("TrendKorea", "error : " + e);
    }
    
  }
}
