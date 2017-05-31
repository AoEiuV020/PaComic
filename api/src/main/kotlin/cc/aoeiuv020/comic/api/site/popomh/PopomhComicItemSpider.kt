package cc.aoeiuv020.comic.api.site.popomh

import cc.aoeiuv020.comic.api.site.ComicDetailSpider
import cc.aoeiuv020.comic.api.site.ComicItemSpider
import org.jsoup.nodes.Element

class PopomhComicItemSpider(popomh: Popomh, element: Element) : ComicItemSpider() {
    override val name: String = element.text()
    override val imgUrl: String = element.select("img").attr("src")
    override val comicDetailUrl = popomh.home + element.attr("href")
    override val comicDetail: ComicDetailSpider by lazy {
        PopomhComicDetailSpider(popomh, comicDetailUrl, name)
    }
}