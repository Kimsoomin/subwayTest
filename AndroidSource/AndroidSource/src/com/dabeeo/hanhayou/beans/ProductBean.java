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
  
  public void setJSONObject(JSONObject obj)
  {
    try
    {
     
    }
    catch (Exception e)
    {
      BlinkingCommon.smlLibPrintException("TrendKorea", "error : " + e);
    }
    
  }
}
