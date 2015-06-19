package com.dabeeo.hanhayou.activities.mypage;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.beans.MyPhotoLogBean;
import com.dabeeo.hanhayou.controllers.mypage.EditPhotoLogListAdapter;
import com.dabeeo.hanhayou.external.libraries.DragSortController;
import com.dabeeo.hanhayou.external.libraries.DragSortListView;

@SuppressWarnings("deprecation")
public class NewAndEditPhotoLogActivity extends ActionBarActivity
{
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
    
    adapter = new EditPhotoLogListAdapter();
    listView = (DragSortListView) findViewById(android.R.id.list);
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
    MyPhotoLogBean bean1 = new MyPhotoLogBean();
    bean1.title = "1";
    adapter.add(bean1);
    MyPhotoLogBean bean2 = new MyPhotoLogBean();
    bean2.title = "2";
    adapter.add(bean2);
    MyPhotoLogBean bean3 = new MyPhotoLogBean();
    bean3.title = "3";
    adapter.add(bean3);
    MyPhotoLogBean bean4 = new MyPhotoLogBean();
    bean4.title = "3";
    adapter.add(bean4);
    adapter.notifyDataSetChanged();
  }
}
