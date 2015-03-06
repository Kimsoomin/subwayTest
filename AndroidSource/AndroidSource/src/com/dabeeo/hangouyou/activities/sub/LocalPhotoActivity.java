package com.dabeeo.hangouyou.activities.sub;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.controllers.LocalPhotoAdapter;

public class LocalPhotoActivity extends Activity
{
  private LocalPhotoAdapter photoAdapter;
  private String rootPath;
  private Spinner directorySpinner;
  private String takedPhotoPath;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_local_photo);
    
    directorySpinner = (Spinner) findViewById(R.id.spinner_directory);
    findViewById(R.id.btn_cancel).setOnClickListener(clickListener);
    findViewById(R.id.btn_ok).setOnClickListener(clickListener);
    
    rootPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/";
    
    photoAdapter = new LocalPhotoAdapter(getApplicationContext(), getIntent().getBooleanExtra("can_select_multiple", false));
    GridView grid = (GridView) findViewById(R.id.gridView);
    grid.setAdapter(photoAdapter);
    grid.setOnItemClickListener(itemClickListener);
    loadDirectories();
  }
  
  
  private void loadDirectories()
  {
    // 숨겨지지 않은, 폴더만
    FileFilter filter = new FileFilter()
    {
      @Override
      public boolean accept(File file)
      {
        return file.isDirectory() && !file.toString().contains("/.");
      }
    };
    
    File folder = new File(rootPath);
    final ArrayList<String> directories = new ArrayList<>();
    directories.add("전체");
    for (File file : folder.listFiles(filter))
    {
      directories.add(file.getName());
    }
    
    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, directories);
    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    directorySpinner.setAdapter(spinnerArrayAdapter);
    directorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
    {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
      {
        photoAdapter.clear();
        
        if (position == 0)
          loadPhotos(rootPath);
        else
          loadPhotos(rootPath + directories.get(position));
        
        photoAdapter.notifyDataSetChanged();
      }
      
      
      @Override
      public void onNothingSelected(AdapterView<?> parent)
      {
        
      }
    });
  }
  
  
  private void loadPhotos(String path)
  {
    File folder = new File(path);
    if (!folder.exists() || path.startsWith("."))
      return;
    
    // except .thumbnail folder
    FilenameFilter filter = new FilenameFilter()
    {
      @Override
      public boolean accept(File dir, String filename)
      {
        return !filename.startsWith(".");
      }
    };
    
    for (File file : folder.listFiles(filter))
    {
      if (file.isDirectory())
        loadPhotos(path + file.getName());
      else
      {
        photoAdapter.add(file.toString(), -1);
      }
    }
  }
  
  
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode != RESULT_OK)
      return;
    
    photoAdapter.add(takedPhotoPath, 1);
    photoAdapter.notifyDataSetChanged();
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
        File storageDir = new File(rootPath + "TourPlanB");
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
