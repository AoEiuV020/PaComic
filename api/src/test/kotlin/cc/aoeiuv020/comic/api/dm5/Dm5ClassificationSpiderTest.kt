package cc.aoeiuv020.comic.api.dm5

import org.junit.Before
import org.junit.Test

/**
 * Created by AoEiuV020 on 17-6-11.
 */
class Dm5ClassificationSpiderTest {
    lateinit var c: Dm5ClassificationSpider
    @Before
    fun setUp() {
        val site = Dm5()
        val cs = site.classificationSpiders
        c = cs[2]
    }

    @Test
    fun getPageCount() {
        println(c.pageCount)
    }

    @Test
    fun getComicCount() {
        println(c.comicCount)
    }

    @Test
    fun comicList() {
        val comicList = c.comicList(3)
        comicList.forEach {
            println(it.name)
        }
    }
}