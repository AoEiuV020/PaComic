package cc.aoeiuv020.comic.api.site.popomh

import cc.aoeiuv020.comic.api.site.ComicDetailSpider
import org.junit.Before
import org.junit.Test

/**
 * Created by AoEiuV020 on 17-5-31.
 */
class PopomhComicDetailSpiderTest {
    lateinit var comic: ComicDetailSpider
    @Before
    fun setUp() {
        val site = Popomh()
        comic = PopomhComicDetailSpider(site, "http://www.popomh.com/manhua/28851.html", "妄想高校教员森下")
    }

    @Test
    fun getInfo() {
        println(comic.info)
    }

    @Test
    fun getImgUrl() {
        println(comic.imgUrl)
    }

    @Test
    fun getContents() {
        val contents = comic.contents
        contents.forEach {
            println("${it.name}, ${it.comicPageUrl}")
        }
    }
}