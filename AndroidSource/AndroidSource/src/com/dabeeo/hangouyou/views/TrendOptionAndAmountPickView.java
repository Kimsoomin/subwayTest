package com.dabeeo.hangouyou.views;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;

public class TrendOptionAndAmountPickView extends RelativeLayout
{
  private Context context;
  public View view;
  public LinearLayout containerOptions;
  private Button btnMinus, btnPlus;
  private TextView textAmount;
  private View background;
  private int amount = 1;
  
  
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
  
  
  public void setOptions(ArrayList<String> options)
  {
    //추후 이 곳에 옵션에 따라 뷰를 여러번 애드 시켜야 함
    containerOptions.removeAllViews();
    
    OptionPickerView pickerView = new OptionPickerView(context);
    containerOptions.addView(pickerView);
    
    pickerView = new OptionPickerView(context);
    containerOptions.addView(pickerView);
  }
  
  
  public void init()
  {
    int resId = R.layout.view_trend_option_and_amount_pick;
    view = LayoutInflater.from(context).inflate(resId, null);
    
    containerOptions = (LinearLayout) view.findViewById(R.id.option_container);
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
    }
  };
  
  private OnClickListener shareClickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      String url = "";
      
      if (v.getId() == R.id.container_qq)
      {
        if (isAppInstalled("com.tencent.mobileqq"))
        {
          share("com.tencent.mobileqq");
          return;
        }
        else
          url = "https://play.google.com/store/apps/details?id=com.tencent.mobileqq";
      }
      else if (v.getId() == R.id.container_wechat)
      {
        if (isAppInstalled("com.tencent.mm"))
        {
          share("com.tencent.mm");
          return;
        }
        else
          url = "https://play.google.com/store/apps/details?id=com.tencent.mm";
      }
      else if (v.getId() == R.id.container_weibo)
      {
        if (isAppInstalled("com.sina.weibo"))
        {
          share("com.sina.weibo");
          return;
        }
        else
          url = "https://play.google.com/store/apps/details?id=com.sina.weibo";
      }
      
      Intent i = new Intent(Intent.ACTION_VIEW);
      i.setData(Uri.parse(url));
      context.startActivity(i);
    }
  };
  
  
  @SuppressLint("DefaultLocale")
  private void share(String sharePackageName)
  {
    boolean found = false;
    Intent intent = new Intent(android.content.Intent.ACTION_SEND);
    
    Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
    String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
    File file = new File(extStorageDirectory, "ic_launcher.png");
    
    try
    {
      if (!file.exists())
        file.createNewFile();
    }
    catch (IOException e1)
    {
      e1.printStackTrace();
    }
    FileOutputStream outStream;
    try
    {
      outStream = new FileOutputStream(file);
      bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
      outStream.flush();
      outStream.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    intent.putExtra(Intent.EXTRA_TEXT, "[Hanhayou] Download Hanhayou! http://dabeeo.com");
    intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(file.getPath()));
    
    //Weibo는 Image+Text가능, WeChat/QQ는 Text만 가능 
    intent.setType("*/*");
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//    intent.setType("text/plain");
    
    List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(intent, 0);
    if (!resInfo.isEmpty())
    {
      for (ResolveInfo info : resInfo)
      {
        if (info.activityInfo.packageName.toLowerCase().equals(sharePackageName))
        {
          intent.setPackage(info.activityInfo.packageName);
          found = true;
          break;
        }
      }
      if (!found)
        return;
      
      context.startActivity(Intent.createChooser(intent, "Share"));
    }
  }
  
  
  private boolean isAppInstalled(String packageName)
  {
    try
    {
      context.getPackageManager().getApplicationInfo(packageName, 0);
      return true;
    }
    catch (PackageManager.NameNotFoundException e)
    {
      return false;
    }
  }
}
