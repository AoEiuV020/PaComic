package com.aoeiuv020.comic;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.aoeiuv020.reptile.Reptile;
import com.aoeiuv020.tool.Logger;
import com.aoeiuv020.tool.Tool;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by AoEiuV020 on 2016/04/07 - 22:51:03
 */
public class ComicInfoActivity extends Activity implements AdapterView.OnItemClickListener {
    private Reptile mReptile = null;
    private String mUrl = null;
    private ItemAdapter mAdapter = null;
    private ViewGroup mInfo = null;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Logger.v("ComicInfoActivity onCreate");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_activity_comic_info);
        ListView listView = (ListView) findViewById(R.id.listview);
        mInfo = (ViewGroup) getLayoutInflater().inflate(R.layout.layout_comic_info, null);
        listView.addHeaderView(mInfo);
        mAdapter = new ItemAdapter(this, R.layout.layout_item, R.id.item_title, R.id.item_image, R.id.item_content);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
        init();
        loadInfo();
        loadCatalog();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Item item = ((Item) parent.getAdapter().getItem(position));
        String url = item.url;
        if (Tool.isEmpty(url))
            return;
        Intent intent = new Intent();
        intent.setClass(this, ComicPagerActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("sitejson", mReptile.getSiteJson().toString());
        startActivity(intent);
    }

    private void loadCatalog() {
        ComicCatalogLoadAsyncTask task = new ComicCatalogLoadAsyncTask(mReptile, mAdapter, mUrl);
        task.execute();
    }

    private void loadInfo() {
        ComicInfoLoadAsyncTask task = new ComicInfoLoadAsyncTask(mReptile, mInfo, mUrl);
        task.execute();
    }

    private void init() {
        Intent intent = getIntent();
        try {
            String json = intent.getStringExtra("sitejson");
            JSONObject siteJson = new JSONObject(json);
            mReptile = new Reptile(this);
            mReptile.setSite(siteJson);
        } catch (JSONException e) {
            //不可到达，
            if (Main.DEBUG)
                throw new RuntimeException(e);
            finish();
        }
        mUrl = intent.getStringExtra("url");
        if (mReptile == null || mUrl == null) {
            //不可到达，
            if (Main.DEBUG)
                throw new RuntimeException("intent带的值不对");
            finish();
        }
    }
}

class ComicCatalogLoadAsyncTask extends AsyncTask<Void, Integer, List<Item>> {
    Reptile mReptile = null;
    ItemAdapter mAdapter = null;
    String mUrl = null;
    private Throwable mThrowable = null;

    public ComicCatalogLoadAsyncTask(Reptile reptile, ItemAdapter adapter, String url) {
        mReptile = reptile;
        mAdapter = adapter;
        mUrl = url;
    }

    @Override
    protected List<Item> doInBackground(Void... parms) {
        List<Item> list = null;
        try {
            list = mReptile.getCatalog(mUrl);
        } catch (RuntimeException e) {
            //任何异常都表示没有内容了，
            mThrowable = e.getCause();
            if (Main.DEBUG)
                throw e;
        }
        return list;
    }

    @Override
    protected void onPostExecute(List<Item> list) {
        if (list != null) {
            mAdapter.addAll(list);
        }
    }
}

class ComicInfoLoadAsyncTask extends AsyncTask<Void, Integer, Item> {
    Reptile mReptile = null;
    String mUrl = null;
    TextView mTitle = null;
    TextView mContext = null;
    ImageView mImageView = null;
    private Throwable mThrowable = null;

    public ComicInfoLoadAsyncTask(Reptile reptile, View view, String url) {
        mReptile = reptile;
        mUrl = url;
        mTitle = (TextView) view.findViewById(R.id.info_title);
        mContext = (TextView) view.findViewById(R.id.info_content);
        mImageView = (ImageView) view.findViewById(R.id.info_image);
    }

    @Override
    protected Item doInBackground(Void... parms) {
        Item item = null;
        try {
            item = mReptile.getComicInfo(mUrl);
        } catch (RuntimeException e) {
            //任何异常都表示没有内容了，
            mThrowable = e.getCause();
            Logger.e(e);
        }
        return item;
    }

    @Override
    protected void onPostExecute(Item item) {
        if (item != null) {
            if (!Tool.isEmpty(item.title))
                mTitle.setText(item.title);
            if (!Tool.isEmpty(item.content))
                mContext.setText(item.content);
            if (!Tool.isEmpty(item.image))
                ImageLoader.showImage(mImageView, item.image);
        }
    }
}
