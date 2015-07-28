package com.dabeeo.hanhayou.views;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dabeeo.hanhayou.R;
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
  
  public PopularWishListParticleView(Context context)
  {
    super(context);
    this.context = context;
    apiClient = new ApiClient(context);
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
    TextView rightText = (TextView) view.findViewById(R.id.text_right);
    
    leftText.setText(firstBean);
    leftText.setId(Integer.parseInt(firstId));
    if(!TextUtils.isEmpty(secondBean) && !TextUtils.isEmpty(secondId) && !TextUtils.equals(secondId, "null"))
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
    
    view.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        String id = "" + v.getId();
        new ToggleWishList().execute(id);
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
        }else
        {
          Toast.makeText(context, context.getString(R.string.msg_remove_wishlist), Toast.LENGTH_SHORT).show();
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }
}
