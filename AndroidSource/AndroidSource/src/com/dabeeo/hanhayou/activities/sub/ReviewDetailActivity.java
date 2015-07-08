package com.dabeeo.hanhayou.activities.sub;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.beans.ReviewBean;
import com.dabeeo.hanhayou.managers.PreferenceManager;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.managers.network.NetworkResult;
import com.dabeeo.hanhayou.utils.ImageDownloader;
import com.dabeeo.hanhayou.views.DeclareReviewView;

public class ReviewDetailActivity extends ActionBarActivity
{
  private ImageView icon;
  private TextView name;
  private TextView time;
  private TextView content;
  private TextView reviewScore;
  private ImageView btnMore;
  
  private ListPopupWindow listPopupWindow;
  
  private String reviewIdx = "";
  private ApiClient apiClient;
  private ReviewBean bean;
  
  private ProgressBar progressBar;
  
  private GridView myGrid;
  private ArrayList<ImageView> imageArray;
  private ImageAdapter imageadapter;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    @SuppressLint("InflateParams")
    View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
    TextView title = (TextView) customActionBar.findViewById(R.id.title);
    title.setText(getString(R.string.term_review));
    getSupportActionBar().setCustomView(customActionBar);
    getSupportActionBar().setDisplayShowCustomEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    setContentView(R.layout.activity_review_detail);
    
    apiClient = new ApiClient(this);
    reviewIdx = getIntent().getStringExtra("review_idx");
    
    myGrid = (GridView) findViewById(R.id.MyGrid);
    imageArray = new ArrayList<ImageView>();
    
    progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    icon = (ImageView) findViewById(R.id.icon);
    name = (TextView) findViewById(R.id.name);
    time = (TextView) findViewById(R.id.time);
    content = (TextView) findViewById(R.id.content);
    reviewScore = (TextView) findViewById(R.id.text_review_score);
    btnMore = (ImageView) findViewById(R.id.btn_review_list_more);
    
    btnMore.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        Log.w("WARN", "Click!");
        listPopupWindow = new ListPopupWindow(ReviewDetailActivity.this);
        String[] listPopupArray = new String[1];
        
        if (TextUtils.isEmpty(PreferenceManager.getInstance(ReviewDetailActivity.this).getUserSeq()))
          listPopupArray[0] = getString(R.string.term_declare);
        else
        {
          if (PreferenceManager.getInstance(ReviewDetailActivity.this).getUserSeq().equals(bean.ownerUserSeq))
            listPopupArray[0] = getString(R.string.term_delete);
          else
            listPopupArray[0] = getString(R.string.term_declare);
        }
        
