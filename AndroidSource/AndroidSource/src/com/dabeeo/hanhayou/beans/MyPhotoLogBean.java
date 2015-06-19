package com.dabeeo.hanhayou.beans;

public class MyPhotoLogBean
{
  public String title;
  public String days;
  public int photoCount;
  public String date;
  public int likeCount = 0;
  
  //로컬에 저장하지 않아도 됨 삭제를 위한 임시 flag 
  public boolean isChecked = false;
}
