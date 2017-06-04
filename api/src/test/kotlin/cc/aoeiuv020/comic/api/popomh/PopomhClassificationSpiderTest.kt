package cc.aoeiuv020.comic.api.popomh

import org.junit.Test

/**
 * Created by AoEiuV020 on 17-5-31.
 */
class PopomhClassificationSpiderTest {
    lateinit var c: PopomhClassificationSpider
    @org.junit.Before
    fun setUp() {
        val site = Popomh()
        val cs = site.classificationSpiders
        c = cs[2]
    }

    @Test
    fun pageCount() {
        val pageCount = c.pageCount
        println(pageCount)
    }

    @Test
    fun comicList() {
        val comicList = c.comicList(3)
        comicList.forEach {
            println(it.name)
        }
    }

    @Test
    fun getClassificationUrl() {
        println(c.classificationUrl)
    }
}