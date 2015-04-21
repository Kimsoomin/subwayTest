package com.dabeeo.hangouyou.beans;

import org.json.JSONObject;

public class StationBean
{
  public String nameCn;
  public String nameKo;
  public String stationId;
  public String line;
  public double lon = -1, lat = -1;
  
  
  public void setJSONObject(JSONObject object)
  {
    try
    {
      this.nameCn = object.getString("name");
      this.nameKo = object.getString("name_ko");
      this.stationId = object.getString("id");
      this.line = object.getString("line");
      this.lat = object.getDouble("lat");
      this.lon = object.getDouble("lat");
    }
    catch (Exception e)
    {
    }
  }
}
