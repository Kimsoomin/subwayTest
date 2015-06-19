package com.dabeeo.hanhayou.beans;

import android.annotation.SuppressLint;
import android.database.Cursor;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class ScheduleBean
{
  public String idx;
  public String planCode;
  public String title;
  
  public String startDateString;
  public Date startDate;
  public String endDateString;
  public Date endDate;
  public String insertDateString;
  public Date insertDate;
  public String updateDateString;
  public Date updateDate;
  
  public int likeCount = 0;
  public int reviewCount = 0;
  public int bookmarkCount = 0;
  public int shareCount = 0;
  public int rate = 0;
  public int ownerUserSeq;
  public String userName;
  public String gender;
  public String mfidx;
  public int dayCount = 0;
  public String distance;
  public String budgetTotal;
  public String imageUrl;
  
  //로컬에 저장하지 않아도 됨 삭제를 위한 임시 flag 
  public boolean isChecked = false;
  
  
  @SuppressLint("SimpleDateFormat")
  public void setJSONObject(JSONObject obj)
  {
    try
    {
      if (obj.has("idx"))
        idx = obj.getString("idx");
      
      if (obj.has("planCode"))
        planCode = obj.getString("planCode");
      
      if (obj.has("title"))
        title = obj.getString("title");
      
      try
      {
        if (obj.has("likeCount"))
          likeCount = obj.getInt("likeCount");
        if (obj.has("reviewCount"))
          reviewCount = obj.getInt("reviewCount");
        if (obj.has("bookmarkCount"))
          bookmarkCount = obj.getInt("bookmarkCount");
        if (obj.has("shareCount"))
          shareCount = obj.getInt("shareCount");
        if (obj.has("rate"))
          rate = obj.getInt("rate");
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      
      if (obj.has("ownerUserSeq"))
        ownerUserSeq = obj.getInt("ownerUserSeq");
      
      if (obj.has("userName"))
        userName = obj.getString("userName");
      if (obj.has("gender"))
        gender = obj.getString("gender");
      if (obj.has("mfidx"))
        mfidx = obj.getString("mfidx");
      if (obj.has("dayCount"))
        dayCount = obj.getInt("dayCount");
      if (obj.has("distance"))
        distance = obj.getString("distance");
      if (obj.has("budgetTotal"))
        budgetTotal = obj.getString("budgetTotal");
      if (obj.has("image"))
      {
        JSONObject imageObj = obj.getJSONObject("image");
        imageUrl = imageObj.getString("url");
      }
      
      if (obj.has("startDate"))
      {
        try
        {
          startDateString = obj.getString("startDate");
          SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
          startDate = format.parse(startDateString);
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
      
      if (obj.has("endDate"))
      {
        try
        {
          endDateString = obj.getString("endDate");
          SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
          endDate = format.parse(endDateString);
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
      
      if (obj.has("insertDate"))
      {
        try
        {
          insertDateString = obj.getString("insertDate");
          SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
          insertDate = format.parse(insertDateString);
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
      
      if (obj.has("updateDate"))
      {
        try
        {
          updateDateString = obj.getString("updateDate");
          SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
          updateDate = format.parse(updateDateString);
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
      
    }
    catch (JSONException e)
    {
      e.printStackTrace();
    }
  }
  
  
  public void setCursor(Cursor c)
  {
    try
    {
      idx = c.getString(c.getColumnIndex("idx"));
      planCode = c.getString(c.getColumnIndex("planCode"));
      title = c.getString(c.getColumnIndex("title"));
      startDateString = c.getString(c.getColumnIndex("startDate"));
      endDateString = c.getString(c.getColumnIndex("endDate"));
      insertDateString = c.getString(c.getColumnIndex("insertDate"));
      updateDateString = c.getString(c.getColumnIndex("updateDate"));
      likeCount = c.getInt(c.getColumnIndex("likeCount"));
      reviewCount = c.getInt(c.getColumnIndex("reviewCount"));
      bookmarkCount = c.getInt(c.getColumnIndex("bookmarkCount"));
      shareCount = c.getInt(c.getColumnIndex("shareCount"));
      rate = c.getInt(c.getColumnIndex("rate"));
      userName = c.getString(c.getColumnIndex("userName"));
      gender = c.getString(c.getColumnIndex("gender"));
      mfidx = c.getString(c.getColumnIndex("mfidx"));
      dayCount = c.getInt(c.getColumnIndex("dayCount"));
      distance = c.getString(c.getColumnIndex("distance"));
      budgetTotal = c.getString(c.getColumnIndex("budgetTotal"));
//      budget1 = c.getString(c.getColumnIndex("budget1"));
//      budget2 = c.getString(c.getColumnIndex("budget2"));
//      budget3 = c.getString(c.getColumnIndex("budget3"));
//      days = c.getString(c.getColumnIndex("days"));
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
