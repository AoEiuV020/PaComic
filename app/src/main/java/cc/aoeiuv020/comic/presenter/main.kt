package cc.aoeiuv020.comic.presenter

import cc.aoeiuv020.comic.App
import cc.aoeiuv020.comic.R
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
    private var site: ComicSite? = null
    private var listComponent: ListComponent? = null
    private var isEnd = false
    private var isLoadingNextPage = false

    fun start() {
        debug { "读取记住的选择，" }
        App.component.plus(SiteModule()).site?.also { site ->
            debug { "已有记住网站：${site.name}" }
            setSite(site)
            App.component.plus(GenreModule(site)).genre?.let { genre ->
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

    fun requestSites() {
        App.component.plus(SiteModule())
                .getSites()
                .async()
                .toList()
                .subscribe { sites ->
                    view.showSites(sites)
                }
    }

    fun search(query: String) {
        debug { "搜索：$query" }
        site?.also {
            debug { "当前选择了的网站：${it.name}" }
            val loadingDialog = view.loading(R.string.search_result)
            App.component.plus(SearchModule(it, query)).search().async().subscribe({ genre ->
                view.showGenre(genre)
                loadingDialog.dismiss()
            }, { e ->
                val message = "加载搜索结果失败，"
                error(message, e)
                view.alertError(message, e)
                loadingDialog.dismiss()
            })
        } ?: run {
            debug { "没有选择网站，先弹出网站选择，" }
            requestSites()
        }
    }

    fun requestComicList(genre: ComicGenre) {
        isEnd = false
        isLoadingNextPage = false
        val loadingDialog = view.loading(R.string.comic_list)
        App.component.plus(ListModule(genre)).also { listComponent = it }
                .getComicList()
                .async()
                .toList()
                .subscribe({ comicList ->
                    view.showComicList(comicList)
                    loadingDialog.dismiss()
                }, { e ->
                    val message = "加载漫画列表失败，"
                    error(message, e)
                    view.alertError(message, e)
                    loadingDialog.dismiss()
                })
    }

    fun setSite(site: ComicSite) {
        debug { "选中网站：${site.name}，弹出侧栏，" }
        this.site = site
        view.showSite(site)
    }

    fun requestGenres(site: ComicSite) {
        val loadingDialog = view.loading(R.string.genre_list)
        App.component.plus(GenreModule(site))
                .getGenres()
                .async()
                .toList()
                .subscribe({ genres ->
                    debug { "加载网站分类列表成功，数量：${genres.size}" }
                    view.showGenres(genres)
                    loadingDialog.dismiss()
                }, { e ->
                    val message = "加载网站分类列表失败，"
                    error(message, e)
                    view.alertError(message, e)
                    loadingDialog.dismiss()
                })
    }

    fun loadNextPage() {
        if (isLoadingNextPage || isEnd) {
            return
        }
        isLoadingNextPage = true
        val loadingDialog = view.loading(R.string.next_page)
        debug { "加载下一页，已经设置listComponent: ${listComponent != null}" }
        listComponent?.run {
            getNextPage().async().toList().subscribe({ genres ->
                if (genres.isEmpty()) {
                    debug { "没有下一页" }
                    isEnd = true
                    isLoadingNextPage = false
                    loadingDialog.dismiss()
                    view.alert(R.string.yet_last_page).show()
                    return@subscribe
                }
                val genre = genres.first()
                view.showUrl(genre.url)
                App.component.plus(ListModule(genre)).also { listComponent = it }
                        .getComicList()
                        .async()
                        .toList()
                        .subscribe({ comicList ->
                            debug { "展示漫画列表，数量：${comicList.size}" }
                            view.addComicList(comicList)
                            // 重制这个标志，以便继续加载下一页，
                            isLoadingNextPage = false
                            loadingDialog.dismiss()
                        }, { e ->
                            val message = "加载下一页漫画列表失败，"
                            error(message, e)
                            view.alertError(message, e)
                            isLoadingNextPage = false
                            loadingDialog.dismiss()
                        })
            }, { e ->
                val message = "加载漫画列表一下页地址失败，"
                error(message, e)
                isEnd = true
                view.alertError(message, e)
                loadingDialog.dismiss()
            })
        }
    }
}