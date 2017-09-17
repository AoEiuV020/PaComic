package cc.aoeiuv020.comic.ui

import android.annotation.SuppressLint
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
import cc.aoeiuv020.comic.di.GenreModule
import cc.aoeiuv020.comic.di.ListComponent
import cc.aoeiuv020.comic.di.ListModule
import cc.aoeiuv020.comic.di.SiteModule
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.comic_list_item.view.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import kotlinx.android.synthetic.main.site_list_item.view.*
import org.jetbrains.anko.*


/**
 * 主页，展示网站，分类，漫画列表，
 * Created by AoEiuV020 on 2017.09.12-19:04:44.
 */

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, AnkoLogger {
    private var url: String? = null
    @SuppressLint("PrivateResource")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        searchView.setHintTextColor(getColor(R.color.abc_hint_foreground_material_light))

        debug { "读取记住的选择，" }
        App.component.plus(SiteModule()).site?.also { site ->
            debug { "已有记住网站：${site.name}" }
            selectSite(site)
            App.component.plus(GenreModule(site)).genre?.let { genre ->
                debug { "已有记住分类：${genre.name}" }
                selectComicGenre(genre)
            } ?: run {
                debug { "没有记住的分类，弹出侧栏，" }
                openDrawer()
            }
        } ?: run {
            debug { "没有记住的网站，弹出网站选择，" }
            showSites()
        }
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
            url?.let { browse(it) } ?: false
        }

        return true
    }

    private val GROUP_ID: Int = 1

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.groupId) {
            GROUP_ID -> {
                selectComicGenre(genres[item.order])
            }
            else -> when (item.itemId) {
                R.id.select_sites -> showSites()
                R.id.settings -> {
                    Snackbar.make(drawer_layout, "没实现", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
        closeDrawer()
        return true
    }

    private var listComponent: ListComponent? = null

    private fun selectComicGenre(genre: ComicGenre) {
        val loadingDialog = loading(R.string.comic_list)
        url = genre.url
        title = genre.name
        App.component.plus(ListModule(genre)).also { listComponent = it }
                .getComicList()
                .async()
                .toList()
                .subscribe({ comicList ->
                    setComicList(comicList)
                    loadingDialog.dismiss()
                }, { e ->
                    val message = "加载漫画列表失败，"
                    error(message, e)
                    alertError(message, e)
                    loadingDialog.dismiss()
                })
    }

    private fun addComicList(comicList: List<ComicListItem>) {
        (listView.adapter as ComicListAdapter).addAll(comicList)
    }

    private fun setComicList(comicList: List<ComicListItem>) {
        debug { "展示漫画列表，数量：${comicList.size}" }
        listView.run {
            adapter = ComicListAdapter(this@MainActivity, comicList)
            setOnItemClickListener { _, _, position, _ ->
                startActivity<ComicDetailActivity>("item" to adapter.getItem(position))
            }
            setOnScrollListener(object : AbsListView.OnScrollListener {
                private var lastItem = 0
                private var loadingNextPage = false
                override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                    // 求画面上最后一个的索引，并不准，可能是最后一个+1,
                    lastItem = firstVisibleItem + visibleItemCount
                }

                override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
                    // 差不多就好，反正没到底也快了，
                    if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                            && lastItem >= adapter.count - 2 && !loadingNextPage) {
                        loadingNextPage = true
                        val loadingDialog = loading(getString(R.string.next_page))
                        listComponent?.run {
                            getNextPage().async().subscribe { nullableGenre ->
                                nullableGenre?.also { genre ->
                                    App.component.plus(ListModule(genre)).also { listComponent = it }
                                            .getComicList()
                                            .async()
                                            .toList()
                                            .subscribe({ comicList ->
                                                addComicList(comicList)
                                                loadingDialog.dismiss()
                                                loadingNextPage = false
                                            }, { e ->
                                                val message = "加载漫画列表一下页失败，"
                                                error(message, e)
                                                alertError(message, e)
                                                loadingDialog.dismiss()
                                                loadingNextPage = false
                                            })
                                } ?: loading(R.string.yet_last_page)
                            }
                        }
                    }
                }
            })
        }
    }

    private fun showSites() {
        App.component.plus(SiteModule())
                .getSites()
                .async()
                .toList()
                .subscribe { sites ->
                    AlertDialog.Builder(this@MainActivity).setAdapter(SiteListAdapter(this@MainActivity, sites)) { _, index ->
                        val site = sites[index]
                        debug { "选中网站：${site.name}，弹出侧栏，" }
                        openDrawer()
                        selectSite(site)
                    }.show()
                }
    }

    private fun selectSite(site: ComicSite) {
        url = site.baseUrl
        nav_view.getHeaderView(0).apply {
            selectedSiteName.text = site.name
            glide()?.also {
                it.load(site.logo).holdInto(selectedSiteLogo)
            }
        }
        val loadingDialog = loading(R.string.genre_list)
        App.component.plus(GenreModule(site))
                .getGenres()
                .async()
                .toList()
                .subscribe({ genres ->
                    debug { "加载网站分类列表成功，数量：${genres.size}" }
                    setGenres(genres)
                    loadingDialog.dismiss()
                }, { e ->
                    val message = "加载网站分类列表失败，"
                    error(message, e)
                    alertError(message, e)
                    loadingDialog.dismiss()
                })
    }

    private lateinit var genres: List<ComicGenre>

    private fun setGenres(genres: List<ComicGenre>) {
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