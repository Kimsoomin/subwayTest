package com.dabeeo.hanhayou.beans;

import org.json.JSONObject;

import com.dabeeo.hanhayou.map.BlinkingCommon;

public class TrendKoreaBean
{
  public String idx;
  public String title;
  public String content;
  public String imageUrl;
  
  public void setJSONObject(JSONObject obj)
  {
    try
    {
      if(obj.has("idx"))
        idx = obj.getString("idx");
      
      if(obj.has("title"))
        title = obj.getString("title");
      
      if(obj.has("content"))
        content = obj.getString("content");
      
      if (obj.has("image"))
      {
        JSONObject imageObj = obj.getJSONObject("image");
        imageUrl = imageObj.getString("url");
      }
        
    }
    catch (Exception e)
    {
      BlinkingCommon.smlLibPrintException("TrendKorea", "error : " + e);
    }
    
  }
}
