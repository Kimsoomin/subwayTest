package com.dabeeo.hanhayou.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.database.Cursor;

public class PlaceBean
{
	public String idx;
	public String seqCode;
	public String cityIdx;
	public String title;
	
	public int categoryId;
	public double lat, lng;
	public int popularCount = 0;
	public int rate = 0;
	public int likeCount = 0;
	public int reviewCount = 0;
	public int bookmarkCount = 0;
	public int shareCount = 0;
	public int userSeq;
	public String premiumIdx;
	public String userName;
	public String gender;
	public String mfidx;
	public double area;
	
	public String insertDateString;
	public Date insertDate;
	public String imageUrl;
	public boolean isOpen = true;
	
	public boolean isChecked = false;
	
	public boolean isHasCoupon = false;
	
	
	public JSONObject getJSONObject()
	{
		JSONObject obj = new JSONObject();
		
		try
		{
			obj.put("idx", idx);
			obj.put("seqCode", seqCode);
			obj.put("cityIdx", cityIdx);
			obj.put("title", title);
			obj.put("lat", lat);
			obj.put("lng", lng);
			obj.put("category", categoryId);
			obj.put("insertDate", insertDateString);
			obj.put("popularCount", popularCount);
			obj.put("likeCount", likeCount);
			obj.put("reviewCount", reviewCount);
			obj.put("bookmarkCount", bookmarkCount);
			obj.put("shareCount", shareCount);
			obj.put("rate", rate);
			obj.put("ownerUserSeq", userSeq);
			obj.put("userName", userName);
			obj.put("gender", gender);
			obj.put("mfidx", mfidx);
			obj.put("premiumIdx", premiumIdx);
			obj.put("imageUrl", imageUrl);
		} catch (JSONException e)
		{
			e.printStackTrace();
		}
		return obj;
	}
	
	
	@SuppressLint("SimpleDateFormat")
	public void setJSONObject(JSONObject obj)
	{
		try
		{
			if (obj.has("seqCode"))
				seqCode = obj.getString("seqCode");
			
			if (obj.has("idx"))
				idx = obj.getString("idx");
			
			if (obj.has("title"))
				title = obj.getString("title");
			
			if (obj.has("cityIdx"))
				cityIdx = obj.getString("cityIdx");
			
			if (obj.has("category"))
				categoryId = obj.getInt("category");
			
			try
			{
				if (obj.has("lat"))
					lat = obj.getDouble("lat");
				if (obj.has("lng"))
					lng = obj.getDouble("lng");
			} catch (Exception e)
			{
			}
			if (obj.has("popular"))
				popularCount = obj.getInt("popular");
			
			if (obj.has("rate"))
				rate = obj.getInt("rate");
			if (obj.has("likeCount") && !obj.getString("likeCount").equals("null"))
				likeCount = obj.getInt("likeCount");
			if (obj.has("reviewCount") && !obj.getString("reviewCount").equals("null"))
				reviewCount = obj.getInt("reviewCount");
			if (obj.has("bookmarkCount") && !obj.getString("bookmarkCount").equals("null"))
				bookmarkCount = obj.getInt("bookmarkCount");
			if (obj.has("shareCount") && !obj.getString("shareCount").equals("null"))
				shareCount = obj.getInt("shareCount");
			
			if (obj.has("insertDate"))
			{
				insertDateString = obj.getString("insertDate");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				insertDate = format.parse(insertDateString);
			}
			
			if (obj.has("ownerUserSeq"))
				userSeq = obj.getInt("ownerUserSeq");
			
			if (obj.has("premiumIdx"))
			{
				premiumIdx = obj.getString("premiumIdx");
			}
			else
			{
				premiumIdx = "null";
			}
			
			if (obj.has("userName"))
				userName = obj.getString("userName");
			if (obj.has("gender"))
				gender = obj.getString("gender");
			if (obj.has("mfidx"))
				mfidx = obj.getString("mfidx");
			
			try
			{
				if (obj.has("area"))
					area = obj.getDouble("area");
			} catch (Exception e)
			{
				
			}
			if (obj.has("imageUrl"))
				imageUrl = obj.getString("imageUrl");
			
			if (obj.has("image"))
			{
				JSONArray arr = obj.getJSONArray("image");
				imageUrl = arr.getJSONObject(0).getString("url");
			}
			
			if (obj.has("isOpen"))
			{
				int isOpenInt = obj.getInt("isOpen");
				if (isOpenInt != 1)
					isOpen = false;
			}
			
			if (obj.has("coupon"))
			{
				JSONObject couponObj = obj.getJSONObject("coupon");
				try
				{
					if (couponObj.has("status"))
					{
						String couponStatus = couponObj.getString("status");
						if (!couponStatus.equals("NO_DATA"))
							isHasCoupon = true;
					}
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	
	@SuppressLint("SimpleDateFormat")
	public void setCursor(Cursor c)
	{
		try
		{
			idx = c.getString(c.getColumnIndex("idx"));
			seqCode = c.getString(c.getColumnIndex("seqCode"));
			cityIdx = c.getString(c.getColumnIndex("cityIdx"));
			userName = c.getString(c.getColumnIndex("userName"));
			gender = c.getString(c.getColumnIndex("gender"));
			mfidx = c.getString(c.getColumnIndex("mfidx"));
			categoryId = c.getInt(c.getColumnIndex("category"));
			title = c.getString(c.getColumnIndex("title"));
			likeCount = c.getInt(c.getColumnIndex("likeCount"));
			bookmarkCount = c.getInt(c.getColumnIndex("bookmarkCount"));
			shareCount = c.getInt(c.getColumnIndex("shareCount"));
			reviewCount = c.getInt(c.getColumnIndex("reviewCount"));
			insertDateString = c.getString(c.getColumnIndex("insertDate"));
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			insertDate = format.parse(insertDateString);
			premiumIdx = c.getString(c.getColumnIndex("premiumIdx"));
			
			imageUrl = c.getString(c.getColumnIndex("offlineimage"));
		} catch (ParseException e)
		{
			e.printStackTrace();
		}
	}
}
