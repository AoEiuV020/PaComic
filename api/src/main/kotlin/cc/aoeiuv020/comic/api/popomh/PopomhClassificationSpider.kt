package cc.aoeiuv020.comic.api.popomh

import cc.aoeiuv020.comic.api.ClassificationSpider
import cc.aoeiuv020.comic.api.ComicItemSpider

class PopomhClassificationSpider(private val popomh: Popomh, element: org.jsoup.nodes.Element) : ClassificationSpider() {
    override val name: String = element.text()
    override val classificationUrl: String = popomh.home + element.attr("href")
    val urlTemplate: String = classificationUrl.removeSuffix(".html").removeSuffix("/")
    override val pagesCount: Int by lazy {
        logger.debug("get classification page count $name")
        val elements = firstPage.select("#iComicPC1 > b:nth-child(3)")
        elements.first().text().toInt()
    }
    val firstPage: org.jsoup.nodes.Document by lazy {
        logger.debug("get class page $name")
        val conn = org.jsoup.Jsoup.connect(classificationUrl)
        logger.debug("connect $classificationUrl")
        val root = conn.get()
        logger.debug("title: ${root.title()}")
        root
    }

    fun page(i: Int): org.jsoup.nodes.Document {
        logger.debug("get page $i")
        if (i == 1) return firstPage
        val pageUrl = "$urlTemplate/$i.html"
        val conn = org.jsoup.Jsoup.connect(pageUrl)
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