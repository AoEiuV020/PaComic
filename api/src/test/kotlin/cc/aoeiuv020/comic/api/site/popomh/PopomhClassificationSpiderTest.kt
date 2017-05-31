package cc.aoeiuv020.comic.api.site.popomh

import cc.aoeiuv020.comic.api.site.ClassificationSpider
import org.junit.Before
import org.junit.Test

/**
 * Created by AoEiuV020 on 17-5-31.
 */
class PopomhClassificationSpiderTest {
    lateinit var c: ClassificationSpider
    @Before
    fun setUp() {
        val site = Popomh()
        val cs = site.classificationSpiders
        c = cs[2]
    }

    @Test
    fun pageCount() {
        val pageCount = c.pagesCount
        println(pageCount)
    }

    @Test
    fun comicList() {
        val comicList = c.comicList(3)
        comicList.forEach {
            println(it.name)
        }
    }
}