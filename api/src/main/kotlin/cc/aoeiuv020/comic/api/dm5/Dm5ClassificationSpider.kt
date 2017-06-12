package cc.aoeiuv020.comic.api.dm5

import cc.aoeiuv020.comic.api.ClassificationSpider
import cc.aoeiuv020.comic.api.ComicItemSpider
import org.jsoup.Jsoup
import org.jsoup.Jsoup.connect
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

/**
 * Created by AoEiuV020 on 17-6-11.
 */
class Dm5ClassificationSpider(private val dm5: Dm5, element: Element)  : ClassificationSpider() {
    override val name: String = element.text()
    override val classificationUrl: String = dm5.home + element.attr("href")
    val urlTemplate: String = classificationUrl.removeSuffix("/")
    override val pageCount: Int by lazy {
        logger.debug("get classification page count $name")
        val elements = firstPage.select("#index_left > div.inkk.mato20 > div:nth-child(7) > span:nth-child(3)")
        elements.first().text().split('/')[1].toInt()
    }
    override val comicCount: Int by lazy {
        logger.debug("get comic count $name")
        val elements = firstPage.select("#index_left > div.inkk.mato20 > div:nth-child(7) > span:nth-child(2)")
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
        val pageUrl = "$urlTemplate-p$i"
        val conn = connect(pageUrl)
        logger.debug("connect $pageUrl")
        val root = conn.get()
        logger.debug("title: ${root.title()}")
        return root
    }

    override fun comicList(i: Int): List<Dm5ComicItemSpider> {
        logger.debug("get comic list on page ${i + 1}")
        val root = page(i + 1)
        val elements = root.select("#index_left > div.inkk.mato20 > div.innr3 > li > a:nth-child(1)")
        logger.debug("comic count ${elements.size}")
        return elements.map {
            Dm5ComicItemSpider(dm5, it.text(), it.select("img").attr("src"), dm5.home + it.attr("href"))
        }
    }
}