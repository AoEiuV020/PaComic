package cc.aoeiuv020.comic.di

import android.content.Context
import android.content.SharedPreferences
import cc.aoeiuv020.comic.App
import cc.aoeiuv020.comic.api.ComicGenre
import cc.aoeiuv020.comic.api.ComicIssue
import cc.aoeiuv020.comic.api.ComicListItem
import cc.aoeiuv020.comic.api.ComicSite
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

/**
 * 测试dagger,
 * Created by AoEiuV020 on 2017.09.12-17:55:42.
 */

class DaggerTest {
    @Before
    fun setUp() {
        val sp = Mockito.mock(SharedPreferences::class.java)
        val spe = Mockito.mock(SharedPreferences.Editor::class.java)
        val ctx = Mockito.mock(Context::class.java)
        Mockito.`when`(ctx.getSharedPreferences(Mockito.anyString(), Mockito.anyInt())).thenReturn(sp)
        Mockito.`when`(sp.edit()).thenReturn(spe)
        Mockito.`when`(spe.putString(Mockito.anyString(), Mockito.anyString())).thenReturn(spe)
        App.setComponent(ctx)
    }

    @Test
    fun getSites() {
        val siteComponent: SiteComponent = App.component.plus(SiteModule())
        siteComponent.getSites().flatMapIterable { it }
                .forEach {
                    println(it.name)
                    println(it.baseUrl)
                    println(it.logo)
                }
    }

    @Test
    fun getGenres() {
        val genreComponent: GenreComponent = App.component.plus(GenreModule(ComicSite("", "http://www.popomh.com", "")))
        genreComponent.getGenres()
                .forEach {
                    println(it.name)
                    println(it.url)
                }
    }

    @Test
    fun getComicList() {
        val listComponent: ListComponent = App.component.plus(ListModule(ComicGenre("", "http://www.popomh.com/comic/class_8.html")))
        listComponent.getComicList()
                .flatMapIterable { it }
                .forEach {
                    println(it.name)
                    println(it.url)
                    println(it.img)
                }
    }

    @Test
    fun getComicDetail() {
        val detailComponent: DetailComponent = App.component.plus(DetailModule(ComicListItem("狩猎史莱姆300年", "", "http://www.popomh.com/manhua/32551.html")))
        detailComponent.getComicDetail()
                .forEach {
                    println(it.name)
                    it.bigImg.subscribe {
                        println(it)
                    }
                    println(it.info)
                    it.issuesAsc.forEach {
                        println("[${it.name}](${it.url})")
                    }
                }
    }

    @Test
    fun getComicPages() {
        val pageComponent: PageComponent = App.component.plus(PageModule(ComicIssue("", "http://www.popomh.com/popo290025/1.html?s=3")))
        pageComponent.getComicPages().flatMapIterable { it }
                .forEach {
                    it.img.subscribe {
                        println(it)
                    }
                }
    }
}
