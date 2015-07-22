package com.dabeeo.hanhayou.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

public class NumberFormatter
{
  public static String getTimeString(int time)
  {
    String timeString = "";
    try
    {
      int hours = (time / 60);
      int minutes = (time % 60);
      
      if(hours != 0)
        timeString = Integer.toString(hours) + "h ";
      if (minutes != 0)
        timeString = timeString + Integer.toString(minutes) + "m";
      
      if(timeString.length() == 0)
        timeString = "0h";
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return timeString;
  }
  
  
  /**
   * 콤마를 넣는다
   * 
   * @param value
   * @return
   */
  public static String addComma(int value)
  {
    return addComma(Integer.toString(value));
  }
  
  
  /**
   * 콤마를 넣는다.
   * 
   * @param value
   * @return
   */
  public static String addComma(String value)
  {
    String result = value;
    
    DecimalFormat format = new DecimalFormat("#,###");
    
    try
    {
      // 소수점이 있을 땐, 소수점 앞 쪽의 숫자만 변환
      int index = value.indexOf(".");
      if (value.indexOf(".") >= 0)
      {
        String front = value.substring(0, index);
        Number num = NumberFormat.getInstance().parse(front);
        front = format.format(num).toString();
        String end = value.substring(index + 1, value.length());
        
        result = front + "." + end;
      }
      else
      {
        Number num = NumberFormat.getInstance().parse(value);
        result = format.format(num).toString();
      }
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }
    
    return result;
  }
}
