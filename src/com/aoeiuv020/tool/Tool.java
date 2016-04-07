/* ***************************************************
	^> File Name: Tool.java
	^> Author: AoEiuV020
	^> Mail: 490674483@qq.com
	^> Created Time: 2016/04/07 - 21:50:51
*************************************************** */
package com.aoeiuv020.tool;
import android.widget.TextView;
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
}
