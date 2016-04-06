/* ***************************************************
	^> File Name: ImageLoader.java
	^> Author: AoEiuV020
	^> Mail: 490674483@qq.com
	^> Created Time: 2016/04/06 - 16:19:22
*************************************************** */
package com.aoeiuv020.comic;
import android.widget.ImageView;
import android.os.AsyncTask;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;
import java.net.URL;
import java.io.InputStream;

public class ImageLoader
{
	private static BitmapCachea mCache=new BitmapCachea();
	/**
	  * 不耗时方法，
	  */
	public static void showImage(ImageView imageView,String url)
	{
		if(imageView==null||url==null||url.equals(""))
			return;
		ShowImageAsyncTask task=new ShowImageAsyncTask(imageView,url);
		imageView.setTag(url);
		task.execute();
	}
	/**
	  * 耗时方法，
	  * 失败返回null;
	  */
	public static Bitmap getBitmap(String str)
	{
		Bitmap bitmap=mCache.get(str);
		if(bitmap==null)
		{
			try
			{
				URL url=new URL(str);
				InputStream input=url.openStream();
				bitmap=BitmapFactory.decodeStream(input);
				if(bitmap!=null)
				{
					mCache.put(str,bitmap);
				}
			}
			catch(Exception e)
			{
			}
		}
		return bitmap;
	}
}
class ShowImageAsyncTask extends AsyncTask<Void,Integer,Bitmap>
{
	ImageView mImageView=null;
	String mUrl=null;
	public ShowImageAsyncTask(ImageView imageView,String url)
	{
		mImageView=imageView;
		mUrl=url;
	}
	@Override
	protected void onPreExecute()
	{
		mImageView.setImageResource(android.R.drawable.ic_menu_gallery);
	}
	@Override
	protected Bitmap doInBackground(Void... parms)
	{
		return ImageLoader.getBitmap(mUrl);
	}
	@Override
	protected void onPostExecute(Bitmap bitmap)
	{
		if(mUrl==mImageView.getTag())
		{
			if(bitmap!=null)
			{
				mImageView.setImageBitmap(bitmap);
			}
			else
			{
				mImageView.setImageResource(android.R.drawable.ic_menu_report_image);
			}
		}
	}
}
/**
  * 现在是内存cache，
  * 以后加上外存cache,
  * 只用使用两个方法，
  * Bitmap get(String url);
  * void put(String url,Bitmap bitmap);
  * 不抛空指针异常，
  */
class BitmapCachea
{
	private LruCache<String,Bitmap> mLruCache;
	public BitmapCachea()
	{
		long cacheSize=Runtime.getRuntime().maxMemory()/4;
		mLruCache=new LruCache<String,Bitmap>((int)cacheSize)
		{
			@Override
			protected int sizeOf(String key,Bitmap value)
			{
				//无视String的大小？
				return value.getByteCount();
			}
		};
	}
	public Bitmap get(String url)
	{
		if(url==null)
		{
			return null;
		}
		return mLruCache.get(url);
	}
	public void put(String url,Bitmap bitmap)
	{
		if(url!=null&&bitmap!=null)
		{
			mLruCache.put(url,bitmap);
		}
	}
}
