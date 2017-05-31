package cc.aoeiuv020.comic.api

/**
 * Created by AoEiuV020 on 17-5-31.
 */
class PopomhClassificationSpiderTest {
    lateinit var c: cc.aoeiuv020.comic.api.ClassificationSpider
    @org.junit.Before
    fun setUp() {
        val site = cc.aoeiuv020.comic.api.popomh.Popomh()
        val cs = site.classificationSpiders
        c = cs[2]
    }

    @org.junit.Test
    fun pageCount() {
        val pageCount = c.pagesCount
        println(pageCount)
    }

    @org.junit.Test
    fun comicList() {
        val comicList = c.comicList(3)
        comicList.forEach {
            println(it.name)
        }
    }

    @org.junit.Test
    fun getClassificationUrl() {
        println(c.classificationUrl)
    }
}