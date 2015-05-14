package com.dabeeo.hangouyou.views;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.ReviewBean;

public class ReviewView extends RelativeLayout
{
  private Context context;
  private ReviewBean bean;
  
  private ImageView icon;
  private TextView name;
  private TextView time;
  private TextView content;
  private TextView reviewScore;
  private ImageView btnMore;
  
  private ListPopupWindow listPopupWindow;
  
  
  public ReviewView(Context context)
  {
    super(context);
    this.context = context;
  }
  
  
  public ReviewView(Context context, AttributeSet attrs, int defStyle)
  {
    super(context, attrs, defStyle);
    this.context = context;
  }
  
  
  public ReviewView(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    this.context = context;
  }
  
  
  public void setBean(ReviewBean bean)
  {
    this.bean = bean;
    init();
    
    name.setText(bean.userName);
    time.setText(bean.insertDateString);
    reviewScore.setText(Integer.toString(bean.rate));
    content.setText(bean.content);
  }
  
  
  public void init()
  {
    int resId = R.layout.view_place_review;
    View view = LayoutInflater.from(context).inflate(resId, null);
    view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    
    icon = (ImageView) view.findViewById(R.id.icon);
    name = (TextView) view.findViewById(R.id.name);
    time = (TextView) view.findViewById(R.id.time);
    content = (TextView) view.findViewById(R.id.content);
    reviewScore = (TextView) view.findViewById(R.id.text_review_score);
    btnMore = (ImageView) view.findViewById(R.id.btn_review_list_more);
    
    btnMore.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        Log.w("WARN", "Click!");
        listPopupWindow = new ListPopupWindow(context);
        String[] listPopupArray = new String[2];
        listPopupArray[0] = context.getString(R.string.term_delete);
        listPopupArray[1] = context.getString(R.string.term_declare);
        
        listPopupWindow.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, listPopupArray));
        listPopupWindow.setAnchorView(btnMore);
        listPopupWindow.setWidth(300);
        listPopupWindow.setOnItemClickListener(new OnItemClickListener()
        {
          @Override
          public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
          {
            if (position == 0)
            {
              //삭제
              Builder builder = new AlertDialog.Builder(context);
              builder.setTitle(context.getString(R.string.app_name));
              builder.setMessage(context.getString(R.string.msg_confirm_delete));
              builder.setPositiveButton(android.R.string.ok, null);
              builder.setNegativeButton(android.R.string.cancel, null);
              builder.show();
            }
            else
            {
              //신고
              Builder builder = new AlertDialog.Builder(context);
              builder.setTitle(context.getString(R.string.term_declare_review));
              DeclareReviewView declareView = new DeclareReviewView(context);
              declareView.init();
              final EditText editReasonText = (EditText) declareView.findViewById(R.id.edit_review_declare);
              builder.setView(declareView);
              builder.setPositiveButton(android.R.string.ok, null);
              builder.setNegativeButton(android.R.string.cancel, null);
              builder.show();
            }
            listPopupWindow.dismiss();
          }
        });
        listPopupWindow.show();
      }
    });
    addView(view);
  }
}
