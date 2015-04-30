package com.dabeeo.hangouyou.beans;

import org.json.JSONObject;

public class CouponBean
{
  public int idx;
  public String seqCode;
  public String title;
  public String description;
  public String fromUseableDate, toUseableDate;
  public boolean isUsed;
  
  
  public void setJSONObject(JSONObject obj)
  {
    try
    {
      if (obj.has("seqCode"))
        seqCode = obj.getString("seqCode");
      
      if (obj.has("idx"))
        idx = obj.getInt("idx");
      
      if (obj.has("title"))
        title = obj.getString("title");
      
      if (obj.has("from_useable_date"))
        fromUseableDate = obj.getString("from_useable_date");
      
      if (obj.has("to_useable_date"))
        toUseableDate = obj.getString("to_useable_date");
      
      if (obj.has("description"))
        description = obj.getString("description");
      
      if (obj.has("iss_used"))
        isUsed = obj.getBoolean("is_used");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
  }
}
