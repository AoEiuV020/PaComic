/* ***************************************************
	^> File Name: BottomFragment.java
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

public class BottomFragment extends Fragment implements View.OnClickListener
{  
	public static final int MAIN=0;
	public static final int CLASSIFICATION=1;
	public static final int SITE=2;
	private OnItemClickListener mListener=null;
	@Override  
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  
			Bundle savedInstanceState)  
	{  
		View view=inflater.inflate(R.layout.layout_fragment_bottom, container, false);  
		view.findViewById(R.id.tab_main).setOnClickListener(this);
		view.findViewById(R.id.tab_classification).setOnClickListener(this);
		view.findViewById(R.id.tab_site).setOnClickListener(this);
		return view;
	}  
	@Override
	public void onClick(View view)
	{
		switch(view.getId())
		{
			case R.id.tab_main:
				callOnItemClick(MAIN);
				break;
			case R.id.tab_classification:
				callOnItemClick(CLASSIFICATION);
				break;
			case R.id.tab_site:
				callOnItemClick(SITE);
				break;
		}
	}
	private void callOnItemClick(int position)
	{
		if(mListener!=null)
		{
			mListener.onItemClick(position);
		}
	}
	public void setOnItemClickListener(OnItemClickListener listener)
	{
		mListener=listener;
	}
	public interface OnItemClickListener
	{
		public void onItemClick(int position);
	}
}  
