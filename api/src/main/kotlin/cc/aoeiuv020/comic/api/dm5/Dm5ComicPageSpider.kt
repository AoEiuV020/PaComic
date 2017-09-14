package cc.aoeiuv020.comic.api.dm5

import cc.aoeiuv020.comic.api.ComicPageSpider
import org.jsoup.Jsoup.connect

/**
 * Created by AoEiuV020 on 17-6-11.
 */
class Dm5ComicPageSpider(val dm5: Dm5, firstPageUrl: String, override val pageCount: Int) : ComicPageSpider() {
    val cid = firstPageUrl.replace(Regex(".*/m(\\d*)/"), "$1")
    val chapterfunUrl = dm5.home + "/chapterfun.ashx"
    internal fun pageUrl(i: Int): String {
        return "${dm5.home}/m$cid-p${i + 1}/"
    }

    internal fun chapterfun(i: Int): String {
        logger.debug("get chapterfun $i")
        val pageUrl = chapterfunUrl
        val conn = connect(pageUrl)
                .data("cid", cid)
                .data("page", "$i")
                .data("key", "")
                .data("language", "1")
                .data("gtk", "6")
                .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.86 Safari/537.36")
                .referrer(dm5.home)
        logger.debug("connect $pageUrl")
        conn.execute()
        val str = conn.response().body()
        logger.debug("chapterfun length ${str.length}")
        return str.removeSuffix("\n")
    }

    override fun imgUrl(i: Int): String {
        return decode(chapterfun(i + 1))
    }

    /**
     * 根据这网站反混淆这个js解读的，
     * http://jsbeautifier.org/
     */
    fun decode(chapter: String): String {
        val keys = chapter.replace(Regex(".*\\('.*',\\d*,\\d*,'(.*)'\\.split.*"), "$1").split('|')
        fun antialiasing(r: String, ch: Char): String = r + when (ch.toInt()) {
            in 'a'.toInt()..'z'.toInt() -> keys[ch.toInt() - 'a'.toInt() + 10]
            in '0'.toInt()..'9'.toInt() -> keys[ch.toInt() - '0'.toInt()]
            else -> ch
        }
        // g://f.k-j-a-b.e.c/l/q/3
        val pix = chapter.replace(Regex(".*=\"(\\w://\\w\\.\\w-\\w-\\w-\\w\\.\\w\\.\\w/\\w/\\w/\\w)\";.*"), "$1")
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
        */
        val param = chapter.replace(Regex(".*\\+\\\\\'(\\?[^\\\\]*)\\\\\'\\}.*"), "$1")
                .fold("", ::antialiasing)
        logger.debug("param = $param")
        val url = "$pix$pvalue$param"
        return url
    }
}