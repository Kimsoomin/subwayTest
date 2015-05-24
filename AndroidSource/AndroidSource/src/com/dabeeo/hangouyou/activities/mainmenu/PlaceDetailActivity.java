package com.dabeeo.hangouyou.activities.mainmenu;

import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.CouponBean;
import com.dabeeo.hangouyou.beans.PlaceDetailBean;
import com.dabeeo.hangouyou.beans.ProductBean;
import com.dabeeo.hangouyou.beans.TicketBean;
import com.dabeeo.hangouyou.external.libraries.stikkylistview.StikkyHeaderBuilder;
import com.dabeeo.hangouyou.managers.network.ApiClient;
import com.dabeeo.hangouyou.managers.network.NetworkResult;
import com.dabeeo.hangouyou.map.BlinkingMap;
import com.dabeeo.hangouyou.views.CustomScrollView;
import com.dabeeo.hangouyou.views.CustomScrollView.ScrollViewListener;
import com.dabeeo.hangouyou.views.DetailCouponView;
import com.dabeeo.hangouyou.views.DetailTicketView;
import com.dabeeo.hangouyou.views.PlaceDetailHeaderView;
import com.dabeeo.hangouyou.views.PlaceDetailTitleView;
import com.dabeeo.hangouyou.views.ProductView;
import com.dabeeo.hangouyou.views.ReviewContainerView;

@SuppressWarnings("deprecation")
public class PlaceDetailActivity extends ActionBarActivity
{
	private CustomScrollView scrollView;
	
	private PlaceDetailHeaderView headerView;
	private PlaceDetailTitleView titleView;
	private TextView textDetail;
	private TextView textRate;
	
	private LinearLayout containerProduct;
	
	private RelativeLayout reviewLayout;
	private ReviewContainerView reviewContainerView;
	
	public Button btnReviewBest, btnReviewSoso, btnReviewWorst;
	private ProgressBar progressBar;
	
