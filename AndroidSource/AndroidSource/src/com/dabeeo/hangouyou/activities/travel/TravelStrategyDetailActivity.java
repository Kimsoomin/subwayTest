package com.dabeeo.hangouyou.activities.travel;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.mainmenu.PlaceDetailActivity;
import com.dabeeo.hangouyou.activities.sub.ImagePopUpActivity;
import com.dabeeo.hangouyou.beans.ContentBean;
import com.dabeeo.hangouyou.beans.PremiumDetailBean;
import com.dabeeo.hangouyou.managers.network.ApiClient;
import com.dabeeo.hangouyou.managers.network.NetworkResult;
import com.dabeeo.hangouyou.map.BlinkingMap;
import com.dabeeo.hangouyou.utils.MapCheckUtil;
import com.dabeeo.hangouyou.views.SharePickView;
import com.squareup.picasso.Picasso;

public class TravelStrategyDetailActivity extends ActionBarActivity
{
	private LinearLayout contentContainer;
	private ViewGroup horizontalImagesView, layoutDetailPlaceInfo;
	
	private ApiClient apiClient;
	private ProgressBar progressBar;
	
	private int placeIdx = -1;
	private PremiumDetailBean bean;
	private ScrollView scrollView;
	private TextView likeCount;
	private ImageView btnLike;
	private SharePickView sharePickView;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		getSupportActionBar().setElevation(0);
		setTitle("");
		
		setContentView(R.layout.activity_strategy_seoul_detail);
		
		apiClient = new ApiClient(this);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		
		placeIdx = getIntent().getIntExtra("place_idx", -1);
		progressBar = (ProgressBar) findViewById(R.id.progress_bar);
		contentContainer = (LinearLayout) findViewById(R.id.container_details);
		horizontalImagesView = (ViewGroup) findViewById(R.id.horizontal_images_view);
		layoutDetailPlaceInfo = (ViewGroup) findViewById(R.id.layout_place_detail_info);
		likeCount = (TextView) findViewById(R.id.like_count);
		btnLike = (ImageView) findViewById(R.id.btn_like);
		findViewById(R.id.btn_share).setOnClickListener(clickListener);
		findViewById(R.id.btn_like).setOnClickListener(clickListener);
		sharePickView = (SharePickView) findViewById(R.id.view_share_pick);
		
