package com.dabeeo.hangouyou.activities.schedule;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.travel.TravelScheduleDetailActivity;
import com.dabeeo.hangouyou.beans.ScheduleBean;
import com.dabeeo.hangouyou.external.libraries.RoundedImageView;

public class RecommendScheduleCompeletedActivity extends ActionBarActivity
{
  private RoundedImageView profileImage;
  private TextView textCompelete;
  private LinearLayout containerRecommendSchedule;
  private Button btnAnotherSchedule, btnNewSchedule;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recommend_schedule_completed);
    
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
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
    
    displayRecommendScheduleView();
  }
  
  
  private void displayRecommendScheduleView()
  {
    //User name 
    textCompelete.setText("PlanB 님의 추천일정이 완성되었습니다!");
    
    ScheduleBean bean = new ScheduleBean();
    bean.title = "서울 쇼핑 투어";
    
    ImageView imageView = (ImageView) findViewById(R.id.imageview);
    TextView title = (TextView) findViewById(R.id.title);
    TextView month = (TextView) findViewById(R.id.month);
    TextView likeCount = (TextView) findViewById(R.id.like_count);
    TextView reviewCount = (TextView) findViewById(R.id.review_count);
    
    title.setText(bean.title);
    month.setText(Integer.toString(bean.month) + getString(R.string.term_month));
    likeCount.setText(Integer.toString(bean.likeCount));
    reviewCount.setText(Integer.toString(bean.reviewCount));
    
    containerRecommendSchedule.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        Intent i = new Intent(RecommendScheduleCompeletedActivity.this, TravelScheduleDetailActivity.class);
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
    final ProgressDialog dialog = new ProgressDialog(RecommendScheduleCompeletedActivity.this);
    dialog.setTitle(getString(R.string.app_name));
    dialog.setMessage(getString(R.string.msg_progress_recommend_schedule));
    dialog.setCancelable(false);
    
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
}
