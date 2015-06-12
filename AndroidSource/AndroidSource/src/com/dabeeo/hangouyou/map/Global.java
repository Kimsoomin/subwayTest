package com.dabeeo.hangouyou.map;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Debug;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

// 공통 사용 클레스
public class Global
{
  public static String g_strMapDBFileName = "gs_seoul.mbtiles";
  public static String HangouyouDBFileName = "hanhayou.sqlite";
  static final String strURLPath = "http://blinkingtour.com/map_data/dabeeo_map_seoul.mbtiles";
  static String strSetLanguage = null;
  static String strMapDBFilePath;
  static String strMapDBFilePath2;
  static Display g_iDisplay;
  // static final String g_iCityCode = "695";
  static final String g_iCityCode = "7592";
  
  /**
   * - ver1.5 추가.
   */
  static Activity g_mainActivity = null;
  static boolean g_IsHomeNow = false; // 홈으로 나갔을때의 상태 체크.
  static boolean bShowMapAlert = true; // 맵 데이터 알림 팝업.
  
  static boolean bShowUpdateAlert = true; // 업데이트 데이터 알림 팝업.
  static int UPATE_STATE = 0;
  static final int UPDATE_STOP = 0;
  static final int UPDATE_MORE_PIC = 1;
  static final int UPDATE_MORE_REVIEW = 2;
  static final int UPDATE_MORE_ALL = 3;
  static final int UPDATE_PROGRESS = 4;
  
  // - 리뷰 adapter에서 구분을 위한 상수.
  static final int COURSE = 0;
  static final int PLACE = 1;
  static final int MYPLAN = 2;
  
