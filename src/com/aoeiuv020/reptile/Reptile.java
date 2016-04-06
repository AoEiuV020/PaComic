/* ***************************************************
	^> File Name: Reptile.java
	^> Author: AoEiuV020
	^> Mail: 490674483@qq.com
	^> Created Time: 2016/04/05 - 00:22:32
*************************************************** */
package com.aoeiuv020.reptile;
import com.aoeiuv020.stream.Stream;
import com.aoeiuv020.comic.Item;
import android.content.Context;
import android.content.*;
import android.widget.*;
import android.view.*;
import org.json.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import java.util.*;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
/*
  */
public class Reptile
{
	private String mClassificationUrl=null;
	private int mClassificationIndex=0;
	private JSONObject mJsonSite=null;
	private String encoding=null;
	private String method=null;
	private URL baseUrl=null;
	private Selector mItemsSelector=null;
	private Connection mConnection=null;
	private List<Item> mClassificationList=null;
	private JSONObject mItemsSelectorJson=null;
	public Reptile()
	{
	}
	public void setSite(JSONObject json)
	{
		mJsonSite=json;
		initSite();
		mClassificationList=null;
		mItemsSelectorJson=null;
		setClassification(0);
	}
	private void initSite()
	{
		method="GET";
		String baseuri=null;
		try
		{
			baseuri=mJsonSite.getString("baseuri");
			baseUrl=new URL(baseuri);
			mConnection=Jsoup.connect(baseUrl.toString());
			if(mJsonSite.has("method"))
			{
				method=mJsonSite.getString("method");
			}
			mConnection.method(Connection.Method.valueOf(method));
		}
		catch(JSONException e)
		{
			throw new RuntimeException(e);
		}
		catch(MalformedURLException e)
		{
			throw new RuntimeException("baseuri不规范:"+baseuri,e);
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}
	public boolean loadNext()
	{
		boolean hasNext=false;
		if(mItemsSelector!=null)
		{
			hasNext=mItemsSelector.loadNext();
		}
		return hasNext;
	}
	public void setClassification(int index)
	{
		mClassificationIndex=index;
		mClassificationUrl=null;
		mItemsSelector=null;
	}
	/**
	 * 耗时方法，
	 */
	public List<Item> getClassifications()
	{
		if(mClassificationList!=null)
		{
			return mClassificationList;
		}
		try
		{
			Document document=mConnection.url(baseUrl).execute().parse();
			mClassificationList=new Selector(mJsonSite.getJSONObject("selector").getJSONObject("classification"),mConnection,document).getItems();
		}
		catch(JSONException e)
		{
			throw new RuntimeException("找不到分类",e);
		}
		catch(MalformedURLException e)
		{
			throw new RuntimeException("uri参数错误",e);
		}
		catch(IOException e)
		{
			throw new RuntimeException("可能是网络不通",e);
		}
		return mClassificationList;
	}
	/**
	 * 耗时方法，
	 */
	public List<Item> getItems()
	{
		List<Item> list=null;
		try
		{
			if(mItemsSelector==null)
			{
				if(mItemsSelectorJson==null)
				{
					mItemsSelectorJson=mJsonSite.getJSONObject("selector").getJSONObject("item");
				}
				if(mClassificationUrl==null)
				{
					mClassificationUrl=getClassifications().get(mClassificationIndex).url;
				}
				URL url=new URL(baseUrl,mClassificationUrl);
				Document document=mConnection.url(url).execute().parse();
				mItemsSelector=new Selector(mItemsSelectorJson,mConnection,document);
			}
			list=mItemsSelector.getItems();
		}
		catch(MalformedURLException e)
		{
			throw new RuntimeException("uri参数错误",e);
		}
		catch(JSONException e)
		{
			throw new RuntimeException("指定分类的json解析错误",e);
		}
		catch(IOException e)
		{
			throw new RuntimeException("可能是网络不通",e);
		}
		return list;
	}
}
