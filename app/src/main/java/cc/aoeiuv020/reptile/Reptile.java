package cc.aoeiuv020.reptile;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import cc.aoeiuv020.comic.Item;
import cc.aoeiuv020.tool.Logger;
import cc.aoeiuv020.tool.Tool;

/**
 * Reptile reptile=new Reptile(mContext);
 * reptile.setSite(siteJson); //设置网站，不耗时，不抛异常，
 * List<Item> list=reptile.getItems(); //耗时，只抛RuntimeException,
 * reptile.loadNext(); //耗时，不抛异常，返回是否有Next,boolean
 * reptile.setClassification(3); //设置分类，不耗时，
 * List<Item> classList=reptile.getClassifications(); //耗时，
 * Created by AoEiuV020 on 2016/04/05 - 00:22:32
 */
public class Reptile {
    private static Context mContext = null;
    private static JSONObject mAllSitesJson = null;
    private String mClassificationUrl = null;
    private JSONObject mSiteJson = null;
    private String mMode = null;
    private String PageUrl = null;

    public Reptile() {
    }

    public Reptile(Context context) {
        this();
        setContext(context);
    }

    public static void setContext(Context context) {
        mContext = context;
    }

    /**
     * 耗时方法，
     */
    public static List<Item> getSites(JSONObject sites) {
        Logger.v("getSites sites=%s", sites);
        mAllSitesJson = sites;
        return getSites();
    }

    public static List<Item> getSites() {
        List<Item> list = new LinkedList<Item>();
        List<JSONObject> sitesList = getSitesJsonList();
        if (sitesList == null)
            return null;
        for (JSONObject site : sitesList) {
            Item item = null;
            try {
                Reptile reptile = new Reptile();
                reptile.setSite(site);
                item = reptile.getSiteInfo();
            } catch (Exception e) {
                //无视任何错误，
                Logger.e(e);
            } finally {
                //就算有网站信息加载错误也加上这个网站，
                //毕竟只是网站信息爬不出来，说不定网站其他还能爬的，
                list.add(item);
            }
            Logger.v("getSiteInfo %s", item);
        }
        return list;
    }

    public static void setSitesJson(JSONObject sites) {
        mAllSitesJson = sites;
    }

    public static List<JSONObject> getSitesJsonList() {
        if (mAllSitesJson == null)
            return null;
        List<JSONObject> list = null;
        list = new LinkedList<JSONObject>();
        Iterator<String> iterator = mAllSitesJson.keys();
        while (iterator.hasNext()) {
            try {
                String name = iterator.next();
                JSONObject site = mAllSitesJson.getJSONObject(name);
                String enable = Tool.getString(site, "enable");
                if (enable != null && enable.equals("false")) {
                } else {
                    list.add(site);
                }
            } catch (JSONException e) {
                Logger.e(e);
            }
        }
        return list;
    }

    public void setNextPageUrl(String url) {
    }

    /**
     * 耗时方法，
     */
    public List<Item> getPages(String str) {
        if (str == null)
            str = PageUrl;
        List<Item> list = getItems("page", str);
        return list;
    }

    public boolean loadNextPage() {
        Logger.v("loadNextPage ");
        if (mSiteJson == null)
            return false;
        String next = null;
        try {
            String query = mSiteJson.getJSONObject("selector").getJSONObject("page").getString("next");
            String sJs = mSiteJson.getJSONObject("selector").getJSONObject("page").getString("jsenable");
            boolean bJs = false;
            if (sJs != null && sJs.equals("true")) {
                bJs = true;
            }
            Logger.v("query=%s,js=%s,bjs=%s", query, sJs, bJs);
            next = Selector.select(query, null, bJs);
        } catch (JSONException e) {
            throw new RuntimeException("json中没有" + mMode + "的选择器", e);
        }
        Logger.v("loadNext ok %s", next);
        if (next == null) {
            Logger.v("no next");
            PageUrl = null;
            return false;
        }
        PageUrl = next;
        return true;
    }

