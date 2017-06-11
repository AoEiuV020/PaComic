package cc.aoeiuv020.comic.api.dm5

import cc.aoeiuv020.comic.api.ComicDetailSpider
import org.jsoup.Jsoup.connect
import org.jsoup.nodes.Document

/**
 * Created by AoEiuV020 on 17-6-3.
 */
class Dm5ComicDetailSpider(dm5: Dm5, comicDetailUrl: String,
                           override val name: String) : ComicDetailSpider() {
    val comicDetail: Document by lazy {
        logger.debug("get comic page $name")
        val conn = connect(comicDetailUrl)
        logger.debug("connect $comicDetailUrl")
        val ret = conn.get()
        logger.debug("title: ${ret.title()}")
        ret
    }

    override val info: String by lazy {
        logger.debug("get comic info $name")
        val elements = comicDetail.select("#mhinfo > div.innr9.innr9_min > div:nth-child(3) > p")
        elements.map { it.text() }.joinToString("\n")
    }
    override val imgUrl: String by lazy {
        logger.debug("get comic image $name")
        val elements = comicDetail.select("#mhinfo > div.innr9.innr9_min > div.innr90 > div.innr91 > img")
        elements.attr("src")
    }
    override val contents: List<Dm5ComicContentsSpider> by lazy {
        logger.debug("get comic image $name")
        val elements = comicDetail.select("#cbc_1 > li > a")
        elements.map { Dm5ComicContentsSpider(dm5, it) }
    }
}