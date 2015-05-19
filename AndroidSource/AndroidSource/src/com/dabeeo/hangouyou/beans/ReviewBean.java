package com.dabeeo.hangouyou.beans;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;

public class ReviewBean
{
  public int idx;
  public String seqCode;
  public String reviewSeqCode;
  
  public int rate;
  public int likeCount;
  public String ownerUserSeq;
  public String userName;
  
  public String content;
  
  public String gender;
  public String mfidx;
  
  public String insertDateString;
  public Date insertDate;
  public String updateDateString;
  public Date updateDate;
  
  public ArrayList<String> imageUrls = new ArrayList<String>();
  
  
  @SuppressLint("SimpleDateFormat")
  public void setJSONObject(JSONObject obj)
  {
    try
    {
      if (obj.has("idx"))
        idx = obj.getInt("idx");
      
      if (obj.has("seqCode"))
        seqCode = obj.getString("seqCode");
      if (obj.has("reviewSeqCode"))
        reviewSeqCode = obj.getString("reviewSeqCode");
      
      if (obj.has("rate"))
        rate = obj.getInt("rate");
      if (obj.has("likeCount"))
        likeCount = obj.getInt("likeCount");
      
      if (obj.has("ownerUserSeq"))
        ownerUserSeq = obj.getString("ownerUserSeq");
      if (obj.has("userName"))
        userName = obj.getString("userName");
      
      if (obj.has("contents"))
        content = obj.getString("contents");
      if (obj.has("gender"))
        gender = obj.getString("gender");
      if (obj.has("mfidx"))
        mfidx = obj.getString("mfidx");
      
      if (obj.has("insertDate"))
      {
        insertDateString = obj.getString("insertDate");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        insertDate = format.parse(insertDateString);
      }
      
      if (obj.has("updateDate"))
      {
        updateDateString = obj.getString("updateDate");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        updateDate = format.parse(updateDateString);
      }
      
      if (obj.has("image"))
      {
        JSONArray imageArray = obj.getJSONArray("image");
        for (int i = 0; i < imageArray.length(); i++)
        {
          imageUrls.add(imageArray.getJSONObject(i).getString("url"));
        }
      }
      
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
