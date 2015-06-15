package com.dabeeo.hangouyou.controllers.mainmenu;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.travel.TravelScheduleDetailActivity;
import com.dabeeo.hangouyou.beans.ScheduleBean;
import com.dabeeo.hangouyou.utils.ImageDownloader;
import com.dabeeo.hangouyou.utils.SystemUtil;

public class TravelScheduleListAdapter extends BaseAdapter
{
  Context context;
  LayoutInflater inflater;
  int layoutResourceId;
  float imageWidth;
  
  private ArrayList<ScheduleBean> beans = new ArrayList<>();
  
  
  public TravelScheduleListAdapter(Context context)
  {
    this.context = context;
    @SuppressWarnings("deprecation")
    float width = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
    float margin = (int) SystemUtil.convertDpToPixel(10f, (Activity) context);
    // two images, three margins of 10dips
    imageWidth = ((width - (3 * margin)) / 2);
  }
  
  
  public void add(ScheduleBean bean)
  {
    this.beans.add(bean);
    notifyDataSetChanged();
  }
  
  
  public void addAll(ArrayList<ScheduleBean> beans)
  {
    this.beans.addAll(beans);
    notifyDataSetChanged();
  }
  
  
  public void clear()
  {
    this.beans.clear();
    notifyDataSetChanged();
  }
  
  
  @Override
  public int getCount()
  {
    return beans.size();
  }
  
  
  @Override
  public Object getItem(int position)
  {
    return beans.get(position);
  }
  
  
  @Override
  public long getItemId(int position)
  {
    return position;
  }
  
  
  @SuppressLint("ViewHolder")
  @Override
  public View getView(int position, View convertView, ViewGroup parent)
  {
    final ScheduleBean bean = (ScheduleBean) beans.get(position);
    int resId = R.layout.list_item_travel_schedule;
    
    View view = LayoutInflater.from(parent.getContext()).inflate(resId, null);
    
    ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
    ImageView iconRemommend = (ImageView) view.findViewById(R.id.icon_recommend);
    TextView startDate = (TextView) view.findViewById(R.id.start_date);
    TextView title = (TextView) view.findViewById(R.id.title);
    TextView month = (TextView) view.findViewById(R.id.month);
    TextView likeCount = (TextView) view.findViewById(R.id.like_count);
    TextView reviewCount = (TextView) view.findViewById(R.id.review_count);
    
    iconRemommend.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
    startDate.setText(bean.startDateString);
    title.setText(bean.title);
    month.setText(Integer.toString(bean.dayCount) + parent.getContext().getString(R.string.term_month));
    likeCount.setText(Integer.toString(bean.likeCount));
    reviewCount.setText(Integer.toString(bean.reviewCount));
    
    ImageDownloader.displayImage(context, bean.imageUrl, imageView, null);
    
    view.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        Intent i = new Intent(context, TravelScheduleDetailActivity.class);
        i.putExtra("idx", bean.idx);
        context.startActivity(i);
      }
    });
    return view;
  }
  
}
