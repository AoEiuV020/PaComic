package cc.aoeiuv020.comic.api

/**
 * Created by AoEiuV020 on 17-5-28.
 */
abstract class SiteSpider : Spider() {
    abstract val name: String
    abstract val home: String
    abstract val logoUrl: String
    abstract val classificationSpiders: List<ClassificationSpider>
    abstract fun search(name: String): SearchResultSpider
}