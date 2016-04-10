/* ***************************************************
	^> File Name: Stream.java
	^> Author: AoEiuV020
	^> Mail: 490674483@qq.com
	^> Created Time: 2016/04/02 - 17:31:48
*************************************************** */
package com.aoeiuv020.tool;
import android.content.Context;
import android.content.res.AssetManager;
import java.io.*;
public class Stream
{
	public static final String UTF8="UTF-8";
	public static String defaultEncoding=UTF8;
	public static int bufLenght=4096;
	public static String read(InputStream input)
	{
		return read(input,defaultEncoding);
	}
	public static String read(InputStream input,String charset)
	{
		if(Tool.isEmpty(charset))
			charset=defaultEncoding;
		String result="";
		try
		{
			Reader reader=new InputStreamReader(input,charset);
			result=read(reader);
		}
		catch(UnsupportedEncodingException e)
		{
		}
		return result;
	}
	public static String read(Reader input)
	{
		StringBuffer sb=new StringBuffer();
		char[] buf=new char[bufLenght];
		int len=0;
		try
		{
			while((len=input.read(buf))>0)
			{
				sb.append(buf,0,len);
			}
		}
		catch(IOException e)
		{
		}
		return sb.toString();
	}
	public static String fileRead(String file)
	{
		return fileRead(file,defaultEncoding);
	}
	public static String fileRead(String fileName,String charset)
	{
		String result="";
		File file=new File(fileName);
		result=fileRead(file,charset);
		return result;
	}
	public static String fileRead(File file)
	{
		return fileRead(file,defaultEncoding);
	}
	public static String fileRead(File file,String charset)
	{
		String result="";
		try
		{
			InputStream input=new FileInputStream(file);
			result=read(input,charset);
			input.close();
		}
		catch(IOException e)
		{
		}
		return result;
	}
	public static boolean write(OutputStream output,String context)
	{
		return write(output,context,defaultEncoding);
	}
	public static boolean write(OutputStream output,String context,String charset)
	{
		if(Tool.isEmpty(charset))
			charset=defaultEncoding;
		boolean result=false;
		try
		{
			Writer writer=new OutputStreamWriter(output,charset);
			result=write(writer,context);
		}
		catch(UnsupportedEncodingException e)
		{
		}
		return result;
	}
	public static boolean write(Writer output,String context)
	{
		boolean result=true;
		try
		{
			output.write(context);
			output.flush();
		}
		catch(IOException e)
		{
			result=false;
		}
		return result;
	}
	public static boolean fileWrite(String fileName,String context)
	{
		return fileWrite(fileName,context,defaultEncoding);
	}
	public static boolean fileWrite(String fileName,String context,String charset)
	{
		boolean result=true;
		File file=new File(fileName);
		result=fileWrite(file,context,charset);
		return result;
	}
	public static boolean fileWrite(File file,String context)
	{
		return fileWrite(file,context,defaultEncoding);
	}
	public static boolean fileWrite(File file,String context,String charset)
	{
		boolean result=true;
		try
		{
			OutputStream output=new FileOutputStream(file);
			result=write(output,charset);
			output.close();
		}
		catch(IOException e)
		{
			result=false;
		}
		return result;
	}
	public static String read(String file)
	{
		return fileRead(file);
	}
	public static String read(String fileName,String charset)
	{
		return fileRead(fileName,charset);
	}
	public static String read(File file)
	{
		return fileRead(file);
	}
	public static String read(File file,String charset)
	{
		return fileRead(file,charset);
	}
	public static boolean write(String fileName,String context)
	{
		return fileWrite(fileName,context);
	}
	public static boolean write(String fileName,String context,String charset)
	{
		return fileWrite(fileName,context,charset);
	}
	public static boolean write(File file,String context)
	{
		return fileWrite(file,context);
	}
	public static boolean write(File file,String context,String charset)
	{
		return fileWrite(file,context,charset);
	}
	public static String read(Context context,String fileName)
	{
		return read(context,fileName,defaultEncoding);
	}
	public static String read(Context context,String fileName,String charset)
	{
		String result="";
		try
		{
			InputStream input=context.openFileInput(fileName);
			result=read(input,charset);
		}
		catch(FileNotFoundException e)
		{
		}
		return result;
	}
	public static boolean write(Context context,String fileName,String string)
	{
		return write(context,fileName,string,defaultEncoding);
	}
	public static boolean write(Context context,String fileName,String string,String charset)
	{
		boolean result=true;
		try
		{
			OutputStream output=context.openFileOutput(fileName,Context.MODE_PRIVATE);
			result=write(output,string,charset);
		}
		catch(FileNotFoundException e)
		{
		}
		return result;
	}
	public static String read(AssetManager asset,String fileName)
	{
		return read(asset,fileName,defaultEncoding);
	}
	public static String read(AssetManager asset,String fileName,String charset)
	{
		String result="";
		try
		{
			InputStream input=asset.open(fileName);
			result=read(input,charset);
		}
		catch(IOException e)
		{
		}
		return result;
	}
}
