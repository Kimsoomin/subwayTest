package com.dabeeo.hangouyou.activities.mypage.sub;

import java.util.ArrayList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.schedule.RecommendScheduleActivity;
import com.dabeeo.hangouyou.beans.ScheduleBean;
import com.dabeeo.hangouyou.controllers.mypage.MySchedulesListAdapter;
import com.dabeeo.hangouyou.managers.AlertDialogManager;
import com.dabeeo.hangouyou.managers.AlertDialogManager.AlertListener;
import com.dabeeo.hangouyou.managers.PreferenceManager;
import com.dabeeo.hangouyou.managers.network.ApiClient;
import com.dabeeo.hangouyou.utils.SystemUtil;

@SuppressWarnings("deprecation")
public class MySchedulesActivity extends ActionBarActivity
{
  private ProgressBar progressBar;
  private GridView listView;
  private MySchedulesListAdapter adapter;
  
  private Button btnDelete;
  
  private MenuItem editMenuItem, closeMenuItem;
  private boolean isEditMode = false;
  private Button btnRecommendSchedule, btnRecommendScheduleInEmpty;
  
  private TextView emptyText;
  private CheckBox deleteAllCheckbox;
  private LinearLayout emptyContainer;
  private boolean isLoading = false;
  private boolean isLoadEnded = false;
  private ApiClient apiClient;
  private int page = 1;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my_schedule);
    
    int resId = R.layout.custom_action_bar;
    View customActionBar = LayoutInflater.from(this).inflate(resId, null);
    TextView title = (TextView) customActionBar.findViewById(R.id.title);
    title.setText(getString(R.string.term_my_schedule));
    getSupportActionBar().setCustomView(customActionBar);
    getSupportActionBar().setDisplayShowCustomEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    apiClient = new ApiClient(this);
    progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    emptyContainer = (LinearLayout) findViewById(R.id.empty_container);
    
    emptyText = (TextView) findViewById(R.id.text_empty);
    deleteAllCheckbox = (CheckBox) findViewById(R.id.all_check_box);
    deleteAllCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener()
    {
      @Override
      public void onCheckedChanged(CompoundButton arg0, boolean arg1)
      {
        adapter.setAllCheck(arg1);
      }
    });
    btnRecommendScheduleInEmpty = (Button) findViewById(R.id.recommend_button);
    btnRecommendScheduleInEmpty.setOnClickListener(buttonClickListener);
    
    btnRecommendSchedule = (Button) findViewById(R.id.btn_recommend_travel_schedule);
    btnRecommendSchedule.setOnClickListener(buttonClickListener);
    
    btnDelete = (Button) findViewById(R.id.btn_delete);
    btnDelete.setOnClickListener(buttonClickListener);
    
    adapter = new MySchedulesListAdapter(this);
    listView = (GridView) findViewById(R.id.gridview);
    listView.setAdapter(adapter);
    listView.setOnItemClickListener(itemClickListener);
    listView.setOnScrollListener(scrollListener);
  }
  
  
  @Override
  protected void onResume()
  {
    super.onResume();
    loadSchedules();
  }
  
  
  private void loadSchedules()
  {
    if (!isLoading)
      new LoadScheduleAsyncTask().execute();
  }
  
  private class LoadScheduleAsyncTask extends AsyncTask<String, Integer, ArrayList<ScheduleBean>>
  {
    @Override
    protected void onPreExecute()
    {
      isLoading = true;
      progressBar.setVisibility(View.VISIBLE);
      progressBar.bringToFront();
      super.onPreExecute();
    }
    
    
    @Override
    protected ArrayList<ScheduleBean> doInBackground(String... params)
    {
      return apiClient.getTravelSchedules(page, PreferenceManager.getInstance(getApplicationContext()).getUserSeq());
    }
    
    
    @Override
    protected void onPostExecute(ArrayList<ScheduleBean> result)
    {
      if (result.size() == 0)
        isLoadEnded = true;
      
      adapter.addAll(result);
      
      if (adapter.getCount() == 0 && result.size() == 0)
      {
        emptyContainer.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
        emptyText.setText(getString(R.string.msg_empty_my_schedule));
        btnRecommendSchedule.setVisibility(View.VISIBLE);
      }
      else
      {
        emptyContainer.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
        emptyText.setText(getString(R.string.msg_empty_my_schedule));
        btnRecommendSchedule.setVisibility(View.VISIBLE);
      }
      progressBar.setVisibility(View.GONE);
      isLoading = false;
      super.onPostExecute(result);
    }
  }
  
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    getMenuInflater().inflate(R.menu.menu_edit, menu);
    editMenuItem = menu.findItem(R.id.edit);
    closeMenuItem = menu.findItem(R.id.close);
    if (isEditMode)
    {
      editMenuItem.setVisible(false);
      closeMenuItem.setVisible(true);
    }
    else
    {
      editMenuItem.setVisible(true);
      closeMenuItem.setVisible(false);
    }
    return super.onCreateOptionsMenu(menu);
  }
  
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    if (item.getItemId() == android.R.id.home)
      finish();
    else if (item.getItemId() == editMenuItem.getItemId())
    {
      isEditMode = true;
      displayEditMode();
    }
    else if (item.getItemId() == closeMenuItem.getItemId())
    {
      isEditMode = false;
      displayEditMode();
    }
    return super.onOptionsItemSelected(item);
  }
  
  
  private void displayEditMode()
  {
    if (adapter.getCount() == 0)
    {
      isEditMode = false;
      invalidateOptionsMenu();
      return;
    }
    
    if (isEditMode)
    {
      btnDelete.setVisibility(View.VISIBLE);
      btnRecommendSchedule.setVisibility(View.GONE);
      deleteAllCheckbox.setVisibility(View.VISIBLE);
    }
    else
    {
      btnDelete.setVisibility(View.GONE);
      btnRecommendSchedule.setVisibility(View.VISIBLE);
      deleteAllCheckbox.setVisibility(View.GONE);
    }
    
    adapter.setEditMode(isEditMode);
    invalidateOptionsMenu();
  }
  
  /**************************************************
   * listener
   ***************************************************/
  private OnClickListener buttonClickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      if (v.getId() == btnRecommendScheduleInEmpty.getId() || v.getId() == btnRecommendSchedule.getId())
      {
        if (!SystemUtil.isConnectNetwork(MySchedulesActivity.this))
          new AlertDialogManager(MySchedulesActivity.this).showDontNetworkConnectDialog();
        else
        {
          Intent i = new Intent(MySchedulesActivity.this, RecommendScheduleActivity.class);
          startActivity(i);
        }
      }
      else if (v.getId() == btnDelete.getId())
      {
        AlertDialogManager alert = new AlertDialogManager(MySchedulesActivity.this);
        alert.showAlertDialog(getString(R.string.term_alert), getString(R.string.term_delete_confirm), getString(android.R.string.ok), getString(android.R.string.cancel), new AlertListener()
        {
          @Override
          public void onPositiveButtonClickListener()
          {
            if (deleteAllCheckbox.isChecked())
            {
              listView.setVisibility(View.GONE);
              emptyContainer.setVisibility(View.VISIBLE);
              adapter.setEditMode(false);
              isEditMode = false;
              displayEditMode();
              adapter.clear();
            }
          }
          
          
          @Override
          public void onNegativeButtonClickListener()
          {
            
          }
        });
      }
    }
  };
  
  private OnItemClickListener itemClickListener = new OnItemClickListener()
  {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
      ScheduleBean bean = (ScheduleBean) adapter.getItem(position);
      Intent i = new Intent(MySchedulesActivity.this, MyScheduleDetailActivity.class);
      i.putExtra("idx", bean.idx);
      startActivity(i);
    }
  };
  
  private OnScrollListener scrollListener = new OnScrollListener()
  {
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState)
    {
    }
    
    
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {
      if (!isLoadEnded && !isLoading && totalItemCount > 0 && totalItemCount <= firstVisibleItem + visibleItemCount)
      {
        page++;
        loadSchedules();
      }
    }
  };
}
