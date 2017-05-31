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
        val cls = site.classificationSpiders
        val cl = cls[2]
        val comics = cl.comicList(3)
        val comicItem = comics[4]
        comic = comicItem.comicDetail
    }

    @Test
    fun getInfo() {
        println(comic.info)
    }

    @Test
    fun getImgUrl() {
        println(comic.imgUrl)
    }
}