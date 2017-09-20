package cc.aoeiuv020.comic.api

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * 动漫屋的测试类，
 * Created by AoEiuV020 on 2017.09.13-16:29:56.
 */
class Dm5ContextTest {
    init {
        System.setProperty("org.slf4j.simpleLogger.log.Dm5Context", "info")
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
                "http://www.dm5.com/manhua-shaonianrexue-p149/",
                "http://www.dm5.com/search?title=%E6%9F%AF%E5%8D%97&language=1")
                .map { ComicGenre("", it) }
        genreList.forEach {
            context.getNextPage(it).let {
                println(it?.url)
            }
        }
    }

    @Test
    fun getComicList() {
        val genreList = listOf("http://www.dm5.com/manhua-shaonianrexue/",
                "http://www.dm5.com/search?title=%E6%9F%AF%E5%8D%97&language=1",
                "http://www.dm5.com/manhua-latest/")
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
        context.getComicDetail(ComicListItem("妖精的尾巴", "", "http://www.dm5.com/manhua-yaojingdeweiba/")).let {
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
        val imgList = listOf("http://manhua1032-61-174-50-99.cdndm5.com/34/33771/523824/1_1219.jpg?cid=523824",
                "http://manhua1032-61-174-50-99.cdndm5.com/34/33771/523824/2_2667.jpg?cid=523824",
                "http://manhua1032-61-174-50-99.cdndm5.com/34/33771/523824/3_8727.jpg?cid=523824",
                "http://manhua1032-61-174-50-99.cdndm5.com/34/33771/523824/4_1681.jpg?cid=523824",
                "http://manhua1032-61-174-50-99.cdndm5.com/34/33771/523824/5_1055.jpg?cid=523824",
                "http://manhua1032-61-174-50-99.cdndm5.com/34/33771/523824/6_5063.jpg?cid=523824",
                "http://manhua1032-61-174-50-99.cdndm5.com/34/33771/523824/7_8270.jpg?cid=523824",
                "http://manhua1032-61-174-50-99.cdndm5.com/34/33771/523824/8_3589.jpg?cid=523824")
        context.getComicPages(ComicIssue("", "http://www.dm5.com/m523824/")).forEachIndexed { index, (url) ->
            url.subscribe { (img, cacheableUrl) ->
                assertEquals(imgList[index], cacheableUrl)
                assertTrue(img.startsWith(imgList[index]))
            }
        }
        /*
                      assertEquals("http://manhua1032-61-174-50-99.cdndm5.com/34/33771/523824/1_1219.jpg?cid=523824", it.cacheableUrl)
                      assertTrue(it.img.startsWith("http://manhua1032-61-174-50-99.cdndm5.com/34/33771/523824/1_1219.jpg?cid=523824"))
      */
    }
}