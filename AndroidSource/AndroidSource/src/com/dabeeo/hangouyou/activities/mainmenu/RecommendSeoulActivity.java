package com.dabeeo.hangouyou.activities.mainmenu;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

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
  private ArrayList<TitleCategoryBean> spheres = new ArrayList<>();
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    
    adapter = new RecommendSeoulViewPagerAdapter(getApplicationContext(), getSupportFragmentManager());
    viewPager.setAdapter(adapter);
    
    adapter.add(new TitleCategoryBean("추천서울", -1));
    adapter.add(new TitleCategoryBean("명소", 8));
    adapter.add(new TitleCategoryBean("쇼핑", 9));
    adapter.add(new TitleCategoryBean("레스토랑", 4));
    adapter.notifyDataSetChanged();
    
    for (int i = 0; i < adapter.getCount(); i++)
    {
      getSupportActionBar().addTab(getSupportActionBar().newTab().setText(adapter.getPageTitle(i)).setTabListener(tabListener));
    }
    
    String url = getString(R.string.server_address) + "store_spheres.json";
    JsonArrayRequest request = new JsonArrayRequest(url, titleListener, errorListener);
    NetworkManager.instance(this).call(request);
  }
  
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    getMenuInflater().inflate(R.menu.menu_recommend_seoul, menu);
    return super.onCreateOptionsMenu(menu);
  }
  
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    if (item.getItemId() == R.id.all)
      showSphereDialog();
    
    return super.onOptionsItemSelected(item);
  }
  
  
  private void showSphereDialog()
  {
    
    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice);
    
    for (TitleCategoryBean bean : spheres)
    {
      arrayAdapter.add(bean.title);
    }
    
    AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
    builderSingle.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
    {
      @Override
      public void onClick(DialogInterface dialog, int which)
      {
        dialog.dismiss();
      }
    });
    
    builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener()
    {
      @Override
      public void onClick(DialogInterface dialog, int which)
      {
        String strName = arrayAdapter.getItem(which);
        Toast.makeText(getApplicationContext(), strName, Toast.LENGTH_SHORT).show();
      }
    });
    builderSingle.show();
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
          spheres.add(new TitleCategoryBean(obj.getString("title"), obj.getInt("id")));
        }
      }
      catch (JSONException e)
      {
        e.printStackTrace();
      }
    }
  };
  
  private Response.ErrorListener errorListener = new Response.ErrorListener()
  {
    @Override
    public void onErrorResponse(VolleyError e)
    {
      Log.e("RecommendSeoulActivity.java | onErrorResponse", "|" + e.getLocalizedMessage() + "|" + e.getMessage() + "|");
    }
  };
}
