package cc.aoeiuv020.comic;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import org.json.JSONException;
import org.json.JSONObject;

import cc.aoeiuv020.reptile.Reptile;
import cc.aoeiuv020.reptile.WebViewDaemon;
import cc.aoeiuv020.stream.Stream;
import cc.aoeiuv020.widget.SimpleDialog;

/**
 * Created by AoEiuV020 on 2016/04/06 - 05:44:25
 */
public class Main extends Activity implements BottomFragment.OnItemClickListener, OnTaskFinishListener {
    public static final boolean DEBUG = true;
    JSONObject mSiteJson = null;
    Reptile mReptile = null;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        first();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        mReptile = new Reptile(this);
        WebViewDaemon.getInstance().setContext(this);
        setDefaultSite();
        setDefaultFragment();
    }

    @Override
    public void onFinish() {
        changeTab(BottomFragment.MAIN);
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null)
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onItemClick(int position) {
        changeTab(position);
    }

    public Reptile getReptitle() {
        return mReptile;
    }

    private void changeTab(int position) {
        Fragment fContent = null;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        switch (position) {
            case BottomFragment.MAIN:
                fContent = new ItemsFragment();
                break;
            case BottomFragment.CLASSIFICATION:
                fContent = new ClassificationFragment();
                break;
            case BottomFragment.SITE:
                fContent = new SiteFragment();
                break;
        }
        ft.replace(R.id.fragment_content, fContent);
        ft.commit();
    }

    private void setDefaultSite() {
        try {
            JSONObject sitesJson = new JSONObject(Stream.read(this, "sites.json"));
            Reptile.setSitesJson(sitesJson);
        } catch (JSONException e) {
            throw new RuntimeException("sites.json解析出错", e);
        }
    }

    private void setDefaultFragment() {
        Fragment fTitle = null;
        BottomFragment fBottom = null;
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        fTitle = new TitleFragment();
        fBottom = new BottomFragment();
        fBottom.setOnItemClickListener(this);

        ft.replace(R.id.fragment_title, fTitle);
        ft.replace(R.id.fragment_bottom, fBottom);
        ft.commit();
        changeTab(BottomFragment.SITE);
    }

    private void first() {
        SharedPreferences setting = getSharedPreferences("app", MODE_PRIVATE);
        Boolean user_first = setting.getBoolean("first", true);
        if (user_first) {
            setting.edit().putBoolean("first", false).commit();
        } else {
            return;
        }
        //把assets中的配置文件写到files，
        String sitesjson = "sites.json";
        Stream.write(this, sitesjson, Stream.read(this.getAssets(), sitesjson));
        SimpleDialog.show(this, "说明", Stream.read(this.getAssets(), "info.txt"), null);
    }
}
