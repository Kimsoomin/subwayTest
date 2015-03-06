package com.dabeeo.hangouyou.beans;

public class MyPlaceBean
{
  public String title;
  public String category;
  public int likeCount = 0;
  public int reviewCount = 0;
  
  //로컬에 저장하지 않아도 됨 삭제를 위한 임시 flag 
  public boolean isChecked = false;
}
