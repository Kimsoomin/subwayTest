package com.dabeeo.hanhayou.beans;

import org.json.JSONObject;

public class PushBean
{
  public String status;
  
  public String title;
  public String url;
  public String image;
  
  public String productId;
  public String productTitle;
  public String productCurrency;
  public String productPriceSale;
  public String productPriceDiscount;
  
  
  public void setJSONObject(JSONObject object)
  {
    try
    {
      if (object.has("status"))
        status = object.getString("status");
      
      if (object.has("push"))
      {
        JSONObject pushObject = object.getJSONObject("push");
        if (pushObject.has("title"))
          title = pushObject.getString("title");
        
        if (pushObject.has("url"))
          url = pushObject.getString("url");
        
        if (pushObject.has("image"))
          image = pushObject.getString("image");
        
        if (pushObject.has("product"))
        {
          JSONObject productObject = pushObject.getJSONObject("product");
          if (productObject.has("idx"))
            productId = productObject.getString("idx");
          
          if (productObject.has("name"))
            productTitle = productObject.getString("name");
          
          if (productObject.has("currency"))
            productCurrency = productObject.getString("currency");
          
          if (productObject.has("priceDiscount"))
            productPriceDiscount = productObject.getString("priceDiscount");
          
          if (productObject.has("priceSale"))
            productPriceSale = productObject.getString("priceSale");
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
