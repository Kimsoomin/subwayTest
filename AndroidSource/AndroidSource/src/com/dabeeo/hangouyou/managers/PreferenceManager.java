package com.dabeeo.hangouyou.managers;

import java.util.ArrayList;

import org.json.JSONArray;

import android.content.Context;
import android.text.TextUtils;

public class PreferenceManager extends BasePreferenceManager
{
	private static PreferenceManager instance;
	
	
	public static PreferenceManager getInstance(Context context)
	{
		if (instance == null)
		{
			synchronized (PreferenceManager.class)
			{
				if (instance == null)
					instance = new PreferenceManager(context);
			}
		}
		return instance;
	}
	
	
	private PreferenceManager(Context context)
	{
		super(context);
	}
	
	private static final String KEY_IS_FIRST = "key_is_first";
	private static final String KEY_IS_ALLOW_POPUP = "key_is_allow_pop_up";
	private static final String KEY_RECENT_SEARCH_WORD = "key_recent_search_word";
	private static final String KEY_USER_SEQ = "key_user_seq";
	
	private static final String KEY_DONT_SHOW_POPUP_DATE = "key_dont_show_popup_date";
	
	
	public void setIsFirst(boolean value)
	{
		put(KEY_IS_FIRST, value);
	}
	
	
	public boolean getIsFirst()
	{
		return get(KEY_IS_FIRST, true);
	}
	
	
	public void setAllowPopup(boolean value)
	{
		put(KEY_IS_ALLOW_POPUP, value);
	}
	
	
	public boolean getAllowPopup()
	{
		return get(KEY_IS_ALLOW_POPUP, true);
	}
	
	
	public void setDontShowPopupDate(String value)
	{
		put(KEY_DONT_SHOW_POPUP_DATE, value);
	}
	
	
	public String getDontShowPopupDate()
	{
		return get(KEY_DONT_SHOW_POPUP_DATE);
	}
	
	
	public void setUserSeq(String value)
	{
		put(KEY_USER_SEQ, value);
	}
	
	
	public String getUserSeq()
	{
		return getWithNullToBlank(KEY_USER_SEQ);
	}
	
	
	public void setRecentSearchWord(String word)
	{
		ArrayList<String> result = getRecentSearchWord();
		if (result.contains(word))
			result.remove(word);
		
		result.add(0, word);
		
		// 최근 3개만 남기기
		for (int i = result.size() - 1; i >= 3; i--)
		{
			result.remove(i);
		}
		
		JSONArray array = new JSONArray();
		for (String string : result)
		{
			array.put(string);
		}
		
		put(KEY_RECENT_SEARCH_WORD, array.toString());
	}
	
	
	public ArrayList<String> getRecentSearchWord()
	{
		ArrayList<String> result = new ArrayList<>();
		
		String json = get(KEY_RECENT_SEARCH_WORD);
		if (TextUtils.isEmpty(json))
			json = "[]";
		
		try
		{
			JSONArray array = new JSONArray(json);
			
			for (int i = 0; i < array.length(); i++)
			{
				result.add(array.getString(i));
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return result;
	}
}