		scrollView = (ScrollView) findViewById(R.id.scroll_view);
		
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.argb(0, 45, 189, 182)));
		scrollView.getViewTreeObserver().addOnScrollChangedListener(new OnScrollChangedListener()
		{
			@Override
			public void onScrollChanged()
			{
				int scrollY = scrollView.getScrollY();
				
				if (scrollY > 255)
					scrollY = 254;
				int opacity = scrollY - 255;
				getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.argb(opacity, 45, 189, 182)));
				if (scrollY < 253)
					setTitle("");
				else
					setTitle(bean.title);
			}
		});
		loadPlaceDetail();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_my_place_detail, menu);
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		if (id == android.R.id.home)
			finish();
		else if (id == R.id.map)
		{
			MapCheckUtil.checkMapExist(TravelStrategyDetailActivity.this, new Runnable()
			{
				@Override
				public void run()
				{
					Intent i = new Intent(TravelStrategyDetailActivity.this, BlinkingMap.class);
					i.putExtra("idx", bean.idx);
					startActivity(i);
				}
			});
		}
		else if (id == R.id.close)
		{
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	private void loadPlaceDetail()
	{
		progressBar.setVisibility(View.VISIBLE);
		progressBar.bringToFront();
		new GetPlaceDetailAsyncTask().execute();
	}
	
	private class GetPlaceDetailAsyncTask extends AsyncTask<String, Integer, NetworkResult>
	{
		
		@Override
		protected NetworkResult doInBackground(String... params)
		{
			return apiClient.getPremiumDetail(placeIdx);
		}
		
		
		@Override
		protected void onPostExecute(NetworkResult result)
		{
			if (result.isSuccess)
			{
				try
				{
					JSONObject obj = new JSONObject(result.response);
					bean = new PremiumDetailBean();
					bean.setJSONObject(obj.getJSONObject("premium"));
				} catch (Exception e)
				{
					e.printStackTrace();
				}
				displayContentData();
			}
			
			progressBar.setVisibility(View.GONE);
			super.onPostExecute(result);
		}
	}
	
	
	private void displayContentData()
	{
		if (bean == null)
			return;
		
		setTitle("");
		((TextView) findViewById(R.id.text_title)).setText(bean.title);
		((TextView) findViewById(R.id.text_description)).setText(bean.address);
		
		//Contents
		if (bean.contents.size() > 0)
		{
			if (!bean.contents.get(0).isText)
				bean.contents.remove(0);
		}
		for (int i = 0; i < bean.contents.size(); i++)
		{
			int detailResId = R.layout.view_premium_seoul_content;
			View view = getLayoutInflater().inflate(detailResId, null);
			TextView text = (TextView) view.findViewById(R.id.text_content);
			ImageView image = (ImageView) view.findViewById(R.id.image_content);
			
			ContentBean contentBean = bean.contents.get(i);
			if (contentBean.isText)
			{
				text.setText(contentBean.text);
				text.setVisibility(View.VISIBLE);
			}
			else
			{
				Display display = getWindowManager().getDefaultDisplay();
				@SuppressWarnings("deprecation")
				int width = display.getWidth();
				Picasso.with(this).load(contentBean.imageUrl).fit().centerCrop().into(image);
				image.setVisibility(View.VISIBLE);
				final String imageUrl = contentBean.imageUrl;
				image.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View arg0)
					{
						Intent i = new Intent(TravelStrategyDetailActivity.this, ImagePopUpActivity.class);
						i.putExtra("imageUrls", bean.allImages);
						i.putExtra("imageUrl", imageUrl);
						startActivity(i);
					}
				});
			}
			
			contentContainer.addView(view);
		}
		
		likeCount.setText(Integer.toString(bean.likeCount));
		
		//Main Image
		Picasso.with(this).load(bean.mainImageUrl).resize(300, 300).centerCrop().into((ImageView) findViewById(R.id.imageview));
		Log.w("WARN", "Image Url : " + bean.mainImageUrl);
		
		//Scroll small Images
		horizontalImagesView.removeAllViews();
		
		for (int i = 0; i < bean.smallImages.size(); i++)
		{
			int resId = R.layout.list_item_recommend_seoul_photo;
			View parentView = getLayoutInflater().inflate(resId, null);
			
			final String imageUrl = bean.smallImages.get(i);
			ImageView view = (ImageView) parentView.findViewById(R.id.photo);
			Picasso.with(this).load(imageUrl).resize(300, 300).centerCrop().into(view);
			final int position = i;
			final String finalImageUrl = imageUrl;
			view.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View arg0)
				{
					Log.w("WARN", "onClick!");
					Intent intent = new Intent(TravelStrategyDetailActivity.this, ImagePopUpActivity.class);
					intent.putExtra("imageUrls", bean.allImages);
					intent.putExtra("imageUrl", finalImageUrl);
					startActivity(intent);
				}
			});
			horizontalImagesView.addView(parentView);
		}
		
		addDetailInfo(getString(R.string.term_address), "서울특별시 광진구 능동로 216");
		addDetailInfo(getString(R.string.term_phone), "02-450-9311");
		addDetailInfo(getString(R.string.term_homepage), bean.homepage);
		addDetailInfo(getString(R.string.term_working_time), bean.businessHours);
		addDetailInfo(getString(R.string.term_price_info), bean.priceInfo);
		addDetailInfo(getString(R.string.term_traffic), bean.trafficInfo);
		addDetailInfo(getString(R.string.term_description), bean.tag);
	}
	
	
	private void addDetailInfo(String title, final String text)
	{
		if (!TextUtils.isEmpty(text))
		{
			int detailResId = R.layout.list_item_strategy_seoul_place_detail_info;
			View view = getLayoutInflater().inflate(detailResId, null);
			TextView titleView = (TextView) view.findViewById(R.id.text_title);
			TextView textView = (TextView) view.findViewById(R.id.text_description);
			ImageView imageCall = (ImageView) view.findViewById(R.id.btn_call);
			ImageView imageAddress = (ImageView) view.findViewById(R.id.btn_address);
			
			if (title.equals(getString(R.string.term_phone)))
			{
				imageCall.setVisibility(View.VISIBLE);
				imageAddress.setVisibility(View.GONE);
				
				imageCall.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View arg0)
					{
						Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + text));
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
					}
				});
			}
			else if (title.equals(getString(R.string.term_address)))
			{
				imageCall.setVisibility(View.GONE);
				imageAddress.setVisibility(View.VISIBLE);
				
				imageAddress.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View arg0)
					{
						Intent i = new Intent(TravelStrategyDetailActivity.this, BlinkingMap.class);
						i.putExtra("place_idx", bean.idx);
						startActivity(i);
					}
				});
			}
			else
			{
				imageCall.setVisibility(View.GONE);
				imageAddress.setVisibility(View.GONE);
			}
			titleView.setText(title);
			textView.setText(text);
			layoutDetailPlaceInfo.addView(view);
		}
	}
	
	/**************************************************
	 * listener
	 ***************************************************/
	private OnClickListener clickListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			if (v.getId() == R.id.btn_share)
			{
				// 공유하기
//        Intent sendIntent = new Intent(Intent.ACTION_SEND);
//        sendIntent.putExtra(Intent.EXTRA_TEXT, "공유테스트");
//        sendIntent.setType("text/plain");
//        startActivity(Intent.createChooser(sendIntent, null));
				sharePickView.setVisibility(View.VISIBLE);
				sharePickView.view.setVisibility(View.VISIBLE);
				sharePickView.bringToFront();
			}
			else if (v.getId() == R.id.btn_like)
			{
				//좋아요 
				btnLike.setActivated(!btnLike.isActivated());
			}
		}
	};
}
