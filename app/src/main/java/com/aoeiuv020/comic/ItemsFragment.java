package com.aoeiuv020.comic;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.aoeiuv020.reptile.Reptile;
import com.aoeiuv020.tool.Tool;

/**
 * Created by AoEiuV020 on 2016/04/06 - 19:24:19
 */
public class ItemsFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ListView mListView = null;
    private ItemLoadAsyncTask mTask = null;
    private Button bLoadMore = null;
    private Reptile mReptile = null;
    private ItemAdapter mAdapter = null;

    public ItemsFragment() {
    }

    public ItemsFragment(Reptile reptile) {
        mReptile = reptile;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mReptile == null) {
            mReptile = ((Main) getActivity()).getReptitle();
        }
        View view = inflater.inflate(R.layout.layout_fragment_items, container, false);
        mListView = (ListView) view.findViewById(R.id.listview);
        View vLoadMore = inflater.inflate(R.layout.layout_load_more, null);
        bLoadMore = (Button) vLoadMore.findViewById(R.id.button_load_more);
        bLoadMore.setOnClickListener(this);
        mListView.addFooterView(vLoadMore);
        mAdapter = new ItemAdapter(getActivity(), R.layout.layout_item, R.id.item_title, R.id.item_image, R.id.item_content);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        loadItems();
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent instanceof ListView)
            position -= ((ListView) parent).getHeaderViewsCount();
        String url = ((Item) parent.getAdapter().getItem(position)).url;
        if (Tool.isEmpty(url))
            return;
        Intent intent = new Intent();
        intent.setClass(getActivity(), ComicInfoActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("sitejson", mReptile.getSiteJson().toString());
        getActivity().startActivity(intent);
    }

    private void loadItems() {
        mTask = new ItemLoadAsyncTask(mReptile, mAdapter, bLoadMore);
        mTask.execute(ItemLoadAsyncTask.FIRST);
    }

    @Override
    public void onClick(View view) {
        if (mTask.getStatus() == AsyncTask.Status.FINISHED) {
            mTask = new ItemLoadAsyncTask(mReptile, mAdapter, (Button) view);
            mTask.execute(ItemLoadAsyncTask.NEXT);
        }
    }
}
