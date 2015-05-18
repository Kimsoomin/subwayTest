package com.dabeeo.hangouyou.map;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dabeeo.hangouyou.beans.PlaceBean;
import com.dabeeo.hangouyou.beans.StationBean;

public class DatabaseManager
{
  public static final String KEY_DATE = "date";
  public static final String KEY_COUNT = "count";
  public static final String KEY_TITLE = "title";
  public static final String KEY_ROWID = "_id";
  
  private static final String TAG = "WARN";
  
  private DatabaseHelper mDbHelper;
  private SQLiteDatabase mDb;
  
  private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS place_info (idx TEXT PRIMARY KEY NOT NULL, seqCode TEXT, cityIdx TEXT, ownerUserSeq TEXT, userName TEXT, gender TEXT, mfidx TEXT, category INTEGER, title TEXT NOT NULL, address TEXT, businessHours TEXT, priceInfo TEXT, trafficInfo TEXT, homepage TEXT, contact TEXT, contents TEXT, lat DOUBLE NOT NULL, lng DOUBLE NOT NULL, tag TEXT, popular INTEGER, rate INTEGER, likeCount INTEGER, bookmarkCount INTEGER, shareCount INTEGER, reviewCount INTEGER, isLiked INTEGER, isBookmarked INTEGER, insertDate TEXT, updateDate TEXT)";
  
  private static final String DATABASE_NAME = "hangouyou";
  private static final String DATABASE_TABLE_NAME_PLACE_INFO = "place_info";
  private static final int DATABASE_VERSION = 1;
  private final Context context;
  
