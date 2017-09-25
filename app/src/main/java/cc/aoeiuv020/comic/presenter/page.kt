package cc.aoeiuv020.comic.presenter

import cc.aoeiuv020.comic.App
import cc.aoeiuv020.comic.api.ComicDetailUrl
import cc.aoeiuv020.comic.api.ComicIssue
import cc.aoeiuv020.comic.di.DetailModule
import cc.aoeiuv020.comic.di.PageModule
import cc.aoeiuv020.comic.ui.ComicPageActivity
import cc.aoeiuv020.comic.ui.async
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.error

/**
 * 管理漫画图片页的界面和数据，
 * 初始化时传入漫画地址用来得到章节列表，
 * 启动时加载当前章节漫画并显示，
 * 另外还有加载上一话和下一话的处理，
 * Created by AoEiuV020 on 2017.09.18-18:24:20.
 */
class ComicPagePresenter(private val view: ComicPageActivity, val url: String, private var index: Int) : AnkoLogger {
    private lateinit var issueAsc: List<ComicIssue>
    fun start() {
        App.component.plus(DetailModule(ComicDetailUrl(url))).getComicDetail()
                .async().subscribe({ comicDetail ->
            issueAsc = comicDetail.issuesAsc
            requestComicPages(false)
        }, { e ->
            val message = "加载漫画章节列表失败，"
            error(message, e)
            view.showError(message, e)
        })
    }

    private fun requestComicPages(previous: Boolean) {
        val issue = issueAsc[index]
        debug { "请求${if (previous) "上" else "下"}一话($index.${issue.name})全图片地址" }
        App.component.plus(PageModule(issue))
                .getComicPages()
                .async()
                .subscribe({ pages ->
                    if (previous)
                        view.showPreviousIssue(issue, pages)
                    else
                        view.showNextIssue(issue, pages)
                }, { e ->
                    val message = "加载漫画页面失败，"
                    error(message, e)
                    view.showError(message, e)
                })
    }

    fun requestPreviousIssue() {
        if (index == 0) {
            view.showNoPreviousIssue()
            return
        }
        --index
        requestComicPages(true)
    }

    fun requestNextIssue() {
        if (index == issueAsc.size - 1) {
            view.showNoNextIssue()
            return
        }
        ++index
        requestComicPages(false)
    }
}