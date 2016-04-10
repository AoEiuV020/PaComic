/* ***************************************************
	^> File Name: SelectorInterface.java
	^> Author: AoEiuV020
	^> Mail: 490674483@qq.com
	^> Created Time: 2016/04/11 - 00:27:40
*************************************************** */
package com.aoeiuv020.reptile;
import java.util.*;
public interface SelectorInterface
{
	public List<Object> selectElements(String query,String html);
	public String selectString(String query,String html);
	public List<Object> selectElements(String query,Object html);
	public String selectString(String query,Object html);
	public Object getDocument(String html);
}
