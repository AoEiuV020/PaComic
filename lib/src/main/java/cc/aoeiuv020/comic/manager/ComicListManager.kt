package cc.aoeiuv020.comic.manager

import cc.aoeiuv020.comic.api.ComicItemSpider
import cc.aoeiuv020.comic.model.ComicListItemModel

/**
 * Created by AoEiuV020 on 17-5-31.
 */
class ComicListManager {
    internal val comicItemSpiders: List<ComicItemSpider>?
        get() = ComicManager.classificationManager.classificationSpider?.comicList(comicPageIndex)
    internal val comicItemSpider: ComicItemSpider?
        get() = comicIndex?.let { i -> comicItemSpiders?.let { s -> s[i] } }
    val comicListItemModel: ComicListItemModel?
        get() = comicItemSpider?.let { ComicListItemModel(it) }
    val comicListItemModels: List<ComicListItemModel>?
        get() = comicItemSpiders?.map { ComicListItemModel(it) }
    var comicPageIndex: Int = 0
    var comicIndex: Int? = null
        set(value) {
            field = value
            ComicManager.comicDetailManager.reset()
        }

    fun reset() {
        comicIndex = null;
    }
}