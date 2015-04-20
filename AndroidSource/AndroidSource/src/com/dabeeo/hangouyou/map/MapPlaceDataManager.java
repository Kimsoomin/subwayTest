package com.dabeeo.hangouyou.map;

import java.util.List;

import android.content.Context;

import com.dabeeo.hangouyou.beans.PlaceBean;
import com.dabeeo.hangouyou.beans.StationBean;

public class MapPlaceDataManager
{
  private volatile static MapPlaceDataManager instance;
  private DatabaseManager db;
  private Context context;
  
  
  public static MapPlaceDataManager getInstance(Context context)
  {
    if (instance == null)
    {
      synchronized (MapPlaceDataManager.class)
      {
        if (instance == null)
          instance = new MapPlaceDataManager(context);
      }
    }
    return instance;
  }
  
  
  private MapPlaceDataManager(Context context)
  {
    this.context = context;
    db = new DatabaseManager(context);
  }
  
  
  public void initDatabase()
  {
    db.open();
  }
  
  
  public void deleteDatabase()
  {
    db.deleteDatabase();
  }
  
  
  public void addPlace(PlaceBean bean)
  {
    db.addPlace(bean);
  }
  
  
  public void addSubway(StationBean bean)
  {
    db.addSubway(bean);
  }
  
  
  public List<PlaceInfo> getAllPlaces()
  {
    return db.getAllPlaces();
  }
  
}
