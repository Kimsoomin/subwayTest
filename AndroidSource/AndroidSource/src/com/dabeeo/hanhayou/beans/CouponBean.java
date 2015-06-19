package com.dabeeo.hanhayou.beans;

import org.json.JSONObject;

public class CouponBean
{
  public int idx;
  public String seqCode;
  public String title;
  public String description;
  public String fromValidityDate, toValidityDate, validityCondition, whereUseIn;
  public boolean isUsed;
  public String howToUse, instruction;
  public String couponNumber;
  
  
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
      
      if (obj.has("from_validity_date"))
        fromValidityDate = obj.getString("from_validity_date");
      
      if (obj.has("to_validity_date"))
        toValidityDate = obj.getString("to_validity_date");
      
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
