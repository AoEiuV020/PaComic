package cc.aoeiuv020.comic.di

import cc.aoeiuv020.comic.api.*
import org.junit.Test

/**
 * 测试dagger,
 * Created by AoEiuV020 on 2017.09.12-17:55:42.
 */

class DaggerTest {
    @Test
    fun getSites() {
        val siteComponent: SiteComponent = DaggerSiteComponent.create()
        siteComponent.getSites()
                .forEach {
                    println(it.name)
                    println(it.baseUrl)
                    println(it.logo)
                }
    }

    @Test
    fun getGenres() {
        val genreComponent: GenreComponent = DaggerGenreComponent.builder()
                .genreModule(GenreModule(ComicSite("", "http://www.popomh.com", "")))
                .build()
        genreComponent.getGenre()
                .forEach {
                    println(it.name)
                    println(it.url)
                }
    }

    @Test
    fun getComicList() {
        val listComponent: ListComponent = DaggerListComponent.builder()
                .listModule(ListModule(ComicGenre("", "http://www.popomh.com/comic/class_8.html")))
                .build()
        listComponent.getComicList()
                .forEach {
                    println(it.name)
                    println(it.url)
                    println(it.img)
                }
    }

    @Test
    fun getComicDetail() {
        val detailComponent: DetailComponent = DaggerDetailComponent.builder()
                .detailModule(DetailModule(ComicListItem("狩猎史莱姆300年", "", "http://www.popomh.com/manhua/32551.html")))
                .build()
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
        val pageComponent: PageComponent = DaggerPageComponent.builder()
                .pageModule(PageModule(ComicIssue("", "http://www.popomh.com/popo290025/1.html?s=3")))
                .build()
        pageComponent.getComicPages()
                .forEach {
                    println(it.url)
                }
    }

    @Test
    fun getComicImage() {
        val imageComponent: ImageComponent = DaggerImageComponent.builder()
                .imageModule(ImageModule(ComicPage("http://www.popomh.com/popo290025/24.html?s=3")))
                .build()
        imageComponent.getComicImage()
                .forEach {
                    println(it.img)
                }
    }
}