	private ApiClient apiClient;
	private String placeIdx;
	private PlaceDetailBean bean;
	private ViewGroup layoutDetailPlaceInfo;
	private LinearLayout containerTicketAndCoupon;
	public LinearLayout containerWriteReview;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_detail);
		@SuppressLint("InflateParams")
		View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
		TextView title = (TextView) customActionBar.findViewById(R.id.title);
		title.setText(getString(R.string.term_place));
		getSupportActionBar().setCustomView(customActionBar);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		
		apiClient = new ApiClient(this);
		placeIdx = getIntent().getStringExtra("place_idx");
		
		containerWriteReview = (LinearLayout) findViewById(R.id.write_review_container);
		progressBar = (ProgressBar) findViewById(R.id.progress_bar);
		layoutDetailPlaceInfo = (ViewGroup) findViewById(R.id.layout_place_detail_info);
		
		containerTicketAndCoupon = (LinearLayout) findViewById(R.id.container_coupon_and_ticket);
		
		textDetail = (TextView) findViewById(R.id.text_detail);
		textRate = (TextView) findViewById(R.id.text_rate);
		
		btnReviewBest = (Button) findViewById(R.id.btn_review_best);
		btnReviewSoso = (Button) findViewById(R.id.btn_review_soso);
		btnReviewWorst = (Button) findViewById(R.id.btn_review_worst);
		btnReviewBest.setOnClickListener(rateClickListener);
		btnReviewSoso.setOnClickListener(rateClickListener);
		btnReviewWorst.setOnClickListener(rateClickListener);
		
		containerProduct = (LinearLayout) findViewById(R.id.container_product);
		reviewLayout = (RelativeLayout) findViewById(R.id.review_layout);
		
		headerView = (PlaceDetailHeaderView) findViewById(R.id.header_view);
		titleView = (PlaceDetailTitleView) findViewById(R.id.title_view);
		headerView.init();
		titleView.init();
		
		scrollView = (CustomScrollView) findViewById(R.id.scrollview);
		scrollView.getViewTreeObserver().addOnScrollChangedListener(new OnScrollChangedListener()
		{
			@SuppressLint("NewApi")
			@Override
			public void onScrollChanged()
			{
				int scrollY = scrollView.getScrollY();
				
				if (scrollY > 255)
					scrollY = 254;
				scrollY = scrollY / 10;
				int opacity = 255 - scrollY;
				if (opacity > 255)
					opacity = 255;
				titleView.container.setBackground(new ColorDrawable(Color.argb(255, opacity, opacity, opacity)));
			}
		});
		
		FrameLayout header = (FrameLayout) findViewById(R.id.header);
		Resources r = getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, r.getDisplayMetrics());
		
		findViewById(R.id.btn_bookmark).setOnClickListener(clickListener);
		findViewById(R.id.btn_share).setOnClickListener(clickListener);
		findViewById(R.id.btn_like).setOnClickListener(clickListener);
		findViewById(R.id.btn_write_review).setOnClickListener(clickListener);
		
		StikkyHeaderBuilder.stickTo(scrollView).setHeader(header).minHeightHeaderPixel((int) px).build();
		
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
			Intent i = new Intent(PlaceDetailActivity.this, BlinkingMap.class);
			i.putExtra("idx", bean.idx);
			startActivity(i);
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
			return apiClient.getPlaceDetail(placeIdx);
		}
		
		
		@Override
		protected void onPostExecute(NetworkResult result)
		{
			if (result.isSuccess)
			{
				try
				{
					JSONObject obj = new JSONObject(result.response);
					bean = new PlaceDetailBean();
					bean.setJSONObject(obj.getJSONObject("place"));
				} catch (Exception e)
				{
					e.printStackTrace();
				}
				displayContentData();
				headerView.setBean(bean);
			}
			progressBar.setVisibility(View.GONE);
			super.onPostExecute(result);
		}
	}
	
	
	private void displayContentData()
	{
		//TEST BEAN
		TicketBean ticketBean = new TicketBean();
		ticketBean.title = "경복궁 동반1인 무료 입장";
		DetailTicketView view = new DetailTicketView(PlaceDetailActivity.this);
		view.setBean(ticketBean);
		containerTicketAndCoupon.addView(view);
		
		CouponBean couponBean = new CouponBean();
		couponBean.title = "서울 시티투어 버스 20% 할인";
		DetailCouponView couponView = new DetailCouponView(PlaceDetailActivity.this);
		couponView.setBean(couponBean);
		containerTicketAndCoupon.addView(couponView);
		
		containerProduct.removeAllViews();
		ProductView productView = new ProductView(PlaceDetailActivity.this);
		ProductBean productBean = new ProductBean();
		productBean.title = "XXX 수분크림";
		productBean.originalPrice = 150;
		productBean.discountPrice = 93;
		productView.setBean(productBean, productBean);
		containerProduct.addView(productView);
		
		if (bean == null)
			return;
		
		reviewContainerView = new ReviewContainerView(PlaceDetailActivity.this, "place", bean.idx);
		reviewLayout.addView(reviewContainerView);
		
		textRate.setText(Integer.toString(bean.rate));
		textDetail.setText(bean.contents);
		
		addDetailInfo(getString(R.string.term_address), bean.address);
		addDetailInfo(getString(R.string.term_phone), bean.contact);
		addDetailInfo(getString(R.string.term_homepage), bean.homepage);
		
		titleView.setBean(bean);
	}
	
	
	private void addDetailInfo(String title, final String text)
	{
		if (!TextUtils.isEmpty(text))
		{
			int detailResId = R.layout.list_item_strategy_seoul_place_detail_info;
			View view = getLayoutInflater().inflate(detailResId, null);
			TextView titleView = (TextView) view.findViewById(R.id.text_title);
			TextView textView = (TextView) view.findViewById(R.id.text_description);
			titleView.setText(title);
			textView.setText(text);
			
			ImageView btnCall = (ImageView) view.findViewById(R.id.btn_call);
			ImageView btnAddress = (ImageView) view.findViewById(R.id.btn_address);
			if (title.equals(getString(R.string.term_address)))
			{
				btnAddress.setVisibility(View.VISIBLE);
				btnAddress.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View arg0)
					{
						Intent i = new Intent(PlaceDetailActivity.this, BlinkingMap.class);
						startActivity(i);
					}
				});
			}
			else if (title.equals(getString(R.string.term_phone)))
			{
				btnCall.setVisibility(View.VISIBLE);
				btnCall.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View arg0)
					{
						Intent callIntent = new Intent(Intent.ACTION_DIAL);
						callIntent.setData(Uri.parse("tel:" + text));
						startActivity(callIntent);
					}
				});
			}
			layoutDetailPlaceInfo.addView(view);
		}
	}
	
	/**************************************************
	 * listener
	 ***************************************************/
	private OnClickListener rateClickListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			btnReviewBest.setSelected(false);
			btnReviewSoso.setSelected(false);
			btnReviewWorst.setSelected(false);
			
			int rate = 3;
			if (v.getId() == btnReviewBest.getId())
			{
				btnReviewBest.setSelected(true);
				rate = 5;
			}
			else if (v.getId() == btnReviewSoso.getId())
			{
				btnReviewSoso.setSelected(true);
				rate = 3;
			}
			else if (v.getId() == btnReviewWorst.getId())
			{
				btnReviewWorst.setSelected(true);
				rate = 1;
			}
			
			Intent i = new Intent(PlaceDetailActivity.this, WriteReviewActivity.class);
			i.putExtra("rate", rate);
			startActivity(i);
		}
	};
	
	private OnClickListener clickListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			if (v.getId() == R.id.btn_bookmark)
			{
				// 북마크 토글
			}
			else if (v.getId() == R.id.btn_share)
			{
				// 공유하기
//				Intent sendIntent = new Intent(Intent.ACTION_SEND);
//				sendIntent.putExtra(Intent.EXTRA_TEXT, "공유테스트");
//				sendIntent.setType("text/plain");
//				startActivity(Intent.createChooser(sendIntent, null));
				
				final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(PlaceDetailActivity.this, android.R.layout.select_dialog_singlechoice);
				
				arrayAdapter.add("QQ");
				arrayAdapter.add("weibo");
				arrayAdapter.add("wechat");
				
				AlertDialog.Builder builderSingle = new AlertDialog.Builder(PlaceDetailActivity.this);
				builderSingle.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
					}
				});
				
				builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener()
				{
					@SuppressLint("DefaultLocale")
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						if (arrayAdapter.getItem(which).equals("wechat"))
						{
							boolean found = false;
							Intent share = new Intent(android.content.Intent.ACTION_SEND);
							share.setType("text/plain");
							
							// gets the list of intents that can be loaded.
							List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(share, 0);
							if (!resInfo.isEmpty())
							{
								for (ResolveInfo info : resInfo)
								{
									if (info.activityInfo.packageName.toLowerCase().contains("com.tencent.mm"))
									{
										share.putExtra(Intent.EXTRA_SUBJECT, "subject");
										share.putExtra(Intent.EXTRA_TEXT, "your text");
										share.setPackage(info.activityInfo.packageName);
										found = true;
										break;
									}
								}
								if (!found)
									return;
								
								startActivity(Intent.createChooser(share, "Select"));
							}
						}
					}
				});
				builderSingle.show();
				
			}
			else if (v.getId() == R.id.btn_like)
			{
				//좋아요 
			}
			else if (v.getId() == R.id.btn_write_review)
			{
				//리뷰쓰기
				Intent i = new Intent(PlaceDetailActivity.this, WriteReviewActivity.class);
				if (bean != null)
					i.putExtra("idx", bean.idx);
				i.putExtra("type", "place");
				startActivity(i);
			}
		}
	};
}
