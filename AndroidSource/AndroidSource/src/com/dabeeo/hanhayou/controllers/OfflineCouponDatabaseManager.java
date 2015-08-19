package com.dabeeo.hanhayou.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

import com.dabeeo.hanhayou.beans.CouponDetailBean;
import com.dabeeo.hanhayou.managers.PreferenceManager;
import com.dabeeo.hanhayou.map.Global;

public class OfflineCouponDatabaseManager extends SQLiteOpenHelper
{
	@SuppressLint("SdCardPath")
	public static String DB_NAME = "coupon.sqlite";
	//Tables
	private static String TABLE_NAME_COUPON = "Coupon";
	private SQLiteDatabase myDataBase;
	
	private final Context context;
	
	
	public OfflineCouponDatabaseManager(Context context)
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
			} catch (IOException e)
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
		} catch (SQLiteException e)
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
				Log.w("WARN", "쿠폰 데이터베이스 파일 복사");
				InputStream is = assetManager.open("coupon.sqlite");
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
			} catch (Exception e)
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
		} catch (Exception e)
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
	
	
	public void insert(JSONObject obj)
	{
		if (myDataBase == null)
			openDataBase();
		ContentValues insertValues = new ContentValues();
		try
		{
			insertValues.put("coupon_idx", obj.getString("coupon_idx"));
			insertValues.put("branch_idx", obj.getString("branch_idx"));
			insertValues.put("scope_idx", obj.getString("scope_idx"));
			insertValues.put("place_idx", obj.getString("place_idx"));
			insertValues.put("brand_name", obj.getString("brand_name"));
			insertValues.put("branch_name", obj.getString("branch_name"));
			insertValues.put("category", obj.getString("category"));
			insertValues.put("category_name", obj.getString("category_name"));
			if (obj.has("start_date"))
				insertValues.put("start_date", obj.getString("start_date"));
			if (obj.has("end_date"))
				insertValues.put("end_date", obj.getString("end_date"));
			insertValues.put("is_exhaustion", obj.getString("is_exhaustion"));
			insertValues.put("is_notimelimit", obj.getString("is_notimelimit"));
			insertValues.put("title", obj.getString("title"));
			insertValues.put("info", obj.getString("info"));
			insertValues.put("condition", obj.getString("condition"));
			insertValues.put("howto", obj.getString("howto"));
			insertValues.put("address", obj.getString("address"));
			insertValues.put("tel", obj.getString("tel"));
			insertValues.put("notice", obj.getString("notice"));
			if (obj.has("lat"))
				insertValues.put("lat", obj.getString("lat"));
			if (obj.has("lng"))
				insertValues.put("lng", obj.getString("lng"));
			insertValues.put("coupon_image", obj.getString("coupon_image"));
			insertValues.put("download_image", obj.getString("download_image"));
			insertValues.put("userSeq", PreferenceManager.getInstance(context).getUserSeq());
		} catch (JSONException e)
		{
			e.printStackTrace();
		}
		
		try
		{
			myDataBase.insert(TABLE_NAME_COUPON, null, insertValues);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Get MyCoupons
	 */
	public boolean isHaveCoupon(String couponIdx, String branchIdx)
	{
		this.openDataBase();
		Cursor c = myDataBase.rawQuery("SELECT * FROM " + TABLE_NAME_COUPON + " WHERE coupon_idx = " + couponIdx + " AND branch_idx = " + branchIdx + " AND userSeq = "
				+ PreferenceManager.getInstance(context).getUserSeq(), null);
		Log.w("WARN", "isHave a coupon? " + c.getCount());
		if (c.getCount() > 0)
		{
			c.moveToLast();
			CouponDetailBean bean = new CouponDetailBean();
			bean.setCursor(c);
			if (!bean.isUse)
				return true;
			else
				return false;
		}
		else
			return false;
	}
	
	
	public void deleteCoupon(String couponIdx, String branchIdx, String userSeq)
	{
		this.openDataBase();
		myDataBase.delete(TABLE_NAME_COUPON, "coupon_idx=? AND branch_idx=? AND userSeq=?", new String[] { couponIdx, branchIdx, userSeq });
	}
	
	
	@SuppressLint("SimpleDateFormat")
	public void setUseCoupon(String couponIdx, String branchIdx)
	{
		this.openDataBase();
		
		ContentValues values = new ContentValues();
		values.put("is_use", 1);
		SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
		String date = format.format(new Date());
		values.put("useDate", date);
		myDataBase.update(TABLE_NAME_COUPON, values, "coupon_idx=? AND branch_idx=?", new String[] { couponIdx, branchIdx });
	}
	
	
	public CouponDetailBean getDownloadCoupon(String couponIdx, String branchIdx)
	{
		this.openDataBase();
		ArrayList<CouponDetailBean> coupons = new ArrayList<CouponDetailBean>();
		Cursor c = myDataBase.rawQuery("SELECT * FROM " + TABLE_NAME_COUPON + " WHERE userSeq = " + PreferenceManager.getInstance(context).getUserSeq() + " AND coupon_idx =" + couponIdx
				+ " AND branch_idx = " + branchIdx, null);
		CouponDetailBean bean = null;
		if (c.getCount() > 0)
		{
			if (c.moveToLast())
			{
				try
				{
					bean = new CouponDetailBean();
					bean.setCursor(c);
					coupons.add(bean);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		return bean;
	}
	
	
	public ArrayList<CouponDetailBean> getDownloadCoupons()
	{
		this.openDataBase();
		ArrayList<CouponDetailBean> coupons = new ArrayList<CouponDetailBean>();
		Cursor c = myDataBase.rawQuery("SELECT * FROM " + TABLE_NAME_COUPON + " WHERE userSeq = " + PreferenceManager.getInstance(context).getUserSeq() + " AND is_use = 0", null);
		Log.w("WARN", "Download UnUseCoupoons : " + c.getCount());
		if (c.getCount() > 0)
		{
			if (c.moveToFirst())
			{
				do
				{
					try
					{
						CouponDetailBean bean = new CouponDetailBean();
						bean.setCursor(c);
						coupons.add(bean);
					} catch (Exception e)
					{
						e.printStackTrace();
					}
				} while (c.moveToNext());
			}
		}
		
		c = myDataBase.rawQuery("SELECT * FROM " + TABLE_NAME_COUPON + " WHERE userSeq = " + PreferenceManager.getInstance(context).getUserSeq() + " AND is_use = 1", null);
		Log.w("WARN", "Download UseCoupoons : " + c.getCount());
		if (c.getCount() > 0)
		{
			if (c.moveToFirst())
			{
				do
				{
					try
					{
						CouponDetailBean bean = new CouponDetailBean();
						bean.setCursor(c);
						coupons.add(bean);
					} catch (Exception e)
					{
						e.printStackTrace();
					}
				} while (c.moveToNext());
			}
		}
		
		return coupons;
	}
	
}
