package cc.aoeiuv020.comic.api.dm5

import org.junit.Before
import org.junit.Test

/**
 * Created by AoEiuV020 on 17-6-3.
 */
class Dm5ComicContentsSpiderTest {
    lateinit var cc: List<Dm5ComicContentsSpider>
    @Before
    fun setUp() {
        val site = Dm5()
        val comic = Dm5ComicDetailSpider(site, "http://www.dm5.com/manhua-shoubingweirenfengtie/", "兽兵卫忍风帖")
        cc = comic.contents
    }

    @Test
    fun getComicPageUrl() {
        cc.forEach {
            println("${it.name}, ${it.comicPageUrl}")
        }
    }

}