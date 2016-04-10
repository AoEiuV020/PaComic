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
import com.aoeiuv020.tool.Logger;
import android.content.*;
import android.util.Log;
import org.json.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import java.lang.reflect.Field;
import java.util.*;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.io.IOException;

public class Selector
{
	private static SelectorInterface mSelectorInterface=new XPathSelector();
	public Selector(JSONObject json,Connection parmConnection,Element parmElement)
	{
	}
	/**
	 * 耗时方法，
	 */
	public static List<Item> select(JSONObject json)
	{
		return select(json,Connector.getInstance().getBaseurl());
	}
	/**
	 * 耗时方法，
	 */
	public static List<Item> select(JSONObject json,String url)
	{
		if(Tool.isEmpty(url))
			return null;
		List<Item> list=new LinkedList<Item>();
		String html=Connector.getInstance().load(url);
		String elementsQuery=Tool.getString(json,"elements");
		List<Object> elements=null;
		if(Tool.isEmpty(elementsQuery))
		{
			elements=new LinkedList<Object>();
			elements.add(mSelectorInterface.getDocument(html));
		}
		else
			elements=mSelectorInterface.selectElements(elementsQuery,html);
		Item item=null;
		for(Object e:elements)
		{
			Logger.v("select %s %s",url,e.getClass().getName());
			item=new Item();
			Field[] fields=Item.class.getFields();
			Logger.v("fields length %d",fields.length);
			for(Field f:fields)
			{
				Logger.v("fileds %s",f);
				String fName=f.getName();
				String fQuery=Tool.getString(json,fName);
				try
				{
					f.set(item,mSelectorInterface.selectString(fQuery,e));
				}
				catch(IllegalAccessException ee)
				{
					if(Logger.DEBUG)
						throw new RuntimeException(ee);
				}
			}
			list.add(item);
		}
		return list;
	}
	public String toString()
	{
		return null;
	}
	public List<Item> getItems()
	{
		return null;
	}
	public boolean loadNext()
	{
		return false;
	}
}
