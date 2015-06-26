package com.dabeeo.hanhayou.fragments.mainmenu;

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

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.mainmenu.PlaceDetailActivity;
import com.dabeeo.hanhayou.activities.mypage.MyBookmarkActivity;
import com.dabeeo.hanhayou.beans.PlaceBean;
import com.dabeeo.hanhayou.controllers.mypage.MyPlaceListAdapter;
import com.dabeeo.hanhayou.external.libraries.GridViewWithHeaderAndFooter;
import com.dabeeo.hanhayou.managers.PreferenceManager;
import com.dabeeo.hanhayou.managers.network.ApiClient;

public class MyBookmarkPlaceListFragment extends Fragment
{
  private ProgressBar progressBar;
  private MyPlaceListAdapter adapter;
  private int page = 1;
  private ApiClient apiClient;
  private LinearLayout emptyContainer;
  private GridViewWithHeaderAndFooter listView;
  private LinearLayout allCheckContainer;
  private CheckBox allCheckBox;
  private TextView selectDelete;
  private boolean isLoadEnded = false;
  
  
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
    progressBar = (ProgressBar) getView().findViewById(R.id.progress_bar);
    allCheckContainer = (LinearLayout) getView().findViewById(R.id.container_all_checking);
    allCheckBox = (CheckBox) getView().findViewById(R.id.all_check_box);
    allCheckBox.setOnCheckedChangeListener(checkedChangeListener);
    selectDelete = (TextView) getView().findViewById(R.id.select_delete);
    selectDelete.setOnClickListener(deleteButtonClickListener);
    emptyContainer = (LinearLayout) getView().findViewById(R.id.empty_container);
    
    adapter = new MyPlaceListAdapter(getActivity());
    
    listView = (GridViewWithHeaderAndFooter) getView().findViewById(R.id.gridview);
    listView.setOnItemClickListener(itemClickListener);
    listView.setOnScrollListener(new OnScrollListener()
    {
      @Override
      public void onScrollStateChanged(AbsListView view, int scrollState)
      {
      }
      
      
      @Override
      public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
      {
      }
    });
    listView.setAdapter(adapter);
    
    load(page);
  }
  
  
  public void setEditMode(boolean isEditMode)
  {
    adapter.setEditMode(isEditMode);
    allCheckContainer.setVisibility(isEditMode ? View.VISIBLE : View.GONE);
    selectDelete.setVisibility(isEditMode ? View.VISIBLE : View.GONE);
  }
  
  
  private void load(int offset)
  {
    progressBar.setVisibility(View.VISIBLE);
    new GetAsyncTask().execute(page);
  }
  
  /**************************************************
   * listener
   ***************************************************/
  private OnItemClickListener itemClickListener = new OnItemClickListener()
  {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
      PlaceBean bean = (PlaceBean) adapter.getItem(position);
      Intent i = new Intent(getActivity(), PlaceDetailActivity.class);
      i.putExtra("place_idx", bean.idx);
      startActivity(i);
    }
  };
  
  private OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener()
  {
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
      adapter.setAllCheck(isChecked);
    }
  };
  
  private OnClickListener deleteButtonClickListener = new OnClickListener()
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
          adapter.removeCheckedItem();
          allCheckBox.setChecked(false);
          
          if (adapter.getCount() == 0)
          {
            listView.setVisibility(View.GONE);
            emptyContainer.setVisibility(View.VISIBLE);
            setEditMode(false);
            ((MyBookmarkActivity) getActivity()).toggleEditMode(false);
          }
          
          new DelAsyncTask().execute(idxesString);
        }
      });
      dialog.setNegativeButton(android.R.string.cancel, null);
      dialog.show();
    }
  };
  
  /**************************************************
   * async task
   ***************************************************/
  private class GetAsyncTask extends AsyncTask<Integer, Integer, ArrayList<PlaceBean>>
  {
    @Override
    protected ArrayList<PlaceBean> doInBackground(Integer... params)
    {
      return apiClient.getBookmarkedPlaceList();
    }
    
    
    @Override
    protected void onPostExecute(ArrayList<PlaceBean> result)
    {
      if (result.size() == 0)
        isLoadEnded = true;
      adapter.addAll(result);
      
      if (adapter.getCount() == 0)
      {
        listView.setVisibility(View.GONE);
        emptyContainer.setVisibility(View.VISIBLE);
      }
      progressBar.setVisibility(View.GONE);
      super.onPostExecute(result);
    }
  }
  
  private class DelAsyncTask extends AsyncTask<String, Void, Void>
  {
    @Override
    protected Void doInBackground(String... params)
    {
      for (String idx : params)
      {
        apiClient.setUsedLog(PreferenceManager.getInstance(getActivity()).getUserSeq(), idx, "place", "B");
      }
      return null;
    }
  }
}
