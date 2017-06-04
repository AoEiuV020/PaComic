package cc.aoeiuv020.comic.api

/**
 * Created by AoEiuV020 on 17-6-3.
 */
abstract class SearchResultSpider : Spider() {
    abstract val name: String
    abstract val pageCount: Int
    abstract val comicCount: Int
    abstract val searchResultUrl: String
    abstract fun comicList(i: Int): List<ComicItemSpider>
}