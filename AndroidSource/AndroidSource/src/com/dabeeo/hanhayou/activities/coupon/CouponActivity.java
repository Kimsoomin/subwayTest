package com.dabeeo.hanhayou.activities.coupon;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dabeeo.hanhayou.MainActivity;
import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.beans.CategoryBean;
import com.dabeeo.hanhayou.controllers.coupon.CouponViewPagerAdapter;
import com.dabeeo.hanhayou.fragments.coupon.CouponListFragment;
import com.dabeeo.hanhayou.fragments.coupon.DownloadedCouponListFragment;
import com.dabeeo.hanhayou.managers.AlertDialogManager;
import com.dabeeo.hanhayou.managers.PreferenceManager;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.managers.network.NetworkResult;
import com.dabeeo.hanhayou.utils.SystemUtil;

public class CouponActivity extends ActionBarActivity
{
  private ViewPager viewPager;
  private CouponViewPagerAdapter adapter;
  
  private LinearLayout bottomMenuHome, bottomMenuMyPage, bottomMenuPhotolog, bottomMenuWishList, bottomMenuSearch;
  private LinearLayout containerBottomTab;
  
  private ApiClient apiClient;
  private ArrayList<CategoryBean> categories = new ArrayList<CategoryBean>();
  private HashMap<Integer, String> itemTitle = new HashMap<Integer, String>();
  private MenuItem sortItem;
  private int lastSelectedTab = 0;
  
  
  @SuppressWarnings("deprecation")
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_coupon);
    @SuppressLint("InflateParams")
    View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
    TextView title = (TextView) customActionBar.findViewById(R.id.title);
    title.setText(getString(R.string.term_coupon));
    getSupportActionBar().setCustomView(customActionBar);
    getSupportActionBar().setDisplayShowCustomEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    
    apiClient = new ApiClient(this);
    containerBottomTab = (LinearLayout) findViewById(R.id.container_bottom_tab);
    bottomMenuHome = (LinearLayout) findViewById(R.id.container_menu_home);
    bottomMenuMyPage = (LinearLayout) findViewById(R.id.container_menu_mypage);
    bottomMenuPhotolog = (LinearLayout) findViewById(R.id.container_menu_photolog);
    bottomMenuWishList = (LinearLayout) findViewById(R.id.container_menu_wishlist);
    bottomMenuSearch = (LinearLayout) findViewById(R.id.container_menu_search);
    bottomMenuHome.setOnClickListener(bottomMenuClickListener);
    bottomMenuMyPage.setOnClickListener(bottomMenuClickListener);
    bottomMenuPhotolog.setOnClickListener(bottomMenuClickListener);
    bottomMenuWishList.setOnClickListener(bottomMenuClickListener);
    bottomMenuSearch.setOnClickListener(bottomMenuClickListener);
    
    itemTitle.put(0, getString(R.string.term_all));
    itemTitle.put(1, getString(R.string.term_all));
    
    viewPager = (ViewPager) findViewById(R.id.viewpager);
    viewPager.setOffscreenPageLimit(100);
    
    adapter = new CouponViewPagerAdapter(this, getSupportFragmentManager());
    viewPager.setOnPageChangeListener(pageChangeListener);
    viewPager.setAdapter(adapter);
    
    displayTitles();
    
    loadCategory();
  }
  
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    getMenuInflater().inflate(R.menu.menu_recommend_seoul, menu);
    sortItem = menu.findItem(R.id.all);
    
    if (categories.size() > 0)
    {
      String title = itemTitle.get(lastSelectedTab);
      sortItem.setTitle(title);
      sortItem.setVisible(true);
    }
    else
      sortItem.setVisible(false);
    
    return super.onCreateOptionsMenu(menu);
  }
  
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    if (item.getItemId() == android.R.id.home)
    {
      finish();
      overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }
    if (item.getItemId() == R.id.all)
    {
      showSortDialog();
    }
    return super.onOptionsItemSelected(item);
  }
  
  
  private void showSortDialog()
  {
    ArrayAdapter<CategoryBean> arrayAdapter = new ArrayAdapter<CategoryBean>(this, android.R.layout.select_dialog_item);
    arrayAdapter.addAll(categories);
    final ArrayAdapter<CategoryBean> finalArrayAdapter = arrayAdapter;
    AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
    builderSingle.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
    {
      @Override
      public void onClick(DialogInterface dialog, int which)
      {
        dialog.dismiss();
      }
    });
    
    builderSingle.setAdapter(finalArrayAdapter, new DialogInterface.OnClickListener()
    {
      @Override
      public void onClick(DialogInterface dialog, int which)
      {
        itemTitle.put(lastSelectedTab, categories.get(which).name);
        
        if (lastSelectedTab == 0)
        {
          if (adapter.getItem(0) != null)
            ((CouponListFragment) adapter.getItem(0)).changeFilteringMode(Integer.parseInt(categories.get(which).idx));
        }
        else
        {
          if (adapter.getItem(1) != null)
            ((DownloadedCouponListFragment) adapter.getItem(1)).changeFilteringMode(Integer.parseInt(categories.get(which).idx));
        }
        invalidateOptionsMenu();
      }
    });
    builderSingle.show();
  }
  
  
  @Override
  protected void onResume()
  {
    if (!SystemUtil.isConnectNetwork(this))
      setViewPagerPosition(1);
    super.onResume();
  }
  
  
  private void loadCategory()
  {
    new GetCategoryAsynctask().execute();
  }
  
  
  @SuppressWarnings("deprecation")
  public void setViewPagerPosition(int position)
  {
    viewPager.setCurrentItem(position);
    getSupportActionBar().setSelectedNavigationItem(position);
  }
  
  /**************************************************
   * async task
   ***************************************************/
  private class GetCategoryAsynctask extends AsyncTask<String, Integer, NetworkResult>
  {
    @Override
    protected NetworkResult doInBackground(String... params)
    {
      return apiClient.getCouponCategory();
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      if (!result.isSuccess)
        return;
      
      try
      {
        JSONObject obj = new JSONObject(result.response);
        if (obj.has("category"))
        {
          JSONArray arr = obj.getJSONArray("category");
          CategoryBean bean = new CategoryBean();
          bean.idx = "-1";
          bean.name = getString(R.string.term_all);
          categories.add(bean);
          for (int i = 0; i < arr.length(); i++)
          {
            JSONObject objInArr = arr.getJSONObject(i);
            bean = new CategoryBean();
            bean.setJSONObject(objInArr);
            categories.add(bean);
          }
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      invalidateOptionsMenu();
      super.onPostExecute(result);
    }
  }
  
  /**************************************************
   * listener
   ***************************************************/
  private OnClickListener bottomMenuClickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      if (v.getId() == bottomMenuHome.getId())
      {
        finish();
      }
      else if (v.getId() == bottomMenuMyPage.getId())
      {
        if (PreferenceManager.getInstance(getApplicationContext()).isLoggedIn())
        {
          Intent i = new Intent(CouponActivity.this, MainActivity.class);
          i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          i.putExtra("position", MainActivity.POSITION_MY_PAGE);
          startActivity(i);
        }
        else
        {
          new AlertDialogManager(CouponActivity.this).showNeedLoginDialog(1);
        }
      }
      else if (v.getId() == bottomMenuWishList.getId())
      {
        if (PreferenceManager.getInstance(getApplicationContext()).isLoggedIn())
        {
          Intent i = new Intent(CouponActivity.this, MainActivity.class);
          i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          i.putExtra("position", MainActivity.POSITION_WISHLIST);
          startActivity(i);
        }
        else
        {
          new AlertDialogManager(CouponActivity.this).showNeedLoginDialog(2);
        }
      }
      else if (v.getId() == bottomMenuSearch.getId())
      {
        Intent i = new Intent(CouponActivity.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("position", MainActivity.POSITION_SEARCH);
        startActivity(i);
      }
    }
  };
  
  
  @SuppressWarnings("deprecation")
  private void displayTitles()
  {
    ArrayList<String> titles = new ArrayList<>();
    titles.add(getString(R.string.term_all_coupons));
    titles.add(getString(R.string.term_downloaded_coupon));
    
    for (String title : titles)
    {
      adapter.add(title);
      getSupportActionBar().addTab(getSupportActionBar().newTab().setText(title).setTabListener(tabListener));
    }
    
    adapter.notifyDataSetChanged();
  }
  
  
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    super.onActivityResult(requestCode, resultCode, data);
    
    if (resultCode == RESULT_OK)
      getSupportActionBar().setSelectedNavigationItem(1);
  }
  
  /**************************************************
   * listener
   ***************************************************/
  @SuppressWarnings("deprecation")
  protected TabListener tabListener = new TabListener()
  {
    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft)
    {
      viewPager.setCurrentItem(tab.getPosition());
    }
    
    
    @Override
    public void onTabUnselected(Tab arg0, FragmentTransaction arg1)
    {
      
    }
    
    
    @Override
    public void onTabReselected(Tab arg0, FragmentTransaction arg1)
    {
      
    }
  };
  
  @SuppressWarnings("deprecation")
  protected ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener()
  {
    @Override
    public void onPageSelected(int position)
    {
      invalidateOptionsMenu();
      lastSelectedTab = position;
      getSupportActionBar().setSelectedNavigationItem(position);
      if (position == 0 && !SystemUtil.isConnectNetwork(CouponActivity.this))
      {
        new AlertDialogManager(CouponActivity.this).showDontNetworkConnectDialog();
        Runnable runnn = new Runnable()
        {
          @Override
          public void run()
          {
            getSupportActionBar().setSelectedNavigationItem(1);
          }
        };
        Handler handler = new Handler();
        handler.post(runnn);
      }
    }
  };
}
