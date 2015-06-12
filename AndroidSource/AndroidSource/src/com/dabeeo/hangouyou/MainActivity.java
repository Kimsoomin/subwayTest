package com.dabeeo.hangouyou;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dabeeo.hangouyou.activities.mypage.sub.NewAndEditPhotoLogActivity;
import com.dabeeo.hangouyou.activities.sub.PromotionActivity;
import com.dabeeo.hangouyou.fragments.mainmenu.MainFragment;
import com.dabeeo.hangouyou.fragments.mainmenu.SearchFragment;
import com.dabeeo.hangouyou.fragments.mainmenu.SubwayFragment;
import com.dabeeo.hangouyou.fragments.mainmenu.WishListFragment;
import com.dabeeo.hangouyou.fragments.mypage.MyPageFragment;
import com.dabeeo.hangouyou.managers.AlertDialogManager;
import com.dabeeo.hangouyou.managers.CategoryManager;
import com.dabeeo.hangouyou.managers.PreferenceManager;
import com.dabeeo.hangouyou.utils.SystemUtil;

public class MainActivity extends ActionBarActivity
{
  public static SubwayFragment subwayFrament;
  public final static int POSITION_HOME = 0;
  public final static int POSITION_MY_PAGE = 1;
  public final static int POSITION_SEARCH = 2;
  public final static int POSITION_WISHLIST = 3;
  
  private FragmentManager fragmentManager;
  private LinearLayout bottomMenuHome, bottomMenuMyPage, bottomMenuPhotolog, bottomMenuWishList, bottomMenuSearch;
  private TextView title;
  
  private long backKeyPressedTime = 0;
  private Toast appFininshToast;  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    getSupportActionBar().setElevation(0);
    @SuppressLint("InflateParams")
    View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
    
    title = (TextView) customActionBar.findViewById(R.id.title);
    title.setText(getString(R.string.app_name));
    getSupportActionBar().setCustomView(customActionBar);
    getSupportActionBar().setDisplayShowCustomEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(false);
    getSupportActionBar().setDisplayShowTitleEnabled(false);
    
    fragmentManager = getFragmentManager();
    
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
    
    bottomMenuHome.setSelected(true);
    setFragments(POSITION_HOME);
    
    checkPromotion();
  }
  
  
  @Override
  protected void onDestroy()
  {
    super.onDestroy();
    // 자동 로그인이 아니면 로그인 정보 지우기
    PreferenceManager preferenceManager = PreferenceManager.getInstance(this);
    if (!preferenceManager.getIsAutoLogin())
    {
      preferenceManager.clearUserInfo();
    }
  }
  
  
  private void checkPromotion()
  {
    if (TextUtils.isEmpty(PreferenceManager.getInstance(this).getDontShowPopupDate()))
      startActivity(new Intent(MainActivity.this, PromotionActivity.class));
    else
    {
      String dateString = PreferenceManager.getInstance(this).getDontShowPopupDate();
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
      Date date;
      try
      {
        date = format.parse(dateString);
        if (!DateUtils.isToday(date.getTime()))
          startActivity(new Intent(MainActivity.this, PromotionActivity.class));
      }
      catch (ParseException e)
      {
        e.printStackTrace();
      }
    }
  }
  
  
  @Override
  protected void onNewIntent(Intent intent)
  {
    int position = intent.getIntExtra("position", POSITION_HOME);
    setFragments(position);
    super.onNewIntent(intent);
  }
  
  
  @SuppressWarnings("static-access")
  @Override
  protected void onResume()
  {
    if (subwayFrament != null)
    {
      subwayFrament.viewClear();
      subwayFrament = new SubwayFragment();
    }
    super.onResume();
  }
  
  @Override
  public void onBackPressed()
  {
    
    if (System.currentTimeMillis() > backKeyPressedTime + 2000) 
    {
      backKeyPressedTime = System.currentTimeMillis();
      showGuide();
      return;
    }
    
    if (System.currentTimeMillis() <= backKeyPressedTime + 2000) 
    {
      android.os.Process.killProcess(android.os.Process.myPid());
      appFininshToast.cancel();
    }
    
  }
  
  public void showGuide() 
  {
    appFininshToast = Toast.makeText(this, R.string.app_finish, Toast.LENGTH_SHORT);
    appFininshToast.show();
  }
  
  private class GetCategoryAsyncTask extends AsyncTask<String, Integer, Boolean>
  {
    @Override
    protected Boolean doInBackground(String... params)
    {
      CategoryManager.getInstance(MainActivity.this).getCategories();
      return null;
    }
  };
  
  
  public void setFragments(int position)
  {
    bottomMenuHome.setSelected(false);
    bottomMenuMyPage.setSelected(false);
    bottomMenuWishList.setSelected(false);
    bottomMenuSearch.setSelected(false);
    
    Fragment fragment = null;
    
    switch (position)
    {
      case POSITION_HOME:
        bottomMenuHome.setSelected(true);
        title.setText(getString(R.string.app_name));
        fragment = new MainFragment();
        break;
        
      case POSITION_MY_PAGE:
        if (!PreferenceManager.getInstance(getApplicationContext()).isLoggedIn())
        {
          new AlertDialogManager(MainActivity.this).showNeedLoginDialog();
          return;
        }
        bottomMenuMyPage.setSelected(true);
        title.setText(getString(R.string.term_my_page));
        fragment = new MyPageFragment();
        break;
        
      case POSITION_SEARCH:
        bottomMenuSearch.setSelected(true);
        title.setText(R.string.term_search);
        fragment = new SearchFragment();
        break;
        
      case POSITION_WISHLIST:
        if (!SystemUtil.isConnectNetwork(getApplicationContext()))
        {
          new AlertDialogManager(MainActivity.this).showDontNetworkConnectDialog();
          return;
        }
        
        if (!PreferenceManager.getInstance(getApplicationContext()).isLoggedIn())
        {
          new AlertDialogManager(MainActivity.this).showNeedLoginDialog();
          return;
        }
        bottomMenuWishList.setSelected(true);
        title.setText(getString(R.string.term_wishlist));
        fragment = new WishListFragment();
        break;
    }
    
    if (fragment != null)
    {
      FragmentTransaction ft = fragmentManager.beginTransaction();
      ft.replace(R.id.content, fragment);
      ft.commit();
    }
  }
  
  private OnClickListener bottomMenuClickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      if (v.getId() == bottomMenuHome.getId())
      {
        setFragments(POSITION_HOME);
      }
      else if (v.getId() == bottomMenuMyPage.getId())
      {
        setFragments(POSITION_MY_PAGE);
      }
      else if (v.getId() == bottomMenuPhotolog.getId())
      {
        if (!PreferenceManager.getInstance(getApplicationContext()).isLoggedIn())
        {
          new AlertDialogManager(MainActivity.this).showNeedLoginDialog();
          return;
        }
        startActivity(new Intent(getApplicationContext(), NewAndEditPhotoLogActivity.class));
      }
      else if (v.getId() == bottomMenuWishList.getId())
      {
        setFragments(POSITION_WISHLIST);
      }
      else if (v.getId() == bottomMenuSearch.getId())
      {
        setFragments(POSITION_SEARCH);
      }
    }
  };
}
