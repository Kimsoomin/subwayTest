package com.dabeeo.hanhayou.views;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.controllers.SpinnerAdapter;

public class OptionPickerView extends RelativeLayout
{
  private Context context;
  private Spinner spinner;
  public String selectString;
  
  
  public OptionPickerView(Context context)
  {
    super(context);
    this.context = context;
    init();
  }
  
  
  public OptionPickerView(Context context, AttributeSet attrs, int defStyle)
  {
    super(context, attrs, defStyle);
    this.context = context;
    init();
  }
  
  
  public OptionPickerView(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    this.context = context;
    init();
  }
  
  
  public void init()
  {
    int resId = R.layout.view_option_picker;
    View view = LayoutInflater.from(context).inflate(resId, null);
    view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    spinner = (Spinner) view.findViewById(R.id.spinner);
    
    ArrayList<String> options = new ArrayList<String>();
    options.add("컬러");
    options.add("RED");
    options.add("BLACK");
    options.add("BLUE");
    final SpinnerAdapter spinnerArrayAdapter = new SpinnerAdapter(context, android.R.layout.simple_list_item_1, options);
//  spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(spinnerArrayAdapter);
    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
    {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
      {
        selectString = spinnerArrayAdapter.getItem(position);
      }
      
      
      @Override
      public void onNothingSelected(AdapterView<?> parent)
      {
        
      }
    });
    addView(view);
  }
}
