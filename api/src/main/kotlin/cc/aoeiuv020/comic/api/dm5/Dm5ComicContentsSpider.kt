package cc.aoeiuv020.comic.api.dm5

import cc.aoeiuv020.comic.api.ComicContentsSpider
import cc.aoeiuv020.comic.api.ComicPageSpider
import org.jsoup.nodes.Element

/**
 * Created by AoEiuV020 on 17-6-3.
 */
class Dm5ComicContentsSpider(dm5: Dm5, element: Element) : ComicContentsSpider() {
    override val name: String = element.select("a").attr("title")
    val comicPageUrl = dm5.home + element.select("a").attr("href")
    override val comicPage: ComicPageSpider by lazy {
        val pageCount = element.text().replace(Regex(".*(\\d).*"), "$1").toInt()
        Dm5ComicPageSpider(dm5, comicPageUrl, pageCount)
    }
}