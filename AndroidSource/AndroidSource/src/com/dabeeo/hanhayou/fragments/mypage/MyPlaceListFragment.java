package com.dabeeo.hanhayou.fragments.mypage;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
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
import com.dabeeo.hanhayou.activities.mypage.MyPlaceActivity;
import com.dabeeo.hanhayou.activities.mypage.MyPlaceDetailActivity;
import com.dabeeo.hanhayou.beans.OfflineBehaviorBean;
import com.dabeeo.hanhayou.beans.PlaceBean;
import com.dabeeo.hanhayou.controllers.OfflineContentDatabaseManager;
import com.dabeeo.hanhayou.controllers.OfflineDeleteManager;
import com.dabeeo.hanhayou.controllers.mypage.MyPlaceListAdapter;
import com.dabeeo.hanhayou.external.libraries.GridViewWithHeaderAndFooter;
import com.dabeeo.hanhayou.managers.PreferenceManager;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.managers.network.NetworkResult;
import com.dabeeo.hanhayou.map.BlinkingCommon;
import com.dabeeo.hanhayou.utils.SystemUtil;

public class MyPlaceListFragment extends Fragment
{
  private int categoryId = -1;
  private int page = 1;
  private boolean isLoadEnded = false;
  
  private ProgressBar progressBar;
  private MyPlaceListAdapter adapter;
  private ApiClient apiClient;
  private LinearLayout emptyContainer;
  private LinearLayout allCheckContainer;
  private CheckBox allCheckBox;
  private TextView selectDelete;
  private GridViewWithHeaderAndFooter listView;
  
