package cc.aoeiuv020.comic.api

import org.junit.Before
import org.junit.Test

/**
 * 泡泡漫画网的测试类，
 * Created by AoEiuV020 on 2017.09.09-22:17:08.
 */
class PopomhTest {
    private lateinit var context: PopomhContext
    @Before
    fun setUp() {
        context = PopomhContext()
    }

    @Test
    fun getGenres() {
        context.getGenres().forEach {
            println("[${it.name}](${it.url})")
        }
    }

    @Test
    fun getNextPage() {
        val genreList = listOf("http://www.popomh.com/comic/",
                "http://www.popomh.com/comic/10.html",
                "http://www.popomh.com/comic/1091.html",
                "http://www.popomh.com/comic/class_4.html",
                "http://www.popomh.com/comic/class_4/22.html",
                "http://www.popomh.com/comic/class_4/29.html")
                .map { ComicGenre("", it) }
        genreList.forEach {
            context.getNextPage(it).let {
                println(it?.url)
            }
        }
    }

    @Test
    fun getComicList() {
        context.getComicList(ComicGenre("", "http://www.popomh.com/comic/class_8.html")).forEach {
            println(it.name)
            println(it.url)
            println(it.img)
        }
    }

    @Test
    fun getComicDetail() {
        context.getComicDetail(ComicListItem("狩猎史莱姆300年", "", "http://www.popomh.com/manhua/32551.html")).let {
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
        context.getComicPages(ComicIssue("", "http://www.popomh.com/popo290025/1.html?s=3")).forEach {
            println(it.url)
        }
    }

    @Test
    fun getComicImage() {
        context.getComicImage(ComicPage("http://www.popomh.com/popo290025/24.html?s=3")).let {
            println(it.img)
        }
    }
}