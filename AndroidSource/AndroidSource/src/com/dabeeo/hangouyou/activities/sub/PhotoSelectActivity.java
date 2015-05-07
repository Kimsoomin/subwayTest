package com.dabeeo.hangouyou.activities.sub;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Spinner;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.controllers.SpinnerAdapter;
import com.dabeeo.hangouyou.controllers.mypage.LocalPhotoAdapter;

public class PhotoSelectActivity extends Activity
{
  private LocalPhotoAdapter photoAdapter;
  private Spinner directorySpinner;
  private String takedPhotoPath;
  private boolean isCallFromCustomView = false;
  private String callbackKeyCallForCustomView;
  private ArrayList<String> allImages = new ArrayList<>();
  private ArrayList<String> fullDirectoriesPath = new ArrayList<>();
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_local_photo);
    
    directorySpinner = (Spinner) findViewById(R.id.spinner_directory);
    findViewById(R.id.btn_cancel).setOnClickListener(clickListener);
    findViewById(R.id.btn_ok).setOnClickListener(clickListener);
    
    isCallFromCustomView = getIntent().getBooleanExtra("is_call_from_custom_view", false);
    if (isCallFromCustomView)
      callbackKeyCallForCustomView = getIntent().getStringExtra("callback_key_for_custom_view");
    
    photoAdapter = new LocalPhotoAdapter(this, getIntent().getBooleanExtra("can_select_multiple", false));
    GridView grid = (GridView) findViewById(R.id.gridView);
    grid.setAdapter(photoAdapter);
    grid.setOnItemClickListener(itemClickListener);
    getPathOfAllImages();
  }
  
  
  private void getPathOfAllImages()
  {
    Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    String[] projection = { MediaColumns.DATA, MediaColumns.DISPLAY_NAME };
    
    Cursor cursor = getContentResolver().query(uri, projection, null, null, MediaColumns.DATE_ADDED + " desc");
    int columnIndex = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
    int columnDisplayname = cursor.getColumnIndexOrThrow(MediaColumns.DISPLAY_NAME);
    
    int lastIndex;
    while (cursor.moveToNext())
    {
      String absolutePathOfImage = cursor.getString(columnIndex);
      String nameOfFile = cursor.getString(columnDisplayname);
      lastIndex = absolutePathOfImage.lastIndexOf(nameOfFile);
      lastIndex = lastIndex >= 0 ? lastIndex : nameOfFile.length() - 1;
      
      if (!TextUtils.isEmpty(absolutePathOfImage))
      {
        allImages.add(absolutePathOfImage);
      }
    }
    
    loadDirectories();
  }
  
  
  private void loadDirectories()
  {
    ArrayList<String> directories = new ArrayList<>();
    directories.add("전체");
    fullDirectoriesPath.add("전체");
    
    for (String string : allImages)
    {
      String fullPath = new File(string).getParent();
      
      if (!fullDirectoriesPath.contains(fullPath))
        fullDirectoriesPath.add(fullPath);
      
      String pathname = new File(fullPath).getName();
      
      if (!directories.contains(pathname))
        directories.add(pathname);
    }
    
    SpinnerAdapter spinnerArrayAdapter = new SpinnerAdapter(PhotoSelectActivity.this, android.R.layout.simple_list_item_1, directories);
//    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    directorySpinner.setAdapter(spinnerArrayAdapter);
    directorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
    {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
      {
        photoAdapter.clear();
        
        loadPhotos(position);
        
        photoAdapter.notifyDataSetChanged();
      }
      
      
      @Override
      public void onNothingSelected(AdapterView<?> parent)
      {
        
      }
    });
  }
  
  
  private void loadPhotos(int position)
  {
    
    if (position == 0)
    {
      for (String string : allImages)
      {
        photoAdapter.add(string, -1);
      }
    }
    else
    {
      String path = fullDirectoriesPath.get(position);
      for (String string : allImages)
      {
        if (string.contains(path))
          photoAdapter.add(string, -1);
      }
    }
  }
  
  
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode != RESULT_OK)
      return;
    
    allImages.add(0, takedPhotoPath);
    photoAdapter.add(takedPhotoPath, 1);
    photoAdapter.notifyDataSetChanged();
    
    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(takedPhotoPath))));
  }
  
  
  @Override
  public void onConfigurationChanged(Configuration newConfig)
  {
    super.onConfigurationChanged(newConfig);
  }
  
  /**************************************************
   * listener
   ***************************************************/
  private OnClickListener clickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      if (v.getId() == R.id.btn_ok)
      {
        Intent intent = new Intent();
        intent.putExtra("photos", photoAdapter.selectedPhotos());
        if (isCallFromCustomView)
        {
          intent.setAction(callbackKeyCallForCustomView);
          sendBroadcast(intent);
        }
        else
          setResult(RESULT_OK, intent);
      }
      finish();
    }
  };
  
  private OnItemClickListener itemClickListener = new OnItemClickListener()
  {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
      if (position != 0)
        return;
      
      try
      {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/TourPlanB";
        if (directorySpinner.getSelectedItemPosition() > 0)
          path = fullDirectoriesPath.get(directorySpinner.getSelectedItemPosition()) + "/";
        
        File storageDir = new File(path);
        if (!storageDir.exists())
          storageDir.mkdirs();
        
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        takedPhotoPath = image.getAbsolutePath();
        
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
        startActivityForResult(intent, 1);
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  };
}
