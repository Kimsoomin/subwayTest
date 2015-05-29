package com.dabeeo.hangouyou.fragments.trends;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.utils.ImageDownloader;

public class TrendProductimageFragment extends Fragment
{
	private ImageView imageView;
	private String imageUrl;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		int resId = R.layout.fragment_trend_product_image;
		return inflater.inflate(resId, null);
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		imageView = (ImageView) getView().findViewById(R.id.imageview);
	}
	
	
	public void setImageUrl(String imageUrl)
	{
		this.imageUrl = imageUrl;
	}
	
	
	@Override
	public void onResume()
	{
		Log.w("WARN", "Load images");
		ImageDownloader.displayImage(getActivity(), imageUrl, imageView, null);
		super.onResume();
	}
	
}
