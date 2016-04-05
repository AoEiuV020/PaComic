package com.aoeiuv020.comic;
import com.aoeiuv020.stream.Stream;

import android.app.Activity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.content.*;
import android.view.View;

public class Main extends Activity
{
	private View mainView=null;
	private BroadcastReceiver receiver=null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		first();
		mainView=new MainView(this);
        setContentView(mainView);
		final MainView fMainView=(MainView)mainView;
		receiver=new BroadcastReceiver(){
			@Override
			public void onReceive(Context context,Intent intent)
			{
				fMainView.notifyDataSetChanged();
			}
		};
		IntentFilter filter=new IntentFilter();
		filter.addAction("com.aoeiuv020.comic.notifyDataSetChanged");
		registerReceiver(receiver,filter);
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
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		unregisterReceiver(receiver);
	}
}
