package com.dabeeo.hangouyou.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

public class ImageUtil
{
  /**
   * 썸네일 path 가져오기
   * 
   * @param context
   * @param path
   * @return
   */
  public static String getThumnailPath(Context context, String path)
  {
    String result = null;
    long imageId = -1;
    
    try
    {
      String[] projection = new String[] { MediaStore.MediaColumns._ID };
      String selection = MediaStore.MediaColumns.DATA + "=?";
      String[] selectionArgs = new String[] { path };
      Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, null);
      if (cursor != null && cursor.moveToFirst())
      {
        imageId = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
      }
      cursor.close();
      
      cursor = MediaStore.Images.Thumbnails.queryMiniThumbnail(context.getContentResolver(), imageId, MediaStore.Images.Thumbnails.MINI_KIND, null);
      if (cursor != null && cursor.getCount() > 0)
      {
        cursor.moveToFirst();
        result = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA));
      }
      cursor.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
    return result;
  }
}
