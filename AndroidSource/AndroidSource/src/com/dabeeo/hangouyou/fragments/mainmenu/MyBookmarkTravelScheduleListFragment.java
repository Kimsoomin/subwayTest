package com.dabeeo.hangouyou.fragments.mainmenu;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.mypage.sub.MyBookmarkActivity;
import com.dabeeo.hangouyou.activities.travel.TravelScheduleDetailActivity;
import com.dabeeo.hangouyou.beans.ScheduleBean;
import com.dabeeo.hangouyou.controllers.mypage.MySchedulesListAdapter;
import com.dabeeo.hangouyou.external.libraries.GridViewWithHeaderAndFooter;
import com.dabeeo.hangouyou.managers.network.ApiClient;
import com.dabeeo.hangouyou.managers.network.NetworkResult;

public class MyBookmarkTravelScheduleListFragment extends Fragment
{
	public static final int SCHEDULE_TYPE_POPULAR = 0;
	public static final int SCHEDULE_TYPE_MY = 1;
	public static final int SCHEDULE_TYPE_BOOKMARK = 2;
	
	private ProgressBar progressBar;
	private MySchedulesListAdapter adapter;
	private ApiClient apiClient;
	private int page = 1;
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
		selectDelete = (TextView) getView().findViewById(R.id.select_delete);
		emptyContainer = (LinearLayout) getView().findViewById(R.id.empty_container);
		emptyText = (TextView) getView().findViewById(R.id.text_empty);
		recommendContainer = (LinearLayout) getView().findViewById(R.id.recommend_container);
		
		adapter = new MySchedulesListAdapter(getActivity());
		listView = (GridViewWithHeaderAndFooter) getView().findViewById(R.id.gridview);
//    ScheduleListHeaderMallView view = new ScheduleListHeaderMallView(getActivity());
//    view.setBean(null);
//    listView.addHeaderView(view);
		
		listView.setOnItemClickListener(itemClickListener);
		listView.setOnScrollListener(scrollListener);
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
				if (!isLoadEnded && totalItemCount > 0 && totalItemCount <= firstVisibleItem + visibleItemCount)
				{
					page++;
					loadSchedules();
				}
			}
		});
		loadSchedules();
	}
	
	
	public void setEditMode(boolean isEditMode)
	{
		if (adapter.getCount() == 0)
		{
			((MyBookmarkActivity) getActivity()).isEditMode = false;
			((MyBookmarkActivity) getActivity()).invalidateOptionsMenu();
		}
		else
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
				selectDelete.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View arg0)
					{
						Log.w("WARN", "선택된 아이템 리스트 : " + adapter.getCheckedArrayList());
						Builder dialog = new AlertDialog.Builder(getActivity());
						dialog.setTitle(getString(R.string.term_alert));
						dialog.setMessage(getString(R.string.term_delete_confirm));
						dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								if (allCheckBox.isChecked())
								{
									listView.setVisibility(View.GONE);
									emptyContainer.setVisibility(View.VISIBLE);
									setEditMode(false);
									((MyBookmarkActivity) getActivity()).isEditMode = false;
									((MyBookmarkActivity) getActivity()).invalidateOptionsMenu();
									adapter.clear();
								}
							}
						});
						dialog.setNegativeButton(android.R.string.cancel, null);
						dialog.show();
					}
				});
			}
			else
			{
				allCheckContainer.setVisibility(View.GONE);
				selectDelete.setVisibility(View.GONE);
			}
		}
	}
	
	
	public void setType(int type)
	{
		this.type = type;
	}
	
	
	private void loadSchedules()
	{
		progressBar.setVisibility(View.VISIBLE);
		
		if (type == SCHEDULE_TYPE_POPULAR)
			new LoadScheduleAsyncTask().execute();
	}
	
	private class LoadScheduleAsyncTask extends AsyncTask<String, Integer, NetworkResult>
	{
		@Override
		protected void onPreExecute()
		{
			isLoading = true;
			progressBar.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}
		
		
		@Override
		protected NetworkResult doInBackground(String... params)
		{
			//TODO 추후 "내"일정으로 바꿔야 함
			return apiClient.getTravelSchedules(page);
		}
		
		
		@Override
		protected void onPostExecute(NetworkResult result)
		{
			if (result.isSuccess)
			{
				ArrayList<ScheduleBean> beans = new ArrayList<ScheduleBean>();
				try
				{
					JSONObject obj = new JSONObject(result.response);
					JSONArray array = obj.getJSONArray("plan");
					for (int i = 0; i < array.length(); i++)
					{
						JSONObject beanObj = array.getJSONObject(i);
						ScheduleBean bean = new ScheduleBean();
						bean.setJSONObject(beanObj);
						beans.add(bean);
					}
				} catch (Exception e)
				{
					e.printStackTrace();
				}
				
				if (beans.size() == 0)
					isLoadEnded = true;
				
				adapter.addAll(beans);
				
				if (adapter.getCount() == 0)
				{
					listView.setVisibility(View.GONE);
					emptyContainer.setVisibility(View.VISIBLE);
					emptyText.setText(getString(R.string.msg_empty_bookmark));
				}
			}
			progressBar.setVisibility(View.GONE);
			isLoading = false;
			super.onPostExecute(result);
		}
	}
	
	/**************************************************
	 * listener
	 ***************************************************/
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
	
	private OnScrollListener scrollListener = new OnScrollListener()
	{
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState)
		{
		}
		
		
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
		{
			if (!isLoading && totalItemCount > 0 && totalItemCount <= firstVisibleItem + visibleItemCount)
			{
				page++;
				loadSchedules();
			}
		}
	};
}