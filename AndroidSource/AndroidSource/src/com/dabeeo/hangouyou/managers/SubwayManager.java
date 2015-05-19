package com.dabeeo.hangouyou.managers;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.dabeeo.hangouyou.beans.StationBean;

public class SubwayManager
{
  private static SubwayManager instance = null;
  private Context context;
  public ArrayList<StationBean> stations = new ArrayList<StationBean>();
  
  
  public static SubwayManager getInstance(Context context)
  {
    if (instance == null)
    {
      synchronized (SubwayManager.class)
      {
        if (instance == null)
          instance = new SubwayManager(context);
      }
    }
    return instance;
  }
  
  
  private SubwayManager(Context context)
  {
    this.context = context;
  }
  
  
  public double getLatitudeWithSubwayId(String stationId)
  {
    StationBean bean = null;
    for (int i = 0; i < stations.size(); i++)
    {
      if (stations.get(i).stationId.equals(stationId))
        bean = stations.get(i);
    }
    
    if (bean == null)
      return -1;
    else
      return bean.lat;
  }
  
  
  public double getLongitudeWithSubwayId(String stationId)
  {
    StationBean bean = null;
    for (int i = 0; i < stations.size(); i++)
    {
      if (stations.get(i).stationId.equals(stationId))
        bean = stations.get(i);
    }
    
    if (bean == null)
      return -1;
    else
      return bean.lon;
  }
  
  
  public StationBean findStation(String stationId)
  {
    StationBean bean = null;
    for (int i = 0; i < stations.size(); i++)
    {
      if (stations.get(i).stationId.equals(stationId))
        bean = stations.get(i);
    }
    
    return bean;
  }
  
  
  //검색 시 사용 
  public ArrayList<StationBean> getSubwayStationsWithTitle(String title)
  {
    ArrayList<StationBean> tempStations = new ArrayList<StationBean>();
    //이름이 포함된 역을 모두 가져옴
    for (int i = 0; i < stations.size(); i++)
    {
      if (stations.get(i).nameKo.contains(title) || stations.get(i).nameCn.contains(title))
      {
        boolean isConatin = false;
        for (int j = 0; j < tempStations.size(); j++)
        {
          if (tempStations.get(j).nameKo.equals(stations.get(i).nameKo))
            isConatin = true;
        }
        
        if (!isConatin)
        {
          tempStations.add(stations.get(i));
        }
      }
    }
    
    for (int i = 0; i < tempStations.size(); i++)
    {
      if (tempStations.get(i).nameKo.equals("대곡"))
      {
        if (!tempStations.get(i).lines.contains("경의중앙선"))
          tempStations.get(i).lines.add("경의중앙선");
      }
      else if (tempStations.get(i).nameKo.equals("연신내"))
      {
        if (!tempStations.get(i).lines.contains("6"))
          tempStations.get(i).lines.add("6");
      }
      else if (tempStations.get(i).nameKo.equals("불광"))
      {
        if (!tempStations.get(i).lines.contains("6"))
          tempStations.get(i).lines.add("6");
      }
      else if (tempStations.get(i).nameKo.equals("도봉산"))
      {
        if (!tempStations.get(i).lines.contains("7"))
          tempStations.get(i).lines.add("7");
      }
      else if (tempStations.get(i).nameKo.equals("창동"))
      {
        if (!tempStations.get(i).lines.contains("4"))
          tempStations.get(i).lines.add("4");
      }
      else if (tempStations.get(i).nameKo.equals("노원"))
      {
        if (!tempStations.get(i).lines.contains("7"))
          tempStations.get(i).lines.add("7");
      }
      else if (tempStations.get(i).nameKo.equals("석계"))
      {
        if (!tempStations.get(i).lines.contains("6"))
          tempStations.get(i).lines.add("6");
      }
      else if (tempStations.get(i).nameKo.equals("태릉입구"))
      {
        if (!tempStations.get(i).lines.contains("7"))
          tempStations.get(i).lines.add("7");
      }
      else if (tempStations.get(i).nameKo.equals("디지털미디어시티"))
      {
        if (!tempStations.get(i).lines.contains("인천1호선"))
          tempStations.get(i).lines.add("인천1호선");
      }
      else if (tempStations.get(i).nameKo.equals("종로3가"))
      {
        if (!tempStations.get(i).lines.contains("3"))
          tempStations.get(i).lines.add("3");
        if (!tempStations.get(i).lines.contains("5"))
          tempStations.get(i).lines.add("5");
      }
      else if (tempStations.get(i).nameKo.equals("동대문"))
      {
        if (!tempStations.get(i).lines.contains("4"))
          tempStations.get(i).lines.add("4");
      }
      else if (tempStations.get(i).nameKo.equals("동묘앞"))
      {
        if (!tempStations.get(i).lines.contains("6"))
          tempStations.get(i).lines.add("6");
      }
      else if (tempStations.get(i).nameKo.equals("신설동"))
      {
        if (!tempStations.get(i).lines.contains("2"))
          tempStations.get(i).lines.add("2");
      }
      else if (tempStations.get(i).nameKo.equals("청량리"))
      {
        if (!tempStations.get(i).lines.contains("경의중앙선"))
          tempStations.get(i).lines.add("경의중앙선");
      }
      else if (tempStations.get(i).nameKo.equals("회기"))
      {
        if (!tempStations.get(i).lines.contains("경의중앙선"))
          tempStations.get(i).lines.add("경의중앙선");
      }
      else if (tempStations.get(i).nameKo.equals("상봉"))
      {
        if (!tempStations.get(i).lines.contains("경의중앙선"))
          tempStations.get(i).lines.add("경의중앙선");
        if (!tempStations.get(i).lines.contains("경춘선"))
          tempStations.get(i).lines.add("경춘선");
      }
      else if (tempStations.get(i).nameKo.equals("망우"))
      {
        if (!tempStations.get(i).lines.contains("경춘선"))
          tempStations.get(i).lines.add("경춘선");
      }
      else if (tempStations.get(i).nameKo.equals("계양"))
      {
        if (!tempStations.get(i).lines.contains("공항철도"))
          tempStations.get(i).lines.add("공항철도");
      }
      else if (tempStations.get(i).nameKo.equals("김포공항"))
      {
        if (!tempStations.get(i).lines.contains("공항철도"))
          tempStations.get(i).lines.add("공항철도");
      }
      else if (tempStations.get(i).nameKo.equals("충정로"))
      {
        if (!tempStations.get(i).lines.contains("5"))
          tempStations.get(i).lines.add("5");
      }
      else if (tempStations.get(i).nameKo.equals("시청"))
      {
        if (!tempStations.get(i).lines.contains("2"))
          tempStations.get(i).lines.add("2");
      }
      else if (tempStations.get(i).nameKo.equals("을지로3가"))
      {
        if (!tempStations.get(i).lines.contains("3"))
          tempStations.get(i).lines.add("3");
      }
      else if (tempStations.get(i).nameKo.equals("을지로4가"))
      {
        if (!tempStations.get(i).lines.contains("5"))
          tempStations.get(i).lines.add("5");
      }
      else if (tempStations.get(i).nameKo.equals("동대문"))
      {
        if (!tempStations.get(i).lines.contains("4"))
          tempStations.get(i).lines.add("4");
      }
      else if (tempStations.get(i).nameKo.equals("동대문역사문화공원"))
      {
        if (!tempStations.get(i).lines.contains("4"))
          tempStations.get(i).lines.add("4");
        if (!tempStations.get(i).lines.contains("5"))
          tempStations.get(i).lines.add("5");
      }
      else if (tempStations.get(i).nameKo.equals("신당"))
      {
        if (!tempStations.get(i).lines.contains("6"))
          tempStations.get(i).lines.add("6");
      }
      else if (tempStations.get(i).nameKo.equals("왕십리"))
      {
        if (!tempStations.get(i).lines.contains("5"))
          tempStations.get(i).lines.add("5");
        if (!tempStations.get(i).lines.contains("경의중앙선"))
          tempStations.get(i).lines.add("경의중앙선");
        if (!tempStations.get(i).lines.contains("신분당선"))
          tempStations.get(i).lines.add("신분당선");
      }
      else if (tempStations.get(i).nameKo.equals("군자"))
      {
        if (!tempStations.get(i).lines.contains("7"))
          tempStations.get(i).lines.add("7");
      }
      else if (tempStations.get(i).nameKo.equals("홍대입구"))
      {
        if (!tempStations.get(i).lines.contains("경의중앙선"))
          tempStations.get(i).lines.add("경의중앙선");
        if (!tempStations.get(i).lines.contains("공항철도"))
          tempStations.get(i).lines.add("공항철도");
      }
      else if (tempStations.get(i).nameKo.equals("공덕"))
      {
        if (!tempStations.get(i).lines.contains("6"))
          tempStations.get(i).lines.add("6");
        if (!tempStations.get(i).lines.contains("경의중앙선"))
          tempStations.get(i).lines.add("경의중앙선");
        if (!tempStations.get(i).lines.contains("공항철도"))
          tempStations.get(i).lines.add("공항철도");
      }
      else if (tempStations.get(i).nameKo.equals("서울역"))
      {
        if (!tempStations.get(i).lines.contains("4"))
          tempStations.get(i).lines.add("4");
        if (!tempStations.get(i).lines.contains("경의중앙선"))
          tempStations.get(i).lines.add("경의중앙선");
        if (!tempStations.get(i).lines.contains("공항철도"))
          tempStations.get(i).lines.add("공항철도");
      }
      else if (tempStations.get(i).nameKo.equals("충무로"))
      {
        if (!tempStations.get(i).lines.contains("4"))
          tempStations.get(i).lines.add("4");
      }
      else if (tempStations.get(i).nameKo.equals("청구"))
      {
        if (!tempStations.get(i).lines.contains("6"))
          tempStations.get(i).lines.add("6");
      }
      else if (tempStations.get(i).nameKo.equals("약수"))
      {
        if (!tempStations.get(i).lines.contains("6"))
          tempStations.get(i).lines.add("6");
      }
      else if (tempStations.get(i).nameKo.equals("옥수"))
      {
        if (!tempStations.get(i).lines.contains("경의중앙선"))
          tempStations.get(i).lines.add("경의중앙선");
      }
      else if (tempStations.get(i).nameKo.equals("건대입구"))
      {
        if (!tempStations.get(i).lines.contains("7"))
          tempStations.get(i).lines.add("7");
      }
      else if (tempStations.get(i).nameKo.equals("합정"))
      {
        if (!tempStations.get(i).lines.contains("6"))
          tempStations.get(i).lines.add("6");
      }
      else if (tempStations.get(i).nameKo.equals("삼각지"))
      {
        if (!tempStations.get(i).lines.contains("6"))
          tempStations.get(i).lines.add("6");
      }
      else if (tempStations.get(i).nameKo.equals("옥수"))
      {
        if (!tempStations.get(i).lines.contains("경의중앙선"))
          tempStations.get(i).lines.add("경의중앙선");
      }
      else if (tempStations.get(i).nameKo.equals("강남구청"))
      {
        if (!tempStations.get(i).lines.contains("신분당선"))
          tempStations.get(i).lines.add("신분당선");
      }
      else if (tempStations.get(i).nameKo.equals("고속터미널"))
      {
        if (!tempStations.get(i).lines.contains("7"))
          tempStations.get(i).lines.add("7");
        if (!tempStations.get(i).lines.contains("9"))
          tempStations.get(i).lines.add("9");
      }
      else if (tempStations.get(i).nameKo.equals("이촌"))
      {
        if (!tempStations.get(i).lines.contains("경의중앙선"))
          tempStations.get(i).lines.add("경의중앙선");
      }
      else if (tempStations.get(i).nameKo.equals("용산"))
      {
        if (!tempStations.get(i).lines.contains("공항철도"))
          tempStations.get(i).lines.add("공항철도");
      }
      else if (tempStations.get(i).nameKo.equals("당산"))
      {
        if (!tempStations.get(i).lines.contains("9"))
          tempStations.get(i).lines.add("9");
      }
      else if (tempStations.get(i).nameKo.equals("영등포구청"))
      {
        if (!tempStations.get(i).lines.contains("5"))
          tempStations.get(i).lines.add("5");
      }
      else if (tempStations.get(i).nameKo.equals("신도림"))
      {
        if (!tempStations.get(i).lines.contains("2"))
          tempStations.get(i).lines.add("2");
      }
      else if (tempStations.get(i).nameKo.equals("신길"))
      {
        if (!tempStations.get(i).lines.contains("5"))
          tempStations.get(i).lines.add("5");
      }
      else if (tempStations.get(i).nameKo.equals("노량진"))
      {
        if (!tempStations.get(i).lines.contains("9"))
          tempStations.get(i).lines.add("9");
      }
      else if (tempStations.get(i).nameKo.equals("동작"))
      {
        if (!tempStations.get(i).lines.contains("9"))
          tempStations.get(i).lines.add("9");
      }
      else if (tempStations.get(i).nameKo.equals("총신대입구(이수)"))
      {
        if (!tempStations.get(i).lines.contains("7"))
          tempStations.get(i).lines.add("7");
      }
      else if (tempStations.get(i).nameKo.equals("선정릉"))
      {
        if (!tempStations.get(i).lines.contains("신분당선"))
          tempStations.get(i).lines.add("신분당선");
      }
      else if (tempStations.get(i).nameKo.equals("잠실"))
      {
        if (!tempStations.get(i).lines.contains("8"))
          tempStations.get(i).lines.add("8");
      }
      else if (tempStations.get(i).nameKo.equals("천효"))
      {
        if (!tempStations.get(i).lines.contains("8"))
          tempStations.get(i).lines.add("8");
      }
      else if (tempStations.get(i).nameKo.equals("오금"))
      {
        if (!tempStations.get(i).lines.contains("5"))
          tempStations.get(i).lines.add("5");
      }
      else if (tempStations.get(i).nameKo.equals("교대"))
      {
        if (!tempStations.get(i).lines.contains("3"))
          tempStations.get(i).lines.add("3");
      }
      else if (tempStations.get(i).nameKo.equals("강남"))
      {
        if (!tempStations.get(i).lines.contains("신분당선"))
          tempStations.get(i).lines.add("신분당선");
      }
      else if (tempStations.get(i).nameKo.equals("도곡"))
      {
        if (!tempStations.get(i).lines.contains("분당선"))
          tempStations.get(i).lines.add("분당선");
      }
      else if (tempStations.get(i).nameKo.equals("수서"))
      {
        if (!tempStations.get(i).lines.contains("분당선"))
          tempStations.get(i).lines.add("분당선");
      }
      else if (tempStations.get(i).nameKo.equals("가락시장"))
      {
        if (!tempStations.get(i).lines.contains("8"))
          tempStations.get(i).lines.add("8");
      }
      else if (tempStations.get(i).nameKo.equals("모란"))
      {
        if (!tempStations.get(i).lines.contains("분당선"))
          tempStations.get(i).lines.add("분당선");
      }
      else if (tempStations.get(i).nameKo.equals("정자"))
      {
        if (!tempStations.get(i).lines.contains("신분당선"))
          tempStations.get(i).lines.add("신분당선");
      }
      else if (tempStations.get(i).nameKo.equals("기흥"))
      {
        if (!tempStations.get(i).lines.contains("에버라인"))
          tempStations.get(i).lines.add("에버라인");
      }
      else if (tempStations.get(i).nameKo.equals("수원"))
      {
        if (!tempStations.get(i).lines.contains("분당선"))
          tempStations.get(i).lines.add("분당선");
      }
      else if (tempStations.get(i).nameKo.equals("사당"))
      {
        if (!tempStations.get(i).lines.contains("4"))
          tempStations.get(i).lines.add("4");
      }
      else if (tempStations.get(i).nameKo.equals("금정"))
      {
        if (!tempStations.get(i).lines.contains("4"))
          tempStations.get(i).lines.add("4");
      }
      else if (tempStations.get(i).nameKo.equals("오이도"))
      {
        if (!tempStations.get(i).lines.contains("수인선"))
          tempStations.get(i).lines.add("9수인선");
      }
      else if (tempStations.get(i).nameKo.equals("가산디지털단지"))
      {
        if (!tempStations.get(i).lines.contains("7"))
          tempStations.get(i).lines.add("7");
      }
      else if (tempStations.get(i).nameKo.equals("대림"))
      {
        if (!tempStations.get(i).lines.contains("7"))
          tempStations.get(i).lines.add("7");
      }
      else if (tempStations.get(i).nameKo.equals("영등포구청"))
      {
        if (!tempStations.get(i).lines.contains("5"))
          tempStations.get(i).lines.add("5");
      }
      else if (tempStations.get(i).nameKo.equals("부평구청"))
      {
        if (!tempStations.get(i).lines.contains("인천1호선"))
          tempStations.get(i).lines.add("인천1호선");
      }
      else if (tempStations.get(i).nameKo.equals("원인재"))
      {
        if (!tempStations.get(i).lines.contains("수인선"))
          tempStations.get(i).lines.add("수인선");
      }
      
    }
    return tempStations;
  }
  
  
  public ArrayList<StationBean> getAllSubwayStations()
  {
    return stations;
  }
  
  
  public ArrayList<String> getAllSubwayNames()
  {
    ArrayList<String> names = new ArrayList<String>();
    for (int i = 0; i < stations.size(); i++)
    {
      names.add(stations.get(i).nameKo);
    }
    
    ArrayList<String> afterArray = new ArrayList<String>();
    for (int i = 0; i < names.size(); i++)
    {
      boolean isContain = false;
      for (int j = 0; j < afterArray.size(); j++)
      {
        if (afterArray.get(j).equals(names.get(i)))
          isContain = true;
      }
      
      if (!isContain)
        afterArray.add(names.get(i));
    }
    
    return afterArray;
  }
  
  
  public ArrayList<String> getAllSubwayCnNames()
  {
    ArrayList<String> names = new ArrayList<String>();
    for (int i = 0; i < stations.size(); i++)
    {
      names.add(stations.get(i).nameCn);
    }
    
    ArrayList<String> afterArray = new ArrayList<String>();
    for (int i = 0; i < names.size(); i++)
    {
      boolean isContain = false;
      for (int j = 0; j < afterArray.size(); j++)
      {
        if (afterArray.get(j).equals(names.get(i)))
          isContain = true;
      }
      
      if (!isContain)
        afterArray.add(names.get(i));
    }
    return afterArray;
  }
  
  
  public String getStationId(String stationName)
  {
    ArrayList<StationBean> sameNameStations = new ArrayList<StationBean>();
    String stationId = "";
    for (int i = 0; i < stations.size(); i++)
    {
      if (stations.get(i).nameKo.equals(stationName))
        sameNameStations.add(stations.get(i));
    }
    
    if (sameNameStations.size() == 1)
      return sameNameStations.get(1).stationId;
    else
    {
      for (int i = 0; i < sameNameStations.size(); i++)
      {
        if (TextUtils.isEmpty(sameNameStations.get(i).stationId))
          stationId = sameNameStations.get(i).stationId;
        
        if (stationId.length() < sameNameStations.get(i).stationId.length())
          stationId = sameNameStations.get(i).stationId;
      }
    }
    return stationId;
  }
  
  
  public int getSubwayLineResourceId(String line)
  {
    Log.w("WARN", "line: " + line);
    int resourceId = context.getResources().getIdentifier("drawable/icon_subway_line1", null, context.getPackageName());
    try
    {
      if (line.equals("2"))
        resourceId = context.getResources().getIdentifier("drawable/icon_subway_line2", null, context.getPackageName());
      else if (line.equals("3"))
        resourceId = context.getResources().getIdentifier("drawable/icon_subway_line3", null, context.getPackageName());
      else if (line.equals("4"))
        resourceId = context.getResources().getIdentifier("drawable/icon_subway_line4", null, context.getPackageName());
      else if (line.equals("5"))
        resourceId = context.getResources().getIdentifier("drawable/icon_subway_line5", null, context.getPackageName());
      else if (line.equals("6"))
        resourceId = context.getResources().getIdentifier("drawable/icon_subway_line6", null, context.getPackageName());
      else if (line.equals("7"))
        resourceId = context.getResources().getIdentifier("drawable/icon_subway_line7", null, context.getPackageName());
      else if (line.equals("8"))
        resourceId = context.getResources().getIdentifier("drawable/icon_subway_line8", null, context.getPackageName());
      else if (line.equals("9"))
        resourceId = context.getResources().getIdentifier("drawable/icon_subway_line9", null, context.getPackageName());
      else if (line.equals("공항철도"))
        resourceId = context.getResources().getIdentifier("drawable/icon_subway_linea", null, context.getPackageName());
      else if (line.equals("분당선"))
        resourceId = context.getResources().getIdentifier("drawable/icon_subway_lineb", null, context.getPackageName());
      else if (line.equals("에버라인"))
        resourceId = context.getResources().getIdentifier("drawable/icon_subway_lineever", null, context.getPackageName());
      else if (line.equals("경춘선"))
        resourceId = context.getResources().getIdentifier("drawable/icon_subway_linegc", null, context.getPackageName());
      else if (line.equals("경의중앙선"))
        resourceId = context.getResources().getIdentifier("drawable/icon_subway_linegj", null, context.getPackageName());
      else if (line.equals("인천1호선"))
        resourceId = context.getResources().getIdentifier("drawable/icon_subway_lineinc1", null, context.getPackageName());
      else if (line.equals("신분당선"))
        resourceId = context.getResources().getIdentifier("drawable/icon_subway_linesb", null, context.getPackageName());
      else if (line.equals("수인선"))
        resourceId = context.getResources().getIdentifier("drawable/icon_subway_linesuin", null, context.getPackageName());
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return resourceId;
  }
}
