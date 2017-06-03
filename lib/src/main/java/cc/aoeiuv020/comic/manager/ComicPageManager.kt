package cc.aoeiuv020.comic.manager

import cc.aoeiuv020.comic.api.ComicPageSpider
import cc.aoeiuv020.comic.model.ComicPageModel
import cc.aoeiuv020.comic.model.ComicPagesCountModel

/**
 * Created by AoEiuV020 on 17-5-31.
 */
class ComicPageManager {
    internal val comicPageSpider: ComicPageSpider?
        get() = ComicManager.comicContentsManager.comicContentsSpider?.comicPage
    val comicPagesCountModel: ComicPagesCountModel?
        get() = comicPageSpider?.let { ComicPagesCountModel(it.pagesCount) }

    fun comicPageModelAt(index: Int) = comicPageSpider?.let { ComicPageModel(it.imgUrl(index)) }
    fun reset() {
    }
}