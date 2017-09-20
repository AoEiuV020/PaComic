package cc.aoeiuv020.comic.presenter

import cc.aoeiuv020.comic.App
import cc.aoeiuv020.comic.R
import cc.aoeiuv020.comic.api.ComicImage
import cc.aoeiuv020.comic.api.ComicIssue
import cc.aoeiuv020.comic.api.ComicPage
import cc.aoeiuv020.comic.di.PageModule
import cc.aoeiuv020.comic.ui.ComicPageActivity
import cc.aoeiuv020.comic.ui.async
import cc.aoeiuv020.comic.ui.loading
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.browse
import org.jetbrains.anko.debug
import org.jetbrains.anko.error

/**
 * Created by AoEiuV020 on 2017.09.18-18:24:20.
 */
class ComicPagePresenter(private val view: ComicPageActivity, private val name: String, private val issue: ComicIssue) : AnkoLogger {
    private var url = issue.url
    fun start() {
        view.showName("$name - ${issue.name}")
        view.showUrl(url)
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

    fun browseCurrentUrl() = view.browse(url)

    private val imgs = mutableMapOf<ComicPage, ComicImage>()
    /**
     * 解析漫画页面，得到该页图片地址，然后回调，
     */
    fun resolveComicPage(page: ComicPage, onComplete: (ComicImage) -> Unit, onError: (String, Throwable) -> Unit = { _, _ -> }) {
        debug { "解析漫画页面，${page.url}" }
        imgs[page]?.let { comicImage ->
            onComplete(comicImage)
        } ?: page.url.async().subscribe({ comicImage ->
            imgs.put(page, comicImage)
            onComplete(comicImage)
        }, { e ->
            val message = "加载漫画页面失败，"
            error(message, e)
            onError(message, e)
        })
    }
}