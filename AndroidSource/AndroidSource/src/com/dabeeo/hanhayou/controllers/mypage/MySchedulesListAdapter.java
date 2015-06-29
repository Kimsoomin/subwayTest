package com.dabeeo.hanhayou.controllers.mypage;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.beans.ScheduleBean;
import com.dabeeo.hanhayou.managers.FileManager;
import com.dabeeo.hanhayou.utils.ImageDownloader;
import com.dabeeo.hanhayou.utils.SystemUtil;

public class MySchedulesListAdapter extends BaseAdapter
{
  public ArrayList<ScheduleBean> beans = new ArrayList<>();
  private boolean isEditMode = false;
  private Context context;
  
  
  public MySchedulesListAdapter(Context context)
  {
    this.context = context;
  }
  
  
  public void setAllCheck(boolean isCheck)
  {
    for (int i = 0; i < beans.size(); i++)
    {
      beans.get(i).isChecked = isCheck;
    }
    notifyDataSetChanged();
  }
  
  
  public void setEditMode(boolean isEditMode)
  {
    this.isEditMode = isEditMode;
    notifyDataSetChanged();
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
  
  
  public void removeCheckedItem()
  {
    for (int i = beans.size() - 1; i >= 0; i--)
    {
      ScheduleBean bean = beans.get(i);
      if (bean.isChecked)
        beans.remove(i);
    }
    
    JSONArray array = new JSONArray();
    for (int i = 0; i < beans.size(); i++)
    {
      array.put(beans.get(i).getJSONObject());
    }
    JSONObject object = new JSONObject();
    try
    {
      object.put("plan", array);
      FileManager.getInstance(context).writeFile(FileManager.FILE_MY_PLAN, object.toString());
    }
    catch (JSONException e)
    {
      e.printStackTrace();
    }
    notifyDataSetChanged();
  }
  
  
  public ArrayList<ScheduleBean> getCheckedArrayList()
  {
    ArrayList<ScheduleBean> checkedLists = new ArrayList<>();
    for (int i = 0; i < beans.size(); i++)
    {
      if (beans.get(i).isChecked)
        checkedLists.add(beans.get(i));
    }
    return checkedLists;
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
  public View getView(final int position, View convertView, ViewGroup parent)
  {
    ScheduleBean bean = (ScheduleBean) beans.get(position);
    int resId = R.layout.list_item_my_schedule;
    View view = LayoutInflater.from(parent.getContext()).inflate(resId, null);
    
    CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
    if (isEditMode)
    {
      checkBox.setVisibility(View.VISIBLE);
      checkBox.bringToFront();
    }
    else
      checkBox.setVisibility(View.GONE);
    
    if (bean.isChecked)
      checkBox.setChecked(true);
    else
      checkBox.setChecked(false);
    checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener()
    {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
      {
        ((ScheduleBean) beans.get(position)).isChecked = isChecked;
      }
    });
    
    ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
    TextView title = (TextView) view.findViewById(R.id.title);
    TextView month = (TextView) view.findViewById(R.id.month);
    TextView likeCount = (TextView) view.findViewById(R.id.like_count);
    TextView reviewCount = (TextView) view.findViewById(R.id.review_count);
    ImageView imagePrivate = (ImageView) view.findViewById(R.id.image_private);
    
    if (!bean.isOpen)
      imagePrivate.setVisibility(View.VISIBLE);
    else
      imagePrivate.setVisibility(View.GONE);
    title.setText(bean.title);
    month.setText(Integer.toString(bean.dayCount) + parent.getContext().getString(R.string.term_month));
    likeCount.setText(Integer.toString(bean.likeCount));
    reviewCount.setText(Integer.toString(bean.reviewCount));
    
    if (!SystemUtil.isConnectNetwork(context))
      imageView.setVisibility(View.GONE);
    else
      ImageDownloader.displayImage(context, bean.imageUrl, imageView, null);
    return view;
  }
}
