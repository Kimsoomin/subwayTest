package com.dabeeo.hangouyou.managers.network;

import org.json.JSONObject;

import android.content.Context;

public class ApiClient
{
  private String siteUrl = "http://gs.blinking.kr:8900/_libs/api.common.php";
  private HttpClient httpClient;
  
  
  public ApiClient(Context context)
  {
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
  
  
  public NetworkResult getCategories()
  {
    return httpClient.requestGet(getSiteUrl() + "?v=m1&mode=CATEGORY_LIST");
  }
  
  
  public NetworkResult getPlaceList(int page)
  {
    return httpClient.requestGet(getSiteUrl() + "?v=m1&mode=PLACE_LIST&p=" + page);
  }
  
  
  public NetworkResult getPlaceDetail(int placeIdx)
  {
    return httpClient.requestGet(getSiteUrl() + "?v=m1&mode=PLACE_VIEW&idx=" + placeIdx);
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
    return httpClient.requestGet(getSiteUrl() + "?v=m1&mode=TRAVELOG_LIST&p=" + page + "&contentType=" + contentType);
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
  
  
  public NetworkResult getOrderDetail(String orderId)
  {
//    return httpClient.requestGet(getSiteUrl() + "?v=m1&mode=TRAVELOG_LIST&p=" + ticketId);
    return new NetworkResult(true, null, 1);
  }
  
  
  public NetworkResult getAllCoupon(int page, String contentType)
  {
    return httpClient.requestGet(getSiteUrl() + "?v=m1&mode=TRAVELOG_LIST&p=" + page + "&contentType=" + contentType);
  }
  
  
  public NetworkResult getMyCoupon(int page, String contentType)
  {
    return httpClient.requestGet(getSiteUrl() + "?v=m1&mode=TRAVELOG_LIST&p=" + page + "&contentType=" + contentType);
  }
  
}
