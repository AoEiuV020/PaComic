package cc.aoeiuv020.comic.api

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URL

/**
 * 漫画网站上下文，
 * 一个Context对象贯穿始终，
 * Created by AoEiuV020 on 2017.09.09-20:50:30.
 */
abstract class ComicContext {
    companion object {
        @Suppress("RemoveExplicitTypeArguments")
        private val contexts = listOf<ComicContext>(PopomhContext(), Dm5Context())
        private val contextsMap = contexts.associateBy { URL(it.getComicSite().baseUrl).host }
        fun getComicContexts(): List<ComicContext> = contexts
        fun getComicContext(url: String): ComicContext? {
            val host: String
            try {
                host = URL(url).host
            } catch (_: Exception) {
                return null
            }
            return contextsMap[host] ?: contexts.firstOrNull { it.check(url) }
        }
    }

    @Suppress("MemberVisibilityCanPrivate")
    protected val logger: Logger = LoggerFactory.getLogger(this.javaClass.simpleName)

    abstract fun getComicSite(): ComicSite
    /**
     * 获取网站分类信息，
     */
    abstract fun getGenres(): List<ComicGenre>

    /**
     * 获取分类页面的下一页，
     */
    abstract fun getNextPage(genre: ComicGenre): ComicGenre?

    /**
     * 获取分类页面里的漫画列表信息，
     */
    abstract fun getComicList(genre: ComicGenre): List<ComicListItem>

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

    internal fun check(url: String): Boolean = URL(getComicSite().baseUrl).host == URL(url).host

    protected fun getHtml(url: String): Document {
        if (logger.isTraceEnabled) {
            val stack = Thread.currentThread().stackTrace
            stack.drop(2).take(6).forEach {
                logger.trace("${it.className}.${it.methodName}(${it.fileName}:${it.lineNumber})")
            }
        }
        logger.debug("get $url")
        val conn = Jsoup.connect(url)
        val root = conn.get()
        logger.debug("title: ${root.title()}")
        return root
    }

    protected fun url(url: String) = getComicSite().baseUrl + url
}
