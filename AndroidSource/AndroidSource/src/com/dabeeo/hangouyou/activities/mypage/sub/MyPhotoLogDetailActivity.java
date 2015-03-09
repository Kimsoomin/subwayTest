package com.dabeeo.hangouyou.activities.mypage.sub;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.views.PhotoLogDaySeperater;
import com.dabeeo.hangouyou.views.PhotoLogDayView;

public class MyPhotoLogDetailActivity extends ActionBarActivity
{
  private ViewGroup container;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my_photo_log_detail);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    container = (ViewGroup) findViewById(R.id.container);
    
    displayContents();
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
      Log.i("MyPhotoLogDetailActivity.java | onOptionsItemSelected", "|" + "map" + "|");
    return super.onOptionsItemSelected(item);
  }
  
  
  private void displayContents()
  {
    PhotoLogDaySeperater seperater = new PhotoLogDaySeperater(this, 1);
    container.addView(seperater);
    
    PhotoLogDayView day = new PhotoLogDayView(
        this,
        "경복궁1",
        "경복궁1(景福宮)은 대한민국 서울 세종로에 있는 조선 왕조의 법궁(法宮, 정궁)이다.1395년(관종 4년)에 창건하였다. ‘새해복(景福)’은 성시경에 나오는 말로 왕과 그 자손, 온 백성들이 태평성대의 큰 복을 누리기를 축원한다는 의미이다. 풍수지리적으로도 백악산을 뒤로하고 좌우에는 낙산과 인왕산으로 둘러싸여 있어 길지의 요건을 갖추고 있다. 1592년, 임진왜란으로 인해 불탄 이후 그 임무를 창덕궁에 넘겨주었다가 1865년(고종 2년)에 흥선대원군의 명으로 중건되었다. 일제강점기에는 조선 총독부 건물을 짓는 등 많은 전각들이 훼손되었으나, 1990년대부터 총독부 건물을 철거하는 등 복원사업을 벌인 덕분에 복원 작업은 현재 부분 완료된 상태다.[1] 근정전, 경회루, 향원정, 아미산 굴뚝 등은 훼손되지 않고 그대로 남아 있다.[2] 면적은 432,703㎡이며, 동서 500m, 남북 700m 규모로 남아 있다.");
    container.addView(day);
    
    seperater = new PhotoLogDaySeperater(this, 2);
    container.addView(seperater);
    day = new PhotoLogDayView(
        this,
        "경복궁2",
        "경복궁2(景福宮)은 대한민국 서울 세종로에 있는 조선 왕조의 법궁(法宮, 정궁)이다.1395년(관종 4년)에 창건하였다. ‘새해복(景福)’은 성시경에 나오는 말로 왕과 그 자손, 온 백성들이 태평성대의 큰 복을 누리기를 축원한다는 의미이다. 풍수지리적으로도 백악산을 뒤로하고 좌우에는 낙산과 인왕산으로 둘러싸여 있어 길지의 요건을 갖추고 있다. 1592년, 임진왜란으로 인해 불탄 이후 그 임무를 창덕궁에 넘겨주었다가 1865년(고종 2년)에 흥선대원군의 명으로 중건되었다. 일제강점기에는 조선 총독부 건물을 짓는 등 많은 전각들이 훼손되었으나, 1990년대부터 총독부 건물을 철거하는 등 복원사업을 벌인 덕분에 복원 작업은 현재 부분 완료된 상태다.[1] 근정전, 경회루, 향원정, 아미산 굴뚝 등은 훼손되지 않고 그대로 남아 있다.[2] 면적은 432,703㎡이며, 동서 500m, 남북 700m 규모로 남아 있다.");
    container.addView(day);
    
    seperater = new PhotoLogDaySeperater(this, 3);
    container.addView(seperater);
    day = new PhotoLogDayView(
        this,
        "경복궁3",
        "경복궁3(景福宮)은 대한민국 서울 세종로에 있는 조선 왕조의 법궁(法宮, 정궁)이다.1395년(관종 4년)에 창건하였다. ‘새해복(景福)’은 성시경에 나오는 말로 왕과 그 자손, 온 백성들이 태평성대의 큰 복을 누리기를 축원한다는 의미이다. 풍수지리적으로도 백악산을 뒤로하고 좌우에는 낙산과 인왕산으로 둘러싸여 있어 길지의 요건을 갖추고 있다. 1592년, 임진왜란으로 인해 불탄 이후 그 임무를 창덕궁에 넘겨주었다가 1865년(고종 2년)에 흥선대원군의 명으로 중건되었다. 일제강점기에는 조선 총독부 건물을 짓는 등 많은 전각들이 훼손되었으나, 1990년대부터 총독부 건물을 철거하는 등 복원사업을 벌인 덕분에 복원 작업은 현재 부분 완료된 상태다.[1] 근정전, 경회루, 향원정, 아미산 굴뚝 등은 훼손되지 않고 그대로 남아 있다.[2] 면적은 432,703㎡이며, 동서 500m, 남북 700m 규모로 남아 있다.");
    container.addView(day);
  }
}
