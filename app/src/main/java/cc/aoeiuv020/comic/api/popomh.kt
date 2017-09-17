package cc.aoeiuv020.comic.api

import java.net.URLEncoder

/**
 * 泡泡漫画，
 * Created by AoEiuV020 on 2017.09.09-21:08:02.
 */
@Suppress("UnnecessaryVariable")
class PopomhContext : ComicContext() {
    private val site = ComicSite(
            name = "泡泡漫画",
            baseUrl = "http://www.popomh.com",
            logo = "http://www.popomh.com/images/logo.png"
    )

    override fun getComicSite(): ComicSite = site
    override fun getGenres(): List<ComicGenre> {
        val root = getHtml(site.baseUrl)
        val elements = root.select("#iHBG > div.cHNav > div > span > a, #iHBG > div.cHNav > div a:has(span)")
        return elements.map {
            val a = it
            ComicGenre(text(a), absHref(a))
        }
    }

    override fun getNextPage(genre: ComicGenre): ComicGenre? {
        if (isSearchResult(genre)) {
            // 这网站搜索结果只有一页，
            return null
        }
        val root = getHtml(genre.url)
        val a = root.select("#iComicPC1 > span > a:nth-child(3)").first()
        val url = absHref(a)
        return if (url.isEmpty()) {
            return null
        } else {
            ComicGenre(genre.name, url)
        }
    }

    override fun getComicList(genre: ComicGenre): List<ComicListItem> {
        val root = getHtml(genre.url)
        val elements = root.select("#list > div.cComicList > li > a")
        return elements.map {
            val a = it
            val img = it.select("img").first()
            ComicListItem(text(a), src(img), absHref(a))
        }
    }

    override fun search(name: String): ComicGenre {
        return ComicGenre(name, searchUrl(name))
/*
        val urlEncodedName = URLEncoder.encode(name, "UTF-8")
        val root = getHtml(absUrl("/comic/?act=search&st=$urlEncodedName"))
        val elements = root.select("#list > div.cComicList > li > a")
        return elements.map {
            val a = it
            val img = it.select("img").first()
            ComicListItem(text(a), src(img), absHref(a))
        }
*/
    }

    private fun searchUrl(name: String): String {
        val urlEncodedName = URLEncoder.encode(name, "UTF-8")
        return absUrl("/comic/?act=search&st=$urlEncodedName")
    }

    override fun isSearchResult(genre: ComicGenre): Boolean = genre.url.matches(Regex(".*act=search.*"))

    override fun getComicDetail(comicListItem: ComicListItem): ComicDetail {
        val root = getHtml(comicListItem.url)
        val name = comicListItem.name
        val img = root.select("#about_style > img").first()
        val bigImg = src(img)
        val info = root.select("#about_kit > ul > li")
                .joinToString("\n") { text(it) }
        val issues = root.select("#permalink > div.cVolList > ul > li > a").map {
            val a = it
            ComicIssue(text(a).removePrefix(name), absHref(a))
        }.asReversed()
        return ComicDetail(name, bigImg, info, issues)
    }

    override fun getComicPages(comicIssue: ComicIssue): List<ComicPage> {
        val first = getHtml(comicIssue.url)
        val pagesCount = first.select("body > div.cHeader > div.cH1 > b")
                .first().text().split('/')[1].trim().toInt()
        return List(pagesCount) {
            ComicPage(comicIssue.url.replace(Regex("/\\d*.html"), "/${it + 1}.html"))
        }
    }

    /**
     * 网站有两个图片服务器，默认第一个，
     */
    private var domainIndex: Int = 0

    override fun getComicImage(comicPage: ComicPage): ComicImage {
        val root = getHtml(comicPage.url)
        val domains = root.select("#hdDomain").first().attr("value").split('|')
        val domain = domains[if (domainIndex < 0) 0 else if (domainIndex > domains.size) domains.size else domainIndex]
        val cipher = root.select("#img7652, #img1021, #img2391, #imgCurr")
                .first().attr("name")
        val img = domain + unsuan(cipher)
        return ComicImage(img)
    }

    /**
     * 从js方法翻译过来，
     * 方法名意义未知，
     * http://www.popomh.com/script/view.js
     */
    private fun unsuan(cipher: String): String {
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