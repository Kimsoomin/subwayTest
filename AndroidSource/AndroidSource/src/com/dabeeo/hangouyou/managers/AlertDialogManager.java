package com.dabeeo.hangouyou.managers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import com.dabeeo.hangouyou.R;

public class AlertDialogManager
{
	public AlertDialog dialog;
	private ProgressDialog progressDialog;
	private Activity context;
	
	
	public AlertDialogManager(Activity context)
	{
		this.context = context;
	}
	
	
	public void dismiss()
	{
		dialog.dismiss();
	}
	
	
	public void showDontNetworkConnectDialog()
	{
		Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(context.getString(R.string.term_alert));
		builder.setMessage(context.getString(R.string.msg_dont_connect_network));
		builder.setCancelable(false);
		builder.setPositiveButton(android.R.string.ok, null);
		dialog = builder.create();
		dialog.show();
	}
	
	
	public void showAlertDialog(String title, String message, String okString, String cancelString, final AlertListener listener)
	{
		if (dialog != null)
		{
			if (dialog.isShowing())
				dialog.dismiss();
			
			dialog = null;
		}
		Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setCancelable(false);
		builder.setPositiveButton(android.R.string.ok, new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				if (listener != null)
					listener.onPositiveButtonClickListener();
				
				dialog.dismiss();
			}
		});
		builder.setNegativeButton(android.R.string.cancel, new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				if (listener != null)
					listener.onNegativeButtonClickListener();
				
				dialog.dismiss();
			}
		});
		dialog = builder.create();
		dialog.show();
	}
	
	
	public void showProgressDialog(String title, String message)
	{
		if (progressDialog == null)
		{
			progressDialog = new ProgressDialog(context);
			progressDialog.setTitle(title);
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(false);
		}
		progressDialog.setMessage(message);
		
		if (!progressDialog.isShowing())
			progressDialog.show();
	}
	
	
	public void hideProgressDialog()
	{
		if (progressDialog != null && progressDialog.isShowing())
		{
			try
			{
				progressDialog.dismiss();
				progressDialog = null;
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public interface AlertListener
	{
		public void onPositiveButtonClickListener();
		
		
		public void onNegativeButtonClickListener();
	}
}
