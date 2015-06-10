package com.dabeeo.hangouyou.activities.mypage.sub;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.dabeeo.hangouyou.activities.sub.CongratulateJoinActivity;
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
  
  public Context mContext;
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_auth_email);
    
    mContext = this;
    apiClient = new ApiClient(mContext);
    
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
  
  public void CreateAlert(String message, final boolean isSuccess)
  {
    AlertDialog.Builder ab = new AlertDialog.Builder(this);
    ab.setTitle(R.string.term_alert);
    ab.setPositiveButton(R.string.term_ok,
        new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) 
      {
        dialog.dismiss();
        if(isSuccess)
        {
          startActivity(new Intent(mContext, CongratulateJoinActivity.class));
        }
      }
    });
    
    ab.setMessage(message);
    ab.show();
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
      String status = null;
      try
      {
        JSONObject jsonObject = new JSONObject(result.response);
        if (jsonObject.has("status"))
        {
          status = jsonObject.getString("status");
        }
      }
      catch (JSONException e)
      {
        status = "";
        e.printStackTrace();
      }
      
      if(status.equals("OK"))
      {
        CreateAlert(getString(R.string.msg_complete_auth), true);
      }else
      {
        CreateAlert(getString(R.string.msg_not_correct_email_auth), false);
      }
      
      super.onPostExecute(result);
    }
  }
}
