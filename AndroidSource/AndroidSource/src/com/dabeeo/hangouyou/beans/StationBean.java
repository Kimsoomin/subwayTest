package com.dabeeo.hangouyou.beans;

import org.json.JSONObject;

public class StationBean
{
  public String nameKo;
  public String stationId;
  public String line;
  
  
  public void setJSONObject(JSONObject object)
  {
    try
    {
      this.nameKo = object.getString("name");
      this.stationId = object.getString("id");
      this.line = object.getString("line");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
