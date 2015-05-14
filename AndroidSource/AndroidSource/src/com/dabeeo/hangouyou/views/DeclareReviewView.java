package com.dabeeo.hangouyou.views;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.controllers.SpinnerAdapter;

public class DeclareReviewView extends RelativeLayout
{
  private Context context;
  
  private Spinner spinner;
  private int declareReason = 0;
  
  
  public DeclareReviewView(Context context)
  {
    super(context);
    this.context = context;
  }
  
  
  public DeclareReviewView(Context context, AttributeSet attrs, int defStyle)
  {
    super(context, attrs, defStyle);
    this.context = context;
  }
  
  
  public DeclareReviewView(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    this.context = context;
  }
  
  
  public void init()
  {
    int resId = R.layout.view_declare_review;
    View view = LayoutInflater.from(context).inflate(resId, null);
    view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    
    ArrayList<String> reasons = new ArrayList<>();
    reasons.add(context.getString(R.string.msg_review_declare_reason_1));
    reasons.add(context.getString(R.string.msg_review_declare_reason_2));
    reasons.add(context.getString(R.string.msg_review_declare_reason_3));
    
    spinner = (Spinner) view.findViewById(R.id.spinner);
    SpinnerAdapter spinnerArrayAdapter = new SpinnerAdapter(context, android.R.layout.simple_list_item_1, reasons);
    spinner.setAdapter(spinnerArrayAdapter);
    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
    {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
      {
        declareReason = position;
      }
      
      
      @Override
      public void onNothingSelected(AdapterView<?> parent)
      {
        
      }
    });
    addView(view);
  }
}
