package cc.aoeiuv020.comic.api.popomh

import cc.aoeiuv020.comic.api.SiteSpider
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

/**
 * Created by AoEiuV020 on 17-5-28.
 */
class Popomh : SiteSpider() {
    override val name = "泡泡漫画"
    override val home = "http://www.popomh.com"
    override val logoUrl = "http://www.popomh.com/images/logo.png"
    override val classificationSpiders by lazy {
        logger.debug("get classificationSpiders")
        val elements = homePage.select("#iHBG > div.cHNav > div > span > a, #iHBG > div.cHNav > div a:has(span)")
        logger.debug("classifications count: ${elements.size}")
        elements.map { PopomhClassificationSpider(this, it) }
    }

    override fun search(name: String) = PopomhSearchResultSpider(this, name)
    val homePage: Document by lazy {
        logger.debug("get home page")
        val conn = Jsoup.connect(home)
        logger.debug("connect $home")
        val root = conn.get()
        logger.debug("title: ${root.title()}")
        logger.trace("charset: ${root.charset()}")
        root
    }
    var domainIndex: Int = 0
}