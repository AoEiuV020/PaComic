/* ***************************************************
	^> File Name: SiteFragment.java
	^> Author: AoEiuV020
	^> Mail: 490674483@qq.com
	^> Created Time: 2016/04/07 - 02:05:40
*************************************************** */
package com.aoeiuv020.comic;
import com.aoeiuv020.stream.Stream;
import com.aoeiuv020.reptile.Reptile;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.content.*;
import android.view.*;
import android.os.*;
import android.widget.*;

import java.util.*;

import org.json.*;

public class SiteFragment extends Fragment implements View.OnClickListener
{
	private Reptile mReptile=null;
	public SiteFragment()
	{}
	public SiteFragment(Reptile reptile)
	{
		mReptile=reptile;
	}
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{
		TextView view=new TextView(getActivity());
		view.setText("SiteFragment");
		return view;
	}
	@Override
	public void onClick(View view)
	{
	}
}
