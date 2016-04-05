/* ***************************************************
	^> File Name: Reptile.java
	^> Author: AoEiuV020
	^> Mail: 490674483@qq.com
	^> Created Time: 2016/04/05 - 00:22:32
*************************************************** */
package com.aoeiuv020.reptile;
import com.aoeiuv020.stream.Stream;
import android.content.Context;
import android.widget.*;
import android.view.*;
import android.graphics.drawable.Drawable;
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
  * Reptile.MAIN
  * Reptile.CLASSIFICATION
  * Reptile.CATALOG
  * Reptile.COMIC
  * Reptile reptile=new Reptile(jsonobject);
  * List<Map<String,Object>> list;
  * //Map.keys {"icon","text1"}
  * list=reptile.getData(Reptile.MAIN);
  * list=reptile.getData(Reptile.CATALOG,index);
  * list=reptile.getData(Reptile.COMIC,index);
  * SimpleAdapter.ViewBinder binder;
  * binder=reptile.getViewBinder();
  */
public class Reptile
{
	public static final int MAIN=1;
	public static final int CLASSIFICATION=2;
	public static final int CATALOG=3;
	public static final int COMIC=4;
	private static JSONObject sitesJson=null;
	private JSONObject mJsonSite=null;
	private String encoding=null;
	private String method=null;
	private String baseuri=null;
	private URL baseUrl=null;
	private URL pageUrl=null;
	private URL catalogUrl=null;
	private URL comicUrl=null;
	public Reptile(JSONObject json)
	{
		mJsonSite=json;
		init();
	}
	private void init()
	{
		encoding="UTF-8";
		method="get";
		try
		{
			baseuri=mJsonSite.getString("baseuri");
			if(mJsonSite.has("encoding"))
			{
				encoding=mJsonSite.getString("encoding");
			}
			if(mJsonSite.has("method"))
			{
				method=mJsonSite.getString("method");
			}
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
	}
	public List<Map<String,Object>> getData(int page)
	{
		return getData(page,0);
	}
	public List<Map<String,Object>> getData(int page,int index)
	{
		List<Map<String,Object>> list=null;
		JSONObject pageJson=null;
		Selector selector=null;
		Document document=null;
		try
		{
			switch(page)
			{
				case MAIN:
					pageJson=mJsonSite.getJSONObject("main");
					pageUrl=new URL(baseUrl,pageJson.getString("uri"));
					selector=new Selector(pageJson.getJSONObject("selector"));
					document=Jsoup.connect(pageUrl.toString()).get();
					list=selector.getData(document);
					break;
			}
		}
		catch(MalformedURLException e)
		{
			throw new RuntimeException(e);
		}
		catch(JSONException e)
		{
			throw new RuntimeException(e);
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
		return list;
	}
	public SimpleAdapter.ViewBinder getViewBinder()
	{
		return new ComicViewBinder();
	}
}
class ComicViewBinder implements SimpleAdapter.ViewBinder
{
	@Override
	public boolean setViewValue(View view,Object data,String textRepresentation)
	{
		if(view instanceof ImageView&&data instanceof Drawable)
		{
			((ImageView)view).setImageDrawable((Drawable)data);
			return true;
		}
		return false;
	}
}
class Selector
{
	private String elementsQuery=null;
	private String aQuery=null;
	private String imgQuery=null;
	private String textQuery=null;
	private JSONObject mJson=null;
	public Selector(JSONObject json)
	{
		this.mJson=json;
		try
		{
			elementsQuery=mJson.getString("elements");
			aQuery=mJson.getString("a");
			imgQuery=mJson.getString("img");
			textQuery=mJson.getString("text");
		}
		catch(JSONException e)
		{
			throw new RuntimeException(e);
		}
	}
	public List<Map<String,Object>> getData(Element parmElement)
	{
		List<Map<String,Object>> list=new LinkedList<Map<String,Object>>();
		Map<String,Object> map=null;
		try
		{
			for(Element element:parmElement.select(elementsQuery))
			{
				Element aElement,imgElement,textElement;
				aElement=element.select(aQuery).first();
				imgElement=element.select(imgQuery).first();
				textElement=element.select(textQuery).first();
				Drawable drawable=null;
				try
				{
					drawable=Drawable.createFromStream(new ByteArrayInputStream(Jsoup.connect(imgElement.absUrl("src")).ignoreContentType(true).execute().bodyAsBytes()),null);
				}
				catch(Exception e)
				{
					throw new RuntimeException(e);
				}
				map=new HashMap<String,Object>();
				map.put("img",drawable);
				map.put("text",textElement.text());
				map.put("url",aElement.absUrl("href"));
				list.add(map);
			}
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
		return list;
	}
}
