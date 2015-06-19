package com.dabeeo.hanhayou.map;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

@SuppressWarnings("serial")
public class DrawableCache extends HashMap<Integer, SoftReference<Drawable>>
{
	public SoftReference<Drawable> get(Context context, Integer key)
	{
		if (super.get(key) == null || super.get(key).get() == null)
		{
			// cache is empty. create a thumbnail image.
			Drawable drawable = null;

			if ((Integer) key > 0)
			{
//				drawable = context.getResources().getDrawable(key);

				drawable = GetDrawable(context, key);
			}

			if (drawable != null)
			{
				put(key, new SoftReference<Drawable>(drawable));
			}
			else 
			{
				Log.e("ERROR", "context.getResources().getDrawable(" + key + ") return null");
				
				return null;
			}
		}
		return super.get(key);
	}

	static public Drawable GetDrawable(Context context, Integer key)
	{
		Bitmap bitmap;
		InputStream stream = null;
		try
		{
			stream = context.getResources().openRawResource(key);
			
			if(stream == null)
				return null;
			
		    bitmap = BitmapFactory.decodeStream(stream);
		    
		    if(bitmap == null)
		    {
		    	return context.getResources().getDrawable(key);
//		    	Global.GC();
//		    	return null;
		    }
		    
		    BitmapDrawable d = new BitmapDrawable(bitmap);
		    
		    bitmap = null;
		    
		    return d;
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
		    try
		    {
		    	if(stream != null)
		    	{
		    		stream.close();
			    	stream = null;
			    }
	    	} 
		    catch(IOException e)
		    {
		    	e.printStackTrace();
		    }
		}

		return null;
	}
}
