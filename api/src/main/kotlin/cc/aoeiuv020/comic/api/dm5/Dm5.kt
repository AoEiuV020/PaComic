package cc.aoeiuv020.comic.api.dm5

import cc.aoeiuv020.comic.api.SiteSpider
import org.jsoup.Jsoup.connect
import org.jsoup.nodes.Document

/**
 * Created by AoEiuV020 on 17-6-3.
 */

class Dm5 : SiteSpider() {
    override val name = "动漫屋"
    override val home = "http://www.dm5.com"
    override val logoUrl = "http://js16.tel.cdndm.com/v201704261735/default/images/newImages/index_main_logo.png"
    override val classificationSpiders: List<Dm5ClassificationSpider> by lazy {
        logger.debug("get classificationSpiders")
        val elements = homePage.select("body > div:nth-child(3) > div:nth-child(2) > ul > li > a")
        logger.debug("classifications count: ${elements.size}")
        elements.map { Dm5ClassificationSpider(this, it) }

    }
    val homePage: Document by lazy {
        logger.debug("get home page")
        val conn = connect(home)
        logger.debug("connect $home")
        val root = conn.get()
        logger.debug("title: ${root.title()}")
        logger.trace("charset: ${root.charset()}")
        root
    }

    override fun search(name: String): Dm5SearchResultSpider = Dm5SearchResultSpider(this, name)
}
