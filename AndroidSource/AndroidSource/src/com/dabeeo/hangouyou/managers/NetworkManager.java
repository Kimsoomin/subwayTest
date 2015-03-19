package com.dabeeo.hangouyou.managers;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class NetworkManager
{
  private static NetworkManager instance = null;
  private RequestQueue queue = null;
  
  
  public static NetworkManager instance(Context context)
  {
    if (instance == null)
    {
      synchronized (NetworkManager.class)
      {
        if (instance == null)
          instance = new NetworkManager(context);
      }
    }
    return instance;
  }
  
  
  private NetworkManager(Context context)
  {
    queue = Volley.newRequestQueue(context);
  }
  
  
  public void call(StringRequest request)
  {
    Log.i("NetworkManager.java | call", "========================\n" + request.getUrl() + "\n========================");
    queue.add(request);
  }
  
  
  public void call(JsonObjectRequest request)
  {
    Log.i("NetworkManager.java | call", "========================\n" + request.getUrl() + "\n========================");
    queue.add(request);
  }
  
  
  public void call(JsonArrayRequest request)
  {
    Log.i("NetworkManager.java | call", "========================\n" + request.getUrl() + "\n========================");
    queue.add(request);
  }
}
