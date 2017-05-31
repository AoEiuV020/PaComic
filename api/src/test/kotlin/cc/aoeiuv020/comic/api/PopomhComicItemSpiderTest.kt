package cc.aoeiuv020.comic.api

/**
 * Created by AoEiuV020 on 17-5-31.
 */
class PopomhComicItemSpiderTest {
    lateinit var comics: List<cc.aoeiuv020.comic.api.ComicItemSpider>
    @org.junit.Before
    fun setUp() {
        val site = cc.aoeiuv020.comic.api.popomh.Popomh()
        val cls = site.classificationSpiders
        val cl = cls[2]
        comics = cl.comicList(3)
    }

    @org.junit.Test
    fun comicList() {
        comics.forEach {
            println("${it.name}, ${it.imgUrl}")
            println(it.comicDetailUrl)
        }
    }
}