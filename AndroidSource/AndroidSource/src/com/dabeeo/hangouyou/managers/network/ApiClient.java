package com.dabeeo.hangouyou.managers.network;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.dabeeo.hangouyou.beans.PlaceBean;
import com.dabeeo.hangouyou.beans.PlaceDetailBean;
import com.dabeeo.hangouyou.beans.ReviewBean;
import com.dabeeo.hangouyou.beans.ScheduleBean;
import com.dabeeo.hangouyou.controllers.OfflineContentDatabaseManager;
import com.dabeeo.hangouyou.utils.SystemUtil;

public class ApiClient
{
  private String siteUrl = "http://gs.blinking.kr:8900/_libs/api.common.php";
  private HttpClient httpClient;
  private Context context;
  private OfflineContentDatabaseManager offlineDatabaseManager;
  
  
  public ApiClient(Context context)
  {
    this.context = context;
    offlineDatabaseManager = new OfflineContentDatabaseManager(context);
    httpClient = new HttpClient(context, siteUrl);
  }
  
  
  public void setSiteUrl(String siteUrl)
  {
    this.siteUrl = siteUrl;
  }
  
  
  public String getSiteUrl()
  {
    return siteUrl;
  }
  
  
  //오프라인 컨텐츠 
  public NetworkResult getOfflineContents()
  {
    return httpClient.requestGet(getSiteUrl() + "?v=m1&mode=OFFLINE_CONTENTS");
  }
  
  
  public NetworkResult getCategories()
  {
    return httpClient.requestGet(getSiteUrl() + "?v=m1&mode=CATEGORY_LIST");
  }
  
  
  public ArrayList<ScheduleBean> getTravelSchedules(int page)
  {
    ArrayList<ScheduleBean> beans = new ArrayList<ScheduleBean>();
    if (SystemUtil.isConnectNetwork(context))
    {
      NetworkResult result = httpClient.requestGet(getSiteUrl() + "?v=m1&mode=PLAN_LIST&p=" + page);
      try
      {
        JSONObject obj = new JSONObject(result.response);
        JSONArray array = obj.getJSONArray("plan");
        for (int i = 0; i < array.length(); i++)
        {
          JSONObject beanObj = array.getJSONObject(i);
          ScheduleBean bean = new ScheduleBean();
          bean.setJSONObject(beanObj);
          beans.add(bean);
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    else
      beans.addAll(offlineDatabaseManager.getTravelSchedules(page));
    return beans;
  }
  
  
  public NetworkResult getTravelScheduleDetail(String idx)
  {
    return httpClient.requestGet(getSiteUrl() + "?v=m1&mode=PLAN_VIEW&idx=" + idx);
  }
  
  
  public ArrayList<PlaceBean> getPlaceList(int page)
  {
    ArrayList<PlaceBean> places = new ArrayList<PlaceBean>();
    if (SystemUtil.isConnectNetwork(context))
    {
      NetworkResult result = httpClient.requestGet(getSiteUrl() + "?v=m1&mode=PLACE_LIST&p=" + page);
      try
      {
        JSONObject obj = new JSONObject(result.response);
        JSONArray arr = obj.getJSONArray("place");
        for (int i = 0; i < arr.length(); i++)
        {
          JSONObject objInArr = arr.getJSONObject(i);
          PlaceBean bean = new PlaceBean();
          bean.setJSONObject(objInArr);
          places.add(bean);
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      places.addAll(offlineDatabaseManager.getPlaceList(page, -1));
    }
    return places;
  }
  
  
  public ArrayList<PlaceBean> getPlaceList(int page, int categoryId)
  {
    ArrayList<PlaceBean> places = new ArrayList<PlaceBean>();
    if (SystemUtil.isConnectNetwork(context))
    {
      NetworkResult result = httpClient.requestGet(getSiteUrl() + "?v=m1&mode=PLACE_LIST&p=" + page + "&category=" + categoryId);
      try
      {
        JSONObject obj = new JSONObject(result.response);
        JSONArray arr = obj.getJSONArray("place");
        for (int i = 0; i < arr.length(); i++)
        {
          JSONObject objInArr = arr.getJSONObject(i);
          PlaceBean bean = new PlaceBean();
          bean.setJSONObject(objInArr);
          places.add(bean);
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    else
      places.addAll(offlineDatabaseManager.getPlaceList(page, categoryId));
    return places;
  }
  
  
  public PlaceDetailBean getPlaceDetail(String placeIdx)
  {
    PlaceDetailBean bean = new PlaceDetailBean();
    if (SystemUtil.isConnectNetwork(context))
    {
      NetworkResult result = httpClient.requestGet(getSiteUrl() + "?v=m1&mode=PLACE_VIEW&idx=" + placeIdx);
      try
      {
        JSONObject obj = new JSONObject(result.response);
        bean = new PlaceDetailBean();
        bean.setJSONObject(obj.getJSONObject("place"));
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    else
      bean = offlineDatabaseManager.getPlaceDetail(placeIdx);
    return bean;
  }
  
  
  public NetworkResult getPremiumDetail(int placeIdx)
  {
    return httpClient.requestGet(getSiteUrl() + "?v=m1&mode=PREMIUM_VIEW&idx=" + placeIdx);
  }
  
  
  public NetworkResult getPremiumList(int page)
  {
    return httpClient.requestGet(getSiteUrl() + "?v=m1&mode=PREMIUM_LIST&p=" + page);
  }
  
  
  public NetworkResult getTravelog(int page, String contentType)
  {
    return httpClient.requestGet(getSiteUrl() + "?v=m1&mode=TRAVELOG_LIST&p=" + page + "&contentType=" + contentType + "&pn=10");
  }
  
  
  public NetworkResult postReview(int rate, String content, int placeIdx, String placeType)
  {
    JSONObject object = new JSONObject();
    try
    {
      object.put("userSeq", 1);
      object.put("rate", rate);
      object.put("contents", content);
      object.put("parentIdx", placeIdx);
      object.put("parentType", placeType);
      
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return httpClient.requestPost(getSiteUrl() + "?v=m1&mode=REVIEW_INS", object.toString());
  }
  
  
  public NetworkResult getAllTicket(int page, String contentType)
  {
    return httpClient.requestGet(getSiteUrl() + "?v=m1&mode=TRAVELOG_LIST&p=" + page + "&contentType=" + contentType);
  }
  
  
  public NetworkResult getBoughtTicket(int page, String contentType)
  {
    return httpClient.requestGet(getSiteUrl() + "?v=m1&mode=TRAVELOG_LIST&p=" + page + "&contentType=" + contentType);
  }
  
  
  public ReviewBean getReviewDetail(String reviewIdx)
  {
    ReviewBean bean = new ReviewBean();
    if (SystemUtil.isConnectNetwork(context))
    {
      NetworkResult result = httpClient.requestGet(getSiteUrl() + "?v=m1&mode=REVIEW_VIEW&idx=" + reviewIdx);
      try
      {
        JSONObject obj = new JSONObject(result.response);
        bean = new ReviewBean();
        bean.setJSONObject(obj.getJSONObject("review"));
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    else
      bean = offlineDatabaseManager.getReview(reviewIdx);
    return bean;
  }
  
  
  public ArrayList<ReviewBean> getReviews(int page, String parentType, String parentIdx)
  {
    ArrayList<ReviewBean> beans = new ArrayList<ReviewBean>();
    if (SystemUtil.isConnectNetwork(context))
    {
      NetworkResult result = httpClient.requestGet(getSiteUrl() + "?v=m1&mode=REVIEW_LIST&parentType=" + parentType + "&parentIdx=" + parentIdx + "&p=" + page + "&pn=10");
      JSONObject obj;
      try
      {
        obj = new JSONObject(result.response);
        JSONArray arr = obj.getJSONArray("review");
        for (int i = 0; i < arr.length(); i++)
        {
          JSONObject reviewObj = arr.getJSONObject(i);
          ReviewBean bean = new ReviewBean();
          bean.setJSONObject(reviewObj);
          beans.add(bean);
        }
      }
      catch (JSONException e)
      {
        e.printStackTrace();
      }
    }
    else
      beans.addAll(offlineDatabaseManager.getReviews(page, parentType));
    return beans;
  }
  
  
  public NetworkResult getTicketDetail(String ticketId)
  {
//    return httpClient.requestGet(getSiteUrl() + "?v=m1&mode=TRAVELOG_LIST&p=" + ticketId);
    return new NetworkResult(true, null, 1);
  }
  
  
  public NetworkResult likeTicket(String ticketId)
  {
//    return httpClient.requestGet(getSiteUrl() + "?v=m1&mode=TRAVELOG_LIST&p=" + ticketId);
    return new NetworkResult(true, null, 1);
  }
  
  
  public NetworkResult addTicketToCart(String ticketId)
  {
//    return httpClient.requestGet(getSiteUrl() + "?v=m1&mode=TRAVELOG_LIST&p=" + ticketId);
    return new NetworkResult(true, null, 1);
  }
  
  
  public NetworkResult checkoutTicket(String ticketId)
  {
//    return httpClient.requestGet(getSiteUrl() + "?v=m1&mode=TRAVELOG_LIST&p=" + ticketId);
    return new NetworkResult(true, null, 1);
  }
  
  
  public NetworkResult getTicketOrderDetail(String orderId)
  {
//    return httpClient.requestGet(getSiteUrl() + "?v=m1&mode=TRAVELOG_LIST&p=" + ticketId);
    return new NetworkResult(true, null, 1);
  }
  
  
  public NetworkResult getAllCoupon(int page, String contentType)
  {
    return httpClient.requestGet(getSiteUrl() + "?v=m1&mode=TRAVELOG_LIST&p=" + page + "&contentType=" + contentType);
  }
  
  
  public NetworkResult getDownloadedCoupon(int page, String contentType)
  {
    return httpClient.requestGet(getSiteUrl() + "?v=m1&mode=TRAVELOG_LIST&p=" + page + "&contentType=" + contentType);
  }
  
  
  public NetworkResult getCouponDetail(String couponId)
  {
//    return httpClient.requestGet(getSiteUrl() + "?v=m1&mode=TRAVELOG_LIST&p=" + page + "&contentType=" + contentType);
    return new NetworkResult(true, null, 1);
  }
  
  
  public NetworkResult checkAlreadyHaveCoupon(String couponId)
  {
//    return httpClient.requestGet(getSiteUrl() + "?v=m1&mode=TRAVELOG_LIST&p=" + page + "&contentType=" + contentType);
    return new NetworkResult(true, null, 1);
  }
  
  
  public NetworkResult couponDownload(String couponId)
  {
//    return httpClient.requestGet(getSiteUrl() + "?v=m1&mode=TRAVELOG_LIST&p=" + page + "&contentType=" + contentType);
    return new NetworkResult(true, null, 1);
  }
  
  
  public NetworkResult useCoupon(String couponId)
  {
//    return httpClient.requestGet(getSiteUrl() + "?v=m1&mode=TRAVELOG_LIST&p=" + page + "&contentType=" + contentType);
    return new NetworkResult(true, null, 1);
  }
  
  
  public NetworkResult getRecommendSchedule(String type, int days, int year, int month, int dayOfMonth)
  {
    return httpClient.requestGet(getSiteUrl() + "v=m1&mode=PLAN_LIST&isRec=1");
  }
}
