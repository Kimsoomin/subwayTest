package com.dabeeo.hangouyou.map;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

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

	private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS place (idx TEXT PRIMARY KEY NOT NULL, seqCode TEXT, cityIdx TEXT, ownerUserSeq TEXT, userName TEXT, gender TEXT, mfidx TEXT, category INTEGER, title TEXT NOT NULL, address TEXT, businessHours TEXT, priceInfo TEXT, trafficInfo TEXT, homepage TEXT, contact TEXT, contents TEXT, lat DOUBLE NOT NULL, lng DOUBLE NOT NULL, tag TEXT, popular INTEGER, rate INTEGER, likeCount INTEGER, bookmarkCount INTEGER, shareCount INTEGER, reviewCount INTEGER, isLiked INTEGER, isBookmarked INTEGER, insertDate TEXT, updateDate TEXT)";

	private static final String DATABASE_NAME = "hanhayou.sqtlite";
	private static final String DATABASE_TABLE_NAME_PLACE_INFO = "place";
	private static final String DATABASE_TABLE_NAME_SUBWAY = "subway";
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
		try {
			mDb = OpenDatabase();
		} catch (Exception e) {
			BlinkingCommon.smlLibPrintException("ERROR",""+Global.strMapDBFilePath2);
		}
		
	}

	public SQLiteDatabase OpenDatabase() {
		Global.strMapDBFilePath2 = Global.GetDatabaseFilePath() + Global.HangouyouDBFileName;

		if (new File(Global.strMapDBFilePath2).exists() == false) 
		{
			BlinkingCommon.smlLibPrintException("ERROR", "Can't open the MapDB: " + Global.strMapDBFilePath2);
			return null;
		}else
		{
			BlinkingCommon.smlLibDebug("Success", "open the MapDB: " + Global.strMapDBFilePath2);
		}

		return SQLiteDatabase.openDatabase(Global.strMapDBFilePath2, null, SQLiteDatabase.OPEN_READWRITE);
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
		initialValues.put("line", bean.line);
		initialValues.put("name_ko", bean.nameKo);
		initialValues.put("name_en", bean.nameEn);
		initialValues.put("name_cn",bean.nameCn);
		initialValues.put("category", 99);
		initialValues.put("lat", bean.lat);
		initialValues.put("lng", bean.lon);
		mDb.insert(DATABASE_TABLE_NAME_SUBWAY, null, initialValues);
	}


	public Map<String,PlaceInfo> getAllPlaces()
	{
		Map<String,PlaceInfo> placeList = new HashMap<>();
		String selectQuery = "SELECT idx, category, title, address, lat, lng, premiumIdx, image, offlineimage FROM " + "place";

		//SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = mDb.rawQuery(selectQuery, null);

		try
		{
			if (cursor.moveToFirst())
			{
				do
				{
					PlaceInfo placeInfo = new PlaceInfo();
					placeInfo.idx = cursor.getString(0);
					placeInfo.category = cursor.getInt(1);
					placeInfo.title = cursor.getString(2);
					placeInfo.address = cursor.getString(3);
					placeInfo.lat = cursor.getDouble(4);
					placeInfo.lng = cursor.getDouble(5);
					placeInfo.premiumIdx = cursor.getString(6);
					placeInfo.image = cursor.getString(7);
					placeInfo.offlineimage = cursor.getString(8);

					placeList.put(placeInfo.idx,placeInfo);
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
		String selectQuery = "SELECT idx, category, title, address, lat, lng, image, offlineimage FROM " + "place WHERE (lat BETWEEN '"
		                      +botomLat+"' AND '"+topLat+"') " + " AND (lng BETWEEN '"+bottomLng+"' AND '" + topLng +"')";

		//SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = mDb.rawQuery(selectQuery, null);

		try
		{
			if (cursor.moveToFirst())
			{
				do
				{
				  PlaceInfo placeInfo = new PlaceInfo();
          placeInfo.idx = cursor.getString(0);
          placeInfo.category = cursor.getInt(1);
          placeInfo.title = cursor.getString(2);
          placeInfo.address = cursor.getString(3);
          placeInfo.lat = cursor.getDouble(4);
          placeInfo.lng = cursor.getDouble(5);
          placeInfo.image = cursor.getString(6);
          placeInfo.offlineimage = cursor.getString(7);

          placeList.put(placeInfo.idx,placeInfo);
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

	public PlaceInfo getPlacefromIDX(String Idx)
	{
		PlaceInfo placeInfo = new PlaceInfo();
		String selectQuery = "SELECT idx, category, title, address, lat, lng, premiumIdx, image, offlineimage FROM " + "place  where idx like '%"+Idx+"%'";
		
		Cursor cursor = mDb.rawQuery(selectQuery, null);

		try
		{
			if (cursor.moveToFirst())
			{
				do
				{
          placeInfo.idx = cursor.getString(0);
          placeInfo.category = cursor.getInt(1);
          placeInfo.title = cursor.getString(2);
          placeInfo.address = cursor.getString(3);
          placeInfo.lat = cursor.getDouble(4);
          placeInfo.lng = cursor.getDouble(5);
          placeInfo.image = cursor.getString(6);
          placeInfo.offlineimage = cursor.getString(7);
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
		return placeInfo;
	}
	
	public String getPremiumfromIDX(String Idx)
  {
    String idx = null;
    String selectQuery = "SELECT idx FROM " + "place  where premiumIdx ='"+Idx+"'";
    
    Cursor cursor = mDb.rawQuery(selectQuery, null);

    try
    {
      if (cursor.moveToFirst())
      {
        do
        {
          idx = cursor.getString(0);
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
    return idx;
  }

	public Map<String, SubwayInfo> getallSubwayInfo()
	{
		Map<String,SubwayInfo> subwayList = new HashMap<>();
		String selectQuery = "SELECT  * FROM " + "Subway";
		
		Cursor cursor = mDb.rawQuery(selectQuery, null);
		
		try
		{
			if (cursor.moveToFirst())
			{
				do
				{
					SubwayInfo subwayInfo = new SubwayInfo();
					subwayInfo.idx = cursor.getString(0);
					subwayInfo.line = cursor.getString(1);
					subwayInfo.name_ko = cursor.getString(2);
					subwayInfo.name_en = cursor.getString(3);
					subwayInfo.name_cn = cursor.getString(4);
					subwayInfo.category = 99;
					subwayInfo.lat = cursor.getFloat(5);
					subwayInfo.lng = cursor.getFloat(6);
					
					subwayList.put(subwayInfo.idx, subwayInfo);
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
		return subwayList;
	}
	
	public SubwayInfo getSubwayfromIdx(String Idx)
	{
	  SubwayInfo subwayInfo = new SubwayInfo();
		String selectQuery = "SELECT  * FROM " + "Subway"+ " where idx like '%"+Idx+"%'";
		
		Cursor cursor = mDb.rawQuery(selectQuery, null);
		
		try
		{
			if (cursor.moveToFirst())
			{
				do
				{
					
					subwayInfo.idx = cursor.getString(0);
					subwayInfo.line = cursor.getString(1);
					subwayInfo.name_ko = cursor.getString(2);
					subwayInfo.name_en = cursor.getString(3);
					subwayInfo.name_cn = cursor.getString(4);
					subwayInfo.category = 99;
					subwayInfo.lat = cursor.getFloat(5);
					subwayInfo.lng = cursor.getFloat(6);
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

		return subwayInfo;
	}
	
	public Map<String, SubwayExitInfo> getallSubwayExitInfo()
	{
		Map<String,SubwayExitInfo> subwayExitList = new HashMap<>();
		String selectQuery = "SELECT  * FROM " + "SubwayExit";
		
		Cursor cursor = mDb.rawQuery(selectQuery, null);
		try
		{
			if (cursor.moveToFirst())
			{
				do
				{
					SubwayExitInfo subwayExitInfo = new SubwayExitInfo();
					subwayExitInfo.idx = cursor.getString(0);
					subwayExitInfo.exit = cursor.getString(1);
					JSONObject contentObj = new JSONObject(subwayExitInfo.exit);
					subwayExitInfo.setSubwayExitJSON(contentObj);
					
					subwayExitList.put(subwayExitInfo.idx, subwayExitInfo);
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
		return subwayExitList;
	}
	
}
