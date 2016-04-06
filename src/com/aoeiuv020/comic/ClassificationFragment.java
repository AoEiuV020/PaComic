/* ***************************************************
	^> File Name: ClassificationFragment.java
	^> Author: AoEiuV020
	^> Mail: 490674483@qq.com
	^> Created Time: 2016/04/07 - 02:01:42
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

public class ClassificationFragment extends Fragment implements View.OnClickListener
{
	private Reptile mReptile=null;
	public ClassificationFragment(Reptile reptile)
	{
		mReptile=reptile;
	}
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{
		TextView view=new TextView(getActivity());
		view.setText("ClassificationFragment");
		return view;
	}
	@Override
	public void onClick(View view)
	{
	}
}
