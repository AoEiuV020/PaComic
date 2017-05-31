package cc.aoeiuv020.comic.api.popomh

import cc.aoeiuv020.comic.api.ComicContentsSpider

/**
 * Created by AoEiuV020 on 17-5-31.
 */
class PopomhComicContentsSpider(popomh: Popomh, element: org.jsoup.nodes.Element) : ComicContentsSpider() {
    override val name: String = element.text()
    val comicPageUrl = popomh.home + element.attr("href")
    val comicPage: PopomhComicPageSpider by lazy {
        PopomhComicPageSpider(popomh, comicPageUrl)
    }
}