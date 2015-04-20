package com.dabeeo.hangouyou.map;

import android.util.Log;

public class BlinkingCommon {

	static boolean Log_on = true;

	public static void smlLibDebug(String title, String content) 
	{
		if (Log_on) 
		{
			// StackTraceElement[] trace = new Throwable().getStackTrace();
			StackTraceElement[] trace = new UnknownError().getStackTrace();

			String msg = "";

			if (trace.length >= 1) 
			{
				StackTraceElement elt = trace[1];

//				 msg = elt.getFileName() + ": " + elt.getClassName() + "." +
//				 elt.getMethodName() + "(line " + elt.getLineNumber() + "): "
//				 + content;
				 msg = "[" + elt.getFileName() + " Line:" +
				 elt.getLineNumber() + "] " + elt.getMethodName() + " " +
				 content;
//				msg = "[" + " Line:" + elt.getLineNumber() + "] ["
//						+ elt.getMethodName() + "] " + content;
				Log.i(title, msg);
			} else 
			{
				Log.i(title, msg);
			}
		}
	}

	public static void smlLibPrintException(String title, String content) 
	{
		if (Log_on) 
		{
			// StackTraceElement[] trace = new Throwable().getStackTrace();
			StackTraceElement[] trace = new UnknownError().getStackTrace();
			String msg = "";

			if (trace.length >= 1) 
			{
				StackTraceElement elt = trace[1];

				 msg = elt.getFileName() + ": " + elt.getClassName() + "." +
				 elt.getMethodName() + "(line " + elt.getLineNumber() + "): "
				 + content;
				// msg = "Warning!!! [" + elt.getFileName() + " Line:" +
				// elt.getLineNumber() + "] " + elt.getMethodName() + " " +
				// content;
//				msg = "Warning!!! [" + " Line:" + elt.getLineNumber() + "] ["
//						+ elt.getMethodName() + "] " + content;
				Log.e(title, msg);
			} else 
			{
				Log.e(title, msg);
			}
		}
	}

}
