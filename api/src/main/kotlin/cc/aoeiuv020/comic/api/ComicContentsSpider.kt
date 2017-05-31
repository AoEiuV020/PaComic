package cc.aoeiuv020.comic.api

/**
 * Created by AoEiuV020 on 17-5-31.
 */
abstract class ComicContentsSpider : Spider() {
    abstract val name: String
    abstract val comicPage: ComicPageSpider
}