package com.dabeeo.hangouyou.activities.mypage.sub;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.controllers.EditPhotoLogListAdapter;
import com.dabeeo.hangouyou.external.libraries.DragSortController;
import com.dabeeo.hangouyou.external.libraries.DragSortListView;

public class NewAndEditPhotoLogActivity extends ActionBarActivity
{
  private ViewGroup container;
  private DragSortListView listView;
  private DragSortController controller;
  private EditPhotoLogListAdapter adapter;
  
  public int dragStartMode = DragSortController.ON_DOWN;
  public int removeMode = DragSortController.FLING_REMOVE;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_photo_log);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    listView = (DragSortListView) findViewById(R.id.listview);
    adapter = new EditPhotoLogListAdapter(this);
    listView.setAdapter(adapter);
    listView.setDropListener(onDropListener);
    
    DragSortController controller = new DragSortController(listView);
    controller.setDragHandleId(R.id.photo);
//    controller.setClickRemoveId(R.id.click_remove);
    controller.setRemoveEnabled(false);
    controller.setSortEnabled(true);
    controller.setDragInitMode(dragStartMode);
    controller.setRemoveMode(removeMode);
    
    listView.setFloatViewManager(controller);
    listView.setOnTouchListener(controller);
    listView.setDragEnabled(true);
    displayContents();
  }
  
  private DragSortListView.DropListener onDropListener = new DragSortListView.DropListener()
  {
    @Override
    public void drop(int from, int to)
    {
    }
  };
  
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    getMenuInflater().inflate(R.menu.menu_new_photo_log, menu);
    return true;
  }
  
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    int id = item.getItemId();
    if (id == android.R.id.home)
      finish();
    else if (id == R.id.save)
      Log.i("NewPhotoLogActivity.java | onOptionsItemSelected", "|" + "save" + "|");
    return super.onOptionsItemSelected(item);
  }
  
  
  private void displayContents()
  {
  }
}
