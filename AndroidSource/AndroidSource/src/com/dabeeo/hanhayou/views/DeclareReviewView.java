package com.dabeeo.hanhayou.views;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.controllers.SpinnerAdapter;

public class DeclareReviewView extends RelativeLayout
{
  private Context context;
  
  private Spinner spinner;
  private int declareReason = 0;
  private EditText editReason;
  
  
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
  
  
  public String getReason()
  {
    String reason = "";
    if (spinner.getSelectedItemPosition() == 1 || spinner.getSelectedItemPosition() == 2)
      reason = (String) spinner.getSelectedItem();
    else
      reason = editReason.getText().toString();
    
    Log.w("WARN", "신고 이유 : " + reason);
    return reason;
  }
  
  
  public void init()
  {
    int resId = R.layout.view_declare_review;
    View view = LayoutInflater.from(context).inflate(resId, null);
    view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    
    ArrayList<String> reasons = new ArrayList<>();
    reasons.add(context.getString(R.string.msg_review_declare_reason));
    reasons.add(context.getString(R.string.msg_review_declare_reason_1));
    reasons.add(context.getString(R.string.msg_review_declare_reason_2));
    reasons.add(context.getString(R.string.msg_review_declare_reason_3));
    
    editReason = (EditText) view.findViewById(R.id.edit_review_declare);
    spinner = (Spinner) view.findViewById(R.id.spinner);
    SpinnerAdapter spinnerArrayAdapter = new SpinnerAdapter(context, android.R.layout.simple_list_item_1, reasons);
    spinner.setAdapter(spinnerArrayAdapter);
    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
    {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
      {
        declareReason = position;
        if (position == 3)
          editReason.setVisibility(View.VISIBLE);
        else
          editReason.setVisibility(View.GONE);
      }
      
      
      @Override
      public void onNothingSelected(AdapterView<?> parent)
      {
        
      }
    });
    addView(view);
  }
}
