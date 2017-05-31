package cc.aoeiuv020.comic.api.site

/**
 * Created by AoEiuV020 on 17-5-31.
 */
abstract class ComicPageSpider : Spider() {
    abstract val pagesCount: Int
}