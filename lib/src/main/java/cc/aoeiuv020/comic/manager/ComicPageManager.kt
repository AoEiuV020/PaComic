package cc.aoeiuv020.comic.manager

import cc.aoeiuv020.comic.api.ComicPageSpider
import cc.aoeiuv020.comic.model.ComicPageModel

/**
 * Created by AoEiuV020 on 17-5-31.
 */
class ComicPageManager {
    internal val comicPageSpider: ComicPageSpider?
        get() = ApiManager.comicContentsManager.comicContentsSpider?.comicPage
    val comicPageModel: ComicPageModel?
        get() = comicPageSpider?.let { ComicPageModel(it, 1) }

    fun reset() {
    }
}