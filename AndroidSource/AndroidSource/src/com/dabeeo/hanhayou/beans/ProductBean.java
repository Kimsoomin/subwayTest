package com.dabeeo.hanhayou.beans;

import org.json.JSONObject;

import com.dabeeo.hanhayou.map.BlinkingCommon;

public class ProductBean
{
  public String currencyConvert;
  public String id;
  public String name;
  public String currency;
  public String priceDiscount;
  public String priceSale;
  public String saleRate;
  public String origin;
  public String manufacture;
  public String imageAlt;
  public String imageTitle;
  public String imageUrl;
  public boolean isWished = false;
  public String categoryId;
  public int rate;
  public boolean productNull = false;
  
  public void setJSONObject(JSONObject obj)
  {
    try
    {
      if(obj.has("currencyConvert")) 
        currencyConvert = obj.getString("currencyConvert");
      
      if(obj.has("id"))
      {
        id = obj.getString("id");
        if(id.equals("null"))
        {
          productNull = true;
        }else
          productNull = false;
      }
      
      if(obj.has("name"))
        name = obj.getString("name");
      
      if(obj.has("currency"))
        currency = obj.getString("currency");
      
      if(obj.has("priceDiscount"))
        priceDiscount = obj.getString("priceDiscount");
      
      if(obj.has("priceSale"))
        priceSale = obj.getString("priceSale");
      
      if(obj.has("saleRate"))
        saleRate = obj.getString("saleRate");
      
      if(obj.has("origin"))
        origin = obj.getString("origin");
      
      if(obj.has("manufacture"))
        manufacture = obj.getString("manufacture");
      
      if(obj.has("imageAlt"))
        imageAlt = obj.getString("imageAlt");
      
      if(obj.has("imageTitle"))
        imageTitle = obj.getString("imageTitle");
      
      if(obj.has("imageUrl"))
        imageUrl = obj.getString("imageUrl");
      
      if(obj.has("isWished"))
        isWished = obj.getInt("isWished") != 0;
      
      if(obj.has("categoryId"))
        categoryId = obj.getString("categoryId");
      
      if(obj.has("rate"))
        rate = obj.getInt("rate");
    }
    catch (Exception e)
    {
      BlinkingCommon.smlLibPrintException("TrendKorea", "error : " + e);
    }
    
  }
}
