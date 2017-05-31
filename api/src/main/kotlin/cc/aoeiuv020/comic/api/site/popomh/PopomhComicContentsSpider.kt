package cc.aoeiuv020.comic.api.site.popomh

import cc.aoeiuv020.comic.api.site.ComicContentsSpider
import org.jsoup.nodes.Element

/**
 * Created by AoEiuV020 on 17-5-31.
 */
class PopomhComicContentsSpider(popomh: Popomh, element: Element) : ComicContentsSpider() {
    override val name: String = element.text()
    override val comicPageUrl = popomh.home + element.attr("href")
}