package cc.aoeiuv020.model

import cc.aoeiuv020.comic.api.site.ComicContentsSpider

/**
 * Created by AoEiuV020 on 17-5-31.
 */
class ComicContentsModel(comicContentsSpider: ComicContentsSpider) {
    val name = comicContentsSpider.name
}