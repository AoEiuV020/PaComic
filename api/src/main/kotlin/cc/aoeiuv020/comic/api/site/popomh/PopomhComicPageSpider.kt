package cc.aoeiuv020.comic.api.site.popomh

import cc.aoeiuv020.comic.api.site.ComicPageSpider
import org.jsoup.Jsoup
import org.jsoup.nodes.Document


/**
 * Created by AoEiuV020 on 17-5-31.
 */
class PopomhComicPageSpider(popomh: Popomh, comicPageUrl: String) : ComicPageSpider() {
    override val pagesCount: Int by lazy {
        logger.debug("get comic page count")
        val elements = firstPage.select("body > div.cHeader > div.cH1 > b")
        elements.first().text().split('/')[1].trim().toInt()
    }
    val firstPageUrl = comicPageUrl
    internal fun pageUrl(i: Int): String {
        return firstPageUrl.replace(Regex("/\\d*.html"), "/$i.html")
    }

    fun page(i: Int): Document {
        logger.debug("get page $i")
        if (i == 1) return firstPage
        val pageUrl = pageUrl(i)
        val conn = Jsoup.connect(pageUrl)
        logger.debug("connect $pageUrl")
        val root = conn.get()
        logger.debug("title: ${root.title()}")
        return root
    }

    internal val firstPage: Document by lazy {
        logger.debug("get comic first page")
        val conn = Jsoup.connect(comicPageUrl)
        logger.debug("connect $comicPageUrl")
        val root = conn.get()
        logger.debug("title: ${root.title()}")
        root
    }
    val domains: List<String> by lazy {
        firstPage.select("#hdDomain").first().attr("value").split('|')
    }
    val domain: String by lazy {
        domains[popomh.domainIndex]
    }
    val cipher: String by lazy {
        val elements = firstPage.select("#img7652, #img1021, #img2391, #imgCurr")
        elements.first().attr("name")
    }
    val imgUrl: String by lazy {
        domain + unsuan(cipher)
    }

    /**
     * 从js方法翻译过来，
     * 方法名意义未知，
     */
    fun unsuan(cipher: String): String {
        var s = cipher
        val x = s.substring(s.length - 1)
        val w = "abcdefghijklmnopqrstuvwxyz"
        val xi = w.indexOf(x) + 1
        val sk = s.substring(s.length - xi - 12, s.length - xi - 1)
        s = s.substring(0, s.length - xi - 12)
        val k = sk.substring(0, sk.length - 1)
        val f = sk.substring(sk.length - 1)
        var i = 0
        while (i < k.length) {
            s = s.replace(k[i], i.toString()[0])
            i++
        }
        val ss = s.split(f)
        s = ""
        i = 0
        while (i < ss.size) {
            s += ss[i].toInt().toChar()
            i++
        }
        return s
    }
}