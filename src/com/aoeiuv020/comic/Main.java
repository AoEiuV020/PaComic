/* ***************************************************
	^> File Name: Main.java
	^> Author: AoEiuV020
	^> Mail: 490674483@qq.com
	^> Created Time: 2016/04/06 - 05:44:25
*************************************************** */
package com.aoeiuv020.comic;
import com.aoeiuv020.stream.Stream;
import com.aoeiuv020.reptile.Reptile;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.content.*;
import android.view.*;
import android.os.*;
import android.widget.*;

import java.util.*;

import org.json.*;

public class Main extends Activity implements BottomFragment.OnItemClickListener
{
	Fragment mContentFragment=null,mTitleFragment=null,mBottomFragment=null;
	FragmentTransaction mFragmentTransaction=null;
	JSONObject mSiteJson=null;
	Reptile mReptile=null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		first();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
		mFragmentTransaction=getFragmentManager().beginTransaction();
		mReptile=new Reptile();
		setDefaultSite();
		setDefaultFragment();
    }
	@Override
	public void onItemClick(int position)
	{
		changeTab(position);
	}
	private void changeTab(int position)
	{
		switch(position)
		{
			case BottomFragment.MAIN:
				break;
			case BottomFragment.CLASSIFICATION:
				break;
			case BottomFragment.SITE:
				break;
		}
	}
	private void setDefaultSite()
	{
		try
		{
			JSONObject sitesJson=new JSONObject(Stream.read(this,"sites.json"));
			mSiteJson=sitesJson.getJSONObject(sitesJson.keys().next());
			mReptile.setSite(mSiteJson);
		}
		catch(JSONException e)
		{
			throw new RuntimeException("sites.json解析出错",e);
		}
	}
	private void setDefaultFragment()
	{
		mContentFragment=new ItemsFragment(mReptile);
		mFragmentTransaction.replace(R.id.fragment_content,mContentFragment);
		mTitleFragment=new TitleFragment();
		mFragmentTransaction.replace(R.id.fragment_title,mTitleFragment);
		mBottomFragment=new BottomFragment();
		mFragmentTransaction.replace(R.id.fragment_bottom,mBottomFragment);
		mFragmentTransaction.commit();
	}
	private void first()
	{
		SharedPreferences setting = getSharedPreferences("app", MODE_PRIVATE);  
		Boolean user_first = setting.getBoolean("first",true);  
		if(user_first)
		{
			setting.edit().putBoolean("first", false).commit();  
		}
		else
		{
			return;
		}
		//把assets中的配置文件写到files，
		String sitesjson="sites.json";
		Stream.write(this,sitesjson,Stream.read(this.getAssets(),sitesjson));
	}
}
