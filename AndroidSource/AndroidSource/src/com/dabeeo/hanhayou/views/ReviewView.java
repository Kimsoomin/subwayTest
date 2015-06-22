package com.dabeeo.hanhayou.views;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.sub.ImagePopUpActivity;
import com.dabeeo.hanhayou.beans.ReviewBean;
import com.dabeeo.hanhayou.managers.PreferenceManager;
import com.dabeeo.hanhayou.utils.ImageDownloader;

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
  private LinearLayout imageContainer;
  private DeleteListener deleteListener;
  
  
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
  
  public interface DeleteListener
  {
    public void onDelete(String idx);
    
    
    public void onDeclare(String idx, String reason);
  }
  
  
  public void setBean(final ReviewBean bean, final DeleteListener deleteListener)
  {
    this.bean = bean;
    this.deleteListener = deleteListener;
    init();
    
    name.setText(bean.userName);
    time.setText(bean.insertDateString);
    reviewScore.setText(Integer.toString(bean.rate));
    content.setText(bean.content);
    
    ImageDownloader.displayProfileImage(context, "", icon);
    
    for (int i = 0; i < bean.imageUrls.size(); i++)
    {
      ImageView imageView = new ImageView(context);
      LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(80, 80);
      params.setMargins(8, 8, 8, 8);
      imageView.setLayoutParams(params);
      final String imageUrl = bean.imageUrls.get(i);
      imageView.setOnClickListener(new OnClickListener()
      {
        @Override
        public void onClick(View arg0)
        {
          Intent i = new Intent(context, ImagePopUpActivity.class);
          i.putExtra("imageUrls", bean.imageUrls);
          i.putExtra("imageUrl", imageUrl);
          context.startActivity(i);
        }
      });
      imageView.setImageResource(R.drawable.default_thumbnail_s);
      ImageDownloader.displayImage(context, bean.imageUrls.get(i), imageView, null);
      imageContainer.addView(imageView);
    }
    
    btnMore.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        Log.w("WARN", "Click!");
        listPopupWindow = new ListPopupWindow(context);
        String[] listPopupArray = new String[1];
        
        if (TextUtils.isEmpty(PreferenceManager.getInstance(context).getUserSeq()))
          listPopupArray[0] = context.getString(R.string.term_declare);
        else
        {
          if (PreferenceManager.getInstance(context).getUserSeq().equals(bean.ownerUserSeq))
            listPopupArray[0] = context.getString(R.string.term_delete);
          else
            listPopupArray[0] = context.getString(R.string.term_declare);
        }
        
        final String[] finalListPopupArray = listPopupArray;
        listPopupWindow.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, listPopupArray));
        listPopupWindow.setAnchorView(btnMore);
        listPopupWindow.setWidth(300);
        listPopupWindow.setOnItemClickListener(new OnItemClickListener()
        {
          @Override
          public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
          {
            if (finalListPopupArray[position].equals(context.getString(R.string.term_delete)))
            {
              //삭제
              Builder builder = new AlertDialog.Builder(context);
              builder.setTitle(context.getString(R.string.app_name));
              builder.setMessage(context.getString(R.string.msg_confirm_delete));
              builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
              {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                  if (deleteListener != null)
                    deleteListener.onDelete(bean.idx);
                }
              });
              builder.setNegativeButton(android.R.string.cancel, null);
              builder.show();
            }
            else
            {
              //신고
              Builder builder = new AlertDialog.Builder(context);
              builder.setTitle(context.getString(R.string.term_declare_review));
              final DeclareReviewView declareView = new DeclareReviewView(context);
              declareView.init();
              builder.setView(declareView);
              builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
              {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                  if (deleteListener != null)
                    deleteListener.onDeclare(bean.idx, declareView.getReason());
                }
              });
              builder.setNegativeButton(android.R.string.cancel, null);
              builder.show();
            }
            listPopupWindow.dismiss();
          }
        });
        listPopupWindow.show();
      }
    });
  }
  
  
  public void init()
  {
    int resId = R.layout.view_place_review;
    View view = LayoutInflater.from(context).inflate(resId, null);
    view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    
    imageContainer = (LinearLayout) view.findViewById(R.id.image_container);
    
    icon = (ImageView) view.findViewById(R.id.icon);
    name = (TextView) view.findViewById(R.id.name);
    time = (TextView) view.findViewById(R.id.time);
    content = (TextView) view.findViewById(R.id.content);
    reviewScore = (TextView) view.findViewById(R.id.text_review_score);
    btnMore = (ImageView) view.findViewById(R.id.btn_review_list_more);
    
    addView(view);
  }
  
}
