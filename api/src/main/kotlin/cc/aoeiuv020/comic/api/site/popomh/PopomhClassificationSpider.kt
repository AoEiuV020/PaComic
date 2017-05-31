package cc.aoeiuv020.comic.api.site.popomh

import cc.aoeiuv020.comic.api.site.ClassificationSpider
import cc.aoeiuv020.comic.api.site.ComicItemSpider
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class PopomhClassificationSpider(private val popomh: Popomh, element: Element) : ClassificationSpider() {
    override val name: String = element.text()
    val url: String = popomh.home + element.attr("href")
    val urlTemplate: String = url.removeSuffix(".html").removeSuffix("/")
    override val pagesCount: Int by lazy {
        logger.debug("get classification page count $name")
        val elements = firstPage.select("#iComicPC1 > b:nth-child(3)")
        elements.first().text().toInt()
    }
    val firstPage: Document by lazy {
        logger.debug("get class page $name")
        val conn = Jsoup.connect(url)
        logger.debug("connect $url")
        val root = conn.get()
        logger.debug("title: ${root.title()}")
        root
    }

    fun page(i: Int): Document {
        logger.debug("get page $i")
        if (i == 1) return firstPage
        val pageUrl = "$urlTemplate/$i.html"
        val conn = Jsoup.connect(pageUrl)
        logger.debug("connect $pageUrl")
        val root = conn.get()
        logger.debug("title: ${root.title()}")
        return root
    }

    override fun comicList(i: Int): List<ComicItemSpider> {
        logger.debug("get comic list on page $i")
        val root = page(i)
        val elements = root.select("#list > div.cComicList > li > a")
        logger.debug("comic count ${elements.size}")
        return elements.map { PopomhComicItemSpider(popomh, it) }
    }
}