package com.dabeeo.hanhayou.map;

import java.util.Map;

import android.content.Context;

import com.dabeeo.hanhayou.beans.PlaceBean;
import com.dabeeo.hanhayou.beans.StationBean;

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


	public Map<String,PlaceInfo> getAllPlaces()
	{
		return db.getAllPlaces();
	}

	public Map<String,PlaceInfo> getBoundaryPlace(double botomLat, double topLat, double bottomLng, double topLng)
	{
		return db.getBoundaryPlace(botomLat, topLat, bottomLng, topLng);
	}

	public PlaceInfo getPlacefromIdx(String Idx)
	{
		return db.getPlacefromIDX(Idx);
	}
	
	public String getPremiumfromIDX(String Idx)
	{
	  return db.getPremiumfromIDX(Idx);
	}

	public Map<String, SubwayInfo> getallSubwayInfo()
	{
		return db.getallSubwayInfo();
	}

	public SubwayInfo getSubwayfromIdx(String Idx)
	{
		return db.getSubwayfromIdx(Idx);
	}
	
	public Map<String, SubwayExitInfo> getallSubwayExitInfo()
	{
		return db.getallSubwayExitInfo();
	}
}
