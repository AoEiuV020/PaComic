/* ***************************************************
	^> File Name: ItemAdapter.java
	^> Author: AoEiuV020
	^> Mail: 490674483@qq.com
	^> Created Time: 2016/04/06 - 05:17:24
*************************************************** */
package com.aoeiuv020.comic;
import com.aoeiuv020.tool.Tool;
import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;
/**
  * layoutId 布局文件id，
  * idList View的id，依次是，
  * TextView 显示 item.text,
  * ImageView 显示 item.image,
  * TextView 显示 item.content,
  */
public class ItemAdapter extends BaseAdapter
{
	private Context mContext=null;
	private List<Item> mList=null;
	private LayoutInflater mInflater=null;
	private Object mLock=new Object();
	private int mLayoutId=0;
	private List<Integer> mIdList=null;
	public ItemAdapter(Context context,int layoutId,Integer... idArray)
	{
		mContext=context;
		mList=new LinkedList<Item>();
		mIdList=new LinkedList<Integer>();
		mLayoutId=layoutId;
		mIdList.addAll(Arrays.asList(idArray));
		mInflater=LayoutInflater.from(context);
	}
	public void add(Item item)
	{
		synchronized(mLock)
		{
			mList.add(item);
		}
		notifyDataSetChanged();
	}
	public void addAll(Collection<Item> list)
	{
		synchronized(mLock)
		{
			mList.addAll(list);
		}
		notifyDataSetChanged();
	}
	@Override
	public int getCount()
	{
		return mList.size();
	}
	@Override
	public Object getItem(int position)
	{
		return mList.get(position);
	}
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	@Override
	public View getView(int position,View convertView,ViewGroup parent)
	{
		ViewHolder viewHolder=null;
		if(convertView==null)
		{
			convertView=mInflater.inflate(mLayoutId,null);
			viewHolder=new ViewHolder();
			switch(mIdList.size())
			{
				default:
				case 3:
					viewHolder.content=(TextView)findViewById(convertView,mIdList.get(2));
				case 2:
					viewHolder.image=(ImageView)findViewById(convertView,mIdList.get(1));
				case 1:
					viewHolder.title=(TextView)findViewById(convertView,mIdList.get(0));
				case 0:
					break;
			}
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder=(ViewHolder)convertView.getTag();
		}
		Item item=mList.get(position);
		if(item!=null)
		{
			setText(viewHolder.title,item.title);
			loadImage(viewHolder.image,item.image);
			setText(viewHolder.content,item.content);
		}
		return convertView;
	}
	private View findViewById(View parent,Integer id)
	{
		View view=null;
		try
		{
			view=parent.findViewById(id);
		}
		catch(NullPointerException e)
		{
			//skip;
		}
		return view;
	}
	private void setText(TextView textView,String text)
	{
		if(textView==null||text==null)
			return;
		textView.setText(text);
	}
	private void loadImage(ImageView imageView,String url)
	{
		//if语句多余，但是看着爽一点，
		if(imageView==null||url==null||url.equals(""))
			return;
		ImageLoader.showImage(imageView,url);
	}
	public class ViewHolder
	{
		public ImageView image;
		public TextView title;
		public TextView content;
	}
}
