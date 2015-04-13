package com.dabeeo.hangouyou.beans;

import org.json.JSONObject;

public class PlaceBean
{
  public int idx;
  public String seqCode;
  public int cityIdx;
  public String title;
  public int categoryId;
  public double lat, lng;
  public int popularCount = 0;
  public int rate = 0;
  public int likeCount = 0;
  public int reviewCount = 0;
  public int bookmarkCount = 0;
  public int shareCount = 0;
  public int userSeq;
  public String userName;
  public String gender;
  public String mfidx;
  public double area;
  
  
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
      
      if (obj.has("cityIdx"))
        cityIdx = obj.getInt("cityIdx");
      
      if (obj.has("category"))
        categoryId = obj.getInt("category");
      
      if (obj.has("lat"))
        lat = obj.getDouble("lat");
      if (obj.has("lng"))
        lng = obj.getDouble("lng");
      
      if (obj.has("popular"))
        popularCount = obj.getInt("popular");
      
      if (obj.has("rate"))
        rate = obj.getInt("rate");
      if (obj.has("likeCount"))
        likeCount = obj.getInt("likeCount");
      if (obj.has("reviewCount"))
        reviewCount = obj.getInt("reviewCount");
      
      if (obj.has("reviewCount"))
        reviewCount = obj.getInt("reviewCount");
      if (obj.has("bookmarkCount"))
        bookmarkCount = obj.getInt("bookmarkCount");
      if (obj.has("shareCount"))
        shareCount = obj.getInt("shareCount");
      if (obj.has("userSeq"))
        userSeq = obj.getInt("userSeq");
      
      if (obj.has("userName"))
        userName = obj.getString("userName");
      if (obj.has("gender"))
        gender = obj.getString("gender");
      if (obj.has("mfidx"))
        mfidx = obj.getString("mfidx");
      
      if (obj.has("area"))
        area = obj.getDouble("reviewCount");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
  }
}
