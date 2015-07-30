package com.dabeeo.hanhayou.views;

import java.text.SimpleDateFormat;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.beans.PlaceDetailBean;
import com.dabeeo.hanhayou.utils.ImageDownloader;

public class PlaceDetailTitleView extends RelativeLayout
{
  private Context context;
  public RelativeLayout container;
  private ImageView imageView;
  public TextView title;
  private TextView name, time, likeCount, bookmarkCount;
  public View titleDivider;
  private ImageView isPrivateImage;
  
  
  public PlaceDetailTitleView(Context context)
  {
    super(context);
    this.context = context;
    init();
  }
  
  
  public PlaceDetailTitleView(Context context, AttributeSet attrs, int defStyle)
  {
    super(context, attrs, defStyle);
    this.context = context;
    init();
  }
  
  
  public PlaceDetailTitleView(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    this.context = context;
    init();
  }
  
  
  public void reloadLikeCount(int count)
  {
    if (count != -1)
      likeCount.setText(Integer.toString(count));
  }
  
  
  public void reloadBookmarkCount(int count)
  {
    if (count != -1)
      bookmarkCount.setText(Integer.toString(count));
  }
  
  
  @SuppressLint("SimpleDateFormat")
  public void setBean(PlaceDetailBean bean)
  {
    title.setText(bean.title);
    name.setText(bean.userName);
    SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd hh:mm");
    if (bean.updateDate != null)
      time.setText(format.format(bean.updateDate));
    likeCount.setText(Integer.toString(bean.likeCount));
    bookmarkCount.setText(Integer.toString(bean.bookmarkCount));
    ImageDownloader.displayProfileImage(context, bean.mfidx, imageView);
    
    if (!bean.isOpen)
      isPrivateImage.setVisibility(View.VISIBLE);
  }
  
  
  public void init()
  {
    int resId = R.layout.view_place_detail_title;
    View view = LayoutInflater.from(context).inflate(resId, null);
    view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    
    isPrivateImage = (ImageView) view.findViewById(R.id.is_public);
    container = (RelativeLayout) view.findViewById(R.id.container);
    title = (TextView) view.findViewById(R.id.text_title);
    imageView = (ImageView) view.findViewById(R.id.imageview);
    name = (TextView) view.findViewById(R.id.name);
    time = (TextView) view.findViewById(R.id.time);
    likeCount = (TextView) view.findViewById(R.id.like_count);
    bookmarkCount = (TextView) view.findViewById(R.id.bookmark_count);
    titleDivider = (View) view.findViewById(R.id.place_title_divider);
    
    addView(view);
  }
}
