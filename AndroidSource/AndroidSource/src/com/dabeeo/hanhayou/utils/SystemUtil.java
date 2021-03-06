package com.dabeeo.hanhayou.utils;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.util.DisplayMetrics;

public class SystemUtil
{
  public static boolean isConnectedWiFi(Context context)
  {
    try
    {
      ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
      if (manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI) == null)
        return false;
      return manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return false;
  }
  
  
  public static boolean isConnected3G(Context context)
  {
    if (context.getPackageManager().hasSystemFeature("com.google.android.tv"))
      return true;
    
    try
    {
      ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
      if (manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) == null)
        return false;
      return manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return false;
  }
  
  
  public static boolean isConnectNetwork(Context context)
  {
    return isConnectedWiFi(context) || isConnected3G(context);
  }
  
  
  public static float convertDpToPixel(float dp, Context context)
  {
    Resources resources = context.getResources();
    DisplayMetrics metrics = resources.getDisplayMetrics();
    float px = dp * (metrics.densityDpi / 160f);
    return px;
  }
}
