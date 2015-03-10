package com.dabeeo.hangouyou.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.mypage.sub.PlaceDetailActivity;

public class ScheduleView extends RelativeLayout
{
  private Context context;
  
  
  public ScheduleView(Context context)
  {
    super(context);
    this.context = context;
    init();
  }
  
  
  @SuppressWarnings("unused")
  @SuppressLint("InflateParams")
  public void init()
  {
    View view = LayoutInflater.from(context).inflate(R.layout.view_schedule, null);
    
    ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
    TextView title = (TextView) view.findViewById(R.id.text_tile);
    TextView position = (TextView) view.findViewById(R.id.text_position);
    
    //가데이터
    title.setText("삼청동길");
    position.setText("1");
    
    view.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        Intent i = new Intent(context, PlaceDetailActivity.class);
        context.startActivity(i);
      }
    });
    
    addView(view);
  }
}
