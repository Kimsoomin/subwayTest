package com.dabeeo.hangouyou.beans;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ScheduleDetailBean
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
  public String budget1;
  public String budget2;
  public String budget3;
  
  public ArrayList<ScheduleDayBean> days = new ArrayList<ScheduleDayBean>();
  
  
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
      try
      {
        if (obj.has("dayCount"))
          dayCount = obj.getInt("dayCount");
      }
      catch (Exception e)
      {
        
      }
      if (obj.has("distance"))
        distance = obj.getString("distance");
      if (obj.has("budgetTotal"))
        budgetTotal = obj.getString("budgetTotal");
      if (obj.has("budget1"))
        budget1 = obj.getString("budget1");
      if (obj.has("budget2"))
        budget2 = obj.getString("budget2");
      if (obj.has("budget3"))
        budget3 = obj.getString("budget3");
      
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
      
      if (obj.has("days"))
      {
        days.clear();
        JSONArray daysJSONArray = obj.getJSONArray("days");
        for (int i = 0; i < daysJSONArray.length(); i++)
        {
          ScheduleDayBean detailBean = new ScheduleDayBean();
          detailBean.setJSONObject(daysJSONArray.getJSONObject(i));
          days.add(detailBean);
        }
      }
    }
    catch (JSONException e)
    {
      e.printStackTrace();
    }
  }
}
