/* ***************************************************
	^> File Name: ClassificationFragment.java
	^> Author: AoEiuV020
	^> Mail: 490674483@qq.com
	^> Created Time: 2016/04/07 - 02:01:42
*************************************************** */
package com.aoeiuv020.comic;
import com.aoeiuv020.stream.Stream;
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

public class ClassificationFragment extends Fragment implements View.OnClickListener,AdapterView.OnItemClickListener
{
	private ListView mListView=null;
	private Reptile mReptile=null;
	private ItemAdapter mAdapter=null;
	private OnTaskFinishListener mListener=null;
	private TextView mEditText=null;
	public ClassificationFragment()
	{
	}
	public ClassificationFragment(Reptile reptile)
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
		View view=inflater.inflate(R.layout.layout_fragment_classification,container,false);
		mListView=(ListView)view.findViewById(R.id.listview);
		View vSearch=inflater.inflate(R.layout.layout_search,null);
		mListView.addHeaderView(vSearch);
		View bSearch=vSearch.findViewById(R.id.search_button);
		bSearch.setOnClickListener(this);
		mEditText=(TextView)vSearch.findViewById(R.id.search_edit);
		mAdapter=new ItemAdapter(getActivity(),R.layout.layout_item,R.id.item_title,R.id.item_image,R.id.item_content);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		loadClassifications();
		setDefaultListener();
		return view;
	}
	@Override
	public void onClick(View view)
	{
		String sSearch=""+mEditText.getText();
		if(sSearch.equals(""))
			return;
		mReptile.setSearch(sSearch);
		callOnFinish();
	}
	private void setDefaultListener()
	{
		if(getActivity() instanceof OnTaskFinishListener)
		{
			setOnTaskFinishListener((OnTaskFinishListener)getActivity());
		}
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
	public void onItemClick(AdapterView<?> parent,View view,int position,long id)
	{
		mReptile.setClassification(position-1);
		callOnFinish();
	}
	private void loadClassifications()
	{
		ClassificationLoadAsyncTask task=new ClassificationLoadAsyncTask(mReptile,mAdapter);
		task.execute();
	}
}
class ClassificationLoadAsyncTask extends AsyncTask<Void,Integer,List<Item>>
{
	private static final boolean DEBUG=Main.DEBUG;
	Reptile mReptile=null;
	ItemAdapter mAdapter=null;
	//加载失败的原因，
	private Throwable mThrowable=null;
	public ClassificationLoadAsyncTask(Reptile reptile,ItemAdapter adapter)
	{
		mReptile=reptile;
		mAdapter=adapter;
	}
	@Override
	protected void onPreExecute()
	{
	}
	@Override
	protected List<Item> doInBackground(Void... parms)
	{
		List<Item> list=null;
		try
		{
			list=mReptile.getClassifications();
		}
		catch(RuntimeException e)
		{
			//任何异常都表示没有内容了，
			mThrowable=e.getCause();
			if(DEBUG) throw new RuntimeException(e);
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
