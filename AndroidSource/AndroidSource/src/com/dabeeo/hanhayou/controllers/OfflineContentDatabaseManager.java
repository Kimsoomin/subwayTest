package com.dabeeo.hanhayou.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.beans.PlaceBean;
import com.dabeeo.hanhayou.beans.PlaceDetailBean;
import com.dabeeo.hanhayou.beans.ReviewBean;
import com.dabeeo.hanhayou.beans.ScheduleBean;
import com.dabeeo.hanhayou.beans.ScheduleDetailBean;
import com.dabeeo.hanhayou.beans.SearchResultBean;
import com.dabeeo.hanhayou.beans.StationBean;
import com.dabeeo.hanhayou.managers.FileManager;
import com.dabeeo.hanhayou.managers.PreferenceManager;
import com.dabeeo.hanhayou.map.Global;

public class OfflineContentDatabaseManager extends SQLiteOpenHelper
{
  @SuppressLint("SdCardPath")
//  public static String DB_PATH = "";
  public static String DB_NAME = "hanhayou.sqlite";
  //Tables
  private static String TABLE_NAME_SUBWAY = "Subway";
  private static String TABLE_NAME_SUBWAY_EXITS = "SubwayExit";
  private static String TABLE_NAME_PLACE = "place";
  private static String TABLE_NAME_PLAN = "plan";
  public static String TABLE_NAME_REVIEW = "review";
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
      String myPath = Global.GetDatabaseFilePath() + DB_NAME;
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
    AssetManager assetManager = context.getAssets();
    
    File file = new File(Global.GetDatabaseFilePath() + DB_NAME);
    
