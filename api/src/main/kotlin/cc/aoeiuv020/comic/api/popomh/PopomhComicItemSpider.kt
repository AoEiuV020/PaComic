package cc.aoeiuv020.comic.api.popomh

import cc.aoeiuv020.comic.api.ComicDetailSpider
import cc.aoeiuv020.comic.api.ComicItemSpider

class PopomhComicItemSpider(popomh: Popomh, element: org.jsoup.nodes.Element) : ComicItemSpider() {
    override val name: String = element.text()
    override val imgUrl: String = element.select("img").attr("src")
    override val comicDetailUrl = popomh.home + element.attr("href")
    override val comicDetail: ComicDetailSpider by lazy {
        PopomhComicDetailSpider(popomh, comicDetailUrl, name)
    }
}