package com.dabeeo.hangouyou.beans;

import org.json.JSONObject;

public class TicketBean
{
  public int idx;
  public String seqCode;
  public String title;
  public int priceWon, priceYuan;
  public String discountRate;
  public String fromUseableDate, toUseableDate;
  
  
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
      
      if (obj.has("price_won"))
        priceWon = obj.getInt("price_won");
      
      if (obj.has("price_yuan"))
        priceYuan = obj.getInt("price_yuan");
      
      if (obj.has("discount_rate"))
        discountRate = obj.getString("discount_rate");
      
      if (obj.has("from_useable_date"))
        fromUseableDate = obj.getString("from_useable_date");
      
      if (obj.has("to_useable_date"))
        toUseableDate = obj.getString("to_useable_date");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
  }
}
