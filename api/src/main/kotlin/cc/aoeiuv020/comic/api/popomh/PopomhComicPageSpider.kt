package cc.aoeiuv020.comic.api.popomh

import cc.aoeiuv020.comic.api.ComicPageSpider


/**
 * Created by AoEiuV020 on 17-5-31.
 */
class PopomhComicPageSpider(popomh: Popomh, val firstPageUrl: String) : ComicPageSpider() {
    override val pagesCount: Int by lazy {
        logger.debug("get comic page count")
        val elements = firstPage.select("body > div.cHeader > div.cH1 > b")
        elements.first().text().split('/')[1].trim().toInt()
    }

    internal fun pageUrl(i: Int): String {
        return firstPageUrl.replace(Regex("/\\d*.html"), "/$i.html")
    }

    internal fun page(i: Int): org.jsoup.nodes.Document {
        logger.debug("get page $i")
        if (i == 1) return firstPage
        val pageUrl = pageUrl(i)
        val conn = org.jsoup.Jsoup.connect(pageUrl)
        logger.debug("connect $pageUrl")
        val root = conn.get()
        logger.debug("title: ${root.title()}")
        return root
    }

    internal val firstPage: org.jsoup.nodes.Document by lazy {
        logger.debug("get comic first page")
        val conn = org.jsoup.Jsoup.connect(firstPageUrl)
        logger.debug("connect $firstPageUrl")
        val root = conn.get()
        logger.debug("title: ${root.title()}")
        root
    }
    internal val domains: List<String> by lazy {
        firstPage.select("#hdDomain").first().attr("value").split('|')
    }
    internal val domain: String by lazy {
        domains[popomh.domainIndex]
    }

    internal fun cipher(i: Int): String {
        val elements = page(i).select("#img7652, #img1021, #img2391, #imgCurr")
        return elements.first().attr("name")
    }

    override fun getImgUrl(i: Int): String {
        return domain + unsuan(cipher(i + 1))
    }

    /**
     * 从js方法翻译过来，
     * 方法名意义未知，
     * http://www.popomh.com/script/view.js
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