package cc.aoeiuv020.comic.api

import org.junit.Before
import org.junit.Test

/**
 * 动漫屋的测试类，
 * Created by AoEiuV020 on 2017.09.13-16:29:56.
 */
class Dm5Test {
    init {
        System.setProperty("org.slf4j.simpleLogger.log.Dm5Context", "trace")
    }

    private lateinit var context: Dm5Context
    @Before
    fun setUp() {
        context = Dm5Context()
    }

    @Test
    fun getGenres() {
        context.getGenres().forEach {
            println("[${it.name}](${it.url})")
        }
    }

    @Test
    fun getNextPage() {
        val genreList = listOf("http://www.dm5.com/manhua-shaonianrexue/",
                "http://www.dm5.com/manhua-shaonianrexue-p2/",
                "http://www.dm5.com/manhua-shaonianrexue-p149/")
                .map { ComicGenre("", it) }
        genreList.forEach {
            context.getNextPage(it).let {
                println(it?.url)
            }
        }
    }

    @Test
    fun getComicList() {
        context.getComicList(ComicGenre("", "http://www.dm5.com/manhua-shaonianrexue/")).forEach {
            println(it.name)
            println(it.url)
            println(it.img)
        }
    }

    @Test
    fun getComicDetail() {
        context.getComicDetail(ComicListItem("妖神记", "", "http://www.dm5.com/manhua-yaoshenji/")).let {
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
        context.getComicPages(ComicIssue("", "http://www.dm5.com/m523824/")).forEach {
            println(it.url)
        }
    }

    @Test
    fun getComicImage() {
        context.getComicImage(ComicPage("http://www.dm5.com/m529922-p1/")).let {
            println(it.img)
        }
    }
}