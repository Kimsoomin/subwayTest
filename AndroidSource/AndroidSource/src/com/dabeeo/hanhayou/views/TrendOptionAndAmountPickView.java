package com.dabeeo.hanhayou.views;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.beans.ProductDetailBean.ProductOptionInfo;
import com.dabeeo.hanhayou.controllers.SpinnerAdapter;
import com.dabeeo.hanhayou.controllers.mainmenu.WishListAdapter.WishListListener;
import com.dabeeo.hanhayou.map.BlinkingCommon;

public class TrendOptionAndAmountPickView extends RelativeLayout
{
  private Context context;
  public View view;
  public LinearLayout containerOptions, amountLayout;
  private Button btnMinus, btnPlus;
  private TextView textAmount;
  private View background;
  public int amount = 1;
  
  private Spinner firstOption;
  private Spinner secondOption;
  public String optionSize;
  public String optionColor;
  public String itemAttribute;
  public JSONObject productOptionObj;
  public boolean selected = false;
  public SpinnerAdapter spinnerArrayAdapter2;
  public SpinnerAdapter spinnerArrayAdapter;
  
  public ArrayList<productSet> productSetList = new ArrayList<productSet>();
  
  private WishListListener wishListener;
  
  public TrendOptionAndAmountPickView(Context context)
  {
    super(context);
    this.context = context;
    init();
  }
  
  
  public TrendOptionAndAmountPickView(Context context, AttributeSet attrs, int defStyle)
  {
    super(context, attrs, defStyle);
    this.context = context;
    init();
  }
  
  
  public TrendOptionAndAmountPickView(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    this.context = context;
    init();
  }
  
  public void setOptionSelected(boolean selecte)
  {
    selected = selecte;
  }
  
  
  public boolean getOptionSelected()
  {
    return selected;
  }
  
  
  public void initSpinner()
  {
    firstOption.setSelection(0);
    secondOption.setSelection(0);
    setOptionSelected(false);
  }
  
  public void setWishListener(WishListListener wishListenr)
  {
    this.wishListener = wishListenr;
  }
  
  
  public void setOptions(ArrayList<ProductOptionInfo> options, JSONObject productOptionList)
  {
    JSONArray arr;
    
    containerOptions.removeAllViews();
    
    productOptionObj = productOptionList;
    
    try
    {
      for(int i = 0; i < options.get(0).optionAttributeArr.size(); i++)
      {
        String optionId = options.get(0).optionAttributeArr.get(i).optionId;
        arr = productOptionObj.getJSONArray(optionId);
        for(int j = 0; j < arr.length(); j++)
        {
          String attributeId = arr.getString(j);
          productSet productset = new productSet();
          productset.setId(optionId, attributeId);
          productSetList.add(productset);
        }
      }
      BlinkingCommon.smlLibDebug("TrendOptionAndAmountPickView", "productSetList size : " + productSetList.size());      
    }
    catch (Exception e)
    {
      BlinkingCommon.smlLibPrintException("TrendOptionAndAmountPickView", "e : " + e);
    }
    containerOptions.addView(initOptionPicker(options));
  }
  
  
  public View initOptionPicker(final ArrayList<ProductOptionInfo> attributeArray)
  {
    int resId = R.layout.view_option_picker;
    View view = LayoutInflater.from(context).inflate(resId, null);
    view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    firstOption = (Spinner) view.findViewById(R.id.first_option);
    secondOption = (Spinner) view.findViewById(R.id.second_option);
    
    ArrayList<String> firstOptions = new ArrayList<String>();
    firstOptions.add(attributeArray.get(0).optionAtrributeNm);
    for(int i = 0; i < attributeArray.get(0).optionAttributeArr.size(); i++)
    {
      firstOptions.add(attributeArray.get(0).optionAttributeArr.get(i).attributeNm);
    }
    
    final ArrayList<String> secondOptions = new ArrayList<String>();
    spinnerArrayAdapter2 = new SpinnerAdapter(context, android.R.layout.simple_spinner_dropdown_item, secondOptions);;
    if(attributeArray.size() > 1)
    {
      secondOptions.add(attributeArray.get(1).optionAtrributeNm);
      secondOption.setAdapter(spinnerArrayAdapter2);
      secondOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
      {
        
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
        {
          if(position != 0)
          {
            setOptionSelected(true);
            optionSize = spinnerArrayAdapter2.getItem(position);
            amountLayout.setVisibility(View.VISIBLE);
          }else
          {
            setOptionSelected(false);
            amountLayout.setVisibility(View.GONE);
          }
        }
        
        @Override
        public void onNothingSelected(AdapterView<?> parent)
        {
          
        }
        
      });
    }else
    {
      secondOption.setVisibility(View.GONE);
    }
    
