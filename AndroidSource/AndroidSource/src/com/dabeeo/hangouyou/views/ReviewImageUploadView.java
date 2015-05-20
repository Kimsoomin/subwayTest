package com.dabeeo.hangouyou.views;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dabeeo.hangouyou.R;

public class ReviewImageUploadView extends RelativeLayout
{
  private Context context;
  
  public ImageView image;
  public ImageView imageX;
  public RelativeLayout container;
  public String fileUrl;
  public ImageView addImage;
  
  private RelativeLayout imageContainer, addImageContainer;
  
  
  public ReviewImageUploadView(Context context)
  {
    super(context);
    this.context = context;
    init();
  }
  
  
  public ReviewImageUploadView(Context context, AttributeSet attrs, int defStyle)
  {
    super(context, attrs, defStyle);
    this.context = context;
    init();
  }
  
  
  public ReviewImageUploadView(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    this.context = context;
    init();
  }
  
  
  public void setData(String fileUrl)
  {
    if (TextUtils.isEmpty(fileUrl))
    {
      imageContainer.setVisibility(View.GONE);
      addImageContainer.setVisibility(View.VISIBLE);
    }
    else
    {
      imageContainer.setVisibility(View.VISIBLE);
      addImageContainer.setVisibility(View.GONE);
      this.fileUrl = fileUrl;
      
      BitmapFactory.Options options = new BitmapFactory.Options();
      InputStream is = null;
      try
      {
        is = new FileInputStream(new File(fileUrl));
        BitmapFactory.decodeStream(is, null, options);
        is.close();
        is = new FileInputStream(new File(fileUrl));
        options.inSampleSize = 2;
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
        
        image.setImageResource(R.drawable.default_thumbnail_s);
        image.setImageBitmap(bitmap);
      }
      catch (FileNotFoundException e)
      {
        e.printStackTrace();
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
  }
  
  
  public void init()
  {
    int resId = R.layout.view_review_image_upload;
    View view = LayoutInflater.from(context).inflate(resId, null);
    view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    
    imageContainer = (RelativeLayout) view.findViewById(R.id.image_container);
    addImageContainer = (RelativeLayout) view.findViewById(R.id.add_container);
    container = (RelativeLayout) view.findViewById(R.id.container);
    
    addImage = (ImageView) view.findViewById(R.id.add_pic);
    image = (ImageView) view.findViewById(R.id.image);
    imageX = (ImageView) view.findViewById(R.id.img_x);
    
    addView(view);
  }
}
