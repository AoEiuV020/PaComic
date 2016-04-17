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
import com.aoeiuv020.reptile.WebViewDaemon;

import android.app.Activity;
import android.util.Log;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.webkit.*;
import android.view.inputmethod.InputMethodManager;
import android.os.Bundle;
import android.content.*;
import android.view.*;
import android.os.*;
import android.widget.*;

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
		if(Main.DEBUG)
			System.out.println("onCreate "+this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_activity_comic_pager);
		mWebView=(WebView)findViewById(R.id.webview);
		WebViewDaemon.getInstance().setWebView(mWebView);
		//mWebView.setVisibility(View.GONE);
		ListView listView=(ListView)findViewById(R.id.listview);
		mAdapter=new ItemAdapter(this,R.layout.layout_page,null,R.id.page_image);
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(this);
		init();
		loadPages();
    }
	public WebView getWebView()
	{
		return mWebView;
	}
	private void loadPages()
	{
		ComicPageLoadAsyncTask task=new ComicPageLoadAsyncTask(mReptile,mAdapter,mUrl);
		task.execute(ComicPageLoadAsyncTask.FIRST);
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
			mReptile=new Reptile(this);
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
class ComicPageLoadAsyncTask extends AsyncTask<Integer,Integer,List<Item>>
{
	Reptile mReptile=null;
	ItemAdapter mAdapter=null;
	String mUrl=null;
	private Throwable mThrowable=null;
	public static final int FIRST=0;
	public static final int NEXT=1;
	public ComicPageLoadAsyncTask(Reptile reptile,ItemAdapter adapter,String url)
	{
		if(Main.DEBUG)
			Log.v(""+this,"ComicPageLoadAsyncTask "+this);
		mReptile=reptile;
		mAdapter=adapter;
		mUrl=url;
	}
	@Override
	protected List<Item> doInBackground(Integer... parms)
	{
		List<Item> list=null;
		try
		{
			switch(parms[0])
			{
				case NEXT:
					if(mReptile.loadNextPage())
						list=mReptile.getPages(null);
					break;
				case FIRST:
					list=mReptile.getPages(mUrl);
					break;
			}
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
			if(Main.DEBUG)
				Log.v(""+this,"img="+list.get(0).image);
			mAdapter.addAll(list);
			ComicPageLoadAsyncTask task=new ComicPageLoadAsyncTask(mReptile,mAdapter,mUrl);
			task.execute(NEXT);
		}
	}
}
