package cc.aoeiuv020.comic.api.popomh

import org.junit.Before
import org.junit.Test

/**
 * Created by AoEiuV020 on 17-6-3.
 */
class PopomhSearchResultSpiderTest {
    lateinit var sr: PopomhSearchResultSpider
    @Before
    fun setUp() {
        val site = Popomh()
        sr = site.search("风")
    }

    @Test
    fun getSearchResultUrl() {
        println(sr.searchResultUrl)
    }

    @Test
    fun getPageCount() {
        println(sr.pageCount)
    }

    @Test
    fun getComicCount() {
        println(sr.comicCount)
    }

    @Test
    fun comicList() {
        val cl = sr.comicList(0)
        cl.forEach {
            println("${it.name}, ${it.imgUrl}")
            println(it.comicDetailUrl)
        }
    }
}