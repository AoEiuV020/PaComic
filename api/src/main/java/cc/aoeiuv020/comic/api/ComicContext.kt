package cc.aoeiuv020.comic.api

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.slf4j.LoggerFactory

/**
 * 漫画网站上下文，
 * 一个Context对象贯穿始终，
 * Created by AoEiuV020 on 2017.09.09-20:50:30.
 */
abstract class ComicContext {
    protected val logger = LoggerFactory.getLogger(this.javaClass.simpleName)

    abstract val name: String
    abstract val baseUrl: String
    abstract val logoUrl: String

    /**
     * 获取网站分类信息，
     */
    abstract fun getGenres(): List<ComicGenre>

    /**
     * 获取分类页面的下一页，
     */
    abstract fun getNextPage(listPage: ComicListPage): ComicListPage?

    /**
     * 获取分类页面里的漫画列表信息，
     */
    abstract fun getComicList(listPage: ComicListPage): List<ComicListItem>

    /**
     * 获取漫画详情页信息，
     */
    abstract fun getComicDetail(comicListItem: ComicListItem): ComicDetail

    /**
     * 获取章节漫画所有页面信息，
     */
    abstract fun getComicPages(comicIssue: ComicIssue): List<ComicPage>

    /**
     * 从漫画页面获取漫画图片，
     */
    abstract fun getComicImage(comicPage: ComicPage): ComicImage

    protected fun getHtml(url: String): Document {
        logger.debug("get $url")
        val conn = Jsoup.connect(url)
        logger.debug("connect $url")
        val root = conn.get()
        logger.debug("title: ${root.title()}")
        logger.trace("charset: ${root.charset()}")
        return root
    }
}

/**
 * 包含漫画列表的页面，
 * 比如，分类，搜索结果，
 */
open class ComicListPage(
        val url: String
)

/**
 * 漫画分类页面，
 * 该分类第一页地址，
 */
open class ComicGenre(
        val name: String,
        url: String
) : ComicListPage(url)

/**
 * 漫画列表中的一个漫画，
 * @param url 该漫画详情页地址，
 */
open class ComicListItem(
        val name: String,
        val img: String,
        val url: String
)

/**
 * 漫画详情页，
 */
open class ComicDetail(
        val name: String,
        val bigImg: String,
        val info: String,
        val issues: List<ComicIssue>
)

/**
 * 漫画目录，
 * @param url 本章节第一页地址，
 */
open class ComicIssue(
        val name: String,
        val url: String
)

/**
 * 漫画页面，
 */
open class ComicPage(
        val url: String
)

/**
 * 漫画图片，
 * @param img 漫画图片地址，
 */
open class ComicImage(
        val img: String
)