  static DrawableCache g_drawableCache = new DrawableCache();
  
  
  // URL을 보내서 결과값을 JSON으로 리턴..
  // ※ MainThread에서 실행하면 안됨!
  static public JSONObject SendURLRequest(String strURL) throws Exception
  {
    // Log.i("SendURLRequest", strURL);
    
    StringBuilder builder = new StringBuilder();
    
    HttpParams httpParameters = new BasicHttpParams();
    HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
    HttpConnectionParams.setSoTimeout(httpParameters, 10000);
    
    HttpClient client = new DefaultHttpClient(httpParameters);
    HttpGet httpGet = new HttpGet(strURL);
    try
    {
      HttpResponse response = client.execute(httpGet);
      StatusLine statusLine = response.getStatusLine();
      int statusCode = statusLine.getStatusCode();
      if (statusCode == 200)
      {
        HttpEntity entity = response.getEntity();
        InputStream content = entity.getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
        String line;
        while ((line = reader.readLine()) != null)
        {
          builder.append(line);
        }
      }
      else
      {
        Log.e("ERROR", "[HTTP GET] ret code = " + statusCode);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throw e;
    }
    
    try
    {
      JSONObject jsonObject = new JSONObject(builder.toString());
      return jsonObject;
    }
    catch (JSONException e)
    {
      e.printStackTrace();
    }
    
    return null;
  }
  
  
  static public JSONObject SendURLRequest(String strURL, HashMap<String, String> params) throws Exception
  {
    Iterator<String> iter = params.keySet().iterator();
    
    StringBuffer sb = new StringBuffer(strURL);
    
    while (iter.hasNext())
    {
      String strKey = iter.next();
      
      String strValue = params.get(strKey);
      
      if (strValue == null)
      {
        Log.e("ERROR", "[SendURLRequest] key: " + strKey);
        continue;
      }
      sb.append("&" + strKey + "=" + URLEncoder.encode(strValue));
    }
    
    String url = sb.toString();
    
    return Global.SendURLRequest(url);
  }
  
  
  // 이건 POST 방식으로 요청함.
  static public JSONObject SendURLRequest(int nCode, HashMap<String, String> params) throws Exception
  {
    String strJSON = null;
    
    try
    {
      List<NameValuePair> qparams = new ArrayList<NameValuePair>();
      // qparams.add(new BasicNameValuePair("LanguageCode", "eng"));
      // qparams.add(new BasicNameValuePair("CityCode", "695"));
      qparams.add(new BasicNameValuePair("mode", String.valueOf(nCode)));
      
      Iterator<String> iter = params.keySet().iterator();
      
      while (iter.hasNext())
      {
        String strKey = iter.next();
        
        String strValue = params.get(strKey);
        
        if (strValue == null)
        {
          Log.e("ERROR", "[SendURLRequest] key: " + strKey);
          continue;
        }
        
        qparams.add(new BasicNameValuePair(strKey, strValue));
      }
      
      HttpPost post = new HttpPost("http://www.blinkingtour.com/jsonApi2.action");
      
      HttpClient client = new DefaultHttpClient();
      
      UrlEncodedFormEntity ent = new UrlEncodedFormEntity(qparams, HTTP.UTF_8);
      post.setEntity(ent);
      HttpResponse responsePOST = client.execute(post);
      HttpEntity resEntity = responsePOST.getEntity();
      if (resEntity != null)
      {
        strJSON = EntityUtils.toString(resEntity);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throw e;
    }
    
    try
    {
      JSONObject jsonObject = new JSONObject(strJSON);
      return jsonObject;
    }
    catch (JSONException e)
    {
      e.printStackTrace();
    }
    
    return null;
  }
  
  
  // strPath는 DB에 들어있는 경로값 : Ex) /Pics/Attraction_Pics/656_1.jpg
  // 이미지가 로컬에 있으면 이미지를 리턴
  // inSampleSize는 1/n 만큼 줄여서 이미지를 로딩함. 2, 4, 8, 16.. 일 때 가장 빠르다고함.
  static Bitmap GetImageFromPath(String strPath, int inSampleSize)
  {
    String strLocalFilePath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/.BlinkingSeoul2" + strPath;
    
    if (new File(strLocalFilePath).exists() == false)
    {
      // Log.e("ERROR", "File not exists : " + strLocalFilePath);
      return null;
    }
    
    String strDir = strLocalFilePath.substring(0, strLocalFilePath.lastIndexOf("/"));
    // 폴더 생성
    new File(strDir).mkdirs();
    
    if (inSampleSize > 0)
    {
      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inSampleSize = inSampleSize;
      
      SoftReference<Bitmap> myBitmap = new SoftReference<Bitmap>(BitmapFactory.decodeFile(strLocalFilePath, options));
      
      return myBitmap.get();
    }
    
    // 원본 디코딩
    SoftReference<Bitmap> myBitmap = new SoftReference<Bitmap>(BitmapFactory.decodeFile(strLocalFilePath));
    
    if (myBitmap.get() == null)
    {
      Log.e("ERROR", "BitmapFactory.decodeFile return null : " + strLocalFilePath);
    }
    
    return myBitmap.get();
  }
  
  
  static Bitmap GetReviewImageFromPath(String strPath, int inSampleSize)
  {
    if (new File(strPath).exists() == false)
    {
      // Log.e("ERROR", "File not exists : " + strLocalFilePath);
      return null;
    }
    
    String strDir = strPath.substring(0, strPath.lastIndexOf("/"));
    // 폴더 생성
    new File(strDir).mkdirs();
    
    if (inSampleSize > 0)
    {
      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inSampleSize = inSampleSize;
      
      SoftReference<Bitmap> myBitmap = new SoftReference<Bitmap>(BitmapFactory.decodeFile(strPath, options));
      
      return myBitmap.get();
    }
    
    // 원본 디코딩
    SoftReference<Bitmap> myBitmap = new SoftReference<Bitmap>(BitmapFactory.decodeFile(strPath));
    
    if (myBitmap.get() == null)
    {
      Log.e("ERROR", "BitmapFactory.decodeFile return null : " + strPath);
    }
    
    return myBitmap.get();
  }
  
  
  static Bitmap GetImageFromUri(String uri, int inSampleSize)
  {
    if (new File(uri).exists() == false)
    {
      // Log.e("ERROR", "File not exists : " + uri);
      return null;
    }
    
    // String strDir = uri.substring(0, uri.lastIndexOf("/"));
    // 폴더 생성
    // new File(strDir).mkdirs();
    
    if (inSampleSize > 0)
    {
      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inSampleSize = inSampleSize;
      
      SoftReference<Bitmap> myBitmap = new SoftReference<Bitmap>(BitmapFactory.decodeFile(uri, options));
      
      return myBitmap.get();
    }
    
    // 원본 디코딩
    
    SoftReference<Bitmap> myBitmap = new SoftReference<Bitmap>(BitmapFactory.decodeFile(uri));
    
    if (myBitmap.get() == null)
    {
      Log.e("ERROR", "BitmapFactory.decodeFile return null : " + uri);
    }
    
    return myBitmap.get();
  }
  
  
  // strPath는 DB에 들어있는 경로값 : Ex) /Pics/Attraction_Pics/656_1.jpg
  // 이미지를 로컬에 저장 -> MainThread에서 실행하면 안됨..
  static boolean DownloadImage(String url, String fileName) throws MalformedURLException, IOException
  {
    try
    {
      String strURL = url;
      // String strLocalFilePath = strDir + "/" + strPath.replace("/",
      // "+");
      String strLocalFilePath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/Hangouyou/.temp/" + fileName;
      
      String strDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/Hangouyou/.temp";
      // 폴더 생성
      new File(strDir).mkdirs();
      
      InputStream inputStream = new URL(strURL).openStream();
      
      File file = new File(strLocalFilePath);
      
      // 파일 삭제
      //      file.delete();
      
      OutputStream out = new FileOutputStream(file);
      
      byte[] buffer = new byte[1024 * 4];
      int i = 0;
      while ((i = inputStream.read(buffer)) != -1)
        out.write(buffer, 0, i);
      
      out.flush();
      out.close();
      
      return true;
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
    return false;
  }
  
  
  // 경로 생성 -> "sdcard/폴더명"
  public static String GetPathWithSDCard()
  {
    String sdCardPath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
    
    String strPath = sdCardPath + "/Hangouyou/";
    
    //    if (subPath.substring(0, 1).compareTo("/") != 0)
    //    {
    //      strPath += "/";
    //    }
    
    //    strPath += 
    
    return strPath;
  }
  
  
  // 폴더 안에 초기화를 위한 파일 삭제. - root 폴더를 지우면 안되나?
  static public boolean deleteFolder(File targetFolder)
  {
    File[] childFile = targetFolder.listFiles();
    
    if (childFile == null)
      return true;
    
    boolean confirm = false;
    int size = childFile.length;
    
    if (size > 0)
    {
      for (int i = 0; i < size; i++)
      {
        if (childFile[i].isFile())
        {
          confirm = childFile[i].delete();
          System.out.println(childFile[i] + ":" + confirm + " 삭제");
        }
        else
        {
          deleteFolder(childFile[i]);
        }
      }
    }
    
    targetFolder.delete();
    
    System.out.println(targetFolder + " 폴더삭제됨삭제");
    System.out.println(targetFolder + ":" + confirm + " 삭제");
    
    return (!targetFolder.exists());
  }
  
  
  public static Drawable ResizeDrawable(Resources resources, int nID, int width, int height)
  {
    Drawable d = resources.getDrawable(nID);
    
    if (d instanceof BitmapDrawable)
    {
      Bitmap bm = ((BitmapDrawable) d).getBitmap();
      
      return new BitmapDrawable(resources, Bitmap.createScaledBitmap(bm, width, height, true));
    }
    
    NinePatchDrawable nd = (NinePatchDrawable) d;
    
    nd.setBounds(0, 0, width * 2, height * 2);
    
    Bitmap bmp = Bitmap.createBitmap(width * 2, height * 2, Bitmap.Config.ARGB_4444);
    Canvas canvas = new Canvas(bmp);
    nd.draw(canvas);
    
    bmp = Bitmap.createScaledBitmap(bmp, width, height, true);
    
    return new BitmapDrawable(bmp);
  }
  
  
  public static Drawable ResizeDrawable(Context context, int nID, int width, int height)
  {
    Drawable d = GetDrawable(context, nID);
    
    if (d instanceof BitmapDrawable)
    {
      Bitmap bm = ((BitmapDrawable) d).getBitmap();
      
      return new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(bm, width, height, true));
    }
    
    NinePatchDrawable nd = (NinePatchDrawable) d;
    
    nd.setBounds(0, 0, width * 2, height * 2);
    
    Bitmap bmp = Bitmap.createBitmap(width * 2, height * 2, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bmp);
    nd.draw(canvas);
    
    bmp = Bitmap.createScaledBitmap(bmp, width, height, true);
    
    return new BitmapDrawable(bmp);
  }
  
  
  public static Bitmap fitImageSize(Context _context, int _res, int maxWidth, int maxHeight)
  {
    
    Bitmap bm = null;
    
    final BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    options.inPreferredConfig = Config.RGB_565;
    BitmapFactory.decodeResource(_context.getResources(), _res, options);
    
    int width = options.outWidth;
    int height = options.outHeight;
    
    double scalex = ((double) maxWidth / (double) width);
    double scaley = ((double) maxHeight / (double) height);
    
    double scale = (scalex < scaley) ? scalex : scaley;
    if (scale > 1)
      scale = 1;
    
    //		// Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false;
    bm = new SoftReference<Bitmap>(BitmapFactory.decodeResource(_context.getResources(), _res, options)).get();
    
//    if (width > maxWidth || height > maxHeight)
//    {
      Bitmap resizebm = Bitmap.createScaledBitmap(bm, ((int) (scale * width)), ((int) (scale * height)), true);
      
      SoftReference<Bitmap> myBitmap = new SoftReference<Bitmap>(resizebm);
      
      return myBitmap.get();
//    }
//    else
//    {
//      SoftReference<Bitmap> myBitmap = new SoftReference<Bitmap>(bm);
//      return myBitmap.get();
//    }
  }
  
  
  // 사진을 임시 폴더에 저장함.
  //  public static String SaveTempPicture(Bitmap bmp)
  //  {
  //    String strTempDir = GetPathWithSDCard(".BlinkingSeoul/temp");
  //    
  //    if (new File(strTempDir).exists() == false)
  //    {
  //      new File(strTempDir).mkdirs();
  //    }
  //    
  //    String strTempPath = strTempDir + "/" + UUID.randomUUID().toString() + ".jpg";
  //    
  //    File file = new File(strTempPath);
  //    try
  //    {
  //      file.createNewFile();
  //      
  //      FileOutputStream os = new FileOutputStream(file);
  //      bmp.compress(Bitmap.CompressFormat.JPEG, 100, os);
  //      
  //      try
  //      {
  //        // 이미지를 상황에 맞게 회전시킨다
  //        ExifInterface exif = new ExifInterface(strTempPath);
  //        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
  //        int exifDegree = exifOrientationToDegrees(exifOrientation);
  //        bmp = rotate(bmp, exifDegree);
  //        
  //      }
  //      catch (Exception e)
  //      {
  //      }
  //      
  //      os.flush();
  //      os.close();
  //    }
  //    catch (Exception e)
  //    {
  //      e.printStackTrace();
  //      return null;
  //    }
  //    
  //    return strTempPath;
  //  }
  
  // ver 1.5 추가
  
  /**
   * EXIF정보를 회전각도로 변환하는 메서드
   * 
   * @param exifOrientation
   *          EXIF 회전각
   * @return 실제 각도
   */
  public static int exifOrientationToDegrees(int exifOrientation)
  {
    if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90)
    {
      return 90;
    }
    else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180)
    {
      return 180;
    }
    else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270)
    {
      return 270;
    }
    return 0;
  }
  
  
  /**
   * 이미지를 회전시킵니다.
   * 
   * @param bitmap
   *          비트맵 이미지
   * @param degrees
   *          회전 각도
   * @return 회전된 이미지
   */
  public static Bitmap rotate(Bitmap bitmap, float degrees)
  {
    if (degrees != 0 && bitmap != null)
    {
      Matrix m = new Matrix();
      m.setRotate(degrees, (float) bitmap.getWidth(), (float) bitmap.getHeight());
      
      try
      {
        Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
        WeakReference<Bitmap> weak = new WeakReference<Bitmap>(converted);
        converted = weak.get();
        
        bitmap.recycle();
        bitmap = converted;
      }
      catch (OutOfMemoryError ex)
      {
        ex.printStackTrace();
        // 메모리가 부족하여 회전을 시키지 못할 경우 그냥 원본을 반환합니다.
      }
    }
    return bitmap;
  }
  
  
  /** 메모리 관리 및 체크 - 전달 받거나 줄 데이터가 있다면 하지말자. */
  public static void GC()
  {
    System.gc();
    
    // float fFree = Runtime.getRuntime().freeMemory() / (1024*1024);
    // float fTotal = + Runtime.getRuntime().totalMemory() / (1024*1024);
    
    Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
    Debug.getMemoryInfo(memoryInfo);
    
    String memMessage = String.format("Memory: Pss=%.2f MB, Private=%.2f MB, Shared=%.2f MB", memoryInfo.getTotalPss() / 1024.0, memoryInfo.getTotalPrivateDirty() / 1024.0,
        memoryInfo.getTotalSharedDirty() / 1024.0);
    
    Log.e("MEMORY", memMessage);
    // Log.e("MEMORY",
    // String.format("#### [Free] %.2f MB / [Total] %.2f MB", fFree,
    // fTotal));
  }
  
  
  public static Point GetSubwayCoordinate(int nSubwayCode)
  {
    String sdCardPath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
    String strDir = sdCardPath + "/.BlinkingSeoul2";
    String strDBFilePath = strDir + "/subway.sqlite";
    
    Point ptRet = null;
    
    try
    {
      SQLiteDatabase db = SQLiteDatabase.openDatabase(strDBFilePath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READONLY);
      
      String strQuery = String.format("SELECT * FROM subway where subway_code = %d", nSubwayCode);
      
      // Log.i("SQL", strQuery);
      
      Cursor cursor = db.rawQuery(strQuery, null);
      
      if (cursor.moveToNext())
      {
        ptRet = new Point();
        
        ptRet.x = cursor.getInt(1);
        ptRet.y = cursor.getInt(2);
      }
      db.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
    return ptRet;
  }
  
  
  public static void CopyAsset(Context context, String strAssetFileName, String strTargetFileName)
  {
    String sdCardPath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
    String strDBFilePath = sdCardPath + "/.BlinkingSeoul/" + strTargetFileName;
    
    if (new File(strDBFilePath).exists() == true)
      return;
    
    // Log.i("ASSET", "파일 복사 : " + strAssetFileName + "  >> " +
    // strTargetFileName);
    
    AssetManager am = context.getResources().getAssets();
    
    String strDir = strDBFilePath.substring(0, strDBFilePath.lastIndexOf("/"));
    
    // 폴더 생성
    new File(strDir).mkdirs();
    
    try
    {
      InputStream is = am.open(strAssetFileName);
      OutputStream out = new FileOutputStream(new File(strDBFilePath));
      
      int size = is.available();
      
      if (size > 0)
      {
        byte[] data = new byte[size];
        is.read(data);
        
        out.write(data);
      }
      out.flush();
      out.close();
      
      is.close();
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  
  /** Drawalbe 객체 생성 */
  public static Drawable GetDrawable(Context context, int nDrawableID)
  {
    try
    {
      SoftReference<Drawable> sr = g_drawableCache.get(context, nDrawableID);
      
      if (sr == null)
        return null;
      
      Drawable d = sr.get();
      
      return d;
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
    return null;
  }
  
  
  public static Drawable getDrawableWeekRefer(Context context, int nDrawableID)
  {
    try
    {
      
      WeakReference<Drawable> drawable = new WeakReference<Drawable>(context.getResources().getDrawable(nDrawableID));
      Drawable d = drawable.get();
      return d;
      
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return null;
  }
  
  
  /** 네트워크 Checking Module (3) */
  public static boolean IsNetworkConnected(Context c, boolean bShowAlert, DialogInterface.OnClickListener listener)
  {
    // Wifi & Mobile Check. 사용 가능하고, 연결이 되어 있으면 통과.
    ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    if (ni.isAvailable() && ni.isConnected())
      return true;
    
    ni = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
    if (ni.isAvailable() && ni.isConnected())
      return true;
    
    // 연결 안돼있음
    if (bShowAlert == true)
    {
      AlertDialog.Builder b = new AlertDialog.Builder(c);
      AlertDialog ad = b.create();
      ad.setCancelable(false); // This blocks the 'BACK' button
      ad.setMessage("You are currently offline.\nPlease try again when you are online.");
      
      if (listener == null)
      {
        ad.setButton("OK", new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface dialog, int which)
          {
            dialog.dismiss();
            
            // 만약 DB 파일이 없으면 앱 종료시킴
            
            if (new File(Global.GetDatabaseFilePath()).exists() == false)
            {
              System.exit(0);
            }
          }
        });
      }
      else
      {
        ad.setButton("OK", listener);
      }
      
      ad.show();
    }
    
    return false;
  }
  
  
  public static RotateAnimation GetViewRotation(float toDegree, long duration)
  {
    if (toDegree < 0)
      toDegree += 360;
    
    if (toDegree > 360)
      toDegree -= 360;
    
    RotateAnimation r = new RotateAnimation(toDegree, toDegree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    r.setDuration(duration);
    r.setFillAfter(true);
    
    return r;
  }
  
  
  /** 네트워크 Checking Module (2) */
  public static boolean IsNetworkConnected(Context c, boolean bShowAlert)
  {
    return Global.IsNetworkConnected(c, bShowAlert, null);
  }
  
  
  static public boolean DestroyActivity(LocalActivityManager activityManager, String id)
  {
    if (activityManager != null)
    {
      activityManager.destroyActivity(id, false);
      // http://code.google.com/p/android/issues/detail?id=12359
      // http://www.netmite.com/android/mydroid/frameworks/base/core/java/android/app/LocalActivityManager.java
      try
      {
        final Field mActivitiesField = LocalActivityManager.class.getDeclaredField("mActivities");
        if (mActivitiesField != null)
        {
          mActivitiesField.setAccessible(true);
          
          @SuppressWarnings("unchecked")
          final Map<String, Object> mActivities = (Map<String, Object>) mActivitiesField.get(activityManager);
          if (mActivities != null)
          {
            mActivities.remove(id);
          }
          
          final Field mActivityArrayField = LocalActivityManager.class.getDeclaredField("mActivityArray");
          
          if (mActivityArrayField != null)
          {
            mActivityArrayField.setAccessible(true);
            @SuppressWarnings("unchecked")
            final ArrayList<Object> mActivityArray = (ArrayList<Object>) mActivityArrayField.get(activityManager);
            
            if (mActivityArray != null)
            {
              for (Object record : mActivityArray)
              {
                final Field idField = record.getClass().getDeclaredField("id");
                if (idField != null)
                {
                  idField.setAccessible(true);
                  final String _id = (String) idField.get(record);
                  if (id.equals(_id))
                  {
                    mActivityArray.remove(record);
                    break;
                  }
                }
              }
            }
          }
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      return true;
    }
    return false;
  }
  
  
  public static String GetDatabaseFilePath()
  {
    String sdCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    String strDBFilePath = sdCardPath + "/Android/data/com.dabeeo.hanhayou/file/.db/";
    return strDBFilePath;
  }
  
  
  public static String GetImageFilePath()
  {
    String sdCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    String strImageFilePath = sdCardPath + "/Android/data/com.dabeeo.hanhayou/file/.image/";
    return strImageFilePath;
  }
  
  
  // -v1.5 추가.
  
  // - 색 필터 - GlayScale로 이미지의 색을 덮는다.
  public static Drawable setConvertGrayScale(Context context, int nDrawableID)
  {
    Drawable dImage = GetDrawable(context, nDrawableID);
    ColorMatrix matrix = new ColorMatrix();
    matrix.setSaturation(0); // 0이면 grayscale
    ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
    dImage.setColorFilter(cf);
    
    return dImage;
  }
  
  
  // - 알림 - nLayout 의 0 이면 기본 메세지 노출 모드. 0 이 아니면 Layout 정보를 받아야함.
  public static void showUseAlert(Activity thisActivity, String Title, String strMessage, int nLayout)
  {
    
    String strTitle = Title;
    
    if (strTitle == null)
      strTitle = "Notification";
    
    AlertDialog.Builder b = new AlertDialog.Builder(thisActivity.getParent());
    
    // 아래에 커스텀 뷰를 추가 하자면 이 기능을 사용하면 된다.
    if (nLayout > 0)
    {
      LayoutInflater inflater = (LayoutInflater) thisActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      View view = inflater.inflate(nLayout, null);
      b.setView(view);
    }
    else
    {
      TextView tMessage = new TextView(thisActivity);
      // You Can Customise your Message here
      tMessage.setText(strMessage);
      // tMessage.setBackgroundColor(Color.DKGRAY);
      tMessage.setPadding(10, 10, 10, 20);
      tMessage.setGravity(Gravity.CENTER);
      tMessage.setTextColor(Color.WHITE);
      tMessage.setTextSize(16);
      b.setView(tMessage);
    }
    
    TextView title = new TextView(thisActivity);
    // You Can Customise your Title here
    title.setText(strTitle);
    // title.setBackgroundColor(Color.DKGRAY);
    title.setPadding(10, 15, 10, 15);
    title.setGravity(Gravity.CENTER);
    title.setTextColor(Color.WHITE);
    title.setTextSize(20);
    b.setCustomTitle(title);
    
    AlertDialog ad = b.create();
    ad.setCancelable(false); // This blocks the 'BACK' button
    // ad.setTitle("Notification");
    // ad.setMessage(strMessage);
    ad.setButton("OK", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface dialog, int which)
      {
        dialog.dismiss();
      }
    });
    
    ad.show();
    
  }
  
  
  public static String MD5Encoding(String str)
  {
    String MD5 = "";
    try
    {
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(str.getBytes());
      byte byteData[] = md.digest();
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < byteData.length; i++)
      {
        sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
      }
      MD5 = sb.toString();
      
    }
    catch (NoSuchAlgorithmException e)
    {
      e.printStackTrace();
      MD5 = null;
    }
    return MD5;
  }
  
  
  public static int DpToPixel(Context context, int DP)
  {
    float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DP, context.getResources().getDisplayMetrics());
    
    return (int) px;
  }
  
}
