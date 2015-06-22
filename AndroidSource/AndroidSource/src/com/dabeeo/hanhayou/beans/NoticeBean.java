package com.dabeeo.hanhayou.beans;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class NoticeBean
{
  public int isTop;
  public String idx;
  public String url;
  public String title;
  
  public String insertDateString;
  public ArrayList<NoticeContentBean> contents = new ArrayList<NoticeContentBean>();
  
  
  public void setJSONObject(JSONObject obj)
  {
    try
    {
      isTop = obj.getInt("isTop");
      idx = obj.getString("idx");
      url = obj.getString("url");
      title = obj.getString("title");
      insertDateString = obj.getString("insertDate");
      try
      {
        JSONArray contentArr = obj.getJSONArray("contents");
        for (int i = 0; i < contentArr.length(); i++)
        {
          JSONObject object = contentArr.getJSONObject(i);
          NoticeContentBean bean = new NoticeContentBean();
          if (object.has("text"))
          {
            bean.text = object.getString("text");
            contents.add(bean);
          }
          if (object.has("image"))
          {
            bean.isText = false;
            bean.text = object.getString("image");
            contents.add(bean);
          }
        }
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
  
  public class NoticeContentBean
  {
    public String text;
    public boolean isText = true;
  }
}
