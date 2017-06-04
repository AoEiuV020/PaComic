package cc.aoeiuv020.comic.api.dm5

import org.junit.Before
import org.junit.Test

/**
 * Created by AoEiuV020 on 17-6-3.
 */
class Dm5SearchResultSpiderTest {
    lateinit var sr: Dm5SearchResultSpider
    @Before
    fun setUp() {
        val site = Dm5()
        sr = site.search("风")
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