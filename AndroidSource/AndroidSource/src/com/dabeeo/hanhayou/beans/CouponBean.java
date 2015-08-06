package com.dabeeo.hanhayou.beans;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

public class CouponBean
{
  public int idx;
  public String couponIdx;
  public String branchIdx;
  public String scopeIdx;
  public String placeIdx;
  public String number;
  public String brandName;
  public String branchName;
  public String category;
  public String categoryName;
  public Date startDate;
  public Date endDate;
  public boolean isExhaustion = false;
  public boolean isNotimeLimit = false;
  public String title;
  public String couponImageUrl;
  public int distance = -1;
  
  
  public void setJSONObject(JSONObject obj)
  {
    try
    {
      if (obj.has("coupon_idx"))
        couponIdx = obj.getString("coupon_idx");
      
      if (obj.has("branch_idx"))
        branchIdx = obj.getString("branch_idx");
      if (obj.has("scope_idx"))
        scopeIdx = obj.getString("scope_idx");
      if (obj.has("place_idx"))
        placeIdx = obj.getString("place_idx");
      if (obj.has("number"))
        number = obj.getString("number");
      if (obj.has("brand_name"))
        brandName = obj.getString("brand_name");
      if (obj.has("branch_name"))
        branchName = obj.getString("branch_name");
      if (obj.has("category"))
        category = obj.getString("category");
      
      if (obj.has("category_name"))
        categoryName = obj.getString("category_name");
      if (obj.has("start_date"))
      {
        try
        {
          String dateStr = obj.getString("start_date");
          SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
          startDate = format.parse(dateStr);
        }
        catch (Exception e)
        {
        }
      }
      if (obj.has("end_date"))
      {
        try
        {
          String dateStr = obj.getString("end_date");
          SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
          endDate = format.parse(dateStr);
        }
        catch (Exception e)
        {
        }
      }
      
      if (obj.has("is_exhaustion"))
        isExhaustion = obj.getInt("is_exhaustion") == 1;
      if (obj.has("is_notimelimit"))
        isNotimeLimit = obj.getInt("is_notimelimit") == 1;
      
      if (obj.has("title"))
        title = obj.getString("title");
      if (obj.has("coupon_image"))
        couponImageUrl = obj.getString("coupon_image");
      
      if (obj.has("distance"))
        distance = obj.getInt("distance");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
  }
}
