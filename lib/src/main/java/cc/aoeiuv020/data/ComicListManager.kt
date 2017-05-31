package cc.aoeiuv020.data

import cc.aoeiuv020.comic.api.site.ComicItemSpider
import cc.aoeiuv020.model.ComicListItemModel

/**
 * Created by AoEiuV020 on 17-5-31.
 */
class ComicListManager {
    internal val comicItemSpiders: List<ComicItemSpider>?
        get() = ApiManager.classificationManager.classificationSpider?.comicList(comicPageIndex)
    internal val comicItemSpider: ComicItemSpider?
        get() = comicIndex?.let { i -> comicItemSpiders?.let { s -> s[i] } }
    val comicListItemModel: ComicListItemModel?
        get() = comicItemSpider?.let { ComicListItemModel(it) }
    val comicListItemModels: List<ComicListItemModel>?
        get() = comicItemSpiders?.map { ComicListItemModel(it) }
    var comicPageIndex: Int = 1
    var comicIndex: Int? = null
}