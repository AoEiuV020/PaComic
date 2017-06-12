package cc.aoeiuv020.comic.api.dm5

import org.junit.Before
import org.junit.Test

/**
 * Created by AoEiuV020 on 17-6-11.
 */
class Dm5ComicPageSpiderTest {
    lateinit var page: Dm5ComicPageSpider
    @Before
    fun setUp() {
        val site = Dm5()
        page = Dm5ComicPageSpider(site, "http://www.dm5.com/m172972/", 6)
    }
    @Test
    fun getPageUrl() {
        println(page.pageUrl(3))
        println(page.pageUrl(5))
    }

    @Test
    fun getChapterfunUrl() {
        println(page.chapterfun(3))
        println(page.chapterfun(5))
    }

    @Test
    fun imgUrl() {
        println(page.imgUrl(3))
        println(page.imgUrl(5))
    }
}