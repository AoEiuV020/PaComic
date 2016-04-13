/* ***************************************************
	^> File Name: SiteFragment.java
	^> Author: AoEiuV020
	^> Mail: 490674483@qq.com
	^> Created Time: 2016/04/07 - 02:05:40
*************************************************** */
package com.aoeiuv020.comic;
import com.aoeiuv020.stream.Stream;
import com.aoeiuv020.tool.Logger;
import com.aoeiuv020.reptile.Reptile;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.content.*;
import android.view.*;
import android.os.*;
import android.widget.*;

import java.util.*;

import org.json.*;

public class SiteFragment extends Fragment implements View.OnClickListener,AdapterView.OnItemClickListener
{
	private ListView mListView=null;
	private Reptile mReptile=null;
	private ItemAdapter mAdapter=null;
	private OnTaskFinishListener mListener=null;
	private JSONObject mSitesJson=null;
	public SiteFragment()
	{
	}
	public SiteFragment(Reptile reptile)
	{
		mReptile=reptile;
	}
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{
		if(mReptile==null)
		{
			mReptile=((Main)getActivity()).getReptitle();
		}
		View view=inflater.inflate(R.layout.layout_fragment_site,container,false);
		mListView=(ListView)view.findViewById(R.id.listview);
		mAdapter=new ItemAdapter(getActivity(),R.layout.layout_item,R.id.item_title,R.id.item_image,R.id.item_content);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		setDefaultListener();
		setDefaultSitesJson();
		loadSites();
		return view;
	}
	private void setDefaultListener()
	{
		if(getActivity() instanceof OnTaskFinishListener&&mListener==null)
		{
			setOnTaskFinishListener((OnTaskFinishListener)getActivity());
		}
	}
	private void setDefaultSitesJson()
	{
		if(mSitesJson!=null)
			return;
		try
		{
			mSitesJson=new JSONObject(Stream.read(getActivity(),"sites.json"));
		}
		catch(JSONException e)
		{
			Logger.e(e);
			callOnFinish();
		}
	}
	public void setSitesJson(JSONObject sites)
	{
		mSitesJson=sites;
	}
	@Override
	public void onItemClick(AdapterView<?> parent,View view,int position,long id)
	{
		try
		{
			int headerCount=0;
			if(parent instanceof ListView)
				headerCount=((ListView)parent).getHeaderViewsCount();
			JSONObject site=mSitesJson.getJSONObject(mSitesJson.names().getString(position-headerCount));
			mReptile.setSite(site);
		}
		catch(JSONException e)
		{
			Logger.e(e);
		}
		callOnFinish();
	}
	public void setOnTaskFinishListener(OnTaskFinishListener listener)
	{
		mListener=listener;
	}
	private void callOnFinish()
	{
		if(mListener!=null)
		{
			mListener.onFinish();
		}
	}
	@Override
	public void onClick(View view)
	{
	}
	private void loadSites()
	{
		SiteLoadAsyncTask task=new SiteLoadAsyncTask(mReptile,mAdapter,mSitesJson);
		task.execute();
	}
}
/**
 * 这个类可能没必要，
 * 只是顺便从ClassificationLoadAsyncTask复制过来，
 * 因为解析json不是耗时操作，
 * 但如果要加载图片之类的就要连网耗时了，
 */
class SiteLoadAsyncTask extends AsyncTask<Void,Integer,List<Item>>
{
	JSONObject mSitesJson=null;
	Reptile mReptile=null;
	ItemAdapter mAdapter=null;
	//加载失败的原因，
	private Throwable mThrowable=null;
	public SiteLoadAsyncTask(Reptile reptile,ItemAdapter adapter,JSONObject sites)
	{
		mReptile=reptile;
		mSitesJson=sites;
		mAdapter=adapter;
	}
	@Override
	protected void onPreExecute() { }
	@Override
	protected List<Item> doInBackground(Void... parms)
	{
		List<Item> list=null;
		try
		{
			list=Reptile.getSites(mSitesJson);
		}
		catch(RuntimeException e)
		{
			//任何异常都表示没有内容了，
			mThrowable=e.getCause();
			Logger.e(e);
		}
		return list;
	}
	@Override
	protected void onPostExecute(List<Item> list)
	{
		if(list==null)
		{
		}
		else
		{
			mAdapter.addAll(list);
		}
	}
}
