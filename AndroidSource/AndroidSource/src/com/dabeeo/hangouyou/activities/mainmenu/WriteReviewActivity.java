package com.dabeeo.hangouyou.activities.mainmenu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.sub.PhotoSelectActivity;

public class WriteReviewActivity extends ActionBarActivity
{
  private final static int REQUEST_CODE = 120;
  
  private Button btnBest, btnSoso, btnWorst;
  private ImageView imageView;
  private EditText editReview;
  private Button btnSave;
  
  private int rate = 5;
  private String reviewImageFilePath;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_write_review);
    
    btnBest = (Button) findViewById(R.id.btn_review_best);
    btnSoso = (Button) findViewById(R.id.btn_review_soso);
    btnWorst = (Button) findViewById(R.id.btn_review_worst);
    imageView = (ImageView) findViewById(R.id.imageview);
    editReview = (EditText) findViewById(R.id.edit_review);
    btnSave = (Button) findViewById(R.id.btn_save);
    
    btnBest.setOnClickListener(rateClickListener);
    btnSoso.setOnClickListener(rateClickListener);
    btnWorst.setOnClickListener(rateClickListener);
    
    imageView.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        Intent intent = new Intent(WriteReviewActivity.this, PhotoSelectActivity.class);
        intent.putExtra("can_select_multiple", false);
        startActivityForResult(intent, REQUEST_CODE);
      }
    });
    
    btnSave.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        if (!TextUtils.isEmpty(editReview.getText().toString()))
        {
          
        }
        else
          Toast.makeText(WriteReviewActivity.this, getString(R.string.msg_empty_value), Toast.LENGTH_LONG).show();
      }
    });
  }
  
  private OnClickListener rateClickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      if (v.getId() == btnBest.getId())
        rate = 5;
      else if (v.getId() == btnSoso.getId())
        rate = 3;
      else if (v.getId() == btnWorst.getId())
        rate = 1;
    }
  };
  
  
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode != Activity.RESULT_OK)
      return;
    Log.w("WARN", "ActivityResult");
    if (data != null && data.hasExtra("photos"))
    {
      String[] photos = data.getStringArrayExtra("photos");
      if (photos.length == 0)
        return;
      
      reviewImageFilePath = photos[0];
      try
      {
        BitmapFactory.Options options = new BitmapFactory.Options();
        InputStream is = null;
        is = new FileInputStream(new File(photos[0]));
        BitmapFactory.decodeStream(is, null, options);
        is.close();
        is = new FileInputStream(new File(photos[0]));
        options.inSampleSize = 2;
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
        imageView.setImageBitmap(bitmap);
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
}
