package com.dabeeo.hangouyou.activities.schedule;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.mypage.sub.MySchedulesActivity;
import com.dabeeo.hangouyou.activities.travel.TravelScheduleDetailActivity;
import com.dabeeo.hangouyou.beans.ScheduleBean;
import com.dabeeo.hangouyou.external.libraries.RoundedImageView;
import com.dabeeo.hangouyou.managers.PreferenceManager;
import com.dabeeo.hangouyou.managers.network.ApiClient;
import com.dabeeo.hangouyou.managers.network.NetworkResult;
import com.dabeeo.hangouyou.utils.ImageDownloader;
import com.dabeeo.hangouyou.views.CharacterProgressView;

@SuppressWarnings("deprecation")
public class RecommendScheduleCompeletedActivity extends ActionBarActivity
{
  private RoundedImageView profileImage;
  private TextView textCompelete;
  private RelativeLayout containerRecommendSchedule;
  private Button btnAnotherSchedule, btnNewSchedule;
  private ScheduleBean bean;
  
  
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
    
    profileImage = (RoundedImageView) findViewById(R.id.profile_image);
    textCompelete = (TextView) findViewById(R.id.text_compelete);
    containerRecommendSchedule = (RelativeLayout) findViewById(R.id.container_recommend_schedule);
    
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
  
  
  private void displayRecommendScheduleView(String json, String startDate)
  {
    textCompelete.setText(PreferenceManager.getInstance(getApplicationContext()).getUserName() + " 님의 추천일정이 완성되었습니다!");
    
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
    
    title.setText(bean.title);
    month.setText(Integer.toString(bean.dayCount) + getString(R.string.term_month));
    ImageDownloader.displayImage(RecommendScheduleCompeletedActivity.this, bean.imageUrl, imageView, null);
    
    containerRecommendSchedule.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        Intent i = new Intent(RecommendScheduleCompeletedActivity.this, TravelScheduleDetailActivity.class);
        i.putExtra("idx", bean.idx);
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
      dialog.setNegativeButton(android.R.string.cancel, null);
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
        finish();
      }
    });
    dialog.setNegativeButton(android.R.string.cancel, null);
    dialog.show();
  }
  
  
  private void generateNewSchedule()
  {
    Builder builder = new AlertDialog.Builder(RecommendScheduleCompeletedActivity.this);
    CharacterProgressView pView = new CharacterProgressView(RecommendScheduleCompeletedActivity.this);
    pView.title.setText(getString(R.string.msg_progress_recommend_schedule));
    builder.setView(pView);
    builder.setCancelable(false);
    final AlertDialog dialog = builder.create();
    
    if (!dialog.isShowing())
      dialog.show();
    
    new Handler().postDelayed(new Runnable()
    {
      @Override
      public void run()
      {
        dialog.dismiss();
      }
    }, 1500);
  }
  
  /**************************************************
   * async task
   ***************************************************/
  private class CompleteAsyncTask extends AsyncTask<ScheduleBean, Integer, NetworkResult>
  {
    @Override
    protected NetworkResult doInBackground(ScheduleBean... params)
    {
      ApiClient apiClient = new ApiClient(getApplicationContext());
      return apiClient.completeCreateRecommendSchedule(bean.startDateString, bean.idx, bean.dayCount);
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
