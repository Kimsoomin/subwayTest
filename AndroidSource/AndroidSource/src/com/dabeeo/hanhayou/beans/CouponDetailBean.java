package com.dabeeo.hanhayou.beans;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

public class CouponDetailBean
{
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
  
  public String info;
  public String condition;
  public String howto;
  public String address;
  public String tel;
  public String notice;
  public double lat;
  public double lng;
  
  public String couponImageUrl;
  public String downloadCouponImage;
  
  public boolean isUse = false;
  public Date useDate;
  public String userSeq;
  
  
  @SuppressLint("SimpleDateFormat")
  public void setCursor(Cursor c)
  {
    try
    {
      couponIdx = c.getString(c.getColumnIndex("coupon_idx"));
      branchIdx = c.getString(c.getColumnIndex("branch_idx"));
      scopeIdx = c.getString(c.getColumnIndex("scope_idx"));
      placeIdx = c.getString(c.getColumnIndex("place_idx"));
      brandName = c.getString(c.getColumnIndex("brand_name"));
      branchName = c.getString(c.getColumnIndex("branch_name"));
      category = c.getString(c.getColumnIndex("category"));
      categoryName = c.getString(c.getColumnIndex("category_name"));
      
      try
      {
        String dateStr = c.getString(c.getColumnIndex("start_date"));
        if (!TextUtils.isEmpty(dateStr))
        {
          SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
          startDate = format.parse(dateStr);
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      
      try
      {
        String dateStr = c.getString(c.getColumnIndex("end_date"));
        if (!TextUtils.isEmpty(dateStr))
        {
          SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
          endDate = format.parse(dateStr);
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      
      isExhaustion = c.getString(c.getColumnIndex("is_exhaustion")).equals("true");
      isNotimeLimit = c.getString(c.getColumnIndex("is_notimelimit")).equals("true");
      title = c.getString(c.getColumnIndex("title"));
      info = c.getString(c.getColumnIndex("info"));
      condition = c.getString(c.getColumnIndex("condition"));
      howto = c.getString(c.getColumnIndex("howto"));
      address = c.getString(c.getColumnIndex("address"));
      tel = c.getString(c.getColumnIndex("tel"));
      notice = c.getString(c.getColumnIndex("notice"));
      try
      {
        lat = Double.parseDouble(c.getString(c.getColumnIndex("lat")));
        lng = Double.parseDouble(c.getString(c.getColumnIndex("lng")));
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      couponImageUrl = c.getString(c.getColumnIndex("coupon_image"));
      downloadCouponImage = c.getString(c.getColumnIndex("download_image"));
      
      isUse = c.getInt(c.getColumnIndex("is_use")) == 1;
      
      try
      {
        String dateStr = c.getString(c.getColumnIndex("useDate"));
        if (!TextUtils.isEmpty(dateStr))
        {
          SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
          Log.w("WARN", "UseDate : " + dateStr);
          useDate = format.parse(dateStr);
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      
      try
      {
        userSeq = c.getString(c.getColumnIndex("userSeq"));
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  
  public JSONObject getJSONObject(Context context)
  {
    JSONObject obj = new JSONObject();
    
    try
    {
      obj.put("coupon_idx", couponIdx);
      obj.put("branch_idx", branchIdx);
      obj.put("scope_idx", scopeIdx);
      obj.put("place_idx", placeIdx);
      obj.put("number", number);
      obj.put("brand_name", brandName);
      obj.put("branch_name", branchName);
      obj.put("category", category);
      obj.put("category_name", categoryName);
      
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
      try
      {
        obj.put("start_date", format.format(startDate));
      }
      catch (Exception e)
      {
      }
      
      try
      {
        obj.put("end_date", format.format(endDate));
      }
      catch (Exception e)
      {
      }
      
      if (isExhaustion)
        obj.put("is_exhaustion", true);
      else
        obj.put("is_exhaustion", false);
      
      if (isNotimeLimit)
        obj.put("is_notimelimit", true);
      else
        obj.put("is_notimelimit", false);
      obj.put("title", title);
      obj.put("info", info);
      obj.put("condition", condition);
      obj.put("howto", howto);
      obj.put("address", address);
      obj.put("tel", tel);
      obj.put("notice", notice);
      obj.put("lat", lat);
      obj.put("lng", lng);
      obj.put("coupon_image", couponImageUrl);
      obj.put("download_image", downloadCouponImage);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return obj;
  }
  
  
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
      
      if (obj.has("info"))
        info = obj.getString("info");
      if (obj.has("condition"))
        condition = obj.getString("condition");
      if (obj.has("notice"))
        notice = obj.getString("notice");
      if (obj.has("howto"))
        howto = obj.getString("howto");
      if (obj.has("address"))
        address = obj.getString("address");
      if (obj.has("tel"))
        tel = obj.getString("tel");
      
      if (obj.has("lat"))
        lat = obj.getDouble("lat");
      if (obj.has("lng"))
        lng = obj.getDouble("lng");
      
      if (obj.has("download_image"))
        downloadCouponImage = obj.getString("download_image");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
  }
}
