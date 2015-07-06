package com.dabeeo.hanhayou.managers.network;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.beans.PlaceBean;
import com.dabeeo.hanhayou.beans.PlaceDetailBean;
import com.dabeeo.hanhayou.beans.ReviewBean;
import com.dabeeo.hanhayou.beans.ScheduleBean;
import com.dabeeo.hanhayou.beans.ScheduleDetailBean;
import com.dabeeo.hanhayou.beans.SearchResultBean;
import com.dabeeo.hanhayou.controllers.OfflineContentDatabaseManager;
import com.dabeeo.hanhayou.managers.FileManager;
import com.dabeeo.hanhayou.managers.PreferenceManager;
import com.dabeeo.hanhayou.utils.SystemUtil;

public class ApiClient
{
  private String siteUrl = "http://gs.blinking.kr:8900/_libs/api.common.php";
//  private String testUrl = "http://10.53.13.18/_libs/api.common.php";
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
  
  
  //오프라인 컨텐츠 update 관련 - updateDate 로 판별 
  public NetworkResult updateOfflineContents()
  {
    return httpClient.requestGet(getSiteUrl() + "v=m1&mode=OFFLINE_CONTENTS");
  }
  
  
  //프로모션 
  public NetworkResult getPoromotion()
  {
    return httpClient.requestGet(getSiteUrl() + "?v=m1&mode=PROMOTION_LIST");
  }
  
  
  public NetworkResult getCategories()
  {
    return httpClient.requestGet(getSiteUrl() + "?v=m1&mode=CATEGORY_LIST");
  }
  
  
  public ArrayList<ScheduleBean> getTravelSchedules(int page, int dayCount)
  {
    ArrayList<ScheduleBean> beans = new ArrayList<ScheduleBean>();
    if (SystemUtil.isConnectNetwork(context))
    {
      String url = getSiteUrl() + "?v=m1&mode=PLAN_LIST&p=" + page;
      if (dayCount != -1)
        url += "&dayCount=" + dayCount;
      NetworkResult result = httpClient.requestGet(url);
      try
      {
        JSONObject obj = new JSONObject(result.response);
        if (!obj.has("plan"))
          return beans;
        
        JSONArray array = obj.getJSONArray("plan");
        for (int i = 0; i < array.length(); i++)
        {
          JSONObject beanObj = array.getJSONObject(i);
          ScheduleBean bean = new ScheduleBean();
          bean.setJSONObject(beanObj);
          if (dayCount == -1)
            beans.add(bean);
          else
          {
            if (bean.dayCount == dayCount)
              beans.add(bean);
          }
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      ArrayList<ScheduleBean> tempArray = offlineDatabaseManager.getTravelSchedules(page - 1);
      for (int i = 0; i < tempArray.size(); i++)
      {
        ScheduleBean bean = tempArray.get(i);
        if (dayCount == -1)
          beans.add(bean);
        else
        {
          if (bean.dayCount == dayCount)
            beans.add(bean);
        }
      }
    }
    return beans;
  }
  
  
  public ArrayList<ScheduleBean> getMyTravelSchedules(int dayCount)
  {
    ArrayList<ScheduleBean> beans = new ArrayList<ScheduleBean>();
    if (SystemUtil.isConnectNetwork(context))
    {
      String url = getSiteUrl() + "?v=m1&mode=MY_PLAN_LIST&ownerUserSeq=" + PreferenceManager.getInstance(context).getUserSeq();
      NetworkResult result = httpClient.requestGet(url);
      FileManager.getInstance(context).writeFile(FileManager.FILE_MY_PLAN, result.response);
      try
      {
        JSONObject obj = new JSONObject(result.response);
        if (!obj.has("plan"))
          return beans;
        
        JSONArray array = obj.getJSONArray("plan");
        for (int i = 0; i < array.length(); i++)
        {
          JSONObject beanObj = array.getJSONObject(i);
          ScheduleBean bean = new ScheduleBean();
          bean.setJSONObject(beanObj);
          if (dayCount == -1)
            beans.add(bean);
          else
          {
            if (bean.dayCount == dayCount)
              beans.add(bean);
          }
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      try
      {
        JSONObject obj = new JSONObject(FileManager.getInstance(context).readFile(FileManager.FILE_MY_PLAN));
        if (!obj.has("plan"))
          return beans;
        
        JSONArray array = obj.getJSONArray("plan");
        for (int i = 0; i < array.length(); i++)
        {
          JSONObject beanObj = array.getJSONObject(i);
          ScheduleBean bean = new ScheduleBean();
          bean.setJSONObject(beanObj);
          if (dayCount == -1)
            beans.add(bean);
          else
          {
            if (bean.dayCount == dayCount)
              beans.add(bean);
          }
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    return beans;
  }
  
  
  public ArrayList<ScheduleBean> getBookmarkedSchedules(int dayCount)
  {
    ArrayList<ScheduleBean> beans = new ArrayList<ScheduleBean>();
    if (SystemUtil.isConnectNetwork(context))
    {
      String url = getSiteUrl() + "?v=m1&mode=PLAN_LIST&isBookmark=1&userSeq=" + PreferenceManager.getInstance(context).getUserSeq();
      
      NetworkResult result = httpClient.requestGet(url);
      try
      {
        JSONObject obj = new JSONObject(result.response);
        if (!obj.has("plan"))
          return beans;
        
        JSONArray array = obj.getJSONArray("plan");
        for (int i = 0; i < array.length(); i++)
        {
          JSONObject beanObj = array.getJSONObject(i);
          ScheduleBean bean = new ScheduleBean();
          bean.setJSONObject(beanObj);
          if (dayCount == -1)
            beans.add(bean);
          else
          {
            if (bean.dayCount == dayCount)
              beans.add(bean);
          }
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    return beans;
  }
  
  
  public ScheduleDetailBean getTravelScheduleDetail(String idx)
  {
    ScheduleDetailBean bean = new ScheduleDetailBean();
    if (SystemUtil.isConnectNetwork(context))
    {
      String url = getSiteUrl() + "?v=m1&mode=PLAN_VIEW&idx=" + idx;
      if (!TextUtils.isEmpty(PreferenceManager.getInstance(context).getUserSeq()))
        url += "&userSeq=" + PreferenceManager.getInstance(context).getUserSeq();
      NetworkResult result = httpClient.requestGet(url);
      if (result.isSuccess)
      {
        try
        {
          JSONObject obj = new JSONObject(result.response);
          bean = new ScheduleDetailBean();
          bean.setJSONObject(obj.getJSONObject("plan"));
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
        
      }
    }
    else
      bean = offlineDatabaseManager.getTravelScheduleDetailBean(idx);
    return bean;
  }
  
  
  public ScheduleDetailBean getMyTravelScheduleDetail(String idx)
  {
    ScheduleDetailBean bean = new ScheduleDetailBean();
    if (SystemUtil.isConnectNetwork(context))
    {
      String url = getSiteUrl() + "?v=m1&mode=PLAN_VIEW&idx=" + idx;
      if (!TextUtils.isEmpty(PreferenceManager.getInstance(context).getUserSeq()))
        url += "&userSeq=" + PreferenceManager.getInstance(context).getUserSeq();
      NetworkResult result = httpClient.requestGet(url);
      if (result.isSuccess)
      {
        try
        {
          JSONObject obj = new JSONObject(result.response);
          bean = new ScheduleDetailBean();
          bean.setJSONObject(obj.getJSONObject("plan"));
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
        
      }
    }
    else
      bean = offlineDatabaseManager.getMYTravelScheduleDetailBean(idx);
    return bean;
  }
  
  
  /**
   * 장소 목록 - 인기있는
   * 
   * @param page
   * @param categoryId
   * @return
   */
  public ArrayList<PlaceBean> getPlaceListByPopular(int page, int categoryId)
  {
    if (SystemUtil.isConnectNetwork(context))
    {
      String url = getSiteUrl() + "?v=m1&mode=PLACE_LIST&p=" + page + "&sort=Popular&category=" + categoryId;
      return getPlaceList(url);
    }
    else
      return offlineDatabaseManager.getPlaceList(categoryId, true);
  }
  
  
  /**
   * 장소 목록 - 북마크한 장소
   * 
   * @param page
   * @param categoryId
   * @return
   */
  public ArrayList<PlaceBean> getPlaceListByBookmarked(int page, int categoryId)
  {
    ArrayList<PlaceBean> bookmarkArray = getBookmarkedPlaceList();
    ArrayList<PlaceBean> tempArray = new ArrayList<PlaceBean>();
    for (int i = 0; i < bookmarkArray.size(); i++)
    {
      if (bookmarkArray.get(i).categoryId == categoryId)
        tempArray.add(bookmarkArray.get(i));
    }
    return tempArray;
  }
  
  
  /**
   * 장소 목록 - 내가 등록한 장소
   * 
   * @param page
   * @param categoryId
   * @return
   */
  public ArrayList<PlaceBean> getPlaceListByAddedByMe(int page, int categoryId)
  {
    ArrayList<PlaceBean> lists = getMyPlaceList();
    ArrayList<PlaceBean> tempArray = new ArrayList<PlaceBean>();
    for (int i = 0; i < lists.size(); i++)
    {
      if (lists.get(i).categoryId == categoryId)
        tempArray.add(lists.get(i));
    }
    return tempArray;
  }
  
  
  public ArrayList<PlaceBean> getPlaceList(int page, int categoryId)
  {
    ArrayList<PlaceBean> places = new ArrayList<PlaceBean>();
    
    if (SystemUtil.isConnectNetwork(context))
    {
      String url = getSiteUrl() + "?v=m1&mode=PLACE_LIST&p=" + page;
      
      if (categoryId != -1)
        url += "&category=" + categoryId;
      
      places.addAll(getPlaceList(url));
    }
    else
      places.addAll(offlineDatabaseManager.getPlaceList(categoryId));
    return places;
  }
  
  
  public ArrayList<PlaceBean> getMyPlaceList()
  {
    ArrayList<PlaceBean> places = new ArrayList<PlaceBean>();
    
    if (SystemUtil.isConnectNetwork(context))
    {
      String url = getSiteUrl() + "?v=m1&mode=MY_PLACE_LIST";
      url += "&ownerUserSeq=" + PreferenceManager.getInstance(context).getUserSeq();
      
      places.addAll(getPlaceList(url));
    }
    else
    {
      String localMyPlacesString = FileManager.getInstance(context).readFile(FileManager.FILE_MY_PLACE);
      try
      {
        JSONObject obj = new JSONObject(localMyPlacesString);
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
    
    return places;
  }
  
  
  public ArrayList<PlaceBean> getBookmarkedPlaceList()
  {
    ArrayList<PlaceBean> places = new ArrayList<PlaceBean>();
    
    if (SystemUtil.isConnectNetwork(context))
    {
      String url = getSiteUrl() + "?v=m1&mode=PLACE_LIST&isBookmark=1&userSeq=" + PreferenceManager.getInstance(context).getUserSeq();
      places.addAll(getPlaceList(url));
    }
    return places;
  }
  
  
  // TODO 오프라인 처리하기
  public ArrayList<PlaceBean> getPlaceList(String url)
  {
    ArrayList<PlaceBean> places = new ArrayList<PlaceBean>();
    
    NetworkResult result = httpClient.requestGet(url);
    if (url.contains("MY"))
      FileManager.getInstance(context).writeFile(FileManager.FILE_MY_PLACE, result.response);
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
    
    return places;
  }
  
  
  public PlaceDetailBean getPlaceDetail(String placeIdx)
  {
    PlaceDetailBean bean = new PlaceDetailBean();
    if (SystemUtil.isConnectNetwork(context))
    {
      String url = getSiteUrl() + "?v=m1&mode=PLACE_VIEW&idx=" + placeIdx;
      if (!TextUtils.isEmpty(PreferenceManager.getInstance(context).getUserSeq()))
        url += "&userSeq=" + PreferenceManager.getInstance(context).getUserSeq();
      NetworkResult result = httpClient.requestGet(url);
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
  
  
  public NetworkResult getPremiumDetail(String placeIdx)
  {
    String url = getSiteUrl() + "?v=m1&mode=PREMIUM_VIEW&lang=zh_cn&idx=" + placeIdx;
    if (!TextUtils.isEmpty(PreferenceManager.getInstance(context).getUserSeq()))
      url += "&userSeq=" + PreferenceManager.getInstance(context).getUserSeq();
    return httpClient.requestGet(url);
  }
  
  
  public NetworkResult getPremiumList(int page, int area)
  {
    return httpClient.requestGet(getSiteUrl() + "?v=m1&mode=PREMIUM_LIST&lang=zh_cn&p=" + page + "&area=" + area);
  }
  
  
  public NetworkResult getTravelog(int page, String contentType)
  {
    return httpClient.requestGet(getSiteUrl() + "?v=m1&mode=TRAVELOG_LIST&p=" + page + "&contentType=" + contentType + "&pn=10");
  }
  
  
  //내 장소, 내 일정 삭제
  public NetworkResult deleteMyPlace(String idx, String ownerUserSeq)
  {
    return httpClient.requestPost(getSiteUrl() + "?v=m1&mode=PLACE_DEL&idx=" + idx + "&userSeq=" + ownerUserSeq);
  }
  
  
  public NetworkResult deleteMyPlan(String idx, String ownerUserSeq)
  {
    return httpClient.requestPost(getSiteUrl() + "?v=m1&mode=PLAN_DEL&idx=" + idx + "&userSeq=" + ownerUserSeq);
  }
  
  
  //user_LOG
  public NetworkResult postReviewRate(String parentType, String parentIdx, String ownerUserSeq, int rate, String contents)
  {
//    mode: "REVIEW_INS",
//    parentType: [부모컨텐츠종류],
//    parentIdx: [부모컨텐츠idx],
//    userSeq: [회원번호],
//    rate: [평점],1/3/5
//    contents: [내용],
//    regDate: [등록일자]
    if (TextUtils.isEmpty(contents))
      return httpClient.requestPost(getSiteUrl() + "?v=m1&mode=REVIEW_INS&parentType=" + parentType + "&parentIdx=" + parentIdx + "&userSeq=" + ownerUserSeq + "&rate=" + rate);
    else
      return httpClient.requestPost(getSiteUrl() + "?v=m1&mode=REVIEW_INS&parentType=" + parentType + "&parentIdx=" + parentIdx + "&userSeq=" + ownerUserSeq + "&rate=" + rate + "&contents="
          + contents);
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
          offlineDatabaseManager.insert(OfflineContentDatabaseManager.TABLE_NAME_REVIEW, reviewObj);
        }
      }
      catch (JSONException e)
      {
      }
    }
    else
      beans.addAll(offlineDatabaseManager.getReviews(page, parentIdx));
    return beans;
  }
  
  
  public boolean removeReview(String idx)
  {
    NetworkResult result = httpClient.requestGet(getSiteUrl() + "?v=m1&mode=REVIEW_DEL&idx=" + idx);
    boolean isSuccess = false;
    try
    {
      JSONObject obj = new JSONObject(result.response);
      if (obj.getString("status").equals("OK"))
        isSuccess = true;
    }
    catch (JSONException e)
    {
      e.printStackTrace();
    }
    return isSuccess;
  }
  
  
  public NetworkResult declareReview(String idx, String reason)
  {
    boolean isSuccess = false;
    try
    {
      NetworkResult result = httpClient.requestGet(getSiteUrl() + "?v=m1&mode=CLAIM_INS&parentType=review&parentIdx=" + idx + "&userSeq=" + PreferenceManager.getInstance(context).getUserSeq()
          + "&reason=" + URLEncoder.encode(reason, "utf-8"));
      
      JSONObject obj = new JSONObject(result.response);
      if (obj.getString("status").equals("OK"))
        isSuccess = true;
      return result;
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return null;
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
  
  
  /**
   * 추천 자동일정 만들기
   * 
   * @param startDate
   *          2015-06-15
   * @param dayCount
   *          1, 2, 3
   * @param theme
   *          0: 쇼핑, 1: 한류, 2: 관광, 3: 미식, 4: 휴양, 5: 랜덤
   * @return
   */
  public NetworkResult getCreateRecommendSchedule(String startDate, int dayCount, int theme)
  {
    return httpClient.requestGet(getSiteUrl() + "?v=m1&mode=PLAN_SMART&startDate=" + startDate + "&dayCount=" + dayCount + "&theme=" + theme);
  }
  
  
  /**
   * 일정 비공개 / 공개로 변경하기
   * 
   * @param isOpen
   *          0 : 비공개, 1 : 공개
   */
  public NetworkResult requestOpenMySchedule(String idx, String isOpen)
  {
    return httpClient.requestGet(getSiteUrl() + "?v=m1&mode=PLAN_OPEN&userSeq=" + PreferenceManager.getInstance(context).getUserSeq() + "&idx=" + idx + "&isOpen=" + isOpen);
  }
  
  
  /**
   * 만들어진 자동추천일정을 내 일정으로 만들기
   * 
   * @param startDate
   *          2015-06-20
   * @param planIdx
   *          279
   * @param dayCount
   *          2
   * @return
   */
  public NetworkResult completeCreateRecommendSchedule(String startDate, String idx, int dayCount, String title)
  {
    return httpClient.requestGet(getSiteUrl() + "?v=m1&mode=PLAN_SMART_SAVE&startDate=" + startDate + "&dayCount=" + dayCount + "&planIdx=" + idx + "&userSeq="
        + PreferenceManager.getInstance(context).getUserSeq() + "&title=" + title);
  }
  
  
  //공지사항
  public NetworkResult getNotices()
  {
    return httpClient.requestGet(getSiteUrl() + "?v=m1&mode=NOTICE_LIST");
  }
  
  
  //검색관련 API
  public NetworkResult searchPopular()
  {
    return httpClient.requestPost(getSiteUrl() + "?v=m1&mode=SEARCH_POPULAR");
  }
  
  
  public ArrayList<SearchResultBean> searchAuto(String keyword)
  {
    ArrayList<SearchResultBean> results = new ArrayList<SearchResultBean>();
    if (TextUtils.isEmpty(keyword))
      return results;
    
    if (SystemUtil.isConnectNetwork(context))
    {
      String status = "";
      NetworkResult result = null;
      try
      {
        result = httpClient.requestPost(getSiteUrl() + "?v=m1&mode=SEARCH_AUTO&keyword=" + URLEncoder.encode(keyword, "utf-8"));
      }
      catch (UnsupportedEncodingException e1)
      {
        e1.printStackTrace();
      }
      
      try
      {
        JSONObject jsonObject = new JSONObject(result.response);
        if (jsonObject.has("status"))
        {
          status = jsonObject.getString("status");
        }
        
        if (status.equals("OK"))
        {
          JSONArray jsonArray = jsonObject.getJSONArray("data");
          for (int i = 0; i < jsonArray.length(); i++)
          {
            SearchResultBean bean = new SearchResultBean();
            JSONObject obj = jsonArray.getJSONObject(i);
            bean.text = obj.getString("title");
            bean.idx = obj.getString("idx");
            bean.setLogType(obj.getString("logType"));
            results.add(bean);
          }
        }
      }
      catch (JSONException e)
      {
        status = "";
        e.printStackTrace();
      }
    }
    else
      return offlineDatabaseManager.getSearchAuto(keyword);
    
    return results;
  }
  
  
  public ArrayList<SearchResultBean> searchResult(String keyword)
  {
    return searchResult(keyword, -1);
  }
  
  
  public ArrayList<SearchResultBean> searchResult(String keyword, int limit)
  {
    ArrayList<SearchResultBean> array = new ArrayList<SearchResultBean>();
    if (TextUtils.isEmpty(keyword))
      return array;
    
    if (SystemUtil.isConnectNetwork(context))
    {
      NetworkResult result = null;
      try
      {
        if (TextUtils.isEmpty(PreferenceManager.getInstance(context).getUserSeq()))
          result = httpClient.requestPost(getSiteUrl() + "?v=m1&mode=SEARCH_RESULT&keyword=" + URLEncoder.encode(keyword, "utf-8"));
        else
          result = httpClient.requestPost(getSiteUrl() + "?v=m1&mode=SEARCH_RESULT&keyword=" + URLEncoder.encode(keyword, "utf-8") + "&userSeq=" + PreferenceManager.getInstance(context).getUserSeq());
        
      }
      catch (UnsupportedEncodingException e1)
      {
        e1.printStackTrace();
      }
      
      String status = "";
      try
      {
        JSONObject jsonObject = new JSONObject(result.response);
        if (jsonObject.has("status"))
        {
          status = jsonObject.getString("status");
        }
        
        if (status.equals("OK"))
        {
          ArrayList<SearchResultBean> placeList = new ArrayList<SearchResultBean>();
          ArrayList<SearchResultBean> planList = new ArrayList<SearchResultBean>();
          ArrayList<SearchResultBean> premiumList = new ArrayList<SearchResultBean>();
          
          JSONArray jsonArray = jsonObject.getJSONArray("data");
          
          for (int i = 0; i < jsonArray.length(); i++)
          {
            SearchResultBean bean = new SearchResultBean();
            JSONObject obj = jsonArray.getJSONObject(i);
            bean.setLogType(obj.getString("logType"));
            bean.text = obj.getString("title");
            bean.idx = obj.getString("idx");
            
            if (bean.type == SearchResultBean.TYPE_PLACE)
              placeList.add(bean);
            else if (bean.type == SearchResultBean.TYPE_SCHEDULE)
              planList.add(bean);
            else if (bean.type == SearchResultBean.TYPE_RECOMMEND_SEOUL)
              premiumList.add(bean);
          }
          
          Log.w("WARN", "검색결과 장소 사이즈 : " + placeList.size());
          Log.w("WARN", "검색결과 일정 사이즈 : " + planList.size());
          Log.w("WARN", "검색결과 추천서울 사이즈 : " + premiumList.size());
          
          SearchResultBean titleBean = new SearchResultBean();
          if (placeList.size() > 0)
          {
            titleBean = new SearchResultBean();
            titleBean.addPlaceTitle(context.getString(R.string.term_place), jsonObject.getInt("placeCount"));
            titleBean.type = SearchResultBean.TYPE_PLACE;
            array.add(titleBean);
            
            if (placeList.size() > 0)
            {
              int maxCount = limit;
              if (limit == -1)
                maxCount = placeList.size();
              for (int i = 0; i < maxCount; i++)
              {
                try
                {
                  SearchResultBean listBean = new SearchResultBean();
                  listBean.idx = placeList.get(i).idx;
                  listBean.addText(placeList.get(i).text, placeList.get(i).type);
                  array.add(listBean);
                }
                catch (Exception e)
                {
                }
              }
            }
          }
          
          if (premiumList.size() > 0)
          {
            titleBean = new SearchResultBean();
            titleBean.addPlaceTitle(context.getString(R.string.term_strategy_seoul), jsonObject.getInt("premiumCount"));
            titleBean.type = SearchResultBean.TYPE_RECOMMEND_SEOUL;
            array.add(titleBean);
            
            if (premiumList.size() > 0)
            {
              int maxCount = limit;
              if (limit == -1)
                maxCount = premiumList.size();
              for (int i = 0; i < maxCount; i++)
              {
                try
                {
                  SearchResultBean listBean = new SearchResultBean();
                  listBean.idx = premiumList.get(i).idx;
                  listBean.addText(premiumList.get(i).text, premiumList.get(i).type);
                  array.add(listBean);
                }
                catch (Exception e)
                {
                }
              }
            }
          }
          
          if (planList.size() > 0)
          {
            titleBean = new SearchResultBean();
            titleBean.addPlaceTitle(context.getString(R.string.term_travel_schedule), jsonObject.getInt("planCount"));
            titleBean.type = SearchResultBean.TYPE_SCHEDULE;
            array.add(titleBean);
            
            if (planList.size() > 0)
            {
              int maxCount = limit;
              if (limit == -1)
                maxCount = planList.size();
              for (int i = 0; i < maxCount; i++)
              {
                try
                {
                  SearchResultBean listBean = new SearchResultBean();
                  listBean.idx = planList.get(i).idx;
                  listBean.addText(planList.get(i).text, planList.get(i).type);
                  array.add(listBean);
                }
                catch (Exception e)
                {
                }
              }
            }
          }
        }
      }
      catch (JSONException e)
      {
        status = "";
        e.printStackTrace();
      }
      return array;
    }
    else
    {
      return offlineDatabaseManager.getSearchResult(keyword, limit);
    }
  }
  
  
  /**
   * 사용자 로그 등록 (좋아요, 북마크, 공유, 위시)
   * 
   * @param parentType
   *          = "place": 장소,"plan": 일정, "review": 리뷰, "product": 상품
   * @param usedType
   *          = "L": 좋아요, "B": 북마크, "S": 공유, "W": 위시
   */
  public NetworkResult setUsedLog(String ownerUserSeq, String prentIdx, String parentType, String usedType)
  {
    return httpClient.requestPost(getSiteUrl() + "?v=m1&mode=SET_USEDLOG&userSeq=" + ownerUserSeq + "&parentIdx=" + prentIdx + "&parentType=" + parentType + "&usedType=" + usedType);
  }
  
  
  //회원관련 API
  public NetworkResult userLogin(String Email, String Password)
  {
    return httpClient.requestPost(getSiteUrl() + "?v=m1&mode=USER_LOGIN&userEmail=" + Email + "&userPw=" + Password);
  }
  
  
  public NetworkResult userIdDuplicateCheck(String Email)
  {
    return httpClient.requestPost(getSiteUrl() + "?v=m1&mode=USER_IDCHECK&userEmail=" + Email);
  }
  
  
  public NetworkResult userNameDuplicateCheck(String userName)
  {
    return httpClient.requestPost(getSiteUrl() + "?v=m1&mode=USER_NAMECHECK&userName=" + userName);
  }
  
  
  public NetworkResult userJoin(String Email, String Password, String userName, String phoneNum, String gender, String birthday, String agreeEmail, String agreeSms)
  {
    return httpClient.requestPost(getSiteUrl() + "?v=m1&mode=USER_INS&userEmail=" + Email + "&userPw=" + Password + "&userName=" + userName + "&phone=" + phoneNum + "&gender=" + gender + "&birthday="
        + birthday + "&agreeEmail=" + agreeEmail + "&agreeSms=" + agreeSms);
  }
  
  
  public NetworkResult userEmailKeycheck(String userSeq, String Key)
  {
    return httpClient.requestPost(getSiteUrl() + "?v=m1&mode=USER_EMAILKEYCHECK&userSeq=" + userSeq + "&key=" + Key);
  }
  
  
  public NetworkResult userEmailKeyResend(String userEmail)
  {
    return httpClient.requestPost(getSiteUrl() + "?v=m1&mode=USER_EMAILKEY_RESEND&userEmail=" + userEmail);
  }
  
  
  public NetworkResult userNameModify(String userSeq, String userName)
  {
    return httpClient.requestPost(getSiteUrl() + "?v=m1&mode=USER_INFO_MOD&userSeq=" + userSeq + "&userName=" + userName);
  }
  
  
  public NetworkResult userPasswordModify(String userSeq, String userName, String userOldPw, String userNewPw)
  {
    return httpClient.requestPost(getSiteUrl() + "?v=m1&mode=USER_INFO_MOD&userSeq=" + userSeq + "&userName=" + userName + "&userOldPw=" + userOldPw + "&userNewPw=" + userNewPw);
  }
  
  
  public NetworkResult userInfoinquiry(String userSeq)
  {
    return httpClient.requestPost(getSiteUrl() + "?v=m1&mode=USER_INFO&userSeq=" + userSeq);
  }
  
  
  public NetworkResult userTempPasswordGet(String userEmail)
  {
    return httpClient.requestPost(getSiteUrl() + "?v=m1&mode=USER_FINDPW&userEmail=" + userEmail);
  }
  
  
  public NetworkResult uploadReviewImage(String idx, String filePath)
  {
    return httpClient.requestPostWithFile(getSiteUrl() + "?v=m1&mode=FILE_UPLOAD&folderName=profile&seqCode=" + idx + "&userSeq=" + PreferenceManager.getInstance(context).getUserSeq(), filePath);
  }
  
  
  public NetworkResult uploadProfileImage(String filePath)
  {
    return httpClient.requestPostWithFile(getSiteUrl() + "?v=m1&mode=FILE_UPLOAD&folderName=profile&seqCode=" + PreferenceManager.getInstance(context).getUserSeq() + "&userSeq="
        + PreferenceManager.getInstance(context).getUserSeq(), filePath);
  }
}
