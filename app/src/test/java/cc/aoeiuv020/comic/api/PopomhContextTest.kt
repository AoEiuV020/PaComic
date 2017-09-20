package cc.aoeiuv020.comic.api

import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * 泡泡漫画网的测试类，
 * Created by AoEiuV020 on 2017.09.09-22:17:08.
 */
class PopomhContextTest {
    init {
        System.setProperty("org.slf4j.simpleLogger.log.PopomhContext", "info")
    }

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
                "http://www.popomh.com/comic/class_4/29.html",
                "http://www.popomh.com/comic/?act=search&st=%E6%9F%AF%E5%8D%97")
                .map { ComicGenre("", it) }
        genreList.forEach {
            context.getNextPage(it).let {
                println(it?.url)
            }
        }
    }

    @Test
    fun getComicList() {
        val genreList = listOf("http://www.popomh.com/comic/class_8.html",
                "http://www.popomh.com/comic/?act=search&st=%E6%9F%AF%E5%8D%97")
                .map { ComicGenre("", it) }
        genreList.forEach {
            context.getComicList(it).forEach {
                println(it.name)
                println(it.url)
                println(it.img)
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
        val imgList = listOf("http://124.94201314.net/dm03//ok-comic03/A/32744/act_008/z_0001_20239.PNG",
                "http://124.94201314.net/dm03//ok-comic03/A/32744/act_008/z_0002_20503.JPG")
        context.getComicPages(ComicIssue("", "http://www.popomh.com/popo290338/1.html?s=3")).forEachIndexed { index, (url) ->
            url.subscribe { (img) ->
                assertEquals(imgList[index], img)
            }
        }
    }
}