package com.dabeeo.hanhayou.views;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.sub.ImagePopUpActivity;
import com.dabeeo.hanhayou.beans.ReviewBean;
import com.squareup.picasso.Picasso;

public class ProductReviewView extends RelativeLayout
{
  private Context context;
  private ReviewBean bean;
  
  private TextView name;
  private TextView time;
  private TextView content;
  private ImageView btnMore;
  
  private ListPopupWindow listPopupWindow;
  private HorizontalScrollView productHorizontalScrollView;
  private ViewGroup horizontalImagesView;
  
  
  public ProductReviewView(Context context)
  {
    super(context);
    this.context = context;
  }
  
  
  public ProductReviewView(Context context, AttributeSet attrs, int defStyle)
  {
    super(context, attrs, defStyle);
    this.context = context;
  }
  
  
  public ProductReviewView(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    this.context = context;
  }
  
  
  public void setBean(final ReviewBean bean)
  {
    this.bean = bean;
    init();
    initHorizontalImage();
    name.setText(bean.userName);
    time.setText(bean.insertDateString);
    content.setText(bean.content);
  }
  
  public void initHorizontalImage()
  {
    horizontalImagesView.removeAllViews();
    for (int i = 0; i < bean.imageUrls.size(); i++)
    {
      int resId = R.layout.list_item_recommend_seoul_photo;
      View parentView = LayoutInflater.from(context).inflate(resId, null);
      
      final String imageUrl = bean.imageUrls.get(i);
      ImageView view = (ImageView) parentView.findViewById(R.id.photo);
      float density = getResources().getDisplayMetrics().density;
      Picasso.with(context).load(imageUrl).resize((int) (70 * density), (int) (70 * density)).centerCrop().into(view);
      final String finalImageUrl = imageUrl;
      view.setOnClickListener(new OnClickListener()
      {
        @Override
        public void onClick(View arg0)
        {
          Log.w("WARN", "onClick!");
          Intent intent = new Intent(context, ImagePopUpActivity.class);
          intent.putExtra("imageUrls", bean.imageUrls);
          intent.putExtra("imageUrl", finalImageUrl);
          context.startActivity(intent);
        }
      });
      horizontalImagesView.addView(parentView);
    }
  }
  
  
  public void init()
  {
    int resId = R.layout.view_product_review;
    View view = LayoutInflater.from(context).inflate(resId, null);
    view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    
    horizontalImagesView = (ViewGroup) view.findViewById(R.id.horizontal_images_view);
    productHorizontalScrollView = (HorizontalScrollView) view.findViewById(R.id.proudct_review_scrollView);
    productHorizontalScrollView.setHorizontalScrollBarEnabled(false);
    name = (TextView) view.findViewById(R.id.name);
    time = (TextView) view.findViewById(R.id.time);
    content = (TextView) view.findViewById(R.id.content);
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
