package com.dabeeo.hangouyou.map;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class testDB extends SQLiteOpenHelper
{
  
  private SQLiteDatabase testdb = null;
  
  
  public testDB(Context context)
  {
    super(context, "testDB", null, 1);
    
//    testdb = OpenDatabase();
  }
  
  
//  public SQLiteDatabase OpenDatabase()
//  {
//		String sdCardPath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
//		String strDBFilePath = sdCardPath + "/.BlinkingSeoul/" + Global.g_strMapDBFileName;
  
//    if (new File(Global.strMapDBFilePath2).exists() == false)
//    {
//      BlinkingCommon.smlLibPrintException("ERROR", "Can't open the MapDB: " + Global.strMapDBFilePath2);
//      return null;
//    }
//    
//    return SQLiteDatabase.openDatabase(Global.strMapDBFilePath2, null, SQLiteDatabase.OPEN_READWRITE | SQLiteDatabase.NO_LOCALIZED_COLLATORS);
//  }
  
  @Override
  public void onCreate(SQLiteDatabase db)
  {
    // TODO Auto-generated method stub
    db.execSQL("CREATE TABLE IF NOT EXISTS place (idx TEXT PRIMARY KEY NOT NULL, seqCode TEXT, cityIdx TEXT, ownerUserSeq TEXT, userName TEXT, gender TEXT, mfidx TEXT, category INTEGER, title TEXT NOT NULL, address TEXT, businessHours TEXT, priceInfo TEXT, trafficInfo TEXT, homepage TEXT, contact TEXT, contents TEXT, lat DOUBLE NOT NULL, lng DOUBLE NOT NULL, tag TEXT, popular INTEGER, rate INTEGER, likeCount INTEGER, bookmarkCount INTEGER, shareCount INTEGER, reviewCount INTEGER, isLiked INTEGER, isBookmarked INTEGER, insertDate TEXT, updateDate TEXT)");
  }
  
  
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
  {
    
  }
  
  
  public List<PlaceInfo> getAllPlace()
  {
    List<PlaceInfo> placeList = new ArrayList<PlaceInfo>();
    String selectQuery = "SELECT  * FROM " + "place_info";
    
    //SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = testdb.rawQuery(selectQuery, null);
    
    try
    {
      // looping through all rows and adding to list
      if (cursor.moveToFirst())
      {
        do
        {
          PlaceInfo placeInfo = new PlaceInfo();
          placeInfo.m_nID = cursor.getString(0);
          placeInfo.m_nCategoryID = cursor.getInt(6);
          placeInfo.m_fLatitude = cursor.getFloat(8);
          placeInfo.m_fLongitude = cursor.getFloat(9);
          placeInfo.m_strName = cursor.getString(13);
          placeInfo.m_strIntro = cursor.getString(14);
          
          // Adding contact to list
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
  
}
