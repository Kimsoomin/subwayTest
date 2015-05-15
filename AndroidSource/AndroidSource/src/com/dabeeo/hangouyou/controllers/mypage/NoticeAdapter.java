package com.dabeeo.hangouyou.controllers.mypage;

import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;

public class NoticeAdapter extends BaseExpandableListAdapter
{
  private List<String> titles;
  private HashMap<String, List<String>> childs;
  private Context context;
  
  
  public NoticeAdapter(Context context, List<String> titles, HashMap<String, List<String>> childs)
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
    final String childText = (String) getChild(groupPosition, childPosition);
    
    convertView = new TextView(parent.getContext());
    ((TextView) convertView).setPadding(32, 32, 32, 32);
    ((TextView) convertView).setTextSize(16);
    ((TextView) convertView).setTextColor(Color.parseColor("#444a4b"));
    ((TextView) convertView).setMovementMethod(LinkMovementMethod.getInstance());
    ((TextView) convertView).setText(childText);
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
    
    title.setText(headerTitle);
    date.setText("2015-01-01");
    
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
