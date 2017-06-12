package cc.aoeiuv020.comic.api.dm5

import org.junit.Test

/**
 * Created by AoEiuV020 on 17-6-3.
 */
class Dm5ComicContentsSpiderTest {
    @Test
    fun normal() {
        val site = Dm5()
        val comic = Dm5ComicDetailSpider(site, "http://www.dm5.com/manhua-shoubingweirenfengtie/", "兽兵卫忍风帖")
        val cc = comic.contents
        cc.forEach {
            println("${it.name}, ${it.comicPageUrl}")
        }
    }

    @Test
    fun yaojing() {
        // 这个妖精的尾巴章节太多，为此换了选择器，
        val site = Dm5()
        val comic = Dm5ComicDetailSpider(site, "http://www.dm5.com/manhua-yaojingdeweiba/", "妖精的尾巴")
        val cc = comic.contents
        cc.forEach {
            println("${it.name}, ${it.comicPageUrl}")
        }
    }

}