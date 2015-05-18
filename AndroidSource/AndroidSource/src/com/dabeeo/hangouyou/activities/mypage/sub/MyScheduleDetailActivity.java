package com.dabeeo.hangouyou.activities.mypage.sub;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.travel.TravelScheduleDetailActivity;

public class MyScheduleDetailActivity extends TravelScheduleDetailActivity
{
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    adapter.setIsMySchedule(true);
  }
  
  
  @Override
  protected void onResume()
  {
    super.containerWriteReview.setVisibility(View.GONE);
    super.containerLike.setVisibility(View.GONE);
    super.containerIsPublic.setVisibility(View.VISIBLE);
    super.btnIsPublic.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        CharSequence[] menus = new CharSequence[2];
        menus[0] = getString(R.string.term_public);
        menus[1] = getString(R.string.term_private);
        final CharSequence[] menuTitles = menus;
        
        Builder builder = new AlertDialog.Builder(MyScheduleDetailActivity.this);
        builder.setItems(menus, new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface dialog, int whichButton)
          {
            if (menuTitles[whichButton].equals(getString(R.string.term_public)))
            {
            }
            else if (menuTitles[whichButton].equals(getString(R.string.term_private)))
            {
            }
          }
        });
        builder.create().show();
      }
    });
    super.onResume();
  }
  
}