  public boolean isEditmode = false;
  public View bottomMargin;
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    int resId = R.layout.fragment_my_place_list;
    return inflater.inflate(resId, null);
  }
  
  
  @Override
  public void onActivityCreated(Bundle savedInstanceState)
  {
    super.onActivityCreated(savedInstanceState);
    
    apiClient = new ApiClient(getActivity());
    
    allCheckContainer = (LinearLayout) getView().findViewById(R.id.container_all_checking);
    progressBar = (ProgressBar) getView().findViewById(R.id.progress_bar);
    emptyContainer = (LinearLayout) getView().findViewById(R.id.empty_container);
    allCheckBox = (CheckBox) getView().findViewById(R.id.all_check_box);
    selectDelete = (TextView) getView().findViewById(R.id.select_delete);
    
    adapter = new MyPlaceListAdapter(getActivity());
    listView = (GridViewWithHeaderAndFooter) getView().findViewById(R.id.gridview);
    listView.setOnItemClickListener(itemClickListener);
    listView.setOnScrollListener(scrollListener);
    listView.setAdapter(adapter);
    
    bottomMargin = (View) getView().findViewById(R.id.bottom_margin);
    
    load();
  }
  
  
  public int listCount()
  {
    return adapter.getCount();
  }
  
  
  public void refresh()
  {
    page = 1;
    isLoadEnded = false;
    adapter.clear();
    load();
  }
  
  
  public void setEditMode(boolean isEditMode)
  {
    adapter.setEditMode(isEditMode);
    isEditmode = isEditMode;
    
    if (isEditMode)
    {
      allCheckContainer.setVisibility(View.VISIBLE);
      allCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener()
      {
        @Override
        public void onCheckedChanged(CompoundButton arg0, boolean arg1)
        {
          adapter.setAllCheck(arg1);
        }
      });
      selectDelete.setVisibility(View.VISIBLE);
      selectDelete.setOnClickListener(deleteClickListener);
    }
    else
    {
      allCheckContainer.setVisibility(View.GONE);
      selectDelete.setVisibility(View.GONE);
    }
  }
  
  
  private void load()
  {
    progressBar.setVisibility(View.VISIBLE);
    new GetStoreAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
  }
  
  
  public void setCategoryId(int categoryId)
  {
    //전체, 명소, 쇼핑 등 
    this.categoryId = categoryId;
  }
  
  /**************************************************
   * listener
   ***************************************************/
  private OnScrollListener scrollListener = new OnScrollListener()
  {
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState)
    {
      
    }
    
    
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    { 
      if(isEditmode)
      {
        if (totalItemCount > 0 && totalItemCount == firstVisibleItem + visibleItemCount)
        {
          bottomMargin.setVisibility(View.VISIBLE);
        }else
        {
          bottomMargin.setVisibility(View.GONE);
        }
      }
    }
  };
  
  private OnItemClickListener itemClickListener = new OnItemClickListener()
  {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
      PlaceBean bean = (PlaceBean) adapter.getItem(position);
      Intent i = new Intent(getActivity(), MyPlaceDetailActivity.class);
      i.putExtra("place_idx", bean.idx);
      startActivity(i);
    }
  };
  
  private OnClickListener deleteClickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      ArrayList<String> idxes = new ArrayList<>();
      for (PlaceBean bean : adapter.getCheckedArrayList())
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
          allCheckBox.setChecked(false);
          adapter.removeCheckedItem();
          
          if (adapter.getCount() == 0)
          {
            listView.setVisibility(View.GONE);
            emptyContainer.setVisibility(View.VISIBLE);
            ((MyPlaceActivity) getActivity()).toggleEditMode(false);
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
              new DelPlaceTask().execute(deleteIdxs);
          }
          else
          {
            Log.w("WARN", "내 일정 삭제 오프라인 처리 ");
            OfflineDeleteManager manager = new OfflineDeleteManager(getActivity());
            for (int i = 0; i < finalIdxs.size(); i++)
            {
              OfflineBehaviorBean bean = new OfflineBehaviorBean();
              bean.setDeleteMyPlace(getActivity(), finalIdxs.get(i));
              manager.addBehavior(bean);
            }
          }
        }
      });
      dialog.setNegativeButton(android.R.string.cancel, null);
      dialog.show();
    }
  };
  
  /**************************************************
   * async task
   ***************************************************/
  private class GetStoreAsyncTask extends AsyncTask<Void, Integer, ArrayList<PlaceBean>>
  {
    @Override
    protected ArrayList<PlaceBean> doInBackground(Void... params)
    {
      return apiClient.getMyPlaceList(categoryId);
    }
    
    
    @Override
    protected void onPostExecute(ArrayList<PlaceBean> result)
    {
      ArrayList<PlaceBean> tempArray = new ArrayList<PlaceBean>();
      tempArray.addAll(result);
      
      if (categoryId == -1)
        adapter.addAll(tempArray);
      else
      {
        for (int i = 0; i < tempArray.size(); i++)
        {
          if (tempArray.get(i).categoryId == categoryId)
            adapter.add(tempArray.get(i));
        }
      }
      
      if (adapter.getCount() == 0)
      {
        isLoadEnded = true;
        listView.setVisibility(View.GONE);
        emptyContainer.setVisibility(View.VISIBLE);
      }
      progressBar.setVisibility(View.GONE);
      if (categoryId == -1 && SystemUtil.isConnectNetwork(getActivity()))
        new SaveLocalAsyncTask().execute();
      super.onPostExecute(result);
    }
  }
  
  private class SaveLocalAsyncTask extends AsyncTask<Void, Integer, String>
  {
    @Override
    protected String doInBackground(Void... params)
    {
      OfflineContentDatabaseManager offlineDatabaseManager = new OfflineContentDatabaseManager(getActivity());
      offlineDatabaseManager.writeDatabaseMyPlace(adapter.getJSONArrayString());
      return null;
    }
    
    
    @Override
    protected void onPostExecute(String result)
    {
      Log.w("WARN", "Save to local Finish!");
      super.onPostExecute(result);
    }
  }
  
  private class DelPlaceTask extends AsyncTask<String, Void, NetworkResult>
  {
    @Override
    protected NetworkResult doInBackground(String... params)
    {
      NetworkResult result = null;
      result = apiClient.deleteMyPlace(params[0], PreferenceManager.getInstance(getActivity()).getUserSeq());
      
      return result;
    }
  }
}
