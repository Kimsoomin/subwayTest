package com.dabeeo.hangouyou.views.photolog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;

public class PhotoLogDayView extends LinearLayout
{
  public PhotoLogDayView(Context context, String location, String description)
  {
    super(context);
    int resId = R.layout.view_photo_log_day_view;
    View view = LayoutInflater.from(context).inflate(resId, null);
    view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    
    ImageView photo = (ImageView) view.findViewById(R.id.photo);
    TextView textLocation = (TextView) view.findViewById(R.id.location);
    TextView textDescription = (TextView) view.findViewById(R.id.description);
    
    textLocation.setText(location);
    textDescription.setText(description);
    
    addView(view);
  }
}
