package cc.aoeiuv020.comic.presenter

import cc.aoeiuv020.comic.App
import cc.aoeiuv020.comic.R
import cc.aoeiuv020.comic.api.ComicIssue
import cc.aoeiuv020.comic.di.PageModule
import cc.aoeiuv020.comic.ui.ComicPageActivity
import cc.aoeiuv020.comic.ui.async
import cc.aoeiuv020.comic.ui.loading
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.error

/**
 * Created by AoEiuV020 on 2017.09.18-18:24:20.
 */
class ComicPagePresenter(private val view: ComicPageActivity, private val name: String, private val issue: ComicIssue) : AnkoLogger {
    fun start() {
        view.showName("$name - ${issue.name}")
        requestComicPages()
    }

    private fun requestComicPages() {
        debug { "请求(${issue.name})全图片地址" }
        val loadingDialog = view.loading(R.string.comic_page)
        App.component.plus(PageModule(issue))
                .getComicPages()
                .async()
                .toList()
                .subscribe({ pages ->
                    view.showComicPages(pages)
                    loadingDialog.dismiss()
                }, { e ->
                    val message = "加载漫画页面失败，"
                    error(message, e)
                    view.alertError(message, e)
                    loadingDialog.dismiss()
                })
    }
}