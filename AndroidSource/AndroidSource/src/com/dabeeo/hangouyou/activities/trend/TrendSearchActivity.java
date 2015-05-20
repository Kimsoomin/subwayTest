package com.dabeeo.hangouyou.activities.trend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.ProductBean;
import com.dabeeo.hangouyou.controllers.trend.TrendSearchListAdapter;

public class TrendSearchActivity extends Activity
{
  private EditText editSearch;
  private ImageView backImage, cartImage;
  
  private ListView searchListView;
  private TrendSearchListAdapter adapter;
  
  private LinearLayout categoryContainer;
  private TextView searchTitle;
  private LinearLayout searchContainer;
  private RelativeLayout searchNotExistContainer;
  
  //Bottom
  private LinearLayout conatinerAll, conatinerCosmetic, conatinerFood, conatinerBaby, conatinerLiving, conatinerEtc;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_trend_search);
    
    conatinerAll = (LinearLayout) findViewById(R.id.container_all);
    conatinerCosmetic = (LinearLayout) findViewById(R.id.container_cosmetic);
    conatinerFood = (LinearLayout) findViewById(R.id.container_food);
    conatinerBaby = (LinearLayout) findViewById(R.id.container_baby);
    conatinerLiving = (LinearLayout) findViewById(R.id.container_living);
    conatinerEtc = (LinearLayout) findViewById(R.id.container_etc);
    
    conatinerAll.setOnClickListener(clickListener);
    conatinerCosmetic.setOnClickListener(clickListener);
    conatinerFood.setOnClickListener(clickListener);
    conatinerBaby.setOnClickListener(clickListener);
    conatinerLiving.setOnClickListener(clickListener);
    conatinerEtc.setOnClickListener(clickListener);
    
    editSearch = (EditText) findViewById(R.id.edit_search);
    searchTitle = (TextView) findViewById(R.id.search_title);
    searchTitle.setText(getString(R.string.term_search_result));
    
    categoryContainer = (LinearLayout) findViewById(R.id.container_category);
    searchContainer = (LinearLayout) findViewById(R.id.search_container);
    searchNotExistContainer = (RelativeLayout) findViewById(R.id.search_not_exist_container);
    
    cartImage = (ImageView) findViewById(R.id.btn_cart);
    backImage = (ImageView) findViewById(R.id.image_back_button);
    backImage.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        finish();
      }
    });
    
    searchListView = (ListView) findViewById(R.id.listview);
    adapter = new TrendSearchListAdapter(this);
    searchListView.setAdapter(adapter);
    
    searchListView.setOnItemClickListener(new OnItemClickListener()
    {
      @Override
      public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3)
      {
        Intent i = new Intent(TrendSearchActivity.this, TrendProductDetailActivity.class);
        startActivity(i);
      }
    });
    
    TextWatcher watcher = new TextWatcher()
    {
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count)
      {
        Log.w("WARN", "length: " + editSearch.getText().toString().length());
        if (editSearch.getText().toString().length() > 1)
        {
          searchContainer.setVisibility(View.VISIBLE);
          categoryContainer.setVisibility(View.GONE);
          //If search exist
          searchListView.setVisibility(View.VISIBLE);
          searchNotExistContainer.setVisibility(View.GONE);
          //else
//          searchListView.setVisibility(View.VISIBLE);
//          searchNotExistContainer.setVisibility(View.GONE);
        }
        else
        {
          searchContainer.setVisibility(View.GONE);
          categoryContainer.setVisibility(View.VISIBLE);
        }
        
        adapter.clear();
        
        ProductBean bean = new ProductBean();
        bean.title = "TEST";
        adapter.add(bean);
        
        bean = new ProductBean();
        bean.title = "TEST 1";
        adapter.add(bean);
        
        bean = new ProductBean();
        bean.title = "TEST 2";
        adapter.add(bean);
      }
      
      
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after)
      {
        
      }
      
      
      @Override
      public void afterTextChanged(Editable s)
      {
        
      }
    };
    editSearch.addTextChangedListener(watcher);
  }
  
  private OnClickListener clickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      Intent i = new Intent(TrendSearchActivity.this, TrendProductWithCategoryActivity.class);
      if (v.getId() == conatinerAll.getId())
      {
        i.putExtra("category_title", getString(R.string.term_shopping_all));
      }
      else if (v.getId() == conatinerCosmetic.getId())
      {
        i.putExtra("category_title", getString(R.string.term_shopping_cosmetic));
      }
      else if (v.getId() == conatinerFood.getId())
      {
        i.putExtra("category_title", getString(R.string.term_shopping_food));
      }
      else if (v.getId() == conatinerBaby.getId())
      {
        i.putExtra("category_title", getString(R.string.term_shopping_baby));
      }
      else if (v.getId() == conatinerLiving.getId())
      {
        i.putExtra("category_title", getString(R.string.term_shopping_living));
      }
      else if (v.getId() == conatinerEtc.getId())
      {
        i.putExtra("category_title", getString(R.string.term_shopping_etc));
      }
      startActivity(i);
    }
  };
}
