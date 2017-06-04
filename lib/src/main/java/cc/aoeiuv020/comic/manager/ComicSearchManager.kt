package cc.aoeiuv020.comic.manager

import cc.aoeiuv020.comic.api.ComicItemSpider
import cc.aoeiuv020.comic.model.ComicListItemModel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug

/**
 * 先调用search得到comicItemSpiders,
 * 再传入comicIndex，使comicItemSpider非空，
 * 同时将漫画详情页信息传给comicDetailManager，
 * Created by AoEiuV020 on 17-6-3.
 */
class ComicSearchManager : AnkoLogger {
    internal var comicItemSpiders: List<ComicItemSpider>? = null
    internal val comicItemSpider: ComicItemSpider?
        get() = comicIndex?.let { i -> comicItemSpiders?.let { s -> s[i] } }
    var comicIndex: Int? = null
        set(value) {
            debug { value }
            field = value
            ComicManager.comicDetailManager.reset(comicItemSpider?.comicDetail)
        }

    fun search(name: String): List<ComicListItemModel>? = ComicManager.siteManager.siteSpider
            ?.run {
                search(name).comicList(0).run {
                    comicItemSpiders = this
                    map { ComicListItemModel(it) }
                }
            }


    fun reset() {
        comicItemSpiders = null
    }
}