package com.dabeeo.hangouyou.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.widget.ImageView;

import com.dabeeo.hangouyou.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class ImageDownloader
{
  private static ImageLoader imageLoader;
  
  
  public static void cleanMemory()
  {
    try
    {
      imageLoader.clearDiscCache();
      imageLoader.clearMemoryCache();
    }
    catch (Exception e)
    {
      
    }
  }
  
  
  public static void displayImage(Context context, String url, ImageView imageView, ImageLoadingListener listener)
  {
    if (imageLoader == null)
      initImageLoader(context);
    
    DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(null)
                                                                   .bitmapConfig(Bitmap.Config.ARGB_8888)
                                                                   .showImageForEmptyUri(R.drawable.default_thumbnail_s)
                                                                   .showImageOnFail(R.drawable.default_thumbnail_s)
                                                                   .cacheInMemory(false)
                                                                   .cacheOnDisc(true)
                                                                   .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                                                                   .handler(new Handler())
                                                                   .build();
    imageLoader.displayImage(url, imageView, options, listener);
  }
  
  
  public static void displayProfileImage(Context context, String url, ImageView imageView)
  {
    if (imageLoader == null)
      initImageLoader(context);
    
    DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.default_profile_s)
                                                                   .bitmapConfig(Bitmap.Config.ARGB_8888)
                                                                   .showImageForEmptyUri(R.drawable.default_profile_s)
                                                                   .showImageOnFail(R.drawable.default_profile_s)
                                                                   .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                                                                   .cacheInMemory(false)
                                                                   .cacheOnDisc(true)
                                                                   .handler(new Handler())
                                                                   .displayer(new RoundedBitmapDisplayer(150))
                                                                   .build();
    imageLoader.displayImage(url, imageView, options);
  }
  
  
  private static void initImageLoader(Context context)
  {
    imageLoader = ImageLoader.getInstance();
    ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).memoryCache(new WeakMemoryCache()).imageDecoder(new BaseImageDecoder(false)).discCache(
        new UnlimitedDiscCache(context.getFilesDir())).discCacheFileNameGenerator(new HashCodeFileNameGenerator()).build();
    
    imageLoader.init(config);
  }
}
