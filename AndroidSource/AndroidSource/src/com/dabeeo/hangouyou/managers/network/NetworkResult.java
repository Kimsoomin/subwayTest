package com.dabeeo.hangouyou.managers.network;

import android.text.TextUtils;

public class NetworkResult
{
  public boolean isSuccess = false;
  public String response;
  public int responseCode = 0;
  
  
  public NetworkResult(boolean isSuccess, String response, int responseCode)
  {
    this.isSuccess = isSuccess;
    this.response = response;
    this.responseCode = responseCode;
  }
  
  
  public boolean isSuccess()
  {
    return isSuccess && !TextUtils.isEmpty(response);
  }
}
