package com.dabeeo.hanhayou.views.photolog;

import java.io.File;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.sub.PhotoSelectActivity;

public class NewPhotoLogView extends LinearLayout
{
  private Context context;
  private ImageView photo;
  private ImageButton btnLocalPhoto;
  
  
  public NewPhotoLogView(Context context, AttributeSet attrs, int defStyleAttr)
  {
    super(context, attrs, defStyleAttr);
    init(context);
  }
  
  
  public NewPhotoLogView(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    init(context);
  }
  
  
  public NewPhotoLogView(Context context)
  {
    super(context);
    init(context);
  }
  
  
  private void init(Context context)
  {
    this.context = context;
    
    int resId = R.layout.view_new_photo_log;
    View view = LayoutInflater.from(context).inflate(resId, null);
    
    photo = (ImageView) view.findViewById(R.id.photo);
    photo.setOnClickListener(clickListener);
    btnLocalPhoto = (ImageButton) view.findViewById(R.id.btn_local_photo);
    btnLocalPhoto.setOnClickListener(clickListener);
    
    addView(view);
  }
  
  private OnClickListener clickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      IntentFilter filter = new IntentFilter(this.toString());
      context.registerReceiver(receiver, filter);
      
      Intent intent = new Intent(context, PhotoSelectActivity.class);
      intent.putExtra("is_call_from_custom_view", true);
      intent.putExtra("callback_key_for_custom_view", this.toString());
      
      context.startActivity(intent);
    }
  };
  
  private BroadcastReceiver receiver = new BroadcastReceiver()
  {
    @Override
    public void onReceive(Context context, Intent intent)
    {
      context.unregisterReceiver(receiver);
      
      if (intent.hasExtra("photos"))
      {
        String[] photos = intent.getStringArrayExtra("photos");
        if (photos.length == 0)
          return;
        
        photo.setImageURI(Uri.fromFile(new File(photos[0])));
        btnLocalPhoto.setVisibility(View.GONE);
      }
    }
  };
}