    /**
     * 耗时方法，
     */
    public List<Item> getCatalog(String str) {
        return getItems("catalog", str);
    }

    /**
     * 耗时方法，
     */
    public Item getComicInfo(String str) {
        return getItems("info", str).get(0);
    }

    /**
     * 耗时方法，
     */
    public Item getSiteInfo() {
        if (mSiteJson == null)
            return null;
        Item item = null;
        item = getItems("site").get(0);
        Logger.v("getSiteInfo %s", item);
        return item;
    }

    public void setSite(int index) {
        Logger.v("setSite i=" + index);
        JSONObject site = getSitesJsonList().get(index);
        setSite(site);
    }

    public void setSite(JSONObject json) {
        mSiteJson = json;
        Connector.getInstance().putAll(mSiteJson);
        mClassificationUrl = null;
    }

    public JSONObject getSiteJson() {
        return mSiteJson;
    }

    public boolean loadNext() {
        Logger.v("loadNext %s", mClassificationUrl);
        if (mSiteJson == null || Tool.isEmpty(mClassificationUrl))
            return false;
        String next = null;
        try {
            String query = mSiteJson.getJSONObject("selector").getJSONObject(mMode).getString("next");
            String sJs = mSiteJson.getJSONObject("selector").getJSONObject("page").getString("jsenable");
            boolean bJs = false;
            if (sJs != null && sJs.equals("true")) {
                bJs = true;
            }
            next = Selector.select(query, mClassificationUrl, bJs);
        } catch (JSONException e) {
            throw new RuntimeException("json中没有" + mMode + "的选择器", e);
        }
        Logger.v("loadNextPage ok %s", next);
        if (next == null || next.equals(mClassificationUrl))
            return false;
        mClassificationUrl = next;
        return true;
    }

    /**
     * 不抛异常，
     * 所有错误都无视，
     */
    public void setSearch(String sSearch) {
        if (mSiteJson == null)
            return;
        mMode = "search";
        String encoding = null;
        try {
            JSONObject searchSelectorJson = mSiteJson.getJSONObject("selector").getJSONObject("search");
            if (searchSelectorJson.has("encoding"))
                encoding = searchSelectorJson.getString("encoding");
            else
                encoding = Connector.getInstance().getEncoding();
            sSearch = URLEncoder.encode(sSearch, encoding);
            String sUrl = searchSelectorJson.getString("searchurl");
            sUrl = String.format(sUrl, sSearch);
            mClassificationUrl = sUrl;
            Logger.v("setSearch %s", mClassificationUrl);
        } catch (UnsupportedEncodingException e) {
            Logger.e(e);
        } catch (JSONException e) {
            Logger.e(e);
        }
    }

    public void setClassification(int index) {
    }

    public void setClassification(String url) {
        mClassificationUrl = url;
        mMode = "item";
    }

    /**
     * 耗时方法，
     */
    public List<Item> getClassifications() {
        return getItems("classification", Connector.getInstance().getBaseurl());
    }

    /**
     * 耗时方法，
     */
    public List<Item> getItems() {
        if (mClassificationUrl == null)
            setClassification(getClassifications().get(0).url);
        return getItems(mMode, mClassificationUrl);
    }

    public List<Item> getItems(String str) {
        return getItems(str, Connector.getInstance().getBaseurl());
    }

    public List<Item> getItems(String str, String url) {
        if (mSiteJson == null)
            return null;
        List<Item> list = null;
        Logger.v("getItems %s,%s", str, url);
        try {
            JSONObject selectorJson = mSiteJson.getJSONObject("selector").getJSONObject(str);
            list = Selector.select(selectorJson, url);
        } catch (JSONException e) {
            throw new RuntimeException("json中没有" + str + "的选择器", e);
        }
        return list;
    }
}
