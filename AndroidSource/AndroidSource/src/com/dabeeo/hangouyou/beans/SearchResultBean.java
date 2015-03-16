package com.dabeeo.hangouyou.beans;

import org.json.JSONObject;

public class SearchResultBean
{
  public static final int TYPE_NORMAL = -1;
  public static final int TYPE_PLACE = 0;
  public static final int TYPE_PRODUCT = 1;
  public static final int TYPE_RECOMMEND_SEOUL = 2;
  public static final int TYPE_SCHEDULE = 3;
  public static final int TYPE_PHOTO_LOG = 4;
  
  public String text;
  public boolean isTitle = false;
  public int moreCount = 0;
  public int type = TYPE_NORMAL;
  
  
  public SearchResultBean()
  {
    
  }
  
  
  public SearchResultBean(String json)
  {
    try
    {
      JSONObject obj = new JSONObject(json);
      isTitle = obj.getBoolean("is_title");
      text = obj.getString("text");
      moreCount = obj.getInt("more_count");
      type = obj.getInt("type");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  
  public void addText(String text, int type)
  {
    this.text = text;
    this.isTitle = false;
    this.type = type;
  }
  
  
  private void addTitle(String title, int moreCount)
  {
    this.text = title;
    this.moreCount = moreCount;
    this.isTitle = true;
  }
  
  
  /**
   * 일반적인 제목
   * 
   * @param title
   */
  public void addNormalTitle(String title, int moreCount)
  {
    this.type = TYPE_NORMAL;
    addTitle(title, moreCount);
  }
  
  
  /**
   * 장소 검색결과
   * 
   * @param title
   * @param moreCount
   */
  public void addPlaceTitle(String title, int moreCount)
  {
    this.type = TYPE_PLACE;
    addTitle(title, moreCount);
  }
  
  
  /**
   * 상품 검색결과
   * 
   * @param title
   * @param moreCount
   */
  public void addProductTitle(String title, int moreCount)
  {
    this.type = TYPE_PRODUCT;
    addTitle(title, moreCount);
  }
  
  
  /**
   * 추천서울 검색결과
   * 
   * @param title
   * @param moreCount
   */
  public void addRecommendSeoulTitle(String title, int moreCount)
  {
    this.type = TYPE_RECOMMEND_SEOUL;
    addTitle(title, moreCount);
  }
  
  
  /**
   * 스케쥴 검색결과
   * 
   * @param title
   * @param moreCount
   */
  public void addScheduleTitle(String title, int moreCount)
  {
    this.type = TYPE_SCHEDULE;
    addTitle(title, moreCount);
  }
  
  
  /**
   * 포토로그 검색결과
   * 
   * @param title
   * @param moreCount
   */
  public void addPhotoLogTitle(String title, int moreCount)
  {
    this.type = TYPE_PHOTO_LOG;
    addTitle(title, moreCount);
  }
  
  
  public JSONObject toJsonObject()
  {
    JSONObject result = new JSONObject();
    
    try
    {
      result.put("is_title", isTitle);
      result.put("text", text);
      result.put("type", type);
      result.put("more_count", moreCount);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
    return result;
  }
}
