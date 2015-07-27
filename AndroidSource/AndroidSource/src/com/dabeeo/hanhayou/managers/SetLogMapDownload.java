package com.dabeeo.hanhayou.managers;

import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.managers.network.NetworkResult;

import android.content.Context;
import android.os.AsyncTask;

public class SetLogMapDownload extends AsyncTask<Context, Void, NetworkResult>
{

  @Override
  protected NetworkResult doInBackground(Context... params)
  {
    ApiClient apiClient = new ApiClient(params[0]);
    return apiClient.setLogApp(PreferenceManager.getInstance(params[0]).getDeviceId(), 2);
  }
  
}
