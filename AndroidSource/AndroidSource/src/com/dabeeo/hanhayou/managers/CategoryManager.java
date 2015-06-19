package com.dabeeo.hanhayou.managers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.dabeeo.hanhayou.beans.CategoryBean;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.managers.network.NetworkResult;

public class CategoryManager
{
  private static CategoryManager instance = null;
  private Context context;
  public ArrayList<CategoryBean> categories = new ArrayList<CategoryBean>();
  private ApiClient client;
  
  
  public static CategoryManager getInstance(Context context)
  {
    if (instance == null)
    {
      synchronized (SubwayManager.class)
      {
        if (instance == null)
          instance = new CategoryManager(context);
      }
    }
    return instance;
  }
  
  
  private CategoryManager(Context context)
  {
    this.context = context;
    client = new ApiClient(context);
  }
  
  
  public void getCategories()
  {
    categories.clear();
    NetworkResult result = client.getCategories();
    try
    {
      JSONObject obj = new JSONObject(result.response);
      JSONArray arr = obj.getJSONArray("category");
      for (int i = 0; i < arr.length(); i++)
      {
        CategoryBean bean = new CategoryBean();
        bean.setJSONObject(arr.getJSONObject(i));
        categories.add(bean);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  
  public String getCategoryName(int idx)
  {
    String name = "";
    for (int i = 0; i < categories.size(); i++)
    {
      try
      {
        if (categories.get(i).idx.equals(Integer.toString(idx)))
          name = categories.get(i).name;
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    return name;
  }
}
