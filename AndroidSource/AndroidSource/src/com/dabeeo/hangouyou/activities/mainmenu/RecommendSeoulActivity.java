package com.dabeeo.hangouyou.activities.mainmenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.bases.BaseNavigationTabActivity;
import com.dabeeo.hangouyou.beans.TitleCategoryBean;
import com.dabeeo.hangouyou.controllers.mainmenu.RecommendSeoulViewPagerAdapter;
import com.dabeeo.hangouyou.managers.NetworkManager;

public class RecommendSeoulActivity extends BaseNavigationTabActivity
{
  private RecommendSeoulViewPagerAdapter adapter;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    
    adapter = new RecommendSeoulViewPagerAdapter(getApplicationContext(), getSupportFragmentManager());
    TitleCategoryBean bean = new TitleCategoryBean("전체", -1);
    adapter.add(bean);
    viewPager.setAdapter(adapter);
    
    String url = getString(R.string.server_address) + "store_spheres.json";
    JsonArrayRequest request = new JsonArrayRequest(url, titleListener, errorListener);
    NetworkManager.instance(this).call(request);
  }
  
  
  @SuppressWarnings("deprecation")
  private void displayTitles()
  {
    for (int i = 0; i < adapter.getCount(); i++)
    {
      getSupportActionBar().addTab(getSupportActionBar().newTab().setText(adapter.getPageTitle(i)).setTabListener(tabListener));
    }
  }
  
  /**************************************************
   * listener
   ***************************************************/
  private Response.Listener<JSONArray> titleListener = new Response.Listener<JSONArray>()
  {
    @Override
    public void onResponse(JSONArray jsonArray)
    {
      try
      {
        for (int i = 0; i < jsonArray.length(); i++)
        {
          JSONObject obj = jsonArray.getJSONObject(i);
          adapter.add(new TitleCategoryBean(obj.getString("title"), obj.getInt("id")));
        }
        adapter.notifyDataSetChanged();
      }
      catch (JSONException e)
      {
        e.printStackTrace();
      }
      
      displayTitles();
    }
  };
  
  private Response.ErrorListener errorListener = new Response.ErrorListener()
  {
    @Override
    public void onErrorResponse(VolleyError e)
    {
      Log.e("RecommendSeoulActivity.java | onErrorResponse", "|" + e.getLocalizedMessage() + "|" + e.getMessage() + "|");
      displayTitles();
    }
  };
}
