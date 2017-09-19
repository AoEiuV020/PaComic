package cc.aoeiuv020.comic.api

import org.junit.Before
import org.junit.Test

/**
 * 漫画台测试类，
 * Created by AoEiuV020 on 2017.09.19-11:19:01.
 */
class ManhuataiContextTest {
    init {
        System.setProperty("org.slf4j.simpleLogger.log.ManhuataiContext", "trace")
    }

    private lateinit var context: ManhuataiContext
    @Before
    fun setUp() {
        context = ManhuataiContext()
    }

    @Test
    fun getGenres() {
        context.getGenres().forEach {
            println("[${it.name}](${it.url})")
        }
    }

    @Test
    fun getNextPage() {
        val genreList = listOf("http://www.manhuatai.com/zhiyinmanke.html",
                "http://www.manhuatai.com/getjson.shtml?d=1505801840099&q=%E6%9F%AF%E5%8D%97",
                "http://www.manhuatai.com/all.html",
                "http://www.manhuatai.com/all_p813.html")
                .map { ComicGenre("", it) }
        genreList.forEach {
            context.getNextPage(it).let {
                println(it?.url)
            }
        }
    }

    @Test
    fun getComicList() {
        val genreList = listOf("http://www.manhuatai.com/zhiyinmanke.html",
                "http://www.manhuatai.com/getjson.shtml?d=1505801840099&q=%E6%9F%AF%E5%8D%97",
                "http://www.manhuatai.com/all.html")
                .map { ComicGenre("", it) }
        genreList.forEach {
            context.getComicList(it).forEach {
                println(it.name)
                println(it.url)
                println(it.img)
                println(it.info)
            }
        }
    }

    @Test
    fun search() {
        context.search("柯南").let {
            println(it.name)
            println(it.url)
        }
    }

    @Test
    fun getComicDetail() {
        context.getComicDetail(ComicListItem("斗破苍穹", "", "http://www.manhuatai.com/doupocangqiong/")).let {
            println(it.name)
            println(it.bigImg)
            println(it.info)
            it.issuesAsc.forEach {
                println("[${it.name}](${it.url})")
            }
        }
    }

    @Test
    fun getComicPages() {
        context.getComicPages(ComicIssue("", "http://www.manhuatai.com/doupocangqiong/dpcq_615h.html")).forEach {
            println(it.url)
            context.getComicImage(it).let {
                println(it.img)
            }
        }
    }
}