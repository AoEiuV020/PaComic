/* ***************************************************
	^> File Name: Tool.java
	^> Author: AoEiuV020
	^> Mail: 490674483@qq.com
	^> Created Time: 2016/04/07 - 21:50:51
*************************************************** */
package com.aoeiuv020.tool;
import android.widget.TextView;
import org.json.*;
public class Tool
{
	private Tool()
	{
	}
	public static boolean isEmpty(Object object)
	{
		try
		{
			if(object==null)
				return true;
			if(object instanceof CharSequence)
				return "".equals(object);
			if(object instanceof TextView)
				return "".equals(((TextView)object).getText());
		}
		catch(Exception e)
		{
			//不抛异常;
			return true;
		}
		return false;
	}
	public static void put(JSONObject json,String key,Object obj)
	{
		if(isEmpty(key))
			return;
		if(obj==null)
			obj=JSONObject.NULL;
		try
		{
			json.put(key,obj);
		}
		catch(JSONException e)
		{
		}
	}
	public static String getString(JSONObject json,String str)
	{
		String result=null;
		try
		{
			result=json.getString(str);
		}
		catch(JSONException e)
		{
		}
		return result;
	}
	public static boolean isEmpty(JSONObject json,String str)
	{
		return isEmpty(getString(json,str));
	}
}