        final String[] finalListPopupArray = listPopupArray;
        listPopupWindow.setAdapter(new ArrayAdapter<String>(ReviewDetailActivity.this, android.R.layout.simple_list_item_1, listPopupArray));
        listPopupWindow.setAnchorView(btnMore);
        listPopupWindow.setWidth(300);
        listPopupWindow.setOnItemClickListener(new OnItemClickListener()
        {
          @Override
          public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
          {
            if (finalListPopupArray[position].equals(getString(R.string.term_delete)))
            {
              //삭제
              Builder builder = new AlertDialog.Builder(ReviewDetailActivity.this);
              builder.setTitle(ReviewDetailActivity.this.getString(R.string.app_name));
              builder.setMessage(ReviewDetailActivity.this.getString(R.string.msg_confirm_delete));
              builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
              {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                  new DeleteAsyncTask().execute(bean.idx);
                }
              });
              builder.setNegativeButton(R.string.term_cancel, null);
              builder.show();
            }
            else
            {
              //신고
              Builder builder = new AlertDialog.Builder(ReviewDetailActivity.this);
              builder.setTitle(ReviewDetailActivity.this.getString(R.string.term_declare_review));
              final DeclareReviewView declareView = new DeclareReviewView(ReviewDetailActivity.this);
              declareView.init();
              builder.setView(declareView);
              builder.setPositiveButton(R.string.term_ok, new DialogInterface.OnClickListener()
              {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                  new DeclareAsyncTask().execute(bean.idx, declareView.getReason());
                }
              });
              builder.setNegativeButton(android.R.string.cancel, null);
              builder.show();
            }
            listPopupWindow.dismiss();
          }
        });
        listPopupWindow.show();
      }
    });
    loadReviewDetail();
  }
  
  
  private void loadReviewDetail()
  {
    new LoadReviewDetailAsyncTask().execute();
  }
  
  private class LoadReviewDetailAsyncTask extends AsyncTask<String, Integer, ReviewBean>
  {
    @Override
    protected void onPreExecute()
    {
      progressBar.setVisibility(View.VISIBLE);
      super.onPreExecute();
    }
    
    
    @Override
    protected ReviewBean doInBackground(String... params)
    {
      return apiClient.getReviewDetail(reviewIdx);
    }
    
    
    @Override
    protected void onPostExecute(ReviewBean result)
    {
      bean = result;
      progressBar.setVisibility(View.GONE);
      displayContent();
      super.onPostExecute(result);
    }
  }
  
  
  private void displayContent()
  {
    if (bean != null)
    {
      name.setText(bean.userName);
      SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd hh:mm");
      if(bean.insertDate != null)
        time.setText(format.format(bean.insertDate));
      reviewScore.setText(Float.toString(bean.rate));
      content.setText(bean.content);
      
      for (int i = 0; i < bean.imageUrls.size(); i++)
      {
        ImageView imageView = new ImageView(ReviewDetailActivity.this);
        float density = getResources().getDisplayMetrics().density;
        int size = (int) (80*density);
        imageView.setLayoutParams(new GridView.LayoutParams(size, size));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(8, 8, 8, 8);
        final String imageUrl = bean.imageUrls.get(i);
        imageView.setOnClickListener(new OnClickListener()
        {
          @Override
          public void onClick(View arg0)
          {
            Intent i = new Intent(ReviewDetailActivity.this, ImagePopUpActivity.class);
            i.putExtra("imageUrls", bean.imageUrls);
            i.putExtra("imageUrl", imageUrl);
            startActivity(i);
          }
        });
        imageView.setImageResource(R.drawable.default_thumbnail_s);
        ImageDownloader.displayImage(ReviewDetailActivity.this, bean.imageUrls.get(i), imageView, null);
        imageArray.add(imageView);
      }
      
      imageadapter = new ImageAdapter(ReviewDetailActivity.this, imageArray);
      myGrid.setAdapter(imageadapter);
    }
    
    ImageDownloader.displayProfileImage(ReviewDetailActivity.this, bean.mfidx, icon);
  }
  
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    getMenuInflater().inflate(R.menu.menu_empty, menu);
    return super.onCreateOptionsMenu(menu);
  }
  
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    int id = item.getItemId();
    if (id == android.R.id.home)
      finish();
    return super.onOptionsItemSelected(item);
  }
  
  /**************************************************
   * async task
   ***************************************************/
  private class DeleteAsyncTask extends AsyncTask<String, Integer, Boolean>
  {
    public String reviewIdx;
    
    
    @Override
    protected void onPreExecute()
    {
      super.onPreExecute();
    }
    
    
    @Override
    protected Boolean doInBackground(String... params)
    {
      reviewIdx = params[0];
      return apiClient.removeReview(params[0]);
    }
    
    
    @Override
    protected void onPostExecute(Boolean result)
    {
      if (result)
        finish();
      super.onPostExecute(result);
    }
  }
  
  private class DeclareAsyncTask extends AsyncTask<String, Integer, NetworkResult>
  {
    
    @Override
    protected void onPreExecute()
    {
      super.onPreExecute();
    }
    
    
    @Override
    protected NetworkResult doInBackground(String... params)
    {
      return apiClient.declareReview(params[0], params[1]);
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      try
      {
        JSONObject object = new JSONObject(result.response);
        if (object.has("status") && object.getString("status").equals("OK"))
          Toast.makeText(ReviewDetailActivity.this, getString(R.string.msg_declare_compelete), Toast.LENGTH_SHORT).show();
        else
          Toast.makeText(ReviewDetailActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      super.onPostExecute(result);
    }
  }
  
  class ImageAdapter extends BaseAdapter{
    private Context mContext;
    ArrayList<ImageView> images;
    
    public ImageAdapter(Context mContext, ArrayList<ImageView> images) {
      this.mContext = mContext;
      this.images = images;
    } 
    
    public int getCount() {
      return images.size();
    }
    
    public Object getItem(int position) {
      return images.get(position);
    }
    
    public long getItemId(int position) {
      return 0;
    }
    
    // 뷰에있는 데이터를 하나씩 가져와서 출력
    public View getView(int position, View convertView, ViewGroup parent) {
      ImageView imageview;
      if(convertView == null){
        imageview = new ImageView(mContext);
      }else{
        imageview = (ImageView)convertView;
      }
      imageview = images.get(position);
      return imageview;
    }      
  }
}
