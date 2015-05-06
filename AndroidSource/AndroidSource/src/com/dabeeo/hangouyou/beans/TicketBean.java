package com.dabeeo.hangouyou.beans;

import org.json.JSONObject;

public class TicketBean
{
  public int idx;
  public String seqCode;
  public String title;
  public int displayPriceWon, displayPriceYuan, priceWon, priceYuan;
  public String discountRate;
  public String fromValidityDate, toValidityDate;
  public String validityCondition, refundCondition;
  public String whereUseIn;
  public String code;
  
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
      
      if (obj.has("display_price_won"))
        displayPriceWon = obj.getInt("display_price_won");
      
      if (obj.has("display_price_yuan"))
        displayPriceYuan = obj.getInt("display_price_yuan");
      
      if (obj.has("price_won"))
        priceWon = obj.getInt("price_won");
      
      if (obj.has("price_yuan"))
        priceYuan = obj.getInt("price_yuan");
      
      if (obj.has("discount_rate"))
        discountRate = obj.getString("discount_rate");
      
      if (obj.has("from_validity_date"))
        fromValidityDate = obj.getString("from_validity_date");
      
      if (obj.has("to_validity_date"))
        toValidityDate = obj.getString("to_validity_date");
      
      if (obj.has("validity_condition"))
        validityCondition = obj.getString("validity_condition");
      
      if (obj.has("refund_condition"))
        refundCondition = obj.getString("refund_condition");
      
      if (obj.has("where_use_in"))
        whereUseIn = obj.getString("where_use_in");
      
      if (obj.has("code"))
        code = obj.getString("code");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
  }
}
