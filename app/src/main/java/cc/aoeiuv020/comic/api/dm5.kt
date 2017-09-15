package cc.aoeiuv020.comic.api

import org.jsoup.Jsoup

/**
 * 动漫屋，
 * Created by AoEiuV020 on 2017.09.13-15:15:49.
 */
class Dm5Context : ComicContext() {
    private val site = ComicSite(
            name = "动漫屋",
            baseUrl = "http://www.dm5.com",
            logo = "http://js16.tel.cdndm.com/v201704261735/default/images/newImages/index_main_logo.png"
    )

    override fun getComicSite(): ComicSite = site
    override fun getGenres(): List<ComicGenre> {
        val root = getHtml(site.baseUrl)
        val elements = root.select("body > div:nth-child(3) > div:nth-child(2) > ul > li > a")
        return elements.map { ComicGenre(it.text(), url(it.attr("href"))) }
    }

    override fun getNextPage(genre: ComicGenre): ComicGenre? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getComicList(genre: ComicGenre): List<ComicListItem> {
        val root = getHtml(genre.url)
        val elements = root.select("#index_left > div.inkk.mato20 > div.innr3 > li > a:nth-child(1)")
        return elements.map {
            ComicListItem(it.text(), it.select("img").attr("src"), url(it.attr("href")))
        }
    }

    override fun getComicDetail(comicListItem: ComicListItem): ComicDetail {
        val root = getHtml(comicListItem.url)
        // 这个name也可以改成从html解析，
        val name = comicListItem.name
        val bigImg = root.select("#mhinfo > div.innr9.innr9_min > div.innr90 > div.innr91 > img")
                .attr("src")
        val info = root.select("#mhinfo > div.innr9.innr9_min > div:nth-child(3) > p")
                .joinToString("\n") { it.text() }
        val issues = root.select("ul.nr6.lan2 > li:has(a.tg)")
                .map {
                    ComicIssue(it.select("a").attr("title"), url(it.select("a").attr("href")))
                }.asReversed()
        return ComicDetail(name, bigImg, info, issues)
    }

    override fun getComicPages(comicIssue: ComicIssue): List<ComicPage> {
        val cid = comicIssue.url.replace(Regex(".*/m(\\d*)/"), "$1")
        val first = getHtml(comicIssue.url)
        val chapter = first.select("body > div.viewBar > div:nth-child(9) > div#chapterpager").firstOrNull() ?: return emptyList()
        val pagesCount = chapter.select("a:nth-last-child(1)").firstOrNull()?.run { text().toInt() } ?: 1
        return List(pagesCount) {
            ComicPage(url("/m$cid-p${it + 1}/"))
        }
    }

    private val chapterfunUrl = url("/chapterfun.ashx")
    /**
     * http://css99tel.cdndm5.com/v201709121708/default/js/chapternew_v22.js
     */
    override fun getComicImage(comicPage: ComicPage): ComicImage {
        val cid = comicPage.url.replace(Regex(".*/m(\\d*)-p\\d*/"), "$1")
        val index = comicPage.url.replace(Regex(".*/m\\d*-p(\\d*)/"), "$1")
        val pageUrl = chapterfunUrl
        val conn = Jsoup.connect(pageUrl)
                .data("cid", cid)
                .data("page", index)
                .data("key", "")
                .data("language", "1")
                .data("gtk", "6")
                .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.86 Safari/537.36")
                .referrer(site.baseUrl)
        logger.debug("connect $pageUrl")
        conn.execute()
        val str = conn.response().body()
        logger.debug("chapterfun: $str")
        val chapter = str.removeSuffix("\n")
        return ComicImage(decode(chapter))
    }

    /**
     * 根据这网站反混淆这个js解读的，
     * http://jsbeautifier.org/
     */
    @SuppressWarnings("")
    private fun decode(chapter: String): String {
        val keys = chapter.replace(Regex(".*\\('.*',\\d*,\\d*,'(.*)'\\.split.*"), "$1").split('|')
        fun antialiasing(r: String, ch: Char): String = r + when (ch.toInt()) {
            in 'a'.toInt()..'z'.toInt() -> keys[ch.toInt() - 'a'.toInt() + 10]
            in '0'.toInt()..'9'.toInt() -> keys[ch.toInt() - '0'.toInt()]
            else -> ch
        }
        // g://f-k-j-a-b.e.c/l/q/3
        val pix = chapter.replace(Regex(".*=\"(\\w://[^\"]*)\";.*"), "$1")
                .fold("", ::antialiasing)
        logger.debug("pix = $pix")
        // /p.4
        val pvalue = chapter.replace(Regex(".*\\w=\\[\"([^\"]*)\"[,\\]].*"), "$1")
                .fold("", ::antialiasing)
        logger.debug("pvalue = $pvalue")
        /* 这里android和java不一样，
         * android上右花括号必须转义，java都可以，
         * 然而android studio 甚至警告不要转义，
         * android api 25,
         * java oracle 1.8.0_131
         * 反馈bug ?
        */
        val param = chapter.replace(Regex(".*\\+\\\\\'(\\?[^\\\\]*)\\\\\'\\}.*"), "$1")
                .fold("", ::antialiasing)
        logger.debug("param = $param")
        return "$pix$pvalue$param"
    }
}