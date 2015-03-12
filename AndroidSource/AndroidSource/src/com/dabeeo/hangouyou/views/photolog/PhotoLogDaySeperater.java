package com.dabeeo.hangouyou.views.photolog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;

public class PhotoLogDaySeperater extends LinearLayout
{
  public PhotoLogDaySeperater(Context context, int day)
  {
    super(context);
    int resId = R.layout.view_photo_log_day_seperater;
    View view = LayoutInflater.from(context).inflate(resId, null);
    view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    TextView textDay = (TextView) view.findViewById(R.id.day);
    textDay.setText(Integer.toString(day));
    addView(view);
  }
}
