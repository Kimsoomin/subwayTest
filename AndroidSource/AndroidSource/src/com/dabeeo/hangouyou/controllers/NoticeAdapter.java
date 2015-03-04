package com.dabeeo.hangouyou.controllers;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class NoticeAdapter extends BaseExpandableListAdapter
{
  private Context context;
  private List<String> titles;
  private HashMap<String, List<String>> childs;
  
  
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
  
  
  @Override
  public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
  {
    final String childText = (String) getChild(groupPosition, childPosition);
    
    convertView = new TextView(context);
    ((TextView) convertView).setPadding(16, 16, 16, 16);
    ((TextView) convertView).setTextSize(16);
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
  
  
  @Override
  public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
  {
    String headerTitle = (String) getGroup(groupPosition);
    
    convertView = new TextView(context);
    ((TextView) convertView).setPadding(80, 16, 16, 16);
    ((TextView) convertView).setTextSize(18);
    ((TextView) convertView).setSingleLine();
    ((TextView) convertView).setBackgroundColor(Color.parseColor("#D4D4D4"));
    ((TextView) convertView).setTypeface(null, Typeface.BOLD);
    ((TextView) convertView).setText(headerTitle);
    
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
