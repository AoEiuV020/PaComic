package cc.aoeiuv020.comic.api.popomh

import org.junit.Before
import org.junit.Test

/**
 * Created by AoEiuV020 on 17-5-31.
 */
class PopomhComicItemSpiderTest {
    lateinit var comics: List<PopomhComicItemSpider>
    @Before
    fun setUp() {
        val site = Popomh()
        val cls = site.classificationSpiders
        val cl = cls[2]
        comics = cl.comicList(3)
    }

    @Test
    fun comicList() {
        comics.forEach {
            println("${it.name}, ${it.imgUrl}")
            println(it.comicDetailUrl)
        }
    }
}