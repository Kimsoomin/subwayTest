package com.dabeeo.hanhayou.beans;

import org.json.JSONObject;

public class PremiumBean
{
  public String idx;
  public String seqCode;
  public int cityIdx;
  public String title;
  public String placeTitle;
  public String categoryId;
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
  public String imageUrl;
  
  
  public void setJSONObject(JSONObject obj)
  {
    try
    {
      if (obj.has("seqCode"))
        seqCode = obj.getString("seqCode");
      
      if (obj.has("idx"))
        idx = obj.getString("idx");
      
      if (obj.has("title"))
        title = obj.getString("title");
      
      if (obj.has("cityIdx"))
        cityIdx = obj.getInt("cityIdx");
      
      if (obj.has("category"))
        categoryId = obj.getString("category");
      
      try
      {
        if (obj.has("lat"))
          lat = obj.getDouble("lat");
        if (obj.has("lng"))
          lng = obj.getDouble("lng");
      }
      catch (Exception e2)
      {
      }
      
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
      
      if (obj.has("mainImage"))
      {
        JSONObject mainImageObj = obj.getJSONObject("mainImage");
        imageUrl = mainImageObj.getString("url");
      }
      
      if (obj.has("placeTitle"))
      {
        placeTitle = obj.getString("placeTitle");
      }else
      {
        placeTitle = "";
      }
      
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
  }
}
