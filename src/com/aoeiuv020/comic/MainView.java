/* ***************************************************
	^> File Name: MainView.java
	^> Author: AoEiuV020
	^> Mail: 490674483@qq.com
	^> Created Time: 2016/04/05 - 00:01:56
*************************************************** */
package com.aoeiuv020.comic;
import com.aoeiuv020.reptile.Reptile;
import com.aoeiuv020.stream.Stream;
import android.content.*;
import android.view.*;
import android.widget.*;
import org.json.*;
public class MainView extends LinearLayout
{
	ListView mListView=null;
	SimpleAdapter mAdapter=null;
	public MainView(Context context)
	{
		super(context);
		setOrientation(LinearLayout.VERTICAL);
		setGravity(Gravity.TOP);
		mListView=new ListView(getContext());
		addView(mListView);
		JSONObject json=null;
		try
		{
			json=new JSONObject(Stream.read(getContext().getAssets(),"sites.json"));
			json=json.getJSONObject(json.keys().next());
		}
		catch(JSONException e)
		{
			throw new RuntimeException("sites.json解析错误，",e);
		}
		Reptile reptile=new Reptile(json);
		mAdapter=new SimpleAdapter(
				getContext(),
				reptile.getData(Reptile.MAIN),
				R.layout.item,
				new String[]{"img","text","url"},
				new int[]{android.R.id.icon,android.R.id.text1,android.R.id.text2}
				);
		mAdapter.setViewBinder(reptile.getViewBinder());
		mListView.setAdapter(mAdapter);
	}
}
