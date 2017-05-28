/* ***************************************************
	^> File Name: TitleFragment.java
	^> Author: AoEiuV020
	^> Mail: 490674483@qq.com
	^> Created Time: 2016/04/06 - 21:19:41
*************************************************** */
package com.aoeiuv020.comic;

import android.app.Fragment;  
import android.os.Bundle;  
import android.view.LayoutInflater;  
import android.view.View;  
import android.view.ViewGroup;  

public class TitleFragment extends Fragment implements View.OnClickListener
{  

	@Override  
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  
			Bundle savedInstanceState)  
	{  
		View view=inflater.inflate(R.layout.layout_fragment_title, container, false);  
		view.setOnClickListener(this);
		return view;
	}  
	@Override
	public void onClick(View view)
	{

	}

}  
