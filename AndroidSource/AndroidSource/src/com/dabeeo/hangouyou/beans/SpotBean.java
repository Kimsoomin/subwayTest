package com.dabeeo.hangouyou.beans;

import java.io.Serializable;

import org.json.JSONObject;

public class SpotBean implements Serializable
{
    public String dayNum;
    public int type;
    public int slotNum;
    public int time;
    public String cityIdx;
    public String idx;

    public String startTime;
    public String endTime;

    public int budget;
    public String currency;
    public int transType;
    public int transTime;
    public String transMemo;
    public String memo;

    public int nextIdx;
    public String title;
    public double lat;
    public double lng;

    public String distance;
    public String address;
    public String ownerUserSeq;
    public int isDelete;
    public int isOpen;
    public int reviewCount;
    public String imageUrl;


    public void setJSONObject(JSONObject obj)
    {
        try
        {
            if(obj.has("dayNum"))
                dayNum = obj.getString("dayNum");
            if (obj.has("type"))
                type = obj.getInt("type");
            if (obj.has("slotNum"))
                slotNum = obj.getInt("slotNum");
            if (obj.has("time"))
                time = obj.getInt("time");

            if (obj.has("idx"))
                idx = obj.getString("idx");
            if (obj.has("cityIdx"))
                cityIdx = obj.getString("cityIdx");
            if (obj.has("budget"))
                budget = obj.getInt("budget");
            if (obj.has("currency"))
                currency = obj.getString("currency");

            if (obj.has("transType"))
                transType = obj.getInt("transType");
            if (obj.has("transTime"))
                transTime = obj.getInt("transTime");
            if (obj.has("transMemo"))
                transMemo = obj.getString("transMemo");
            if (obj.has("memo"))
                memo = obj.getString("memo");

            if (obj.has("nextIdx"))
                nextIdx = obj.getInt("nextIdx");
            if (obj.has("title"))
                title = obj.getString("title");

            if (obj.has("distance"))
                distance = obj.getString("distance");
            if (obj.has("address"))
                address = obj.getString("address");
            if (obj.has("ownerUserSeq"))
                ownerUserSeq = obj.getString("ownerUserSeq");

            try
            {
                if (obj.has("isDelete"))
                    isDelete = obj.getInt("isDelete");
                if (obj.has("isOpen"))
                    isOpen = obj.getInt("isOpen");
                if (obj.has("reviewCount"))
                    reviewCount = obj.getInt("reviewCount");
            }
            catch (Exception e)
            {
            }

            if (obj.has("images"))
            {
                JSONObject imageObj = obj.getJSONObject("images");
                imageUrl = imageObj.getString("url");
            }

            try
            {
                if (obj.has("lat"))
                    lat = obj.getDouble("lat");
                if (obj.has("lng"))
                    lng = obj.getDouble("lng");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            startTime = obj.getString("startTime");
            endTime = obj.getString("endTime");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }  
}
