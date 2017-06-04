package cc.aoeiuv020.comic.api.popomh

import cc.aoeiuv020.comic.api.ClassificationSpider
import org.jsoup.Jsoup.connect
import org.jsoup.nodes.Document

class PopomhClassificationSpider(private val popomh: Popomh, element: org.jsoup.nodes.Element) : ClassificationSpider() {
    override val name: String = element.text()
    override val classificationUrl: String = popomh.home + element.attr("href")
    val urlTemplate: String = classificationUrl.removeSuffix(".html").removeSuffix("/")
    override val pageCount: Int by lazy {
        logger.debug("get classification page count $name")
        val elements = firstPage.select("#iComicPC1 > b:nth-child(3)")
        elements.first().text().toInt()
    }
    override val comicCount: Int by lazy {
        logger.debug("get comic count $name")
        val elements = firstPage.select("#iComicPC1 > b:nth-child(1)")
        elements.first().text().toInt()
    }
    val firstPage: Document by lazy {
        logger.debug("get class page $name")
        val conn = connect(classificationUrl)
        logger.debug("connect $classificationUrl")
        val root = conn.get()
        logger.debug("title: ${root.title()}")
        root
    }

    fun page(i: Int): Document {
        logger.debug("get page $i")
        if (i == 1) return firstPage
        val pageUrl = "$urlTemplate/$i.html"
        val conn = connect(pageUrl)
        logger.debug("connect $pageUrl")
        val root = conn.get()
        logger.debug("title: ${root.title()}")
        return root
    }

    override fun comicList(i: Int): List<PopomhComicItemSpider> {
        logger.debug("get comic list on page ${i + 1}")
        val root = page(i + 1)
        val elements = root.select("#list > div.cComicList > li > a")
        logger.debug("comic count ${elements.size}")
        return elements.map {
            PopomhComicItemSpider(popomh, it.text(), it.select("img").attr("src"), popomh.home + it.attr("href"))
        }
    }
}