package com.dabeeo.hangouyou.activities.mypage.sub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.MyScheduleBean;
import com.dabeeo.hangouyou.controllers.MySchedulesListAdapter;
import com.dabeeo.hangouyou.managers.AlertDialogManager;
import com.dabeeo.hangouyou.managers.AlertDialogManager.AlertListener;

public class MySchedulesActivity extends ActionBarActivity
{
  private ProgressBar progressBar;
  private ListView listView;
  private MySchedulesListAdapter adapter;
  
  private Button btnDeleteAll, btnDelete;
  private LinearLayout deleteContainer;
  
  private MenuItem editMenuItem, closeMenuItem;
  private boolean isEditMode = false;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my_schedule);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    
    deleteContainer = (LinearLayout) findViewById(R.id.container_delete);
    btnDelete = (Button) findViewById(R.id.btn_delete);
    btnDeleteAll = (Button) findViewById(R.id.btn_delete_all);
    btnDelete.setOnClickListener(deleteBtnClickListener);
    btnDeleteAll.setOnClickListener(deleteBtnClickListener);
    
    listView = (ListView) findViewById(R.id.listview);
    adapter = new MySchedulesListAdapter(this);
    listView.setAdapter(adapter);
    
    listView.setOnItemClickListener(new OnItemClickListener()
    {
      @Override
      public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
      {
        startActivity(new Intent(MySchedulesActivity.this, MyScheduleDetailActivity.class));
      }
    });
    
    listView.setOnScrollListener(new OnScrollListener()
    {
      @Override
      public void onScrollStateChanged(AbsListView view, int scrollState)
      {
        
      }
      
      
      @Override
      public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
      {
//        if (!isLoading && !isLoadEnded && (totalItemCount > 0 && (totalItemCount - firstVisibleItem <= visibleItemCount)))
//        {
//          offset = offset + limit;
//          loadSchedules();
//        }
      }
    });
    
    loadSchedules();
  }
  
  
  private void loadSchedules()
  {
    progressBar.setVisibility(View.VISIBLE);
    
    //테스트 가데이터 
    MyScheduleBean bean = new MyScheduleBean();
    bean.title = "서울 2박 3일 여행";
    bean.month = 3;
    bean.likeCount = 70;
    bean.reviewCount = 11;
    adapter.add(bean);
    
    bean = new MyScheduleBean();
    bean.title = "명동 쇼핑";
    bean.month = 1;
    bean.likeCount = 50;
    bean.reviewCount = 13;
    adapter.add(bean);
    
    progressBar.setVisibility(View.GONE);
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
  
  private OnClickListener deleteBtnClickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      AlertListener listener;
      
      if (v.getId() == btnDelete.getId())
      {
        listener = new AlertListener()
        {
          @Override
          public void onPositiveButtonClickListener()
          {
            adapter.delete();
          }
          
          
          @Override
          public void onNegativeButtonClickListener()
          {
            
          }
        };
      }
      else
      {
        listener = new AlertListener()
        {
          @Override
          public void onPositiveButtonClickListener()
          {
            adapter.clear();
            isEditMode = false;
            displayEditMode();
          }
          
          
          @Override
          public void onNegativeButtonClickListener()
          {
            
          }
        };
      }
      
      AlertDialogManager alert = new AlertDialogManager(MySchedulesActivity.this);
      alert.showAlertDialog(getString(R.string.term_alert), getString(R.string.term_delete_confirm), getString(android.R.string.ok), getString(android.R.string.cancel), listener);
    }
  };
  
  
  private void displayEditMode()
  {
    if (isEditMode)
      deleteContainer.setVisibility(View.VISIBLE);
    else
      deleteContainer.setVisibility(View.GONE);
    
    adapter.setEditMode(isEditMode);
    invalidateOptionsMenu();
  }
  
}
