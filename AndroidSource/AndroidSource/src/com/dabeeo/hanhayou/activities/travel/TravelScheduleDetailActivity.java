package com.dabeeo.hanhayou.activities.travel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.mainmenu.WriteReviewActivity;
import com.dabeeo.hanhayou.activities.mypage.MySchedulesActivity;
import com.dabeeo.hanhayou.beans.ScheduleDetailBean;
import com.dabeeo.hanhayou.controllers.mainmenu.TravelScheduleDetailViewPagerAdapter;
import com.dabeeo.hanhayou.managers.AlertDialogManager;
import com.dabeeo.hanhayou.managers.PreferenceManager;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.managers.network.NetworkResult;
import com.dabeeo.hanhayou.map.BlinkingMap;
import com.dabeeo.hanhayou.utils.MapCheckUtil;
import com.dabeeo.hanhayou.utils.SystemUtil;
import com.dabeeo.hanhayou.views.SharePickView;

@SuppressWarnings("deprecation")
public class TravelScheduleDetailActivity extends ActionBarActivity
{
  private ViewPager viewPager;
  public TravelScheduleDetailViewPagerAdapter adapter;
  private ProgressBar progressBar;
  private ApiClient apiClient;
  private TextView title;
  
  private String idx;
  public ScheduleDetailBean bean;
  private String startDateString = "";
  private int dayCount;
  
