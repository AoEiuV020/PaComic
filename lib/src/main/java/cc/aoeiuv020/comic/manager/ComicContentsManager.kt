package cc.aoeiuv020.comic.manager

import cc.aoeiuv020.comic.api.ComicContentsSpider
import cc.aoeiuv020.comic.model.ComicContentsModel

/**
 * Created by AoEiuV020 on 17-5-31.
 */
class ComicContentsManager {
    internal val comicContentsSpiders: List<ComicContentsSpider>?
        get() = ComicManager.comicDetailManager.comicDetailSpider?.contents
    internal val comicContentsSpider: ComicContentsSpider?
        get() = comicContentsIndex?.let { i -> comicContentsSpiders?.let { s -> s[i] } }
    val comicContentsModel: ComicContentsModel?
        get() = comicContentsSpider?.let { ComicContentsModel(it) }
    var comicContentsIndex: Int? = null
        set(value) {
            field = value
            ComicManager.comicPageManager.reset()
        }
    val comicContentsModels: List<ComicContentsModel>?
        get() = comicContentsSpiders?.map { ComicContentsModel(it) }

    fun reset() {
        comicContentsIndex = null
    }
}