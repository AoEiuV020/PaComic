package cc.aoeiuv020.comic.api.dm5

import org.junit.Before
import org.junit.Test

/**
 * Created by AoEiuV020 on 17-6-3.
 */
class Dm5ComicDetailSpiderTest {
    lateinit var comic: Dm5ComicDetailSpider
    @Before
    fun setUp() {
        val site = Dm5()
        comic = Dm5ComicDetailSpider(site, "http://www.dm5.com/manhua-shoubingweirenfengtie/", "兽兵卫忍风帖")
    }

    @Test
    fun getInfo() {
        println(comic.info)
    }
}