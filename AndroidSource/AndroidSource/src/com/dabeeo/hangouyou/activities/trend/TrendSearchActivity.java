package com.dabeeo.hangouyou.activities.trend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.TrendSearchListBean;
import com.dabeeo.hangouyou.controllers.trend.TrendSearchListAdapter;

public class TrendSearchActivity extends Activity
{
  private EditText editSearch;
  private ImageView backImage;
  
  private ListView listView;
  private TrendSearchListAdapter adapter;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_trend_search);
    
    editSearch = (EditText) findViewById(R.id.edit_search);
    
    backImage = (ImageView) findViewById(R.id.image_back_button);
    backImage.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        finish();
      }
    });
    
    listView = (ListView) findViewById(R.id.listview);
    adapter = new TrendSearchListAdapter();
    listView.setAdapter(adapter);
    
    listView.setOnItemClickListener(new OnItemClickListener()
    {
      @Override
      public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3)
      {
        Intent i = new Intent(TrendSearchActivity.this, TrendCategoryListActivity.class);
        startActivity(i);
      }
    });
    loadDefaultCategory();
  }
  
  
  private void loadDefaultCategory()
  {
    TrendSearchListBean bean = new TrendSearchListBean();
    bean.title = "패션";
    adapter.add(bean);
    
    bean = new TrendSearchListBean();
    bean.title = "화장품/미용";
    adapter.add(bean);
    
    bean = new TrendSearchListBean();
    bean.title = "잡화/슈즈";
    adapter.add(bean);
    
    bean = new TrendSearchListBean();
    bean.title = "라이프 스타일";
    adapter.add(bean);
  }
  
}
