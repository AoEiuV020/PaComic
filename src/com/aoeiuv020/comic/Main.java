/* ***************************************************
	^> File Name: Main.java
	^> Author: AoEiuV020
	^> Mail: 490674483@qq.com
	^> Created Time: 2016/04/06 - 05:44:25
*************************************************** */
package com.aoeiuv020.comic;
import com.aoeiuv020.stream.Stream;
import com.aoeiuv020.reptile.Reptile;

import android.app.Activity;
import android.os.Bundle;
import android.content.*;
import android.view.*;
import android.os.*;
import android.widget.*;

import java.util.*;

import org.json.*;

public class Main extends Activity implements View.OnClickListener
{
	ListView mListView=null;
	List<Item> mList=null;
	ItemLoadAsyncTask mTask=null;
	Button bLoadMore=null;
	Reptile mReptile=null;
	ItemAdapter mAdapter=null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		first();
        setContentView(R.layout.main);
		mListView=(ListView)findViewById(R.id.listview);
		View vLoadMore=getLayoutInflater().inflate(R.layout.layout_load_more,null);
		bLoadMore=(Button)vLoadMore.findViewById(R.id.button_load_more);
		bLoadMore.setOnClickListener(this);
		mListView.addFooterView(vLoadMore);
		mAdapter=new ItemAdapter(this,null);
		mListView.setAdapter(mAdapter);
		mReptile=getReptile();
		loadMain();
    }
	private void loadMain()
	{
		mTask=new ItemLoadAsyncTask(mReptile,mAdapter,bLoadMore);
		mTask.execute(ItemLoadAsyncTask.MAIN);
	}
	private Reptile getReptile()
	{
		Reptile reptile=null;
		try
		{
			JSONObject sitesJson=new JSONObject(Stream.read(this,"sites.json"));
			reptile=new Reptile(this,sitesJson.getJSONObject(sitesJson.keys().next()));
		}
		catch(JSONException e)
		{
			throw new RuntimeException("sites.json解析出错",e);
		}
		return reptile;
	}
	@Override
	public void onClick(View view)
	{
		if(mTask.getStatus()==AsyncTask.Status.FINISHED)
		{
			mTask=new ItemLoadAsyncTask(mReptile,mAdapter,(Button)view);
			mTask.execute(ItemLoadAsyncTask.NEXT);
		}
	}
	private void loadMore()
	{
	}
	private void setAdapter(List<Item> list)
	{
		mList=list;
	}
	private void first()
	{
		SharedPreferences setting = getSharedPreferences("app", MODE_PRIVATE);  
		Boolean user_first = setting.getBoolean("first",true);  
		if(user_first)
		{
			setting.edit().putBoolean("first", false).commit();  
		}
		else
		{
			return;
		}
		//把assets中的配置文件写到files，
		String sitesjson="sites.json";
		Stream.write(this,sitesjson,Stream.read(this.getAssets(),sitesjson));
	}
}
