package com.dabeeo.hanhayou.managers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class FileManager
{
  public static final String FILE_MY_PLAN = "my_plan.txt";
  public static final String FILE_MY_PLACE = "my_place.txt";
  private volatile static FileManager instance;
  private Context context;
  
  
  public static FileManager getInstance(Context context)
  {
    if (instance == null)
    {
      synchronized (FileManager.class)
      {
        if (instance == null)
          instance = new FileManager(context);
      }
    }
    return instance;
  }
  
  
  private FileManager(Context context)
  {
    this.context = context;
  }
  
  
  private void clear()
  {
    deleteFile(FileManager.FILE_MY_PLACE);
    deleteFile(FileManager.FILE_MY_PLAN);
  }
  
  
  private void deleteAndWriteFile(String fileName, String content)
  {
    deleteFile(fileName);
    writeFile(fileName, content);
  }
  
  
  private String readFile(String fileName)
  {
    //Get the text file
    File file = new File(context.getFilesDir(), fileName);
    
    //Read text from file
    StringBuilder text = new StringBuilder();
    
    try
    {
      BufferedReader br = new BufferedReader(new FileReader(file));
      String line;
      
      while ((line = br.readLine()) != null)
      {
        text.append(line);
        text.append('\n');
      }
      br.close();
    }
    catch (IOException e)
    {
    }
    Log.w("WARN", "Read File : \n" + text.toString());
    return text.toString();
  }
  
  
  private void writeFile(String fileName, String content)
  {
    try
    {
      File file = new File(context.getFilesDir(), fileName);
      if (!file.exists())
        file.createNewFile();
      FileOutputStream fOut = new FileOutputStream(file);
      OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
      myOutWriter.append(content);
      myOutWriter.close();
      fOut.close();
    }
    catch (Exception e)
    {
      Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
    }
    readFile(fileName);
  }
  
  
  public void deleteFile(String fileName)
  {
    try
    {
      File file = new File(context.getFilesDir(), fileName);
      Log.w("WARN", "파일삭제 존재여부? : " + fileName + " / " + file.exists());
      if (file.exists())
        file.delete();
    }
    catch (Exception e)
    {
      Log.w("WARN", "파일삭제 에러 : " + e.toString());
      Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
    }
  }
}
