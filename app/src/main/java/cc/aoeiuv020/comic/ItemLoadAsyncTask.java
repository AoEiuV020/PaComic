package cc.aoeiuv020.comic;

import android.os.AsyncTask;
import android.widget.Button;

import java.util.List;

import cc.aoeiuv020.reptile.Reptile;

/**
 * Created by AoEiuV020 on 2016/04/06 - 19:10:07
 */
public class ItemLoadAsyncTask extends AsyncTask<Integer, Integer, List<Item>> {
    public static final int FIRST = 0;
    public static final int NEXT = 1;
    private static final boolean DEBUG = Main.DEBUG;
    Reptile mReptile = null;
    ItemAdapter mAdapter = null;
    Button mButton = null;
    //加载失败的原因，
    private Throwable mThrowable = null;

    public ItemLoadAsyncTask(Reptile reptile, ItemAdapter adapter, Button button) {
        mReptile = reptile;
        mAdapter = adapter;
        mButton = button;
    }

    @Override
    protected void onPreExecute() {
        mButton.setClickable(false);
        mButton.setText("加载中");
    }

    @Override
    protected List<Item> doInBackground(Integer... parms) {
        List<Item> list = null;
        try {
            switch (parms[0]) {
                case NEXT:
                    if (!mReptile.loadNext())
                        break;
                case FIRST:
                    list = mReptile.getItems();
                    break;
            }
        } catch (RuntimeException e) {
            //任何异常都表示没有内容了，
            mThrowable = e.getCause();
            if (Main.DEBUG) throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    protected void onPostExecute(List<Item> list) {
        if (list == null) {
            mButton.setText("没有了");
        } else {
            mAdapter.addAll(list);
            mButton.setText("继续加载");
        }
        mButton.setClickable(true);
    }
}
