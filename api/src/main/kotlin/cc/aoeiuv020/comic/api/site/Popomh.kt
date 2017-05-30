package cc.aoeiuv020.comic.api.site

import org.jsoup.Jsoup

/**
 * Created by AoEiuV020 on 17-5-28.
 */
class Popomh : Site() {
    override val name = "泡泡漫画"
    override val baseUrl: String = "http://www.popomh.com"
    override val logoUrl = "http://www.popomh.com/images/logo.png"
    override val classifications by lazy {
        logger.debug("get classifications")
        val ret = homePage.select("#iHBG > div.cHNav > div > span > a")
        logger.debug("classifications count: ${ret.size}")
        ret.map { Classification(it.text(), it.attr("href")) }
    }
    val homePage by lazy {
        logger.debug("get home page")
        val conn = Jsoup.connect(baseUrl)
        logger.trace("connect $baseUrl")
        val root = conn.get()
        logger.debug("title: ${root.title()}")
        logger.debug("charset: ${root.charset()}")
        root
    }
}