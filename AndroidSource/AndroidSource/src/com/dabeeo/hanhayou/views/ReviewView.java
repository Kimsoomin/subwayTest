package com.dabeeo.hanhayou.views;

import java.text.SimpleDateFormat;

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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.sub.ImagePopUpActivity;
import com.dabeeo.hanhayou.beans.ReviewBean;
import com.dabeeo.hanhayou.managers.PreferenceManager;
import com.dabeeo.hanhayou.utils.ImageDownloader;
import com.dabeeo.hanhayou.utils.SystemUtil;
import com.squareup.picasso.Picasso;

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
  private ViewGroup horizontalImagesView;
  private DeleteListener deleteListener;
  
  private View view;
  
  
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
    
    SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd hh:mm");
    if (bean.insertDate != null)
      time.setText(format.format(bean.insertDate));
    
    reviewScore.setText(Float.toString(bean.rate));
    content.setText(bean.content);
    
    ImageDownloader.displayProfileImage(context, bean.mfidx, icon);
    
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
    view = LayoutInflater.from(context).inflate(resId, null);
    view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    
    horizontalImagesView = (ViewGroup) view.findViewById(R.id.horizontal_images_view);
    
    icon = (ImageView) view.findViewById(R.id.icon);
    name = (TextView) view.findViewById(R.id.name);
    time = (TextView) view.findViewById(R.id.time);
    content = (TextView) view.findViewById(R.id.content);
    reviewScore = (TextView) view.findViewById(R.id.text_review_score);
    btnMore = (ImageView) view.findViewById(R.id.btn_review_list_more);
    
    if(!SystemUtil.isConnectNetwork(context))
      btnMore.setVisibility(View.INVISIBLE);
    
    addView(view);
  }
  
}
