package com.dabeeo.hangouyou.activities.mainmenu;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.RecommendSeoulBean;
import com.dabeeo.hangouyou.views.SquareImageView;

public class RecommendSeoulDetailActivity extends ActionBarActivity
{
  private TextView textDetail;
  private ViewGroup horizontalImagesView, layoutDetailPlaceInfo;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recommend_seoul_detail);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    textDetail = (TextView) findViewById(R.id.text_detail);
    horizontalImagesView = (ViewGroup) findViewById(R.id.horizontal_images_view);
    layoutDetailPlaceInfo = (ViewGroup) findViewById(R.id.layout_place_detail_info);
    
    findViewById(R.id.btn_place_detail_info).setOnClickListener(clickListener);
    findViewById(R.id.btn_bookmark).setOnClickListener(clickListener);
    findViewById(R.id.btn_share).setOnClickListener(clickListener);
    findViewById(R.id.btn_like).setOnClickListener(clickListener);
    
    displayContentData();
  }
  
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    getMenuInflater().inflate(R.menu.menu_my_place_detail, menu);
    return true;
  }
  
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    int id = item.getItemId();
    if (id == android.R.id.home)
      finish();
    else if (id == R.id.map)
      Toast.makeText(this, "준비 중입니다", Toast.LENGTH_LONG).show();
    return super.onOptionsItemSelected(item);
  }
  
  
  private void displayContentData()
  {
    RecommendSeoulBean bean = new RecommendSeoulBean();
    bean.title = "서울 여행자";
    bean.detail = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
    bean.address = "서울 강남구 신사동 534-27";
    bean.phone = "02-1111-2222";
    bean.homepage = "http://google.com";
    bean.workingTime = "오전 9시 ~ 오후 6시";
    bean.price = "1000원 부터";
    bean.traffic = "신사역 8번 출구";
    bean.description = "역시 마찬가지로, 단순히 고통이라는 이유 때문에 고통 그 자체를 사랑하거나 추구하거나 소유하려는 자는 없다. 다만 노역과 고통이 아주 큰 즐거움을 선사하는 상황이 때로는 발생하기 때문에 고통을 찾는 사람이 있는 것이다. 간단한 예를 들자면, 모종의 이익을 얻을 수도 없는데 힘든 육체적 노력을 기꺼이 할 사람이 우리들 중에 과연 있겠는가? 하지만 귀찮은 일이 뒤따르지 않는 즐거움을 누리는 것을 선택한 사람, 혹은 아무런 즐거움도 생기지 않는 고통을 회피하는 사람을 누가 탓할 수 있겠는가?";
    
    setTitle(bean.title);
    textDetail.setText(bean.detail);
    
    int resId = R.layout.list_item_recommend_seoul_photo;
    for (int i = 0; i < 10; i++)
    {
      SquareImageView view = (SquareImageView) getLayoutInflater().inflate(resId, null);
      view.setImageResource(R.drawable.ic_launcher);
      horizontalImagesView.addView(view);
    }
    
    addDetailInfo(getString(R.string.address), bean.address);
    addDetailInfo(getString(R.string.phone), bean.phone);
    addDetailInfo(getString(R.string.homepage), bean.homepage);
    addDetailInfo(getString(R.string.working_time), bean.workingTime);
    addDetailInfo(getString(R.string.price_info), bean.price);
    addDetailInfo(getString(R.string.traffic), bean.traffic);
    addDetailInfo(getString(R.string.description), bean.description);
  }
  
  
  private void addDetailInfo(String title, String text)
  {
    if (!TextUtils.isEmpty(text))
    {
      int detailResId = R.layout.list_item_recommend_seoul_place_detail_info;
      View view = getLayoutInflater().inflate(detailResId, null);
      TextView titleView = (TextView) view.findViewById(android.R.id.text1);
      TextView textView = (TextView) view.findViewById(android.R.id.text2);
      titleView.setText(title);
      titleView.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
      textView.setText(text);
      layoutDetailPlaceInfo.addView(view);
    }
  }
  
  /**************************************************
   * listener
   ***************************************************/
  private OnClickListener clickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      if (v.getId() == R.id.btn_place_detail_info)
      {
        layoutDetailPlaceInfo.setVisibility(layoutDetailPlaceInfo.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
      }
      else if (v.getId() == R.id.btn_bookmark)
      {
        // 북마크 토글
      }
      else if (v.getId() == R.id.btn_share)
      {
        // 공유하기
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "공유테스트");
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, null));
      }
      else if (v.getId() == R.id.btn_like)
      {
        //좋아요 
      }
    }
  };
}
