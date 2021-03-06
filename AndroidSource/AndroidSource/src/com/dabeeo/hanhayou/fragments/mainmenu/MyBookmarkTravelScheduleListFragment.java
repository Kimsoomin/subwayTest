package com.dabeeo.hanhayou.fragments.mainmenu;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.mypage.MyBookmarkActivity;
import com.dabeeo.hanhayou.activities.travel.TravelScheduleDetailActivity;
import com.dabeeo.hanhayou.beans.ScheduleBean;
import com.dabeeo.hanhayou.controllers.mypage.MySchedulesListAdapter;
import com.dabeeo.hanhayou.external.libraries.GridViewWithHeaderAndFooter;
import com.dabeeo.hanhayou.managers.PreferenceManager;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.utils.SystemUtil;

public class MyBookmarkTravelScheduleListFragment extends Fragment
{
  public static final int SCHEDULE_TYPE_POPULAR = 0;
  public static final int SCHEDULE_TYPE_MY = 1;
  public static final int SCHEDULE_TYPE_BOOKMARK = 2;
  
  private ProgressBar progressBar;
  private MySchedulesListAdapter adapter;
  private ApiClient apiClient;
  private int type = SCHEDULE_TYPE_POPULAR;
  private boolean isLoading = false;
  private int lastVisibleItem = 0;
  private boolean isLoadEnded = false;
  
  private LinearLayout emptyContainer;
  private TextView emptyText;
  private LinearLayout recommendContainer;
  private GridViewWithHeaderAndFooter listView;
  private LinearLayout allCheckContainer;
  private CheckBox allCheckBox;
  private TextView selectDelete;
  
