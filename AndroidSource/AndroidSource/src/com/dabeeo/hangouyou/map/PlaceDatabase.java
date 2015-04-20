package com.dabeeo.hangouyou.map;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PlaceDatabase extends SQLiteOpenHelper
{
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "myPlaceManager";
	
	private static final String TABLE_PLACE = "Place";
	private static final String N_ID = "nId";
	private static final String N_TYPE = "nType";
	private static final String PLACE_NAME = "pName";
	private static final String LATITUDE = "lat";
	private static final String LONGITUDE = "lng";
	
	public PlaceDatabase(Context context) 
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		// TODO Auto-generated method stub
		String CREATE_MYPLACE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_PLACE + "(" + N_ID + " INTEGER PRIMARY KEY, "
									+ N_TYPE + " INTEGER, " + PLACE_NAME + " TEXT, " + LATITUDE + " DOUBLE, "
									+ LONGITUDE + " DOUBLE" + ")";
		db.execSQL(CREATE_MYPLACE_TABLE);		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		// TODO Auto-generated method stub
		 db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACE);
         // Create tables again
         onCreate(db);
	}
	
	public void addPlace(int nID, int nType, String pName, double lat, double lng) 
	{
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(N_ID, nID);
        values.put(N_TYPE, nType);
        values.put(PLACE_NAME, pName);
        values.put(LATITUDE, lat);
        values.put(LONGITUDE, lng);
//
//        // Inserting Row
//        BlinkingCommon.smlLibDebug("N_ID", ""+values.getAsInteger(N_ID));
//        BlinkingCommon.smlLibDebug("N_ID", ""+nID);
//        db.insert(TABLE_PLACE, null, values);
        db.execSQL("INSERT INTO Place(nId,nType,pName,lat,lng) VALUES ("+ nID+ "," + nType + ","
        		+ "\"" + pName + "\"" + "," + lat + "," + lng + ")");

        db.close(); // Closing database connection
	}
	
	public Place getPlace(int id) 
	{
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PLACE, new String[] { N_ID,
                             N_TYPE, PLACE_NAME, LATITUDE, LONGITUDE }, N_ID + "=?",
                             new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
                   cursor.moveToFirst();

        Place place = new Place(cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
        						cursor.getDouble(3), cursor.getDouble(4));
        // return contact
        //db.close();
        return place;
	}
	
	public List<Place> getAllPlace() 
	{
		List<Place> palceList = new ArrayList<PlaceDatabase.Place>();
		String selectQuery = "SELECT  * FROM " + TABLE_PLACE;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		try {
			// looping through all rows and adding to list
			if (cursor.moveToFirst()) 
			{
				do 
				{
					Place place = new Place();
					place.setID(cursor.getInt(0));
					place.setType(cursor.getInt(1));
					place.setPlaceName(cursor.getString(2));
					place.setLat(cursor.getDouble(3));
					place.setLng(cursor.getDouble(4));
					// Adding contact to list
					palceList.add(place);
				} while (cursor.moveToNext());
			}
		} finally
		{
			if(cursor != null)
			{
				cursor.close();
			}
		}		
		
		//db.close();
		
		return palceList;
	}
	
	public List<Place> getTypePlace(int nType) 
	{
		List<Place> palceList = new ArrayList<PlaceDatabase.Place>();
		String selectQuery = "SELECT   FROM " + TABLE_PLACE + "WHERE " + N_TYPE + " = " + nType;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) 
		{
			do 
			{
				Place place = new Place();
				place.setID(cursor.getInt(0));
				place.setType(cursor.getInt(1));
				place.setPlaceName(cursor.getString(2));
				place.setLat(cursor.getDouble(3));
				place.setLng(cursor.getDouble(4));
				// Adding contact to list
				palceList.add(place);
			} while (cursor.moveToNext());
		}
		return palceList;
	}
	
	public void deletePlace(Place place) 
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PLACE, N_ID + " = ?",
				new String[] { String.valueOf(place.getID()) });
		//db.close();
	}
	
	public int getPlaceCount() 
	{
		String countQuery = "SELECT  * FROM " + TABLE_PLACE;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();
		
		//db.close();

		// return count
		return cursor.getCount();
	}

	class Place 
	{
		private int n_id;
		private int n_type;
		private String p_name;
		private double lat;
		private double lng;
		
		public Place()
		{
			
		}
		
		public Place(int _n_id, int _n_type, String _p_name, double _lat, double _lng)
		{
			this.n_id = _n_id;
			this.n_type = _n_type;
			this.p_name = _p_name;
			this.lat = _lat;
			this.lng = _lng;
		}
		
		public void setID(int nID)
		{
			this.n_id = nID;
		}
		
		public int getID()
		{
			return n_id;
		}
		
		public void setType(int nType)
		{
			this.n_type = nType;
		}
		
		public int getType()
		{
			return n_type;
		}
		
		public void setPlaceName(String pName)
		{
			this.p_name = pName;
		}
		
		public String getPlaceName()
		{
			return p_name;
		}
		
		public void setLat(double lat)
		{
			this.lat = lat;
		}
		
		public double getlat()
		{
			return lat;
		}
		
		public void setLng(double lng)
		{
			this.lng = lng;
		}
		
		public double getlng()
		{
			return lng;
		}
	}
}
