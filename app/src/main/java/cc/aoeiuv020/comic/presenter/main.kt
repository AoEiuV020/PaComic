package cc.aoeiuv020.comic.presenter

import android.content.Context
import cc.aoeiuv020.comic.App
import cc.aoeiuv020.comic.api.ComicContext
import cc.aoeiuv020.comic.api.ComicGenre
import cc.aoeiuv020.comic.api.ComicSite
import cc.aoeiuv020.comic.di.GenreModule
import cc.aoeiuv020.comic.di.SearchModule
import cc.aoeiuv020.comic.di.SiteModule
import cc.aoeiuv020.comic.ui.MainActivity
import cc.aoeiuv020.comic.ui.async
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.error

/**
 * 管理主页的界面和数据，
 * 启动时读取之前的选择，网站和分类，
 * Created by AoEiuV020 on 2017.09.18-15:30:37.
 */
class MainPresenter(private val view: MainActivity) : AnkoLogger {
    fun start() {
        debug { "读取记住的选择，" }
        loadSite()?.also { site ->
            debug { "已有记住网站：${site.name}" }
            view.showSite(site)
            loadGenre(site)?.let { genre ->
                debug { "已有记住分类：${genre.name}" }
                view.showGenre(genre)
            } ?: run {
                debug { "没有记住的分类，" }
            }
        } ?: run {
            debug { "没有记住的网站，弹出网站选择，" }
            requestSites()
        }
    }

    /**
     * 提供记住了的分类选择，
     */
    private fun loadGenre(site: ComicSite): ComicGenre? {
        return App.component.ctx.getSharedPreferences("genre", Context.MODE_PRIVATE).run {
            val url = getString("url", null) ?: return null
            val name = getString("name", "")
            // 仅当url属于这个site,
            ComicGenre(name, url).takeIf { ComicContext.getComicContext(site.baseUrl)!!.check(url) }
        }
    }

    /**
     * 保存记住了的网站选择，
     */
    private fun saveSite(site: ComicSite) {
        App.component.ctx.getSharedPreferences("site", Context.MODE_PRIVATE)
                .edit()
                .putString("baseUrl", site.baseUrl)
                .apply()
    }

    /**
     * 提供记住了的网站选择，
     */
    private fun loadSite(): ComicSite? {
        val baseUrl = App.component.ctx.getSharedPreferences("site", Context.MODE_PRIVATE)
                .getString("baseUrl", "")
        return ComicContext.getComicContext(baseUrl)?.getComicSite()
    }

    fun requestSites() {
        App.component.plus(SiteModule())
                .getSites()
                .async()
                .subscribe { sites ->
                    view.showSites(sites)
                }
    }

    fun search(site: ComicSite, query: String) {
        debug { "在网站(${site.name})搜索：$query, " }
        App.component.plus(SearchModule(site, query)).search().async().subscribe({ genre ->
            view.showGenre(genre)
        }, { e ->
            val message = "加载搜索结果失败，"
            error(message, e)
            view.showError(message, e)
        })
    }

    fun requestGenres(site: ComicSite) {
        saveSite(site)
        App.component.plus(GenreModule(site))
                .getGenres()
                .async()
                .toList()
                .subscribe({ genres ->
                    debug { "加载网站分类列表成功，数量：${genres.size}" }
                    view.showGenres(genres)
                }, { e ->
                    val message = "加载网站分类列表失败，"
                    error(message, e)
                    view.showError(message, e)
                })
    }
}