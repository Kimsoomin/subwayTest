package com.dabeeo.hangouyou.activities.mypage.sub;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.managers.network.ApiClient;
import com.dabeeo.hangouyou.managers.network.NetworkResult;

public class AuthEmailActivity extends Activity implements OnClickListener
{
  private EditText editText;
  @SuppressWarnings("unused")
  private Button btnReSend;
  private Button btnAuth;
  private Button btnAuthCancel;
  private String userSeq = null;
  private RelativeLayout progressLayout;
  
  private ApiClient apiClient;
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_auth_email);
    
    apiClient = new ApiClient(this);
    
    Intent intent = getIntent();
    if(intent.hasExtra("userSeq"))
      userSeq = intent.getStringExtra("userSeq");
    
    progressLayout = (RelativeLayout) findViewById(R.id.progressLayout);
        
    btnAuth = (Button) findViewById(R.id.btn_auth);
    btnAuth.setOnClickListener(this);
    btnAuthCancel = (Button) findViewById(R.id.btn_auth_cancel);
    btnAuthCancel.setOnClickListener(this);
    btnReSend = (Button) findViewById(R.id.btn_re_send);
    editText = (EditText) findViewById(R.id.edit_text);
    TextWatcher watcher = new TextWatcher()
    {
      @Override
      public void afterTextChanged(Editable s)
      {
      }
      
      
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after)
      {
      }
      
      
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count)
      {
        if (editText.getText().toString().length() > 1)
        {
          btnAuth.setEnabled(true);
          btnAuth.setActivated(true);
        }
        else
        {
          btnAuth.setEnabled(false);
          btnAuth.setActivated(false);
        }
      }
    };
    editText.addTextChangedListener(watcher);
  }

  @Override
  public void onClick(View v)
  {
    switch (v.getId()) 
    {
      case R.id.btn_auth:
        new emailAuthCheckTask().execute();
        break;
        
      case R.id.btn_auth_cancel:
        finish();
        break;
    }
  }
  
  private class emailAuthCheckTask extends AsyncTask<Void, Void, NetworkResult>
  {
    
    @Override
    protected void onPreExecute()
    {
      progressLayout.setVisibility(View.VISIBLE);
      progressLayout.bringToFront();
      super.onPreExecute();
    }
    
    @Override
    protected NetworkResult doInBackground(Void... params)
    {
      return apiClient.userEmailKeycheck(userSeq, editText.getText().toString());
    }
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      progressLayout.setVisibility(View.GONE);
      if(!result.isSuccess)
      {
        
      }
      super.onPostExecute(result);
    }
  }
}
