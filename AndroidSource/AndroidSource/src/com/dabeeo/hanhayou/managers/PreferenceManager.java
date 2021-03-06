package com.dabeeo.hanhayou.managers;

import android.content.Context;
import android.text.TextUtils;

import com.dabeeo.hanhayou.controllers.OfflineContentDatabaseManager;

public class PreferenceManager extends BasePreferenceManager
{
  private static PreferenceManager instance;
  private Context context;
  private OfflineContentDatabaseManager offlineManger;
  
  
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
    this.context = context;
    this.offlineManger = new OfflineContentDatabaseManager(context);
  }
  
  private static final String KEY_IS_FIRST = "key_is_first";
  private static final String KEY_IS_ALLOW_POPUP = "key_is_allow_pop_up";
  private static final String KEY_USER_SEQ = "key_user_seq";
  //유저정보 추가
  private static final String KEY_USER_EMAIL = "key_user_email";
  private static final String KEY_USER_NAME = "key_user_Name";
  private static final String KEY_USER_GENDER = "key_user_gender";
  private static final String KEY_USER_PROFILE = "key_user_profile";
  private static final String KEY_AUTO_LOGIN = "key_auto_login";
  private static final String KEY_DEVICE_ID = "key_device_id";
  
  private static final String KEY_DONT_SHOW_POPUP_DATE = "key_dont_show_popup_date";
  
  private static final String KEY_SYNC_ONLY_WIFI = "key_sync_only_wifi";
  
  private static final String KEY_COUPON_CATEGORY = "key_coupon_category";
  
  private static final String KEY_AGREEMENT = "key_agreement";
  private static final String KEY_AGREEMENT_PRIVATE = "key_agreement_private";
  private static final String KEY_AGREEMENT_GPS_INFO = "key_agreement_gps_info";
  
  
  public void setCouponCategoryJSONString(String value)
  {
    put(KEY_COUPON_CATEGORY, value);
  }
  
  
  public String getCouponCategoryJSONString()
  {
    return get(KEY_COUPON_CATEGORY);
  }
  
  
  public void setAgreementHtmlString(String value)
  {
    put(KEY_AGREEMENT, value);
  }
  
  
  public String getAgreementHtmlString()
  {
    return get(KEY_AGREEMENT);
  }
  
  
  public void setAgreementPrivateHtmlString(String value)
  {
    put(KEY_AGREEMENT_PRIVATE, value);
  }
  
  
  public String getAgreementPrivateHtmlString()
  {
    return get(KEY_AGREEMENT_PRIVATE);
  }
  
  
  public void setAgreementGPSInfoHtmlString(String value)
  {
    put(KEY_AGREEMENT_GPS_INFO, value);
  }
  
  
  public String getAgreementGPSInfoHtmlString()
  {
    return get(KEY_AGREEMENT_GPS_INFO);
  }
  
  
  //
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
  
  
  public String getUserProfile()
  {
    String profile = get(KEY_USER_PROFILE);
    if (TextUtils.isEmpty(profile))
      return "";
    else
      return profile;
  }
  
  
  public void setIsAutoLogin(boolean value)
  {
    put(KEY_AUTO_LOGIN, value);
  }
  
  
  public boolean getIsAutoLogin()
  {
    return get(KEY_AUTO_LOGIN, false);
  }
  
  
  public void setDeviceId(String deviceId)
  {
    put(KEY_DEVICE_ID, deviceId);
  }
  
  
  public String getDeviceId()
  {
    return get(KEY_DEVICE_ID);
  }
}
