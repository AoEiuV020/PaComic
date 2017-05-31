package cc.aoeiuv020.comic.api.site

/**
 * Created by AoEiuV020 on 17-5-30.
 */
abstract class ClassificationSpider : Spider() {
    abstract val name: String
    abstract val pagesCount: Int
    abstract val classificationUrl: String
    abstract fun comicList(i: Int): List<ComicItemSpider>
}
