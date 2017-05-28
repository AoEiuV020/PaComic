package cc.aoeiuv020.comic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.InputStream;

import cc.aoeiuv020.reptile.Connector;
import cc.aoeiuv020.tool.Tool;

/**
 * Created by AoEiuV020 on 2016/04/06 - 16:19:22
 */
public class ImageLoader {
    private static BitmapCachea mCache = new BitmapCachea();

    /**
     * 不耗时方法，
     */
    public static void showImage(ImageView imageView, String url) {
        if (imageView == null)
            return;
        if (Tool.isEmpty(url)) {
            imageView.setImageBitmap(null);
            return;
        }
        Bitmap bitmap = getBitmap(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setTag(url);
            ShowImageAsyncTask task = new ShowImageAsyncTask(imageView, url);
            task.execute();
        }
    }

    /**
     * 耗时方法，
     * 失败返回null;
     */
    public static Bitmap downloadBitmap(String str) {
        Bitmap bitmap = null;
        try {
            InputStream input = Connector.getInstance().getInputStream(str);
            bitmap = BitmapFactory.decodeStream(input);
            if (bitmap != null) {
                mCache.put(str, bitmap);
            }
        } catch (Exception e) {
        }
        return bitmap;
    }

    /**
     * 不耗时方法，
     */
    public static Bitmap getBitmap(String str) {
        Bitmap bitmap = mCache.get(str);
        return bitmap;
    }
}

class ShowImageAsyncTask extends AsyncTask<Void, Integer, Bitmap> {
    private static Object mLock = new Object();
    ImageView mImageView = null;
    String mUrl = null;

    public ShowImageAsyncTask(ImageView imageView, String url) {
        mImageView = imageView;
        mUrl = url;
    }

    @Override
    protected void onPreExecute() {
        mImageView.setImageResource(android.R.drawable.ic_menu_gallery);
    }

    @Override
    protected Bitmap doInBackground(Void... parms) {
        return ImageLoader.downloadBitmap(mUrl);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (mUrl == mImageView.getTag()) {
            if (bitmap != null) {
                mImageView.setImageBitmap(bitmap);
            } else {
                mImageView.setImageResource(android.R.drawable.ic_menu_report_image);
            }
        }
    }
}

/**
 * 现在是内存cache，
 * 以后加上外存cache,
 * 只用使用两个方法，
 * Bitmap get(String url);
 * void put(String url,Bitmap bitmap);
 * 不抛空指针异常，
 */
class BitmapCachea {
    private LruCache<String, Bitmap> mLruCache;

    public BitmapCachea() {
        long cacheSize = Runtime.getRuntime().maxMemory() / 4;
        mLruCache = new LruCache<String, Bitmap>((int) cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //无视String的大小？
                return value.getByteCount();
            }
        };
    }

    public Bitmap get(String url) {
        if (url == null) {
            return null;
        }
        return mLruCache.get(url);
    }

    public void put(String url, Bitmap bitmap) {
        if (url != null && bitmap != null) {
            mLruCache.put(url, bitmap);
        }
    }
}
