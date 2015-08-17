package com.dabeeo.hanhayou.controllers;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dabeeo.hanhayou.beans.OfflineBehaviorBean;

public class OfflineDeleteManager
{
  public static final String KEY_DATE = "date";
  public static final String KEY_COUNT = "count";
  public static final String KEY_TITLE = "title";
  public static final String KEY_ROWID = "_id";
  
  private static final String TAG = "WARN";
  
  private DatabaseHelper mDbHelper;
  private SQLiteDatabase mDb;
  
  private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS offline_user_behavior (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, behavior TEXT, idx TEXT, userSeq TEXT)";
  
  private static final String DATABASE_NAME = "offline_behavior";
  private static final int DATABASE_VERSION = 1;
  private final Context context;
  
  private static class DatabaseHelper extends SQLiteOpenHelper
  {
    DatabaseHelper(Context context)
    {
      super(context, DATABASE_NAME, null, DATABASE_VERSION);
      Log.w("WARN", "DATABASE CONSTRUCTOR");
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
      db.execSQL("DROP TABLE IF EXISTS offline_user_behavior");
      db.execSQL("DROP TABLE IF EXISTS offline_user_behavior");
      onCreate(db);
    }
  }
  
  
  public OfflineDeleteManager(Context ctx)
  {
    this.context = ctx;
    mDbHelper = new DatabaseHelper(ctx);
    open();
  }
  
  
  public void open()
  {
    mDb = mDbHelper.getWritableDatabase();
  }
  
  
  public void deleteDatabase()
  {
    mDb.delete("offline_user_behavior", null, null);
  }
  
  
  public void close()
  {
    mDbHelper.close();
  }

  public void deleteBehavior(int id)
  {
    if (mDb == null)
      open();
    mDb.delete("offline_user_behavior", "ID = " + id, null);
  }
  
  
  public void addBehavior(OfflineBehaviorBean bean)
  {
    if (mDb == null)
      open();
    ContentValues initialValues = new ContentValues();
    initialValues.put("behavior", bean.behavior);
    initialValues.put("idx", bean.idx);
    initialValues.put("userSeq", bean.userSeq);
    Log.w("WARN", "mDb : " + mDb);
    mDb.insert("offline_user_behavior", null, initialValues);
  }
  
  
  public ArrayList<OfflineBehaviorBean> getAllBehavior()
  {
    ArrayList<OfflineBehaviorBean> behaviorList = new ArrayList<>();
    String selectQuery = "SELECT id, behavior, idx, userSeq FROM " + "offline_user_behavior";
    Cursor cursor = mDb.rawQuery(selectQuery, null);
    
    try
    {
      if (cursor.moveToFirst())
      {
        do
        {
          OfflineBehaviorBean info = new OfflineBehaviorBean();
          info.id = cursor.getInt(0);
          info.behavior = cursor.getString(1);
          info.idx = cursor.getString(2);
          info.userSeq = cursor.getString(3);
          
          behaviorList.add(info);
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
    
    return behaviorList;
  }
  
}
