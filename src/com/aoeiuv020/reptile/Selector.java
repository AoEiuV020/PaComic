/* ***************************************************
	^> File Name: Selector.java
	^> Author: AoEiuV020
	^> Mail: 490674483@qq.com
	^> Created Time: 2016/04/07 - 01:06:58
*************************************************** */
package com.aoeiuv020.reptile;
import com.aoeiuv020.comic.Item;
import android.content.*;
import org.json.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import java.util.*;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.ByteArrayInputStream;
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
			elementsQuery=mJson.getString("elements");
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
			throw new RuntimeException(e);
		}
		mElement=parmElement;
	}
	public List<Item> getItems()
	{
		List<Item> list=new LinkedList<Item>();
		Item item=null;
		for(Element element:mElement.select(elementsQuery))
		{
			Element aElement=null,imgElement=null,textElement=null,contentElement=null;
			item=new Item();
			if(textQuery!=null)
				textElement=element.select(textQuery).first();
			if(textElement!=null)
				item.title=textElement.text();
			if(imgQuery!=null)
				imgElement=element.select(imgQuery).first();
			if(imgElement!=null)
				item.image=imgElement.absUrl("src");
			if(contentQuery!=null)
				contentElement=element.select(contentQuery).first();
			if(contentElement!=null)
				item.image=contentElement.absUrl("src");
			if(aQuery!=null)
				aElement=element.select(aQuery).first();
			if(aElement!=null)
				item.url=aElement.absUrl("href");
			list.add(item);
		}
		return list;
	}
	public boolean loadNext()
	{
		boolean hasNext=false;
		if(nextQuery==null||"".equals(nextQuery))
		{
			return false;
		}
		Element next=mElement.select(nextQuery).first();
		if(next!=null)
		{
			try
			{
				mElement=mConnection.url(next.absUrl("href")).execute().parse();
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
