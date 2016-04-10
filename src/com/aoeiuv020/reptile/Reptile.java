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
	private static Context mContext=null;
	private JSONObject mSiteJson=null;
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
		return null;
	}
	/**
	 * 耗时方法，
	 */
	public Item getComicInfo(String str)
	{
		return null;
	}
	/**
	 * 耗时方法，
	 * 整个过程其实也可以在其他类里执行，
	 */
	public static List<Item> getSites(JSONObject sitesJson)
	{
		if(sitesJson==null)
			return null;
		List<Item> list=new LinkedList<Item>();
		Iterator<String> iterator=sitesJson.keys();
		while(iterator.hasNext())
		{
			Item item=null;
			try
			{
				String name=iterator.next();
				Reptile reptile=new Reptile();
				reptile.setSite(sitesJson.getJSONObject(name));
				item=reptile.getSiteInfo();
				if(item.title==null)
					item.title=name;
			}
			catch(Exception e)
			{
				//无视任何错误，
				if(Logger.DEBUG)
					throw new RuntimeException(e);
			}
			finally
			{
				//就算有网站信息加载错误也加上这个网站，
				//毕竟只是网站信息爬不出来，说不定网站其他还能爬的，
				list.add(item);
			}
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
			if(Tool.isEmpty(item)&&!Tool.isEmpty(name))
				item.title=name;
		}
		catch(JSONException e)
		{
			throw new RuntimeException("json中没有网站信息的选择器",e);
		}
		Logger.v("getSiteInfo item=%s",item);
		return item;
	}
	public void setSite(JSONObject json)
	{
		mSiteJson=json;
		Connector.getInstance().putAll(mSiteJson);
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
	/**
	 * 耗时方法，
	 */
	public List<Item> getClassifications()
	{
		return null;
	}
	/**
	 * 耗时方法，
	 */
	public List<Item> getItems()
	{
		return null;
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
