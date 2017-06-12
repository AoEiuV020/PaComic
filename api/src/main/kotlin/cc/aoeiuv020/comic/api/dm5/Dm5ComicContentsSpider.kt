package cc.aoeiuv020.comic.api.dm5

import cc.aoeiuv020.comic.api.ComicContentsSpider
import cc.aoeiuv020.comic.api.ComicPageSpider
import org.jsoup.nodes.Element

/**
 * Created by AoEiuV020 on 17-6-3.
 */
class Dm5ComicContentsSpider(dm5: Dm5, element: Element) : ComicContentsSpider() {
    override val name: String = element.select("a").attr("title")
    val comicPageUrl = dm5.home + element.select("a").attr("href")
    override val comicPage: ComicPageSpider by lazy {
        // dm5有的漫画不是本地阅读，而是跳到腾讯贴吧漫本等其他网站，
        // 这种情况这里没有页数，暂时不处理，直接不让看了，
        val reg = Regex(".*（(\\d+)页）.*")
        val pageCount = element.text().let { if (it.matches(reg)) it.replace(reg, "$1").toInt() else 0 }
        Dm5ComicPageSpider(dm5, comicPageUrl, pageCount)
    }
}