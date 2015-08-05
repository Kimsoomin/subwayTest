package com.dabeeo.hanhayou.views;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.controllers.mainmenu.WishListAdapter.WishListListener;
import com.dabeeo.hanhayou.managers.PreferenceManager;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.managers.network.NetworkResult;

public class PopularWishListParticleView extends RelativeLayout
{
  
  private Context context;
  private String firstBean;
  private String secondBean;
  private String firstId, secondId;
  private int position;
  
  private ApiClient apiClient;
  private int imageWidth = 0;
  private WishListListener listener;
  
  public PopularWishListParticleView(Context context, WindowManager wm, WishListListener listener)
  {
    super(context);
    this.context = context;
    DisplayMetrics metrics = new DisplayMetrics();
    wm.getDefaultDisplay().getMetrics(metrics);
    imageWidth = metrics.widthPixels/2;
    apiClient = new ApiClient(context);
    this.listener = listener;
  }
  
  
  public void setBean(int position, String firstBean, String secondBean, String firstId, String secondId)
  {
    this.position = position;
    this.firstBean = firstBean;
    this.secondBean = secondBean;
    this.firstId = firstId;
    this.secondId = secondId;
    init();
  }
  
  
  public void init()
  {
    int resId = R.layout.view_popular_wishlist_view;
    View view = LayoutInflater.from(context).inflate(resId, null);
    
    TextView leftText = (TextView) view.findViewById(R.id.text_left);
    leftText.setMaxWidth(imageWidth);
    TextView rightText = (TextView) view.findViewById(R.id.text_right);
    
    leftText.setText(firstBean);
    
    if(!TextUtils.isEmpty(secondBean) && !TextUtils.isEmpty(secondId))
    {
      rightText.setText(secondBean);
      rightText.setId(Integer.parseInt(secondId));
    }
    else
    {
      rightText.setVisibility(View.GONE);
    }
       
    if (position % 2 == 0)
    {
      leftText.setBackgroundResource(R.drawable.yellow_rectangle);
      rightText.setBackgroundResource(R.drawable.green_rectangle);
    }
    else
    {
      leftText.setBackgroundResource(R.drawable.blue_rectangle);
      rightText.setBackgroundResource(R.drawable.red_rectangle);
    }
    
    leftText.setOnClickListener(new OnClickListener()
    {
      
      @Override
      public void onClick(View v)
      {
        new ToggleWishList().execute(firstId);
      }
    });
    
    rightText.setOnClickListener(new OnClickListener()
    {
      
      @Override
      public void onClick(View v)
      {
        new ToggleWishList().execute(secondId);
      }
    });
    
    addView(view);
  }
  
  private class ToggleWishList extends AsyncTask<String, Void, NetworkResult>
  {
    @Override
    protected NetworkResult doInBackground(String... params)
    {
      return apiClient.setUsedLog(PreferenceManager.getInstance(context).getUserSeq(), params[0], "product", "W");
    }
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      super.onPostExecute(result);
      try
      {
        JSONObject obj = new JSONObject(result.response);
        if(obj.getString("result").equals("INS"))
        {
          Toast.makeText(context, context.getString(R.string.msg_add_wishlist), Toast.LENGTH_SHORT).show();
          if(listener != null)
            listener.onRefresh();
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }
}
