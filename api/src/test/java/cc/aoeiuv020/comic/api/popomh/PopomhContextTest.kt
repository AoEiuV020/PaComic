package cc.aoeiuv020.comic.api.popomh

import cc.aoeiuv020.comic.api.ComicIssue
import cc.aoeiuv020.comic.api.ComicListItem
import cc.aoeiuv020.comic.api.ComicListPage
import cc.aoeiuv020.comic.api.ComicPage
import org.junit.Before
import org.junit.Test

/**
 * 泡泡漫画网的测试类，
 * Created by AoEiuV020 on 2017.09.09-22:17:08.
 */
class PopomhContextTest {
    lateinit var context: PopomhContext
    @Before
    fun setUp() {
        context = PopomhContext()
    }

    @Test
    fun getGenres() {
        context.getGenres().forEach {
            println("[${it.name}](${it.url})")
        }
    }

    @Test
    fun getNextPage() {
    }

    @Test
    fun getComicList() {
        context.getComicList(ComicListPage("http://www.popomh.com/comic/class_8.html")).forEach {
            println(it.name)
            println(it.url)
            println(it.img)
        }
    }

    @Test
    fun getComicDetail() {
        context.getComicDetail(ComicListItem("狩猎史莱姆300年", "", "http://www.popomh.com/manhua/32551.html")).let {
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
        context.getComicPages(ComicIssue("", "http://www.popomh.com/popo290025/1.html?s=3")).forEach {
            println(it.url)
        }
    }

    @Test
    fun getComicImage() {
        context.getComicImage(ComicPage("http://www.popomh.com/popo290025/24.html?s=3")).let {
            println(it.img)
        }
    }
}