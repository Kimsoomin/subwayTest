package com.dabeeo.hanhayou.managers;

import java.util.ArrayList;

import android.content.Context;

import com.dabeeo.hanhayou.beans.CategoryBean;

public class CategoryManager
{
  private static CategoryManager instance = null;
  private Context context;
  public ArrayList<CategoryBean> categories = new ArrayList<CategoryBean>();
  
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
  }
  
  public String getCategoryName(int idx)
  {
    String name = "";
    switch (idx)
    {
      case 1:
        name = "其他景点";
        break;
      
      case 2:
        name = "购物";
        break;
        
      case 3:
        name = "文化";
        break;
        
      case 4:
        name = "历史";
        break;
        
      case 5:
        name = "休闲";
        break;
        
      case 6:
        name = "体验";
        break;
      
      case 7:
        name = "美食";
        break;
    }
    return name;
  }
}
