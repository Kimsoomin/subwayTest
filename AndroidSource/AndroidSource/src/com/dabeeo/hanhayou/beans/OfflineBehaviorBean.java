package com.dabeeo.hanhayou.beans;

import android.content.Context;

import com.dabeeo.hanhayou.managers.PreferenceManager;

public class OfflineBehaviorBean
{
  public static final String BEHAVIOR_DELETE_MY_PLAN = "behavior_delete_my_plan";
  public static final String BEHAVIOR_DELETE_MY_PLACE = "behavior_delete_my_place";
  public static final String BEHAVIOR_USE_COUPOON = "behavior_use_coupon";
  
  public int id;
  public String userSeq;
  public String behavior;
  public String idx;
  
  
  public void setDeleteMyPlan(Context context, String idx)
  {
    this.behavior = BEHAVIOR_DELETE_MY_PLAN;
    this.idx = idx;
    this.userSeq = PreferenceManager.getInstance(context).getUserSeq();
  }
  
  
  public void setDeleteMyPlace(Context context, String idx)
  {
    this.behavior = BEHAVIOR_DELETE_MY_PLACE;
    this.idx = idx;
    this.userSeq = PreferenceManager.getInstance(context).getUserSeq();
  }
  
  
  public void setUseCoupon(Context context, String couponIdx, String branchIdx)
  {
    this.behavior = BEHAVIOR_USE_COUPOON;
    this.idx = couponIdx + "/" + branchIdx;
    this.userSeq = PreferenceManager.getInstance(context).getUserSeq();
  }
}