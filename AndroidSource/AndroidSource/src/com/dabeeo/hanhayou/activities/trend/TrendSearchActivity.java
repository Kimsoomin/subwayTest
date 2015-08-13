package com.dabeeo.hanhayou.activities.trend;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.beans.ProductBean;
import com.dabeeo.hanhayou.controllers.trend.TrendSearchListAdapter;
import com.dabeeo.hanhayou.managers.network.ApiClient;

public class TrendSearchActivity extends Activity
{
  private EditText editSearch;
  private ImageView backImage, cartImage, imageX;
  private TextWatcher watcher;
  
  private ListView searchListView;
  private TrendSearchListAdapter adapter;
  
  private LinearLayout categoryContainer;
  private TextView searchTitle;
  private LinearLayout searchContainer;
  private RelativeLayout searchNotExistContainer;
  
  //Bottom
  private LinearLayout containerAll, containerCosmetic, containerShoesbag, containerAccessory, containerBaby, containerFood;
  
  //API
  private ApiClient apiClient;
  
  private ArrayList<ProductBean> productList;
  private boolean resultStataus = true;
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_trend_search);
    
    productList = new ArrayList<ProductBean>();
    apiClient = new ApiClient(TrendSearchActivity.this);
    
    containerAll = (LinearLayout) findViewById(R.id.container_all);
    containerCosmetic = (LinearLayout) findViewById(R.id.container_cosmetic);
    containerShoesbag = (LinearLayout) findViewById(R.id.container_shoesbag);
    containerAccessory = (LinearLayout) findViewById(R.id.container_accessory);
    containerBaby = (LinearLayout) findViewById(R.id.container_baby);
    containerFood = (LinearLayout) findViewById(R.id.container_food);
    
    containerAll.setOnClickListener(clickListener);
    containerCosmetic.setOnClickListener(clickListener);
    containerShoesbag.setOnClickListener(clickListener);
    containerAccessory.setOnClickListener(clickListener);
    containerBaby.setOnClickListener(clickListener);
    containerFood.setOnClickListener(clickListener);
    
    editSearch = (EditText) findViewById(R.id.edit_search);
    searchTitle = (TextView) findViewById(R.id.search_title);
    
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
        i.putExtra("product_idx", productList.get(position).id);
        i.putExtra("product_isWished", productList.get(position).isWished);
        i.putExtra("proudct_categoryId", productList.get(position).categoryId);
        i.putExtra("product_rate", productList.get(position).rate);
        startActivity(i);
      }
    });
    
    watcher = new TextWatcher()
    {
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count)
      {
        changeUI(editSearch.getText().toString().length());
        
      }
      
      @Override
      public void afterTextChanged(Editable s)
      {
        if(editSearch.getText().toString().length()>1)
        {
          imageX.setVisibility(View.VISIBLE);
          resultStataus = true;
          new GetProductListAsyncTask().execute(editSearch.getText().toString());
        }else
        {
          imageX.setVisibility(View.GONE);
        }
      }
      
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count,int after) {
        
      }
    };
    editSearch.addTextChangedListener(watcher);
    
    OnEditorActionListener editorActionListener = new OnEditorActionListener()
    {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
      {
        if (actionId == EditorInfo.IME_ACTION_SEARCH)
        {
          hideKeyboard();
          
          if(!TextUtils.isEmpty(editSearch.getText()))
          {
            resultStataus = false;
            new GetProductListAsyncTask().execute(editSearch.getText().toString());
          }
        }
        return false;
      }
    };
    editSearch.setOnEditorActionListener(editorActionListener);
    
    imageX = (ImageView) findViewById(R.id.search_cancel);
    imageX.setVisibility(View.GONE);
    imageX.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        editSearch.removeTextChangedListener(watcher);
        editSearch.setText("");
        imageX.setVisibility(View.GONE);
        changeUI(editSearch.getText().toString().length());
        hideKeyboard();
        editSearch.addTextChangedListener(watcher);
      }
    });
    
  }
  
  private OnClickListener clickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      Intent i = new Intent(TrendSearchActivity.this, TrendProductWithCategoryActivity.class);
      if (v.getId() == containerAll.getId())
      {
        i.putExtra("category_title", getString(R.string.term_shopping_all));
        i.putExtra("categoryId", "0");
      }
      else if (v.getId() == containerCosmetic.getId())
      {
        i.putExtra("category_title", getString(R.string.term_shopping_cosmetic));
        i.putExtra("categoryId", "37");
      }
      else if (v.getId() == containerShoesbag.getId())
      {
        i.putExtra("category_title", getString(R.string.term_shopping_shoesbag));
        i.putExtra("categoryId", "39");
      }
      else if (v.getId() == containerAccessory.getId())
      {
        i.putExtra("category_title", getString(R.string.term_shopping_accessory));
        i.putExtra("categoryId", "40");
      }
      else if (v.getId() == containerBaby.getId())
      {
        i.putExtra("category_title", getString(R.string.term_shopping_baby));
        i.putExtra("categoryId", "41");
      }
      else if (v.getId() == containerFood.getId())
      {
        i.putExtra("category_title", getString(R.string.term_shopping_food));
        i.putExtra("categoryId", "42");
      }
      startActivity(i);
    }
  };
  
  private class GetProductListAsyncTask extends AsyncTask<String, Void, ArrayList<ProductBean>>
  {
    
    @Override
    protected ArrayList<ProductBean> doInBackground(String... params) 
    {
      if(!TextUtils.isEmpty(params[0]))
      {
        productList = apiClient.getProductListSearch(params[0]);
      }
      
      return productList;
    }
    
    @Override
    protected void onPostExecute(ArrayList<ProductBean> result) 
    {
      adapter.clear();
      adapter.addAll(result);
      
      if(!resultStataus){
        searchResult(productList.size());
      }
      
      super.onPostExecute(result);
    }
  }
  
  private void hideKeyboard()
  {
    editSearch.clearFocus();
    try
    {
      InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  private void changeUI(int size) 
  {
    if (size >= 2)
    {
      searchContainer.setVisibility(View.VISIBLE);
      searchListView.setVisibility(View.VISIBLE);
      searchTitle.setVisibility(View.GONE);
      categoryContainer.setVisibility(View.GONE);
      cartImage.setVisibility(View.GONE);
      searchNotExistContainer.setVisibility(View.GONE);
    }
    else
    {
      cartImage.setVisibility(View.VISIBLE);
      categoryContainer.setVisibility(View.VISIBLE);
      searchContainer.setVisibility(View.GONE);
      searchListView.setVisibility(View.GONE);
      searchNotExistContainer.setVisibility(View.GONE);
    }
  }
  
  private void searchResult(int result)
  {
    String title = getString(R.string.term_search_result)+" ("+result+")";
    
    searchTitle.setText(title);
    searchContainer.setVisibility(View.VISIBLE);
    searchTitle.setVisibility(View.VISIBLE);
    categoryContainer.setVisibility(View.GONE);
    
    if(result>0)
    {
      searchListView.setVisibility(View.VISIBLE);
      searchNotExistContainer.setVisibility(View.GONE);
    }else
    {
      searchListView.setVisibility(View.GONE);
      searchNotExistContainer.setVisibility(View.VISIBLE);
    }
  }
}
