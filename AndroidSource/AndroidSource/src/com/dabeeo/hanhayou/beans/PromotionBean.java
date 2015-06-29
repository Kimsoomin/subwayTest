package com.dabeeo.hanhayou.beans;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class PromotionBean
{
  public String idx;
  public String title;
  public String url;
  public String insertDateString;
  public String text;
  public String imageUrl;
  
  
  public void setJSONArray(JSONArray arr)
  {
    
    try
    {
      JSONObject obj = null;
      obj = arr.getJSONObject(0);
      if (obj.has("idx"))
        idx = obj.getString("idx");
      if (obj.has("title"))
        title = obj.getString("title");
      if (obj.has("url"))
        url = obj.getString("url");
      if (obj.has("insertDate"))
        insertDateString = obj.getString("insertDate");
      if (obj.has("contents"))
      {
        try
        {
          JSONArray contentArr = obj.getJSONArray("contents");
          text = contentArr.getJSONObject(0).getString("text");
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
      
      obj = arr.getJSONObject(1);
      if (obj.has("image"))
      {
        imageUrl = obj.getJSONObject("image").getString("url");
      }
      
      Log.w("WARN", "Promotion Content text : " + text);
      Log.w("WARN", "Promotion imageUrl text : " + imageUrl);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
