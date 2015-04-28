package com.dabeeo.hangouyou.managers;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.dabeeo.hangouyou.IntroActivity;
import com.dabeeo.hangouyou.beans.StationBean;
import com.dabeeo.hangouyou.map.MapPlaceDataManager;

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
    
    ArrayList<String> afterArray = new ArrayList<String>();
    for (int i = 0; i < names.size(); i++)
    {
      boolean isContain = false;
      for (int j = 0; j < afterArray.size(); j++)
      {
        if (afterArray.get(j).equals(names.get(i)))
          isContain = true;
      }
      
      if (!isContain)
        afterArray.add(names.get(i));
    }
    
    return afterArray;
  }
  
  
  public ArrayList<String> getAllSubwayCnNames()
  {
    ArrayList<String> names = new ArrayList<String>();
    for (int i = 0; i < stations.size(); i++)
    {
      names.add(stations.get(i).nameCn);
    }
    
    ArrayList<String> afterArray = new ArrayList<String>();
    for (int i = 0; i < names.size(); i++)
    {
      boolean isContain = false;
      for (int j = 0; j < afterArray.size(); j++)
      {
        if (afterArray.get(j).equals(names.get(i)))
          isContain = true;
      }
      
      if (!isContain)
        afterArray.add(names.get(i));
    }
    return afterArray;
  }
  
  
  public String getStationId(String stationName)
  {
    ArrayList<StationBean> sameNameStations = new ArrayList<StationBean>();
    String stationId = "";
    for (int i = 0; i < stations.size(); i++)
    {
      if (stations.get(i).nameKo.equals(stationName))
        sameNameStations.add(stations.get(i));
    }
    
    if (sameNameStations.size() == 1)
      return sameNameStations.get(1).stationId;
    else
    {
      for (int i = 0; i < sameNameStations.size(); i++)
      {
        if (TextUtils.isEmpty(sameNameStations.get(i).stationId))
          stationId = sameNameStations.get(i).stationId;
        
        if (stationId.length() < sameNameStations.get(i).stationId.length())
          stationId = sameNameStations.get(i).stationId;
      }
    }
    return stationId;
  }
  
  
  public int getSubwayLineResourceId(String line)
  {
    Log.w("WARN", "line: " + line);
    int resourceId = context.getResources().getIdentifier("drawable/icon_subway_line1", null, context.getPackageName());
    try
    {
      if (line.equals("2"))
        resourceId = context.getResources().getIdentifier("drawable/icon_subway_line2", null, context.getPackageName());
      else if (line.equals("3"))
        resourceId = context.getResources().getIdentifier("drawable/icon_subway_line3", null, context.getPackageName());
      else if (line.equals("4"))
        resourceId = context.getResources().getIdentifier("drawable/icon_subway_line4", null, context.getPackageName());
      else if (line.equals("5"))
        resourceId = context.getResources().getIdentifier("drawable/icon_subway_line5", null, context.getPackageName());
      else if (line.equals("6"))
        resourceId = context.getResources().getIdentifier("drawable/icon_subway_line6", null, context.getPackageName());
      else if (line.equals("7"))
        resourceId = context.getResources().getIdentifier("drawable/icon_subway_line7", null, context.getPackageName());
      else if (line.equals("8"))
        resourceId = context.getResources().getIdentifier("drawable/icon_subway_line8", null, context.getPackageName());
      else if (line.equals("9"))
        resourceId = context.getResources().getIdentifier("drawable/icon_subway_line9", null, context.getPackageName());
      else if (line.equals("공항철도"))
        resourceId = context.getResources().getIdentifier("drawable/icon_subway_linea", null, context.getPackageName());
      else if (line.equals("분당선"))
        resourceId = context.getResources().getIdentifier("drawable/icon_subway_lineb", null, context.getPackageName());
      else if (line.equals("에버라인"))
        resourceId = context.getResources().getIdentifier("drawable/icon_subway_lineever", null, context.getPackageName());
      else if (line.equals("경춘선"))
        resourceId = context.getResources().getIdentifier("drawable/icon_subway_linegc", null, context.getPackageName());
      else if (line.equals("경의중앙선"))
        resourceId = context.getResources().getIdentifier("drawable/icon_subway_linegj", null, context.getPackageName());
      else if (line.equals("인천1호선"))
        resourceId = context.getResources().getIdentifier("drawable/icon_subway_lineinc1", null, context.getPackageName());
      else if (line.equals("신분당선"))
        resourceId = context.getResources().getIdentifier("drawable/icon_subway_linesb", null, context.getPackageName());
      else if (line.equals("수인선"))
        resourceId = context.getResources().getIdentifier("drawable/icon_subway_linesuin", null, context.getPackageName());
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return resourceId;
  }
}
