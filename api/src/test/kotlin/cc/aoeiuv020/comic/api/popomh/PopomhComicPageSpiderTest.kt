package cc.aoeiuv020.comic.api.popomh

import org.junit.Before
import org.junit.Test

/**
 * Created by AoEiuV020 on 17-5-31.
 */
class PopomhComicPageSpiderTest {
    lateinit var page: PopomhComicPageSpider
    @Before
    fun setUp() {
        val site = Popomh()
        page = PopomhComicPageSpider(site, "http://www.popomh.com/popo281379/1.html?s=7")
    }

    @Test
    fun getPagesCount() {
        println(page.pagesCount)
    }

    @Test
    fun getFirstPage() {
        println(page.firstPage)
    }

    @Test
    fun getDomains() {
        println(page.domains)
    }

    @Test
    fun getCipher() {
        println(page.cipher(8))
    }

    @Test
    fun getImgUrl() {
        println(page.imgUrl(8))
    }

    @Test
    fun pageUrl() {
        println(page.pageUrl(8))
    }

    @Test
    fun page() {
        println(page.page(8))
    }
}