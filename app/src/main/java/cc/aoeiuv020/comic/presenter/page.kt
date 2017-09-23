package cc.aoeiuv020.comic.presenter

import cc.aoeiuv020.comic.App
import cc.aoeiuv020.comic.api.ComicIssue
import cc.aoeiuv020.comic.api.ComicListItem
import cc.aoeiuv020.comic.di.DetailModule
import cc.aoeiuv020.comic.di.PageModule
import cc.aoeiuv020.comic.ui.ComicPageActivity
import cc.aoeiuv020.comic.ui.async
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.error

/**
 * Created by AoEiuV020 on 2017.09.18-18:24:20.
 */
class ComicPagePresenter(private val view: ComicPageActivity, val name: String, val url: String, private var index: Int) : AnkoLogger {
    private lateinit var issueAsc: List<ComicIssue>
    fun start() {
        App.component.plus(DetailModule(ComicListItem(name, "", url))).getComicDetail()
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
        --index
        requestComicPages(true)
    }

    fun requestNextIssue() {
        ++index
        requestComicPages(false)
    }
}