  private static class DatabaseHelper extends SQLiteOpenHelper
  {
    DatabaseHelper(Context context)
    {
      super(context, DATABASE_NAME, null, DATABASE_VERSION);
      
    }
    
    
    @Override
    public void onCreate(SQLiteDatabase db)
    {
      Log.w("WARN", "DATABASE CREATE");
      db.execSQL(DATABASE_CREATE);
    }
    
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
      Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
      db.execSQL("DROP TABLE IF EXISTS hangouyou");
      db.execSQL("DROP TABLE IF EXISTS hangouyou");
      onCreate(db);
    }
  }
  
  
  public DatabaseManager(Context ctx)
  {
    this.context = ctx;
    mDb = OpenDatabase();
  }
  
	public SQLiteDatabase OpenDatabase() {
		Global.strMapDBFilePath2 = Global.GetPathWithSDCard()+ Global.HangouyouDBFileName;

		if (new File(Global.strMapDBFilePath2).exists() == false) {
			BlinkingCommon.smlLibPrintException("ERROR",
					"Can't open the MapDB: " + Global.strMapDBFilePath);
			return null;
		}else
			BlinkingCommon.smlLibPrintException("Success",
					"Can't open the MapDB: " + Global.strMapDBFilePath);

		return SQLiteDatabase.openDatabase(Global.strMapDBFilePath2, null,
				SQLiteDatabase.OPEN_READWRITE
						| SQLiteDatabase.NO_LOCALIZED_COLLATORS);
	}
  
  
  public DatabaseManager open() throws SQLException
  {
    mDbHelper = new DatabaseHelper(context);
//    mDb = mDbHelper.getWritableDatabase();
//    try
//    {
//      mDb.execSQL(DATABASE_CREATE);
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//    }
    return this;
  }
  
  
  public void deleteDatabase()
  {
    mDb.delete(DATABASE_TABLE_NAME_PLACE_INFO, null, null);
  }
  
  
  public void close()
  {
    mDbHelper.close();
  }
  
  
  public void addPlace(PlaceBean bean)
  {
    ContentValues initialValues = new ContentValues();
    initialValues.put("idx", bean.idx);
    initialValues.put("seqCode", bean.seqCode);
    initialValues.put("cityIdx", bean.cityIdx);
    initialValues.put("title", bean.title);
    initialValues.put("category", bean.categoryId);
    initialValues.put("lat", bean.lat);
    initialValues.put("lng", bean.lng);
    initialValues.put("popular", bean.popularCount);
    initialValues.put("rate", bean.rate);
    initialValues.put("likeCount", bean.likeCount);
    initialValues.put("bookmarkCount", bean.bookmarkCount);
    initialValues.put("shareCount", bean.shareCount);
    initialValues.put("reviewCount", bean.reviewCount);
    initialValues.put("ownerUserSeq", bean.userSeq);
    initialValues.put("userName", bean.userName);
    initialValues.put("gender", bean.gender);
    mDb.insert(DATABASE_TABLE_NAME_PLACE_INFO, null, initialValues);
  }
  
  
  public void addSubway(StationBean bean)
  {
    ContentValues initialValues = new ContentValues();
    initialValues.put("idx", bean.stationId);
    initialValues.put("title", bean.nameCn);
    initialValues.put("category", 99);
    initialValues.put("lat", bean.lat);
    initialValues.put("lng", bean.lon);
    initialValues.put("address", bean.line);
    mDb.insert(DATABASE_TABLE_NAME_PLACE_INFO, null, initialValues);
  }
  
  
  public Map<String,PlaceInfo> getAllPlaces()
  {
	  Map<String,PlaceInfo> placeList = new HashMap<>();
    String selectQuery = "SELECT  * FROM " + "place_info WHERE category != '8'";
    
    //SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = mDb.rawQuery(selectQuery, null);
    
    try
    {
      if (cursor.moveToFirst())
      {
        do
        {
          PlaceInfo placeInfo = new PlaceInfo();
          placeInfo.m_nID = cursor.getString(0);
          placeInfo.m_nCategoryID = cursor.getInt(7);
          placeInfo.m_fLatitude = cursor.getFloat(16);
          placeInfo.m_fLongitude = cursor.getFloat(17);
          placeInfo.m_strName = cursor.getString(8);
          placeInfo.m_strAddress = cursor.getString(9);
          
          placeList.put(placeInfo.m_nID,placeInfo);
        } while (cursor.moveToNext());
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      if (cursor != null)
      {
        cursor.close();
      }
    }
    
    return placeList;
  }
  
  public Map<String,PlaceInfo> getBoundaryPlace(double botomLat, double topLat, double bottomLng, double topLng)
  {
	  Map<String,PlaceInfo> placeList = new HashMap<>();
    String selectQuery = "SELECT  * FROM " + "place_info WHERE (lat BETWEEN '"+botomLat+"' AND '"+topLat+"') "
    					 +" AND (lng BETWEEN '"+bottomLng+"' AND '" + topLng +"')";
    
    //SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = mDb.rawQuery(selectQuery, null);
    
    try
    {
      if (cursor.moveToFirst())
      {
        do
        {
          PlaceInfo placeInfo = new PlaceInfo();
          placeInfo.m_nID = cursor.getString(0);
          placeInfo.m_nCategoryID = cursor.getInt(7);
          placeInfo.m_fLatitude = cursor.getFloat(16);
          placeInfo.m_fLongitude = cursor.getFloat(17);
          placeInfo.m_strName = cursor.getString(8);
          placeInfo.m_strAddress = cursor.getString(9);
          
          placeList.put(placeInfo.m_nID,placeInfo);
        } while (cursor.moveToNext());
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      if (cursor != null)
      {
        cursor.close();
      }
    }
    
    return placeList;
  }
  
  public List<PlaceInfo> getPlacefromIDX(String Idx)
  {
	  List<PlaceInfo> placeList = new ArrayList<PlaceInfo>();
	  String selectQuery = "SELECT  * FROM " + "place_info"+ " where idx like '%"+Idx+"%'";
	  
	  //SQLiteDatabase db = this.getWritableDatabase();
	  Cursor cursor = mDb.rawQuery(selectQuery, null);
	    
	    try
	    {
	      if (cursor.moveToFirst())
	      {
	        do
	        {
	        	PlaceInfo placeInfo = new PlaceInfo();
	        
	        	placeInfo.m_nID = cursor.getString(0);
	        	placeInfo.m_nCategoryID = cursor.getInt(7);
	        	placeInfo.m_fLatitude = cursor.getFloat(16);
	        	placeInfo.m_fLongitude = cursor.getFloat(17);
	        	placeInfo.m_strName = cursor.getString(8);
	        	placeInfo.m_strAddress = cursor.getString(9);
	          
	        	placeList.add(placeInfo);
	        } while (cursor.moveToNext());
	      }
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	    }
	    finally
	    {
	      if (cursor != null)
	      {
	        cursor.close();
	      }
	    }
	    return placeList;
  }
  
  public Map<String,PremiumInfo> getallPremiumInfo()
  {
	  Map<String,PremiumInfo> premiumList = new HashMap<>();
	  String selectQuery = "SELECT  * FROM " + "premium_info";
	    
	    Cursor cursor = mDb.rawQuery(selectQuery, null);
	    
	    try
	    {
	      if (cursor.moveToFirst())
	      {
	        do
	        {
	          PremiumInfo premiumInfo = new PremiumInfo();
	          premiumInfo.m_nID = cursor.getString(0);
	          premiumInfo.m_strName = cursor.getString(2);
	          premiumInfo.m_fLatitude = cursor.getFloat(3);
	          premiumInfo.m_fLongitude = cursor.getFloat(4);
	          premiumInfo.m_strImageFilePath = cursor.getString(5);
	          premiumInfo.m_strIntro = cursor.getString(6);
	          premiumInfo.m_strAddress = cursor.getString(10);
	          premiumInfo.m_nLikeCount = cursor.getInt(11);
	          premiumInfo.m_nCategoryID = cursor.getInt(12);
	          
	          premiumList.put(premiumInfo.m_nID, premiumInfo);
	        } while (cursor.moveToNext());
	      }
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	    }
	    finally
	    {
	      if (cursor != null)
	      {
	        cursor.close();
	      }
	    }
	  return premiumList;
  }
  
  public List<PremiumInfo> getPremiumfromIDX(String Idx)
  {
	  List<PremiumInfo> premiumList = new ArrayList<PremiumInfo>();
	  String selectQuery = "SELECT  * FROM " + "premium_info"+ " where idx = '"+Idx+"'";
	  Cursor cursor = mDb.rawQuery(selectQuery, null);
	  
	  try
	    {
	      if (cursor.moveToFirst())
	      {
	        do
	        {
	          PremiumInfo premiumInfo = new PremiumInfo();
	          premiumInfo.m_nID = cursor.getString(0);
	          premiumInfo.m_strName = cursor.getString(2);
	          premiumInfo.m_fLatitude = cursor.getFloat(3);
	          premiumInfo.m_fLongitude = cursor.getFloat(4);
	          premiumInfo.m_strImageFilePath = cursor.getString(5);
	          premiumInfo.m_strIntro = cursor.getString(6);
	          premiumInfo.m_strAddress = cursor.getString(10);
	          premiumInfo.m_nLikeCount = cursor.getInt(11);
	          premiumInfo.m_nCategoryID = cursor.getInt(12);
	          
	          premiumList.add(premiumInfo);
	        } while (cursor.moveToNext());
	      }
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	    }
	    finally
	    {
	      if (cursor != null)
	      {
	        cursor.close();
	      }
	    }
	  return premiumList;
  }
}
