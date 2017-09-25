package cc.aoeiuv020.comic.api

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import org.jsoup.select.Elements
import org.jsoup.select.NodeTraversor
import org.jsoup.select.NodeVisitor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.URL

/**
 * 漫画网站上下文，
 * 一个Context对象贯穿始终，
 * Created by AoEiuV020 on 2017.09.09-20:50:30.
 */
@Suppress("unused")
abstract class ComicContext {
    companion object {
        @Suppress("RemoveExplicitTypeArguments")
        private val contexts = listOf<ComicContext>(ManhuataiContext(), Dm5Context(), PopomhContext())
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
     * 搜索漫画得到漫画列表，
     */
    abstract fun search(name: String): ComicGenre

    abstract fun isSearchResult(genre: ComicGenre): Boolean

    /**
     * 获取漫画详情页信息，
     */
    abstract fun getComicDetail(comicDetailUrl: ComicDetailUrl): ComicDetail

    /**
     * 获取章节漫画所有页面信息，
     */
    abstract fun getComicPages(comicIssue: ComicIssue): List<ComicPage>

    internal fun check(url: String): Boolean = URL(getComicSite().baseUrl).host == URL(url).host

    protected fun getHtml(url: String): Document {
        logger.trace {
            val stack = Thread.currentThread().stackTrace
            stack.drop(2).take(6).joinToString("\n", "stack trace\n") {
                "\tat ${it.className}.${it.methodName}(${it.fileName}:${it.lineNumber})"
            }
        }
        logger.debug { "get $url" }
        val conn = Jsoup.connect(url)
        // 网络连接失败直接抛出，
        val root = conn.get()
        logger.debug { "status code: ${conn.response().statusCode()}" }
        logger.debug { "response url: ${conn.response().url()}" }
        if (!check(conn.response().url().toString())) {
            throw IOException("网络被重定向，检查网络是否可用，")
        }
        return root
    }

    protected fun absUrl(url: String) = getComicSite().baseUrl + url
    protected fun text(e: Element): String = e.text()
    protected fun text(e: Elements): String = e.text()
    protected fun src(img: Element): String = img.attr("src")
    protected fun absHref(a: Element): String = a.absUrl("href")
    protected fun title(a: Element): String = a.attr("title")

    /**
     * br结点转成换行的方法，
     * https://stackoverflow.com/a/17989379/5615186
     * @param maxDepth 深度，为 1 则不处理子结点，
     */
    protected fun textWithNewLine(element: Element, maxDepth: Int): String {
        val buffer = StringBuilder()
        NodeTraversor(object : NodeVisitor {
            internal var isNewline = true
            override fun head(node: Node, depth: Int) {
                if (depth > maxDepth) {
                    return
                }
                if (node is TextNode) {
                    val text = node.text().replace('\u00A0', ' ').trim { it <= ' ' }
                    if (!text.isEmpty()) {
                        buffer.append(text)
                        isNewline = false
                    }
                } else if (node is Element) {
                    if (!isNewline) {
                        if (node.isBlock || node.tagName() == "br") {
                            buffer.append("\n")
                            isNewline = true
                        }
                    }
                }
            }

            override fun tail(node: Node, depth: Int) {}
        }).traverse(element)
        return buffer.toString()
    }
}
