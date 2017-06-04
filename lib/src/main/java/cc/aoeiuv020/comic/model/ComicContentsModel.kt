package cc.aoeiuv020.comic.model

import cc.aoeiuv020.comic.api.ComicContentsSpider

/**
 * Created by AoEiuV020 on 17-5-31.
 */
class ComicContentsModel(comicContentsSpider: ComicContentsSpider) {
    val name = comicContentsSpider.name
}