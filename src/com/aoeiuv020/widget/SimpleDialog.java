/* ***************************************************
	^> File Name: SimpleDialog.java
	^> Author: AoEiuV020
	^> Mail: 490674483@qq.com
	^> Created Time: 2016/03/01 - 15:15:06
*************************************************** */
package com.aoeiuv020.widget;
import android.os.Looper;
import android.os.Message;
import android.os.Handler;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
/**
  *1,阻塞式的dialog,
  *直到点击OK返回true或其他操作返回false;
  *boolean flag=SimpleDialog.show(getContext(),"title","message");
  *2,非阻塞式的dialog,
  *SimpleDialog.show(getContext(),"title","message",null);
  *或者传送SimpleDialog.Status类，可以以后阻塞，
  *SimpleDialog.Status status=new SimpleDialog.Status();
  *SimpleDialog.show(getContext(),"title","message",status);
  *dosomething;
  *boolean flag=status.getStatus();
  *在getStatus()这里阻塞，
 */
public class SimpleDialog implements DialogInterface.OnClickListener,DialogInterface.OnCancelListener
{
	private SimpleDialog()
	{
	}
	public static class Status
	{
		public enum StatusEnum
		{
			BEGIN,OK,CANCEL;
		}
		StatusEnum statusEnum;
		SimpleDialog simpleDialog;
		public Status()
		{
			statusEnum=StatusEnum.BEGIN;
		}
		private Status(SimpleDialog simpleDialog)
		{
			this();
			this.simpleDialog=simpleDialog;
		}
		private void ok()
		{
			statusEnum=StatusEnum.OK;
		}
		private void cancel()
		{
			statusEnum=StatusEnum.CANCEL;
		}
		public boolean isBegin()
		{
			return statusEnum==StatusEnum.BEGIN;
		}
		public boolean isOk()
		{
			return statusEnum==StatusEnum.OK;
		}
		public boolean isCancel()
		{
			return statusEnum==StatusEnum.CANCEL;
		}
		public boolean getStatus()
		{
			if(simpleDialog==null||!isBegin())
			{
				return false;
			}
			else
			{
				if(simpleDialog.handler==null)
				{
					simpleDialog.handler=new LoopHandler();
				}
			}
			try
			{
				Looper.getMainLooper().loop();
			}
			catch(Exception e)
			{
			}
			return isOk();
		}
		private void setSimpleDialog(SimpleDialog simpleDialog)
		{
			this.simpleDialog=simpleDialog;
		}
	}
	Status status;
	Handler handler=null;
	private static class LoopHandler extends Handler
	{
		@Override
		public void handleMessage(Message msg)
		{
			throw new RuntimeException("");
		}
	}
	public static boolean show(Context context,String title,String message)
	{
		SimpleDialog simpleDialog=new SimpleDialog();
		Status status=new Status(simpleDialog);
		show(context,title,message,status,simpleDialog);
		return status.getStatus();
	}
	public static void show(Context context,String title,String message,Status status)
	{
		SimpleDialog simpleDialog=new SimpleDialog();
		if(status==null)
		{
			status=new Status(simpleDialog);
		}
		else
		{
			status.setSimpleDialog(simpleDialog);
		}
		show(context,title,message,status,simpleDialog);
	}
	private static void show(Context context,String title,String message,Status status,SimpleDialog simpleDialog)
	{
		simpleDialog.status=status;
		AlertDialog.Builder builder=new AlertDialog.Builder(context);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("OK",simpleDialog);
		builder.setOnCancelListener(simpleDialog);
		AlertDialog dialog=builder.create();
		dialog.show();
	}
	@Override
	public void onClick(DialogInterface dialog,int id)
	{
		status.ok();
		quitLooper();
	}
	@Override
	public void onCancel(DialogInterface dialog)
	{
		status.cancel();
		quitLooper();
	}
	private void quitLooper()
	{
		if(handler!=null)
		{
			handler.sendMessage(handler.obtainMessage());
		}
	}
}
