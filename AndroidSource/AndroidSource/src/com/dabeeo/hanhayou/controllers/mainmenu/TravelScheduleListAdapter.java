package com.dabeeo.hanhayou.controllers.mainmenu;

import java.text.SimpleDateFormat;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.mypage.MyScheduleDetailActivity;
import com.dabeeo.hanhayou.activities.travel.TravelScheduleDetailActivity;
import com.dabeeo.hanhayou.beans.ScheduleBean;
import com.dabeeo.hanhayou.fragments.mainmenu.TravelScheduleListFragment;
import com.dabeeo.hanhayou.utils.ImageDownloader;
import com.dabeeo.hanhayou.utils.SystemUtil;

public class TravelScheduleListAdapter extends BaseAdapter
{
  Context context;
  LayoutInflater inflater;
  int layoutResourceId;
  private float imageWidth;
  private int type = TravelScheduleListFragment.SCHEDULE_TYPE_POPULAR;
  private ArrayList<ScheduleBean> beans = new ArrayList<>();
  
  public RelativeLayout layoutRecommend;
  
  
  public TravelScheduleListAdapter(Context context)
  {
    this.context = context;
    @SuppressWarnings("deprecation")
    float width = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
    float margin = (int) SystemUtil.convertDpToPixel(4f, (Activity) context);
    // two images, three margins of 10dips
    imageWidth = ((width - (3 * margin)) / 2);
  }
  
  
  public void setType(int type)
  {
    this.type = type;
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
  
  public void setVisibleRecommend()
  {
    layoutRecommend.setVisibility(View.VISIBLE);
  }
  
  @SuppressLint({ "ViewHolder", "SimpleDateFormat" })
  @Override
  public View getView(int position, View convertView, ViewGroup parent)
  {
    final ScheduleBean bean = (ScheduleBean) beans.get(position);
    int resId = R.layout.list_item_travel_schedule;
    
    View view = LayoutInflater.from(parent.getContext()).inflate(resId, null);
    
    ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
    layoutRecommend = (RelativeLayout) view.findViewById(R.id.recommend_container);
    TextView startDate = (TextView) view.findViewById(R.id.start_date);
    TextView title = (TextView) view.findViewById(R.id.title);
    TextView month = (TextView) view.findViewById(R.id.month);
    TextView likeCount = (TextView) view.findViewById(R.id.like_count);
    TextView reviewCount = (TextView) view.findViewById(R.id.review_count);
    
    int height = (int) (imageWidth * 0.77);
    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) imageWidth, height);
    imageView.setLayoutParams(params);
    
    SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
    if (bean.startDate != null)
      startDate.setText(format.format(bean.insertDate));
    title.setText(bean.title);
    month.setText(Integer.toString(bean.dayCount) + "天");
    likeCount.setText(Integer.toString(bean.likeCount));
    reviewCount.setText(Integer.toString(bean.reviewCount));
    
    if (!SystemUtil.isConnectNetwork(context))
      imageView.setVisibility(View.GONE);
    else
      imageView.setVisibility(View.VISIBLE);
    
    if (SystemUtil.isConnectNetwork(context))
    {
      if(type == TravelScheduleListFragment.SCHEDULE_TYPE_POPULAR)
        layoutRecommend.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
    }
    else
      layoutRecommend.setVisibility(View.GONE);
    
    if (imageView.getVisibility() == View.VISIBLE)
      ImageDownloader.displayImage(context, bean.imageUrl, imageView, null);
    
    view.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        Intent i = new Intent(context, TravelScheduleDetailActivity.class);
        if (type == TravelScheduleListFragment.SCHEDULE_TYPE_MY)
          i = new Intent(context, MyScheduleDetailActivity.class);
        i.putExtra("idx", bean.idx);
        context.startActivity(i);
      }
    });
    return view;
  }
}
