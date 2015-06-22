package com.dabeeo.hanhayou.controllers.mypage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.sub.ImagePopUpJustOneActivity;
import com.dabeeo.hanhayou.beans.NoticeBean.NoticeContentBean;
import com.dabeeo.hanhayou.utils.ImageDownloader;

public class NoticeAdapter extends BaseExpandableListAdapter
{
  private List<String> titles;
  private HashMap<String, List<ArrayList<NoticeContentBean>>> childs;
  private Context context;
  
  
  public NoticeAdapter(Context context, List<String> titles, HashMap<String, List<ArrayList<NoticeContentBean>>> childs)
  {
    this.context = context;
    this.titles = titles;
    this.childs = childs;
  }
  
  
  @Override
  public Object getChild(int groupPosition, int childPosititon)
  {
    return this.childs.get(this.titles.get(groupPosition)).get(childPosititon);
  }
  
  
  @Override
  public long getChildId(int groupPosition, int childPosition)
  {
    return childPosition;
  }
  
  
  @SuppressLint("ResourceAsColor")
  @Override
  public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
  {
    @SuppressWarnings("unchecked")
    final ArrayList<NoticeContentBean> childBean = (ArrayList<NoticeContentBean>) getChild(groupPosition, childPosition);
    
    convertView = new LinearLayout(parent.getContext());
    ((LinearLayout) convertView).setOrientation(LinearLayout.VERTICAL);
    for (int i = 0; i < childBean.size(); i++)
    {
      if (childBean.get(i).isText)
      {
        TextView textView = new TextView(parent.getContext());
        textView.setPadding(32, 32, 32, 32);
        textView.setTextSize(16);
        textView.setTextColor(Color.parseColor("#444a4b"));
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(childBean.get(i).text);
        ((LinearLayout) convertView).addView(textView);
      }
      else
      {
        ImageView imageView = new ImageView(parent.getContext());
        imageView.setPadding(32, 32, 32, 32);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        final String imageUrl = childBean.get(i).text;
        imageView.setOnClickListener(new OnClickListener()
        {
          @Override
          public void onClick(View arg0)
          {
            Intent i = new Intent(context, ImagePopUpJustOneActivity.class);
            i.putExtra("image_url", imageUrl);
            context.startActivity(i);
          }
        });
        ImageDownloader.displayImage(parent.getContext(), childBean.get(i).text, imageView, null);
        ((LinearLayout) convertView).addView(imageView);
      }
    }
    
    return convertView;
  }
  
  
  @Override
  public int getChildrenCount(int groupPosition)
  {
    return this.childs.get(this.titles.get(groupPosition)).size();
  }
  
  
  @Override
  public Object getGroup(int groupPosition)
  {
    return this.titles.get(groupPosition);
  }
  
  
  @Override
  public int getGroupCount()
  {
    return this.titles.size();
  }
  
  
  @Override
  public long getGroupId(int groupPosition)
  {
    return groupPosition;
  }
  
  
  @SuppressLint("InflateParams")
  @Override
  public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
  {
    String headerTitle = (String) getGroup(groupPosition);
    
    convertView = LayoutInflater.from(context).inflate(R.layout.list_item_notice_header, null);
    TextView title = (TextView) convertView.findViewById(R.id.title);
    TextView date = (TextView) convertView.findViewById(R.id.date);
    ImageView btnOpener = (ImageView) convertView.findViewById(R.id.btn_opener);
    
    String[] splits = headerTitle.split("&&&");
    title.setText(splits[0]);
    date.setText(splits[1]);
    
    if (isExpanded)
      btnOpener.setImageResource(R.drawable.icon_arrow_g_open);
    else
      btnOpener.setImageResource(R.drawable.icon_arrow_g_close);
    return convertView;
  }
  
  
  @Override
  public boolean hasStableIds()
  {
    return false;
  }
  
  
  @Override
  public boolean isChildSelectable(int groupPosition, int childPosition)
  {
    return true;
  }
}
