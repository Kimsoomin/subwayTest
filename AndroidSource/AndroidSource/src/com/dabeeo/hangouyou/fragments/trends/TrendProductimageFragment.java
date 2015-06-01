package com.dabeeo.hangouyou.fragments.trends;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.sub.ImagePopUpActivity;
import com.dabeeo.hangouyou.utils.ImageDownloader;

public class TrendProductimageFragment extends Fragment
{
  public ImageView imageView;
  private ArrayList<String> imageUrls = new ArrayList<String>();
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
  
  
  public void setImageUrl(ArrayList<String> imageUrls, String imageUrl)
  {
    this.imageUrl = imageUrl;
    this.imageUrls.addAll(imageUrls);
  }
  
  
  @Override
  public void onResume()
  {
    Log.w("WARN", "Load images");
    ImageDownloader.displayImage(getActivity(), imageUrl, imageView, null);
    imageView.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        Intent i = new Intent(getActivity(), ImagePopUpActivity.class);
        i.putExtra("imageUrls", imageUrls);
        i.putExtra("imageUrl", imageUrl);
        getActivity().startActivity(i);
      }
    });
    super.onResume();
  }
  
}
