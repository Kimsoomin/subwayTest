package com.dabeeo.hangouyou.managers.network;

import java.io.File;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.util.Log;

import com.dabeeo.hangouyou.utils.SystemUtil;

@SuppressWarnings("deprecation")
public class HttpClient
{
  private String siteUrl;
  private Context context;
  
  
  public HttpClient(Context context, String siteUrl)
  {
    this.context = context;
    this.siteUrl = siteUrl;
  }
  
  
  public NetworkResult requestGet(String url)
  {
    NetworkResult result = new NetworkResult(false, "", 0);
    Log.w("WARN", "------------------------");
    Log.w("WARN", "request Url : " + url);
    if (!SystemUtil.isConnectNetwork(context))
      return result;
    
    try
    {
      DefaultHttpClient client = new DefaultHttpClient();
      HttpRequestBase requestBase = null;
      
      requestBase = new HttpGet(url);
      
      HttpResponse response = client.execute(requestBase);
      String responseString = EntityUtils.toString(response.getEntity());
      
      if (response.getStatusLine().getStatusCode() < 400)
        result = new NetworkResult(true, responseString, response.getStatusLine().getStatusCode());
      else
        result = new NetworkResult(false, responseString, response.getStatusLine().getStatusCode());
      
      Log.w("WARN", "responseString : " + responseString);
      Log.w("WARN", "------------------------");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return result;
  }
  
  
  private NetworkResult requestPostWithFile(String siteUrl, String storeId, String filePath, boolean isShown)
  {
    NetworkResult result = new NetworkResult(false, "", 0);
    
    try
    {
      DefaultHttpClient client = new DefaultHttpClient();
      HttpRequestBase requestBase = null;
      
      requestBase = new HttpPost(siteUrl);
      
      MultipartEntity multipartEntity = new MultipartEntity();
      if (filePath.contains("zip"))
        multipartEntity.addPart("store_picture[zip]", new FileBody(new File(filePath), "application/zip"));
      else
        multipartEntity.addPart("store_picture[picture]", new FileBody(new File(filePath), "image/jpeg"));
      multipartEntity.addPart("store_picture[shown]", new StringBody(Boolean.toString(isShown)));
      multipartEntity.addPart("store_id", new StringBody(storeId));
      Log.w("WARN", "HttpRequestHandler storeId::: " + storeId);
      
      ((HttpPost) requestBase).setEntity(multipartEntity);
      
      HttpResponse response = client.execute(requestBase);
      
      String responseString = EntityUtils.toString(response.getEntity());
      
      Log.w("WARN", "HttpRequestHandler responseCode::: " + response.getStatusLine().getStatusCode());
      Log.w("WARN", "HttpRequestHandler responseString::: " + responseString);
      
      if (response.getStatusLine().getStatusCode() < 400)
        result = new NetworkResult(true, responseString, response.getStatusLine().getStatusCode());
      else
        result = new NetworkResult(false, responseString, response.getStatusLine().getStatusCode());
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return result;
  }
  
  
  public NetworkResult requestPost(String url)
  {
    NetworkResult result = new NetworkResult(false, "", 0);
    
    if (!SystemUtil.isConnectNetwork(context))
      return result;
    
    try
    {
      DefaultHttpClient client = new DefaultHttpClient();
      HttpPost post = new HttpPost(url);
      post.setHeader("Content-Type", "application/json");
      post.setHeader("Accept-Charset", HTTP.UTF_8);
      
      HttpResponse response = client.execute(post);
      String responseString = EntityUtils.toString(response.getEntity());
      
      if (response.getStatusLine().getStatusCode() < 400)
        result = new NetworkResult(true, responseString, response.getStatusLine().getStatusCode());
      else
        result = new NetworkResult(false, responseString, response.getStatusLine().getStatusCode());
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return result;
  }
  
  
  public NetworkResult requestPut(String url, String content)
  {
    NetworkResult result = new NetworkResult(false, "", 0);
    try
    {
      DefaultHttpClient client = new DefaultHttpClient();
      HttpRequestBase requestBase = null;
      
      requestBase = new HttpPut(url);
      requestBase.setHeader("Content-Type", "application/json");
      ((HttpPut) requestBase).setEntity(new StringEntity(content, "utf-8"));
      
      HttpResponse response = client.execute(requestBase);
      String responseString = EntityUtils.toString(response.getEntity());
      
      if (response.getStatusLine().getStatusCode() < 400)
        result = new NetworkResult(true, responseString, response.getStatusLine().getStatusCode());
      else
        result = new NetworkResult(false, responseString, response.getStatusLine().getStatusCode());
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return result;
  }
  
  
  public NetworkResult requestDelete(String url)
  {
    NetworkResult result = new NetworkResult(false, "", 0);
    
    if (!SystemUtil.isConnectNetwork(context))
      return result;
    
    try
    {
      DefaultHttpClient client = new DefaultHttpClient();
      HttpRequestBase requestBase = null;
      
      requestBase = new HttpDelete(url);
      
      HttpResponse response = client.execute(requestBase);
      String responseString = EntityUtils.toString(response.getEntity());
      
      if (response.getStatusLine().getStatusCode() < 400)
        result = new NetworkResult(true, responseString, response.getStatusLine().getStatusCode());
      else
        result = new NetworkResult(false, responseString, response.getStatusLine().getStatusCode());
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return result;
  }
}
