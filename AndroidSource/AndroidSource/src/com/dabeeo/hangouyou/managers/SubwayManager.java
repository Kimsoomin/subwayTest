package com.dabeeo.hangouyou.managers;

import java.util.ArrayList;

import android.content.Context;

import com.dabeeo.hangouyou.beans.StationBean;

public class SubwayManager
{
  private static SubwayManager instance = null;
  private Context context;
  public ArrayList<StationBean> stations = new ArrayList<StationBean>();
  
  
  public static SubwayManager getInstance(Context context)
  {
    if (instance == null)
    {
      synchronized (SubwayManager.class)
      {
        if (instance == null)
          instance = new SubwayManager(context);
      }
    }
    return instance;
  }
  
  
  private SubwayManager(Context context)
  {
    this.context = context;
  }
  
  
  public double getLatitudeWithSubwayId(String stationId)
  {
    StationBean bean = null;
    for (int i = 0; i < stations.size(); i++)
    {
      if (stations.get(i).stationId.equals(stationId))
        bean = stations.get(i);
    }
    
    if (bean == null)
      return -1;
    else
      return bean.lat;
  }
  
  
  public double getLongitudeWithSubwayId(String stationId)
  {
    StationBean bean = null;
    for (int i = 0; i < stations.size(); i++)
    {
      if (stations.get(i).stationId.equals(stationId))
        bean = stations.get(i);
    }
    
    if (bean == null)
      return -1;
    else
      return bean.lon;
  }
  
  
  public StationBean findStation(String stationId)
  {
    StationBean bean = null;
    for (int i = 0; i < stations.size(); i++)
    {
      if (stations.get(i).stationId.equals(stationId))
        bean = stations.get(i);
    }
    
    return bean;
  }
  
  
  public ArrayList<String> getAllSubwayNames()
  {
    ArrayList<String> names = new ArrayList<String>();
    for (int i = 0; i < stations.size(); i++)
    {
      names.add(stations.get(i).nameKo);
      
    }
    return names;
  }
  
  
  public String getStationId(String stationName)
  {
    String stationId = "";
    for (int i = 0; i < stations.size(); i++)
    {
      if (stations.get(i).nameKo.equals(stationName))
        stationId = stations.get(i).stationId;
    }
    return stationId;
  }
  
  
  public int getSubwayLineResourceId(String line)
  {
    return 0;
  }
}
