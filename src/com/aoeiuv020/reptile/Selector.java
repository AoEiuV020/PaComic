/* ***************************************************
	^> File Name: Selector.java
	^> Author: AoEiuV020
	^> Mail: 490674483@qq.com
	^> Created Time: 2016/04/07 - 01:06:58
*************************************************** */
package com.aoeiuv020.reptile;
import com.aoeiuv020.comic.Item;
import com.aoeiuv020.comic.Main;
import com.aoeiuv020.tool.Tool;
import android.content.*;
import org.json.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import java.util.*;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.io.IOException;

public class Selector
{
	private String elementsQuery=null;
	private String aQuery=null;
	private String imgQuery=null;
	private String textQuery=null;
	private String contentQuery=null;
	private String nextQuery=null;
	private JSONObject mJson=null;
	private Element mElement=null;
	private Connection mConnection=null;
	public Selector(JSONObject json,Connection parmConnection,Element parmElement)
	{
		this.mJson=json;
		mConnection=parmConnection;
		try
		{
			if(mJson.has("elements"))
			{
				elementsQuery=mJson.getString("elements");
			}
			if(mJson.has("a"))
			{
				aQuery=mJson.getString("a");
			}
			if(mJson.has("img"))
			{
				imgQuery=mJson.getString("img");
			}
			if(mJson.has("text"))
			{
				textQuery=mJson.getString("text");
			}
			if(mJson.has("content"))
			{
				contentQuery=mJson.getString("content");
			}
			if(mJson.has("next"))
			{
				nextQuery=mJson.getString("next");
			}
		}
		catch(JSONException e)
		{
			//不可到达，
			throw new RuntimeException(e);
		}
		mElement=parmElement;
	}
	public String toString()
	{
		return String.format("a=%s,text=%s,img=%s,\njson=%s\n",aQuery,textQuery,imgQuery,mJson);
	}
	public List<Item> getItems()
	{
		if(mElement==null)
			return null;
		List<Item> list=new LinkedList<Item>();
		Item item=null;
		Elements elements=null;
		if(elementsQuery!=null)
		{
			elements=mElement.select(elementsQuery);
		}
		if(elements==null)
		{
			elements=new Elements(mElement);
		}
		for(Element element:elements)
		{
			Element aElement=null,imgElement=null,textElement=null,contentElement=null;
			item=new Item();
			if(!Tool.isEmpty(textQuery))
				textElement=element.select(textQuery).first();
			if(!Tool.isEmpty(textElement))
				item.title=textElement.text();
			if(!Tool.isEmpty(imgQuery))
				imgElement=element.select(imgQuery).first();
			if(!Tool.isEmpty(imgElement))
				item.image=imgElement.absUrl("src");
			if(!Tool.isEmpty(contentQuery))
				contentElement=element.select(contentQuery).first();
			if(!Tool.isEmpty(contentElement))
				item.content=contentElement.text();
			if(!Tool.isEmpty(aQuery))
				aElement=element.select(aQuery).first();
			if(!Tool.isEmpty(aElement))
				item.url=aElement.absUrl("href");
			list.add(item);
		}
		return list;
	}
	public boolean loadNext()
	{
		boolean hasNext=false;
		if(nextQuery==null||"".equals(nextQuery)||mElement==null)
		{
			return false;
		}
		//直接赋值给mElement,如果没有下一页，以后getItems()就直接返回null,
		mElement=mElement.select(nextQuery).first();
		if(mElement!=null)
		{
			try
			{
				mElement=mConnection.url(mElement.absUrl("href")).execute().parse();
				hasNext=true;
			}
			catch(IOException e)
			{
				hasNext=false;
			}
		}
		return hasNext;
	}
}
