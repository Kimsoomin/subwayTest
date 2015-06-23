package com.dabeeo.hanhayou.managers;

import java.util.ArrayList;

import org.json.JSONArray;

import android.content.Context;
import android.text.TextUtils;

public class PreferenceManager extends BasePreferenceManager
{
  private static PreferenceManager instance;
  
  
  public static PreferenceManager getInstance(Context context)
  {
    if (instance == null)
    {
      synchronized (PreferenceManager.class)
      {
        if (instance == null)
          instance = new PreferenceManager(context);
      }
    }
    return instance;
  }
  
  
  private PreferenceManager(Context context)
  {
    super(context);
  }
  
  private static final String KEY_IS_FIRST = "key_is_first";
  private static final String KEY_IS_ALLOW_POPUP = "key_is_allow_pop_up";
  private static final String KEY_RECENT_SEARCH_WORD = "key_recent_search_word";
  private static final String KEY_USER_SEQ = "key_user_seq";
  //유저정보 추가
  private static final String KEY_USER_EMAIL = "key_user_email";
  private static final String KEY_USER_NAME = "key_user_Name";
  private static final String KEY_USER_GENDER = "key_user_gender";
  private static final String KEY_USER_PROFILE = "key_user_profile";
  private static final String KEY_AUTO_LOGIN = "key_auto_login";
  
  private static final String KEY_DONT_SHOW_POPUP_DATE = "key_dont_show_popup_date";
  
  private static final String KEY_SYNC_ONLY_WIFI = "key_sync_only_wifi";
  
  
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
  
  
  public void setSyncOnlyWifi(boolean value)
  {
    put(KEY_SYNC_ONLY_WIFI, value);
  }
  
  
  public boolean getSyncOnlyWifi()
  {
    return get(KEY_SYNC_ONLY_WIFI, true);
  }
  
  
  public void setDontShowPopupDate(String value)
  {
    put(KEY_DONT_SHOW_POPUP_DATE, value);
  }
  
  
  public String getDontShowPopupDate()
  {
    return get(KEY_DONT_SHOW_POPUP_DATE);
  }
  
  
  public void clearUserInfo()
  {
    put(KEY_USER_SEQ, "");
    put(KEY_USER_EMAIL, "");
    put(KEY_USER_NAME, "");
    put(KEY_USER_GENDER, "");
    put(KEY_USER_PROFILE, "");
    put(KEY_AUTO_LOGIN, false);
  }
  
  
  // 로그인이 되어 있는가
  public boolean isLoggedIn()
  {
    return !TextUtils.isEmpty(getUserSeq());
  }
  
  
  public void setUserSeq(String value)
  {
    put(KEY_USER_SEQ, value);
  }
  
  
  //회원과련 정보 추가
  public void setUserEmail(String value)
  {
    put(KEY_USER_EMAIL, value);
  }
  
  
  public String getUserEmail()
  {
    return get(KEY_USER_EMAIL);
  }
  
  
  public void setUserName(String value)
  {
    put(KEY_USER_NAME, value);
  }
  
  
  public String getUserName()
  {
    return get(KEY_USER_NAME);
  }
  
  
  public void setUserGender(String value)
  {
    put(KEY_USER_GENDER, value);
  }
  
  
  public void setUserProfile(String value)
  {
    put(KEY_USER_PROFILE, value);
  }
  
  
  public String getUserSeq()
  {
    return get(KEY_USER_SEQ);
  }
  
  
  public void setIsAutoLogin(boolean value)
  {
    put(KEY_AUTO_LOGIN, value);
  }
  
  
  public boolean getIsAutoLogin()
  {
    return get(KEY_AUTO_LOGIN, false);
  }
  
  
  public void setRecentSearchWord(String word)
  {
    ArrayList<String> result = getRecentSearchWord();
    if (result.contains(word))
      result.remove(word);
    
    result.add(0, word);
    
    // 최근 3개만 남기기
    for (int i = result.size() - 1; i >= 3; i--)
    {
      result.remove(i);
    }
    
    JSONArray array = new JSONArray();
    for (String string : result)
    {
      array.put(string);
    }
    
    put(KEY_RECENT_SEARCH_WORD, array.toString());
  }
  
  
  public ArrayList<String> getRecentSearchWord()
  {
    ArrayList<String> result = new ArrayList<>();
    
    String json = get(KEY_RECENT_SEARCH_WORD);
    if (TextUtils.isEmpty(json))
      json = "[]";
    
    try
    {
      JSONArray array = new JSONArray(json);
      
      for (int i = 0; i < array.length(); i++)
      {
        result.add(array.getString(i));
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
    return result;
  }
}
