package cc.aoeiuv020.comic.presenter

import android.content.Context
import cc.aoeiuv020.comic.App
import cc.aoeiuv020.comic.api.ComicContext
import cc.aoeiuv020.comic.api.ComicGenre
import cc.aoeiuv020.comic.api.ComicSite
import cc.aoeiuv020.comic.di.*
import cc.aoeiuv020.comic.ui.MainActivity
import cc.aoeiuv020.comic.ui.async
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.error

/**
 * Created by AoEiuV020 on 2017.09.18-15:30:37.
 */
class MainPresenter(private val view: MainActivity) : AnkoLogger {
    private var listComponent: ListComponent? = null

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

    private fun saveGenre(genre: ComicGenre) {
        App.component.ctx.getSharedPreferences("genre", Context.MODE_PRIVATE)
                .edit()
                .putString("name", genre.name)
                .putString("url", genre.url)
                .apply()
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

    fun requestComicList(genre: ComicGenre) {
        saveGenre(genre)
        App.component.plus(ListModule(genre)).also { listComponent = it }
                .getComicList()
                .async()
                .subscribe({ comicList ->
                    view.showComicList(comicList)
                }, { e ->
                    val message = "加载漫画列表失败，"
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

    fun loadNextPage() {
        debug { "加载下一页，已经设置listComponent: ${listComponent != null}" }
        listComponent?.run {
            getNextPage().async().toList().subscribe({ genres ->
                if (genres.isEmpty()) {
                    debug { "没有下一页" }
                    view.showYetLastPage()
                    return@subscribe
                }
                val genre = genres.first()
                view.showUrl(genre.url)
                App.component.plus(ListModule(genre)).also { listComponent = it }
                        .getComicList()
                        .async()
                        .subscribe({ comicList ->
                            debug { "展示漫画列表，数量：${comicList.size}" }
                            view.addComicList(comicList)
                        }, { e ->
                            val message = "加载下一页漫画列表失败，"
                            error(message, e)
                            view.showError(message, e)
                        })
            }, { e ->
                val message = "加载漫画列表一下页地址失败，"
                error(message, e)
                view.showError(message, e)
            })
        }
    }
}