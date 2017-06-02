package cc.aoeiuv020.comic.api

/**
 * Created by AoEiuV020 on 17-5-31.
 */
class PopomhComicPageSpiderTest {
    lateinit var page: cc.aoeiuv020.comic.api.popomh.PopomhComicPageSpider
    @org.junit.Before
    fun setUp() {
        val site = cc.aoeiuv020.comic.api.popomh.Popomh()
        page = cc.aoeiuv020.comic.api.popomh.PopomhComicPageSpider(site, "http://www.popomh.com/popo281379/1.html?s=7")
    }

    @org.junit.Test
    fun getPagesCount() {
        println(page.pagesCount)
    }

    @org.junit.Test
    fun getFirstPage() {
        println(page.firstPage)
    }

    @org.junit.Test
    fun getDomains() {
        println(page.domains)
    }

    @org.junit.Test
    fun getCipher() {
        println(page.cipher(8))
    }

    @org.junit.Test
    fun getImgUrl() {
        println(page.imgUrl(8))
    }

    @org.junit.Test
    fun pageUrl() {
        println(page.pageUrl(8))
    }

    @org.junit.Test
    fun page() {
        println(page.page(8))
    }
}