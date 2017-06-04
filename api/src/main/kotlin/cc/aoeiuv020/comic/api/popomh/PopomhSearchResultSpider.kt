package cc.aoeiuv020.comic.api.popomh

import cc.aoeiuv020.comic.api.ComicItemSpider
import cc.aoeiuv020.comic.api.SearchResultSpider
import org.jsoup.helper.HttpConnection.connect
import org.jsoup.nodes.Document
import java.net.URL
import java.net.URLEncoder

/**
 * Created by AoEiuV020 on 17-6-3.
 */
class PopomhSearchResultSpider(val popomh: Popomh, override val name: String) : SearchResultSpider() {
    val urlTemplate = popomh.home + "/comic/?act=search&st="
    val encoding = "UTF-8"
    val url by lazy { URL(urlTemplate + URLEncoder.encode(name, encoding)) }
    override val searchResultUrl: String = url.toString()
    //这个网站搜索结果始终只有一页，
    override val pageCount: Int = 1
    override val comicCount: Int by lazy {
        logger.debug("get comic count $name")
        val elements = firstPage.select("#iComicPC1 > b:nth-child(1)")
        elements.first().text().toInt()
    }

    val firstPage: Document by lazy {
        logger.debug("get search result $name")
        // UTF-8 是默认编码，所以其实不用URL也一样，
        val conn = connect(url)
        logger.debug("connect $searchResultUrl")
        val root = conn.get()
        logger.debug("title: ${root.title()}")
        root
    }

    fun page(i: Int): Document {
        logger.debug("get page $i")
        return firstPage
    }

    override fun comicList(i: Int): List<ComicItemSpider> {
        logger.debug("get comic list on page ${i + 1}")
        val root = page(i + 1)
        val elements = root.select("#list > div.cComicList > li > a")
        logger.debug("comic count ${elements.size}")
        return elements.map {
            PopomhComicItemSpider(popomh, it.text(), it.select("img").attr("src"), popomh.home + it.attr("href"))
        }
    }
}