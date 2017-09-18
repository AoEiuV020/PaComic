package cc.aoeiuv020.comic.ui

import android.content.Context
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import cc.aoeiuv020.comic.R
import cc.aoeiuv020.comic.api.ComicGenre
import cc.aoeiuv020.comic.api.ComicListItem
import cc.aoeiuv020.comic.api.ComicSite
import cc.aoeiuv020.comic.presenter.AlertableView
import cc.aoeiuv020.comic.presenter.MainPresenter
import com.miguelcatalan.materialsearchview.MaterialSearchView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.comic_list_item.view.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import kotlinx.android.synthetic.main.site_list_item.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.browse as ankoBrowse


/**
 * 主页，展示网站，分类，漫画列表，
 * Created by AoEiuV020 on 2017.09.12-19:04:44.
 */

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, AlertableView {
    override val ctx: Context = this
    private lateinit var presenter: MainPresenter
    private lateinit var genres: List<ComicGenre>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        presenter = MainPresenter(this)

        searchView.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // 收起软键盘，
                searchView.hideKeyboard(searchView)
                presenter.search(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean = false

        })

        presenter.start()
    }

    private fun isDrawerOpen() = drawer_layout.isDrawerOpen(GravityCompat.START)

    private fun closeDrawer() {
        drawer_layout.closeDrawer(GravityCompat.START)
    }

    private fun openDrawer() {
        drawer_layout.openDrawer(GravityCompat.START)
    }

    override fun onBackPressed() {
        if (isDrawerOpen()) {
            closeDrawer()
        } else {
            if (searchView.isSearchOpen) {
                searchView.closeSearch()
            } else {
                super.onBackPressed()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val item = menu.findItem(R.id.action_search)
        searchView.setMenuItem(item)

        menu.findItem(R.id.browse).setOnMenuItemClickListener {
            presenter.browseCurrentUrl()
        }

        return true
    }

    private val GROUP_ID: Int = 1

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.groupId) {
            GROUP_ID -> {
                showGenre(genres[item.order])
            }
            else -> when (item.itemId) {
                R.id.select_sites -> presenter.requestSites()
                R.id.settings -> {
                    Snackbar.make(drawer_layout, "没实现", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
        closeDrawer()
        return true
    }

    fun showGenre(genre: ComicGenre) {
        title = genre.name
        closeDrawer()
        presenter.requestComicList(genre)
    }

    fun addComicList(comicList: List<ComicListItem>) {
        if (listView.adapter != null) {
            (listView.adapter as ComicListAdapter).addAll(comicList)
        } else {
            showComicList(comicList)
        }
    }

    fun showComicList(comicList: List<ComicListItem>) {
        listView.run {
            adapter = ComicListAdapter(this@MainActivity, comicList)
            setOnItemClickListener { _, _, position, _ ->
                startActivity<ComicDetailActivity>("item" to adapter.getItem(position))
            }
            setOnScrollListener(object : AbsListView.OnScrollListener {
                private var lastItem = 0

                override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                    // 求画面上最后一个的索引，并不准，可能是最后一个+1,
                    lastItem = firstVisibleItem + visibleItemCount
                }

                override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
                    // 差不多就好，反正没到底也快了，
                    if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                            && lastItem >= adapter.count - 2) {
                        presenter.loadNextPage()
                    }
                }
            })
        }
    }

    fun showSites(sites: List<ComicSite>) {
        AlertDialog.Builder(this@MainActivity).setAdapter(SiteListAdapter(this@MainActivity, sites)) { _, index ->
            presenter.setSite(sites[index])
        }.show()
    }

    fun showSite(site: ComicSite) {
        openDrawer()
        nav_view.getHeaderView(0).apply {
            selectedSiteName.text = site.name
            glide()?.also {
                it.load(site.logo).holdInto(selectedSiteLogo)
            }
        }
        presenter.requestGenres(site)
    }

    fun showGenres(genres: List<ComicGenre>) {
        this.genres = genres
        nav_view.menu.run {
            removeGroup(GROUP_ID)
            genres.forEachIndexed { index, (name) ->
                add(GROUP_ID, index, index, name)
            }
        }
    }
}

class SiteListAdapter(val ctx: Context, private val sites: List<ComicSite>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: View.inflate(ctx, R.layout.site_list_item, null)
        val site = getItem(position)
        view.apply {
            siteName.text = site.name
            ctx.glide()?.also {
                it.load(site.logo).into(siteLogo)
            }
        }
        return view
    }

    override fun getItem(position: Int) = sites[position]

    override fun getItemId(position: Int) = 0L

    override fun getCount() = sites.size
}

class ComicListAdapter(val ctx: Context, data: List<ComicListItem>) : BaseAdapter(), AnkoLogger {
    private val items = data.toMutableList()
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View
            = (convertView ?: View.inflate(ctx, R.layout.comic_list_item, null)).apply {
        val comic = getItem(position)
        comic_name.text = comic.name
        ctx.glide()?.also {
            it.load(comic.img).into(comic_icon)
        }
    }

    override fun getItem(position: Int) = items[position]

    override fun getItemId(position: Int) = 0L

    override fun getCount() = items.size
    fun addAll(comicList: List<ComicListItem>) {
        items.addAll(comicList)
        notifyDataSetChanged()
    }
}