    spinnerArrayAdapter = new SpinnerAdapter(context, android.R.layout.simple_spinner_dropdown_item, firstOptions);
    firstOption.setAdapter(spinnerArrayAdapter);
    firstOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
    {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
      {
        String optionid = "";
        if(attributeArray.size() > 1 )
        {
          if(position != 0)
          {
            optionColor = spinnerArrayAdapter.getItem(position);
            
            for(int i = 0; i < attributeArray.get(0).optionAttributeArr.size(); i++)
            {
              String attributeName = attributeArray.get(0).optionAttributeArr.get(i).attributeNm;
              if(optionColor.equals(attributeName))
              {
                optionid = attributeArray.get(0).optionAttributeArr.get(i).optionId;
                spinnerArrayAdapter2.notifyDataSetChanged();
                secondOptions.clear();
                secondOptions.add(attributeArray.get(1).optionAtrributeNm);
              }
            }
            
            for(int j = 0; j < productSetList.size(); j++)
            {
              productSet ps = productSetList.get(j);
              if(optionid.equals(ps.colorId))
              {
                for(int k = 0; k < attributeArray.get(1).optionAttributeArr.size(); k++)
                {
                  if(ps.sizeId.equals(attributeArray.get(1).optionAttributeArr.get(k).optionId))
                    secondOptions.add(attributeArray.get(1).optionAttributeArr.get(k).attributeNm);
                }
              }
            }
            secondOption.setSelection(0);
          }else
          {
            if(position != 0)
            {
              setOptionSelected(true);
              optionColor = spinnerArrayAdapter.getItem(position);
              amountLayout.setVisibility(View.VISIBLE);              
            }else
            {
              setOptionSelected(false);
              amountLayout.setVisibility(View.GONE);
            }
          }
          
        }
      }
      
      @Override
      public void onNothingSelected(AdapterView<?> parent)
      {
        
      }
    });
    
    return view;
  }
  
  
  public void init()
  {
    int resId = R.layout.view_trend_option_and_amount_pick;
    view = LayoutInflater.from(context).inflate(resId, null);
    view.setVisibility(View.GONE);
    
    containerOptions = (LinearLayout) view.findViewById(R.id.option_container);
    amountLayout = (LinearLayout) view.findViewById(R.id.amount_layout);
    btnMinus = (Button) view.findViewById(R.id.date_down);
    btnPlus = (Button) view.findViewById(R.id.date_up);
    textAmount = (TextView) view.findViewById(R.id.text_amount);
    background = (View) view.findViewById(R.id.background);
    background.setOnClickListener(finishClickListener);
    
    textAmount.setText(Integer.toString(amount));
    btnMinus.setOnClickListener(amountClickListener);
    btnPlus.setOnClickListener(amountClickListener);
    addView(view);
  }
  
  
  private OnClickListener amountClickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      if (v.getId() == btnMinus.getId())
      {
        if (amount == 1)
          return;
        amount--;
      }
      else
        amount++;
      textAmount.setText(Integer.toString(amount));
    }
  };
  
  
  private OnClickListener finishClickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      view.setVisibility(View.GONE);
      if(wishListener != null)
        wishListener.onOptionClose();
    }
  };
  
  class productSet
  {
    public String colorId;
    public String sizeId;
    
    public void setId(String colorId, String sizeId)
    {
      this.colorId = colorId;
      this.sizeId = sizeId;
    }
  }
}
