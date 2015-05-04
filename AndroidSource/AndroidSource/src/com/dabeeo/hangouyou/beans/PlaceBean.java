package com.dabeeo.hangouyou.beans;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.util.Log;

public class PlaceBean
{
	public int idx;
	public String seqCode;
	public int cityIdx;
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
	public String userName;
	public String gender;
	public String mfidx;
	public double area;
	
	public String insertDateString;
	public Date insertDate;
	
	
	@SuppressLint("SimpleDateFormat")
	public void setJSONObject(JSONObject obj)
	{
		try
		{
			if (obj.has("seqCode"))
				seqCode = obj.getString("seqCode");
			
			if (obj.has("idx"))
				idx = obj.getInt("idx");
			
			if (obj.has("title"))
				title = obj.getString("title");
			
			if (obj.has("cityIdx"))
				cityIdx = obj.getInt("cityIdx");
			
			if (obj.has("category"))
				categoryId = obj.getInt("category");
			
			if (obj.has("lat"))
				lat = obj.getDouble("lat");
			if (obj.has("lng"))
				lng = obj.getDouble("lng");
			
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
			
			if (obj.has("userSeq"))
				userSeq = obj.getInt("userSeq");
			
			if (obj.has("userName"))
				userName = obj.getString("userName");
			if (obj.has("gender"))
				gender = obj.getString("gender");
			if (obj.has("mfidx"))
				mfidx = obj.getString("mfidx");
			
			if (obj.has("area"))
				area = obj.getDouble("area");
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
}
