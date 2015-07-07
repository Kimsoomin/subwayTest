package com.dabeeo.hanhayou.activities.schedule;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.mypage.MySchedulesActivity;
import com.dabeeo.hanhayou.activities.travel.TravelScheduleDetailActivity;
import com.dabeeo.hanhayou.beans.ScheduleBean;
import com.dabeeo.hanhayou.external.libraries.RoundedImageView;
import com.dabeeo.hanhayou.managers.PreferenceManager;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.managers.network.NetworkResult;
import com.dabeeo.hanhayou.utils.ImageDownloader;
import com.dabeeo.hanhayou.views.CharacterProgressView;

public class RecommendScheduleCompeletedActivity extends ActionBarActivity
{
  private RoundedImageView profileImage;
  private TextView textCompelete;
  private LinearLayout containerRecommendSchedule;
  private Button btnAnotherSchedule, btnNewSchedule;
  private ScheduleBean bean;
  private ApiClient apiClient;
  private int theme = 0;
  
  public String myScheduleTitle;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recommend_schedule_completed);
    
    @SuppressLint("InflateParams")
    View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
    TextView title = (TextView) customActionBar.findViewById(R.id.title);
    title.setText(getString(R.string.term_recommend_schedule_compeleted));
    getSupportActionBar().setCustomView(customActionBar);
    getSupportActionBar().setDisplayShowCustomEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    theme = getIntent().getIntExtra("theme", 1);
    apiClient = new ApiClient(getApplicationContext());
    profileImage = (RoundedImageView) findViewById(R.id.profile_image);
    textCompelete = (TextView) findViewById(R.id.text_compelete);
    containerRecommendSchedule = (LinearLayout) findViewById(R.id.container_recommend_schedule);
    
    btnAnotherSchedule = (Button) findViewById(R.id.btn_another_schedule_recommend);
    btnNewSchedule = (Button) findViewById(R.id.btn_new_schedule);
    
    btnAnotherSchedule.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        generateNewSchedule();
      }
    });
    btnNewSchedule.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        showFinishDialog();
      }
    });
    
    // TODO http 결과에서 startDate가 넘어오면 stardDate 관련 부분은 지우기  
    displayRecommendScheduleView(getIntent().getStringExtra("json"), getIntent().getStringExtra("startDate"));
  }
  
  
  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event)
  {
    if (keyCode == KeyEvent.KEYCODE_BACK)
    {
      showFinishDialog();
      return true;
    }
    return false;
  }
  
  
  private void displayRecommendScheduleView(String json, String startDate)
  {
    String userName = PreferenceManager.getInstance(getApplicationContext()).getUserName();
    String completeText = userName + getString(R.string.msg_comeplete_recommend_schedule);
    SpannableStringBuilder style = new SpannableStringBuilder(completeText);
    style.setSpan(new ForegroundColorSpan(Color.parseColor("#444a4b")), 0, userName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    textCompelete.setText(style);
    
    bean = new ScheduleBean();
    try
    {
      JSONObject obj = new JSONObject(json);
      JSONObject planObj = new JSONObject(obj.getString("plan"));
      bean.setJSONObject(planObj);
      bean.startDateString = startDate;
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return;
    }
    
    ImageView imageView = (ImageView) findViewById(R.id.imageview);
    TextView title = (TextView) findViewById(R.id.title);
    TextView month = (TextView) findViewById(R.id.month);
    
    myScheduleTitle = getString(R.string.msg_recommend_schedule_title);
    myScheduleTitle = myScheduleTitle.replaceAll("#1", PreferenceManager.getInstance(RecommendScheduleCompeletedActivity.this).getUserName());
    myScheduleTitle = myScheduleTitle.replaceAll("#2", Integer.toString(bean.dayCount));
    final String finalMyScheduleTitle = myScheduleTitle;
    title.setText(myScheduleTitle);
    month.setText(Integer.toString(bean.dayCount) + "天");
    ImageDownloader.displayImage(RecommendScheduleCompeletedActivity.this, bean.imageUrl, imageView, null);
    
    containerRecommendSchedule.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        Intent i = new Intent(RecommendScheduleCompeletedActivity.this, TravelScheduleDetailActivity.class);
        i.putExtra("title", finalMyScheduleTitle);
        
        i.putExtra("idx", bean.idx);
        i.putExtra("isRecommend", true);
        i.putExtra("startDate", bean.startDateString);
        i.putExtra("dayCount", bean.dayCount);
        startActivity(i);
      }
    });
  }
  
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    getMenuInflater().inflate(R.menu.menu_compelete, menu);
    return super.onCreateOptionsMenu(menu);
  }
  
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    int id = item.getItemId();
    if (id == android.R.id.home)
    {
      showFinishDialog();
    }
    else if (id == R.id.compelete)
    {
      Builder dialog = new AlertDialog.Builder(RecommendScheduleCompeletedActivity.this);
      dialog.setTitle(getString(R.string.app_name));
      dialog.setMessage(getString(R.string.msg_save_my_schedule));
      dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
      {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
          new CompleteAsyncTask().execute(bean);
        }
      });
      dialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
      {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
          Intent i = new Intent(RecommendScheduleCompeletedActivity.this, MySchedulesActivity.class);
          startActivity(i);
          finish();
        }
      });
      dialog.show();
    }
    
    return super.onOptionsItemSelected(item);
  }
  
  
  private void showFinishDialog()
  {
    Builder dialog = new AlertDialog.Builder(RecommendScheduleCompeletedActivity.this);
    dialog.setTitle(getString(R.string.app_name));
    dialog.setMessage(getString(R.string.msg_init_recommend_schedule));
    dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
    {
      @Override
      public void onClick(DialogInterface dialog, int which)
      {
        Intent i = new Intent(RecommendScheduleCompeletedActivity.this, RecommendScheduleActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
      }
    });
    dialog.setNegativeButton(android.R.string.cancel, null);
    dialog.show();
  }
  
  
  private void generateNewSchedule()
  {
    new CreateAsyncTask().execute();
  }
  
  /**************************************************
   * async task
   ***************************************************/
  private class CreateAsyncTask extends AsyncTask<String, Integer, NetworkResult>
  {
    AlertDialog dialog;
    String startDate;
    
    
    @Override
    protected void onPreExecute()
    {
      Builder builder = new AlertDialog.Builder(RecommendScheduleCompeletedActivity.this);
      CharacterProgressView pView = new CharacterProgressView(RecommendScheduleCompeletedActivity.this);
      pView.title.setText(getString(R.string.msg_progress_recommend_schedule));
      builder.setView(pView);
      builder.setCancelable(false);
      dialog = builder.create();
      
      if (!dialog.isShowing())
        dialog.show();
      
      super.onPreExecute();
    }
    
    
    @Override
    protected NetworkResult doInBackground(String... params)
    {
      return apiClient.getCreateRecommendSchedule(startDate, bean.dayCount, theme);
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      dialog.dismiss();
      
      if (TextUtils.isEmpty(result.response))
        return;
      
      displayRecommendScheduleView(result.response, startDate);
      super.onPostExecute(result);
    }
  }
  
  private class CompleteAsyncTask extends AsyncTask<ScheduleBean, Integer, NetworkResult>
  {
    @Override
    protected NetworkResult doInBackground(ScheduleBean... params)
    {
      return apiClient.completeCreateRecommendSchedule(bean.startDateString, bean.idx, bean.dayCount, myScheduleTitle);
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      super.onPostExecute(result);
      startActivity(new Intent(RecommendScheduleCompeletedActivity.this, MySchedulesActivity.class));
      finish();
    }
  }
}
