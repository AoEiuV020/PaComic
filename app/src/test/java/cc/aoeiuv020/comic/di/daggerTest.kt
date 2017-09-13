package cc.aoeiuv020.comic.di

import android.test.mock.MockContext
import cc.aoeiuv020.comic.api.*
import org.junit.Before
import org.junit.Test

/**
 * 测试dagger,
 * Created by AoEiuV020 on 2017.09.12-17:55:42.
 */

class DaggerTest {
    lateinit var component: AppComponent

    @Before
    fun setUp() {
        component = DaggerAppComponent.builder()
                .appModule(AppModule(MockContext()))
                .build()
    }

    @Test
    fun getSites() {
        val siteComponent: SiteComponent = component.plus(SiteModule())
        siteComponent.getSites()
                .forEach {
                    println(it.name)
                    println(it.baseUrl)
                    println(it.logo)
                }
    }

    @Test
    fun getGenres() {
        val genreComponent: GenreComponent = component.plus(GenreModule(ComicSite("", "http://www.popomh.com", "")))
        genreComponent.getGenre()
                .forEach {
                    println(it.name)
                    println(it.url)
                }
    }

    @Test
    fun getComicList() {
        val listComponent: ListComponent = component.plus(ListModule(ComicGenre("", "http://www.popomh.com/comic/class_8.html")))
        listComponent.getComicList()
                .forEach {
                    println(it.name)
                    println(it.url)
                    println(it.img)
                }
    }

    @Test
    fun getComicDetail() {
        val detailComponent: DetailComponent = component.plus(DetailModule(ComicListItem("狩猎史莱姆300年", "", "http://www.popomh.com/manhua/32551.html")))
        detailComponent.getComicDetail()
                .forEach {
                    println(it.name)
                    println(it.bigImg)
                    println(it.info)
                    it.issues.forEach { issue ->
                        println("[${issue.name}](${issue.url})")
                    }
                }
    }

    @Test
    fun getComicPages() {
        val pageComponent: PageComponent = component.plus(PageModule(ComicIssue("", "http://www.popomh.com/popo290025/1.html?s=3")))
        pageComponent.getComicPages()
                .forEach {
                    println(it.url)
                }
    }

    @Test
    fun getComicImage() {
        val imageComponent: ImageComponent = component.plus(ImageModule(ComicPage("http://www.popomh.com/popo290025/24.html?s=3")))
        imageComponent.getComicImage()
                .forEach {
                    println(it.img)
                }
    }
}
