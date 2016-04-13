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
import java.lang.reflect.Field;
import java.util.*;
import org.json.*;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.io.IOException;

public class Selector
{
	private static SelectorInterface mSelectorInterface=new XPathSelector();
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
	public static String select(String query,String url)
	{
		List<Item> list=new LinkedList<Item>();
		String html=Connector.getInstance().load(url);
		Object root=mSelectorInterface.getDocument(html);
		String result=mSelectorInterface.selectString(query,root);
		return result;
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
		Object root=mSelectorInterface.getDocument(html);
		String elementsQuery=Tool.getString(json,"elements");
		List<Object> elements=null;
		if(Tool.isEmpty(elementsQuery))
		{
			elements=new LinkedList<Object>();
			elements.add(root);
		}
		else
			elements=mSelectorInterface.selectElements(elementsQuery,root);
		if(elements==null)
			return null;
		Item item=null;
		for(Object e:elements)
		{
			item=new Item();
			Field[] fields=Item.class.getFields();
			for(Field f:fields)
			{
				String fName=f.getName();
				String fQuery=Tool.getString(json,fName);
				try
				{
					f.set(item,mSelectorInterface.selectString(fQuery,e));
				}
				catch(IllegalAccessException ee)
				{
					Logger.e(ee);
				}
			}
			list.add(item);
		}
		Logger.v("select %s",list);
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
