package cc.aoeiuv020.comic.api

import org.jsoup.Jsoup
import java.net.URLEncoder

/**
 * 动漫屋，
 * Created by AoEiuV020 on 2017.09.13-15:15:49.
 */
@Suppress("UnnecessaryVariable")
class Dm5Context : ComicContext() {
    private val site = ComicSite(
            name = "动漫屋",
            baseUrl = "http://www.dm5.com",
            logo = "http://js16.tel.cdndm.com/v201704261735/default/images/newImages/index_main_logo.png"
    )

    override fun getComicSite(): ComicSite = site
    override fun getGenres(): List<ComicGenre> {
        val root = getHtml(site.baseUrl)
        val elements = root.select("body > div:nth-child(1) > div.navBar > ul > li > a[class='item ib'] , " +
                "body > div:nth-child(3) > div:nth-child(2) > ul > li > a")
        return elements.map {
            val a = it
            ComicGenre(text(a), absHref(a))
        }
    }

    override fun getNextPage(genre: ComicGenre): ComicGenre? {
        val root = getHtml(genre.url)
        val query = if (isSearchResult(genre)) {
            "body > div.container > div.midBar > div.pager > a:contains(下一页)"
        } else {
            "#search_fy > a:contains(下一页)"
        }
        val a = root.select(query).first()
        return if (a == null) {
            null
        } else {
            ComicGenre(genre.name, absHref(a))
        }
    }

    override fun getComicList(genre: ComicGenre): List<ComicListItem> {
        if (isSearchResult(genre)) {
            val root = getHtml(genre.url)
            val elements = root.select("body > div.container > div.midBar > div.item")
            return elements.map {
                val a = it.select("dt > p > a").first()
                val img = it.select("dl > a > img").first()
                ComicListItem(text(a), src(img), absHref(a), text(it))
            }
        }
        val root = getHtml(genre.url)
        val elements = root.select("#index_left > div.inkk.mato20 > div.innr3 > li")
        return elements.map {
            val a = it.select("a:has(strong)").first()
            val img = it.select("img").first()
            ComicListItem(text(a), src(img), absHref(a), text(it).removePrefix(text(a)))
        }
    }

    override fun search(name: String): ComicGenre {
        val index = 1
        return ComicGenre(name, searchUrl(name, index))
    }

    private fun searchUrl(name: String, index: Int): String {
        val urlEncodedName = URLEncoder.encode(name, "UTF-8")
        return absUrl("/search?page=$index&title=$urlEncodedName&language=1")
    }

    override fun isSearchResult(genre: ComicGenre): Boolean = genre.url.matches(Regex(".*/search.*"))

    override fun getComicDetail(comicListItem: ComicListItem): ComicDetail {
        val root = getHtml(comicListItem.url)
        // 这个name也可以改成从html解析，
        val name = comicListItem.name
        val bigImg = root.select("#mhinfo > div.innr9.innr9_min > div.innr90 > div.innr91 > img")
                .attr("src")
        val info = root.select("#mhinfo > div.innr9.innr9_min > div:nth-child(3) > p")
                .first().let { it.ownText() + (it.select("span").first()?.ownText() ?: "") }
        val issues = root.select("ul.nr6.lan2 > li:has(a.tg)").map {
            val a = it.select("a").first()
            ComicIssue(title(a).removePrefix(name), absHref(a))
        }.asReversed()
        return ComicDetail(name, bigImg, info, issues)
    }

    override fun getComicPages(comicIssue: ComicIssue): List<ComicPage> {
        val cid = comicIssue.url.replace(Regex(".*/m(\\d*)/"), "$1")
        val first = getHtml(comicIssue.url)
        val chapter = first.select("div#chapterpager").firstOrNull() ?: return emptyList()
        val pagesCount = chapter.select("a:nth-last-child(1)").firstOrNull()?.run { text().toInt() } ?: 1
        return List(pagesCount) {
            ComicPage(absUrl("/m$cid-p${it + 1}/"))
        }
    }

    /**
     * http://css99tel.cdndm5.com/v201709121708/default/js/chapternew_v22.js
     */
    override fun getComicImage(comicPage: ComicPage): ComicImage {
        val cid = comicPage.url.replace(Regex(".*/m(\\d*)-p\\d*/"), "$1")
        val index = comicPage.url.replace(Regex(".*/m\\d*-p(\\d*)/"), "$1")
        val pageUrl = absUrl("/chapterfun.ashx")
        val conn = Jsoup.connect(pageUrl)
                .data("cid", cid)
                .data("page", index)
                .data("key", "")
                .data("language", "1")
                .data("gtk", "6")
                .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.86 Safari/537.36")
                .referrer(site.baseUrl)
        logger.debug { "connect $pageUrl" }
        conn.execute()
        val str = conn.response().body()
        logger.debug { "chapterfun: $str" }
        val chapter = str.removeSuffix("\n")
        val img = decode(chapter)
        logger.debug { "img: $img" }
        val cacheableUrl = img.replace(Regex("&key=\\w*"), "")
        logger.debug { "cacheableUrl: $cacheableUrl" }
        return ComicImage(img)
    }

    /**
     * 根据这网站反混淆这个js解读的，
     * 真难伺候，只能发现点问题再改改，
     * http://jsbeautifier.org/
     */
    @SuppressWarnings("")
    private fun decode(chapter: String): String {
        val keys = chapter.replace(Regex(".*\\('.*',\\d*,\\d*,'(.*)'\\.split.*"), "$1").split('|')
        fun antialiasing(r: String, ch: Char): String = r + when (ch.toInt()) {
            in 'a'.toInt()..'z'.toInt() -> keys[ch.toInt() - 'a'.toInt() + 10].takeIf { it.isNotEmpty() } ?: ch
            in '0'.toInt()..'9'.toInt() -> keys[ch.toInt() - '0'.toInt()].takeIf { it.isNotEmpty() } ?: ch
            else -> ch
        }
        // g://f-k-j-a-b.e.c/l/q/3
        val pix = chapter.replace(Regex(".*=\"(\\w://[^\"]*)\";.*"), "$1")
                .fold("", ::antialiasing)
        logger.debug { "pix = $pix" }
        // /p.4
        val pvalue = chapter.replace(Regex(".*\\w=\\[\"([^\"]*)\"[,\\]].*"), "$1")
                .fold("", ::antialiasing)
        logger.debug { "pvalue = $pvalue" }
        /* 这里android和java不一样，
         * android上右花括号必须转义，java都可以，
         * 然而android studio 甚至警告不要转义，
         * android api 25,
         * java oracle 1.8.0_131
         * 反馈bug ?
        */
        val param = chapter.replace(Regex(".*\\+\\\\\'(\\?[^\\\\]*)\\\\\'\\}.*"), "$1")
                .fold("", ::antialiasing)
        logger.debug { "param = $param" }
        return "$pix$pvalue$param"
    }
}