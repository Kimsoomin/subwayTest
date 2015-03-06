package com.dabeeo.hangouyou.managers;

import android.content.Context;

public class PreferenceManager extends BasePreferenceManager
{
  private static PreferenceManager _instance;
  
  
  public static PreferenceManager getInstance(Context context)
  {
    if (_instance == null)
      _instance = new PreferenceManager(context);
    return _instance;
  }
  
  
  private PreferenceManager(Context context)
  {
    super(context);
  }
  
  private static final String KEY_IS_FIRST = "key_is_first";
  private static final String KEY_IS_ALLOW_POPUP = "key_is_allow_pop_up";
  
  
  public void setIsFirst(boolean value)
  {
    put(KEY_IS_FIRST, value);
  }
  
  
  public boolean getIsFirst()
  {
    return get(KEY_IS_FIRST, true);
  }
  
  
  public void setAllowPopup(boolean value)
  {
    put(KEY_IS_ALLOW_POPUP, value);
  }
  
  
  public boolean getAllowPopup()
  {
    return get(KEY_IS_ALLOW_POPUP, true);
  }
}
