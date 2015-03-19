package com.dabeeo.hangouyou.activities.mainmenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.mypage.sub.MyPlaceActivity;
import com.dabeeo.hangouyou.bases.BaseNavigationTabActivity;
import com.dabeeo.hangouyou.beans.TitleCategoryBean;
import com.dabeeo.hangouyou.controllers.mainmenu.FamousPlaceViewPagerAdapter;
import com.dabeeo.hangouyou.managers.NetworkManager;

public class FamousPlaceActivity extends BaseNavigationTabActivity
{
  private FamousPlaceViewPagerAdapter adapter;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    
    adapter = new FamousPlaceViewPagerAdapter(this, getSupportFragmentManager());
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
  
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    getMenuInflater().inflate(R.menu.menu_my_place, menu);
    return super.onCreateOptionsMenu(menu);
  }
  
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    if (item.getItemId() == R.id.my_place)
      startActivity(new Intent(FamousPlaceActivity.this, MyPlaceActivity.class));
    return super.onOptionsItemSelected(item);
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
      Log.e("FamousPlaceActivity.java | onErrorResponse", "|" + e.getLocalizedMessage() + "|" + e.getMessage() + "|");
      displayTitles();
    }
  };
}
