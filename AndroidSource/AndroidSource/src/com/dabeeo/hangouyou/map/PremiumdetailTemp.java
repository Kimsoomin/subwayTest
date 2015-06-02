package com.dabeeo.hangouyou.map;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.squareup.picasso.Picasso;

public class PremiumdetailTemp extends ActionBarActivity {
	
	private LinearLayout contentContainer;
	
	private ProgressBar progressBar;
	
	private int placeIdx = -1;
	private ScrollView scrollView;
	private TextView likeCount;
	
	List<PremiumInfo> premiumInfo;
	public ArrayList<PremiumData> premiumData = new ArrayList<PremiumData>();
	
	Context mContext;
	String ImageUrl;
	String ImageName;
	Map<String, ImageView> premiumImage = new HashMap<String, ImageView>();
	Map<String, String> imageUrl = new HashMap<String, String>();
	ActionBar actionBar;
	
	int imageHeight = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		getSupportActionBar().setElevation(0);
		
		setContentView(R.layout.main_mapview2);
		mContext = this;
		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);
//		actionBar.setHomeButtonEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		
		placeIdx = getIntent().getIntExtra("place_idx", -1);
		progressBar = (ProgressBar) findViewById(R.id.progress_bar_temp);
		contentContainer = (LinearLayout) findViewById(R.id.container_details_temp);
		likeCount = (TextView) findViewById(R.id.like_count_temp);
		
		findViewById(R.id.btn_share_temp).setOnClickListener(clickListener);
		findViewById(R.id.btn_like_temp).setOnClickListener(clickListener);
		
		scrollView = (ScrollView) findViewById(R.id.scroll_view_temp);
		
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.argb(0, 247, 177, 89)));
		scrollView.getViewTreeObserver().addOnScrollChangedListener(new OnScrollChangedListener()
		{
			@Override
			public void onScrollChanged()
			{
				int scrollY = scrollView.getScrollY();
				
				if (scrollY > 255)
				{
					scrollY = 254;
				}
				if(scrollY <= 10)
				{
					actionBar.setDisplayShowTitleEnabled(false);
					actionBar.setBackgroundDrawable(new ColorDrawable(Color.argb(0, 247, 177, 89)));
				}
				else
				{
					int opacity = scrollY - 255;
					actionBar.setBackgroundDrawable(new ColorDrawable(Color.argb(opacity, 247, 177, 89)));
					actionBar.setDisplayShowTitleEnabled(true);
				}
			}
		});
		premiumInfo = MapPlaceDataManager.getInstance(this).getPremiumfromIdx(""+placeIdx);
		loadPlaceDetail();
		imageHeight = (int) (scrollView.getWidth()*0.66);
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
			Intent i = new Intent(PremiumdetailTemp.this, BlinkingMap.class);
			i.putExtra("idx", premiumInfo.get(0).m_nID);
			startActivity(i);
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
		new PlaceDetailAsyncTask().execute();
	}
	
	private class PlaceDetailAsyncTask extends AsyncTask<String, Integer, Bitmap>
	{
		
		@Override
		protected Bitmap doInBackground(String... params)
		{
			
			return null;
		}
		
		
		@Override
		protected void onPostExecute(Bitmap result)
		{
			 try {				 
				 JSONArray arr = new JSONArray(premiumInfo.get(0).m_strIntro);
				 for(int i = 0; arr.length()>i; i++)
				 {
					 JSONObject contentObj = arr.getJSONObject(i);
					 PremiumData bean = new PremiumData();
					 bean.setJSONObject(contentObj);
					 premiumData.add(bean);
				 }
			 } catch (JSONException e1) {
				 // TODO Auto-generated catch block
				 e1.printStackTrace();
			 }
				displayContentData();
//			}
			progressBar.setVisibility(View.GONE);
			super.onPostExecute(result);
		}
	}
	
	private class premiumImageDownloadAsyncTask extends AsyncTask<String, Integer, Boolean>
	{	
		public String testurl;
		public String testname;
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			testurl = params[0];
			testname = params[1];
			try {
				Global.DownloadImage(testurl , testname);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			File file = new File(Global.GetPathWithSDCard()+".temp/"+testname);
			if(file.exists())
			{
				Picasso.with(mContext).load(file).fit().centerCrop().into(premiumImage.get(testname));
			}
		}
	}
	
	
	private void displayContentData()
	{
		if (premiumData == null)
			return;
		
		setTitle(premiumInfo.get(0).m_strName);
		((TextView) findViewById(R.id.text_title_temp)).setText(premiumInfo.get(0).m_strName);
		((TextView) findViewById(R.id.text_description_temp)).setText(premiumInfo.get(0).m_strAddress);
		
		//Contents
		for (int i = 0; i < premiumData.size(); i++)
		{
			int detailResId = R.layout.view_premium_seoul_content;
			View view = getLayoutInflater().inflate(detailResId, null);
			TextView text = (TextView) view.findViewById(R.id.text_content);
			ImageView image = (ImageView) view.findViewById(R.id.image_content);
			image.setMaxHeight(imageHeight);
			if (premiumData.get(i).isText)
			{
				text.setText(premiumData.get(i).text);
				text.setVisibility(View.VISIBLE);
			}
			else
			{
				ImageUrl = premiumData.get(i).imageUrl;
				ImageName = Global.MD5Encoding(ImageUrl);
				try {
					if(i != 0)
					{
						Display display = getWindowManager().getDefaultDisplay();
						int width = display.getWidth();
						File file = new File(Global.GetPathWithSDCard()+".temp/"+ImageName);
						if(file.exists())
							Picasso.with(this).load(file).fit().centerCrop().into(image);
						else
						{
							Picasso.with(this).load(R.drawable.default_recommend_detail).fit().centerCrop().into(image);
							if(Global.IsNetworkConnected(mContext, false))
								new premiumImageDownloadAsyncTask().execute(ImageUrl,ImageName);
						}
						premiumImage.put(ImageName, image);
						image.setVisibility(View.VISIBLE);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
			contentContainer.addView(view);
		}
		
		likeCount.setText(""+premiumInfo.get(0).m_nLikeCount);
		
		//Main Image
		File mainImage = new File(Global.GetPathWithSDCard()+".temp/"+Global.MD5Encoding(premiumInfo.get(0).m_strImageFilePath));
		if(mainImage.exists())
			Picasso.with(this).load(mainImage).resize(300, 300).centerCrop().into((ImageView) findViewById(R.id.imageview_temp));
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
				Intent sendIntent = new Intent(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_TEXT, "공유테스트");
				sendIntent.setType("text/plain");
				startActivity(Intent.createChooser(sendIntent, null));
			}
			else if (v.getId() == R.id.btn_like)
			{
				//좋아요 
			}
		}
	};

}
