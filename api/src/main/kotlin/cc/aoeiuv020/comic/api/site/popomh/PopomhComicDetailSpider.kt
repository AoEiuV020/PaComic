package cc.aoeiuv020.comic.api.site.popomh

import cc.aoeiuv020.comic.api.site.ComicDetailSpider
import org.jsoup.nodes.Document

class PopomhComicDetailSpider(private val popomh: Popomh, comicPage: Document,
                              override val name: String) : ComicDetailSpider() {
    override val info: String by lazy {
        logger.debug("get comic info $name")
        val elements = comicPage.select("#about_kit > ul > li")
        elements.map { it.text() }.joinToString("\n")
    }
    override val imgUrl: String by lazy {
        logger.debug("get comic image $name")
        val elements = comicPage.select("#about_style > img")
        elements.attr("src")
    }
}