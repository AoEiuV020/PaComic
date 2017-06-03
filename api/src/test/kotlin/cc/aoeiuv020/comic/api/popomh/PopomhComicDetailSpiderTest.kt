package cc.aoeiuv020.comic.api.popomh

import org.junit.Before
import org.junit.Test

/**
 * Created by AoEiuV020 on 17-5-31.
 */
class PopomhComicDetailSpiderTest {
    lateinit var comic: PopomhComicDetailSpider
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