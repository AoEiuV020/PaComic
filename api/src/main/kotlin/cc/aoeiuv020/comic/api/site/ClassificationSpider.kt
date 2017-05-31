package cc.aoeiuv020.comic.api.site

/**
 * Created by AoEiuV020 on 17-5-30.
 */
abstract class ClassificationSpider {
    abstract val name: String
    abstract val pagesCount: Int
    abstract fun comicList(i: Int): List<ComicItemSpider>
}
