package cc.aoeiuv020.comic.api.dm5

import cc.aoeiuv020.comic.api.ComicDetailSpider
import cc.aoeiuv020.comic.api.ComicItemSpider

/**
 * Created by AoEiuV020 on 17-6-3.
 */
class Dm5ComicItemSpider(dm5: Dm5,
                         override val name: String,
                         override val imgUrl: String,
                         override val comicDetailUrl: String) : ComicItemSpider() {
    override val comicDetail: ComicDetailSpider
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
}