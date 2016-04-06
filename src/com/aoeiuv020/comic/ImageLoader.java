/* ***************************************************
	^> File Name: ImageLoader.java
	^> Author: AoEiuV020
	^> Mail: 490674483@qq.com
	^> Created Time: 2016/04/06 - 16:19:22
*************************************************** */
package com.aoeiuv020.comic;
import android.widget.ImageView;
import android.os.AsyncTask;
import android.graphics.drawable.Drawable;
import java.net.URL;
import java.io.InputStream;

public class ImageLoader
{
	public static void showImage(ImageView imageView,String url)
	{
		ImageLoaderAsyncTask task=new ImageLoaderAsyncTask(imageView);
		task.execute(url);
	}
}
class ImageLoaderAsyncTask extends AsyncTask<String,Integer,Drawable>
{
	ImageView mImageView=null;
	public ImageLoaderAsyncTask(ImageView imageView)
	{
		mImageView=imageView;
	}
	@Override
	protected void onPreExecute()
	{
		mImageView.setImageResource(android.R.drawable.ic_menu_gallery);
	}
	@Override
	protected Drawable doInBackground(String... parms)
	{
		Drawable drawable=null;
		try
		{
			URL url=new URL(parms[0]);
			InputStream input=url.openStream();
			drawable=Drawable.createFromStream(input,null);
		}
		catch(Exception e)
		{

		}
		return drawable;
	}
	@Override
	protected void onPostExecute(Drawable drawable)
	{
		if(drawable!=null)
		{
			mImageView.setImageDrawable(drawable);
		}
		else
		{
			mImageView.setImageResource(android.R.drawable.ic_menu_report_image);
		}
	}
}
