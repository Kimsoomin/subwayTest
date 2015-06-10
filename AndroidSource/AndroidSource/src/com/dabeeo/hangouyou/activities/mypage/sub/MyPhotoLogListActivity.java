package com.dabeeo.hangouyou.activities.mypage.sub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
import com.dabeeo.hangouyou.beans.MyPhotoLogBean;
import com.dabeeo.hangouyou.controllers.mypage.MyPhotoLogListAdapter;
import com.dabeeo.hangouyou.managers.AlertDialogManager;
import com.dabeeo.hangouyou.managers.AlertDialogManager.AlertListener;

@SuppressWarnings("deprecation")
public class MyPhotoLogListActivity extends ActionBarActivity
{
  private ProgressBar progressBar;
  private ListView listView;
  
  private MyPhotoLogListAdapter adapter;
  
  private Button btnDeleteAll, btnDelete;
  private LinearLayout deleteContainer;
  
  private MenuItem editMenuItem, closeMenuItem;
  private boolean isEditMode = false;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my_photo_log);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    
    deleteContainer = (LinearLayout) findViewById(R.id.container_delete);
    btnDelete = (Button) findViewById(R.id.btn_delete);
    btnDeleteAll = (Button) findViewById(R.id.btn_delete_all);
    btnDelete.setOnClickListener(deleteBtnClickListener);
    btnDeleteAll.setOnClickListener(deleteBtnClickListener);
    
    adapter = new MyPhotoLogListAdapter();
    listView = (ListView) findViewById(android.R.id.list);
    listView.setAdapter(adapter);
    listView.setOnItemClickListener(itemClickListener);
    listView.setOnScrollListener(scrollListener);
    
    findViewById(R.id.btn_register_photolog).setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        startActivity(new Intent(MyPhotoLogListActivity.this, NewAndEditPhotoLogActivity.class));
      }
    });
    isEditMode = true;
    loadPlaces();
  }
  
  
  private void loadPlaces()
  {
    progressBar.setVisibility(View.VISIBLE);
    
    //테스트 가데이터 
    MyPhotoLogBean bean = new MyPhotoLogBean();
    bean.title = "2014 Seoul Design Festival";
    bean.days = "2Days";
    bean.date = "2015.01.01 16:53";
    bean.photoCount = 9;
    bean.likeCount = 7;
    adapter.add(bean);
    
    bean = new MyPhotoLogBean();
    bean.title = "Sally님의 포토로그";
    bean.days = "3Days";
    bean.date = "2015.12.05 12:22";
    bean.photoCount = 33;
    bean.likeCount = 7;
    adapter.add(bean);
    
    bean = new MyPhotoLogBean();
    bean.title = "2015 서울 자전거 대행진";
    bean.days = "4Days";
    bean.date = "2015.01.01 16:53";
    bean.photoCount = 123;
    bean.likeCount = 7;
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
  
  
  private void displayEditMode()
  {
    if (isEditMode)
      deleteContainer.setVisibility(View.VISIBLE);
    else
      deleteContainer.setVisibility(View.GONE);
    
    adapter.setEditMode(isEditMode);
    invalidateOptionsMenu();
  }
  
  /**************************************************
   * listener
   ***************************************************/
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
      
      AlertDialogManager alert = new AlertDialogManager(MyPhotoLogListActivity.this);
      alert.showAlertDialog(getString(R.string.term_alert), getString(R.string.term_delete_confirm), getString(android.R.string.ok), getString(android.R.string.cancel), listener);
    }
  };
  
  /**************************************************
   * listener
   ***************************************************/
  private OnItemClickListener itemClickListener = new OnItemClickListener()
  {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
      startActivity(new Intent(MyPhotoLogListActivity.this, MyPhotoLogDetailActivity.class));
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
//      if (totalItemCount > 0 && totalItemCount > offset && totalItemCount <= firstVisibleItem + visibleItemCount)
//      {
//        offset += limit;
//        load(offset);
//      }
    }
  };
}
