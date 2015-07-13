package com.dabeeo.hanhayou.views;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;

public class SharePickView extends RelativeLayout
{
  private static final String WECHAP_APP_ID = "wx3dc1d3c651002e56";
  private Context context;
  public View view;
  public LinearLayout btnWechat, btnQQ, btnWeibo;
  private TextView btnClose;
  private View background;
  
//  private IWXAPI api;
  
  private String body;
  private String imageUrl;
  
  
  public SharePickView(Context context)
  {
    super(context);
    this.context = context;
    init();
  }
  
  
  public SharePickView(Context context, AttributeSet attrs, int defStyle)
  {
    super(context, attrs, defStyle);
    this.context = context;
    init();
  }
  
  
  public SharePickView(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    this.context = context;
    init();
  }
  
  
  //추천서울 type=premium 일정상세 type=plan 장소상세 type=place
  public void setData(String body, String imageUrl, String type, String idx)
  {
    this.body = "[Hanhayou]\n" + body;
    this.imageUrl = imageUrl;
    if (!TextUtils.isEmpty(idx))
      this.body += "http://gs.blinking.kr:8900/?type=" + type + "&mode=view&idx=" + 204;
  }
  
  
  public void init()
  {
    int resId = R.layout.view_share;
    view = LayoutInflater.from(context).inflate(resId, null);
    
    btnQQ = (LinearLayout) view.findViewById(R.id.container_qq);
    btnWechat = (LinearLayout) view.findViewById(R.id.container_wechat);
    btnWeibo = (LinearLayout) view.findViewById(R.id.container_weibo);
    btnQQ.setOnClickListener(shareClickListener);
    btnWechat.setOnClickListener(shareClickListener);
    btnWeibo.setOnClickListener(shareClickListener);
    btnClose = (TextView) view.findViewById(R.id.btn_close);
    btnClose.setOnClickListener(finishClickListener);
    background = (View) view.findViewById(R.id.background);
    background.setOnClickListener(finishClickListener);
//    regToWx();
    addView(view);
  }
  
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
//          regToWx();
//          shareWeChat();
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
  
  
//  private void regToWx()
//  {
//    api = WXAPIFactory.createWXAPI(context, WECHAP_APP_ID);
//    api.registerApp(WECHAP_APP_ID);
//  }
//  
//  
//  private void shareWeChat()
//  {
//    WXTextObject textObj = new WXTextObject();
//    textObj.text = "TEST";
//    
//    WXMediaMessage msg = new WXMediaMessage();
//    msg.mediaObject = textObj;
//    msg.description = "TEST";
//    
//    SendMessageToWX.Req req = new SendMessageToWX.Req();
//    req.transaction = buildTransaction("text");
//    req.message = msg;
//    req.scene = SendMessageToWX.Req.WXSceneSession;
//    api.sendReq(req);
//  }
  
//  private String buildTransaction(final String type)
//  {
//    return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
//  }
  
  @SuppressLint("DefaultLocale")
  private void share(String sharePackageName)
  {
    boolean found = false;
    Intent intent = new Intent(android.content.Intent.ACTION_SEND);
    
    //Weibo는 Image+Text가능, WeChat/QQ는 Text만 가능 
    if (sharePackageName.equals("com.sina.weibo"))
    {
      if (TextUtils.isEmpty(imageUrl))
      {
        intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.putExtra(android.content.Intent.EXTRA_TEXT, body);
        intent.setType("image/*");
      }
      else
        new ImageDownloader().execute();
    }
    else
    {
      intent.putExtra(android.content.Intent.EXTRA_TEXT, body);
      intent.setType("text/plain");
    }
    
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
  
  private class ImageDownloader extends AsyncTask<String, Integer, String>
  {
    
    @Override
    protected String doInBackground(String... param)
    {
      String fileUrl = "";
      Bitmap bitmap = null;
      
      final DefaultHttpClient client = new DefaultHttpClient();
      final HttpGet getRequest = new HttpGet(imageUrl);
      try
      {
        HttpResponse response = client.execute(getRequest);
        final int statusCode = response.getStatusLine().getStatusCode();
        
        if (statusCode != HttpStatus.SC_OK)
        {
          Log.w("ImageDownloader", "Error " + statusCode + " while retrieving bitmap from ");
          return null;
          
        }
        
        final HttpEntity entity = response.getEntity();
        if (entity != null)
        {
          InputStream inputStream = null;
          try
          {
            inputStream = entity.getContent();
            bitmap = BitmapFactory.decodeStream(inputStream);
            
          }
          finally
          {
            if (inputStream != null)
            {
              inputStream.close();
            }
            entity.consumeContent();
          }
        }
      }
      catch (Exception e)
      {
        getRequest.abort();
        Log.e("ImageDownloader", "Something went wrong while" + " retrieving bitmap from " + e.toString());
      }
      
      if (bitmap == null)
        return null;
      else
      {
//        File fileCacheItem = new File(context.getFilesDir(), "share.jpg");
        String sdCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String strDBFilePath = sdCardPath + "/Android/data/com.dabeeo.hanhayou/file/.share/";
        File shareFolder = new File(strDBFilePath);
        if (!shareFolder.exists())
          shareFolder.mkdir();
        
        File fileCacheItem = new File(strDBFilePath, "share.jpg");
        if (!fileCacheItem.exists())
          try
          {
            fileCacheItem.createNewFile();
          }
          catch (IOException e1)
          {
            e1.printStackTrace();
          }
        
        fileUrl = fileCacheItem.getAbsolutePath();
        OutputStream out = null;
        
        try
        {
          fileCacheItem.createNewFile();
          out = new FileOutputStream(fileCacheItem);
          
          bitmap.compress(CompressFormat.JPEG, 100, out);
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
        finally
        {
          try
          {
            out.close();
          }
          catch (IOException e)
          {
            e.printStackTrace();
          }
        }
        
        return fileUrl;
      }
    }
    
    
    @Override
    protected void onPostExecute(String result)
    {
      if (result != null)
      {
        Log.w("WARN", "이미지 있음 " + result);
        
        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        Uri imageUri = Uri.parse(result);
        ArrayList<Uri> uris = new ArrayList<Uri>();
        uris.add(imageUri);
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        intent.putExtra(android.content.Intent.EXTRA_TEXT, body);
        intent.setType("image/*");
        
        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(intent, 0);
        if (!resInfo.isEmpty())
        {
          for (ResolveInfo info : resInfo)
          {
            if (info.activityInfo.packageName.toLowerCase().equals("com.sina.weibo"))
            {
              intent.setPackage(info.activityInfo.packageName);
              break;
            }
          }
          
          context.startActivity(Intent.createChooser(intent, "Share"));
        }
      }
      super.onPostExecute(result);
    }
  }
}
