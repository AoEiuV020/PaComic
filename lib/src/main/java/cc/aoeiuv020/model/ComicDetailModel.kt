package cc.aoeiuv020.model

import cc.aoeiuv020.comic.api.site.ComicDetailSpider

/**
 * Created by AoEiuV020 on 17-5-31.
 */
class ComicDetailModel(comicDetailSpider: ComicDetailSpider) {
    val name: String = comicDetailSpider.name
    val info: String = comicDetailSpider.info
    val imgUrl: String = comicDetailSpider.imgUrl
}