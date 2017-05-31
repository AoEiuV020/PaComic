package cc.aoeiuv020.comic.api.site.popomh

import cc.aoeiuv020.comic.api.site.ComicDetailSpider
import cc.aoeiuv020.comic.api.site.ComicItemSpider
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class PopomhComicItemSpider(private val popomh: Popomh, element: Element) : ComicItemSpider() {
    override val name: String = element.text()
    override val imgUrl: String = element.select("img").attr("src")
    val comicDetailUrl = popomh.home + element.attr("href")
    override val comicDetail: ComicDetailSpider by lazy {
        logger.debug("get comic page $name")
        val conn = Jsoup.connect(comicDetailUrl)
        logger.debug("connect $comicDetailUrl")
        val root = conn.get()
        logger.debug("title: ${root.title()}")
        PopomhComicDetailSpider(popomh, root, name)
    }
}