/* ***************************************************
	^> File Name: ItemAdapter.java
	^> Author: AoEiuV020
	^> Mail: 490674483@qq.com
	^> Created Time: 2016/04/06 - 05:17:24
*************************************************** */
package com.aoeiuv020.comic;
import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;
public class ItemAdapter extends BaseAdapter
{
	private Context mContext=null;
	private List<Item> mList=null;
	private LayoutInflater mInflater=null;
	private Object mLock=new Object();
	public ItemAdapter(Context context,List<Item> list)
	{
		mContext=context;
		if(list!=null)
		{
			mList=list;
		}
		else
		{
			mList=new LinkedList<Item>();
		}
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
			convertView=mInflater.inflate(R.layout.layout_item,null);
			viewHolder=new ViewHolder();
			viewHolder.image=(ImageView)convertView.findViewById(R.id.item_image);
			viewHolder.title=(TextView)convertView.findViewById(R.id.item_title);
			viewHolder.content=(TextView)convertView.findViewById(R.id.item_content);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder=(ViewHolder)convertView.getTag();
		}
		Item item=mList.get(position);
		try
		{
			loadImage(viewHolder.image,item.image);
			viewHolder.title.setText(item.title);
			viewHolder.content.setText(item.url);
		}
		catch(NullPointerException e)
		{
		}
		return convertView;
	}
	private void loadImage(ImageView imageView,String url)
	{
		ImageLoader.showImage(imageView,url);
	}
	class ViewHolder
	{
		public ImageView image;
		public TextView title;
		public TextView content;
	}
}