  public boolean isEditmode = false;
  public View bottomMargin;
  
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    int resId = R.layout.fragment_my_bookmark_travel_schedule;
    return inflater.inflate(resId, null);
  }
  
  
  @Override
  public void onActivityCreated(Bundle savedInstanceState)
  {
    super.onActivityCreated(savedInstanceState);
    apiClient = new ApiClient(getActivity());
    progressBar = (ProgressBar) getView().findViewById(R.id.progress_bar);
    
    allCheckContainer = (LinearLayout) getView().findViewById(R.id.container_all_checking);
    allCheckBox = (CheckBox) getView().findViewById(R.id.all_check_box);
    allCheckBox.setOnCheckedChangeListener(checkedChangeListener);
    selectDelete = (TextView) getView().findViewById(R.id.select_delete);
    selectDelete.setOnClickListener(deleteButtonClickListener);
    emptyContainer = (LinearLayout) getView().findViewById(R.id.empty_container);
    emptyText = (TextView) getView().findViewById(R.id.text_empty);
    recommendContainer = (LinearLayout) getView().findViewById(R.id.recommend_container);
    
    adapter = new MySchedulesListAdapter(getActivity());
    listView = (GridViewWithHeaderAndFooter) getView().findViewById(R.id.gridview);
    
    bottomMargin = (View) getView().findViewById(R.id.bottom_margin);
    listView.setOnItemClickListener(itemClickListener);
    listView.setAdapter(adapter);
    listView.setOnScrollListener(new OnScrollListener()
    {
      @Override
      public void onScrollStateChanged(AbsListView view, int scrollState)
      {
      }
      
      
      @Override
      public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
      {
        if (isEditmode)
        {
          if (totalItemCount > 0 && totalItemCount == firstVisibleItem + visibleItemCount)
          {
            bottomMargin.setVisibility(View.VISIBLE);
          }
          else
          {
            bottomMargin.setVisibility(View.GONE);
          }
        }
      }
    });
    loadSchedules();
  }
  
  
  public void setEditMode(boolean isEditMode)
  {
    adapter.setEditMode(isEditMode);
    isEditmode = isEditMode;
    allCheckContainer.setVisibility(isEditMode ? View.VISIBLE : View.GONE);
    selectDelete.setVisibility(isEditMode ? View.VISIBLE : View.GONE);
  }
  
  
  public void setType(int type)
  {
    this.type = type;
  }
  
  
  private void loadSchedules()
  {
    progressBar.setVisibility(View.VISIBLE);
    new LoadScheduleAsyncTask().execute();
  }
  
  private class LoadScheduleAsyncTask extends AsyncTask<String, Integer, ArrayList<ScheduleBean>>
  {
    @Override
    protected void onPreExecute()
    {
      isLoading = true;
      progressBar.setVisibility(View.VISIBLE);
      super.onPreExecute();
    }
    
    
    @Override
    protected ArrayList<ScheduleBean> doInBackground(String... params)
    {
      return apiClient.getBookmarkedSchedules(-1);
    }
    
    
    @Override
    protected void onPostExecute(ArrayList<ScheduleBean> result)
    {
      
      if (result.size() == 0)
        isLoadEnded = true;
      
      adapter.addAll(result);
      
      if (adapter.getCount() == 0)
      {
        listView.setVisibility(View.GONE);
        emptyContainer.setVisibility(View.VISIBLE);
        emptyText.setText(getString(R.string.msg_empty_bookmark));
      }
      progressBar.setVisibility(View.GONE);
      isLoading = false;
      ((MyBookmarkActivity) getActivity()).invalidateOptionsMenu();
      super.onPostExecute(result);
    }
  }
  
  private OnClickListener deleteButtonClickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      ArrayList<String> idxes = new ArrayList<>();
      for (ScheduleBean bean : adapter.getCheckedArrayList())
      {
        idxes.add(bean.idx);
      }
      
      if (idxes.isEmpty())
        return;
      
      final ArrayList<String> finalIdxs = idxes;
      Builder dialog = new AlertDialog.Builder(getActivity());
      dialog.setTitle(getString(R.string.term_alert));
      dialog.setMessage(getString(R.string.term_delete_confirm));
      dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
      {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
          adapter.removeCheckedItem();
          allCheckBox.setChecked(false);
          
          if (adapter.getCount() == 0)
          {
            listView.setVisibility(View.GONE);
            emptyContainer.setVisibility(View.VISIBLE);
            setEditMode(false);
            ((MyBookmarkActivity) getActivity()).toggleEditMode(false);
          }
          
          String deleteIdxs = "";
          if (finalIdxs.size() > 1)
          {
            for (int i = 0; i < finalIdxs.size(); i++)
            {
              deleteIdxs += finalIdxs.get(i);
              if (i != finalIdxs.size() - 1)
                deleteIdxs += ",";
            }
          }
          else
          {
            try
            {
              deleteIdxs = finalIdxs.get(0);
            }
            catch (Exception e)
            {
              e.printStackTrace();
            }
          }
          
          if (SystemUtil.isConnectNetwork(getActivity()))
          {
            if (!TextUtils.isEmpty(deleteIdxs))
              new DelAsyncTask().execute(deleteIdxs);
          }
        }
      });
      dialog.setNegativeButton(android.R.string.cancel, null);
      dialog.show();
    }
  };
  
  private class DelAsyncTask extends AsyncTask<String, Void, Void>
  {
    @Override
    protected Void doInBackground(String... params)
    {
      apiClient.reqeustBookmarkCancel(PreferenceManager.getInstance(getActivity()).getUserSeq(), params[0], "plan");
      return null;
    }
    
    
    @Override
    protected void onPostExecute(Void result)
    {
      ((MyBookmarkActivity) getActivity()).invalidateOptionsMenu();
      super.onPostExecute(result);
    }
  }
  
  
  public int getCount()
  {
    return adapter.getCount();
  }
  
  /**************************************************
   * listener
   ***************************************************/
  private OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener()
  {
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
      adapter.setAllCheck(isChecked);
    }
  };
  
  private OnItemClickListener itemClickListener = new OnItemClickListener()
  {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
      ScheduleBean bean = (ScheduleBean) adapter.getItem(position);
      Intent i = new Intent(getActivity(), TravelScheduleDetailActivity.class);
      i.putExtra("idx", bean.idx);
      startActivity(i);
    }
  };
  
}
