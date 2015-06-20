package com.dabeeo.hanhayou.managers.network;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.dabeeo.hanhayou.beans.PlaceBean;
import com.dabeeo.hanhayou.beans.PlaceDetailBean;
import com.dabeeo.hanhayou.beans.ReviewBean;
import com.dabeeo.hanhayou.beans.ScheduleBean;
import com.dabeeo.hanhayou.beans.ScheduleDetailBean;
import com.dabeeo.hanhayou.controllers.OfflineContentDatabaseManager;
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
  
  
  //오프라인 컨텐츠 update 관련 - 추후 동기화 시나리오 적용 필요
  public NetworkResult updateOfflineContents()
  {
    return httpClient.requestGet(getSiteUrl() + "v=m1&mode=OFFLINE_CONTENTS");
  }
  
  
  public NetworkResult getCategories()
  {
    return httpClient.requestGet(getSiteUrl() + "?v=m1&mode=CATEGORY_LIST");
  }
  
  
  public ArrayList<ScheduleBean> getTravelSchedules(int page, int daycount)
  {
    ArrayList<ScheduleBean> beans = new ArrayList<ScheduleBean>();
    if (SystemUtil.isConnectNetwork(context))
    {
      String url = getSiteUrl() + "?v=m1&mode=PLAN_LIST&p=" + page + "&dayCount=" + daycount;
      
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
  
  
  public ArrayList<ScheduleBean> getMyTravelSchedules()
  {
    ArrayList<ScheduleBean> beans = new ArrayList<ScheduleBean>();
    if (SystemUtil.isConnectNetwork(context))
    {
      String url = getSiteUrl() + "?v=m1&mode=MY_PLAN_LIST&ownerUserSeq=" + PreferenceManager.getInstance(context).getUserSeq();
      
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
          beans.add(bean);
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
//    else
//      beans.addAll(offlineDatabaseManager.getTravelSchedules(page));
    return beans;
  }
  
  
  public ScheduleDetailBean getTravelScheduleDetail(String idx)
  {
    ScheduleDetailBean bean = new ScheduleDetailBean();
    if (SystemUtil.isConnectNetwork(context))
    {
      NetworkResult result = httpClient.requestGet(getSiteUrl() + "?v=m1&mode=PLAN_VIEW&idx=" + idx);
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
    {
      bean = offlineDatabaseManager.getTravelScheduleDetailBean(idx);
    }
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
    String url = getSiteUrl() + "?v=m1&mode=PLACE_LIST&p=" + page + "&sort=Popular&category=" + categoryId;
    return getPlaceList(url);
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
    String url = getSiteUrl() + "?v=m1&mode=PLACE_LIST&p=" + page + "&category=" + categoryId + "&isBookmarked=1";
    return getPlaceList(url);
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
    String url = getSiteUrl() + "?v=m1&mode=PLACE_LIST&p=" + page + "&category=" + categoryId + "&userSeq=" + PreferenceManager.getInstance(context).getUserSeq();
    return getPlaceList(url);
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
      places.addAll(offlineDatabaseManager.getPlaceList(page, categoryId));
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
//    else
//      places.addAll(offlineDatabaseManager.getPlaceList(page, categoryId));
    return places;
  }
  
  
  // TODO 오프라인 처리하기
  public ArrayList<PlaceBean> getPlaceList(String url)
  {
    ArrayList<PlaceBean> places = new ArrayList<PlaceBean>();
    
    NetworkResult result = httpClient.requestGet(url);
    
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
    return httpClient.requestGet(getSiteUrl() + "?v=m1&mode=PREMIUM_VIEW&idx=" + placeIdx);
  }
  
  
  public NetworkResult getPremiumList(int page, int area)
  {
    return httpClient.requestGet(getSiteUrl() + "?v=m1&mode=PREMIUM_LIST&p=" + page + "&area=" + area);
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
        }
      }
      catch (JSONException e)
      {
        e.printStackTrace();
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
  public NetworkResult completeCreateRecommendSchedule(String startDate, String idx, int dayCount)
  {
    return httpClient.requestGet(getSiteUrl() + "?v=m1&mode=PLAN_SMART_SAVE&startDate=" + startDate + "&dayCount=" + dayCount + "&planIdx=" + idx + "&userSeq="
        + PreferenceManager.getInstance(context).getUserSeq());
  }
  
  
  //검색관련 API
  public NetworkResult searchPopular()
  {
    return httpClient.requestPost(getSiteUrl() + "?v=m1&mode=SEARCH_POPULAR");
  }
  
  
  public NetworkResult searchAuto(String keyword)
  {
    return httpClient.requestPost(getSiteUrl() + "?v=m1&mode=SEARCH_AUTO&keyword=" + keyword);
  }
  
  
  public NetworkResult searchResult(String keyword)
  {
    if (TextUtils.isEmpty(PreferenceManager.getInstance(context).getUserSeq()))
      return httpClient.requestPost(getSiteUrl() + "?v=m1&mode=SEARCH_RESULT&keyword=" + keyword);
    else
      return httpClient.requestPost(getSiteUrl() + "?v=m1&mode=SEARCH_RESULT&keyword=" + keyword + "&userSeq=" + PreferenceManager.getInstance(context).getUserSeq());
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
    return httpClient.requestPost(getSiteUrl() + "?v=m1&mode=USER_EMAILKEY_RESEND&userEmai=" + userEmail);
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
    return httpClient.requestPost(getSiteUrl() + "?v=m1&mode=USERINFO&userSeq=" + userSeq);
  }
  
  
  public NetworkResult userTempPasswordGet(String userEmail)
  {
    return httpClient.requestPost(getSiteUrl() + "?v=m1&mode=USER_FINDPW&userEmail=" + userEmail);
  }
}