    if (!file.exists())
    {
      try
      {
        InputStream is = assetManager.open("hanhayou.sqlite");
        OutputStream out = new FileOutputStream(file);
        
        int size = is.available();
        
        if (size > 0)
        {
          byte[] data = new byte[size];
          is.read(data);
          
          out.write(data);
        }
        out.flush();
        out.close();
        
        is.close();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }
  
  
  public void openDataBase()
  {
    try
    {
      String myPath = Global.GetDatabaseFilePath() + DB_NAME;
      myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
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
    if (myDataBase == null)
      openDataBase();
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
        if (obj.has("currency"))
          insertValues.put("currency", obj.getString("currency"));
        if (obj.has("currencySymbol"))
          insertValues.put("currencySymbol", obj.getString("currencySymbol"));
      }
      else if (tableName.equals(TABLE_NAME_PLACE))
      {
        insertValues.put("idx", obj.getString("idx"));
        insertValues.put("seqCode", obj.getString("seqCode"));
        insertValues.put("cityIdx", obj.getString("cityIdx"));
        insertValues.put("ownerUserSeq", obj.getString("ownerUserSeq"));
        insertValues.put("userName", obj.getString("userName"));
        insertValues.put("gender", obj.getString("gender"));
        if (obj.has("category"))
          insertValues.put("category", obj.getInt("category"));
        insertValues.put("title", obj.getString("title"));
        if (obj.has("address"))
          insertValues.put("address", obj.getString("address"));
        if (obj.has("businessHours"))
          insertValues.put("businessHours", obj.getString("businessHours"));
        if (obj.has("priceInfo"))
          insertValues.put("priceInfo", obj.getString("priceInfo"));
        if (obj.has("trafficInfo"))
          insertValues.put("trafficInfo", obj.getString("trafficInfo"));
        if (obj.has("homepage"))
          insertValues.put("homepage", obj.getString("homepage"));
        if (obj.has("contact"))
          insertValues.put("contact", obj.getString("contact"));
        if (obj.has("contents"))
          insertValues.put("contents", obj.getString("contents"));
        if (obj.has("useTime"))
          insertValues.put("useTime", obj.getInt("useTime"));
        insertValues.put("lat", obj.getDouble("lat"));
        insertValues.put("lng", obj.getDouble("lng"));
        if (obj.has("tag"))
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
        if (obj.has("insertDate"))
          insertValues.put("insertDate", obj.getString("insertDate"));
        if (obj.has("updateDate"))
          insertValues.put("updateDate", obj.getString("updateDate"));
        if (obj.has("premiumIdx"))
        {
          if (obj.getString("premiumIdx") != null)
          {
            insertValues.put("premiumIdx", obj.getString("premiumIdx"));
          }
          else
          {
            insertValues.put("premiumIdx", "null");
          }
        }
        if (obj.has("offlineImage"))
          insertValues.put("offlineimage", obj.getString("offlineImage"));
        if (obj.has("image"))
          insertValues.put("image", obj.getString("image"));
      }
      else if (tableName.equals(TABLE_NAME_REVIEW))
      {
        if (obj.has("parentType"))
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
        if (obj.has("parent"))
        {
          JSONObject parentObj = new JSONObject(obj.getString("parent"));
          if (parentObj.has("idx"))
          {
            insertValues.put("parentIdx", parentObj.getString("idx"));
          }
        }
      }
    }
    catch (JSONException e)
    {
      e.printStackTrace();
    }
    
    try
    {
      myDataBase.insert(tableName, null, insertValues);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
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
    
    if (bean.exits.size() > 0 && bean.line.contains("환승"))
    {
      insertValues = new ContentValues();
      insertValues.put("idx", bean.stationId);
      insertValues.put("exit", bean.exitsJsonString);
      myDataBase.insert(TABLE_NAME_SUBWAY_EXITS, null, insertValues);
    }
  }
  
  
  public void writeDatabaseMyPlace(String jsonString)
  {
    this.openDataBase();
    try
    {
      JSONObject obj = new JSONObject(jsonString);
      
      //Plan
      JSONArray arr = obj.getJSONArray("place");
      
      for (int i = 0; i < arr.length(); i++)
      {
        JSONObject innerObj = arr.getJSONObject(i);
        innerObj.put("ownerUserSeq", PreferenceManager.getInstance(context).getUserSeq());
        innerObj.put("userName", PreferenceManager.getInstance(context).getUserName());
        Cursor c = myDataBase.rawQuery("SELECT * FROM " + TABLE_NAME_PLACE + " WHERE idx = " + innerObj.getString("idx"), null);
        c.moveToFirst();
        if (c.getCount() > 0)
        {
          Log.w("WARN", "이미 있는 장소");
          ContentValues insertValues = new ContentValues();
          try
          {
            insertValues.put("idx", obj.getString("idx"));
            insertValues.put("seqCode", obj.getString("seqCode"));
            insertValues.put("cityIdx", obj.getString("cityIdx"));
            insertValues.put("ownerUserSeq", obj.getString("ownerUserSeq"));
            insertValues.put("userName", obj.getString("userName"));
            insertValues.put("gender", obj.getString("gender"));
            if (obj.has("category"))
              insertValues.put("category", obj.getInt("category"));
            insertValues.put("title", obj.getString("title"));
            if (obj.has("address"))
              insertValues.put("address", obj.getString("address"));
            if (obj.has("businessHours"))
              insertValues.put("businessHours", obj.getString("businessHours"));
            if (obj.has("priceInfo"))
              insertValues.put("priceInfo", obj.getString("priceInfo"));
            if (obj.has("trafficInfo"))
              insertValues.put("trafficInfo", obj.getString("trafficInfo"));
            if (obj.has("homepage"))
              insertValues.put("homepage", obj.getString("homepage"));
            if (obj.has("contact"))
              insertValues.put("contact", obj.getString("contact"));
            if (obj.has("contents"))
              insertValues.put("contents", obj.getString("contents"));
            if (obj.has("useTime"))
              insertValues.put("useTime", obj.getInt("useTime"));
            insertValues.put("lat", obj.getDouble("lat"));
            insertValues.put("lng", obj.getDouble("lng"));
            if (obj.has("tag"))
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
            if (obj.has("insertDate"))
              insertValues.put("insertDate", obj.getString("insertDate"));
            if (obj.has("updateDate"))
              insertValues.put("updateDate", obj.getString("updateDate"));
            if (obj.has("premiumIdx"))
            {
              if (obj.getString("premiumIdx") != null)
              {
                insertValues.put("premiumIdx", obj.getString("premiumIdx"));
              }
              else
              {
                insertValues.put("premiumIdx", "null");
              }
            }
          }
          catch (Exception e)
          {
            e.printStackTrace();
          }
          myDataBase.update(TABLE_NAME_PLACE, insertValues, "idx = ?", new String[] { innerObj.getString("idx") });
        }
        else
        {
          Log.w("WARN", "없는 장소");
          try
          {
            insert(TABLE_NAME_PLACE, innerObj);
          }
          catch (Exception e)
          {
            e.printStackTrace();
          }
        }
        c.close();
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  
  public void writeDatabase(String jsonString)
  {
    this.openDataBase();
    try
    {
      JSONObject obj = new JSONObject(jsonString);
      
//			putSubwayStationDatas();
      
      //Plan
      JSONArray arr = obj.getJSONArray("plan");
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
  
  
  /**
   * Get Data
   */
  public ArrayList<PlaceBean> getPlaceList(int categoryId)
  {
    return getPlaceList(categoryId, false);
  }
  
  
  public ArrayList<PlaceBean> getPlaceList(int categoryId, boolean isSortPopular)
  {
    ArrayList<PlaceBean> beans = new ArrayList<PlaceBean>();
    try
    {
      this.openDataBase();
      
      Cursor c;
      if (categoryId == 9)
      {
        c = myDataBase.rawQuery("SELECT * FROM " + TABLE_NAME_PLACE + " WHERE category = " + "1 or category = 3 or category = 4 or category = 5 or category = 6", null);
      }
      else
        c = myDataBase.rawQuery("SELECT * FROM " + TABLE_NAME_PLACE + " WHERE category = " + categoryId, null);
      if (categoryId == 0)
        c = myDataBase.rawQuery("SELECT * FROM " + TABLE_NAME_PLACE, null);
      if (c.moveToFirst())
      {
        do
        {
          try
          {
            PlaceBean bean = new PlaceBean();
            bean.setCursor(c);
            if (!bean.premiumIdx.equals("null"))
              beans.add(bean);
          }
          catch (Exception e)
          {
          }
        } while (c.moveToNext());
      }
      
      if (isSortPopular)
      {
        Comparator<PlaceBean> compare = new Comparator<PlaceBean>()
        {
          @Override
          public int compare(PlaceBean lhs, PlaceBean rhs)
          {
            return rhs.likeCount - lhs.likeCount;
          }
        };
        Collections.sort(beans, compare);
      }
      
      c.close();
      myDataBase.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return beans;
  }
  
  
  public ScheduleDetailBean getMYTravelScheduleDetailBean(String idx)
  {
    ScheduleDetailBean bean = new ScheduleDetailBean();
    
    JSONArray array;
    try
    {
      array = new JSONObject(FileManager.getInstance(context).readFile(FileManager.FILE_MY_PLAN)).getJSONArray("plan");
      for (int i = 0; i < array.length(); i++)
      {
        JSONObject obj = array.getJSONObject(i);
        if (obj.getString("idx").equals(idx))
          bean.setJSONObject(obj);
      }
    }
    catch (JSONException e)
    {
      e.printStackTrace();
    }
    return bean;
  }
  
  
  public ArrayList<PlaceBean> getMyPlaces(int categoryId)
  {
    ArrayList<PlaceBean> beans = new ArrayList<PlaceBean>();
    try
    {
      this.openDataBase();
      
      Cursor c = myDataBase.rawQuery("SELECT * FROM " + TABLE_NAME_PLACE + " WHERE ownerUserSeq = " + PreferenceManager.getInstance(context).getUserSeq(), null);
      if (categoryId != -1)
        c = myDataBase.rawQuery("SELECT * FROM " + TABLE_NAME_PLACE + " WHERE ownerUserSeq = " + PreferenceManager.getInstance(context).getUserSeq() + " AND category = " + categoryId, null);
      c.moveToFirst();
      if (c.moveToFirst())
      {
        do
        {
          try
          {
            PlaceBean bean = new PlaceBean();
            bean.setCursor(c);
//            if (!bean.premiumIdx.equals("null"))
            beans.add(bean);
          }
          catch (Exception e)
          {
          }
        } while (c.moveToNext());
      }
      
      c.close();
      myDataBase.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return beans;
  }
  
  
  public PlaceDetailBean getPlaceDetail(String placeIdx)
  {
    this.openDataBase();
    PlaceDetailBean bean = new PlaceDetailBean();
    Cursor c = myDataBase.rawQuery("SELECT * FROM " + TABLE_NAME_PLACE + " WHERE idx = " + placeIdx, null);
    c.moveToFirst();
    try
    {
      Log.w("WARN", "c : " + c.getString(c.getColumnIndex("title")));
      bean.setCursor(c);
    }
    catch (Exception e)
    {
    }
    c.close();
    
    myDataBase.close();
    return bean;
  }
  
  
  public ArrayList<ScheduleBean> getTravelSchedules(int page)
  {
    ArrayList<ScheduleBean> beans = new ArrayList<ScheduleBean>();
    try
    {
      this.openDataBase();
      Cursor c = myDataBase.rawQuery("SELECT * FROM " + TABLE_NAME_PLAN + " LIMIT 10 OFFSET " + (10 * page), null);
      if (c.moveToFirst())
      {
        do
        {
          try
          {
            ScheduleBean bean = new ScheduleBean();
            bean.setCursor(c);
            beans.add(bean);
          }
          catch (Exception e)
          {
          }
        } while (c.moveToNext());
      }
      
      c.close();
      myDataBase.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return beans;
  }
  
  
  public ArrayList<SearchResultBean> getSearchAuto(String keyword)
  {
    ArrayList<SearchResultBean> beans = new ArrayList<SearchResultBean>();
    try
    {
      this.openDataBase();
      Cursor c = myDataBase.rawQuery("SELECT * FROM " + TABLE_NAME_PLAN + " WHERE title LIKE '%" + keyword + "%'", null);
      if (c.moveToFirst())
      {
        do
        {
          try
          {
            SearchResultBean bean = new SearchResultBean();
            bean.setCursor(c);
            bean.setLogType("plan");
            beans.add(bean);
            Log.w("WARN", "일정 더하기 : " + bean.text);
          }
          catch (Exception e)
          {
            e.printStackTrace();
          }
        } while (c.moveToNext());
      }
      
      c = myDataBase.rawQuery("SELECT * FROM " + TABLE_NAME_PLACE + " WHERE title LIKE '%" + keyword + "%'", null);
      if (c.moveToFirst())
      {
        do
        {
          try
          {
            SearchResultBean bean = new SearchResultBean();
            bean.setCursor(c);
            bean.setLogType("place");
            beans.add(bean);
            Log.w("WARN", "장소 더하기 : " + bean.text);
          }
          catch (Exception e)
          {
          }
        } while (c.moveToNext());
      }
      
      c.close();
      myDataBase.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return beans;
  }
  
  
  public ArrayList<SearchResultBean> getSearchResult(String keyword)
  {
    return getSearchResult(keyword, -1);
  }
  
  
  public ArrayList<SearchResultBean> getSearchResult(String keyword, int limit)
  {
    ArrayList<SearchResultBean> array = new ArrayList<SearchResultBean>();
    int queryLimit = limit;
    
    try
    {
      this.openDataBase();
      String query = "SELECT * FROM " + TABLE_NAME_PLAN + " WHERE title LIKE '%" + keyword + "%'";
      Cursor c = myDataBase.rawQuery(query, null);
      
      if (limit != -1)
        queryLimit = 0;
      
      if (c.moveToFirst())
      {
        if (c.getCount() > 0)
        {
          SearchResultBean titleBean = new SearchResultBean();
          titleBean = new SearchResultBean();
          titleBean.addPlaceTitle(context.getString(R.string.term_schedule), c.getCount());
          titleBean.type = SearchResultBean.TYPE_PLACE;
          array.add(titleBean);
        }
        
        do
        {
          if (queryLimit == 3)
            break;
          
          try
          {
            SearchResultBean bean = new SearchResultBean();
            bean.setCursor(c);
            bean.setLogType("plan");
            array.add(bean);
          }
          catch (Exception e)
          {
          }
          
          if (limit != -1)
            queryLimit++;
        } while (c.moveToNext());
      }
      
      if (limit != -1)
        queryLimit = 0;
      query = "SELECT * FROM " + TABLE_NAME_PLACE + " WHERE title LIKE '%" + keyword + "%'";
      c = myDataBase.rawQuery(query, null);
      
      if (c.moveToFirst())
      {
        if (c.getCount() > 0)
        {
          SearchResultBean titleBean = new SearchResultBean();
          titleBean = new SearchResultBean();
          titleBean.addPlaceTitle(context.getString(R.string.term_place), c.getCount());
          titleBean.type = SearchResultBean.TYPE_PLACE;
          array.add(titleBean);
        }
        
        do
        {
          if (queryLimit == 3)
            break;
          
          try
          {
            SearchResultBean bean = new SearchResultBean();
            bean.setCursor(c);
            bean.setLogType("place");
            Log.w("WARN", "Search offline plan : " + c.getString(c.getColumnIndex("title")));
            array.add(bean);
          }
          catch (Exception e)
          {
          }
          
          if (limit != -1)
            queryLimit++;
        } while (c.moveToNext());
      }
      
      c.close();
      myDataBase.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return array;
  }
  
  
  public ScheduleDetailBean getTravelScheduleDetailBean(String idx)
  {
    
    this.openDataBase();
    ScheduleDetailBean bean = new ScheduleDetailBean();
    Log.w("WARN", "SELECT * FROM " + TABLE_NAME_PLAN + " WHERE idx = " + idx);
    Cursor c = myDataBase.rawQuery("SELECT * FROM " + TABLE_NAME_PLAN + " WHERE idx = " + idx, null);
    c.moveToFirst();
    try
    {
      bean.setCursor(c);
    }
    catch (Exception e)
    {
    }
    c.close();
    myDataBase.close();
    return bean;
  }
  
  
  public ReviewBean getReview(String reviewIdx)
  {
    this.openDataBase();
    Log.w("WARN", "찾을 리뷰 아이디 : " + reviewIdx);
    ReviewBean bean = new ReviewBean();
    Cursor c = myDataBase.rawQuery("SELECT * FROM " + TABLE_NAME_REVIEW + " WHERE idx = " + reviewIdx, null);
    c.moveToFirst();
    try
    {
      bean.setCursor(c);
    }
    catch (Exception e)
    {
    }
    c.close();
    myDataBase.close();
    return bean;
  }
  
  
  public ArrayList<ReviewBean> getReviews(int page, String parentIdx)
  {
    this.openDataBase();
    Log.w("WARN", "GetOffline reviews " + page);
    ArrayList<ReviewBean> beans = new ArrayList<ReviewBean>();
    Cursor c = myDataBase.rawQuery("SELECT * FROM " + TABLE_NAME_REVIEW + " WHERE parentIdx = " + parentIdx + " LIMIT 10 OFFSET " + (10 * page), null);
    if (c.moveToFirst())
    {
      do
      {
        try
        {
          ReviewBean bean = new ReviewBean();
          bean.setCursor(c);
          beans.add(bean);
        }
        catch (Exception e)
        {
        }
      } while (c.moveToNext());
    }
    c.close();
    myDataBase.close();
    return beans;
  }
}
