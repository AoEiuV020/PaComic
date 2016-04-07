/* ***************************************************
	^> File Name: ComicInfoActivity.java
	^> Author: AoEiuV020
	^> Mail: 490674483@qq.com
	^> Created Time: 2016/04/07 - 22:51:03
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

import java.util.*;

import org.json.*;

public class ComicInfoActivity extends Activity
{
	private Reptile mReptile=null;
	private String mUrl=null;
	private ItemAdapter mAdapter=null;
	private ViewGroup mInfo=null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		View mainView=getLayoutInflater().inflate(R.layout.layout_activity_comic_info,null);
        setContentView(mainView);
		ListView listView=(ListView)findViewById(R.id.listview);
		mInfo=(ViewGroup)getLayoutInflater().inflate(R.layout.layout_comic_info,null);
		listView.addHeaderView(mInfo);
		mAdapter=new ItemAdapter(this,R.layout.layout_item,R.id.item_title,R.id.item_image,R.id.item_content);
		listView.setAdapter(mAdapter);
		init();
		loadInfo();
		loadCatalog();
    }
	private void loadCatalog()
	{
		ComicInfoLoadAsyncTask task=new ComicInfoLoadAsyncTask(mReptile,mAdapter,mUrl);
		task.execute();
	}
	private void loadInfo()
	{
		//耗时操作，连网下载一个页面，
		TextView title=(TextView)mInfo.findViewById(R.id.info_title);
		TextView content=(TextView)mInfo.findViewById(R.id.info_content);
		ImageView image=(ImageView)mInfo.findViewById(R.id.info_image);
		Item item=mReptile.getComicInfo(mUrl);
		if(item!=null)
		{
			if(!Tool.isEmpty(item.title))
				title.setText(item.title);
			if(!Tool.isEmpty(item.content))
				content.setText(item.content);
			if(!Tool.isEmpty(item.image))
				ImageLoader.showImage(image,item.image);
		}
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
class ComicInfoLoadAsyncTask extends AsyncTask<Void,Integer,List<Item>>
{
	Reptile mReptile=null;
	ItemAdapter mAdapter=null;
	String mUrl=null;
	private Throwable mThrowable=null;
	public ComicInfoLoadAsyncTask(Reptile reptile,ItemAdapter adapter,String url)
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
			list=mReptile.getCatalog(mUrl);
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
