package com.dabeeo.hangouyou.activities.mypage.sub;

import android.os.Bundle;
import android.view.View;

import com.dabeeo.hangouyou.activities.mainmenu.PlaceDetailActivity;

public class MyPlaceDetailActivity extends PlaceDetailActivity
{
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
  }
  
  
  @Override
  protected void onResume()
  {
    super.btnReviewBest.setVisibility(View.GONE);
    super.btnReviewSoso.setVisibility(View.GONE);
    super.btnReviewWorst.setVisibility(View.GONE);
    super.containerWriteReview.setVisibility(View.GONE);
    super.onResume();
  }
}
