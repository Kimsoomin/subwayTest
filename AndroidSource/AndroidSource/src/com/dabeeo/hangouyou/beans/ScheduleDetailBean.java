package com.dabeeo.hangouyou.beans;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.database.Cursor;

public class ScheduleDetailBean
{
	public String idx;
	public String planCode;
	public String title;
	
	public String startDateString;
	public Date startDate;
	public String endDateString;
	public Date endDate;
	public String insertDateString;
	public Date insertDate;
	public String updateDateString;
	public Date updateDate;
	
	public int likeCount = 0;
	public int reviewCount = 0;
	public int bookmarkCount = 0;
	public int shareCount = 0;
	public int rate = 0;
	public int ownerUserSeq;
	public String userName;
	public String gender;
	public String mfidx;
	public int dayCount = 0;
	public String dayNum;
	public String distance;
	public String budgetTotal = "0";
	public String budget1 = "0";
	public String budget2 = "0";
	public String budget3 = "0";
	public String imageUrl;
	
	public ArrayList<ScheduleDayBean> days = new ArrayList<ScheduleDayBean>();
	
	
	@SuppressLint("SimpleDateFormat")
	public void setJSONObject(JSONObject obj)
	{
		try
		{
			if (obj.has("idx"))
				idx = obj.getString("idx");
			
			if (obj.has("planCode"))
				planCode = obj.getString("planCode");
			
			if (obj.has("title"))
				title = obj.getString("title");
			
			try
			{
				if (obj.has("likeCount"))
					likeCount = obj.getInt("likeCount");
				if (obj.has("reviewCount"))
					reviewCount = obj.getInt("reviewCount");
				if (obj.has("bookmarkCount"))
					bookmarkCount = obj.getInt("bookmarkCount");
				if (obj.has("shareCount"))
					shareCount = obj.getInt("shareCount");
				if (obj.has("rate"))
					rate = obj.getInt("rate");
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			
			if (obj.has("ownerUserSeq"))
				ownerUserSeq = obj.getInt("ownerUserSeq");
			
			if (obj.has("userName"))
				userName = obj.getString("userName");
			if (obj.has("gender"))
				gender = obj.getString("gender");
			if (obj.has("mfidx"))
				mfidx = obj.getString("mfidx");
			try
			{
				if (obj.has("dayCount"))
					dayCount = obj.getInt("dayCount");
			} catch (Exception e)
			{
				
			}
			if (obj.has("distance"))
				distance = obj.getString("distance");
			if (obj.has("budgetTotal"))
				budgetTotal = obj.getString("budgetTotal");
			if (obj.has("budget1"))
				budget1 = obj.getString("budget1");
			if (obj.has("budget2"))
				budget2 = obj.getString("budget2");
			if (obj.has("budget3"))
				budget3 = obj.getString("budget3");
			
			if (obj.has("image"))
			{
				JSONObject imgObject = obj.getJSONObject("image");
				imageUrl = imgObject.getString("url");
			}
			if (obj.has("startDate"))
			{
				try
				{
					startDateString = obj.getString("startDate");
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					startDate = format.parse(startDateString);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			
			if (obj.has("endDate"))
			{
				try
				{
					endDateString = obj.getString("endDate");
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					endDate = format.parse(endDateString);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			
			if (obj.has("insertDate"))
			{
				try
				{
					insertDateString = obj.getString("insertDate");
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					insertDate = format.parse(insertDateString);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			
			if (obj.has("updateDate"))
			{
				try
				{
					updateDateString = obj.getString("updateDate");
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					updateDate = format.parse(updateDateString);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			
			if (obj.has("days"))
			{
				if (obj.has("dayNum"))
					dayNum = obj.getString("dayNum");
				days.clear();
				JSONArray daysJSONArray = obj.getJSONArray("days");
				for (int i = 0; i < daysJSONArray.length(); i++)
				{
					ScheduleDayBean detailBean = new ScheduleDayBean();
					detailBean.setJSONObject(daysJSONArray.getJSONObject(i));
					days.add(detailBean);
				}
			}
		} catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
	
	
	public void setCursor(Cursor c)
	{
		try
		{
			idx = c.getString(c.getColumnIndex("idx"));
			planCode = c.getString(c.getColumnIndex("planCode"));
			title = c.getString(c.getColumnIndex("title"));
			startDateString = c.getString(c.getColumnIndex("startDate"));
			try
			{
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				startDate = format.parse(startDateString);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			endDateString = c.getString(c.getColumnIndex("endDate"));
			try
			{
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				endDate = format.parse(endDateString);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			insertDateString = c.getString(c.getColumnIndex("insertDate"));
			try
			{
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				insertDate = format.parse(insertDateString);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			updateDateString = c.getString(c.getColumnIndex("updateDate"));
			try
			{
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				updateDate = format.parse(updateDateString);
			} catch (Exception e)
			{
				e.printStackTrace();
			}

			likeCount = c.getInt(c.getColumnIndex("likeCount"));
			reviewCount = c.getInt(c.getColumnIndex("reviewCount"));
			bookmarkCount = c.getInt(c.getColumnIndex("bookmarkCount"));
			shareCount = c.getInt(c.getColumnIndex("shareCount"));
			rate = c.getInt(c.getColumnIndex("rate"));
			userName = c.getString(c.getColumnIndex("userName"));
			gender = c.getString(c.getColumnIndex("gender"));
			mfidx = c.getString(c.getColumnIndex("mfidx"));
			dayCount = c.getInt(c.getColumnIndex("dayCount"));
			distance = c.getString(c.getColumnIndex("distance"));
			budgetTotal = c.getString(c.getColumnIndex("budgetTotal"));
			budget1 = c.getString(c.getColumnIndex("budget1"));
			budget2 = c.getString(c.getColumnIndex("budget2"));
			budget3 = c.getString(c.getColumnIndex("budget3"));
			
			days.clear();
			JSONArray daysJSONArray = new JSONArray(c.getString(c.getColumnIndex("days")));
			for (int i = 0; i < daysJSONArray.length(); i++)
			{
				ScheduleDayBean detailBean = new ScheduleDayBean();
				detailBean.setJSONObject(daysJSONArray.getJSONObject(i));
				days.add(detailBean);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
