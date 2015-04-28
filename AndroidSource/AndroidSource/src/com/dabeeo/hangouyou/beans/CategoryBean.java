package com.dabeeo.hangouyou.beans;

import org.json.JSONObject;

public class CategoryBean
{
  public String idx;
  public String name;
  
  
  public void setJSONObject(JSONObject object)
  {
    try
    {
      if (object.has("idx"))
        idx = object.getString("idx");
      if (object.has("name"))
        name = object.getString("name");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
