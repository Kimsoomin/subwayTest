package com.dabeeo.hangouyou.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.dabeeo.hangouyou.beans.StationBean;
import com.dabeeo.hangouyou.managers.SubwayManager;

public class OfflineContentDatabaseManager extends SQLiteOpenHelper
{
  @SuppressLint("SdCardPath")
  public static String DB_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Hangouyou/";
  public static String DB_NAME = "contents.sqlite";
  //Tables
  private static String TABLE_NAME_SUBWAY = "Subway";
  private static String TABLE_NAME_SUBWAY_EXITS = "SubwayExit";
  private static String TABLE_NAME_PLACE = "place";
  private static String TABLE_NAME_PLAN = "plan";
  private static String TABLE_NAME_REVIEW = "review";
  
  private SQLiteDatabase myDataBase;
  
  private final Context context;
  
  
  public OfflineContentDatabaseManager(Context context)
  {
    super(context, DB_NAME, null, 1);
    this.context = context;
  }
  
  
  public void createDataBase() throws IOException
  {
    boolean dbExist = checkDataBase();
    
    if (dbExist)
      Log.w("WARN", "Content Database is exist");
    else
    {
      try
      {
        copyDataBase();
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    Log.w("WARN", "Content Database Copy finish");
  }
  
  
  private boolean checkDataBase()
  {
    SQLiteDatabase checkDB = null;
    try
    {
      String myPath = DB_PATH + DB_NAME;
      Log.w("WARN", "MyPath : " + myPath);
      File file = new File(myPath);
      if (file.exists())
        checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
      else
        checkDB = null;
    }
    catch (SQLiteException e)
    {
      e.printStackTrace();
    }
    
    if (checkDB != null)
      checkDB.close();
    
    return checkDB != null ? true : false;
  }
  
  
  private void copyDataBase() throws IOException
  {
    InputStream myInput = context.getAssets().open("hanhayou.sqlite");
    String outFileName = DB_PATH + DB_NAME;
    File dbFolder = new File(DB_PATH);
    if (!dbFolder.exists())
      dbFolder.mkdir();
    
    File dbFile = new File(outFileName);
    if (!dbFile.exists())
      dbFile.createNewFile();
    OutputStream myOutput = new FileOutputStream(dbFile);
    
    byte[] buffer = new byte[1024];
    int length;
    while ((length = myInput.read(buffer)) > 0)
    {
      myOutput.write(buffer, 0, length);
    }
    
    myOutput.flush();
    myOutput.close();
    myInput.close();
  }
  
  
  public void openDataBase() throws SQLException
  {
    String myPath = DB_PATH + DB_NAME;
    myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
  }
  
  
  @Override
  public synchronized void close()
  {
    if (myDataBase != null)
      myDataBase.close();
    
    super.close();
  }
  
  
  @Override
  public void onCreate(SQLiteDatabase db)
  {
    
  }
  
  
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
  {
    
  }
  
  
  public void insert(String tableName, JSONObject obj)
  {
    ContentValues insertValues = new ContentValues();
    try
    {
      if (tableName.equals(TABLE_NAME_PLAN))
      {
        insertValues.put("idx", obj.getString("idx"));
        insertValues.put("planCode", obj.getString("planCode"));
        insertValues.put("title", obj.getString("title"));
        insertValues.put("startDate", obj.getString("startDate"));
        insertValues.put("endDate", obj.getString("endDate"));
        insertValues.put("insertDate", obj.getString("insertDate"));
        insertValues.put("updateDate", obj.getString("updateDate"));
        insertValues.put("likeCount", obj.getInt("likeCount"));
        insertValues.put("reviewCount", obj.getInt("reviewCount"));
        insertValues.put("bookmarkCount", obj.getInt("bookmarkCount"));
        insertValues.put("rate", obj.getInt("rate"));
        insertValues.put("ownerUserSeq", obj.getString("ownerUserSeq"));
        insertValues.put("userName", obj.getString("userName"));
        insertValues.put("gender", obj.getString("gender"));
        insertValues.put("mfidx", obj.getString("mfidx"));
        insertValues.put("dayCount", obj.getInt("dayCount"));
        insertValues.put("distance", obj.getString("distance"));
        insertValues.put("budgetTotal", obj.getString("budgetTotal"));
        insertValues.put("budget1", obj.getString("budget1"));
        insertValues.put("budget2", obj.getString("budget2"));
        insertValues.put("budget3", obj.getString("budget3"));
        insertValues.put("days", obj.getString("days"));
      }
      else if (tableName.equals(TABLE_NAME_PLACE))
      {
        insertValues.put("idx", obj.getString("idx"));
        insertValues.put("seqCode", obj.getString("seqCode"));
        insertValues.put("cityIdx", obj.getString("cityIdx"));
        insertValues.put("ownerUserSeq", obj.getString("ownerUserSeq"));
        insertValues.put("userName", obj.getString("userName"));
        insertValues.put("gender", obj.getString("gender"));
        insertValues.put("category", obj.getInt("category"));
        insertValues.put("title", obj.getString("title"));
        insertValues.put("address", obj.getString("address"));
        insertValues.put("businessHours", obj.getString("businessHours"));
        insertValues.put("priceInfo", obj.getString("priceInfo"));
        insertValues.put("trafficInfo", obj.getString("trafficInfo"));
        insertValues.put("homepage", obj.getString("homepage"));
        insertValues.put("contact", obj.getString("contact"));
        insertValues.put("contents", obj.getString("contents"));
        insertValues.put("useTime", obj.getInt("useTime"));
        insertValues.put("lat", obj.getDouble("lat"));
        insertValues.put("lng", obj.getDouble("lng"));
        insertValues.put("tag", obj.getString("tag"));
        if (obj.has("popular"))
          insertValues.put("popular", obj.getInt("popular"));
        if (obj.has("rate"))
          insertValues.put("rate", obj.getInt("rate"));
        if (obj.has("likeCount"))
          insertValues.put("likeCount", obj.getInt("likeCount"));
        if (obj.has("bookmarkCount"))
          insertValues.put("bookmarkCount", obj.getInt("bookmarkCount"));
        if (obj.has("shareCount"))
          insertValues.put("shareCount", obj.getInt("shareCount"));
        if (obj.has("reviewCount"))
          insertValues.put("reviewCount", obj.getInt("reviewCount"));
        if (obj.has("isLiked"))
          insertValues.put("isLiked", obj.getInt("isLiked"));
        if (obj.has("isBookmarked"))
          insertValues.put("isBookmarked", obj.getInt("isBookmarked"));
        insertValues.put("insertDate", obj.getString("insertDate"));
        insertValues.put("updateDate", obj.getString("updateDate"));
        insertValues.put("premiumIdx", obj.getString("premiumIdx"));
        if (obj.has("image"))
          insertValues.put("image", obj.getString("image"));
        if (obj.has("offlineimage"))
          insertValues.put("offlineimage", obj.getString("offlineimage"));
      }
      else if (tableName.equals(TABLE_NAME_REVIEW))
      {
        insertValues.put("parentType", obj.getString("parentType"));
        insertValues.put("idx", obj.getString("idx"));
        insertValues.put("seqCode", obj.getString("seqCode"));
        insertValues.put("reviewSeqCode", obj.getString("reviewSeqCode"));
        insertValues.put("contents", obj.getString("contents"));
        insertValues.put("rate", obj.getInt("rate"));
        insertValues.put("likeCount", obj.getInt("likeCount"));
        insertValues.put("shareCount", obj.getInt("shareCount"));
        insertValues.put("ownerUserSeq", obj.getString("ownerUserSeq"));
        insertValues.put("userName", obj.getString("userName"));
        insertValues.put("gender", obj.getString("gender"));
        insertValues.put("mfidx", obj.getString("mfidx"));
        insertValues.put("insertDate", obj.getString("insertDate"));
        insertValues.put("updateDate", obj.getString("updateDate"));
      }
    }
    catch (JSONException e)
    {
      e.printStackTrace();
    }
    myDataBase.insert(tableName, null, insertValues);
  }
  
  
  public void insertSubwayStation(StationBean bean)
  {
    ContentValues insertValues = new ContentValues();
    try
    {
      insertValues.put("idx", bean.stationId);
      insertValues.put("line", bean.line);
      insertValues.put("name_ko", bean.nameKo);
      insertValues.put("name_en", bean.nameEn);
      insertValues.put("name_cn", bean.nameCn);
      insertValues.put("lat", bean.lat);
      insertValues.put("lng", bean.lon);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    myDataBase.insert(TABLE_NAME_SUBWAY, null, insertValues);
    
    if (bean.exits.size() > 0)
    {
      insertValues = new ContentValues();
      insertValues.put("idx", bean.stationId);
      insertValues.put("exit", bean.exitsJsonString);
      myDataBase.insert(TABLE_NAME_SUBWAY_EXITS, null, insertValues);
    }
  }
  
  
  public void writeDatabase(String jsonString)
  {
    this.openDataBase();
    try
    {
      JSONObject obj = new JSONObject(jsonString);
      
      putSubwayStationDatas();
      
      //Plan
      JSONArray arr = obj.getJSONArray("plan");
      Log.w("WARN", "오프라인 여행 Plan size : " + arr.length());
      for (int i = 0; i < arr.length(); i++)
      {
        JSONObject innerObj = arr.getJSONObject(i);
        try
        {
          insert(TABLE_NAME_PLAN, innerObj);
        }
        catch (Exception e)
        {
        }
      }
      
      arr = obj.getJSONArray("place");
      Log.w("WARN", "오프라인 Place size : " + arr.length());
      for (int i = 0; i < arr.length(); i++)
      {
        JSONObject innerObj = arr.getJSONObject(i);
        try
        {
          insert(TABLE_NAME_PLACE, innerObj);
        }
        catch (Exception e)
        {
        }
      }
      
      arr = obj.getJSONArray("review");
      Log.w("WARN", "오프라인 Review size : " + arr.length());
      for (int i = 0; i < arr.length(); i++)
      {
        JSONObject innerObj = arr.getJSONObject(i);
        try
        {
          insert(TABLE_NAME_REVIEW, innerObj);
        }
        catch (Exception e)
        {
        }
      }
      
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  
  private void putSubwayStationDatas()
  {
    SubwayManager.getInstance(context).loadAllStations(context);
    ArrayList<StationBean> stations = SubwayManager.getInstance(context).stations;
    for (int i = 0; i < stations.size(); i++)
    {
      StationBean bean = stations.get(i);
      if (!bean.isDuplicate)
      {
        try
        {
          insertSubwayStation(bean);
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
    }
  }
}