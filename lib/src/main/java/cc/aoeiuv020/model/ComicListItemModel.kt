package cc.aoeiuv020.model

import cc.aoeiuv020.comic.api.site.ComicItemSpider

/**
 * Created by AoEiuV020 on 17-5-31.
 */
class ComicListItemModel(comicItemSpider: ComicItemSpider) {
    val name: String = comicItemSpider.name
    val imgUrl: String = comicItemSpider.imgUrl
}