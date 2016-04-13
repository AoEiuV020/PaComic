/* ***************************************************
	^> File Name: Reptile.java
	^> Author: AoEiuV020
	^> Mail: 490674483@qq.com
	^> Created Time: 2016/04/05 - 00:22:32
*************************************************** */
package com.aoeiuv020.reptile;
import com.aoeiuv020.tool.Stream;
import com.aoeiuv020.tool.Tool;
import com.aoeiuv020.tool.Logger;
import com.aoeiuv020.comic.Item;
import com.aoeiuv020.comic.Main;
import android.app.Activity;
import android.content.Context;
import android.content.*;
import android.widget.*;
import android.webkit.*;
import android.view.*;
import android.util.Log;
import org.json.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import java.util.*;
import java.net.URL;
import java.net.URLEncoder;
import java.net.MalformedURLException;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.io.IOException;
/**
  * Reptile reptile=new Reptile(mContext);
  * reptile.setSite(siteJson); //设置网站，不耗时，不抛异常，
  * List<Item> list=reptile.getItems(); //耗时，只抛RuntimeException,
  * reptile.loadNext(); //耗时，不抛异常，返回是否有Next,boolean
  * reptile.setClassification(3); //设置分类，不耗时，
  * List<Item> classList=reptile.getClassifications(); //耗时，
  */
public class Reptile
{
	private String mClassificationUrl=null;
	private static Context mContext=null;
	private JSONObject mSiteJson=null;
	private static JSONObject mAllSitesJson=null;
	public Reptile()
	{
	}
	public Reptile(Context context)
	{
		this();
		setContext(context);
	}
	public static void setContext(Context context)
	{
		mContext=context;
	}
	/**
	 * 耗时方法，
	 */
	private Document getHtmlJavascriptEnable(String str)
	{
		return null;
	}
	private WebView mWebView=null;
	public void setNextPageUrl(String url)
	{
	}
	private String PageUrl=null;
	public List<Item> getPages(String str)
	{
		return null;
	}
	/**
	 * 耗时方法，
	 */
	public List<Item> getCatalog(String str)
	{
		return getItems("catalog",str);
	}
	/**
	 * 耗时方法，
	 */
	public Item getComicInfo(String str)
	{
		return getItems("info",str).get(0);
	}
	/**
	 * 耗时方法，
	 */
	public static List<Item> getSites(JSONObject sites)
	{
		Logger.v("getSites sites=%s",sites);
		mAllSitesJson=sites;
		return getSites();
	}
	public static List<Item> getSites()
	{
		List<Item> list=new LinkedList<Item>();
		List<JSONObject> sitesList=getSitesJsonList();
		if(sitesList==null)
			return null;
		for(JSONObject site:sitesList)
		{
			Item item=null;
			try
			{
				Reptile reptile=new Reptile();
				reptile.setSite(site);
				item=reptile.getSiteInfo();
			}
			catch(Exception e)
			{
				//无视任何错误，
				Logger.e(e);
			}
			finally
			{
				//就算有网站信息加载错误也加上这个网站，
				//毕竟只是网站信息爬不出来，说不定网站其他还能爬的，
				list.add(item);
			}
			Logger.v("getSiteInfo %s",item);
		}
		return list;
	}
	/**
	 * 耗时方法，
	 */
	public Item getSiteInfo()
	{
		if(mSiteJson==null)
			return null;
		Item item=new Item();
		Logger.v("getSiteInfo ");
		try
		{
			JSONObject siteSelector=mSiteJson.getJSONObject("selector").getJSONObject("site");
			item=Selector.select(siteSelector).get(0);
			String name=Tool.getString(mSiteJson,"name");
		}
		catch(JSONException e)
		{
			throw new RuntimeException("json中没有网站信息的选择器",e);
		}
		Logger.v("getSiteInfo item=%s",item);
		return item;
	}
	public static void setSitesJson(JSONObject sites)
	{
		mAllSitesJson=sites;
	}
	public static List<JSONObject> getSitesJsonList()
	{
		if(mAllSitesJson==null)
			return null;
		List<JSONObject> list=null;
		list=new LinkedList<JSONObject>();
		Iterator<String> iterator=mAllSitesJson.keys();
		while(iterator.hasNext())
		{
			try
			{
				String name=iterator.next();
				JSONObject site=mAllSitesJson.getJSONObject(name);
				list.add(site);
			}
			catch(JSONException e)
			{
				Logger.e(e);
			}
		}
		return list;
	}
	public void setSite(int index)
	{
		Logger.v("setSite i="+index);
		JSONObject site=getSitesJsonList().get(index);
		setSite(site);
	}
	public void setSite(JSONObject json)
	{
		mSiteJson=json;
		Connector.getInstance().putAll(mSiteJson);
		mClassificationUrl=null;
	}
	public JSONObject getSiteJson()
	{
		return mSiteJson;
	}
	public boolean loadNext()
	{
		return false;
	}
	/**
	 * 不抛异常，
	 * 所有错误都无视，
	 */
	public void setSearch(String sSearch)
	{
	}
	public void setClassification(int index)
	{
	}
	public void setClassification(String url)
	{
		mClassificationUrl=url;
	}
	/**
	 * 耗时方法，
	 */
	public List<Item> getClassifications()
	{
		return getItems("classification",Connector.getInstance().getBaseurl());
	}
	/**
	 * 耗时方法，
	 */
	public List<Item> getItems()
	{
		if(mClassificationUrl==null)
			mClassificationUrl=getClassifications().get(0).url;
		return getItems("item",mClassificationUrl);
	}
	public List<Item> getItems(String str,String url)
	{
		if(mSiteJson==null)
			return null;
		List<Item> list=null;
		Logger.v("getItems %s",str);
		try
		{
			JSONObject selectorJson=mSiteJson.getJSONObject("selector").getJSONObject(str);
			list=Selector.select(selectorJson,url);
		}
		catch(JSONException e)
		{
			throw new RuntimeException("json中没有"+str+"的选择器",e);
		}
		Logger.v("item list size %d",list.size());
		return list;
	}
}
class JsGetHtml
{
	private String mHtml=null;
	private Thread mThread=null;
	private StringHolder mHolder=null;
	public JsGetHtml(Thread thread,StringHolder holder)
	{
	}
	@JavascriptInterface
	public void setHtml(String html)
	{
	}
}
class JsGetNext
{
	private String mHtml=null;
	private Thread mThread=null;
	private StringHolder mHolder=null;
	public JsGetNext(Thread thread,StringHolder holder)
	{
	}
	@JavascriptInterface
	public void setUrl(String url)
	{
	}
}
class MyWebViewClient extends WebViewClient
{
	private Reptile mReptile=null;
	public MyWebViewClient(Reptile reptile)
	{
	}
	@Override
	public void onPageFinished(WebView view,String url)
	{
	}
	@Override
	public boolean shouldOverrideUrlLoading(WebView view,String url)
	{
		return false;
	}
}
class StringHolder
{
	public String string;
}
