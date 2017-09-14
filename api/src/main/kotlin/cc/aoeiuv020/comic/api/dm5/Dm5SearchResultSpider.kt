package cc.aoeiuv020.comic.api.dm5

import cc.aoeiuv020.comic.api.SearchResultSpider
import org.jsoup.helper.HttpConnection
import org.jsoup.nodes.Document
import java.net.URL
import java.net.URLEncoder

/**
 * Created by AoEiuV020 on 17-6-3.
 */
class Dm5SearchResultSpider(val dm5: Dm5, override val name: String) : SearchResultSpider() {
    val encoding = "UTF-8"
    val rawName: String = URLEncoder.encode(name, encoding)
    fun url(page: Int) = dm5.home + "/search?page=$page&title=$rawName&language=1"
    //TODO: 不止一页，
    override val pageCount: Int = 1
    override val comicCount: Int = -1
    override val searchResultUrl: String = url(1)
    override fun comicList(i: Int): List<Dm5ComicItemSpider> {
        logger.debug("get comic list on page ${i + 1}")
        val root = page(i + 1)
        val elements = root.select("body > div.container > div.midBar > div:has(dl)")
        logger.debug("comic count ${elements.size}")
        return elements.map {
            Dm5ComicItemSpider(dm5,
                    it.select("dt p a").text(),
                    it.select("dl a img").attr("src"),
                    dm5.home + it.select("dl a").attr("href"))
        }
    }

    val firstPage: Document by lazy {
        logger.debug("get search result $name")
        val conn = HttpConnection.connect(URL(searchResultUrl))
        logger.debug("connect $searchResultUrl")
        val root = conn.get()
        logger.debug("title: ${root.title()}")
        root
    }

    fun page(i: Int): Document {
        logger.debug("get page $i")
        return firstPage
    }
}