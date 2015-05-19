package com.dabeeo.hangouyou.beans;

import org.json.JSONObject;

public class PlaceDetailBean
{
  public String idx;
  public String seqCode;
  public int cityIdx;
  
  public int ownerUserSeq;
  public String userName;
  public String gender;
  public String mfidx;
  
  public int categoryId;
  
  public String title;
  public String address;
  public String businessHours;
  public String priceInfo;
  public String trafficInfo;
  public String homepage;
  public String contact;
  public String contents;
  
  public double lat, lng;
  public String tag;
  
  public int popularCount = 0;
  public int rate = 0;
  public int likeCount = 0;
  public int reviewCount = 0;
  public int bookmarkCount = 0;
  public int shareCount = 0;
  
  public int isLiked;
  public int isBookmarked;
  
  public String updateDateString;
  
  
  public void setJSONObject(JSONObject obj)
  {
    try
    {
      if (obj.has("idx"))
        idx = obj.getString("idx");
      
      if (obj.has("ownerUserSeq"))
        ownerUserSeq = obj.getInt("ownerUserSeq");
      if (obj.has("seqCode"))
        seqCode = obj.getString("seqCode");
      
      if (obj.has("cityIdx"))
        cityIdx = obj.getInt("cityIdx");
      
      if (obj.has("userName"))
        userName = obj.getString("userName");
      if (obj.has("gender"))
        gender = obj.getString("gender");
      if (obj.has("mfidx"))
        mfidx = obj.getString("mfidx");
      
      if (obj.has("category"))
        categoryId = obj.getInt("category");
      
      if (obj.has("title"))
        title = obj.getString("title");
      
      if (obj.has("address"))
        address = obj.getString("address");
      if (obj.has("businessHours"))
        businessHours = obj.getString("businessHours");
      if (obj.has("priceInfo"))
        priceInfo = obj.getString("priceInfo");
      if (obj.has("trafficInfo"))
        trafficInfo = obj.getString("trafficInfo");
      if (obj.has("homepage"))
        homepage = obj.getString("homepage");
      if (obj.has("contact"))
        contact = obj.getString("contact");
      if (obj.has("contents"))
        contents = obj.getString("contents");
      
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
      
      if (obj.has("isLiked"))
        isLiked = obj.getInt("isLiked");
      if (obj.has("isBookmarked"))
        isBookmarked = obj.getInt("isBookmarked");
      
      if (obj.has("updateDate"))
        updateDateString = obj.getString("updateDate");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
  }
}