  public LinearLayout containerWriteReview, containerLike, containerIsPublic, bottomLayout;
  public Button btnIsPublic, btnLike, btnBookmark, btnSaveSchedule;
  private SharePickView sharePickView;
  private int currentPosition = 0;
  private boolean isRecommendSchedule = false;
  public boolean isMySchedule = false;
  private String titleFromOutSide;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_schedule_detail);
    @SuppressLint("InflateParams")
    View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
    title = (TextView) customActionBar.findViewById(R.id.title);
    title.setText(getString(R.string.term_travel_schedule));
    getSupportActionBar().setCustomView(customActionBar);
    getSupportActionBar().setDisplayShowCustomEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    
    titleFromOutSide = getIntent().getStringExtra("title");
    isRecommendSchedule = getIntent().getBooleanExtra("isRecommend", false);
    
    idx = getIntent().getStringExtra("idx");
    apiClient = new ApiClient(this);
    progressBar = (ProgressBar) findViewById(R.id.progressbar);
    viewPager = (ViewPager) findViewById(R.id.viewpager);
    viewPager.setOnPageChangeListener(pageChangeListener);
    viewPager.setOffscreenPageLimit(100);
    sharePickView = (SharePickView) findViewById(R.id.view_share_pick);
    
    btnLike = (Button) findViewById(R.id.btn_like);
    containerLike = (LinearLayout) findViewById(R.id.container_like);
    containerWriteReview = (LinearLayout) findViewById(R.id.write_review_container);
    containerIsPublic = (LinearLayout) findViewById(R.id.container_is_public);
    btnIsPublic = (Button) findViewById(R.id.btn_is_public);
    btnBookmark = (Button) findViewById(R.id.btn_bookmark);
    
    bottomLayout = (LinearLayout) findViewById(R.id.bottom_layout);
    btnSaveSchedule = (Button) findViewById(R.id.btn_recommend_travel_schedule_save);
    if (getIntent().hasExtra("startDate"))
    {
      startDateString = getIntent().getStringExtra("startDate");
      dayCount = getIntent().getIntExtra("dayCount", 0);
      bottomLayout.setVisibility(View.GONE);
      btnSaveSchedule.setVisibility(View.VISIBLE);
    }
    else
    {
      bottomLayout.setVisibility(View.VISIBLE);
      btnSaveSchedule.setVisibility(View.GONE);
      
    }
    btnSaveSchedule.setOnClickListener(new OnClickListener()
    {
      
      @Override
      public void onClick(View v)
      {
        new CompleteAsyncTask().execute();
      }
    });
    
    btnBookmark.setOnClickListener(clickListener);
    findViewById(R.id.btn_share).setOnClickListener(clickListener);
    btnLike.setOnClickListener(clickListener);
    findViewById(R.id.btn_write_review).setOnClickListener(clickListener);
    
    loadScheduleDetail();
  }
  
  
  private void loadScheduleDetail()
  {
    new LoadScheduleAsyncTask().execute();
  }
  
  
  private void displayContent()
  {
    if (TextUtils.isEmpty(titleFromOutSide))
      title.setText(bean.title);
    else
      title.setText(titleFromOutSide);
    adapter = new TravelScheduleDetailViewPagerAdapter(this, getSupportFragmentManager());
    adapter.setBean(bean);
    adapter.setIsRecommendSchedule(isRecommendSchedule);
    adapter.setIsMySchedule(isMySchedule);
    
    viewPager.setAdapter(adapter);
    adapter.notifyDataSetChanged();
    viewPager.invalidate();
    
    for (int i = 0; i < adapter.getCount(); i++)
    {
      getSupportActionBar().addTab(getSupportActionBar().newTab().setText(adapter.getPageTitle(i)).setTabListener(tabListener));
    }
  }
  
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    getMenuInflater().inflate(R.menu.menu_map, menu);
    return super.onCreateOptionsMenu(menu);
  }
  
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    if (item.getItemId() == R.id.map)
    {
      MapCheckUtil.checkMapExist(TravelScheduleDetailActivity.this, new Runnable()
      {
        @Override
        public void run()
        {
          Intent intent = new Intent(TravelScheduleDetailActivity.this, BlinkingMap.class);
          intent.putExtra("plan", bean.days);
          SimpleDateFormat simpleDateformat = new SimpleDateFormat("yyyy.MM.dd");
          String currentDate;
          intent.putExtra("palnDayDiff", adapter.getCount() - 1);
          
          try
          {
            if (currentPosition == 0)
            {
              //전체일정
              currentDate = simpleDateformat.format(bean.startDate);
              intent.putExtra("planDayNum", 1);
              intent.putExtra("planDayLat", bean.days.get(0).spots.get(0).lat);
              intent.putExtra("planDayLng", bean.days.get(0).spots.get(0).lng);
              intent.putExtra("planYMD", currentDate);
            }
            else
            {
              //N번쨰 일정인 경우
              Date date = calCalender(currentPosition - 1);
              currentDate = simpleDateformat.format(date);
              intent.putExtra("planDayNum", currentPosition);
              intent.putExtra("planDayLat", bean.days.get(currentPosition - 1).spots.get(0).lat);
              intent.putExtra("planDayLng", bean.days.get(currentPosition - 1).spots.get(0).lng);
              intent.putExtra("planYMD", currentDate);
            }
            startActivity(intent);
          }
          catch (Exception e)
          {
            e.printStackTrace();
          }
        }
      });
    }
    else if (item.getItemId() == android.R.id.home)
    {
      finish();
      overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }
    return super.onOptionsItemSelected(item);
  }
  
  
  public Date calCalender(int calender)
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime(bean.startDate);
    cal.add(Calendar.DATE, calender);
    return cal.getTime();
  }
  
  /**************************************************
   * listener
   ***************************************************/
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
  
  protected ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener()
  {
    @Override
    public void onPageSelected(int position)
    {
      getSupportActionBar().setSelectedNavigationItem(position);
      currentPosition = position;
    }
  };
  
  private OnClickListener clickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      if (v.getId() == R.id.btn_share)
      {
        if (!SystemUtil.isConnectNetwork(getApplicationContext()))
        {
          new AlertDialogManager(TravelScheduleDetailActivity.this).showDontNetworkConnectDialog();
          return;
        }
        // 공유하기
        sharePickView.setVisibility(View.VISIBLE);
        sharePickView.view.setVisibility(View.VISIBLE);
        sharePickView.bringToFront();
      }
      else
      {
        if (!SystemUtil.isConnectNetwork(getApplicationContext()))
        {
          new AlertDialogManager(TravelScheduleDetailActivity.this).showDontNetworkConnectDialog();
          return;
        }
        
        if (PreferenceManager.getInstance(getApplicationContext()).isLoggedIn())
        {
          if (v.getId() == btnBookmark.getId())
          {
            new ToggleBookmarkTask().execute();
          }
          else if (v.getId() == btnLike.getId())
          {
            new ToggleLikeTask().execute();
          }
          else if (v.getId() == R.id.btn_write_review)
          {
            //리뷰쓰기
            Intent i = new Intent(TravelScheduleDetailActivity.this, WriteReviewActivity.class);
            i.putExtra("idx", idx);
            i.putExtra("type", "plan");
            startActivity(i);
          }
        }
        else
        {
          
          new AlertDialogManager(TravelScheduleDetailActivity.this).showNeedLoginDialog(-1);
        }
      }
    }
  };
  
  /**************************************************
   * async task
   ***************************************************/
  private class LoadScheduleAsyncTask extends AsyncTask<String, Integer, ScheduleDetailBean>
  {
    @Override
    protected void onPreExecute()
    {
      progressBar.setVisibility(View.VISIBLE);
      super.onPreExecute();
    }
    
    
    @Override
    protected ScheduleDetailBean doInBackground(String... params)
    {
      return apiClient.getTravelScheduleDetail(idx);
    }
    
    
    @Override
    protected void onPostExecute(ScheduleDetailBean result)
    {
      bean = result;
      btnLike.setActivated(bean.isLiked);
      btnBookmark.setActivated(bean.isBookmarked);
      progressBar.setVisibility(View.GONE);
      displayContent();
      super.onPostExecute(result);
    }
  }
  
  private class CompleteAsyncTask extends AsyncTask<Void, Integer, NetworkResult>
  {
    @Override
    protected NetworkResult doInBackground(Void... params)
    {
      ApiClient apiClient = new ApiClient(getApplicationContext());
      return apiClient.completeCreateRecommendSchedule(startDateString, idx, dayCount);
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      super.onPostExecute(result);
      startActivity(new Intent(TravelScheduleDetailActivity.this, MySchedulesActivity.class));
      finish();
    }
  }
  
  private class ToggleBookmarkTask extends AsyncTask<Void, Void, NetworkResult>
  {
    @Override
    protected NetworkResult doInBackground(Void... params)
    {
      return apiClient.setUsedLog(PreferenceManager.getInstance(getApplicationContext()).getUserSeq(), bean.idx, "plan", "B");
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      super.onPostExecute(result);
      try
      {
        JSONObject obj = new JSONObject(result.response);
        
        if (obj.getString("result").equals("INS"))
        {
          btnBookmark.setActivated(true);
          Toast.makeText(TravelScheduleDetailActivity.this, getString(R.string.msg_add_bookmark), Toast.LENGTH_LONG).show();
        }
        else
        {
          btnBookmark.setActivated(false);
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }
  
  private class ToggleLikeTask extends AsyncTask<Void, Void, NetworkResult>
  {
    @Override
    protected NetworkResult doInBackground(Void... params)
    {
      return apiClient.setUsedLog(PreferenceManager.getInstance(getApplicationContext()).getUserSeq(), bean.idx, "premium", "L");
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      super.onPostExecute(result);
      try
      {
        JSONObject obj = new JSONObject(result.response);
        btnLike.setActivated(obj.getString("result").equals("INS"));
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }
  
}
