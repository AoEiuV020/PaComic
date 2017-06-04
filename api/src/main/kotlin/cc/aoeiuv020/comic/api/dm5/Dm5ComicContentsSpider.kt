package cc.aoeiuv020.comic.api.dm5

import cc.aoeiuv020.comic.api.ComicContentsSpider
import cc.aoeiuv020.comic.api.ComicPageSpider
import org.jsoup.nodes.Element

/**
 * Created by AoEiuV020 on 17-6-3.
 */
class Dm5ComicContentsSpider(dm5: Dm5, element: Element) : ComicContentsSpider() {
    override val name: String = element.attr("title")
    val comicPageUrl = dm5.home + element.attr("href")
    override val comicPage: ComicPageSpider
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
}