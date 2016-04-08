/* ***************************************************
	^> File Name: ComicPagerActivity.java
	^> Author: AoEiuV020
	^> Mail: 490674483@qq.com
	^> Created Time: 2016/04/08 - 04:45:26
*************************************************** */
package com.aoeiuv020.comic;
import com.aoeiuv020.stream.Stream;
import com.aoeiuv020.tool.Tool;
import com.aoeiuv020.reptile.Reptile;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.view.inputmethod.InputMethodManager;
import android.os.Bundle;
import android.content.*;
import android.view.*;
import android.os.*;
import android.widget.*;
import android.webkit.*;

import java.util.*;

import org.json.*;

public class ComicPagerActivity extends Activity implements AdapterView.OnItemClickListener
{
	private Reptile mReptile=null;
	private String mUrl=null;
	private ItemAdapter mAdapter=null;
	private ViewGroup mInfo=null;
	private WebView mWebView=null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_webview);
		init();
		mWebView=(WebView)findViewById(R.id.webview);
		WebSettings webSettings=mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(true);
		webSettings.setUseWideViewPort(true);
		webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		webSettings.setLoadWithOverviewMode(true);
		mWebView.loadUrl(mUrl);
		WebViewClient wvc=new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view,String url)
			{
				view.loadUrl(url);
				return true;
			}
		};
		mWebView.setWebViewClient(wvc);
    }
	@Override
	public void onItemClick(AdapterView<?> parent,View view,int position,long id)
	{
	}
	private void init()
	{
		Intent intent=getIntent();
		try
		{
			String json=intent.getStringExtra("sitejson");
			JSONObject siteJson=new JSONObject(json);
			mReptile=new Reptile();
			mReptile.setSite(siteJson);
		}
		catch(JSONException e)
		{
			//不可到达，
			if(Main.DEBUG)
				throw new RuntimeException(e);
			finish();
		}
		mUrl=intent.getStringExtra("url");
		if(mReptile==null||mUrl==null)
		{
			//不可到达，
			if(Main.DEBUG)
				throw new RuntimeException("intent带的值不对");
			finish();
		}
	}
}
class ComicPageLoadAsyncTask extends AsyncTask<Void,Integer,List<Item>>
{
	Reptile mReptile=null;
	ItemAdapter mAdapter=null;
	String mUrl=null;
	private Throwable mThrowable=null;
	public ComicPageLoadAsyncTask(Reptile reptile,ItemAdapter adapter,String url)
	{
		mReptile=reptile;
		mAdapter=adapter;
		mUrl=url;
	}
	@Override
	protected List<Item> doInBackground(Void... parms)
	{
		List<Item> list=null;
		try
		{
			list=mReptile.getPages(mUrl);
		}
		catch(RuntimeException e)
		{
			//任何异常都表示没有内容了，
			mThrowable=e.getCause();
			if(Main.DEBUG)
				throw e;
		}
		return list;
	}
	@Override
	protected void onPostExecute(List<Item> list)
	{
		if(list!=null)
		{
			mAdapter.addAll(list);
		}
	}
}
