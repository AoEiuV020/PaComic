/* ***************************************************
	^> File Name: Reptile.java
	^> Author: AoEiuV020
	^> Mail: 490674483@qq.com
	^> Created Time: 2016/04/05 - 00:22:32
*************************************************** */
package com.aoeiuv020.reptile;
import com.aoeiuv020.stream.Stream;
import com.aoeiuv020.tool.Tool;
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
	private int mClassificationIndex=0;
	private JSONObject mJsonSite=null;
	private String encoding=null;
	private URL baseUrl=null;
	private Selector mItemsSelector=null;
	private Connection mConnection=null;
	private List<Item> mClassificationList=null;
	private JSONObject mItemsSelectorJson=null;
	private Item mSiteInfo=null;
	private static Context mContext=null;
	public Reptile(Context context)
	{
		mContext=context;
	}
	/**
	 * 耗时方法，
	 */
	private Document getHtmlJavascriptEnable(String str)
	{
		if(Main.DEBUG)
			Log.v(""+this,"getHtmlJavascriptEnable "+str);
		WebView webView=null;
		webView=((com.aoeiuv020.comic.ComicPagerActivity)mContext).getWebView();
		webView.setWebViewClient(new MyWebViewClient());
		webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setLoadsImagesAutomatically(false);
		Thread current=Thread.currentThread();
		StringHolder holder=new StringHolder();
		JsGetHtml js=new JsGetHtml(current,holder);
		webView.addJavascriptInterface(js,"JsGetHtml_JavascriptInterface");
		webView.loadUrl(str);
		try
		{
			if(Main.DEBUG)
				Log.v(""+this,"wait "+current);
			synchronized(current)
			{
				current.wait();
			}
		}
		catch(InterruptedException e)
		{
			if(Main.DEBUG)
				Log.v(""+this,"InterruptedException "+e.getMessage());
		}
		if(Main.DEBUG)
			Log.v(""+this,"wait ok "+holder.string.length());
		return Jsoup.parse(holder.string,baseUrl.toString());
	}
	public List<Item> getPages(String str)
	{
		if(Main.DEBUG)
			Log.v(""+this,"getPages "+str);
		List<Item> list=null;
		try
		{
			JSONObject siteSelector=mJsonSite.getJSONObject("selector").getJSONObject("page");
			Document document=null;
			if(!Tool.isEmpty(siteSelector)&&siteSelector.has("js"))
			{
				document=getHtmlJavascriptEnable(str);
			}
			else
			{
				URL url=new URL(baseUrl,str);
				document=mConnection.url(url).execute().parse();
			}
			list=new Selector(siteSelector,mConnection,document).getItems();
		}
		catch(JSONException e)
		{
			throw new RuntimeException("json中没有目录的选择器",e);
		}
		catch(MalformedURLException e)
		{
			throw new RuntimeException("uri参数错误",e);
		}
		catch(IOException e)
		{
			throw new RuntimeException("可能是网络不通",e);
		}
		return list;
	}
	/**
	 * 耗时方法，
	 */
	public List<Item> getCatalog(String str)
	{
		List<Item> list=null;
		try
		{
			URL url=new URL(baseUrl,str);
			Document document=mConnection.url(url).execute().parse();
			JSONObject siteSelector=mJsonSite.getJSONObject("selector").getJSONObject("catalog");
			list=new Selector(siteSelector,mConnection,document).getItems();
		}
		catch(JSONException e)
		{
			throw new RuntimeException("json中没有目录的选择器",e);
		}
		catch(MalformedURLException e)
		{
			throw new RuntimeException("uri参数错误",e);
		}
		catch(IOException e)
		{
			throw new RuntimeException("可能是网络不通",e);
		}
		return list;
	}
	/**
	 * 耗时方法，
	 */
	public Item getComicInfo(String str)
	{
		Item item=null;
		try
		{
			URL url=new URL(baseUrl,str);
			Document document=mConnection.url(url).execute().parse();
			JSONObject siteSelector=mJsonSite.getJSONObject("selector").getJSONObject("info");
			item=new Selector(siteSelector,mConnection,document).getItems().get(0);
		}
		catch(JSONException e)
		{
			throw new RuntimeException("json中没有info的选择器",e);
		}
		catch(MalformedURLException e)
		{
			throw new RuntimeException("uri参数错误",e);
		}
		catch(IOException e)
		{
			throw new RuntimeException("可能是网络不通",e);
		}
		return item;
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
				Reptile reptile=new Reptile(mContext);
				reptile.setSite(sitesJson.getJSONObject(name));
				item=reptile.getSiteInfo();
				if(item.title==null)
					item.title=name;
			}
			catch(Exception e)
			{
				//无视任何错误，
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
		if(mSiteInfo!=null)
		{
			return mSiteInfo;
		}
		try
		{
			Document document=mConnection.url(baseUrl).execute().parse();
			JSONObject siteSelector=mJsonSite.getJSONObject("selector").getJSONObject("site");
			mSiteInfo=new Selector(siteSelector,mConnection,document).getItems().get(0);
			if(mSiteInfo.title==null&&mJsonSite.has("name"))
				mSiteInfo.title=mJsonSite.getString("name");
		}
		catch(JSONException e)
		{
			throw new RuntimeException("json中没有网站信息的选择器",e);
		}
		catch(MalformedURLException e)
		{
			throw new RuntimeException("uri参数错误",e);
		}
		catch(IOException e)
		{
			throw new RuntimeException("可能是网络不通",e);
		}
		return mSiteInfo;
	}
	public void setSite(JSONObject json)
	{
		mJsonSite=json;
		initSite();
		mClassificationList=null;
		mItemsSelectorJson=null;
		setClassification(0);
	}
	public JSONObject getSiteJson()
	{
		return mJsonSite;
	}
	private void initSite()
	{
		String method="GET";
		String baseuri=null;
		String useragent="Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.73 Safari/537.36";
		int timeout=3000;
		try
		{
			baseuri=mJsonSite.getString("baseuri");
			if(mJsonSite.has("method"))
				method=mJsonSite.getString("method");
			if(mJsonSite.has("timeout"))
				timeout=mJsonSite.getInt("timeout");
			if(mJsonSite.has("useragent"))
				useragent=mJsonSite.getString("useragent");
		}
		catch(JSONException e)
		{
			throw new RuntimeException(e);
		}
		try
		{
			baseUrl=new URL(baseuri);
		}
		catch(MalformedURLException e)
		{
			throw new RuntimeException("baseuri不规范:"+baseuri,e);
		}
		mConnection=Jsoup.connect(baseUrl.toString());
		mConnection.timeout(timeout);
		mConnection.userAgent(useragent);
		mConnection.method(Connection.Method.valueOf(method));
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
	/**
	  * 不抛异常，
	  * 所有错误都无视，
	  */
	public void setSearch(String sSearch)
	{
		try
		{
			String encoding="UTF-8";
			JSONObject searchSelectorJson=mJsonSite.getJSONObject("selector").getJSONObject("search");
			if(searchSelectorJson.has("encoding"))
				encoding=searchSelectorJson.getString("encoding");
			sSearch=URLEncoder.encode(sSearch,encoding);
			String sUrl=searchSelectorJson.getString("url");
			sUrl=String.format(sUrl,sSearch);
			URL url=new URL(baseUrl,sUrl);
			Document document=mConnection.url(url).execute().parse();
			mItemsSelector=new Selector(searchSelectorJson,mConnection,document);
		}
		catch(Exception e)
		{
		}
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
			throw new RuntimeException("json中没有网站信息的选择器",e);
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
				JSONObject itemsSelectorJson=mJsonSite.getJSONObject("selector").getJSONObject("item");
				if(mClassificationUrl==null)
				{
					mClassificationUrl=getClassifications().get(mClassificationIndex).url;
				}
				URL url=new URL(baseUrl,mClassificationUrl);
				Document document=mConnection.url(url).execute().parse();
				mItemsSelector=new Selector(itemsSelectorJson,mConnection,document);
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
class JsGetHtml
{
	private String mHtml=null;
	private Thread mThread=null;
	private StringHolder mHolder=null;
	public JsGetHtml(Thread thread,StringHolder holder)
	{
		mThread=thread;
		mHolder=holder;
	}
	@JavascriptInterface
	public void setHtml(String html)
	{
		if(Main.DEBUG)
			Log.v(""+this,"setHtml "+html.length());
		mHtml=html;
		mHolder.string=html;
		mThread.interrupt();
	}
}
class MyWebViewClient extends WebViewClient
{
	@Override
	public void onPageFinished(WebView view,String url)
	{
		if(Main.DEBUG)
			Log.v(""+this,"onPageFinished "+url);
		view.loadUrl("javascript:JsGetHtml_JavascriptInterface.setHtml(document.firstElementChild.innerHTML)");
	}
	@Override
	public boolean shouldOverrideUrlLoading(WebView view,String url)
	{
		return true;
	}
}
class StringHolder
{
	public String string;
}
