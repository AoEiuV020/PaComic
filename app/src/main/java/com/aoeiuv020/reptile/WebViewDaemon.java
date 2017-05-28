/* ***************************************************
	^> File Name: WebViewDaemon.java
	^> Author: AoEiuV020
	^> Mail: 490674483@qq.com
	^> Created Time: 2016/04/16 - 00:15:23
*************************************************** */
package com.aoeiuv020.reptile;
import com.aoeiuv020.tool.Logger;
import com.aoeiuv020.tool.Stream;
import android.os.*;
import android.view.*;
import android.content.*;
import android.graphics.*;
import android.webkit.*;
import android.util.*;
import android.widget.*;
public class WebViewDaemon
{
	private final String TAG=""+this;
	private static WebViewDaemon mWebViewDaemon=new WebViewDaemon();
	private WebView mWebView=null;
	private Context mContext=null;
	private JsLoadNext js;
	private WebViewDaemon()
	{
	}
	public static WebViewDaemon getInstance()
	{
		return mWebViewDaemon;
	}
	public void setWebView(WebView webview)
	{
		mWebView=webview;
		mContext=mWebView.getContext();
		init();
	}
	public void setContext(Context context)
	{
		mContext=context;
		mWebView=new WebView(context);
		init();
	}
	private void init()
	{
		mWebView.setWebViewClient(new MyWebViewClient());
		mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setLoadsImagesAutomatically(false);
		js=new JsLoadNext(mWebView);
		mWebView.addJavascriptInterface(js,"JsLoadNext_JavascriptInterface");
	}
	public String load(String url,int timeout)
	{
		Logger.v("url=%s ,timeout=%d",url,timeout);
		MyThread thread=new MyThread(timeout);
		js.setThread(thread);
		StringHolder holder=new StringHolder();
		js.setHolder(holder);
		thread.start();
		mWebView.loadUrl(url);
		try
		{
		thread.join();
		}
		catch(InterruptedException e)
		{
			Logger.e(e);
		}
		try
		{
		Logger.v("load %s,%d",holder,holder.string.length());
		}
		catch(NullPointerException e)
		{
			Logger.v("%s",e);
		}
		Logger.v("%s",holder.string);
		return holder.string;
	}
	class StringHolder
	{
		String string=null;
	}
	class MyThread extends Thread
	{
		private int mTimeout=0;
		private WebView mWebView=null;
		public MyThread(int timeout)
		{
			mTimeout=timeout;
		}
		public void setWebView(WebView w)
		{
			mWebView=w;
		}
		@Override
		public void run()
		{
			try
			{
				Thread.sleep(mTimeout);
				Logger.v("awake timeout=%d",mTimeout);
				if(mWebView!=null)
				{
					mWebView.stopLoading();
				}
				Thread.sleep(2000);
			}
			catch(InterruptedException e)
			{
				Logger.v("%s","InterruptedException "+e);
			}
			try
			{
				Logger.v("%s","sleep ok ");
				int progress=0;
				progress=mWebView.getProgress();
				Logger.v("%s","run progress="+progress);
			}
			catch(Exception e)
			{
				Logger.v("%s","thread exception ");
			}
		}
	}
	class JsLoadNext
	{
		private StringHolder mHolder;
		private MyThread mThread;
		private WebView mWebView;
		private int count=0;
		public JsLoadNext(WebView w)
		{
			mWebView=w;
		}
		public void setThread(MyThread thread)
		{
			if(mThread!=null)
				mThread.setWebView(null);
			mThread=thread;
			mThread.setWebView(mWebView);
		}
		public void setHolder(StringHolder holder)
		{
			mHolder=holder;
		}
		@JavascriptInterface
		public void setHtml(String html)
		{
			//if(mHolder!=null)
				mHolder.string=html;
			Logger.v("setHtml %s,%d",mHolder,html.length());
			mThread.interrupt();
			try
			{
			Logger.s(mContext,"html"+count,html);
			++count;
			}
			catch(Exception e)
			{
				Logger.v("setHtml Exception "+e);
			}
		}
	}
	class MyWebViewClient extends WebViewClient
	{
		@Override
		public void doUpdateVisitedHistory(WebView view, String url, boolean isReload)
		{
			super.doUpdateVisitedHistory(view,url,isReload);
			Logger.v("%s","onScaleChanged "+url+","+isReload+"");
		}
		@Override
		public void	onScaleChanged(WebView view, float oldScale, float newScale)
		{
			super.onScaleChanged(view,oldScale,newScale);
			Logger.v("%s","onScaleChanged "+oldScale+","+newScale+"");
		}
		@Override
		public void onLoadResource(WebView view, String url)
		{
			super.onLoadResource(view,url);
			Logger.v("%s","onLoadResource url="+url);
		}
		/**
		 * API 23
		 */
		//@Override
		public void onPageCommitVisible(WebView view, String url)
		{
			//super.onPageCommitVisible(view,url);
			Logger.v("%s","onPageCommitVisible url="+url);
		}
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon)
		{
			super.onPageStarted(view,url,favicon);
			Logger.v("%s","onPageStarted url="+url);
		}
		@Override
		public void onPageFinished(WebView view,String url)
		{
			Logger.v("%s","onPageFinished url="+url);
			view.loadUrl("javascript:JsLoadNext_JavascriptInterface.setHtml(document.getElementsByTagName('html')[0].innerHTML)");
			super.onPageFinished(view,url);
		}
		@Override
		public boolean shouldOverrideUrlLoading(WebView view,String url)
		{
			Logger.v("%s","shouldOverrideUrlLoading url="+url);
			view.loadUrl(url);
			return true;
		}
	}
}
