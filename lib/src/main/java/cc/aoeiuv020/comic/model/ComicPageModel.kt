package cc.aoeiuv020.comic.model

import cc.aoeiuv020.comic.api.ComicPageSpider

/**
 * Created by AoEiuV020 on 17-5-31.
 */
class ComicPageModel(comicPageSpider: ComicPageSpider, val index: Int) {
    val imgUrl = comicPageSpider.getImgUrl(index)
    val pagesCount = comicPageSpider.pagesCount
}