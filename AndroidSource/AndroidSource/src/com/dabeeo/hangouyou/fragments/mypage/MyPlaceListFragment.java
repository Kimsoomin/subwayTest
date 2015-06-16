package com.dabeeo.hangouyou.fragments.mypage;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.mypage.sub.MyPlaceActivity;
import com.dabeeo.hangouyou.activities.mypage.sub.MyPlaceDetailActivity;
import com.dabeeo.hangouyou.beans.PlaceBean;
import com.dabeeo.hangouyou.controllers.mypage.MyPlaceListAdapter;
import com.dabeeo.hangouyou.external.libraries.GridViewWithHeaderAndFooter;
import com.dabeeo.hangouyou.managers.PreferenceManager;
import com.dabeeo.hangouyou.managers.network.ApiClient;
import com.dabeeo.hangouyou.managers.network.NetworkResult;

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
    
    load();
  }
  
  
  public void setEditMode(boolean isEditMode)
  {
    adapter.setEditMode(isEditMode);
    
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
    new GetStoreAsyncTask().execute();
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
      if (!isLoadEnded && totalItemCount > 0 && totalItemCount <= firstVisibleItem + visibleItemCount)
      {
        page++;
        load();
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
      
      final String[] idxesString = idxes.toArray(new String[idxes.size()]);
      
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
          
          new DelPlaceTask().execute(idxesString);
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
      return apiClient.getPlaceList(page, categoryId, PreferenceManager.getInstance(getActivity()).getUserSeq());
    }
    
    
    @Override
    protected void onPostExecute(ArrayList<PlaceBean> result)
    {
      adapter.addAll(result);
      if (result.size() == 0)
        isLoadEnded = true;
      progressBar.setVisibility(View.GONE);
      super.onPostExecute(result);
    }
  }
  
  private class DelPlaceTask extends AsyncTask<String, Void, NetworkResult>
  {
    @Override
    protected NetworkResult doInBackground(String... params)
    {
      NetworkResult result = null;
      for (String idx : params)
      {
        result = apiClient.deleteMyPlace(idx, PreferenceManager.getInstance(getActivity()).getUserSeq());
      }
      return result;
    }
  }
}
