package cc.aoeiuv020.comic.presenter

import cc.aoeiuv020.comic.App
import cc.aoeiuv020.comic.api.ComicListItem
import cc.aoeiuv020.comic.di.DetailModule
import cc.aoeiuv020.comic.ui.ComicDetailActivity
import cc.aoeiuv020.comic.ui.async
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error

/**
 * 管理详情页的界面和数据，
 * 启动时请求漫画详情页的信息，
 * 然后就没了，
 * Created by AoEiuV020 on 2017.09.18-17:52:06.
 */
class ComicDetailPresenter(private val view: ComicDetailActivity, private val comicListItem: ComicListItem) : AnkoLogger {
    fun start() {
        requestComicDetail()
    }

    private fun requestComicDetail() {
        App.component.plus(DetailModule(comicListItem.detailUrl))
                .getComicDetail()
                .async()
                .subscribe({ comicDetail ->
                    view.showComicDetail(comicDetail)
                }, { e ->
                    val message = "加载漫画详情失败，"
                    error(message, e)
                    view.showError(message, e)
                })
    }
}