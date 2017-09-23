package cc.aoeiuv020.comic.presenter

import android.content.Context
import cc.aoeiuv020.comic.App
import cc.aoeiuv020.comic.api.ComicGenre
import cc.aoeiuv020.comic.di.ListComponent
import cc.aoeiuv020.comic.di.ListModule
import cc.aoeiuv020.comic.ui.ComicListFragment
import cc.aoeiuv020.comic.ui.async
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.error

/**
 * 管理漫画列表界面和数据，
 * Created by AoEiuV020 on 2017.09.23-22:47:15.
 */
class ComicListPresenter(private val view: ComicListFragment) : AnkoLogger {
    private var listComponent: ListComponent? = null

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

    private fun saveGenre(genre: ComicGenre) {
        App.component.ctx.getSharedPreferences("genre", Context.MODE_PRIVATE)
                .edit()
                .putString("name", genre.name)
                .putString("url", genre.url)
                .apply()
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