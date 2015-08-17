package com.dabeeo.hanhayou.managers.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.dabeeo.hanhayou.utils.SystemUtil;

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
    Log.w("HttpClient.java | requestGet", "-------------------------\n" + url);
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
      Log.w("HttpClient.java | requestGet", "responseString : " + responseString + "\n------------------------");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return result;
  }
  
  public NetworkResult requestPostWithFile(String siteUrl, String filePath)
  {
    NetworkResult result = new NetworkResult(false, "", 0);
    
    try
    {
      DefaultHttpClient client = new DefaultHttpClient();
      HttpRequestBase requestBase = null;
      
      requestBase = new HttpPost(siteUrl);
      
      MultipartEntity multipartEntity = new MultipartEntity();
      multipartEntity.addPart("uploaded_file", new FileBody(new File(filePath)));
      Log.w("WARN", "HttpRequestHandler multipartEntity ::: " + multipartEntity.getContentLength());
      
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
    Log.w("HttpClient.java | requestPost", "-------------------------\n" + url);
    
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
      Log.w("HttpClient.java | requestPost", "responseString : " + responseString + "\n------------------------");
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
  
  
  public JSONObject requestTrendyPost(String urlString, HashMap<String, String> body)
  {
    JSONObject responseObj = null;
    try {
      URL url = new URL(urlString);
      
      trustAllHosts();
      
      
      
      HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
      httpsURLConnection.setHostnameVerifier(new HostnameVerifier() {
        @Override
        public boolean verify(String s, SSLSession sslSession) {
          return true;
        }
      });
      
      HttpURLConnection connection = httpsURLConnection;
      
      connection.setRequestMethod("POST");
      connection.setDoInput(true);
      connection.setDoOutput(true);
      
      List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
      
      for(Entry<String, String> bodyInfo : body.entrySet())
      {
        String key = bodyInfo.getKey();
        String value = bodyInfo.getValue();
        nameValuePairs.add(new BasicNameValuePair(key, value));
      }
      
      OutputStream outputStream = connection.getOutputStream();
      BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
      bufferedWriter.write(getURLQuery(nameValuePairs));
      bufferedWriter.flush();
      bufferedWriter.close();
      outputStream.close();
      
      connection.connect();
      
      StringBuilder responseStringBuilder = new StringBuilder();
      if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
      {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String stringLine;
        while((stringLine = bufferedReader.readLine()) != null)
        {
          responseStringBuilder.append(stringLine + '\n');
        }
        bufferedReader.close();
        
        try
        {
          responseObj = new JSONObject(responseStringBuilder.toString());
        }
        catch (JSONException e)
        {
          e.printStackTrace();
        }
      }else
        responseObj = null;
      
      connection.disconnect();
      
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    
    return responseObj;
  }
  private static void trustAllHosts() {
    // Create a trust manager that does not validate certificate chains
    TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
      public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return new java.security.cert.X509Certificate[]{};
      }
      
      @Override
      public void checkClientTrusted(
          java.security.cert.X509Certificate[] chain,
          String authType)
              throws java.security.cert.CertificateException {
        // TODO Auto-generated method stub
        
      }
      
      @Override
      public void checkServerTrusted(
          java.security.cert.X509Certificate[] chain,
          String authType)
              throws java.security.cert.CertificateException {
        // TODO Auto-generated method stub
        
      }
    }};
    
    // Install the all-trusting trust manager
    try {
      SSLContext sc = SSLContext.getInstance("TLS");
      sc.init(null, trustAllCerts, new java.security.SecureRandom());
      HttpsURLConnection
      .setDefaultSSLSocketFactory(sc.getSocketFactory());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  private String getURLQuery(List<BasicNameValuePair> params){
    StringBuilder stringBuilder = new StringBuilder();
    boolean first = true;
    
    for (BasicNameValuePair pair : params)
    {
      if (first)
        first = false;
      else
        stringBuilder.append("&");
      
      try {
        stringBuilder.append(URLEncoder.encode(pair.getName(), "UTF-8"));
        stringBuilder.append("=");
        stringBuilder.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    }
    
    return stringBuilder.toString();
  }  
}
