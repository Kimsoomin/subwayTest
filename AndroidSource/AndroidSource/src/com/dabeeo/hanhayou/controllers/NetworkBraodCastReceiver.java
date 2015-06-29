package com.dabeeo.hanhayou.controllers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dabeeo.hanhayou.utils.SystemUtil;

public class NetworkBraodCastReceiver extends BroadcastReceiver
{
  public final static String ACTION_NETWORK_STATUS_CHANGE = "action_network_status_change";
  public final static String ACTION_LOGIN = "action_login";
  
  
  /*
   * 네트워크 실패 / 연결 시 BroadCast ACTION_NETWORK_STATUS_CHANGE를 날리게 됨. 해당 Action의
   * is_network_connected 란 boolean 값을 사용 MyPageFragment 를 참조 (non-Javadoc)
   */
  @Override
  public void onReceive(Context context, Intent intent)
  {
    Intent i = new Intent(ACTION_NETWORK_STATUS_CHANGE);
    i.putExtra("is_network_connected", SystemUtil.isConnectNetwork(context));
    context.sendBroadcast(i);
  }
  
}
