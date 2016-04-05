/* ***************************************************
	^> File Name: MainView.java
	^> Author: AoEiuV020
	^> Mail: 490674483@qq.com
	^> Created Time: 2016/04/05 - 00:01:56
*************************************************** */
package com.aoeiuv020.comic;
import com.aoeiuv020.reptile.Reptile;
import com.aoeiuv020.stream.Stream;
import android.app.Activity;
import android.content.*;
import android.view.*;
import android.widget.*;
import android.os.Handler;
import android.os.Message;
import org.json.*;
public class MainView extends LinearLayout implements Runnable,AbsListView.OnScrollListener
{
	ListView mListView=null;
	SimpleAdapter mAdapter=null;
	Reptile reptile=null;
	public MainView(Context context)
	{
		super(context);
		setOrientation(LinearLayout.VERTICAL);
		setGravity(Gravity.TOP);
		mListView=new ListView(getContext());
		JSONObject json=null;
		try
		{
			json=new JSONObject(Stream.read(getContext(),"sites.json"));
			json=json.getJSONObject(json.keys().next());
		}
		catch(JSONException e)
		{
			throw new RuntimeException("sites.json解析错误，",e);
		}
		reptile=new Reptile(getContext(),json);
		mAdapter=new SimpleAdapter(
				getContext(),
				reptile.getData(Reptile.MAIN),
				R.layout.item,
				new String[]{"img","text","url"},
				new int[]{android.R.id.icon,android.R.id.text1,android.R.id.text2}
				);
		mAdapter.setViewBinder(reptile.getViewBinder());
		mListView.setAdapter(mAdapter);
		mListView.setOnScrollListener(this);
		addView(mListView);
		reptile.loadStart();
		Thread dataThread;
		dataThread=new Thread(this);
		dataThread.start();
	}
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
	{
	}
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState)
	{
		if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE)
		{
			if (view.getLastVisiblePosition() == view.getCount() - 1)
			{  
				reptile.loadNext();
			}
		}
	}
	public void notifyDataSetChanged()
	{
		if(mAdapter!=null)
		{
			mAdapter.notifyDataSetChanged();
		}
	}
	@Override
	public void run()
	{
	}
}
