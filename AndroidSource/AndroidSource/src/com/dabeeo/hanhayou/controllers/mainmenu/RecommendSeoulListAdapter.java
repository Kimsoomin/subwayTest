package com.dabeeo.hanhayou.controllers.mainmenu;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.beans.PremiumBean;
import com.dabeeo.hanhayou.utils.SystemUtil;
import com.squareup.picasso.Picasso;

public class RecommendSeoulListAdapter extends BaseAdapter
{
	private ArrayList<PremiumBean> beans = new ArrayList<>();
	private int lastPosition;
	private Context context;
	
	private int imageWidth = 0;
	private int imageHeight = 0;
	
	
	public RecommendSeoulListAdapter(Context context, int imageWidth, int imageHeight)
	{
		this.context = context;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		
		@SuppressWarnings("deprecation")
		float width = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
		float margin = (int) SystemUtil.convertDpToPixel(4f, (Activity) context);
		// two images, three margins of 10dips
		imageWidth = (int) ((width - (3 * margin)) / 2);
	}
	
	
	public void add(PremiumBean bean)
	{
		this.beans.add(bean);
		notifyDataSetChanged();
	}
	
	
	public void addAll(ArrayList<PremiumBean> beans)
	{
		this.beans.addAll(beans);
		notifyDataSetChanged();
	}
	
	
	public void clear()
	{
		this.beans.clear();
		notifyDataSetChanged();
	}
	
	
	@Override
	public int getCount()
	{
		return beans.size();
	}
	
	
	@Override
	public Object getItem(int position)
	{
		return beans.get(position);
	}
	
	
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	
	//1242, 698
	//1242, 544
	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		PremiumBean bean = (PremiumBean) beans.get(position);
		int resId = R.layout.list_item_recommend_seoul;
		View view = LayoutInflater.from(parent.getContext()).inflate(resId, null);
		
		ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
		TextView title = (TextView) view.findViewById(R.id.title);
		TextView subtitle = (TextView) view.findViewById(R.id.subtitle);
		LinearLayout likeLayout = (LinearLayout) view.findViewById(R.id.like_layout);
		TextView likeCount = (TextView) view.findViewById(R.id.like_count);
		
		int height = (int) (imageWidth * 0.56);
		if (bean.title == null)
		{
			//Product Recommend
			height = (int) (imageWidth * 0.43);
		}
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) imageWidth, height);
		imageView.setLayoutParams(params);
		
		if (bean.title != null)
		{
			SpannableStringBuilder style = new SpannableStringBuilder(bean.title);
			style.setSpan(new BackgroundColorSpan(Color.parseColor("#ffffff")), 0, bean.title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			title.setText(style);
			subtitle.setText(bean.placeTitle);
			likeCount.setText(Integer.toString(bean.likeCount));
		}
		else
		{
			title.setVisibility(View.INVISIBLE);
			subtitle.setVisibility(View.INVISIBLE);
			likeLayout.setVisibility(View.INVISIBLE);
		}
		
		if (bean.title == null)
			Picasso.with(context).load(bean.imageUrl).resize(imageWidth, height).centerCrop().into(imageView);
		else
			Picasso.with(parent.getContext()).load(bean.imageUrl).resize(imageWidth, imageHeight).centerCrop().into(imageView);
		Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
		view.startAnimation(animation);
		lastPosition = position;
		return view;
	}
}
