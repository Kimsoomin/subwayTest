package com.dabeeo.hangouyou;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.dabeeo.hangouyou.activities.mypage.sub.NewAndEditPhotoLogActivity;
import com.dabeeo.hangouyou.fragments.mainmenu.MainFragment;
import com.dabeeo.hangouyou.fragments.mainmenu.SearchResultFragment;
import com.dabeeo.hangouyou.fragments.mainmenu.SubwayFragment;
import com.dabeeo.hangouyou.fragments.mypage.MyPageFragment;
import com.dabeeo.hangouyou.managers.CategoryManager;

public class MainActivity extends ActionBarActivity
{
  public static SubwayFragment subwayFrament;
  public final static int POSITION_HOME = 0;
  public final static int POSITION_MY_PAGE = 1;
  public final static int POSITION_SEARCH = 2;
  
  private FragmentManager fragmentManager;
  private LinearLayout bottomMenuHome, bottomMenuMyPage, bottomMenuPhotolog, bottomMenuWishList, bottomMenuSearch;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
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
    setFragments(POSITION_HOME);
  }
  
  @SuppressWarnings("unused")
  private class GetCategoryAsyncTask extends AsyncTask<String, Integer, Boolean>
  {
    @Override
    protected Boolean doInBackground(String... params)
    {
      CategoryManager.getInstance(MainActivity.this).getCategories();
      return null;
    }
  };
  
  
  private void setFragments(int position)
  {
    bottomMenuHome.setSelected(false);
    bottomMenuMyPage.setSelected(false);
    bottomMenuWishList.setSelected(false);
    bottomMenuSearch.setSelected(false);
    
    Fragment fragment = null;
    
    switch (position)
    {
      case POSITION_HOME:
        setTitle(getString(R.string.app_name));
        fragment = new MainFragment();
        bottomMenuHome.setSelected(true);
        break;
      
      case POSITION_MY_PAGE:
        setTitle(getString(R.string.term_my_page));
        fragment = new MyPageFragment();
        bottomMenuMyPage.setSelected(true);
        break;
      
      case POSITION_SEARCH:
        setTitle(R.string.term_search);
        fragment = new SearchResultFragment();
        bottomMenuSearch.setSelected(true);
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
        //로그인 체크 후 실행
        setFragments(POSITION_MY_PAGE);
      }
      else if (v.getId() == bottomMenuPhotolog.getId())
      {
        //로그인 체크 후 실행
        startActivity(new Intent(getApplicationContext(), NewAndEditPhotoLogActivity.class));
      }
      else if (v.getId() == bottomMenuWishList.getId())
      {
        //로그인 체크 후 실행
      }
      else if (v.getId() == bottomMenuSearch.getId())
      {
        setFragments(POSITION_SEARCH);
      }
    }
  };
  
}
