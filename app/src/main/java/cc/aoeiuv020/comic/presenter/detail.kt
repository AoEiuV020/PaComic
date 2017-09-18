package cc.aoeiuv020.comic.presenter

import cc.aoeiuv020.comic.App
import cc.aoeiuv020.comic.R
import cc.aoeiuv020.comic.api.ComicDetail
import cc.aoeiuv020.comic.api.ComicListItem
import cc.aoeiuv020.comic.di.DetailModule
import cc.aoeiuv020.comic.ui.ComicDetailActivity
import cc.aoeiuv020.comic.ui.alertError
import cc.aoeiuv020.comic.ui.async
import cc.aoeiuv020.comic.ui.loading
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error

/**
 * Created by AoEiuV020 on 2017.09.18-17:52:06.
 */
class ComicDetailPresenter(private val view: ComicDetailActivity, private val comicListItem: ComicListItem) : AnkoLogger {
    private var comicDetail: ComicDetail? = null
    fun start() {
        view.showGeneral(comicListItem)
        requestComicDetail()
    }

    private fun requestComicDetail() {
        val loadingDialog = view.loading(R.string.comic_detail)
        App.component.plus(DetailModule(comicListItem))
                .getComicDetail()
                .async()
                .subscribe({ comicDetail ->
                    this.comicDetail = comicDetail
                    view.showComicDetail(comicDetail)
                    loadingDialog.dismiss()
                }, { e ->
                    val message = "加载漫画详情失败，"
                    error(message, e)
                    view.alertError(message, e)
                    loadingDialog.dismiss()
                })
    }

    fun browseCurrentUrl() = view.browse(comicListItem.url)
    fun requestComicAbout() {
        // 这个comicDetail本质上是调用getter，不是直接传对象到内部类，
        comicDetail?.let {
            view.alert(it.info, it.name).show()
        }
    }
}